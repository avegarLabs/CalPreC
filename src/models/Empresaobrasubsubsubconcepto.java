package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(EmpresaobrasubsubsubconceptoPK.class)
public class Empresaobrasubsubsubconcepto {
    private int empresaconstructoraId;
    private int subsubsubconceptoId;
    private int obraId;
    private double valor;
    private Empresaconstructora empresaconstructoraById;
    private Subsubsubconcepto subconceptosgastoByConceptosgastoId;
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
    @Column(name = "subsubsubconcepto_id")
    public int getSubsubsubconceptoId() {
        return subsubsubconceptoId;
    }

    public void setSubsubsubconceptoId(int subsubsubconceptoId) {
        this.subsubsubconceptoId = subsubsubconceptoId;
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
        Empresaobrasubsubsubconcepto that = (Empresaobrasubsubsubconcepto) o;
        return empresaconstructoraId == that.empresaconstructoraId && subsubsubconceptoId == that.subsubsubconceptoId && obraId == that.obraId && Double.compare(that.valor, valor) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empresaconstructoraId, subsubsubconceptoId, obraId, valor);
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
    @JoinColumn(name = "subsubsubconcepto_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Subsubsubconcepto getSubconceptosgastoByConceptosgastoId() {
        return subconceptosgastoByConceptosgastoId;
    }

    public void setSubconceptosgastoByConceptosgastoId(Subsubsubconcepto subconceptosgastoByConceptosgastoId) {
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
