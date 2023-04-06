package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class SemielaboradosrecursosPK implements Serializable {
    private int suministrossemielaboradosId;
    private int recursosId;

    @Column(name = "suministrossemielaborados__id", nullable = false)
    @Id
    public int getSuministrossemielaboradosId() {
        return suministrossemielaboradosId;
    }

    public void setSuministrossemielaboradosId(int suministrossemielaboradosId) {
        this.suministrossemielaboradosId = suministrossemielaboradosId;
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
        SemielaboradosrecursosPK that = (SemielaboradosrecursosPK) o;
        return suministrossemielaboradosId == that.suministrossemielaboradosId &&
                recursosId == that.recursosId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(suministrossemielaboradosId, recursosId);
    }
}
