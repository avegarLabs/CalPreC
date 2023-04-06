package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(RenglonnivelespecificoPK.class)
public class Renglonnivelespecifico {
    private int renglonvarianteId;
    private int nivelespecificoId;
    private double cantidad;
    private double costmaterial;
    private double costmano;
    private double costequipo;
    private double salario;
    private String conmat;
    private Renglonvariante renglonvarianteByRenglonvarianteId;
    private Nivelespecifico nivelespecificoByNivelespecificoId;
    private Double salariocuc;

    @Id
    @Column(name = "renglonvariante__id")
    public int getRenglonvarianteId() {
        return renglonvarianteId;
    }

    public void setRenglonvarianteId(int renglonvarianteId) {
        this.renglonvarianteId = renglonvarianteId;
    }

    @Id
    @Column(name = "nivelespecifico__id")
    public int getNivelespecificoId() {
        return nivelespecificoId;
    }

    public void setNivelespecificoId(int nivelespecificoId) {
        this.nivelespecificoId = nivelespecificoId;
    }

    @Basic
    @Column(name = "cantidad")
    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    @Basic
    @Column(name = "costmaterial")
    public double getCostmaterial() {
        return costmaterial;
    }

    public void setCostmaterial(double costmaterial) {
        this.costmaterial = costmaterial;
    }

    @Basic
    @Column(name = "costmano")
    public double getCostmano() {
        return costmano;
    }

    public void setCostmano(double costmano) {
        this.costmano = costmano;
    }

    @Basic
    @Column(name = "costequipo")
    public double getCostequipo() {
        return costequipo;
    }

    public void setCostequipo(double costequipo) {
        this.costequipo = costequipo;
    }

    @Basic
    @Column(name = "salario")
    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Basic
    @Column(name = "conmat")
    public String getConmat() {
        return conmat;
    }

    public void setConmat(String conmat) {
        this.conmat = conmat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Renglonnivelespecifico that = (Renglonnivelespecifico) o;
        return renglonvarianteId == that.renglonvarianteId &&
                nivelespecificoId == that.nivelespecificoId &&
                Double.compare(that.cantidad, cantidad) == 0 &&
                Double.compare(that.costmaterial, costmaterial) == 0 &&
                Double.compare(that.costmano, costmano) == 0 &&
                Double.compare(that.costequipo, costequipo) == 0 &&
                Double.compare(that.salario, salario) == 0 &&
                Objects.equals(conmat, that.conmat);
    }

    @Override
    public int hashCode() {

        return Objects.hash(renglonvarianteId, nivelespecificoId, cantidad, costmaterial, costmano, costequipo, salario, conmat);
    }

    @ManyToOne
    @JoinColumn(name = "renglonvariante__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Renglonvariante getRenglonvarianteByRenglonvarianteId() {
        return renglonvarianteByRenglonvarianteId;
    }

    public void setRenglonvarianteByRenglonvarianteId(Renglonvariante renglonvarianteByRenglonvarianteId) {
        this.renglonvarianteByRenglonvarianteId = renglonvarianteByRenglonvarianteId;
    }

    @ManyToOne
    @JoinColumn(name = "nivelespecifico__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Nivelespecifico getNivelespecificoByNivelespecificoId() {
        return nivelespecificoByNivelespecificoId;
    }

    public void setNivelespecificoByNivelespecificoId(Nivelespecifico nivelespecificoByNivelespecificoId) {
        this.nivelespecificoByNivelespecificoId = nivelespecificoByNivelespecificoId;
    }

    @Basic
    @Column(name = "salariocuc")
    public Double getSalariocuc() {
        return salariocuc;
    }

    public void setSalariocuc(Double salariocuc) {
        this.salariocuc = salariocuc;
    }
}
