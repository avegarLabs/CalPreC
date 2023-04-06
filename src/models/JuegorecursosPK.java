package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class JuegorecursosPK implements Serializable {
    private int juegoproductoId;
    private int recursosId;

    @Column(name = "juegoproducto__id", nullable = false)
    @Id
    public int getJuegoproductoId() {
        return juegoproductoId;
    }

    public void setJuegoproductoId(int juegoproductoId) {
        this.juegoproductoId = juegoproductoId;
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
        JuegorecursosPK that = (JuegorecursosPK) o;
        return juegoproductoId == that.juegoproductoId &&
                recursosId == that.recursosId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(juegoproductoId, recursosId);
    }
}
