package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Grupoconstruccion {
    private int id;
    private String codigo;
    private String descripcion;
    private int brigadaconstruccionId;
    private Collection<Cuadrillaconstruccion> cuadrillaconstruccionsById;
    private Brigadaconstruccion brigadaconstruccionByBrigadaconstruccionId;
    private Collection<Certificacion> certificacionsById;
    private Collection<Planificacion> planificacionsById;
    private Collection<Planrenglonvariante> planrenglonvariantesById;

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
    @Column(name = "codigo", nullable = false, length = 5)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "descripcion", nullable = false, length = 50)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "brigadaconstruccion__id", nullable = false)
    public int getBrigadaconstruccionId() {
        return brigadaconstruccionId;
    }

    public void setBrigadaconstruccionId(int brigadaconstruccionId) {
        this.brigadaconstruccionId = brigadaconstruccionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grupoconstruccion that = (Grupoconstruccion) o;
        return id == that.id &&
                brigadaconstruccionId == that.brigadaconstruccionId &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, descripcion, brigadaconstruccionId);
    }

    @OneToMany(mappedBy = "grupoconstruccionByGrupoconstruccionId")
    public Collection<Cuadrillaconstruccion> getCuadrillaconstruccionsById() {
        return cuadrillaconstruccionsById;
    }

    public void setCuadrillaconstruccionsById(Collection<Cuadrillaconstruccion> cuadrillaconstruccionsById) {
        this.cuadrillaconstruccionsById = cuadrillaconstruccionsById;
    }

    @ManyToOne
    @JoinColumn(name = "brigadaconstruccion__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Brigadaconstruccion getBrigadaconstruccionByBrigadaconstruccionId() {
        return brigadaconstruccionByBrigadaconstruccionId;
    }

    public void setBrigadaconstruccionByBrigadaconstruccionId(Brigadaconstruccion brigadaconstruccionByBrigadaconstruccionId) {
        this.brigadaconstruccionByBrigadaconstruccionId = brigadaconstruccionByBrigadaconstruccionId;
    }

    @OneToMany(mappedBy = "grupoconstruccionByGrupoconstruccionId")
    public Collection<Certificacion> getCertificacionsById() {
        return certificacionsById;
    }

    public void setCertificacionsById(Collection<Certificacion> certificacionsById) {
        this.certificacionsById = certificacionsById;
    }

    @OneToMany(mappedBy = "grupoconstruccionByGrupoconstruccionId")
    public Collection<Planificacion> getPlanificacionsById() {
        return planificacionsById;
    }

    public void setPlanificacionsById(Collection<Planificacion> planificacionsById) {
        this.planificacionsById = planificacionsById;
    }

    @OneToMany(mappedBy = "grupoconstruccionByGrupoconstruccionId")
    public Collection<Planrenglonvariante> getPlanrenglonvariantesById() {
        return planrenglonvariantesById;
    }

    public void setPlanrenglonvariantesById(Collection<Planrenglonvariante> planrenglonvariantesById) {
        this.planrenglonvariantesById = planrenglonvariantesById;
    }

    @Override
    public String toString() {
        return codigo + " " + descripcion;
    }
}
