package com.dstsystems.fpv.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DeskAssignment.
 */
@Entity
@Table(name = "desk_assignment")
public class DeskAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Desk desk;

    @ManyToOne
    private Plan plan;

    @ManyToOne
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Desk getDesk() {
        return desk;
    }

    public DeskAssignment desk(Desk desk) {
        this.desk = desk;
        return this;
    }

    public void setDesk(Desk desk) {
        this.desk = desk;
    }

    public Plan getPlan() {
        return plan;
    }

    public DeskAssignment plan(Plan plan) {
        this.plan = plan;
        return this;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Employee getEmployee() {
        return employee;
    }

    public DeskAssignment employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeskAssignment deskAssignment = (DeskAssignment) o;
        if(deskAssignment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, deskAssignment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DeskAssignment{" +
            "id=" + id +
            '}';
    }
}
