package models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(EmpresaobraPK.class)
public class Empresaobra implements Serializable {
    private int empresaconstructoraId;
    private int obraId;
    private Empresaconstructora empresaconstructoraByEmpresaconstructoraId;
    private Obra obraByObraId;

    @Id
    @Column(name = "empresaconstructora__id", nullable = false)
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Id
    @Column(name = "obra__id", nullable = false)
    public int getObraId() {
        return obraId;
    }

    public void setObraId(int obraId) {
        this.obraId = obraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresaobra that = (Empresaobra) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                obraId == that.obraId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(empresaconstructoraId, obraId);
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresaconstructora__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Empresaconstructora getEmpresaconstructoraByEmpresaconstructoraId() {
        return empresaconstructoraByEmpresaconstructoraId;
    }

    public void setEmpresaconstructoraByEmpresaconstructoraId(Empresaconstructora empresaconstructoraByEmpresaconstructoraId) {
        this.empresaconstructoraByEmpresaconstructoraId = empresaconstructoraByEmpresaconstructoraId;
    }

    @ManyToOne
    @JoinColumn(name = "obra__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Obra getObraByObraId() {
        return obraByObraId;
    }

    public void setObraByObraId(Obra obraByObraId) {
        this.obraByObraId = obraByObraId;
    }
}
