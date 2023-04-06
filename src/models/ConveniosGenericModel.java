package models;

import java.util.Objects;

public class ConveniosGenericModel {

    public int idEmp;
    public String empresa;
    public int idBrig;
    public String brigada;
    public int idGrup;
    public String grupo;
    public int idCuad;
    public String Cuadrilla;

    public ConveniosGenericModel(int idEmp, String empresa, int idBrig, String brigada, int idGrup, String grupo, int idCuad, String cuadrilla) {
        this.idEmp = idEmp;
        this.empresa = empresa;
        this.idBrig = idBrig;
        this.brigada = brigada;
        this.idGrup = idGrup;
        this.grupo = grupo;
        this.idCuad = idCuad;
        Cuadrilla = cuadrilla;
    }

    public int getIdEmp() {
        return idEmp;
    }

    public void setIdEmp(int idEmp) {
        this.idEmp = idEmp;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public int getIdBrig() {
        return idBrig;
    }

    public void setIdBrig(int idBrig) {
        this.idBrig = idBrig;
    }

    public String getBrigada() {
        return brigada;
    }

    public void setBrigada(String brigada) {
        this.brigada = brigada;
    }

    public int getIdGrup() {
        return idGrup;
    }

    public void setIdGrup(int idGrup) {
        this.idGrup = idGrup;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getIdCuad() {
        return idCuad;
    }

    public void setIdCuad(int idCuad) {
        this.idCuad = idCuad;
    }

    public String getCuadrilla() {
        return Cuadrilla;
    }

    public void setCuadrilla(String cuadrilla) {
        Cuadrilla = cuadrilla;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConveniosGenericModel that = (ConveniosGenericModel) o;
        return idEmp == that.idEmp &&
                idBrig == that.idBrig &&
                idGrup == that.idGrup &&
                idCuad == that.idCuad &&
                Objects.equals(empresa, that.empresa) &&
                Objects.equals(brigada, that.brigada) &&
                Objects.equals(grupo, that.grupo) &&
                Objects.equals(Cuadrilla, that.Cuadrilla);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmp, empresa, idBrig, brigada, idGrup, grupo, idCuad, Cuadrilla);
    }

}
