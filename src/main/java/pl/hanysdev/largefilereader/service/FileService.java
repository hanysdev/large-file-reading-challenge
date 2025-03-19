package pl.hanysdev.largefilereader.service;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pl.hanysdev.largefilereader.exception.FileReadingException;
import pl.hanysdev.largefilereader.model.YearlyTemperatureDto;

@Slf4j
@Service
public class FileService {

  private final CsvProcessorService csvProcessorService;

  public FileService(CsvProcessorService csvProcessorService) {
    this.csvProcessorService = csvProcessorService;
  }

  public List<YearlyTemperatureDto> readTemperatureData(Resource fileResource) {
    try {
      return csvProcessorService.processCsv(fileResource);
    } catch (IOException e) {
      log.error("Error reading file: {}", fileResource.getFilename(), e);
      throw new FileReadingException("Error reading the file: " + fileResource.getFilename(), e);
    }
  }
}
