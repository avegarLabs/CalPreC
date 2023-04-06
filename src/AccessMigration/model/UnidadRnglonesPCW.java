package AccessMigration.model;

public class UnidadRnglonesPCW {

    public String empresa;
    public String zona;
    public String obj;
    public String niv;
    public String esp;
    public String sub;
    public String uo;
    public String rv;
    public double cant;
    public double cmate;
    public double cmano;
    public double cequipo;
    public double sal;
    public double salcuc;

    public UnidadRnglonesPCW(String empresa, String zona, String obj, String niv, String esp, String sub, String uo, String rv, double cant, double cmate, double cmano, double cequipo, double sal, double salcuc) {
        this.empresa = empresa;
        this.zona = zona;
        this.obj = obj;
        this.niv = niv;
        this.esp = esp;
        this.sub = sub;
        this.uo = uo;
        this.rv = rv;
        this.cant = cant;
        this.cmate = cmate;
        this.cmano = cmano;
        this.cequipo = cequipo;
        this.sal = sal;
        this.salcuc = salcuc;
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

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getNiv() {
        return niv;
    }

    public void setNiv(String niv) {
        this.niv = niv;
    }

    public String getEsp() {
        return esp;
    }

    public void setEsp(String esp) {
        this.esp = esp;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getUo() {
        return uo;
    }

    public void setUo(String uo) {
        this.uo = uo;
    }

    public String getRv() {
        return rv;
    }

    public void setRv(String rv) {
        this.rv = rv;
    }

    public double getCant() {
        return cant;
    }

    public void setCant(double cant) {
        this.cant = cant;
    }

    public double getCmate() {
        return cmate;
    }

    public void setCmate(double cmate) {
        this.cmate = cmate;
    }

    public double getCmano() {
        return cmano;
    }

    public void setCmano(double cmano) {
        this.cmano = cmano;
    }

    public double getCequipo() {
        return cequipo;
    }

    public void setCequipo(double cequipo) {
        this.cequipo = cequipo;
    }

    public double getSal() {
        return sal;
    }

    public void setSal(double sal) {
        this.sal = sal;
    }

    public double getSalcuc() {
        return salcuc;
    }

    public void setSalcuc(double salcuc) {
        this.salcuc = salcuc;
    }
}
