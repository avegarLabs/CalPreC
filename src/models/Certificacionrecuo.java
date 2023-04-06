package models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Certificacionrecuo {
    private int id;
    private int unidadobraId;
    private int renglonId;
    private int recursoId;
    private double cantidad;
    private double costo;
    private Date fini;
    private Date ffin;
    private Integer certificacionId;
    private Double cmateriales;
    private Double cmano;
    private Double cequipo;
    private Double salario;
    private Double salariocuc;
    private String tipo;

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
    @Column(name = "unidadobra_id", nullable = false)
    public int getUnidadobraId() {
        return unidadobraId;
    }

    public void setUnidadobraId(int unidadobraId) {
        this.unidadobraId = unidadobraId;
    }

    @Basic
    @Column(name = "renglon_id", nullable = false)
    public int getRenglonId() {
        return renglonId;
    }

    public void setRenglonId(int renglonId) {
        this.renglonId = renglonId;
    }

    @Basic
    @Column(name = "recurso_id", nullable = false)
    public int getRecursoId() {
        return recursoId;
    }

    public void setRecursoId(int recursoId) {
        this.recursoId = recursoId;
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
    @Column(name = "costo", nullable = false, precision = 0)
    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    @Basic
    @Column(name = "fini", nullable = false)
    public Date getFini() {
        return fini;
    }

    public void setFini(Date fini) {
        this.fini = fini;
    }

    @Basic
    @Column(name = "ffin", nullable = false)
    public Date getFfin() {
        return ffin;
    }

    public void setFfin(Date ffin) {
        this.ffin = ffin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificacionrecuo that = (Certificacionrecuo) o;
        return id == that.id &&
                unidadobraId == that.unidadobraId &&
                renglonId == that.renglonId &&
                recursoId == that.recursoId &&
                Double.compare(that.cantidad, cantidad) == 0 &&
                Double.compare(that.costo, costo) == 0 &&
                Objects.equals(fini, that.fini) &&
                Objects.equals(ffin, that.ffin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unidadobraId, renglonId, recursoId, cantidad, costo, fini, ffin);
    }

    @Basic
    @Column(name = "certificacion_id")
    public Integer getCertificacionId() {
        return certificacionId;
    }

    public void setCertificacionId(Integer certificacionId) {
        this.certificacionId = certificacionId;
    }

    @Basic
    @Column(name = "cmateriales")
    public Double getCmateriales() {
        return cmateriales;
    }

    public void setCmateriales(Double cmateriales) {
        this.cmateriales = cmateriales;
    }

    @Basic
    @Column(name = "cmano")
    public Double getCmano() {
        return cmano;
    }

    public void setCmano(Double cmano) {
        this.cmano = cmano;
    }

    @Basic
    @Column(name = "cequipo")
    public Double getCequipo() {
        return cequipo;
    }

    public void setCequipo(Double cequipo) {
        this.cequipo = cequipo;
    }

    @Basic
    @Column(name = "salario")
    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    @Basic
    @Column(name = "salariocuc")
    public Double getSalariocuc() {
        return salariocuc;
    }

    public void setSalariocuc(Double salariocuc) {
        this.salariocuc = salariocuc;
    }

    @Basic
    @Column(name = "tipo", nullable = true, length = 3)
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
