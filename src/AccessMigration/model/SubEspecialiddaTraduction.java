package AccessMigration.model;

public class SubEspecialiddaTraduction {

    public String codigo;
    public String desciption;

    public SubEspecialiddaTraduction(String codigo, String desciption) {
        this.codigo = codigo;
        this.desciption = desciption;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }
}
