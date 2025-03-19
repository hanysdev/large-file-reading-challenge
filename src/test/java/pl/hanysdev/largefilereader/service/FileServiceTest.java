package pl.hanysdev.largefilereader.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.Resource;
import pl.hanysdev.largefilereader.model.YearlyTemperatureDto;

public class FileServiceTest {

  @InjectMocks private FileService fileService;

  @Mock private CsvProcessorService csvProcessorService;

  @Mock private Resource fileResource;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testReadTemperatureData() throws Exception {
    // Mocking the CsvProcessorService behavior
    YearlyTemperatureDto dto = new YearlyTemperatureDto("Warszawa", "2020", 15.0);
    when(csvProcessorService.processCsv(fileResource)).thenReturn(List.of(dto));

    // Test service method
    List<YearlyTemperatureDto> result = fileService.readTemperatureData(fileResource);

    // Verifying the results
    assertEquals(1, result.size());
    assertEquals("Warszawa", result.getFirst().getCity());
    assertEquals(15.0, result.getFirst().getAverageTemperature());
  }
}
