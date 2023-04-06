package models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Sobregrupo {
    private int id;
    private String codigo;
    private String descipcion;
    private List<Grupo> gruposById;

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
    @Column(name = "codigo", nullable = false, length = 5)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "descipcion", nullable = false, length = 250)
    public String getDescipcion() {
        return descipcion;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sobregrupo that = (Sobregrupo) o;
        return id == that.id &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(descipcion, that.descipcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, descipcion);
    }

    @OneToMany(mappedBy = "sobregrupoBySobregrupoId", cascade = CascadeType.ALL)
    public List<Grupo> getGruposById() {
        return gruposById;
    }

    public void setGruposById(List<Grupo> gruposById) {
        this.gruposById = gruposById;
    }
}
