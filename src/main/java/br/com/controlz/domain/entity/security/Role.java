package br.com.controlz.domain.entity.security;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "role")
public class Role implements Serializable {

    @OneToMany(mappedBy = "role")
    private final List<User> userList = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idRole;

    @Column(name = "name")
    private String name;

    public Role() {
    }

    public Role(Long idRole, String name) {
        this.idRole = idRole;
        this.name = name;
    }

    public Long getIdRole() {
        return idRole;
    }

    public void setIdRole(Long idRole) {
        this.idRole = idRole;
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
        Role role = (Role) o;
        return Objects.equals(userList, role.userList) && Objects.equals(idRole, role.idRole) && Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userList, idRole, name);
    }
}
