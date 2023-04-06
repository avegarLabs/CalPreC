package AccessMigration.model;

public class ObraPCW {

    public String code;
    public String descrip;
    public String clasif;
    public String tipo;

    public ObraPCW(String code, String descrip, String clasif, String tipo) {
        this.code = code;
        this.descrip = descrip;
        this.clasif = clasif;
        this.tipo = tipo;
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

    public String getClasif() {
        return clasif;
    }

    public void setClasif(String clasif) {
        this.clasif = clasif;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
