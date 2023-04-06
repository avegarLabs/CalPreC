package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Subespecialidades {
    private int id;
    private String codigo;
    private String descripcion;
    private int especialidadesId;
    private Especialidades especialidadesByEspecialidadesId;
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

    @Basic
    @Column(name = "especialidades__id", nullable = false)
    public int getEspecialidadesId() {
        return especialidadesId;
    }

    public void setEspecialidadesId(int especialidadesId) {
        this.especialidadesId = especialidadesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subespecialidades that = (Subespecialidades) o;
        return id == that.id &&
                especialidadesId == that.especialidadesId &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, descripcion, especialidadesId);
    }

    @ManyToOne
    @JoinColumn(name = "especialidades__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Especialidades getEspecialidadesByEspecialidadesId() {
        return especialidadesByEspecialidadesId;
    }

    public void setEspecialidadesByEspecialidadesId(Especialidades especialidadesByEspecialidadesId) {
        this.especialidadesByEspecialidadesId = especialidadesByEspecialidadesId;
    }

    @OneToMany(mappedBy = "subespecialidadesBySubespecialidadesId")
    public Collection<Unidadobra> getUnidadobrasById() {
        return unidadobrasById;
    }

    public void setUnidadobrasById(Collection<Unidadobra> unidadobrasById) {
        this.unidadobrasById = unidadobrasById;
    }


    @OneToMany(mappedBy = "subespecialidadesBySubespecialidadesId")
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
