package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class UnidadobrarenglonPK implements Serializable {
    private int unidadobraId;
    private int renglonvarianteId;

    @Column(name = "unidadobra__id", nullable = false)
    @Id
    public int getUnidadobraId() {
        return unidadobraId;
    }

    public void setUnidadobraId(int unidadobraId) {
        this.unidadobraId = unidadobraId;
    }

    @Column(name = "renglonvariante__id", nullable = false)
    @Id
    public int getRenglonvarianteId() {
        return renglonvarianteId;
    }

    public void setRenglonvarianteId(int renglonvarianteId) {
        this.renglonvarianteId = renglonvarianteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnidadobrarenglonPK that = (UnidadobrarenglonPK) o;
        return unidadobraId == that.unidadobraId &&
                renglonvarianteId == that.renglonvarianteId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(unidadobraId, renglonvarianteId);
    }
}
