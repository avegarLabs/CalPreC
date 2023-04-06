package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(EmpresaobraconceptoscoeficientesPK.class)
public class Empresaobraconceptoscoeficientes {
    private int empresaconstructoraId;
    private int conceptosgastoId;
    private int obraId;
    private double coeficiente;

    @Id
    @Column(name = "empresaconstructora_id", nullable = false)
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Id
    @Column(name = "conceptosgasto_id", nullable = false)
    public int getConceptosgastoId() {
        return conceptosgastoId;
    }

    public void setConceptosgastoId(int conceptosgastoId) {
        this.conceptosgastoId = conceptosgastoId;
    }

    @Id
    @Column(name = "obra_id", nullable = false)
    public int getObraId() {
        return obraId;
    }

    public void setObraId(int obraId) {
        this.obraId = obraId;
    }

    @Basic
    @Column(name = "coeficiente", nullable = false, precision = 0)
    public double getCoeficiente() {
        return coeficiente;
    }

    public void setCoeficiente(double coeficiente) {
        this.coeficiente = coeficiente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresaobraconceptoscoeficientes that = (Empresaobraconceptoscoeficientes) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                conceptosgastoId == that.conceptosgastoId &&
                obraId == that.obraId &&
                Double.compare(that.coeficiente, coeficiente) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empresaconstructoraId, conceptosgastoId, obraId, coeficiente);
    }
}
