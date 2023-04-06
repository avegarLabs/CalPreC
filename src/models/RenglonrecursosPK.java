package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class RenglonrecursosPK implements Serializable {
    private int renglonvarianteId;
    private int recursosId;

    @Column(name = "renglonvariante__id", nullable = false)
    @Id
    public int getRenglonvarianteId() {
        return renglonvarianteId;
    }

    public void setRenglonvarianteId(int renglonvarianteId) {
        this.renglonvarianteId = renglonvarianteId;
    }

    @Column(name = "recursos__id", nullable = false)
    @Id
    public int getRecursosId() {
        return recursosId;
    }

    public void setRecursosId(int recursosId) {
        this.recursosId = recursosId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenglonrecursosPK that = (RenglonrecursosPK) o;
        return renglonvarianteId == that.renglonvarianteId &&
                recursosId == that.recursosId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(renglonvarianteId, recursosId);
    }
}
