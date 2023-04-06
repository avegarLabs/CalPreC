package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Empresa {
    private int id;
    private String reup;
    private String nombre;
    private String comercial;

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
    @Column(name = "reup", nullable = false, length = 7)
    public String getReup() {
        return reup;
    }

    public void setReup(String reup) {
        this.reup = reup;
    }

    @Basic
    @Column(name = "nombre", nullable = false, length = 100)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "comercial", nullable = false, length = 50)
    public String getComercial() {
        return comercial;
    }

    public void setComercial(String comercial) {
        this.comercial = comercial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresa empresa = (Empresa) o;
        return id == empresa.id &&
                Objects.equals(reup, empresa.reup) &&
                Objects.equals(nombre, empresa.nombre) &&
                Objects.equals(comercial, empresa.comercial);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, reup, nombre, comercial);
    }
}
