package com.factory.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "prod_id")
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer prodId;

    @Column(name = "prod_name", length = 255, nullable = false)
    private String prodName;

    @Column(name = "prod_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal prodPrice;

    @Column(name = "prod_description", length = 255, nullable = true)
    private String prodDescription;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductComposition> compositions = new ArrayList<>();

    public Product() {
    }

    public void addComposition(ProductComposition composition) {
        compositions.add(composition);
        composition.setProduct(this);
    }

    public void removeComposition(ProductComposition composition) {
        compositions.remove(composition);
        composition.setProduct(null);
    }

    public Integer getProdId() {
        return this.prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return this.prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public BigDecimal getProdPrice() {
        return this.prodPrice;
    }

    public void setProdPrice(BigDecimal prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getProdDescription() {
        return this.prodDescription;
    }

    public void setProdDescription(String prodDescription) {
        this.prodDescription = prodDescription;
    }


    public List<ProductComposition> getCompositions() {
        return this.compositions;
    }

    public void setCompositions(List<ProductComposition> compositions) {
        this.compositions = compositions;
    }


}
