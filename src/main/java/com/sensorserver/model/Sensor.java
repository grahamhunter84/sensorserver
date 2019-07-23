package com.sensorserver.model;

import com.sensorserver.constants.SensorStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Sensor implements Serializable {

  @Id
  @Column
  String uuid;

  @ElementCollection
  List<Alert> alerts = new ArrayList<>();

  @Column
  SensorStatus status = SensorStatus.OK;

  @Embedded
  Measurement currentMax;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride( name = "co2", column = @Column(name = "next_co2")),
      @AttributeOverride( name = "time", column = @Column(name = "next_time")),
  })
  Measurement nextDayMax;

  @Embedded
  MeasurementAverage currentAverage;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride( name = "average", column = @Column(name = "next_average")),
      @AttributeOverride( name = "numberOfEntries", column = @Column(name = "next_numberOfEntries")),
  })
  MeasurementAverage nextDayAverage;


  public Sensor() {
  }

  public Sensor(String uuid) {
    this.uuid = uuid;
  }

  public MeasurementAverage getCurrentAverage() {
    return currentAverage;
  }

  public void setCurrentAverage(MeasurementAverage currentAverage) {
    this.currentAverage = currentAverage;
  }

  public MeasurementAverage getNextDayAverage() {
    return nextDayAverage;
  }

  public void setNextDayAverage(MeasurementAverage nextDayAverage) {
    this.nextDayAverage = nextDayAverage;
  }

  public void setCurrentMax(Measurement currentMax) {
    this.currentMax = currentMax;
  }

  public Measurement getCurrentMax() {
    return currentMax;
  }

  public Measurement getNextDayMax() {
    return nextDayMax;
  }

  public void setNextDayMax(Measurement nextDayMax) {
    this.nextDayMax = nextDayMax;
  }


  public List<Alert> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<Alert> alerts) {
    this.alerts = alerts;
  }

  public SensorStatus getStatus() {
    return status;
  }

  public void setStatus(SensorStatus status) {
    this.status = status;
  }
}
