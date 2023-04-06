package models;


import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;


public class EquiposView extends RecursiveTreeObject<EquiposView> {


    public IntegerProperty id;
    public StringProperty codigo;
    public StringProperty descripcion;
    public StringProperty tipo;
    public DoubleProperty preciomn;
    public DoubleProperty preciomlc;
    /*public DoubleProperty cpo;
    public DoubleProperty cpe;
    public DoubleProperty cet;
    public DoubleProperty otra;*/
    public StringProperty um;
    public StringProperty grupo;
    public DoubleProperty salario;
    public StringProperty idEm;

    public EquiposView(int id, String codigo, String descripcion, String tipo, double preciomn, double preciomlc, String um, String grupo, Double salario, String idem) {
        this.id = new SimpleIntegerProperty(id);
        this.codigo = new SimpleStringProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.tipo = new SimpleStringProperty(tipo);
        this.preciomn = new SimpleDoubleProperty(preciomn);
        this.preciomlc = new SimpleDoubleProperty(preciomlc);
        /*
        this.cpo =  new SimpleDoubleProperty(cpo);
        this.cpe =  new SimpleDoubleProperty(cpe);
        this.cet =  new SimpleDoubleProperty(cet);
        this.otra =  new SimpleDoubleProperty(otra);*/
        this.um = new SimpleStringProperty(um);
        this.grupo = new SimpleStringProperty(grupo);
        this.salario = new SimpleDoubleProperty(salario);
        this.idEm = new SimpleStringProperty(idem);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getCodigo() {
        return codigo.get();
    }

    public void setCodigo(String codigo) {
        this.codigo.set(codigo);
    }

    public StringProperty codigoProperty() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public String getTipo() {
        return tipo.get();
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public double getPreciomn() {
        return preciomn.get();
    }

    public void setPreciomn(double preciomn) {
        this.preciomn.set(preciomn);
    }

    public DoubleProperty preciomnProperty() {
        return preciomn;
    }

    public double getPreciomlc() {
        return preciomlc.get();
    }

    public void setPreciomlc(double preciomlc) {
        this.preciomlc.set(preciomlc);
    }

    public DoubleProperty preciomlcProperty() {
        return preciomlc;
    }

    /*
        public double getCpo() {
            return cpo.get();
        }

        public DoubleProperty cpoProperty() {
            return cpo;
        }

        public void setCpo(double cpo) {
            this.cpo.set(cpo);
        }

        public double getCpe() {
            return cpe.get();
        }

        public DoubleProperty cpeProperty() {
            return cpe;
        }

        public void setCpe(double cpe) {
            this.cpe.set(cpe);
        }

        public double getCet() {
            return cet.get();
        }

        public DoubleProperty cetProperty() {
            return cet;
        }

        public void setCet(double cet) {
            this.cet.set(cet);
        }

        public double getOtra() {
            return otra.get();
        }

        public DoubleProperty otraProperty() {
            return otra;
        }

        public void setOtra(double otra) {
            this.otra.set(otra);
        }
    */
    public String getUm() {
        return um.get();
    }

    public void setUm(String um) {
        this.um.set(um);
    }

    public StringProperty umProperty() {
        return um;
    }

    public String getGrupo() {
        return grupo.get();
    }

    public void setGrupo(String grupo) {
        this.grupo.set(grupo);
    }

    public StringProperty grupoProperty() {
        return grupo;
    }

    public double getSalario() {
        return salario.get();
    }

    public void setSalario(double salario) {
        this.salario.set(salario);
    }

    public DoubleProperty salarioProperty() {
        return salario;
    }

    public String getIdEm() {
        return idEm.get();
    }

    public void setIdEm(String idEm) {
        this.idEm.set(idEm);
    }

    public StringProperty idEmProperty() {
        return idEm;
    }
}
