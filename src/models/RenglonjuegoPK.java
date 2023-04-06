package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class RenglonjuegoPK implements Serializable {
    private int renglonvarianteId;
    private int juegoproductoId;

    @Column(name = "renglonvariante__id")
    @Id
    public int getRenglonvarianteId() {
        return renglonvarianteId;
    }

    public void setRenglonvarianteId(int renglonvarianteId) {
        this.renglonvarianteId = renglonvarianteId;
    }

    @Column(name = "juegoproducto__id")
    @Id
    public int getJuegoproductoId() {
        return juegoproductoId;
    }

    public void setJuegoproductoId(int juegoproductoId) {
        this.juegoproductoId = juegoproductoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenglonjuegoPK that = (RenglonjuegoPK) o;
        return renglonvarianteId == that.renglonvarianteId &&
                juegoproductoId == that.juegoproductoId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(renglonvarianteId, juegoproductoId);
    }
}
