package pl.hanysdev.largefilereader.service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pl.hanysdev.largefilereader.model.YearlyTemperatureDto;

@Slf4j
@Service
public class TemperatureService {

  private final FileService fileService;
  private final Resource fileResource;
  private final FileWatcherService fileWatcherService;

  public TemperatureService(
      FileService fileService,
      @Value("${temperature.file-path}") Resource fileResource,
      FileWatcherService fileWatcherService) {
    this.fileService = fileService;
    this.fileResource = fileResource;
    this.fileWatcherService = fileWatcherService;
  }

  public List<YearlyTemperatureDto> getYearlyAverageTemperature(String city) {
    List<YearlyTemperatureDto> allYearlyTemperatures =
        fileService.readTemperatureData(fileResource);

    return allYearlyTemperatures.stream()
        .filter(dto -> dto.getCity().equalsIgnoreCase(city))
        .collect(Collectors.toList());
  }

  @PostConstruct
  public void startFileWatcher() throws IOException {
    log.info("Starting file watcher...");
    fileWatcherService.watchFileChanges();
  }
}
