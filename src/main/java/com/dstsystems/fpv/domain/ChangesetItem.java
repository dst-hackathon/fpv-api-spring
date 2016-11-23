package com.dstsystems.fpv.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.dstsystems.fpv.domain.enumeration.ChangesetItemStatus;

/**
 * A ChangesetItem.
 */
@Entity
@Table(name = "changeset_item")
public class ChangesetItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChangesetItemStatus status;

    @ManyToOne
    @NotNull
    private Employee employee;

    @ManyToOne
    private Desk fromDesk;

    @ManyToOne
    private Desk toDesk;

    @ManyToOne
    @NotNull
    private Changeset changeset;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChangesetItemStatus getStatus() {
        return status;
    }

    public ChangesetItem status(ChangesetItemStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ChangesetItemStatus status) {
        this.status = status;
    }

    public Employee getEmployee() {
        return employee;
    }

    public ChangesetItem employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Desk getFromDesk() {
        return fromDesk;
    }

    public ChangesetItem fromDesk(Desk desk) {
        this.fromDesk = desk;
        return this;
    }

    public void setFromDesk(Desk desk) {
        this.fromDesk = desk;
    }

    public Desk getToDesk() {
        return toDesk;
    }

    public ChangesetItem toDesk(Desk desk) {
        this.toDesk = desk;
        return this;
    }

    public void setToDesk(Desk desk) {
        this.toDesk = desk;
    }

    public Changeset getChangeset() {
        return changeset;
    }

    public ChangesetItem changeset(Changeset changeset) {
        this.changeset = changeset;
        return this;
    }

    public void setChangeset(Changeset changeset) {
        this.changeset = changeset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChangesetItem changesetItem = (ChangesetItem) o;
        if(changesetItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, changesetItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChangesetItem{" +
            "id=" + id +
            ", status='" + status + "'" +
            '}';
    }
}
