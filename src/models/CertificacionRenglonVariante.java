package models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
public class CertificacionRenglonVariante {
    private int id;
    private double cantidad;
    private double costomaterial;
    private double costomano;
    private double costoequipo;
    private double salario;
    private double salariocuc;
    private Date desde;
    private Date hasta;
    private int nivelespecificoId;
    private int renglonvarianteId;
    private int brigadaconstruccionId;
    private int grupoconstruccionId;
    private Nivelespecifico nivelespecificoByNivelespecificoId;
    private Renglonvariante renglonvarianteByRenglonvarianteId;
    private Brigadaconstruccion brigadaconstruccionByBrigadaconstruccionId;
    private Cuadrillaconstruccion cuadrillaconstruccionByCuadrillaconstruccionId;
    private Grupoconstruccion grupoconstruccionByGrupoconstruccionId;
    private Integer cuadrillaconstruccionId;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "costomaterial", nullable = false, precision = 0)
    public double getCostomaterial() {
        return costomaterial;
    }

    public void setCostomaterial(double costomaterial) {
        this.costomaterial = costomaterial;
    }

    @Basic
    @Column(name = "costomano", nullable = false, precision = 0)
    public double getCostomano() {
        return costomano;
    }

    public void setCostomano(double costomano) {
        this.costomano = costomano;
    }

    @Basic
    @Column(name = "costoequipo", nullable = false, precision = 0)
    public double getCostoequipo() {
        return costoequipo;
    }

    public void setCostoequipo(double costoequipo) {
        this.costoequipo = costoequipo;
    }

    @Basic
    @Column(name = "salario", nullable = false, precision = 0)
    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Basic
    @Column(name = "salariocuc", nullable = false, precision = 0)
    public double getSalariocuc() {
        return salario;
    }

    public void setSalariocuc(double salario) {
        this.salario = salario;
    }

    @Basic
    @Column(name = "desde", nullable = false)
    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    @Basic
    @Column(name = "hasta", nullable = false)
    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    @Basic
    @Column(name = "nivelespecifico__id", nullable = false)
    public int getNivelespecificoId() {
        return nivelespecificoId;
    }

    public void setNivelespecificoId(int nivelespecificoId) {
        this.nivelespecificoId = nivelespecificoId;
    }

    @Basic
    @Column(name = "renglonvariante__id", nullable = false)
    public int getRenglonvarianteId() {
        return renglonvarianteId;
    }

    public void setRenglonvarianteId(int renglonvarianteId) {
        this.renglonvarianteId = renglonvarianteId;
    }

    @Basic
    @Column(name = "brigadaconstruccion__id", nullable = false)
    public int getBrigadaconstruccionId() {
        return brigadaconstruccionId;
    }

    public void setBrigadaconstruccionId(int brigadaconstruccionId) {
        this.brigadaconstruccionId = brigadaconstruccionId;
    }

    @Basic
    @Column(name = "grupoconstruccion__id", nullable = false)
    public int getGrupoconstruccionId() {
        return grupoconstruccionId;
    }

    public void setGrupoconstruccionId(int grupoconstruccionId) {
        this.grupoconstruccionId = grupoconstruccionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertificacionRenglonVariante that = (CertificacionRenglonVariante) o;
        return id == that.id &&
                Double.compare(that.cantidad, cantidad) == 0 &&
                Double.compare(that.costomaterial, costomaterial) == 0 &&
                Double.compare(that.costomano, costomano) == 0 &&
                Double.compare(that.costoequipo, costoequipo) == 0 &&
                Double.compare(that.salario, salario) == 0 &&
                nivelespecificoId == that.nivelespecificoId &&
                renglonvarianteId == that.renglonvarianteId &&
                brigadaconstruccionId == that.brigadaconstruccionId &&
                grupoconstruccionId == that.grupoconstruccionId &&
                Objects.equals(desde, that.desde) &&
                Objects.equals(hasta, that.hasta);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, cantidad, costomaterial, costomano, costoequipo, salario, desde, hasta, nivelespecificoId, renglonvarianteId, brigadaconstruccionId, grupoconstruccionId);
    }

    @ManyToOne
    @JoinColumn(name = "nivelespecifico__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Nivelespecifico getNivelespecificoByNivelespecificoId() {
        return nivelespecificoByNivelespecificoId;
    }

    public void setNivelespecificoByNivelespecificoId(Nivelespecifico nivelespecificoByNivelespecificoId) {
        this.nivelespecificoByNivelespecificoId = nivelespecificoByNivelespecificoId;
    }

    @ManyToOne
    @JoinColumn(name = "renglonvariante__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Renglonvariante getRenglonvarianteByRenglonvarianteId() {
        return renglonvarianteByRenglonvarianteId;
    }

    public void setRenglonvarianteByRenglonvarianteId(Renglonvariante renglonvarianteByRenglonvarianteId) {
        this.renglonvarianteByRenglonvarianteId = renglonvarianteByRenglonvarianteId;
    }

    @ManyToOne
    @JoinColumn(name = "brigadaconstruccion__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Brigadaconstruccion getBrigadaconstruccionByBrigadaconstruccionId() {
        return brigadaconstruccionByBrigadaconstruccionId;
    }

    public void setBrigadaconstruccionByBrigadaconstruccionId(Brigadaconstruccion brigadaconstruccionByBrigadaconstruccionId) {
        this.brigadaconstruccionByBrigadaconstruccionId = brigadaconstruccionByBrigadaconstruccionId;
    }

    @ManyToOne
    @JoinColumn(name = "cuadrillaconstruccion__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Cuadrillaconstruccion getCuadrillaconstruccionByCuadrillaconstruccionId() {
        return cuadrillaconstruccionByCuadrillaconstruccionId;
    }

    public void setCuadrillaconstruccionByCuadrillaconstruccionId(Cuadrillaconstruccion cuadrillaconstruccionByCuadrillaconstruccionId) {
        this.cuadrillaconstruccionByCuadrillaconstruccionId = cuadrillaconstruccionByCuadrillaconstruccionId;
    }

    @ManyToOne
    @JoinColumn(name = "grupoconstruccion__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Grupoconstruccion getGrupoconstruccionByGrupoconstruccionId() {
        return grupoconstruccionByGrupoconstruccionId;
    }

    public void setGrupoconstruccionByGrupoconstruccionId(Grupoconstruccion grupoconstruccionByGrupoconstruccionId) {
        this.grupoconstruccionByGrupoconstruccionId = grupoconstruccionByGrupoconstruccionId;
    }

    @Basic
    @Column(name = "cuadrillaconstruccion__id")
    public Integer getCuadrillaconstruccionId() {
        return cuadrillaconstruccionId;
    }

    public void setCuadrillaconstruccionId(Integer cuadrillaconstruccionId) {
        this.cuadrillaconstruccionId = cuadrillaconstruccionId;
    }
}
