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
    private Double x;

    @Column(name = "y")
    private Double y;

    @Column(name = "width")
    private Double width;

    @Column(name = "height")
    private Double height;

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

    public Double getX() {
        return x;
    }

    public Desk x(Double x) {
        this.x = x;
        return this;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public Desk y(Double y) {
        this.y = y;
        return this;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getWidth() {
        return width;
    }

    public Desk width(Double width) {
        this.width = width;
        return this;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public Desk height(Double height) {
        this.height = height;
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
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
            ", width='" + width + "'" +
            ", height='" + height + "'" +
            '}';
    }
}
