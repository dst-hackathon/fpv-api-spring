package com.dst.hackathon.fpv.data.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by tisana on 24/10/2559.
 */
@Entity
public class Desk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, nullable = false)
    private String code;

    //Position of Desk
    private BigDecimal posX;
    private BigDecimal posY;

    private BigDecimal width;
    private BigDecimal heigth;

    //Relational
    @OneToOne(fetch = FetchType.LAZY)
    private Employee employee; //assigned person who sit on this desk

    @OneToOne(fetch = FetchType.LAZY)
    private Desk desk; //phone assigned to desk;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPosX() {
        return posX;
    }

    public void setPosX(BigDecimal posX) {
        this.posX = posX;
    }

    public BigDecimal getPosY() {
        return posY;
    }

    public void setPosY(BigDecimal posY) {
        this.posY = posY;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeigth() {
        return heigth;
    }

    public void setHeigth(BigDecimal heigth) {
        this.heigth = heigth;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Desk getDesk() {
        return desk;
    }

    public void setDesk(Desk desk) {
        this.desk = desk;
    }
}
