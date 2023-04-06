package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class TarConceptosBases {
    private int id;
    private String descripcion;
    private double valor;
    private int state;
    private int owner;
    private Collection<GrupoEscalaConceptos> tarConceptosCollection;

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
    @Column(name = "descripcion", nullable = false, length = 300)
    public String getDescripcion() {
        return descripcion;
    }


    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "valor", precision = 0)
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }


    @Basic
    @Column(name = "state", nullable = false)
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Basic
    @Column(name = "owner", nullable = false)
    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TarConceptosBases that = (TarConceptosBases) o;
        return id == that.id && Double.compare(that.valor, valor) == 0 && state == that.state && owner == that.owner && Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descripcion, valor, state, owner);
    }

    @OneToMany(mappedBy = "conceptsByEscala")
    public Collection<GrupoEscalaConceptos> getTarConceptosCollection() {
        return tarConceptosCollection;
    }

    public void setTarConceptosCollection(Collection<GrupoEscalaConceptos> tarConceptos) {
        this.tarConceptosCollection = tarConceptos;
    }
}
