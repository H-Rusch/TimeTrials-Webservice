package com.hrusch.webapp.repository;

import com.hrusch.webapp.model.entity.TimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRepository extends JpaRepository<TimeEntity, Long>  {
}
