package AccessMigration.model;

public class UnidadesObraPCW {
    public String code;
    public String descrip;
    public String um;
    public double cant;
    public String rb;
    public double costMaterial;
    public double costEquipo;
    public double costMano;
    public double salario;
    public double salarioCUC;

    public UnidadesObraPCW(String code, String descrip, String um, double cant, String rb, double costMaterial, double costEquipo, double costMano, double salario, double salarioCUC) {
        this.code = code;
        this.descrip = descrip;
        this.um = um;
        this.cant = cant;
        this.rb = rb;
        this.costMaterial = costMaterial;
        this.costEquipo = costEquipo;
        this.costMano = costMano;
        this.salario = salario;
        this.salarioCUC = salarioCUC;
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

    public double getCant() {
        return cant;
    }

    public void setCant(double cant) {
        this.cant = cant;
    }

    public String getRb() {
        return rb;
    }

    public void setRb(String rb) {
        this.rb = rb;
    }

    public double getCostMaterial() {
        return costMaterial;
    }

    public void setCostMaterial(double costMaterial) {
        this.costMaterial = costMaterial;
    }

    public double getCostEquipo() {
        return costEquipo;
    }

    public void setCostEquipo(double costEquipo) {
        this.costEquipo = costEquipo;
    }

    public double getCostMano() {
        return costMano;
    }

    public void setCostMano(double costMano) {
        this.costMano = costMano;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public double getSalarioCUC() {
        return salarioCUC;
    }

    public void setSalarioCUC(double salarioCUC) {
        this.salarioCUC = salarioCUC;
    }
}
