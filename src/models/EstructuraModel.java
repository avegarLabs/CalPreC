package models;

public class EstructuraModel {
    public Integer idEmpresa;
    public Integer idZona;
    public Integer idObjeto;
    public Integer idNivel;
    public Integer idEspecialidad;
    public Integer idSubespecialidad;

    public EstructuraModel(Integer idEmpresa, Integer idZona, Integer idObjeto, Integer idNivel, Integer idEspecialidad, Integer idSubespecialidad) {
        this.idEmpresa = idEmpresa;
        this.idZona = idZona;
        this.idObjeto = idObjeto;
        this.idNivel = idNivel;
        this.idEspecialidad = idEspecialidad;
        this.idSubespecialidad = idSubespecialidad;
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
}
