package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Bajoespecificacionrv {
    private int id;
    private int idsuministro;
    private double cantidad;
    private double costo;
    private String tipo;
    private int renglonvarianteId;
    private int nivelespecificoId;
    private Renglonvariante renglonvarianteByRenglonvarianteId;
    private Nivelespecifico nivelespecificoByNivelespecificoId;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "idsuministro")
    public int getIdsuministro() {
        return idsuministro;
    }

    public void setIdsuministro(int idsuministro) {
        this.idsuministro = idsuministro;
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
    @Column(name = "costo")
    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    @Basic
    @Column(name = "tipo")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Basic
    @Column(name = "renglonvariante__id")
    public int getRenglonvarianteId() {
        return renglonvarianteId;
    }

    public void setRenglonvarianteId(int renglonvarianteId) {
        this.renglonvarianteId = renglonvarianteId;
    }

    @Basic
    @Column(name = "nivelespecifico__id")
    public int getNivelespecificoId() {
        return nivelespecificoId;
    }

    public void setNivelespecificoId(int nivelespecificoId) {
        this.nivelespecificoId = nivelespecificoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bajoespecificacionrv that = (Bajoespecificacionrv) o;
        return id == that.id &&
                idsuministro == that.idsuministro &&
                Double.compare(that.cantidad, cantidad) == 0 &&
                Double.compare(that.costo, costo) == 0 &&
                renglonvarianteId == that.renglonvarianteId &&
                nivelespecificoId == that.nivelespecificoId &&
                Objects.equals(tipo, that.tipo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, idsuministro, cantidad, costo, tipo, renglonvarianteId, nivelespecificoId);
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
}
