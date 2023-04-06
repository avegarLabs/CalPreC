package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class EmpresaobraPK implements Serializable {
    private int empresaconstructoraId;
    private int obraId;

    @Column(name = "empresaconstructora__id", nullable = false)
    @Id
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Column(name = "obra__id", nullable = false)
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
        EmpresaobraPK that = (EmpresaobraPK) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                obraId == that.obraId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(empresaconstructoraId, obraId);
    }
}
