package com.factory.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "raw_material")
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mat_id")
    private Integer matId;

    @Column(name = "mat_name", length = 100, nullable = false)
    private String matName;

    @Column(name = "mat_quantity", precision = 10, scale = 2, nullable = false)
    private BigDecimal matQuantity;

    @JoinColumn(name = "unit_id", nullable = false)
    @ManyToOne
    private UnitMeasure matUnit;


    public RawMaterial() {
    }


    public Integer getMatId() {
        return this.matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    public String getMatName() {
        return this.matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public BigDecimal getMatQuantity() {
        return this.matQuantity;
    }

    public void setMatQuantity(BigDecimal matQuantity) {
        this.matQuantity = matQuantity;
    }


    public UnitMeasure getMatUnit() {
        return this.matUnit;
    }

    public void setMatUnit(UnitMeasure matUnit) {
        this.matUnit = matUnit;
    }


}
