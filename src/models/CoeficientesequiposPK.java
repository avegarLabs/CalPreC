package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class CoeficientesequiposPK implements Serializable {
    private int empresaconstructoraId;
    private int recursosId;
    private int obraId;

    @Column(name = "empresaconstructora_id")
    @Id
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Column(name = "recursos_id")
    @Id
    public int getRecursosId() {
        return recursosId;
    }

    public void setRecursosId(int recursosId) {
        this.recursosId = recursosId;
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
        CoeficientesequiposPK that = (CoeficientesequiposPK) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                recursosId == that.recursosId &&
                obraId == that.obraId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empresaconstructoraId, recursosId, obraId);
    }
}
