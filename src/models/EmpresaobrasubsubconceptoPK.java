package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class EmpresaobrasubsubconceptoPK implements Serializable {
    private int empresaconstructoraId;
    private int subsubconceptoId;
    private int obraId;

    @Column(name = "empresaconstructora_id")
    @Id
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Column(name = "subsubconcepto_id")
    @Id
    public int getSubsubconceptoId() {
        return subsubconceptoId;
    }

    public void setSubsubconceptoId(int subconceptoId) {
        this.subsubconceptoId = subconceptoId;
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
        EmpresaobrasubsubconceptoPK that = (EmpresaobrasubsubconceptoPK) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                subsubconceptoId == that.subsubconceptoId &&
                obraId == that.obraId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(empresaconstructoraId, subsubconceptoId, obraId);
    }
}
