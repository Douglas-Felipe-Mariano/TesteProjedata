package com.factory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.factory.model.ProductComposition;
import com.factory.model.ProductCompositionId;

@Repository
public interface ProductCompositionRepository extends JpaRepository<ProductComposition, ProductCompositionId> {

}
