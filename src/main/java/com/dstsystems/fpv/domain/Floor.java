package com.dstsystems.fpv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Floor.
 */
@Entity
@Table(name = "floor")
public class Floor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private Plan plan;

    @OneToMany(mappedBy = "floor")
    @JsonIgnore
    private Set<Desk> desks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Floor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Plan getPlan() {
        return plan;
    }

    public Floor plan(Plan plan) {
        this.plan = plan;
        return this;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Set<Desk> getDesks() {
        return desks;
    }

    public Floor desks(Set<Desk> desks) {
        this.desks = desks;
        return this;
    }

    public Floor addDesk(Desk desk) {
        desks.add(desk);
        desk.setFloor(this);
        return this;
    }

    public Floor removeDesk(Desk desk) {
        desks.remove(desk);
        desk.setFloor(null);
        return this;
    }

    public void setDesks(Set<Desk> desks) {
        this.desks = desks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Floor floor = (Floor) o;
        if(floor.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, floor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Floor{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
