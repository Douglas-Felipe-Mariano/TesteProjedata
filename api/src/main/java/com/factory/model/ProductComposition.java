package com.factory.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_composition")
public class ProductComposition {

    @EmbeddedId
    private ProductCompositionId id;

    @ManyToOne
    @MapsId("prodId")
    @JoinColumn(name = "prod_id", nullable = false)
    private Product product;

    @ManyToOne
    @MapsId("matId")
    @JoinColumn(name = "mat_id", nullable = false)
    private RawMaterial rawMaterial;

    @Column(name = "quantity", precision = 10, scale = 2, nullable = false)
    private BigDecimal quantityNeeded;

     public ProductComposition() {
    }

    public ProductComposition(Product product, RawMaterial rawMaterial, BigDecimal quantityNeeded) {
        this.product = product;
        this.rawMaterial = rawMaterial;
        this.quantityNeeded = quantityNeeded;
    }

    public ProductCompositionId getId() {
        return this.id;
    }

    public void setId(ProductCompositionId id) {
        this.id = id;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public RawMaterial getRawMaterial() {
        return this.rawMaterial;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public BigDecimal getQuantityNeeded() {
        return this.quantityNeeded;
    }

    public void setQuantityNeeded(BigDecimal quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }


}
