package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Subconcepto {

    private int id;
    private String descripcion;
    private double valor;
    private int conceptoId;
    private Conceptosgasto conceptosgastoById;
    private Collection<Subsubconcepto> subSubConceptosById;
    private Collection<Empresaobrasubconcepto> empresaobrasubconceptosById;

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
    @Column(name = "descripcion", nullable = false, length = 225)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "valor", nullable = false, precision = 0)
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }


    @Column(name = "conceptoId", nullable = false)
    public int getConceptoId() {
        return conceptoId;
    }

    public void setConceptoId(int conceptoId) {
        this.conceptoId = conceptoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subconcepto that = (Subconcepto) o;
        return id == that.id &&
                Double.compare(that.valor, valor) == 0 &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descripcion, valor);
    }

    @ManyToOne
    @JoinColumn(name = "conceptoId", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Conceptosgasto getConceptosgastoById() {
        return conceptosgastoById;
    }

    public void setConceptosgastoById(Conceptosgasto conceptosgastoById) {
        this.conceptosgastoById = conceptosgastoById;
    }

    @OneToMany(mappedBy = "subconceptoById", cascade = {CascadeType.ALL}, orphanRemoval = true)
    public Collection<Subsubconcepto> getSubSubConceptosById() {
        return subSubConceptosById;
    }

    public void setSubSubConceptosById(Collection<Subsubconcepto> subSubConceptosById) {
        this.subSubConceptosById = subSubConceptosById;
    }

    @OneToMany(mappedBy = "conceptosgastoByConceptosgastoId", cascade = {CascadeType.ALL}, orphanRemoval = true)
    public Collection<Empresaobrasubconcepto> getEmpresaobrasubconceptosById() {
        return empresaobrasubconceptosById;
    }

    public void setEmpresaobrasubconceptosById(Collection<Empresaobrasubconcepto> empresaobrasubconceptosById) {
        this.empresaobrasubconceptosById = empresaobrasubconceptosById;
    }
}
