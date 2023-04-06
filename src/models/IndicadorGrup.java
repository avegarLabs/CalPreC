package models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class IndicadorGrup {
    private int id;
    private String descipcion;
    private int pertenece;
    private List<NormasInd> normasById;

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
    @Column(name = "descipcion", nullable = false, length = 100)
    public String getDescipcion() {
        return descipcion;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
    }

    @Basic
    @Column(name = "pertenece", nullable = false)
    public int getPertenece() {
        return pertenece;
    }

    public void setPertenece(int pertenece) {
        this.pertenece = pertenece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndicadorGrup that = (IndicadorGrup) o;
        return id == that.id &&
                Objects.equals(descipcion, that.descipcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, descipcion);
    }

    @OneToMany(mappedBy = "indicadorGrupbyIndicador", cascade = CascadeType.ALL)
    public List<NormasInd> getNormasById() {
        return normasById;
    }

    public void setNormasById(List<NormasInd> gruposById) {
        this.normasById = gruposById;
    }
}
