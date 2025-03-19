package pl.hanysdev.largefilereader.controller;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.hanysdev.largefilereader.exception.CityNotFoundException;
import pl.hanysdev.largefilereader.model.YearlyTemperatureDto;
import pl.hanysdev.largefilereader.service.TemperatureService;

@Slf4j
@RestController
@RequestMapping("/api/v1/temperature/average")
public class TemperatureController {

  private final TemperatureService temperatureService;

  public TemperatureController(TemperatureService temperatureService) {
    this.temperatureService = temperatureService;
  }

  @GetMapping("/{city}")
  public List<YearlyTemperatureDto> getYearlyTemperature(@PathVariable String city) {
    log.info("API Request: GET /temperature/average/{}", city);
    List<YearlyTemperatureDto> result =
        Optional.of(temperatureService.getYearlyAverageTemperature(city))
            .filter(list -> !list.isEmpty())
            .orElseThrow(
                () -> {
                  log.info("City '{}' not found, throwing CityNotFoundException", city);
                  return new CityNotFoundException(city);
                });

    log.info("Returning {} temperature records for city: {}", result.size(), city);
    return result;
  }
}
