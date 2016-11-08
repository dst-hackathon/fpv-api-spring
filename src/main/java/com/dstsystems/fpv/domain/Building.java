package com.dstsystems.fpv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Building.
 */
@Entity
@Table(name = "building")
public class Building implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private Plan plan;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Floor> floors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Building name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Plan getPlan() {
        return plan;
    }

    public Building plan(Plan plan) {
        this.plan = plan;
        return this;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Set<Floor> getFloors() {
        return floors;
    }

    public Building floors(Set<Floor> floors) {
        this.floors = floors;
        return this;
    }

    public Building addFloor(Floor floor) {
        floors.add(floor);
        floor.setBuilding(this);
        return this;
    }

    public Building removeFloor(Floor floor) {
        floors.remove(floor);
        floor.setBuilding(null);
        return this;
    }

    public void setFloors(Set<Floor> floors) {
        this.floors = floors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Building building = (Building) o;
        if(building.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, building.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Building{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
