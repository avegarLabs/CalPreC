package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(EmpresaobrasubconceptoPK.class)
public class Empresaobrasubconcepto {
    private int empresaconstructoraId;
    private int subconceptoId;
    private int obraId;
    private double valor;
    private Empresaconstructora empresaconstructoraByEmpresaconstructoraId;
    private Subconcepto conceptosgastoByConceptosgastoId;
    private Obra obraByObraId;
    private double salario;

    @Id
    @Column(name = "empresaconstructora_id")
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Id
    @Column(name = "subconcepto_id")
    public int getSubconceptoId() {
        return subconceptoId;
    }

    public void setSubconceptoId(int subconceptoId) {
        this.subconceptoId = subconceptoId;
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
    @Column(name = "valor")
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresaobrasubconcepto that = (Empresaobrasubconcepto) o;
        return empresaconstructoraId == that.empresaconstructoraId && subconceptoId == that.subconceptoId && obraId == that.obraId && Double.compare(that.valor, valor) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empresaconstructoraId, subconceptoId, obraId, valor);
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
    @JoinColumn(name = "subconcepto_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Subconcepto getConceptosgastoByConceptosgastoId() {
        return conceptosgastoByConceptosgastoId;
    }

    public void setConceptosgastoByConceptosgastoId(Subconcepto conceptosgastoByConceptosgastoId) {
        this.conceptosgastoByConceptosgastoId = conceptosgastoByConceptosgastoId;
    }

    @ManyToOne
    @JoinColumn(name = "obra_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Obra getObraByObraId() {
        return obraByObraId;
    }

    public void setObraByObraId(Obra obraByObraId) {
        this.obraByObraId = obraByObraId;
    }

    @Basic
    @Column(name = "salario")
    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
}
