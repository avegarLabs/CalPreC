package AccessMigration.model;

public class EstructUOCalP {

    public int zonId;
    public String zona;
    public int objId;
    public String objeto;
    public int nivId;
    public String nivel;

    public EstructUOCalP(int zonId, String zona, int objId, String objeto, int nivId, String nivel) {
        this.zonId = zonId;
        this.zona = zona;
        this.objId = objId;
        this.objeto = objeto;
        this.nivId = nivId;
        this.nivel = nivel;
    }

    public int getZonId() {
        return zonId;
    }

    public void setZonId(int zonId) {
        this.zonId = zonId;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public int getObjId() {
        return objId;
    }

    public void setObjId(int objId) {
        this.objId = objId;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public int getNivId() {
        return nivId;
    }

    public void setNivId(int nivId) {
        this.nivId = nivId;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
}