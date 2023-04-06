package models;

public class SubconceptoValuesTable {
    public int id;
    public String descrip;
    public String valor;
    public String salario;

    public SubconceptoValuesTable(int id, String descrip, String valor, String salario) {
        this.id = id;
        this.descrip = descrip;
        this.valor = valor;
        this.salario = salario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getSalario() {
        return salario;
    }

    public void setSalario(String salario) {
        this.salario = salario;
    }
}
