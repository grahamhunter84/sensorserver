package com.sensorserver.repository;

import com.sensorserver.model.Sensor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends CrudRepository<Sensor, Long> {

  Sensor findOneByUuid(String uuid);
}
