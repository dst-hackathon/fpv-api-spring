package com.dst.hackathon.fpv.data.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by tisana on 24/10/2559.
 */

@Entity
public class Phone {
    @Id
    private int id;

    //Relational
    @OneToOne(fetch = FetchType.LAZY)
    private Employee employee; //Employee who assigned to the phone extension

    @OneToOne(fetch = FetchType.LAZY)
    private Desk desk;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
