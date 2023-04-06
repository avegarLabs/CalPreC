package models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Subgrupo {
    private int id;
    private String codigosub;
    private String descripcionsub;
    private int grupoId;
    private List<Renglonvariante> renglonvariantesById;
    private Grupo grupoByGrupoId;

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
    @Column(name = "codigosub", nullable = false, length = 5)
    public String getCodigosub() {
        return codigosub;
    }

    public void setCodigosub(String codigosub) {
        this.codigosub = codigosub;
    }

    @Basic
    @Column(name = "descripcionsub", nullable = false, length = 250)
    public String getDescripcionsub() {
        return descripcionsub;
    }

    public void setDescripcionsub(String descripcionsub) {
        this.descripcionsub = descripcionsub;
    }

    @Basic
    @Column(name = "grupo__id", nullable = false)
    public int getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(int grupoId) {
        this.grupoId = grupoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subgrupo subgrupo = (Subgrupo) o;
        return grupoId == subgrupo.grupoId &&
                Objects.equals(id, subgrupo.id) &&
                Objects.equals(codigosub, subgrupo.codigosub) &&
                Objects.equals(descripcionsub, subgrupo.descripcionsub);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigosub, descripcionsub, grupoId);
    }

    @OneToMany(mappedBy = "subgrupoBySubgrupoId", cascade = CascadeType.ALL)
    public List<Renglonvariante> getRenglonvariantesById() {
        return renglonvariantesById;
    }

    public void setRenglonvariantesById(List<Renglonvariante> renglonvariantesById) {
        this.renglonvariantesById = renglonvariantesById;
    }

    @ManyToOne
    @JoinColumn(name = "grupo__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Grupo getGrupoByGrupoId() {
        return grupoByGrupoId;
    }

    public void setGrupoByGrupoId(Grupo grupoByGrupoId) {
        this.grupoByGrupoId = grupoByGrupoId;
    }
}
