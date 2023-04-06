package models;

import java.util.Objects;

public class UORVDatabyGruop {

    public Integer idEmpresa;
    public Integer id;
    public String codeUO;
    public String descrip;
    public String um;
    public Double cantidad;
    public Double total;

    public UORVDatabyGruop(Integer idEmpresa, Integer id, String codeUO, String descrip, String um, Double cantidad, Double total) {
        this.idEmpresa = idEmpresa;
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
        UORVDatabyGruop that = (UORVDatabyGruop) o;
        return Objects.equals(getIdEmpresa(), that.getIdEmpresa()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getCodeUO(), that.getCodeUO());

    }

}
