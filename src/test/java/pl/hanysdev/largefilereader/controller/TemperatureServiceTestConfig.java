package pl.hanysdev.largefilereader.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pl.hanysdev.largefilereader.exception.FileReadingException;
import pl.hanysdev.largefilereader.model.YearlyTemperatureDto;
import pl.hanysdev.largefilereader.service.TemperatureService;

@TestConfiguration
public class TemperatureServiceTestConfig {

  @Bean
  public TemperatureService temperatureService() {
    TemperatureService mockService = mock(TemperatureService.class);

    // Mock for shouldReturnAverageTemperatureForExistingCity()
    when(mockService.getYearlyAverageTemperature("Warszawa"))
        .thenReturn(List.of(new YearlyTemperatureDto("Warszawa", "2021", 15.5)));

    // Mock for shouldReturnNotFoundForNonExistingCity()
    when(mockService.getYearlyAverageTemperature("NonExistentCity")).thenReturn(List.of());

    // Mock for shouldReturnInternalServerErrorForFileReadingException()
    when(mockService.getYearlyAverageTemperature("WrongCity"))
        .thenThrow(
            new FileReadingException(
                "Error reading the temperature data file",
                new RuntimeException("File is corrupted")));
    return mockService;
  }
}
