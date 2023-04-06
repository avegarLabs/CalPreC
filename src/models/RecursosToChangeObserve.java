package models;

public class RecursosToChangeObserve {

    public int id;
    public String codigo;
    public String descrip;
    public String um;
    public double precio;
    public String tipo;

    public RecursosToChangeObserve(int id, String codigo, String descrip, String um, double precio, String tipo) {
        this.id = id;
        this.codigo = codigo;
        this.descrip = descrip;
        this.um = um;
        this.precio = precio;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
