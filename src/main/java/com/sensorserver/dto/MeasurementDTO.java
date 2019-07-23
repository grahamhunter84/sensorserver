package com.sensorserver.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.NotNull;

public class MeasurementDTO implements Serializable {

  @NotNull
  long co2;

  @NotNull
  ZonedDateTime time;

  public Long getCo2() {
    return co2;
  }

  public void setCo2(Long co2) {
    this.co2 = co2;
  }

  public ZonedDateTime getTime() {
    return time;
  }

  public void setTime(ZonedDateTime time) {
    this.time = time;
  }
}
