package models;

public class DatosCuantitativaUOGRUPModel implements Comparable<DatosCuantitativaUOGRUPModel> {
    public Integer idEmpresa;
    public String empresa;
    public Integer idZona;
    public String zona;
    public Integer idObjeto;
    public String objeto;
    public Integer idNivel;
    public String nivel;
    public Integer idEspecialidad;
    public String especialidad;
    public String subespecialidad;
    public Integer idSubesp;
    public Integer idUO;
    public String codeUO;
    public String descripUO;
    public String umUO;
    public Double cantUO;
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

    public DatosCuantitativaUOGRUPModel(Integer idEmpresa, String empresa, Integer idZona, String zona, Integer idObjeto, String objeto, Integer idNivel, String nivel, Integer idEspecialidad, String especialidad, String subespecialidad, Integer idSubesp, Integer idUO, String codeUO, String descripUO, String umUO, Double cantUO, String codigo, String descripcion, String um, String tipo, double cpo, double cpe, double cet, double otra, double cantidad, double precio, double preciomlc, double costoTotal) {
        this.idEmpresa = idEmpresa;
        this.empresa = empresa;
        this.idZona = idZona;
        this.zona = zona;
        this.idObjeto = idObjeto;
        this.objeto = objeto;
        this.idNivel = idNivel;
        this.nivel = nivel;
        this.idEspecialidad = idEspecialidad;
        this.especialidad = especialidad;
        this.subespecialidad = subespecialidad;
        this.idSubesp = idSubesp;
        this.idUO = idUO;
        this.codeUO = codeUO;
        this.descripUO = descripUO;
        this.umUO = umUO;
        this.cantUO = cantUO;
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

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public Integer getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Integer idObjeto) {
        this.idObjeto = idObjeto;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public Integer getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Integer idNivel) {
        this.idNivel = idNivel;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(Integer idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
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

    public Integer getIdSubesp() {
        return idSubesp;
    }

    public void setIdSubesp(Integer idSubesp) {
        this.idSubesp = idSubesp;
    }

    public Integer getIdUO() {
        return idUO;
    }

    public void setIdUO(Integer idUO) {
        this.idUO = idUO;
    }

    public String getCodeUO() {
        return codeUO;
    }

    public void setCodeUO(String codeUO) {
        this.codeUO = codeUO;
    }

    public String getDescripUO() {
        return descripUO;
    }

    public void setDescripUO(String descripUO) {
        this.descripUO = descripUO;
    }

    public String getUmUO() {
        return umUO;
    }

    public void setUmUO(String umUO) {
        this.umUO = umUO;
    }

    public Double getCantUO() {
        return cantUO;
    }

    public void setCantUO(Double cantUO) {
        this.cantUO = cantUO;
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
    public int compareTo(DatosCuantitativaUOGRUPModel o) {
        if (o.getIdUO() > idUO) {
            return -1;
        } else if (o.getIdUO() > idUO) {
            return 0;
        } else {
            return 1;
        }
    }


}



