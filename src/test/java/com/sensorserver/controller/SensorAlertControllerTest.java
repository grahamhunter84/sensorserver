package com.sensorserver.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sensorserver.dto.MeasurementDTO;
import com.sensorserver.repository.SensorRepository;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SensorAlertControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private SensorRepository repository;

  @Autowired
  private ObjectMapper objectMapper;

  @Before
  public void setup() {

  }

  @After
  public void tearDown() {
    repository.deleteAll();
  }

  @Test
  public void testThatOneAlertCausesWARN() throws Exception {

    MeasurementDTO dto = new MeasurementDTO();
    dto.setCo2(3000L);
    dto.setTime(ZonedDateTime.now());

    //No Alert
    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("OK"));

    //One alert
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("WARN"));
  }


  @Test
  public void testThatStatusWontCanChangeFromNonConsecutiveThreeAlerts() throws Exception {

    MeasurementDTO dto = new MeasurementDTO();
    dto.setCo2(3000L);
    dto.setTime(ZonedDateTime.now());

    //No Alert
    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("OK"));

    //One alert
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("WARN"));

    //Two alerts
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("WARN"));

    dto = new MeasurementDTO();
    dto.setCo2(1900L);
    dto.setTime(ZonedDateTime.now());

    //Three alerts
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("WARN"));

    dto = new MeasurementDTO();
    dto.setCo2(3000L);
    dto.setTime(ZonedDateTime.now());

    //Three alerts
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("WARN"));

  }


  @Test
  public void testThatStatusCanChangeFromThreeAlerts() throws Exception {

    MeasurementDTO dto = new MeasurementDTO();
    dto.setCo2(3000L);
    dto.setTime(ZonedDateTime.now());

    //No Alert
    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("OK"));

    //One alert
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("WARN"));

    //Two alerts
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("WARN"));

    //Three alerts
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("ALERT"));

  }

  @Test
  public void testThatStatusWontChangeFromNonConsecutiveThreeOKs() throws Exception {

    MeasurementDTO dto = new MeasurementDTO();
    dto.setCo2(3000L);
    dto.setTime(ZonedDateTime.now());

    //One alert
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    //Two alerts
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    //Three alerts
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("ALERT"));

    dto.setCo2(1999L);
    //1
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("ALERT"));

    //2
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("ALERT"));

    dto.setCo2(3000L);
    //3
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("ALERT"));

    dto.setCo2(1000L);
    //3
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("ALERT"));
  }

  @Test
  public void testThatStatusCanChangeFromThreeOKs() throws Exception {

    MeasurementDTO dto = new MeasurementDTO();
    dto.setCo2(3000L);
    dto.setTime(ZonedDateTime.now());

    //One alert
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    //Two alerts
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    //Three alerts
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("ALERT"));

    dto.setCo2(1999L);
    //1
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("ALERT"));

    //2
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("ALERT"));

    //3
    mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
        .contentType("application/json"))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/sensor/1/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sensorStatus").value("OK"));
  }

  @Test
  public void testThatAlertsAreReturned() throws Exception {

    MeasurementDTO dto = new MeasurementDTO();
    dto.setCo2(3000L);
    ZonedDateTime startTime = ZonedDateTime.now();
    dto.setTime(startTime);
    //6 Alerts
    ZonedDateTime endTime = null;
    for (int i = 0; i < 6; i++) {

      mockMvc.perform(post("/sensor/1/measurements").content(objectMapper.writeValueAsString(dto))
          .contentType("application/json"))
          .andExpect(status().isCreated());

      if (i != 5) {
        endTime = ZonedDateTime.now();
      }
      dto.setCo2(dto.getCo2()+1000);
      dto.setTime(endTime);
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    String startTimeExpected = startTime.format(formatter);
    mockMvc.perform(get("/sensor/1/alerts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.startTime").value(startTimeExpected))
        .andExpect(jsonPath("$.endTime").value(endTime.format(formatter)))
        .andExpect(jsonPath("$.measurement1").value("3000"))
        .andExpect(jsonPath("$.measurement2").value("4000"))
        .andExpect(jsonPath("$.measurement3").value("5000"))
        .andExpect(jsonPath("$.measurement4").value("6000"))
        .andExpect(jsonPath("$.measurement5").value("7000"))
        .andExpect(jsonPath("$.measurement6").value("8000"));


  }
}
