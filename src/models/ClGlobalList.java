package models;

import java.util.List;

public class ClGlobalList {

    public String obra;
    public String empresa;
    public String zona;
    public String objeto;
    public String especialidad;
    public List<ClPrintModel> clPrintModelList;

    public ClGlobalList(String obra, String empresa, String zona, String objeto, String especialidad, List<ClPrintModel> clPrintModelList) {
        this.obra = obra;
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.especialidad = especialidad;
        this.clPrintModelList = clPrintModelList;
    }

    public String getObra() {
        return obra;
    }

    public void setObra(String obra) {
        this.obra = obra;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public List<ClPrintModel> getClPrintModelList() {
        return clPrintModelList;
    }

    public void setClPrintModelList(List<ClPrintModel> clPrintModelList) {
        this.clPrintModelList = clPrintModelList;
    }
}