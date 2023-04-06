package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(RenglonsemielaboradosPK.class)
public class Renglonsemielaborados {
    private int renglonvarianteId;
    private int suministrossemielaboradosId;
    private double cantidad;
    private double usos;
    private Renglonvariante renglonvarianteByRenglonvarianteId;
    private Suministrossemielaborados suministrossemielaboradosBySuministrossemielaboradosId;

    @Id
    @Column(name = "renglonvariante__id", nullable = false)
    public int getRenglonvarianteId() {
        return renglonvarianteId;
    }

    public void setRenglonvarianteId(int renglonvarianteId) {
        this.renglonvarianteId = renglonvarianteId;
    }

    @Id
    @Column(name = "suministrossemielaborados__id", nullable = false)
    public int getSuministrossemielaboradosId() {
        return suministrossemielaboradosId;
    }

    public void setSuministrossemielaboradosId(int suministrossemielaboradosId) {
        this.suministrossemielaboradosId = suministrossemielaboradosId;
    }

    @Basic
    @Column(name = "cantidad", nullable = false, precision = 0)
    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    @Basic
    @Column(name = "usos", nullable = false, precision = 0)
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
        Renglonsemielaborados that = (Renglonsemielaborados) o;
        return renglonvarianteId == that.renglonvarianteId &&
                suministrossemielaboradosId == that.suministrossemielaboradosId &&
                Double.compare(that.cantidad, cantidad) == 0 &&
                Double.compare(that.usos, usos) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(renglonvarianteId, suministrossemielaboradosId, cantidad, usos);
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
    @JoinColumn(name = "suministrossemielaborados__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Suministrossemielaborados getSuministrossemielaboradosBySuministrossemielaboradosId() {
        return suministrossemielaboradosBySuministrossemielaboradosId;
    }

    public void setSuministrossemielaboradosBySuministrossemielaboradosId(Suministrossemielaborados suministrossemielaboradosBySuministrossemielaboradosId) {
        this.suministrossemielaboradosBySuministrossemielaboradosId = suministrossemielaboradosBySuministrossemielaboradosId;
    }
}
