package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Firmas {
    private int id;
    private String name;
    private String cargo;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "cargo")
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Firmas firmas = (Firmas) o;
        return id == firmas.id &&
                Objects.equals(name, firmas.name) &&
                Objects.equals(cargo, firmas.cargo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cargo);
    }
}
