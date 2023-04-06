package AccessMigration.model;

public class NivelPCW {

    public String code;
    public String descrip;
    public String obj;
    public String zona;

    public NivelPCW(String code, String descrip, String obj, String zona) {
        this.code = code;
        this.descrip = descrip;
        this.obj = obj;
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

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }
}
