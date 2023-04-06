package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(JuegorecursosPK.class)
public class Juegorecursos {
    private int juegoproductoId;
    private int recursosId;
    private double cantidad;
    private Juegoproducto juegoproductoByJuegoproductoId;
    private Recursos recursosByRecursosId;

    @Id
    @Column(name = "juegoproducto__id", nullable = false)
    public int getJuegoproductoId() {
        return juegoproductoId;
    }

    public void setJuegoproductoId(int juegoproductoId) {
        this.juegoproductoId = juegoproductoId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Juegorecursos that = (Juegorecursos) o;
        return juegoproductoId == that.juegoproductoId &&
                recursosId == that.recursosId &&
                Double.compare(that.cantidad, cantidad) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(juegoproductoId, recursosId, cantidad);
    }

    @ManyToOne
    @JoinColumn(name = "juegoproducto__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Juegoproducto getJuegoproductoByJuegoproductoId() {
        return juegoproductoByJuegoproductoId;
    }

    public void setJuegoproductoByJuegoproductoId(Juegoproducto juegoproductoByJuegoproductoId) {
        this.juegoproductoByJuegoproductoId = juegoproductoByJuegoproductoId;
    }

    @ManyToOne
    @JoinColumn(name = "recursos__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Recursos getRecursosByRecursosId() {
        return recursosByRecursosId;
    }

    public void setRecursosByRecursosId(Recursos recursosByRecursosId) {
        this.recursosByRecursosId = recursosByRecursosId;
    }
}
