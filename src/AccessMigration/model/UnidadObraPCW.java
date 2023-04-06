package AccessMigration.model;

public class UnidadObraPCW {

    public String empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subEspecialidad;
    public String code;
    public String descrip;
    public String um;
    public Double cantidad;
    public Double costMaterial;
    public Double costMano;
    public Double costEquipo;
    public Double salario;
    public Double salariocuc;
    public String rb;

    public UnidadObraPCW(String empresa, String zona, String objeto, String nivel, String especialidad, String subEspecialidad, String code, String descrip, String um, Double cantidad, Double costMaterial, Double costMano, Double costEquipo, Double salario, Double salariocuc, String rb) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subEspecialidad = subEspecialidad;
        this.code = code;
        this.descrip = descrip;
        this.um = um;
        this.cantidad = cantidad;
        this.costMaterial = costMaterial;
        this.costMano = costMano;
        this.costEquipo = costEquipo;
        this.salario = salario;
        this.salariocuc = salariocuc;
        this.rb = rb;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getRb() {
        return rb;
    }

    public void setRb(String rb) {
        this.rb = rb;
    }
}
