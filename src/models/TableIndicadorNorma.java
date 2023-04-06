package models;

public class TableIndicadorNorma {

    public String code;
    public String descrip;
    public String um;

    public TableIndicadorNorma(String code, String descrip, String um) {
        this.code = code;
        this.descrip = descrip;
        this.um = um;
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


}
