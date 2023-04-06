package models;

public class CartaLimiteModelView {

    private int id;
    private String code;
    private String descrip;
    private String um;
    private Double cant;
    private Double disp;
    private Double cost;
    private String tipo;
    private double precio;

    public CartaLimiteModelView(int id, String code, String descrip, String um, Double cant, Double disp, Double cost, String tipo, double precio) {
        this.id = id;
        this.code = code;
        this.descrip = descrip;
        this.um = um;
        this.cant = cant;
        this.disp = disp;
        this.cost = cost;
        this.tipo = tipo;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public Double getCant() {
        return cant;
    }

    public void setCant(Double cant) {
        this.cant = cant;
    }

    public Double getDisp() {
        return disp;
    }

    public void setDisp(Double disp) {
        this.disp = disp;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
