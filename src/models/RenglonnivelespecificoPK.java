package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class RenglonnivelespecificoPK implements Serializable {
    private int renglonvarianteId;
    private int nivelespecificoId;

    @Column(name = "renglonvariante__id")
    @Id
    public int getRenglonvarianteId() {
        return renglonvarianteId;
    }

    public void setRenglonvarianteId(int renglonvarianteId) {
        this.renglonvarianteId = renglonvarianteId;
    }

    @Column(name = "nivelespecifico__id")
    @Id
    public int getNivelespecificoId() {
        return nivelespecificoId;
    }

    public void setNivelespecificoId(int nivelespecificoId) {
        this.nivelespecificoId = nivelespecificoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenglonnivelespecificoPK that = (RenglonnivelespecificoPK) o;
        return renglonvarianteId == that.renglonvarianteId &&
                nivelespecificoId == that.nivelespecificoId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(renglonvarianteId, nivelespecificoId);
    }
}
