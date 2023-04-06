package models;

public class FondoHorarioExplotacionModel {

    public Integer idEmpresa;
    public String code;
    public String descrip;
    public double fhpp;
    public double tarifa;
    public double salario;
    public double cpo;
    public double cpe;
    public double cet;
    public double otra;
    public double fhe;
    public double fhs;

    public FondoHorarioExplotacionModel(Integer idEmpresa, String code, String descrip, double fhpp, double tarifa, double salario, double cpo, double cpe, double cet, double otra, double fhe, double fhs) {
        this.idEmpresa = idEmpresa;
        this.code = code;
        this.descrip = descrip;
        this.fhpp = fhpp;
        this.tarifa = tarifa;
        this.salario = salario;
        this.cpo = cpo;
        this.cpe = cpe;
        this.cet = cet;
        this.otra = otra;
        this.fhe = fhe;
        this.fhs = fhs;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
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

    public double getFhpp() {
        return fhpp;
    }

    public void setFhpp(double fhpp) {
        this.fhpp = fhpp;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
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

    public double getFhe() {
        return fhe;
    }

    public void setFhe(double fhe) {
        this.fhe = fhe;
    }

    public double getFhs() {
        return fhs;
    }

    public void setFhs(double fhs) {
        this.fhs = fhs;
    }
}
