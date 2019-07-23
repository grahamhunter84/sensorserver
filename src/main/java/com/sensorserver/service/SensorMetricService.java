package com.sensorserver.service;

import com.sensorserver.constants.SensorConstants;
import com.sensorserver.dto.MeasurementDTO;
import com.sensorserver.dto.SensorMetricsDTO;
import com.sensorserver.model.Measurement;
import com.sensorserver.model.MeasurementAverage;
import com.sensorserver.model.Sensor;
import com.sensorserver.repository.SensorRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorMetricService {

  @Autowired
  private SensorRepository repository;

  @Transactional
  public SensorMetricsDTO get(String uuid) {
    Sensor sensor = repository.findOneByUuid(uuid);

    return new SensorMetricsDTO(
        sensor.getCurrentMax().getCo2(),
        sensor.getCurrentAverage().getAverage());
  }

  @Transactional
  public void add(String uuid, MeasurementDTO measurementDTO) {
    Sensor sensor = repository.findOneByUuid(uuid);

    LocalDate startDateLimit = LocalDateTime.now()
        .minusDays(SensorConstants.NUMBER_OF_DAYS_TO_RECORD).toLocalDate();

    //If the measurement is older than 30 days then dont do anything with it.
    if (measurementDTO.getTime().toLocalDate().isBefore(startDateLimit)) {
      return;
    }

    //Will this measurement not expire the next day?
    if (!measurementDTO.getTime().toLocalDate().isEqual(startDateLimit)) {
      handleNextDayValues(measurementDTO, sensor);

    }
    //What are the values today?
    handleCurrentValues(measurementDTO, sensor);

    repository.save(sensor);

  }

  private void handleCurrentValues(MeasurementDTO measurementDTO, Sensor sensor) {
    //Is this measurement the new current max?
    if (sensor.getCurrentMax() == null || sensor.getCurrentMax().getCo2() < measurementDTO
        .getCo2()) {
      sensor.setCurrentMax(new Measurement(measurementDTO.getCo2(), measurementDTO.getTime()));
    }

    //Work out the new current average
    if (sensor.getCurrentAverage() == null) {
      sensor.setCurrentAverage(new MeasurementAverage(measurementDTO.getCo2(), 1));
    } else {
      MeasurementAverage currentAverage = sensor.getCurrentAverage();

      if (currentAverage == null) {
        sensor.setCurrentAverage(new MeasurementAverage(measurementDTO.getCo2(), 1));
      } else {
        currentAverage.setAverage(calculateNewAverage(measurementDTO, currentAverage));
        currentAverage.setNumberOfEntries(currentAverage.getNumberOfEntries() + 1);
      }
    }
  }

  private void handleNextDayValues(MeasurementDTO measurementDTO, Sensor sensor) {
    //Calculate next day max
    if (sensor.getNextDayMax() == null || sensor.getNextDayMax().getCo2() < measurementDTO
        .getCo2()) {
      sensor.setNextDayMax(new Measurement(measurementDTO.getCo2(), measurementDTO.getTime()));
    }

    //Calculate next day average
    MeasurementAverage nextDayAverage = sensor.getNextDayAverage();
    if (nextDayAverage == null) {
      sensor.setNextDayAverage(new MeasurementAverage(measurementDTO.getCo2(), 1));
    } else {
      nextDayAverage.setAverage(calculateNewAverage(measurementDTO, nextDayAverage));
      nextDayAverage.setNumberOfEntries(nextDayAverage.getNumberOfEntries() + 1);
    }
  }

  private long calculateNewAverage(MeasurementDTO measurementDTO,
      MeasurementAverage currentAverage) {
    long newAverage = currentAverage.getAverage() + (
        (measurementDTO.getCo2() - currentAverage.getAverage()) / (currentAverage
            .getNumberOfEntries() + 1));

    return newAverage;
  }
}
