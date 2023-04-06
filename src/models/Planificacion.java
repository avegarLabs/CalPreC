package models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Planificacion {
    private int id;
    private double cantidad;
    private double costomaterial;
    private double costoequipo;
    private double costomano;
    private double costosalario;
    private double costsalariocuc;
    private Date desde;
    private Date hasta;
    private int unidadobraId;
    private int brigadaconstruccionId;
    private Integer grupoconstruccionId;
    private Integer cuadrillaconstruccionId;
    private Unidadobra unidadobraByUnidadobraId;
    private Brigadaconstruccion brigadaconstruccionByBrigadaconstruccionId;
    private Grupoconstruccion grupoconstruccionByGrupoconstruccionId;
    private Cuadrillaconstruccion cuadrillaconstruccionByCuadrillaconstruccionId;

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
    @Column(name = "cantidad")
    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    @Basic
    @Column(name = "costomaterial")
    public double getCostomaterial() {
        return costomaterial;
    }

    public void setCostomaterial(double costomaterial) {
        this.costomaterial = costomaterial;
    }

    @Basic
    @Column(name = "costoequipo")
    public double getCostoequipo() {
        return costoequipo;
    }

    public void setCostoequipo(double costoequipo) {
        this.costoequipo = costoequipo;
    }

    @Basic
    @Column(name = "costomano")
    public double getCostomano() {
        return costomano;
    }

    public void setCostomano(double costomano) {
        this.costomano = costomano;
    }

    @Basic
    @Column(name = "costosalario")
    public double getCostosalario() {
        return costosalario;
    }

    public void setCostosalario(double costosalario) {
        this.costosalario = costosalario;
    }

    @Basic
    @Column(name = "costsalariocuc")
    public double getCostsalariocuc() {
        return costsalariocuc;
    }

    public void setCostsalariocuc(double costsalariocuc) {
        this.costsalariocuc = costsalariocuc;
    }

    @Basic
    @Column(name = "desde")
    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    @Basic
    @Column(name = "hasta")
    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    @Basic
    @Column(name = "unidadobra__id")
    public int getUnidadobraId() {
        return unidadobraId;
    }

    public void setUnidadobraId(int unidadobraId) {
        this.unidadobraId = unidadobraId;
    }

    @Basic
    @Column(name = "brigadaconstruccion__id")
    public int getBrigadaconstruccionId() {
        return brigadaconstruccionId;
    }

    public void setBrigadaconstruccionId(int brigadaconstruccionId) {
        this.brigadaconstruccionId = brigadaconstruccionId;
    }

    @Basic
    @Column(name = "grupoconstruccion__id")
    public Integer getGrupoconstruccionId() {
        return grupoconstruccionId;
    }

    public void setGrupoconstruccionId(Integer grupoconstruccionId) {
        this.grupoconstruccionId = grupoconstruccionId;
    }

    @Basic
    @Column(name = "cuadrillaconstruccion__id")
    public Integer getCuadrillaconstruccionId() {
        return cuadrillaconstruccionId;
    }

    public void setCuadrillaconstruccionId(Integer cuadrillaconstruccionId) {
        this.cuadrillaconstruccionId = cuadrillaconstruccionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planificacion that = (Planificacion) o;
        return  /*
                Double.compare(that.cantidad, cantidad) == 0 &&
                Double.compare(that.costomaterial, costomaterial) == 0 &&
                Double.compare(that.costoequipo, costoequipo) == 0 &&
                Double.compare(that.costomano, costomano) == 0 &&
                Double.compare(that.costosalario, costosalario) == 0 &&
                Double.compare(that.costsalariocuc, costsalariocuc) == 0 &&
                */
                unidadobraId == that.unidadobraId &&
                        Objects.equals(desde, that.desde) &&
                        Objects.equals(hasta, that.hasta);
                        /*
                brigadaconstruccionId == that.brigadaconstruccionId &&
                grupoconstruccionId == that.grupoconstruccionId &&
                cuadrillaconstruccionId == that.cuadrillaconstruccionId &&
                */


    }

    @Override
    public int hashCode() {

        return Objects.hash(id, cantidad, costomaterial, costoequipo, costomano, costosalario, costsalariocuc, desde, hasta, unidadobraId, brigadaconstruccionId, grupoconstruccionId, cuadrillaconstruccionId);
    }

    @ManyToOne
    @JoinColumn(name = "unidadobra__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Unidadobra getUnidadobraByUnidadobraId() {
        return unidadobraByUnidadobraId;
    }

    public void setUnidadobraByUnidadobraId(Unidadobra unidadobraByUnidadobraId) {
        this.unidadobraByUnidadobraId = unidadobraByUnidadobraId;
    }

    @ManyToOne
    @JoinColumn(name = "brigadaconstruccion__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Brigadaconstruccion getBrigadaconstruccionByBrigadaconstruccionId() {
        return brigadaconstruccionByBrigadaconstruccionId;
    }

    public void setBrigadaconstruccionByBrigadaconstruccionId(Brigadaconstruccion brigadaconstruccionByBrigadaconstruccionId) {
        this.brigadaconstruccionByBrigadaconstruccionId = brigadaconstruccionByBrigadaconstruccionId;
    }

    @ManyToOne
    @JoinColumn(name = "grupoconstruccion__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Grupoconstruccion getGrupoconstruccionByGrupoconstruccionId() {
        return grupoconstruccionByGrupoconstruccionId;
    }

    public void setGrupoconstruccionByGrupoconstruccionId(Grupoconstruccion grupoconstruccionByGrupoconstruccionId) {
        this.grupoconstruccionByGrupoconstruccionId = grupoconstruccionByGrupoconstruccionId;
    }

    @ManyToOne
    @JoinColumn(name = "cuadrillaconstruccion__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Cuadrillaconstruccion getCuadrillaconstruccionByCuadrillaconstruccionId() {
        return cuadrillaconstruccionByCuadrillaconstruccionId;
    }

    public void setCuadrillaconstruccionByCuadrillaconstruccionId(Cuadrillaconstruccion cuadrillaconstruccionByCuadrillaconstruccionId) {
        this.cuadrillaconstruccionByCuadrillaconstruccionId = cuadrillaconstruccionByCuadrillaconstruccionId;
    }
}
