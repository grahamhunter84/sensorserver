package com.sensorserver.model;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Embeddable
public class Measurement {

  @Column
  Long co2;

  @Column
  ZonedDateTime time;

  public ZonedDateTime getTime() {
    return time;
  }

  public void setTime(ZonedDateTime time) {
    this.time = time;
  }

  public Long getCo2() {
    return co2;
  }

  public void setCo2(Long co2) {
    this.co2 = co2;
  }

  public Measurement() {
  }

  public Measurement(Long co2, ZonedDateTime time) {
    this.co2 = co2;
    this.time = time;
  }
}
