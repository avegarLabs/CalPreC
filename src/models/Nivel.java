package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Nivel {
    private int id;
    private String codigo;
    private String descripcion;
    private int objetosId;
    private Objetos objetosByObjetosId;
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
    @Column(name = "objetos__id", nullable = false)
    public int getObjetosId() {
        return objetosId;
    }

    public void setObjetosId(int objetosId) {
        this.objetosId = objetosId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nivel nivel = (Nivel) o;
        return id == nivel.id &&
                objetosId == nivel.objetosId &&
                Objects.equals(codigo, nivel.codigo) &&
                Objects.equals(descripcion, nivel.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, descripcion, objetosId);
    }

    @ManyToOne
    @JoinColumn(name = "objetos__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Objetos getObjetosByObjetosId() {
        return objetosByObjetosId;
    }

    public void setObjetosByObjetosId(Objetos objetosByObjetosId) {
        this.objetosByObjetosId = objetosByObjetosId;
    }

    @OneToMany(mappedBy = "nivelByNivelId")
    public Collection<Unidadobra> getUnidadobrasById() {
        return unidadobrasById;
    }

    public void setUnidadobrasById(Collection<Unidadobra> unidadobrasById) {
        this.unidadobrasById = unidadobrasById;
    }

    @OneToMany(mappedBy = "nivelByNivelId")
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
