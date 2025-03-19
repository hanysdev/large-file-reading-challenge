package pl.hanysdev.largefilereader.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class YearlyTemperatureDto {
  @JsonIgnore private String city;
  private String year;
  private Double averageTemperature;
}
