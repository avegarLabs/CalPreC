package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Cuadrillaconstruccion {
    private int id;
    private String codigo;
    private String descripcion;
    private int grupoconstruccionId;
    private Grupoconstruccion grupoconstruccionByGrupoconstruccionId;
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
    @Column(name = "grupoconstruccion__id", nullable = false)
    public int getGrupoconstruccionId() {
        return grupoconstruccionId;
    }

    public void setGrupoconstruccionId(int grupoconstruccionId) {
        this.grupoconstruccionId = grupoconstruccionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuadrillaconstruccion that = (Cuadrillaconstruccion) o;
        return id == that.id &&
                grupoconstruccionId == that.grupoconstruccionId &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, descripcion, grupoconstruccionId);
    }

    @ManyToOne
    @JoinColumn(name = "grupoconstruccion__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Grupoconstruccion getGrupoconstruccionByGrupoconstruccionId() {
        return grupoconstruccionByGrupoconstruccionId;
    }

    public void setGrupoconstruccionByGrupoconstruccionId(Grupoconstruccion grupoconstruccionByGrupoconstruccionId) {
        this.grupoconstruccionByGrupoconstruccionId = grupoconstruccionByGrupoconstruccionId;
    }

    @OneToMany(mappedBy = "cuadrillaconstruccionByCuadrillaconstruccionId")
    public Collection<Certificacion> getCertificacionsById() {
        return certificacionsById;
    }

    public void setCertificacionsById(Collection<Certificacion> certificacionsById) {
        this.certificacionsById = certificacionsById;
    }

    @OneToMany(mappedBy = "cuadrillaconstruccionByCuadrillaconstruccionId")
    public Collection<Planificacion> getPlanificacionsById() {
        return planificacionsById;
    }

    public void setPlanificacionsById(Collection<Planificacion> planificacionsById) {
        this.planificacionsById = planificacionsById;
    }

    @OneToMany(mappedBy = "cuadrillaconstruccionByCuadrillaconstruccionId")
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
