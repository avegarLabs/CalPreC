package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class EmpresaobrasubconceptoPK implements Serializable {
    private int empresaconstructoraId;
    private int subconceptoId;
    private int obraId;

    @Column(name = "empresaconstructora_id")
    @Id
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Column(name = "subconcepto_id")
    @Id
    public int getSubconceptoId() {
        return subconceptoId;
    }

    public void setSubconceptoId(int subconceptoId) {
        this.subconceptoId = subconceptoId;
    }

    @Column(name = "obra_id")
    @Id
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
        EmpresaobrasubconceptoPK that = (EmpresaobrasubconceptoPK) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                subconceptoId == that.subconceptoId &&
                obraId == that.obraId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(empresaconstructoraId, subconceptoId, obraId);
    }
}
