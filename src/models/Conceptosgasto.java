package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Conceptosgasto {
    private int id;
    private String descripcion;
    private double coeficiente;
    private String formula;
    private Collection<Empresagastos> empresagastosById;
    private Collection<Empresaobraconcepto> empresaobraconceptosById;
    private Collection<Subconcepto> subConceptosById;
    private Integer calcular;
    private Integer pertence;
    private String code;

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "descripcion", nullable = false, length = 100)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "coeficiente", nullable = false, precision = 0)
    public double getCoeficiente() {
        return coeficiente;
    }

    public void setCoeficiente(double coeficiente) {
        this.coeficiente = coeficiente;
    }

    @Basic
    @Column(name = "formula", nullable = true, length = 50)
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conceptosgasto that = (Conceptosgasto) o;
        return id == that.id &&
                Double.compare(that.coeficiente, coeficiente) == 0 &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(formula, that.formula);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, descripcion, coeficiente, formula);
    }

    @OneToMany(mappedBy = "conceptosgastoByConceptosgastoId", cascade = {CascadeType.ALL}, orphanRemoval = true)
    public Collection<Empresagastos> getEmpresagastosById() {
        return empresagastosById;
    }

    public void setEmpresagastosById(Collection<Empresagastos> empresagastosById) {
        this.empresagastosById = empresagastosById;
    }

    @OneToMany(mappedBy = "conceptosgastoByConceptosgastoId", cascade = {CascadeType.ALL}, orphanRemoval = true)
    public Collection<Empresaobraconcepto> getEmpresaobraconceptosById() {
        return empresaobraconceptosById;
    }

    public void setEmpresaobraconceptosById(Collection<Empresaobraconcepto> empresaobraconceptosById) {
        this.empresaobraconceptosById = empresaobraconceptosById;
    }

    @OneToMany(mappedBy = "conceptosgastoById", cascade = {CascadeType.ALL}, orphanRemoval = true)
    public Collection<Subconcepto> getSubConceptosById() {
        return subConceptosById;
    }

    public void setSubConceptosById(Collection<Subconcepto> subConceptosById) {
        this.subConceptosById = subConceptosById;
    }

    @Basic
    @Column(name = "calcular")
    public Integer getCalcular() {
        return calcular;
    }

    public void setCalcular(Integer calcular) {
        this.calcular = calcular;
    }

    @Basic
    @Column(name = "pertenece")
    public int getPertence() {
        return pertence;
    }

    public void setPertence(int pertence) {
        this.pertence = pertence;
    }

    @Basic
    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
