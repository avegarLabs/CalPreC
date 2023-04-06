package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Tipoobra {
    private int id;
    private String codigo;
    private String descripcion;
    private Collection<Obra> obrasById;

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "codigo")
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tipoobra tipoobra = (Tipoobra) o;
        return id == tipoobra.id &&
                Objects.equals(codigo, tipoobra.codigo) &&
                Objects.equals(descripcion, tipoobra.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, descripcion);
    }

    @OneToMany(mappedBy = "tipoobraByTipoobraId")
    public Collection<Obra> getObrasById() {
        return obrasById;
    }

    public void setObrasById(Collection<Obra> obrasById) {
        this.obrasById = obrasById;
    }
}
