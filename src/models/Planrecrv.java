package models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Planrecrv {
    private Integer id;
    private Double cantidad;
    private Double costo;
    private Date ffin;
    private Date fini;
    private Integer planId;
    private Integer renglonId;
    private Integer nivelespId;
    private Double cmateriales;
    private Double cmano;
    private Double cequipo;
    private Double salario;
    private Double salariocuc;
    private Integer recursoId;
    private String tipo;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "cantidad")
    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    @Basic
    @Column(name = "costo")
    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    @Basic
    @Column(name = "ffin")
    public Date getFfin() {
        return ffin;
    }

    public void setFfin(Date ffin) {
        this.ffin = ffin;
    }

    @Basic
    @Column(name = "fini")
    public Date getFini() {
        return fini;
    }

    public void setFini(Date fini) {
        this.fini = fini;
    }

    @Basic
    @Column(name = "plan_id")
    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    @Basic
    @Column(name = "renglon_id")
    public Integer getRenglonId() {
        return renglonId;
    }

    public void setRenglonId(Integer renglonId) {
        this.renglonId = renglonId;
    }

    @Basic
    @Column(name = "nivelesp_id")
    public Integer getNivelespId() {
        return nivelespId;
    }

    public void setNivelespId(Integer nivelespId) {
        this.nivelespId = nivelespId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planrecrv planrecrv = (Planrecrv) o;
        return Objects.equals(id, planrecrv.id) &&
                Objects.equals(cantidad, planrecrv.cantidad) &&
                Objects.equals(costo, planrecrv.costo) &&
                Objects.equals(ffin, planrecrv.ffin) &&
                Objects.equals(fini, planrecrv.fini) &&
                Objects.equals(planId, planrecrv.planId) &&
                Objects.equals(renglonId, planrecrv.renglonId) &&
                Objects.equals(nivelespId, planrecrv.nivelespId) &&
                Objects.equals(cmateriales, planrecrv.cmateriales) &&
                Objects.equals(cmano, planrecrv.cmano) &&
                Objects.equals(cequipo, planrecrv.cequipo) &&
                Objects.equals(salario, planrecrv.salario) &&
                Objects.equals(salariocuc, planrecrv.salariocuc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cantidad, costo, ffin, fini, planId, renglonId, nivelespId, cmateriales, cmano, cequipo, salario, salariocuc);
    }

    @Basic
    @Column(name = "recurso_id")
    public Integer getRecursoId() {
        return recursoId;
    }

    public void setRecursoId(Integer recursoId) {
        this.recursoId = recursoId;
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
