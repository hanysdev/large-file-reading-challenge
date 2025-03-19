package pl.hanysdev.largefilereader.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TemperatureServiceTestConfig.class)
public class TemperatureControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  public void shouldReturnAverageTemperatureForExistingCity() throws Exception {
    String city = "Warszawa";

    mockMvc
        .perform(
            get("/api/v1/temperature/average/{city}", city).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].averageTemperature").exists());
  }

  @Test
  public void shouldReturnNotFoundForNonExistingCity() throws Exception {
    String city = "NonExistentCity";

    mockMvc
        .perform(
            get("/api/v1/temperature/average/{city}", city).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("City '" + city + "' not found"));
  }

  @Test
  public void shouldReturnInternalServerErrorForFileReadingException() throws Exception {
    String city = "WrongCity";

    mockMvc
        .perform(
            get("/api/v1/temperature/average/{city}", city).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError());
  }
}
