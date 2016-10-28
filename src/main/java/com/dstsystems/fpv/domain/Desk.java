package com.dstsystems.fpv.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Desk.
 */
@Entity
@Table(name = "desk")
public class Desk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    @ManyToOne
    private Floor floor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Desk code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getX() {
        return x;
    }

    public Desk x(Integer x) {
        this.x = x;
        return this;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public Desk y(Integer y) {
        this.y = y;
        return this;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Floor getFloor() {
        return floor;
    }

    public Desk floor(Floor floor) {
        this.floor = floor;
        return this;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Desk desk = (Desk) o;
        if(desk.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, desk.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Desk{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", x='" + x + "'" +
            ", y='" + y + "'" +
            '}';
    }
}
