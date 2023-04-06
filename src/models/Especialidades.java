package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Especialidades {
    private int id;
    private String codigo;
    private String descripcion;
    private Collection<Subespecialidades> subespecialidadesById;
    private Collection<Unidadobra> unidadobrasById;
    private Collection<Nivelespecifico> nivelespecificosById;

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
    @Column(name = "codigo", nullable = false, length = 4)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "descripcion", nullable = false, length = 100)
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
        Especialidades that = (Especialidades) o;
        return id == that.id &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo, descripcion);
    }

    @OneToMany(mappedBy = "especialidadesByEspecialidadesId")
    public Collection<Subespecialidades> getSubespecialidadesById() {
        return subespecialidadesById;
    }

    public void setSubespecialidadesById(Collection<Subespecialidades> subespecialidadesById) {
        this.subespecialidadesById = subespecialidadesById;
    }

    @OneToMany(mappedBy = "especialidadesByEspecialidadesId")
    public Collection<Unidadobra> getUnidadobrasById() {
        return unidadobrasById;
    }

    public void setUnidadobrasById(Collection<Unidadobra> unidadobrasById) {
        this.unidadobrasById = unidadobrasById;
    }

    @OneToMany(mappedBy = "especialidadesByEspecialidadesId")
    public Collection<Nivelespecifico> getNivelespecificosById() {
        return nivelespecificosById;
    }

    public void setNivelespecificosById(Collection<Nivelespecifico> nivelespecificosById) {
        this.nivelespecificosById = nivelespecificosById;
    }

    @Override
    public String toString() {
        return codigo + " " + descripcion;
    }
}
