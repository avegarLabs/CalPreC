package models;

public class DatosCuantitativaModelCert implements Comparable<DatosCuantitativaModelCert> {
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
    public Integer idSubesp;
    public String subespecialidad;
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
    public String unidad;
    public String brigada;
    public String grupo;
    public String cuadrilla;

    public DatosCuantitativaModelCert(Integer idEmpresa, String empresa, Integer idZona, String zona, Integer idObjeto, String objeto, Integer idNivel, String nivel, Integer idEspecialidad, String especialidad, Integer idSubesp, String subespecialidad, String codigo, String descripcion, String um, String tipo, double cpo, double cpe, double cet, double otra, double cantidad, double precio, double preciomlc, double costoTotal, String unidad, String brigada, String grupo, String cuadrilla) {

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
        this.idSubesp = idSubesp;
        this.subespecialidad = subespecialidad;
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
        this.unidad = unidad;
        this.brigada = brigada;
        this.grupo = grupo;
        this.cuadrilla = cuadrilla;
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

    public Integer getIdSubesp() {
        return idSubesp;
    }

    public void setIdSubesp(Integer idSubesp) {
        this.idSubesp = idSubesp;
    }

    public String getSubespecialidad() {
        return subespecialidad;
    }

    public void setSubespecialidad(String subespecialidad) {
        this.subespecialidad = subespecialidad;
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

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
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

    @Override
    public int compareTo(DatosCuantitativaModelCert o) {

        if (idEmpresa != null) {
            if (o.getIdEmpresa() > idEmpresa) {
                return -1;
            } else if (o.getIdEmpresa() > idEmpresa) {
                return 0;
            } else {
                return 1;
            }
        } else if (idZona != null) {
            if (o.getIdZona() > idZona) {
                return -1;
            } else if (o.getIdZona() > idZona) {
                return 0;
            } else {
                return 1;
            }
        } else if (idObjeto != null) {
            if (o.getIdObjeto() > idObjeto) {
                return -1;
            } else if (o.getIdObjeto() > idObjeto) {
                return 0;
            } else {
                return 1;
            }
        } else if (idNivel != null) {
            if (o.getIdNivel() > idNivel) {
                return -1;
            } else if (o.getIdNivel() > idNivel) {
                return 0;
            } else {
                return 1;
            }
        } else if (idEspecialidad != null) {
            if (o.getIdEspecialidad() > idEspecialidad) {
                return -1;
            } else if (o.getIdEspecialidad() > idEspecialidad) {
                return 0;
            } else {
                return 1;
            }
        } else if (idSubesp != null) {
            if (o.getIdSubesp() > idSubesp) {
                return -1;
            } else if (o.getIdSubesp() > idSubesp) {
                return 0;
            } else {
                return 1;
            }
        }
        return 1;
    }
}



