package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class EmpresaobrasalarioPK implements Serializable {
    private int empresaconstructoraId;
    private int obraId;
    private int salarioId;

    @Column(name = "empresaconstructora_id", nullable = false)
    @Id
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Column(name = "obra_id", nullable = false)
    @Id
    public int getObraId() {
        return obraId;
    }

    public void setObraId(int obraId) {
        this.obraId = obraId;
    }

    @Column(name = "salario_id", nullable = false)
    @Id
    public int getSalarioId() {
        return salarioId;
    }

    public void setSalarioId(int salarioId) {
        this.salarioId = salarioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpresaobrasalarioPK that = (EmpresaobrasalarioPK) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                obraId == that.obraId &&
                salarioId == that.salarioId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(empresaconstructoraId, obraId, salarioId);
    }
}
