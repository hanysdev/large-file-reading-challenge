package pl.hanysdev.largefilereader.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.hanysdev.largefilereader.exception.FileReadingException;
import pl.hanysdev.largefilereader.model.YearlyTemperatureDto;

@Slf4j
@Service
public class FileWatcherService {

  private final Resource fileResource;
  private final CsvProcessorService csvProcessorService;

  public FileWatcherService(
      @Value("${temperature.file-path}") Resource fileResource,
      CsvProcessorService csvProcessorService) {
    this.fileResource = fileResource;
    this.csvProcessorService = csvProcessorService;
  }

  @Async
  public void watchFileChanges() throws IOException {
    WatchService watchService = FileSystems.getDefault().newWatchService();
    Path path = Paths.get(fileResource.getURI());
    path.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

    while (true) {
      WatchKey key;
      try {
        key = watchService.take();
      } catch (InterruptedException e) {
        log.error("File watch service interrupted", e);
        return;
      }
      for (WatchEvent<?> event : key.pollEvents()) {
        WatchEvent.Kind<?> kind = event.kind();
        Path modifiedPath = (Path) event.context();

        if (kind == StandardWatchEventKinds.ENTRY_MODIFY
            && modifiedPath.equals(path.getFileName())) {
          log.info("File {} has been modified", path);
          processModifiedFile();
        }
      }

      boolean valid = key.reset();
      if (!valid) {
        log.warn("Watch key is no longer valid");
        break;
      }
    }
  }

  public void processModifiedFile() {
    log.info("Processing modified file: {}", fileResource);
    try {
      List<YearlyTemperatureDto> yearlyTemperatures = csvProcessorService.processCsv(fileResource);
      log.info("Processed {} temperature records", yearlyTemperatures.size());
      processModified(yearlyTemperatures);
    } catch (FileReadingException e) {
      log.error("Error processing modified file", e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error processing modified file", e);
      throw new FileReadingException("Unexpected error processing the modified file", e);
    }
  }

  private void processModified(List<YearlyTemperatureDto> yearlyTemperatures) {
    // Tutaj się trochę zakopałem :D trzeba ustalić z biznesem jak działamy z tymi zmieniającymi się
    // plikami
    log.debug("Processed line: {}", yearlyTemperatures);
  }
}
