package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Subsubsubconcepto {

    private int id;
    private String descripcion;
    private double valor;
    private int subconceptoId;
    private Subsubconcepto subsubconceptoById;
    private Collection<Empresaobrasubsubsubconcepto> empresaobrasubsubsubconceptoCollectionById;

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


    @Column(name = "subconceptoId", nullable = false)
    public int getSubconceptoId() {
        return subconceptoId;
    }

    public void setSubconceptoId(int subconceptoId) {
        this.subconceptoId = subconceptoId;
    }

    @ManyToOne
    @JoinColumn(name = "subconceptoId", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Subsubconcepto getSubsubconceptoById() {
        return subsubconceptoById;
    }

    public void setSubsubconceptoById(Subsubconcepto subsubconceptoById) {
        this.subsubconceptoById = subsubconceptoById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subsubsubconcepto that = (Subsubsubconcepto) o;
        return id == that.id && Double.compare(that.valor, valor) == 0 && subconceptoId == that.subconceptoId && Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descripcion, valor, subconceptoId);
    }

    @OneToMany(mappedBy = "subconceptosgastoByConceptosgastoId", cascade = {CascadeType.ALL}, orphanRemoval = true)
    public Collection<Empresaobrasubsubsubconcepto> getEmpresaobrasubsubsubconceptoCollectionById() {
        return empresaobrasubsubsubconceptoCollectionById;
    }

    public void setEmpresaobrasubsubsubconceptoCollectionById(Collection<Empresaobrasubsubsubconcepto> empresaobrasubconceptosById) {
        this.empresaobrasubsubsubconceptoCollectionById = empresaobrasubconceptosById;
    }
}
