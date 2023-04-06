package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(RenglonjuegoPK.class)
public class Renglonjuego {
    private int renglonvarianteId;
    private int juegoproductoId;
    private double cantidad;
    private double usos;
    private Renglonvariante renglonvarianteByRenglonvarianteId;
    private Juegoproducto juegoproductoByJuegoproductoId;

    @Id
    @Column(name = "renglonvariante__id")
    public int getRenglonvarianteId() {
        return renglonvarianteId;
    }

    public void setRenglonvarianteId(int renglonvarianteId) {
        this.renglonvarianteId = renglonvarianteId;
    }

    @Id
    @Column(name = "juegoproducto__id")
    public int getJuegoproductoId() {
        return juegoproductoId;
    }

    public void setJuegoproductoId(int juegoproductoId) {
        this.juegoproductoId = juegoproductoId;
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
    @Column(name = "usos")
    public double getUsos() {
        return usos;
    }

    public void setUsos(double usos) {
        this.usos = usos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Renglonjuego that = (Renglonjuego) o;
        return renglonvarianteId == that.renglonvarianteId &&
                juegoproductoId == that.juegoproductoId &&
                Double.compare(that.cantidad, cantidad) == 0 &&
                Double.compare(that.usos, usos) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(renglonvarianteId, juegoproductoId, cantidad, usos);
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
    @JoinColumn(name = "juegoproducto__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Juegoproducto getJuegoproductoByJuegoproductoId() {
        return juegoproductoByJuegoproductoId;
    }

    public void setJuegoproductoByJuegoproductoId(Juegoproducto juegoproductoByJuegoproductoId) {
        this.juegoproductoByJuegoproductoId = juegoproductoByJuegoproductoId;
    }
}
