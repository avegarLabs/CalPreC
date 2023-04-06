package models;

public class ControlPresupuestoUORVView {

    public String empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subEspecialidad;
    public String uo;
    public String descrip;
    public String um;
    public Double cant;
    public String rvCode;
    public String rvDescrip;
    public String rvUm;
    public Double rvCantidad;
    public Double total;
    public Double hh;
    public Double salario;
    public Double avance;
    public Double valorEjecutado;
    public Double porEjecutarCost;
    public Double porEjecutarHH;
    public Double porEjecutarSalario;
    public Double porciento;

    public ControlPresupuestoUORVView(String empresa, String zona, String objeto, String nivel, String especialidad, String subEspecialidad, String uo, String descrip, String um, Double cant, String rvCode, String rvDescrip, String rvUm, Double rvCantidad, Double total, Double hh, Double salario, Double avance, Double valorEjecutado, Double porEjecutarCost, Double porEjecutarHH, Double porEjecutarSalario, Double porciento) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subEspecialidad = subEspecialidad;
        this.uo = uo;
        this.descrip = descrip;
        this.um = um;
        this.cant = cant;
        this.rvCode = rvCode;
        this.rvDescrip = rvDescrip;
        this.rvUm = rvUm;
        this.rvCantidad = rvCantidad;
        this.total = total;
        this.hh = hh;
        this.salario = salario;
        this.avance = avance;
        this.valorEjecutado = valorEjecutado;
        this.porEjecutarCost = porEjecutarCost;
        this.porEjecutarHH = porEjecutarHH;
        this.porEjecutarSalario = porEjecutarSalario;
        this.porciento = porciento;
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

    public String getUo() {
        return uo;
    }

    public void setUo(String uo) {
        this.uo = uo;
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

    public Double getCant() {
        return cant;
    }

    public void setCant(Double cant) {
        this.cant = cant;
    }

    public String getRvCode() {
        return rvCode;
    }

    public void setRvCode(String rvCode) {
        this.rvCode = rvCode;
    }

    public String getRvDescrip() {
        return rvDescrip;
    }

    public void setRvDescrip(String rvDescrip) {
        this.rvDescrip = rvDescrip;
    }

    public String getRvUm() {
        return rvUm;
    }

    public void setRvUm(String rvUm) {
        this.rvUm = rvUm;
    }

    public Double getRvCantidad() {
        return rvCantidad;
    }

    public void setRvCantidad(Double rvCantidad) {
        this.rvCantidad = rvCantidad;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getHh() {
        return hh;
    }

    public void setHh(Double hh) {
        this.hh = hh;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Double getAvance() {
        return avance;
    }

    public void setAvance(Double avance) {
        this.avance = avance;
    }

    public Double getValorEjecutado() {
        return valorEjecutado;
    }

    public void setValorEjecutado(Double valorEjecutado) {
        this.valorEjecutado = valorEjecutado;
    }

    public Double getPorEjecutarCost() {
        return porEjecutarCost;
    }

    public void setPorEjecutarCost(Double porEjecutarCost) {
        this.porEjecutarCost = porEjecutarCost;
    }

    public Double getPorEjecutarHH() {
        return porEjecutarHH;
    }

    public void setPorEjecutarHH(Double porEjecutarHH) {
        this.porEjecutarHH = porEjecutarHH;
    }

    public Double getPorEjecutarSalario() {
        return porEjecutarSalario;
    }

    public void setPorEjecutarSalario(Double porEjecutarSalario) {
        this.porEjecutarSalario = porEjecutarSalario;
    }

    public Double getPorciento() {
        return porciento;
    }

    public void setPorciento(Double porciento) {
        this.porciento = porciento;
    }
}
