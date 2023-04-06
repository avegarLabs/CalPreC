package AccessMigration.model;

public class SuminstrosPCW {

    public String codeSuministro;
    public String tipo;
    public String descrip;
    public String um;
    public double peso;
    public double mn;
    public double mlc;

    public SuminstrosPCW(String codeSuministro, String tipo, String descrip, String um, double peso, double mn, double mlc) {
        this.codeSuministro = codeSuministro;
        this.tipo = tipo;
        this.descrip = descrip;
        this.um = um;
        this.peso = peso;
        this.mn = mn;
        this.mlc = mlc;
    }

    public String getCodeSuministro() {
        return codeSuministro;
    }

    public void setCodeSuministro(String codeSuministro) {
        this.codeSuministro = codeSuministro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
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
}
