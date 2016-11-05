package com.dstsystems.fpv.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Shadow.
 */
@Entity
@Table(name = "shadow")
public class Shadow extends AbstractAuditingEntity implements Serializable {

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Shadow employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Desk getFromDesk() {
        return fromDesk;
    }

    public Shadow fromDesk(Desk desk) {
        this.fromDesk = desk;
        return this;
    }

    public void setFromDesk(Desk desk) {
        this.fromDesk = desk;
    }

    public Desk getToDesk() {
        return toDesk;
    }

    public Shadow toDesk(Desk desk) {
        this.toDesk = desk;
        return this;
    }

    public void setToDesk(Desk desk) {
        this.toDesk = desk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shadow shadow = (Shadow) o;
        if(shadow.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, shadow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Shadow{" +
            "id=" + id +
            '}';
    }
}
