package models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Zonas implements Serializable {
    private int id;
    private String codigo;
    private String desripcion;
    private int obraId;
    private Collection<Objetos> objetosById;
    private Obra obraByObraId;
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
    @Column(name = "codigo", nullable = false, length = 3)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "desripcion", nullable = false, length = 100)
    public String getDesripcion() {
        return desripcion;
    }

    public void setDesripcion(String desripcion) {
        this.desripcion = desripcion;
    }

    @Basic
    @Column(name = "obra__id", nullable = false)
    public int getObraId() {
        return obraId;
    }

    public void setObraId(int obraId) {
        this.obraId = obraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zonas zonas = (Zonas) o;
        return id == zonas.id &&
                obraId == zonas.obraId &&
                Objects.equals(codigo, zonas.codigo) &&
                Objects.equals(desripcion, zonas.desripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, desripcion, obraId);
    }

    @OneToMany(mappedBy = "zonasByZonasId", cascade = CascadeType.REMOVE)
    public Collection<Objetos> getObjetosById() {
        return objetosById;
    }

    public void setObjetosById(Collection<Objetos> objetosById) {
        this.objetosById = objetosById;
    }

    @ManyToOne
    @JoinColumn(name = "obra__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Obra getObraByObraId() {
        return obraByObraId;
    }

    public void setObraByObraId(Obra obraByObraId) {
        this.obraByObraId = obraByObraId;
    }

    @OneToMany(mappedBy = "zonasByZonasId", cascade = CascadeType.REMOVE)
    public Collection<Unidadobra> getUnidadobrasById() {
        return unidadobrasById;
    }

    public void setUnidadobrasById(Collection<Unidadobra> unidadobrasById) {
        this.unidadobrasById = unidadobrasById;
    }

    @OneToMany(mappedBy = "zonasByZonasId", cascade = CascadeType.REMOVE)
    public Collection<Nivelespecifico> getNivelespecificosById() {
        return nivelespecificosById;
    }

    public void setNivelespecificosById(Collection<Nivelespecifico> nivelespecificosById) {
        this.nivelespecificosById = nivelespecificosById;
    }

    @Override
    public String toString() {
        return codigo + " " + desripcion;
    }
}
