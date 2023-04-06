package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Bajoespecificacion {
    private int id;
    private int unidadobraId;
    private int renglonvarianteId;
    private int idSuministro;
    private double cantidad;
    private double costo;
    private String tipo;
    private Integer sumrenglon;
    private Unidadobra unidadobraByUnidadobraId;


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
    @Column(name = "unidadobra_id")
    public int getUnidadobraId() {
        return unidadobraId;
    }

    public void setUnidadobraId(int unidadobraId) {
        this.unidadobraId = unidadobraId;
    }

    @Basic
    @Column(name = "renglonvariante_id")
    public int getRenglonvarianteId() {
        return renglonvarianteId;
    }

    public void setRenglonvarianteId(int renglonvarianteId) {
        this.renglonvarianteId = renglonvarianteId;
    }

    @Basic
    @Column(name = "id_suministro")
    public int getIdSuministro() {
        return idSuministro;
    }

    public void setIdSuministro(int idSuministro) {
        this.idSuministro = idSuministro;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bajoespecificacion that = (Bajoespecificacion) o;
        return id == that.id &&
                unidadobraId == that.unidadobraId &&
                renglonvarianteId == that.renglonvarianteId &&
                idSuministro == that.idSuministro &&
                Double.compare(that.cantidad, cantidad) == 0 &&
                Double.compare(that.costo, costo) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, unidadobraId, renglonvarianteId, idSuministro, cantidad, costo);
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
    @Column(name = "sumrenglon")
    public Integer getSumrenglon() {
        return sumrenglon;
    }

    public void setSumrenglon(Integer sumrenglon) {
        this.sumrenglon = sumrenglon;
    }

    @ManyToOne
    @JoinColumn(name = "unidadobra_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Unidadobra getUnidadobraByUnidadobraId() {
        return unidadobraByUnidadobraId;
    }

    public void setUnidadobraByUnidadobraId(Unidadobra unidadobraByUnidadobraId) {
        this.unidadobraByUnidadobraId = unidadobraByUnidadobraId;
    }
}
