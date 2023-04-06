package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Especifica {
    private Integer id;
    private String codigo;
    private String descripcion;
    private Integer subgenericaId;
    private Subgenerica subgenericaBySubgenericaId;

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Basic
    @Column(name = "subgenerica__id")
    public Integer getSubgenericaId() {
        return subgenericaId;
    }

    public void setSubgenericaId(Integer subgenericaId) {
        this.subgenericaId = subgenericaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Especifica that = (Especifica) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(subgenericaId, that.subgenericaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo, descripcion, subgenericaId);
    }

    @ManyToOne
    @JoinColumn(name = "subgenerica__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Subgenerica getSubgenericaBySubgenericaId() {
        return subgenericaBySubgenericaId;
    }

    public void setSubgenericaBySubgenericaId(Subgenerica subgenericaBySubgenericaId) {
        this.subgenericaBySubgenericaId = subgenericaBySubgenericaId;
    }
}
