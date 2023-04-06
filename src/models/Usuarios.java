package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Usuarios {
    private int id;
    private String usuario;
    private String nombre;
    private String cargo;
    private String dpto;
    private String contrasena;
    private int gruposId;
    private Grupos gruposByGruposId;

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
    @Column(name = "usuario", nullable = false, length = 50)
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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
    @Column(name = "cargo", nullable = false, length = 100)
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Basic
    @Column(name = "dpto", nullable = false, length = 100)
    public String getDpto() {
        return dpto;
    }

    public void setDpto(String dpto) {
        this.dpto = dpto;
    }

    @Basic
    @Column(name = "contrasena", nullable = false, length = 10)
    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Basic
    @Column(name = "grupos__id", nullable = false)
    public int getGruposId() {
        return gruposId;
    }

    public void setGruposId(int gruposId) {
        this.gruposId = gruposId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuarios usuarios = (Usuarios) o;
        return id == usuarios.id &&
                gruposId == usuarios.gruposId &&
                Objects.equals(usuario, usuarios.usuario) &&
                Objects.equals(nombre, usuarios.nombre) &&
                Objects.equals(cargo, usuarios.cargo) &&
                Objects.equals(dpto, usuarios.dpto) &&
                Objects.equals(contrasena, usuarios.contrasena);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, usuario, nombre, cargo, dpto, contrasena, gruposId);
    }

    @ManyToOne
    @JoinColumn(name = "grupos__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Grupos getGruposByGruposId() {
        return gruposByGruposId;
    }

    public void setGruposByGruposId(Grupos gruposByGruposId) {
        this.gruposByGruposId = gruposByGruposId;
    }


}
