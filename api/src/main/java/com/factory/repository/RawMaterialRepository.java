package com.factory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.factory.model.RawMaterial;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, Integer> {

}
