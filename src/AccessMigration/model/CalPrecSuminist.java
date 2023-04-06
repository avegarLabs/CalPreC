package AccessMigration.model;

public class CalPrecSuminist {

    public int id;
    public String codig;
    public String descrip;
    public String um;
    public String tipo;
    public double mn;
    public double mlc;
    public double peso;

    public CalPrecSuminist(int id, String codig, String descrip, String um, String tipo, double mn, double mlc, double peso) {
        this.id = id;
        this.codig = codig;
        this.descrip = descrip;
        this.um = um;
        this.tipo = tipo;
        this.mn = mn;
        this.mlc = mlc;
        this.peso = peso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodig() {
        return codig;
    }

    public void setCodig(String codig) {
        this.codig = codig;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getMn() {
        return mn;
    }

    public void setMn(double mn) {
        this.mn = mn;
    }

    public double getMlc() {
        return mlc;
    }

    public void setMlc(double mlc) {
        this.mlc = mlc;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}
