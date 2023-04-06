package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class ComponentesRV extends RecursiveTreeObject<ComponentesRV> {
    public int id;
    public String codigo;
    public String descripcion;
    public String um;
    public Double preciomn;
    public Double cantidad;
    public Double uso;
    public String tag;
    public String tipo;
    public Double peso;
    public String tarifa;

    public ComponentesRV(int id, String codigo, String descripcion, String um, Double preciomn, Double cantidad, Double uso, String tag, String tipo, Double peso, String tarifa) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.um = um;
        this.preciomn = preciomn;
        this.cantidad = cantidad;
        this.uso = uso;
        this.tag = tag;
        this.tipo = tipo;
        this.peso = peso;
        this.tarifa = tarifa;
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

    public Double getPreciomn() {
        return preciomn;
    }

    public void setPreciomn(Double preciomn) {
        this.preciomn = preciomn;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getUso() {
        return uso;
    }

    public void setUso(Double uso) {
        this.uso = uso;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }
}
