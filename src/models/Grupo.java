package models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Grupo {
    private int id;
    private String codigog;
    private String descripciong;
    private int sobregrupoId;
    private Sobregrupo sobregrupoBySobregrupoId;
    private List<Subgrupo> subgruposById;

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
    @Column(name = "codigog", nullable = false, length = 5)
    public String getCodigog() {
        return codigog;
    }

    public void setCodigog(String codigog) {
        this.codigog = codigog;
    }

    @Basic
    @Column(name = "descripciong", nullable = false, length = 250)
    public String getDescripciong() {
        return descripciong;
    }

    public void setDescripciong(String descripciong) {
        this.descripciong = descripciong;
    }

    @Basic
    @Column(name = "sobregrupo__id", nullable = false)
    public int getSobregrupoId() {
        return sobregrupoId;
    }

    public void setSobregrupoId(int sobregrupoId) {
        this.sobregrupoId = sobregrupoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grupo grupo = (Grupo) o;
        return id == grupo.id &&
                sobregrupoId == grupo.sobregrupoId &&
                Objects.equals(codigog, grupo.codigog) &&
                Objects.equals(descripciong, grupo.descripciong);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigog, descripciong, sobregrupoId);
    }

    @ManyToOne
    @JoinColumn(name = "sobregrupo__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Sobregrupo getSobregrupoBySobregrupoId() {
        return sobregrupoBySobregrupoId;
    }

    public void setSobregrupoBySobregrupoId(Sobregrupo sobregrupoBySobregrupoId) {
        this.sobregrupoBySobregrupoId = sobregrupoBySobregrupoId;
    }

    @OneToMany(mappedBy = "grupoByGrupoId", cascade = CascadeType.ALL)
    public List<Subgrupo> getSubgruposById() {
        return subgruposById;
    }

    public void setSubgruposById(List<Subgrupo> subgruposById) {
        this.subgruposById = subgruposById;
    }
}
