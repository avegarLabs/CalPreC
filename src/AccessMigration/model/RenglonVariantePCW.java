package AccessMigration.model;

public class RenglonVariantePCW {

    public String empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subEspecialidad;
    public String codeUO;
    public String codeRV;
    public Double cantidad;
    public Double costMaterial;
    public Double costMano;
    public Double costEquipo;
    public Double salario;
    public Double salariocuc;

    public RenglonVariantePCW(String empresa, String zona, String objeto, String nivel, String especialidad, String subEspecialidad, String codeUO, String codeRV, Double cantidad, Double costMaterial, Double costMano, Double costEquipo, Double salario, Double salariocuc) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subEspecialidad = subEspecialidad;
        this.codeUO = codeUO;
        this.codeRV = codeRV;
        this.cantidad = cantidad;
        this.costMaterial = costMaterial;
        this.costMano = costMano;
        this.costEquipo = costEquipo;
        this.salario = salario;
        this.salariocuc = salariocuc;
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

    public String getSubEspecialidad() {
        return subEspecialidad;
    }

    public void setSubEspecialidad(String subEspecialidad) {
        this.subEspecialidad = subEspecialidad;
    }

    public String getCodeUO() {
        return codeUO;
    }

    public void setCodeUO(String codeUO) {
        this.codeUO = codeUO;
    }

    public String getCodeRV() {
        return codeRV;
    }

    public void setCodeRV(String codeRV) {
        this.codeRV = codeRV;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getCostMaterial() {
        return costMaterial;
    }

    public void setCostMaterial(Double costMaterial) {
        this.costMaterial = costMaterial;
    }

    public Double getCostMano() {
        return costMano;
    }

    public void setCostMano(Double costMano) {
        this.costMano = costMano;
    }

    public Double getCostEquipo() {
        return costEquipo;
    }

    public void setCostEquipo(Double costEquipo) {
        this.costEquipo = costEquipo;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Double getSalariocuc() {
        return salariocuc;
    }

    public void setSalariocuc(Double salariocuc) {
        this.salariocuc = salariocuc;
    }
}
