package com.factory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.factory.model.Product;
import com.factory.model.ProductComposition;
import com.factory.model.ProductCompositionId;

@Repository
public interface ProductCompositionRepository extends JpaRepository<ProductComposition, ProductCompositionId> {
    
    @Query("SELECT pc FROM ProductComposition pc WHERE pc.product.prodId = :prodId")
    List<ProductComposition> findByProdId(@Param("prodId") Integer prodtId);


    @Query("SELECT pc FROM ProductComposition pc " +
           "JOIN FETCH pc.rawMaterial rm " +
           "JOIN FETCH rm.matUnit " +
           "WHERE pc.product IN :products")
    List<ProductComposition> findByProductInWithMaterial(@Param("products") List<Product> products);

    @Query("""
        SELECT pc
        FROM ProductComposition pc
        JOIN FETCH pc.product
        JOIN FETCH pc.rawMaterial
    """)
    List<ProductComposition> findAllWithProductAndMaterial();
}
