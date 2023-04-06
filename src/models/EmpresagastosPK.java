package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class EmpresagastosPK implements Serializable {
    private int empresaconstructoraId;
    private int conceptosgastoId;

    @Column(name = "empresaconstructora__id", nullable = false)
    @Id
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Column(name = "conceptosgasto__id", nullable = false)
    @Id
    public int getConceptosgastoId() {
        return conceptosgastoId;
    }

    public void setConceptosgastoId(int conceptosgastoId) {
        this.conceptosgastoId = conceptosgastoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpresagastosPK that = (EmpresagastosPK) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                conceptosgastoId == that.conceptosgastoId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(empresaconstructoraId, conceptosgastoId);
    }
}
