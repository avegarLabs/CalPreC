package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class RenglonsemielaboradosPK implements Serializable {
    private int renglonvarianteId;
    private int suministrossemielaboradosId;

    @Column(name = "renglonvariante__id", nullable = false)
    @Id
    public int getRenglonvarianteId() {
        return renglonvarianteId;
    }

    public void setRenglonvarianteId(int renglonvarianteId) {
        this.renglonvarianteId = renglonvarianteId;
    }

    @Column(name = "suministrossemielaborados__id", nullable = false)
    @Id
    public int getSuministrossemielaboradosId() {
        return suministrossemielaboradosId;
    }

    public void setSuministrossemielaboradosId(int suministrossemielaboradosId) {
        this.suministrossemielaboradosId = suministrossemielaboradosId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenglonsemielaboradosPK that = (RenglonsemielaboradosPK) o;
        return renglonvarianteId == that.renglonvarianteId &&
                suministrossemielaboradosId == that.suministrossemielaboradosId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(renglonvarianteId, suministrossemielaboradosId);
    }
}
