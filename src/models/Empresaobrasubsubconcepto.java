package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(EmpresaobrasubsubconceptoPK.class)
public class Empresaobrasubsubconcepto {
    private int empresaconstructoraId;
    private int subsubconceptoId;
    private int obraId;
    private double valor;
    private Empresaconstructora empresaconstructoraById;
    private Subsubconcepto subconceptosgastoByConceptosgastoId;
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
    @Column(name = "subsubconcepto_id")
    public int getSubsubconceptoId() {
        return subsubconceptoId;
    }

    public void setSubsubconceptoId(int subsubconceptoId) {
        this.subsubconceptoId = subsubconceptoId;
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
        Empresaobrasubsubconcepto that = (Empresaobrasubsubconcepto) o;
        return empresaconstructoraId == that.empresaconstructoraId && subsubconceptoId == that.subsubconceptoId && obraId == that.obraId && Double.compare(that.valor, valor) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empresaconstructoraId, subsubconceptoId, obraId, valor);
    }

    @ManyToOne
    @JoinColumn(name = "empresaconstructora_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Empresaconstructora getEmpresaconstructoraById() {
        return empresaconstructoraById;
    }

    public void setEmpresaconstructoraById(Empresaconstructora empresaconstructoraById) {
        this.empresaconstructoraById = empresaconstructoraById;
    }

    @ManyToOne
    @JoinColumn(name = "subsubconcepto_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Subsubconcepto getSubconceptosgastoByConceptosgastoId() {
        return subconceptosgastoByConceptosgastoId;
    }

    public void setSubconceptosgastoByConceptosgastoId(Subsubconcepto subconceptosgastoByConceptosgastoId) {
        this.subconceptosgastoByConceptosgastoId = subconceptosgastoByConceptosgastoId;
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
