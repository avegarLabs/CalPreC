package models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Certificacionresumenconceptos {
    private int id;
    private Integer idObra;
    private Integer idEmpresa;
    private Integer idConcepto;
    private Double valor;
    private Double salario;
    private Date fini;
    private Date ffin;

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
    @Column(name = "idobra")
    public Integer getIdObra() {
        return idObra;
    }

    public void setIdObra(Integer idObra) {
        this.idObra = idObra;
    }

    @Basic
    @Column(name = "idempresa")
    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    @Basic
    @Column(name = "idconcepto")
    public Integer getIdConcepto() {
        return idConcepto;
    }

    public void setIdConcepto(Integer idConcepto) {
        this.idConcepto = idConcepto;
    }

    @Basic
    @Column(name = "valor")
    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
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
    @Column(name = "fini")
    public Date getFini() {
        return fini;
    }

    public void setFini(Date fini) {
        this.fini = fini;
    }

    @Basic
    @Column(name = "ffin")
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
        Certificacionresumenconceptos that = (Certificacionresumenconceptos) o;
        return id == that.id && Objects.equals(idObra, that.idObra) && Objects.equals(idEmpresa, that.idEmpresa) && Objects.equals(idConcepto, that.idConcepto) && Objects.equals(valor, that.valor) && Objects.equals(salario, that.salario) && Objects.equals(fini, that.fini) && Objects.equals(ffin, that.ffin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idObra, idEmpresa, idConcepto, valor, salario, fini, ffin);
    }
}
