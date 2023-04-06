package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Inversionista {
    private int id;
    private String codigo;
    private String descripcion;
    private Collection<Obra> obrasById;

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "codigo", nullable = false, length = 5)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "descripcion", nullable = false, length = -1)
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
        Inversionista that = (Inversionista) o;
        return id == that.id &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, descripcion);
    }

    @OneToMany(mappedBy = "inversionistaByInversionistaId")
    public Collection<Obra> getObrasById() {
        return obrasById;
    }

    public void setObrasById(Collection<Obra> obrasById) {
        this.obrasById = obrasById;
    }
}
