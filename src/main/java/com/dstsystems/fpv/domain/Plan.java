package com.dstsystems.fpv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Plan.
 */
@Entity
@Table(name = "plan")
public class Plan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @OneToMany(mappedBy = "plan")
    @JsonIgnore
    private Set<Building> buildings = new HashSet<>();

    @OneToMany(mappedBy = "plan")
    @JsonIgnore
    private Set<DeskAssignment> deskAssignments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Plan name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public Plan effectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Set<Building> getBuildings() {
        return buildings;
    }

    public Plan buildings(Set<Building> buildings) {
        this.buildings = buildings;
        return this;
    }

    public Plan addBuilding(Building building) {
        buildings.add(building);
        building.setPlan(this);
        return this;
    }

    public Plan removeBuilding(Building building) {
        buildings.remove(building);
        building.setPlan(null);
        return this;
    }

    public void setBuildings(Set<Building> buildings) {
        this.buildings = buildings;
    }

    public Set<DeskAssignment> getDeskAssignments() {
        return deskAssignments;
    }

    public Plan deskAssignments(Set<DeskAssignment> deskAssignments) {
        this.deskAssignments = deskAssignments;
        return this;
    }

    public Plan addDeskAssignment(DeskAssignment deskAssignment) {
        deskAssignments.add(deskAssignment);
        deskAssignment.setPlan(this);
        return this;
    }

    public Plan removeDeskAssignment(DeskAssignment deskAssignment) {
        deskAssignments.remove(deskAssignment);
        deskAssignment.setPlan(null);
        return this;
    }

    public void setDeskAssignments(Set<DeskAssignment> deskAssignments) {
        this.deskAssignments = deskAssignments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Plan plan = (Plan) o;
        if(plan.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, plan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Plan{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", effectiveDate='" + effectiveDate + "'" +
            '}';
    }
}
