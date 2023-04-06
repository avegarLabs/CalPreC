package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class EspecialidadnivelPK implements Serializable {
    private int especialidadesId;
    private int nivelId;

    @Column(name = "especialidades__id", nullable = false)
    @Id
    public int getEspecialidadesId() {
        return especialidadesId;
    }

    public void setEspecialidadesId(int especialidadesId) {
        this.especialidadesId = especialidadesId;
    }

    @Column(name = "nivel__id", nullable = false)
    @Id
    public int getNivelId() {
        return nivelId;
    }

    public void setNivelId(int nivelId) {
        this.nivelId = nivelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EspecialidadnivelPK that = (EspecialidadnivelPK) o;
        return especialidadesId == that.especialidadesId &&
                nivelId == that.nivelId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(especialidadesId, nivelId);
    }
}
