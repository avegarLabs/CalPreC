package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class EmpresaobrasubsubsubconceptoPK implements Serializable {
    private int empresaconstructoraId;
    private int subsubsubconceptoId;
    private int obraId;

    @Column(name = "empresaconstructora_id")
    @Id
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Column(name = "subsubsubconcepto_id")
    @Id
    public int getSubsubsubconceptoId() {
        return subsubsubconceptoId;
    }

    public void setSubsubsubconceptoId(int subsubsubconceptoId) {
        this.subsubsubconceptoId = subsubsubconceptoId;
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
        EmpresaobrasubsubsubconceptoPK that = (EmpresaobrasubsubsubconceptoPK) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                subsubsubconceptoId == that.subsubsubconceptoId &&
                obraId == that.obraId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(empresaconstructoraId, subsubsubconceptoId, obraId);
    }
}
