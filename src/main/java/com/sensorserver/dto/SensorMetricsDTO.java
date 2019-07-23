package com.sensorserver.dto;

import java.io.Serializable;

public class SensorMetricsDTO implements Serializable {

  long maxLast30Days;
  long avgLast30Days;

  public SensorMetricsDTO(long maxLast30Days, long avgLast30Days) {
    this.maxLast30Days = maxLast30Days;
    this.avgLast30Days = avgLast30Days;
  }


  public long getMaxLast30Days() {
    return maxLast30Days;
  }

  public void setMaxLast30Days(long maxLast30Days) {
    this.maxLast30Days = maxLast30Days;
  }

  public long getAvgLast30Days() {
    return avgLast30Days;
  }

  public void setAvgLast30Days(long avgLast30Days) {
    this.avgLast30Days = avgLast30Days;
  }
}
