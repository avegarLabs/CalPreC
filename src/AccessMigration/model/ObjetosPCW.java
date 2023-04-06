package AccessMigration.model;

public class ObjetosPCW {

    public String code;
    public String descrip;
    public String zona;


    public ObjetosPCW(String code, String descrip, String zona) {
        this.code = code;
        this.descrip = descrip;
        this.zona = zona;

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

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }


}
