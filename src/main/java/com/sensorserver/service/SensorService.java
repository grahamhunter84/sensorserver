package com.sensorserver.service;

import com.sensorserver.model.Sensor;
import com.sensorserver.repository.SensorRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorService {

  @Autowired
  private SensorRepository repository;

  @Transactional
  public Sensor get(String uuid) {
    return repository.findOneByUuid(uuid);
  }

  @Transactional
  public Sensor create(Sensor sensor) {
    return repository.save(sensor);
  }
}
