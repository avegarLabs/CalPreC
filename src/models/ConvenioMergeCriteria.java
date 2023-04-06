package models;

import java.util.Objects;

public class ConvenioMergeCriteria {
    public int idBrig;
    public int idGrup;
    public int idCuad;
    public String codeUO;

    public ConvenioMergeCriteria(int idBrig, int idGrup, int idCuad, String codeUO) {
        this.idBrig = idBrig;
        this.idGrup = idGrup;
        this.idCuad = idCuad;
        this.codeUO = codeUO;
    }

    public int getIdBrig() {
        return idBrig;
    }

    public void setIdBrig(int idBrig) {
        this.idBrig = idBrig;
    }

    public int getIdGrup() {
        return idGrup;
    }

    public void setIdGrup(int idGrup) {
        this.idGrup = idGrup;
    }

    public int getIdCuad() {
        return idCuad;
    }

    public void setIdCuad(int idCuad) {
        this.idCuad = idCuad;
    }

    public String getCodeUO() {
        return codeUO;
    }

    public void setCodeUO(String codeUO) {
        this.codeUO = codeUO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConvenioMergeCriteria that = (ConvenioMergeCriteria) o;
        return idBrig == that.idBrig && idGrup == that.idGrup && idCuad == that.idCuad && Objects.equals(codeUO, that.codeUO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBrig, idGrup, idCuad, codeUO);
    }
}
