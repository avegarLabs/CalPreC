package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Grupos {
    private int id;
    private String nombre;
    private String controltotal;
    private String insertar;
    private String modificar;
    private String eliminar;
    private String visualizar;
    private Collection<Usuarios> usuariosById;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "controltotal")
    public String getControltotal() {
        return controltotal;
    }

    public void setControltotal(String controltotal) {
        this.controltotal = controltotal;
    }

    @Basic
    @Column(name = "insertar")
    public String getInsertar() {
        return insertar;
    }

    public void setInsertar(String insertar) {
        this.insertar = insertar;
    }

    @Basic
    @Column(name = "modificar")
    public String getModificar() {
        return modificar;
    }

    public void setModificar(String modificar) {
        this.modificar = modificar;
    }

    @Basic
    @Column(name = "eliminar")
    public String getEliminar() {
        return eliminar;
    }

    public void setEliminar(String eliminar) {
        this.eliminar = eliminar;
    }

    @Basic
    @Column(name = "visualizar")
    public String getVisualizar() {
        return visualizar;
    }

    public void setVisualizar(String visualizar) {
        this.visualizar = visualizar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grupos grupos = (Grupos) o;
        return id == grupos.id &&
                Objects.equals(nombre, grupos.nombre) &&
                Objects.equals(controltotal, grupos.controltotal) &&
                Objects.equals(insertar, grupos.insertar) &&
                Objects.equals(modificar, grupos.modificar) &&
                Objects.equals(eliminar, grupos.eliminar) &&
                Objects.equals(visualizar, grupos.visualizar);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, nombre, controltotal, insertar, modificar, eliminar, visualizar);
    }

    @OneToMany(mappedBy = "gruposByGruposId")
    public Collection<Usuarios> getUsuariosById() {
        return usuariosById;
    }

    public void setUsuariosById(Collection<Usuarios> usuariosById) {
        this.usuariosById = usuariosById;
    }
}
