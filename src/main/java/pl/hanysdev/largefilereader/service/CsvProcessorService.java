package pl.hanysdev.largefilereader.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pl.hanysdev.largefilereader.exception.FileReadingException;
import pl.hanysdev.largefilereader.model.YearlyTemperatureDto;

@Slf4j
@Service
public class CsvProcessorService {

  public List<YearlyTemperatureDto> processCsv(Resource fileResource) throws IOException {
    List<YearlyTemperatureDto> yearlyTemperatures = new ArrayList<>();

    try (BufferedReader reader =
            new BufferedReader(
                new InputStreamReader(fileResource.getInputStream(), StandardCharsets.UTF_8));
        CSVParser csvParser =
            CSVParser.parse(
                reader, CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

      Map<String, Map<String, List<Double>>> cityYearTemperatureMap = new HashMap<>();

      for (CSVRecord record : csvParser) {
        String city = record.get(0); // City
        String date = record.get(1); // Date
        String year = date.substring(0, 4); // Year
        Double temperature = Double.parseDouble(record.get(2)); // Temp

        cityYearTemperatureMap
            .computeIfAbsent(city, k -> new HashMap<>())
            .computeIfAbsent(year, k -> new ArrayList<>())
            .add(temperature);
      }

      for (Map.Entry<String, Map<String, List<Double>>> cityEntry :
          cityYearTemperatureMap.entrySet()) {
        String city = cityEntry.getKey();

        for (Map.Entry<String, List<Double>> yearEntry : cityEntry.getValue().entrySet()) {
          String year = yearEntry.getKey();
          List<Double> temperatures = yearEntry.getValue();

          Double avgTemperature =
              temperatures.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

          yearlyTemperatures.add(new YearlyTemperatureDto(city, year, avgTemperature));
        }
      }
    } catch (IOException | ArrayIndexOutOfBoundsException e) {
      log.error("Error reading CSV file: {}", fileResource.getFilename(), e);
      throw new FileReadingException("Error reading CSV file", e);
    }
    return yearlyTemperatures;
  }
}
