package models;

public class UOReportModel {

    public int idEmp;
    public String empresa;
    public int idZona;
    public String zona;
    public int idObj;
    public String objeto;
    public int idONiv;
    public String nivel;
    public int idEsp;
    public String especialidad;
    public int idSub;
    public String subespacialidad;
    public String code;
    public String descripcion;
    public String um;
    public double cantidad;
    public double costoUnitario;
    public double costoTotal;
    public double costoMaterial;
    public double costoMano;
    public double costEquipo;

    public UOReportModel(int idEmp, String empresa, int idZona, String zona, int idObj, String objeto, int idONiv, String nivel, int idEsp, String especialidad, int idSub, String subespacialidad, String code, String descripcion, String um, double cantidad, double costoUnitario, double costoTotal, double costoMaterial, double costoMano, double costEquipo) {
        this.idEmp = idEmp;
        this.empresa = empresa;
        this.idZona = idZona;
        this.zona = zona;
        this.idObj = idObj;
        this.objeto = objeto;
        this.idONiv = idONiv;
        this.nivel = nivel;
        this.idEsp = idEsp;
        this.especialidad = especialidad;
        this.idSub = idSub;
        this.subespacialidad = subespacialidad;
        this.code = code;
        this.descripcion = descripcion;
        this.um = um;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.costoTotal = costoTotal;
        this.costoMaterial = costoMaterial;
        this.costoMano = costoMano;
        this.costEquipo = costEquipo;
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

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public int getIdObj() {
        return idObj;
    }

    public void setIdObj(int idObj) {
        this.idObj = idObj;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public int getIdONiv() {
        return idONiv;
    }

    public void setIdONiv(int idONiv) {
        this.idONiv = idONiv;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public int getIdEsp() {
        return idEsp;
    }

    public void setIdEsp(int idEsp) {
        this.idEsp = idEsp;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public int getIdSub() {
        return idSub;
    }

    public void setIdSub(int idSub) {
        this.idSub = idSub;
    }

    public String getSubespacialidad() {
        return subespacialidad;
    }

    public void setSubespacialidad(String subespacialidad) {
        this.subespacialidad = subespacialidad;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public double getCostoMaterial() {
        return costoMaterial;
    }

    public void setCostoMaterial(double costoMaterial) {
        this.costoMaterial = costoMaterial;
    }

    public double getCostoMano() {
        return costoMano;
    }

    public void setCostoMano(double costoMano) {
        this.costoMano = costoMano;
    }

    public double getCostEquipo() {
        return costEquipo;
    }

    public void setCostEquipo(double costEquipo) {
        this.costEquipo = costEquipo;
    }
}






