package pl.hanysdev.largefilereader.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.Resource;
import pl.hanysdev.largefilereader.model.YearlyTemperatureDto;

public class TemperatureServiceTest {

  @InjectMocks private TemperatureService temperatureService;

  @Mock private FileService fileService;

  @Mock private Resource fileResource;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetYearlyAverageTemperature() {
    // Mocking the FileService behavior
    YearlyTemperatureDto dto1 = new YearlyTemperatureDto("Warszawa", "2020", 15.0);
    YearlyTemperatureDto dto2 = new YearlyTemperatureDto("Warszawa", "2021", 16.0);
    List<YearlyTemperatureDto> mockData = List.of(dto1, dto2);
    when(fileService.readTemperatureData(fileResource)).thenReturn(mockData);

    // Test service method
    List<YearlyTemperatureDto> result = temperatureService.getYearlyAverageTemperature("Warszawa");

    // Verifying the results
    assertEquals(2, result.size());
    assertEquals("Warszawa", result.getFirst().getCity());
  }
}
