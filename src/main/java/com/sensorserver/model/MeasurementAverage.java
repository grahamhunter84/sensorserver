package com.sensorserver.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Embeddable
public class MeasurementAverage {

  @Column
  long average;

  @Column
  long numberOfEntries;

  public MeasurementAverage() {
  }

  public long getAverage() {
    return average;
  }

  public void setAverage(long average) {
    this.average = average;
  }

  public long getNumberOfEntries() {
    return numberOfEntries;
  }

  public void setNumberOfEntries(long numberOfEntries) {
    this.numberOfEntries = numberOfEntries;
  }

  public MeasurementAverage(long average, long numberOfEntries) {
    this.average = average;
    this.numberOfEntries = numberOfEntries;
  }
}
