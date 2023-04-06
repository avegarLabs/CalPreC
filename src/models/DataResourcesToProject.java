package models;

public class DataResourcesToProject {

    public String code;
    public String descrip;
    public Double precio;
    public String tipo;

    public DataResourcesToProject(String code, String descrip, Double precio, String tipo) {
        this.code = code;
        this.descrip = descrip;
        this.precio = precio;
        this.tipo = tipo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
