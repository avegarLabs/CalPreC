package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(CoeficientesequiposPK.class)
public class Coeficientesequipos {
    private int empresaconstructoraId;
    private int recursosId;
    private int obraId;
    private double cpo;
    private double cpe;
    private double cet;
    private double otra;
    private double salario;
    private Empresaconstructora empresaconstructoraByEmpresaconstructoraId;
    private Recursos recursosByRecursosId;
    private Obra obraByObraId;

    @Id
    @Column(name = "empresaconstructora_id")
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Id
    @Column(name = "recursos_id")
    public int getRecursosId() {
        return recursosId;
    }

    public void setRecursosId(int recursosId) {
        this.recursosId = recursosId;
    }

    @Id
    @Column(name = "obra_id")
    public int getObraId() {
        return obraId;
    }

    public void setObraId(int obraId) {
        this.obraId = obraId;
    }

    @Basic
    @Column(name = "cpo")
    public double getCpo() {
        return cpo;
    }

    public void setCpo(double cpo) {
        this.cpo = cpo;
    }

    @Basic
    @Column(name = "cpe")
    public double getCpe() {
        return cpe;
    }

    public void setCpe(double cpe) {
        this.cpe = cpe;
    }

    @Basic
    @Column(name = "cet")
    public double getCet() {
        return cet;
    }

    public void setCet(double cet) {
        this.cet = cet;
    }

    @Basic
    @Column(name = "salario")
    public double getSalario() {
        return otra;
    }

    public void setSalario(double salario) {
        this.otra = salario;
    }

    @Basic
    @Column(name = "otra")
    public double getOtra() {
        return otra;
    }

    public void setOtra(double otra) {
        this.otra = otra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coeficientesequipos that = (Coeficientesequipos) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                recursosId == that.recursosId &&
                obraId == that.obraId &&
                Double.compare(that.cpo, cpo) == 0 &&
                Double.compare(that.cpe, cpe) == 0 &&
                Double.compare(that.cet, cet) == 0 &&
                Double.compare(that.otra, otra) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empresaconstructoraId, recursosId, obraId, cpo, cpe, cet, otra);
    }

    @ManyToOne
    @JoinColumn(name = "empresaconstructora_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Empresaconstructora getEmpresaconstructoraByEmpresaconstructoraId() {
        return empresaconstructoraByEmpresaconstructoraId;
    }

    public void setEmpresaconstructoraByEmpresaconstructoraId(Empresaconstructora empresaconstructoraByEmpresaconstructoraId) {
        this.empresaconstructoraByEmpresaconstructoraId = empresaconstructoraByEmpresaconstructoraId;
    }

    @ManyToOne
    @JoinColumn(name = "recursos_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Recursos getRecursosByRecursosId() {
        return recursosByRecursosId;
    }

    public void setRecursosByRecursosId(Recursos recursosByRecursosId) {
        this.recursosByRecursosId = recursosByRecursosId;
    }

    @ManyToOne
    @JoinColumn(name = "obra_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Obra getObraByObraId() {
        return obraByObraId;
    }

    public void setObraByObraId(Obra obraByObraId) {
        this.obraByObraId = obraByObraId;
    }
}
