package com.sensorserver.model;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Embeddable
public class Alert {

  @Column
  long co2;

  @Column
  Long previousCo2;

  @Column
  ZonedDateTime time;

  public ZonedDateTime getTime() {
    return time;
  }

  public Long getCo2() {
    return co2;
  }

  public Long getPreviousCo2() {
    return previousCo2;
  }

  public Alert() {
  }

  public Alert(long co2, Long previousCo2, ZonedDateTime time) {
    this.co2 = co2;
    this.previousCo2 = previousCo2;
    this.time = time;
  }
}
