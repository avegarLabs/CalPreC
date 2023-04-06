package models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Subgenerica {
    private Integer id;
    private String codigo;
    private String descripcion;
    private String espcode;
    private List<Especifica> especificasById;

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
    @Column(name = "espcode")
    public String getEspcode() {
        return espcode;
    }

    public void setEspcode(String espcode) {
        this.espcode = espcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subgenerica that = (Subgenerica) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(espcode, that.espcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo, descripcion, espcode);
    }

    @OneToMany(mappedBy = "subgenericaBySubgenericaId")
    public List<Especifica> getEspecificasById() {
        return especificasById;
    }

    public void setEspecificasById(List<Especifica> especificasById) {
        this.especificasById = especificasById;
    }
}
