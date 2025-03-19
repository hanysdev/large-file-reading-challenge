package pl.hanysdev.largefilereader.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.Resource;
import pl.hanysdev.largefilereader.exception.FileReadingException;
import pl.hanysdev.largefilereader.model.YearlyTemperatureDto;

public class CsvProcessorServiceTest {

  @InjectMocks private CsvProcessorService csvProcessorService;

  @Mock private Resource fileResource;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testProcessCsv() throws Exception {
    // Setup mock data
    String csvData = "City;Date;Temperature\nWarszawa;2020-01-01;12.5\nWarszawa;2020-01-02;13.0";
    when(fileResource.getInputStream()).thenReturn(new ByteArrayInputStream(csvData.getBytes()));

    // Test processing CSV
    List<YearlyTemperatureDto> result = csvProcessorService.processCsv(fileResource);

    // Verify results
    assertEquals(1, result.size());
    YearlyTemperatureDto dto = result.getFirst();
    assertEquals("Warszawa", dto.getCity());
    assertEquals("2020", dto.getYear());
    assertEquals(12.75, dto.getAverageTemperature());
  }

  @Test
  public void testProcessCsvWithInvalidData() throws Exception {
    String invalidCsvData = "City;Date\nWarszawa;2020-01-01";
    when(fileResource.getInputStream())
        .thenReturn(new ByteArrayInputStream(invalidCsvData.getBytes()));

    try {
      csvProcessorService.processCsv(fileResource);
      fail("Expected FileReadingException not thrown");
    } catch (FileReadingException e) {
      assertTrue(e.getMessage().contains("Error reading CSV file"));
    }
  }
}
