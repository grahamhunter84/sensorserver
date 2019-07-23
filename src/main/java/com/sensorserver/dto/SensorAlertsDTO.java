package com.sensorserver.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class SensorAlertsDTO implements Serializable {

  ZonedDateTime startTime;
  ZonedDateTime endTime;

  List<Long> alerts;


  public SensorAlertsDTO(ZonedDateTime startTime, ZonedDateTime endTime,
      List<Long> alerts) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.alerts = alerts;
  }


  public ZonedDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(ZonedDateTime startTime) {
    this.startTime = startTime;
  }

  public ZonedDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(ZonedDateTime endTime) {
    this.endTime = endTime;
  }

  public List<Long> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<Long> alerts) {
    this.alerts = alerts;
  }
}
