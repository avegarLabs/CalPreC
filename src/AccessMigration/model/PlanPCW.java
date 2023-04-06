package AccessMigration.model;

public class PlanPCW {

    public String empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subEspecialidad;
    public String codeUO;
    public String brigada;
    public String grupo;
    public String cuadrilla;
    public String desde;
    public String hasta;
    public double cantidda;
    public double costomaterial;
    public double costomano;
    public double costoequipo;
    public double salario;
    public double salariocuc;


    public PlanPCW(String empresa, String zona, String objeto, String nivel, String especialidad, String subEspecialidad, String codeUO, String brigada, String grupo, String cuadrilla, String desde, String hasta, double cantidda, double costomaterial, double costomano, double costoequipo, double salario, double salariocuc) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subEspecialidad = subEspecialidad;
        this.codeUO = codeUO;
        this.brigada = brigada;
        this.grupo = grupo;
        this.cuadrilla = cuadrilla;
        this.desde = desde;
        this.hasta = hasta;
        this.cantidda = cantidda;
        this.costomaterial = costomaterial;
        this.costomano = costomano;
        this.costoequipo = costoequipo;
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

    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
    }

    public double getCantidda() {
        return cantidda;
    }

    public void setCantidda(double cantidda) {
        this.cantidda = cantidda;
    }

    public double getCostomaterial() {
        return costomaterial;
    }

    public void setCostomaterial(double costomaterial) {
        this.costomaterial = costomaterial;
    }

    public double getCostomano() {
        return costomano;
    }

    public void setCostomano(double costomano) {
        this.costomano = costomano;
    }

    public double getCostoequipo() {
        return costoequipo;
    }

    public void setCostoequipo(double costoequipo) {
        this.costoequipo = costoequipo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public double getSalariocuc() {
        return salariocuc;
    }

    public void setSalariocuc(double salariocuc) {
        this.salariocuc = salariocuc;
    }
}
