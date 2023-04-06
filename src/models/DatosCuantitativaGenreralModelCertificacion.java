package models;

import java.util.Objects;

public class DatosCuantitativaGenreralModelCertificacion {

    public Integer empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subespecialidad;
    public String brigada;
    public String grupo;
    public String cuadrilla;
    public String codigo;
    public String descripcion;
    public String um;
    public String tipo;
    public double cpo;
    public double cpe;
    public double cet;
    public double otra;
    public double cantidad;
    public double precio;
    public double preciomlc;
    public double costoTotal;

    public DatosCuantitativaGenreralModelCertificacion(Integer empresa, String zona, String objeto, String nivel, String especialidad, String subespecialidad, String brigada, String grupo, String cuadrilla, String codigo, String descripcion, String um, String tipo, double cpo, double cpe, double cet, double otra, double cantidad, double precio, double preciomlc, double costoTotal) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subespecialidad = subespecialidad;
        this.brigada = brigada;
        this.grupo = grupo;
        this.cuadrilla = cuadrilla;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.um = um;
        this.tipo = tipo;
        this.cpo = cpo;
        this.cpe = cpe;
        this.cet = cet;
        this.otra = otra;
        this.cantidad = cantidad;
        this.precio = precio;
        this.preciomlc = preciomlc;
        this.costoTotal = costoTotal;
    }

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
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

    public String getBrigada() {
        return brigada;
    }

    public void setBrigada(String brigada) {
        this.brigada = brigada;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getCuadrilla() {
        return cuadrilla;
    }

    public void setCuadrilla(String cuadrilla) {
        this.cuadrilla = cuadrilla;
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

    public double getCpo() {
        return cpo;
    }

    public void setCpo(double cpo) {
        this.cpo = cpo;
    }

    public double getCpe() {
        return cpe;
    }

    public void setCpe(double cpe) {
        this.cpe = cpe;
    }

    public double getCet() {
        return cet;
    }

    public void setCet(double cet) {
        this.cet = cet;
    }

    public double getOtra() {
        return otra;
    }

    public void setOtra(double otra) {
        this.otra = otra;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPreciomlc() {
        return preciomlc;
    }

    public void setPreciomlc(double preciomlc) {
        this.preciomlc = preciomlc;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatosCuantitativaGenreralModelCertificacion that = (DatosCuantitativaGenreralModelCertificacion) o;
        return Objects.equals(empresa, that.empresa) &&
                Objects.equals(zona, that.zona) &&
                Objects.equals(objeto, that.objeto) &&
                Objects.equals(nivel, that.nivel) &&
                Objects.equals(especialidad, that.especialidad) &&
                Objects.equals(subespecialidad, that.subespecialidad) &&
                Objects.equals(brigada, that.brigada) &&
                Objects.equals(grupo, that.grupo) &&
                Objects.equals(cuadrilla, that.cuadrilla) &&
                Objects.equals(codigo, that.codigo);
    }


}