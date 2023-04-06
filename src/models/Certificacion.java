package models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Certificacion {
    private int id;
    private double cantidad;
    private double costmaterial;
    private double costmano;
    private double costequipo;
    private double salario;
    private double cucsalario;
    private Date desde;
    private Date hasta;
    private int unidadobraId;
    private int brigadaconstruccionId;
    private int grupoconstruccionId;
    private int cuadrillaconstruccionId;
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
    @Column(name = "cucsalario")
    public double getCucsalario() {
        return cucsalario;
    }

    public void setCucsalario(double cucsalario) {
        this.cucsalario = cucsalario;
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
    public int getGrupoconstruccionId() {
        return grupoconstruccionId;
    }

    public void setGrupoconstruccionId(int grupoconstruccionId) {
        this.grupoconstruccionId = grupoconstruccionId;
    }

    @Basic
    @Column(name = "cuadrillaconstruccion__id")
    public int getCuadrillaconstruccionId() {
        return cuadrillaconstruccionId;
    }

    public void setCuadrillaconstruccionId(int cuadrillaconstruccionId) {
        this.cuadrillaconstruccionId = cuadrillaconstruccionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificacion that = (Certificacion) o;
        return unidadobraId == that.unidadobraId && brigadaconstruccionId == that.brigadaconstruccionId && grupoconstruccionId == that.grupoconstruccionId && Objects.equals(desde, that.desde) && Objects.equals(hasta, that.hasta) && Objects.equals(cuadrillaconstruccionId, that.cuadrillaconstruccionId) && Objects.equals(unidadobraByUnidadobraId, that.unidadobraByUnidadobraId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(desde, hasta, unidadobraId, brigadaconstruccionId, grupoconstruccionId, cuadrillaconstruccionId, unidadobraByUnidadobraId);
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
