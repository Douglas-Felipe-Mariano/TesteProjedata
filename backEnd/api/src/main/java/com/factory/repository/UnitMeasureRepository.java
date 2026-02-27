package com.factory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.factory.model.UnitMeasure;

@Repository
public interface UnitMeasureRepository extends JpaRepository<UnitMeasure, Integer>{

}
