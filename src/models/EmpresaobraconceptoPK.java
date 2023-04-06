package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class EmpresaobraconceptoPK implements Serializable {
    private int empresaconstructoraId;
    private int conceptosgastoId;
    private int obraId;

    @Column(name = "empresaconstructora_id")
    @Id
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Column(name = "conceptosgasto_id")
    @Id
    public int getConceptosgastoId() {
        return conceptosgastoId;
    }

    public void setConceptosgastoId(int conceptosgastoId) {
        this.conceptosgastoId = conceptosgastoId;
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
        EmpresaobraconceptoPK that = (EmpresaobraconceptoPK) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                conceptosgastoId == that.conceptosgastoId &&
                obraId == that.obraId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(empresaconstructoraId, conceptosgastoId, obraId);
    }
}
