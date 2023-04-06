package models;

public class RecursosPendientes {


    public String empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subespecialidad;
    public String codigoUO;
    public String descripcionUO;
    public String umUO;
    public String tipo;
    public Double cantPre;
    public Double cant;
    public Double precio;
    public Double monto;

    public RecursosPendientes(String empresa, String zona, String objeto, String nivel, String especialidad, String subespecialidad, String codigoUO, String descripcionUO, String umUO, String tipo, Double cantPre, Double cant, Double precio, Double monto) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subespecialidad = subespecialidad;
        this.codigoUO = codigoUO;
        this.descripcionUO = descripcionUO;
        this.umUO = umUO;
        this.tipo = tipo;
        this.cantPre = cantPre;
        this.cant = cant;
        this.precio = precio;
        this.monto = monto;
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

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getSubespecialidad() {
        return subespecialidad;
    }

    public void setSubespecialidad(String subespecialidad) {
        this.subespecialidad = subespecialidad;
    }

    public String getCodigoUO() {
        return codigoUO;
    }

    public void setCodigoUO(String codigoUO) {
        this.codigoUO = codigoUO;
    }

    public String getDescripcionUO() {
        return descripcionUO;
    }

    public void setDescripcionUO(String descripcionUO) {
        this.descripcionUO = descripcionUO;
    }

    public String getUmUO() {
        return umUO;
    }

    public void setUmUO(String umUO) {
        this.umUO = umUO;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getCantPre() {
        return cantPre;
    }

    public void setCantPre(Double cantPre) {
        this.cantPre = cantPre;
    }

    public Double getCant() {
        return cant;
    }

    public void setCant(Double cant) {
        this.cant = cant;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
