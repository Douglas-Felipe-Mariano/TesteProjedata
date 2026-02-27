package com.factory.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ProductCompositionId implements Serializable {

    private Integer prodId;

    private Integer matId;

    public ProductCompositionId() {
    }


    public ProductCompositionId(Integer prodId, Integer matId) {
        this.prodId = prodId;
        this.matId = matId;
    }

    public Integer getProdId() {
        return this.prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public Integer getMatId() {
        return this.matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProductCompositionId)) {
            return false;
        }
        ProductCompositionId productCompositionId = (ProductCompositionId) o;
        return prodId.equals(productCompositionId.prodId) && matId.equals(productCompositionId.matId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prodId, matId);
    }

}
