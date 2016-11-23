package com.dstsystems.fpv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.dstsystems.fpv.domain.enumeration.ChangesetStatus;

/**
 * A Changeset.
 */
@Entity
@Table(name = "changeset")
public class Changeset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChangesetStatus status;

    @ManyToOne
    @NotNull
    private Plan plan;

    @OneToMany(mappedBy = "changeset")
    @JsonIgnore
    private Set<ChangesetItem> changesetItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public Changeset effectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public ChangesetStatus getStatus() {
        return status;
    }

    public Changeset status(ChangesetStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ChangesetStatus status) {
        this.status = status;
    }

    public Plan getPlan() {
        return plan;
    }

    public Changeset plan(Plan plan) {
        this.plan = plan;
        return this;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Set<ChangesetItem> getChangesetItems() {
        return changesetItems;
    }

    public Changeset changesetItems(Set<ChangesetItem> changesetItems) {
        this.changesetItems = changesetItems;
        return this;
    }

    public Changeset addChangesetItem(ChangesetItem changesetItem) {
        changesetItems.add(changesetItem);
        changesetItem.setChangeset(this);
        return this;
    }

    public Changeset removeChangesetItem(ChangesetItem changesetItem) {
        changesetItems.remove(changesetItem);
        changesetItem.setChangeset(null);
        return this;
    }

    public void setChangesetItems(Set<ChangesetItem> changesetItems) {
        this.changesetItems = changesetItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Changeset changeset = (Changeset) o;
        if(changeset.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, changeset.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Changeset{" +
            "id=" + id +
            ", effectiveDate='" + effectiveDate + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
