package com.factory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "unit_of_measure")
public class UnitMeasure {

    @Column(name = "unit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer unitId;

    @Column(name = "unit_name", length = 50, nullable = false)
    private String unitName;

    @Column(name = "unit_symbol", length = 10, nullable = false)
    private String unitSymbol;

    public UnitMeasure() {
    }

    public Integer getUnitId() {
        return this.unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitSymbol() {
        return this.unitSymbol;
    }

    public void setUnitSymbol(String unitSymbol) {
        this.unitSymbol = unitSymbol;
    }


}
