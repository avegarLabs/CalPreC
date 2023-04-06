package AccessMigration.model;

import java.util.Objects;

public class EstructuraPCWUnidad {

    public String empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String codSub;
    public String sub;
    public String codeUnidad;


    public EstructuraPCWUnidad(String empresa, String zona, String objeto, String nivel, String especialidad, String codSub, String sub, String codeUnidad) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.codSub = codSub;
        this.sub = sub;
        this.codeUnidad = codeUnidad;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
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

    public String getCodSub() {
        return codSub;
    }

    public void setCodSub(String codSub) {
        this.codSub = codSub;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getCodeUnidad() {
        return codeUnidad;
    }

    public void setCodeUnidad(String codeUnidad) {
        this.codeUnidad = codeUnidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstructuraPCWUnidad that = (EstructuraPCWUnidad) o;
        return Objects.equals(empresa, that.empresa) &&
                Objects.equals(zona, that.zona) &&
                Objects.equals(objeto, that.objeto) &&
                Objects.equals(nivel, that.nivel) &&
                Objects.equals(especialidad, that.especialidad) &&
                Objects.equals(codSub, that.codSub) &&
                Objects.equals(sub, that.sub) &&
                Objects.equals(codeUnidad, that.codeUnidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empresa, zona, objeto, nivel, especialidad, codSub, sub, codeUnidad);
    }
}
