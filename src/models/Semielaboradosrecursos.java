package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(SemielaboradosrecursosPK.class)
public class Semielaboradosrecursos {
    private int suministrossemielaboradosId;
    private int recursosId;
    private double cantidad;
    private double usos;

    private Recursos recursosByRecursosId;
    private Suministrossemielaborados suministrossemielaboradosBySuministrossemielaboradosId;

    @Id
    @Column(name = "suministrossemielaborados__id", nullable = false)
    public int getSuministrossemielaboradosId() {
        return suministrossemielaboradosId;
    }

    public void setSuministrossemielaboradosId(int suministrossemielaboradosId) {
        this.suministrossemielaboradosId = suministrossemielaboradosId;
    }

    @Id
    @Column(name = "recursos__id", nullable = false)
    public int getRecursosId() {
        return recursosId;
    }

    public void setRecursosId(int recursosId) {
        this.recursosId = recursosId;
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
        Semielaboradosrecursos that = (Semielaboradosrecursos) o;
        return suministrossemielaboradosId == that.suministrossemielaboradosId &&
                recursosId == that.recursosId &&
                Double.compare(that.cantidad, cantidad) == 0 &&
                Double.compare(that.usos, usos) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(suministrossemielaboradosId, recursosId, cantidad, usos);
    }

    @ManyToOne
    @JoinColumn(name = "recursos__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Recursos getRecursosByRecursosId() {
        return recursosByRecursosId;
    }

    public void setRecursosByRecursosId(Recursos recursosByRecursosId) {
        this.recursosByRecursosId = recursosByRecursosId;
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
