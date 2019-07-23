package com.sensorserver.dto;

import com.sensorserver.constants.SensorStatus;
import java.io.Serializable;

public class SensorStatusDTO implements Serializable {

  SensorStatus sensorStatus;

  public SensorStatusDTO(SensorStatus status) {
    this.sensorStatus = status;
  }


  public SensorStatus getSensorStatus() {
    return sensorStatus;
  }

  public void setSensorStatus(SensorStatus sensorStatus) {
    this.sensorStatus = sensorStatus;
  }
}
