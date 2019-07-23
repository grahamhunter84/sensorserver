package com.sensorserver.service;

import static com.sensorserver.constants.SensorConstants.WARNING_SENSOR_RANGE;
import static com.sensorserver.constants.SensorStatus.ALERT;
import static com.sensorserver.constants.SensorStatus.OK;
import static com.sensorserver.constants.SensorStatus.WARN;

import com.sensorserver.constants.SensorConstants;
import com.sensorserver.constants.SensorStatus;
import com.sensorserver.dto.MeasurementDTO;
import com.sensorserver.dto.SensorAlertsDTO;
import com.sensorserver.model.Alert;
import com.sensorserver.model.Sensor;
import com.sensorserver.repository.SensorRepository;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorAlertService {

  @Autowired
  private SensorRepository repository;

  @Transactional
  public SensorAlertsDTO get(String uuid) {
    Sensor sensor = repository.findOneByUuid(uuid);

    List<Alert> highAlerts = sensor.getAlerts().stream()
        .filter(alert -> alert.getCo2() > WARNING_SENSOR_RANGE).collect(
            Collectors.toList());

    Optional<Alert> startAlert = highAlerts.stream().findFirst();
    Alert lastAlert = highAlerts.get(highAlerts.size() - 1);

    return new SensorAlertsDTO(
        startAlert.get().getTime(),
        lastAlert.getTime(),
        highAlerts.stream().map(Alert::getCo2).collect(
            Collectors.toList()));
  }

  @Transactional
  public void add(String uuid, MeasurementDTO measurementDTO) {
    Sensor sensor = repository.findOneByUuid(uuid);

    handleAlertAndStatus(measurementDTO, sensor);

    repository.save(sensor);
  }


  private static void handleAlertAndStatus(MeasurementDTO measurementDTO, Sensor sensor) {
    SensorStatus sensorStatus =
        determineSensorStatus(sensor, measurementDTO.getCo2());

    List<Alert> alerts = sensor.getAlerts();
    switch (sensorStatus) {
      case OK:
        //Its an OK measurement so dont store alert
        alerts.clear();
        break;
      case WARN:
      case ALERT:
        //WARNING or ALERT so store the measurement
        Alert lastAlert = !alerts.isEmpty() ? alerts.get(alerts.size() - 1) : null;
        alerts.add(createNextAlert(lastAlert, measurementDTO.getCo2(), measurementDTO.getTime()));
        break;
      default:
        break;
    }
    sensor.setStatus(sensorStatus);
  }


  private static SensorStatus determineSensorStatus(Sensor sensor, Long co2) {
    List<Alert> alerts = sensor.getAlerts();

    //Is in alert range
    if (co2 > WARNING_SENSOR_RANGE) {
      //We minus one because we count the current one as well
      if (alerts.size() >= SensorConstants.NUMBER_OF_CONSECUTIVE_ALERTS_ALLOWED - 1) {

        //Do we have more than three consecutive?
        if (!alerts.isEmpty()) {
          Alert lastAlert = alerts.get(alerts.size() - 1);
          if (lastAlert.getPreviousCo2() != null && lastAlert.getCo2() > WARNING_SENSOR_RANGE
              && lastAlert.getPreviousCo2() > WARNING_SENSOR_RANGE) {
            return ALERT;
          }
        }
      } else {
        //Not more than 3 return warning
        return WARN;
      }
    } else {
      //Its out of harmful range

      //More than 3 out of harmful range?
      if (!alerts.isEmpty()) {
        Alert lastAlert = alerts.get(alerts.size() - 1);
        if (lastAlert.getCo2() < WARNING_SENSOR_RANGE
            && lastAlert.getPreviousCo2() < WARNING_SENSOR_RANGE) {
          return OK;
        }
      }
    }

    //Status stays the same
    return sensor.getStatus();
  }


  private static Alert createNextAlert(Alert alert, long co2, ZonedDateTime time) {
    return new Alert(co2, alert == null ? null : alert.getCo2(), time);
  }


}
