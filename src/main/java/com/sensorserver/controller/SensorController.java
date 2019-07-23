package com.sensorserver.controller;

import com.sensorserver.dto.MeasurementDTO;
import com.sensorserver.dto.SensorAlertsDTO;
import com.sensorserver.dto.SensorMetricsDTO;
import com.sensorserver.dto.SensorStatusDTO;
import com.sensorserver.model.Sensor;
import com.sensorserver.service.SensorAlertService;
import com.sensorserver.service.SensorMetricService;
import com.sensorserver.service.SensorService;
import java.util.HashMap;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SensorController {

  @Autowired
  private SensorService service;

  @Autowired
  private SensorAlertService alertService;

  @Autowired
  private SensorMetricService metricService;

  @GetMapping(value = "/sensor/{uuid}/")
  public SensorStatusDTO get(@PathVariable(required = true) String uuid) {
    Sensor sensor = service.get(uuid);

    if (sensor == null) {
      sensor = service.create(new Sensor(uuid));
    }
    return new SensorStatusDTO(sensor.getStatus());
  }

  @GetMapping(value = "/sensor/{uuid}/metrics")
  public SensorMetricsDTO getMetrics(@PathVariable(required = true) String uuid) {
    Sensor sensor = service.get(uuid);
    if (sensor == null) {
      service.create(new Sensor(uuid));
    }
    return metricService.get(uuid);
  }

  @GetMapping(value = "/sensor/{uuid}/alerts")
  public HashMap<String, Object> getAlerts(@PathVariable(required = true) String uuid) {
    Sensor sensor = service.get(uuid);
    if (sensor == null) {
      service.create(new Sensor(uuid));
    }

    SensorAlertsDTO alerts = alertService.get(uuid);

    HashMap<String, Object> result = new HashMap<>();
    result.put("startTime", alerts.getStartTime());
    result.put("endTime", alerts.getEndTime());

    for (int i = 0; i < alerts.getAlerts().size(); i++) {
      result.put("measurement" + (i+1), alerts.getAlerts().get(i));
    }
    return result;
  }

  @PostMapping(value = "/sensor/{uuid}/measurements")
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@Valid @RequestBody MeasurementDTO measurementDTO,
      @PathVariable(required = true) String uuid) {
    Sensor sensor = service.get(uuid);
    if (sensor == null) {
      service.create(new Sensor(uuid));
    }

    alertService.add(uuid, measurementDTO);
    metricService.add(uuid, measurementDTO);
  }
}
