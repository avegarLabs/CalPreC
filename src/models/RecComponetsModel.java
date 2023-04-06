package models;

public class RecComponetsModel {

    public String code;
    public String desc;
    public String um;
    public Double precio;
    public Double preciomlc;
    public Double cantiADouble;
    public String tipo;
    public String grupo;

    public RecComponetsModel(String code, String desc, String um, Double precio, Double preciomlc, Double cantiADouble, String tipo, String grupo) {
        this.code = code;
        this.desc = desc;
        this.um = um;
        this.precio = precio;
        this.preciomlc = preciomlc;
        this.cantiADouble = cantiADouble;
        this.tipo = tipo;
        this.grupo = grupo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getPreciomlc() {
        return preciomlc;
    }

    public void setPreciomlc(Double preciomlc) {
        this.preciomlc = preciomlc;
    }

    public Double getCantiADouble() {
        return cantiADouble;
    }

    public void setCantiADouble(Double cantiADouble) {
        this.cantiADouble = cantiADouble;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
