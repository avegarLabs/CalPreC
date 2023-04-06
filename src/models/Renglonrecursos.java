package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(RenglonrecursosPK.class)
public class Renglonrecursos {
    private int renglonvarianteId;
    private int recursosId;
    private double cantidas;
    private double usos;
    private Renglonvariante renglonvarianteByRenglonvarianteId;
    private Recursos recursosByRecursosId;

    @Id
    @Column(name = "renglonvariante__id", nullable = false)
    public int getRenglonvarianteId() {
        return renglonvarianteId;
    }

    public void setRenglonvarianteId(int renglonvarianteId) {
        this.renglonvarianteId = renglonvarianteId;
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
    @Column(name = "cantidas", nullable = false, precision = 0)
    public double getCantidas() {
        return cantidas;
    }

    public void setCantidas(double cantidas) {
        this.cantidas = cantidas;
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
        Renglonrecursos that = (Renglonrecursos) o;
        return renglonvarianteId == that.renglonvarianteId &&
                recursosId == that.recursosId &&
                Double.compare(that.cantidas, cantidas) == 0 &&
                Double.compare(that.usos, usos) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(renglonvarianteId, recursosId, cantidas, usos);
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
    @JoinColumn(name = "recursos__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Recursos getRecursosByRecursosId() {
        return recursosByRecursosId;
    }

    public void setRecursosByRecursosId(Recursos recursosByRecursosId) {
        this.recursosByRecursosId = recursosByRecursosId;
    }
}
