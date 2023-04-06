package models;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Subsubconcepto {

    private int id;
    private String descripcion;
    private double valor;
    private int subconceptoId;
    private Subconcepto subconceptoById;
    private Collection<Subsubsubconcepto> subsubconceptosById;
    private Collection<Empresaobrasubsubconcepto> empresaobrasubsubconceptosById;

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
    public Subconcepto getSubconceptoById() {
        return subconceptoById;
    }

    public void setSubconceptoById(Subconcepto subconceptoById) {
        this.subconceptoById = subconceptoById;
    }

    @OneToMany(mappedBy = "subsubconceptoById", cascade = {CascadeType.ALL}, orphanRemoval = true)
    public Collection<Subsubsubconcepto> getSubsubconceptosById() {
        return subsubconceptosById;
    }

    public void setSubsubconceptosById(Collection<Subsubsubconcepto> subsubconceptosById) {
        this.subsubconceptosById = subsubconceptosById;
    }

    @OneToMany(mappedBy = "subconceptosgastoByConceptosgastoId", cascade = {CascadeType.ALL}, orphanRemoval = true)
    public Collection<Empresaobrasubsubconcepto> getEmpresaobrasubsubconceptosById() {
        return empresaobrasubsubconceptosById;
    }

    public void setEmpresaobrasubsubconceptosById(Collection<Empresaobrasubsubconcepto> empresaobrasubconceptosById) {
        this.empresaobrasubsubconceptosById = empresaobrasubconceptosById;
    }
}
