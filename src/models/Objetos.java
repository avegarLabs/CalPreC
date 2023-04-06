package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Objetos {
    private int id;
    private String codigo;
    private String descripcion;
    private int zonasId;
    private Collection<Nivel> nivelsById;
    private Zonas zonasByZonasId;
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
    @Column(name = "zonas__id", nullable = false)
    public int getZonasId() {
        return zonasId;
    }

    public void setZonasId(int zonasId) {
        this.zonasId = zonasId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Objetos that = (Objetos) o;
        return Objects.equals(codigo, that.codigo) &&
                descripcion == that.descripcion;

    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, descripcion, zonasId);
    }

    @OneToMany(mappedBy = "objetosByObjetosId", cascade = CascadeType.REMOVE)
    public Collection<Nivel> getNivelsById() {
        return nivelsById;
    }

    public void setNivelsById(Collection<Nivel> nivelsById) {
        this.nivelsById = nivelsById;
    }

    @ManyToOne
    @JoinColumn(name = "zonas__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Zonas getZonasByZonasId() {
        return zonasByZonasId;
    }

    public void setZonasByZonasId(Zonas zonasByZonasId) {
        this.zonasByZonasId = zonasByZonasId;
    }

    @OneToMany(mappedBy = "objetosByObjetosId", cascade = CascadeType.REMOVE)
    public Collection<Unidadobra> getUnidadobrasById() {
        return unidadobrasById;
    }

    public void setUnidadobrasById(Collection<Unidadobra> unidadobrasById) {
        this.unidadobrasById = unidadobrasById;
    }

    @OneToMany(mappedBy = "objetosByObjetosId", cascade = CascadeType.REMOVE)
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
