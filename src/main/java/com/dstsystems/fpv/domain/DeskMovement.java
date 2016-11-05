package com.dstsystems.fpv.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DeskMovement.
 */
@Entity
@Table(name = "desk_movement")
public class DeskMovement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Desk fromDesk;

    @ManyToOne
    private Desk toDesk;

    @ManyToOne
    private Plan plan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public DeskMovement employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Desk getFromDesk() {
        return fromDesk;
    }

    public DeskMovement fromDesk(Desk desk) {
        this.fromDesk = desk;
        return this;
    }

    public void setFromDesk(Desk desk) {
        this.fromDesk = desk;
    }

    public Desk getToDesk() {
        return toDesk;
    }

    public DeskMovement toDesk(Desk desk) {
        this.toDesk = desk;
        return this;
    }

    public void setToDesk(Desk desk) {
        this.toDesk = desk;
    }

    public Plan getPlan() {
        return plan;
    }

    public DeskMovement plan(Plan plan) {
        this.plan = plan;
        return this;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeskMovement deskMovement = (DeskMovement) o;
        if(deskMovement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, deskMovement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DeskMovement{" +
            "id=" + id +
            '}';
    }
}
