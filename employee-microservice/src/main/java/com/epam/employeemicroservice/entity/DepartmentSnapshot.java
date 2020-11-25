package com.epam.employeemicroservice.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "department_snapshot")
public class DepartmentSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_snapshot_id")
    private Long departmentId;

    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;


    public DepartmentSnapshot() {
    }

    public DepartmentSnapshot(Long id, String name) {

        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentSnapshot that = (DepartmentSnapshot) o;
        return Objects.equals(departmentId, that.departmentId) &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, id, name);
    }


}
