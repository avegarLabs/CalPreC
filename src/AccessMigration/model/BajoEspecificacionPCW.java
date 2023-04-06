package AccessMigration.model;

import java.util.Objects;

public class BajoEspecificacionPCW {

    public String emp;
    public String zona;
    public String objet;
    public String niv;
    public String esp;
    public String sup;
    public String uo;
    public String suminist;
    public double cant;
    public double costo;

    public BajoEspecificacionPCW(String emp, String zona, String objet, String niv, String esp, String sup, String uo, String suminist, double cant, double costo) {
        this.emp = emp;
        this.zona = zona;
        this.objet = objet;
        this.niv = niv;
        this.esp = esp;
        this.sup = sup;
        this.uo = uo;
        this.suminist = suminist;
        this.cant = cant;
        this.costo = costo;
    }

    public String getEmp() {
        return emp;
    }

    public void setEmp(String emp) {
        this.emp = emp;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
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

    public String getSup() {
        return sup;
    }

    public void setSup(String sup) {
        this.sup = sup;
    }

    public String getUo() {
        return uo;
    }

    public void setUo(String uo) {
        this.uo = uo;
    }

    public String getSuminist() {
        return suminist;
    }

    public void setSuminist(String suminist) {
        this.suminist = suminist;
    }

    public double getCant() {
        return cant;
    }

    public void setCant(double cant) {
        this.cant = cant;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BajoEspecificacionPCW that = (BajoEspecificacionPCW) o;
        return Objects.equals(suminist, that.suminist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suminist);
    }
}
