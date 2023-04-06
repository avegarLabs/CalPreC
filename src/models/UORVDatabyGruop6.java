package models;

import java.util.Objects;

public class UORVDatabyGruop6 {

    public Integer idEmpresa;
    public Integer idZona;
    public Integer idObjeto;
    public Integer idNivel;
    public Integer idEspecialidad;
    public Integer idSubespecialidad;
    public Integer id;
    public String codeUO;
    public String descrip;
    public String um;
    public Double cantidad;
    public Double total;

    public UORVDatabyGruop6(Integer idEmpresa, Integer idZona, Integer idObjeto, Integer idNivel, Integer idEspecialidad, Integer idSubespecialidad, Integer id, String codeUO, String descrip, String um, Double cantidad, Double total) {
        this.idEmpresa = idEmpresa;
        this.idZona = idZona;
        this.idObjeto = idObjeto;
        this.idNivel = idNivel;
        this.idEspecialidad = idEspecialidad;
        this.idSubespecialidad = idSubespecialidad;
        this.id = id;
        this.codeUO = codeUO;
        this.descrip = descrip;
        this.um = um;
        this.cantidad = cantidad;
        this.total = total;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }

    public Integer getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Integer idObjeto) {
        this.idObjeto = idObjeto;
    }

    public Integer getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Integer idNivel) {
        this.idNivel = idNivel;
    }

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(Integer idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public Integer getIdSubespecialidad() {
        return idSubespecialidad;
    }

    public void setIdSubespecialidad(Integer idSubespecialidad) {
        this.idSubespecialidad = idSubespecialidad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodeUO() {
        return codeUO;
    }

    public void setCodeUO(String codeUO) {
        this.codeUO = codeUO;
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

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UORVDatabyGruop6 that = (UORVDatabyGruop6) o;
        return Objects.equals(getIdEmpresa(), that.getIdEmpresa()) &&
                Objects.equals(getIdZona(), that.getIdZona()) &&
                Objects.equals(getIdObjeto(), that.getIdObjeto()) &&
                Objects.equals(getIdNivel(), that.getIdNivel()) &&
                Objects.equals(getIdEspecialidad(), that.getIdEspecialidad()) &&
                Objects.equals(getIdSubespecialidad(), that.getIdSubespecialidad()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getCodeUO(), that.getCodeUO());

    }


}
