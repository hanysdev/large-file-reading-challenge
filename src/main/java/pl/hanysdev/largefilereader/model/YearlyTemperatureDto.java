package pl.hanysdev.largefilereader.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class YearlyTemperatureDto {
  @JsonIgnore private String city;
  private String year;
  @Setter private Double averageTemperature;
}
