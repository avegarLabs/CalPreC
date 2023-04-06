package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Brigadaconstruccion {
    private int id;
    private String codigo;
    private String descripcion;
    private int empresaconstructoraId;
    private Collection<Grupoconstruccion> grupoconstruccionsById;
    private Empresaconstructora empresaconstructoraByEmpresaconstructoraId;
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
    @Column(name = "empresaconstructora__id", nullable = false)
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brigadaconstruccion that = (Brigadaconstruccion) o;
        return id == that.id &&
                empresaconstructoraId == that.empresaconstructoraId &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, descripcion, empresaconstructoraId);
    }

    @OneToMany(mappedBy = "brigadaconstruccionByBrigadaconstruccionId")
    public Collection<Grupoconstruccion> getGrupoconstruccionsById() {
        return grupoconstruccionsById;
    }

    public void setGrupoconstruccionsById(Collection<Grupoconstruccion> grupoconstruccionsById) {
        this.grupoconstruccionsById = grupoconstruccionsById;
    }

    @ManyToOne
    @JoinColumn(name = "empresaconstructora__id", referencedColumnName = "id", insertable = false, updatable = false)
    public Empresaconstructora getEmpresaconstructoraByEmpresaconstructoraId() {
        return empresaconstructoraByEmpresaconstructoraId;
    }

    public void setEmpresaconstructoraByEmpresaconstructoraId(Empresaconstructora empresaconstructoraByEmpresaconstructoraId) {
        this.empresaconstructoraByEmpresaconstructoraId = empresaconstructoraByEmpresaconstructoraId;
    }

    @OneToMany(mappedBy = "brigadaconstruccionByBrigadaconstruccionId")
    public Collection<Certificacion> getCertificacionsById() {
        return certificacionsById;
    }

    public void setCertificacionsById(Collection<Certificacion> certificacionsById) {
        this.certificacionsById = certificacionsById;
    }

    @OneToMany(mappedBy = "brigadaconstruccionByBrigadaconstruccionId")
    public Collection<Planificacion> getPlanificacionsById() {
        return planificacionsById;
    }

    public void setPlanificacionsById(Collection<Planificacion> planificacionsById) {
        this.planificacionsById = planificacionsById;
    }

    @OneToMany(mappedBy = "brigadaconstruccionByBrigadaconstruccionId")
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
