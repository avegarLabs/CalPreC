package models;

public class ParaBajoEspecificacionCuantitativaModel {
    //rec.codigo, rec.descripcion, rec.um, rec.tipo, SUM(rvr.cantidas * uor.cantRv), rec.peso, rec.preciomn
    public Integer id;
    public String codigo;
    public String descripcion;
    public String um;
    public String tipo;
    public Double cant;
    public Double precio;
    public Double costo;

    public ParaBajoEspecificacionCuantitativaModel(Integer id, String codigo, String descripcion, String um, String tipo, Double cant, Double precio, Double costo) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.um = um;
        this.tipo = tipo;
        this.cant = cant;
        this.precio = precio;
        this.costo = costo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }
}
