package com.sensorserver.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensorserver.dto.MeasurementDTO;
import com.sensorserver.repository.SensorRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SensorMetricsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private SensorRepository repository;

  @Autowired
  private ObjectMapper objectMapper;

  @After
  public void after() {
    repository.deleteAll();
  }

  @Test
  public void testMetricsAreReturned() throws Exception {

    MeasurementDTO dto = new MeasurementDTO();
    dto.setCo2(2000L);
    dto.setTime(ZonedDateTime.now());
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/metrics"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.maxLast30Days").value("2000"))
        .andExpect(jsonPath("$.avgLast30Days").value("2000"));
  }


  @Test
  public void testMetricsAreCorrectForTimeRange() throws Exception {

    //Include this one
    MeasurementDTO dto = new MeasurementDTO();
    dto.setCo2(2000L);
    dto.setTime(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("UTC")));
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    //Include this one
    dto = new MeasurementDTO();
    dto.setCo2(5000L);
    dto.setTime(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("UTC")).minusDays(30));
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    //Include this one
    dto = new MeasurementDTO();
    dto.setCo2(7000L);
    dto.setTime(ZonedDateTime.now().minusDays(29).withZoneSameInstant(ZoneId.of("UTC")));
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    //Excluded this one
    dto = new MeasurementDTO();
    dto.setCo2(8000L);
    dto.setTime(ZonedDateTime.now().minusDays(31).withZoneSameInstant(ZoneId.of("UTC")));
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/metrics"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.maxLast30Days").value("7000"))
        .andExpect(jsonPath("$.avgLast30Days").value("4666"));
  }
}
