package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(UnidadobrarenglonPK.class)
public class Unidadobrarenglon {
    private int unidadobraId;
    private int renglonvarianteId;
    private Unidadobra unidadobraByUnidadobraId;
    private Renglonvariante renglonvarianteByRenglonvarianteId;
    private Double cantRv;
    private Double costMat;
    private Double costMano;
    private Double costEquip;
    private String conMat;
    private Double salariomn;
    private Double salariocuc;

    @Id
    @Column(name = "unidadobra__id", nullable = false)
    public int getUnidadobraId() {
        return unidadobraId;
    }


    public void setUnidadobraId(int unidadobraId) {
        this.unidadobraId = unidadobraId;
    }

    @Id
    @Column(name = "renglonvariante__id", nullable = false)
    public int getRenglonvarianteId() {
        return renglonvarianteId;
    }

    public void setRenglonvarianteId(int renglonvarianteId) {
        this.renglonvarianteId = renglonvarianteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unidadobrarenglon that = (Unidadobrarenglon) o;
        return unidadobraId == that.unidadobraId &&
                renglonvarianteId == that.renglonvarianteId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(unidadobraId, renglonvarianteId);
    }

    @ManyToOne
    @JoinColumn(name = "unidadobra__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Unidadobra getUnidadobraByUnidadobraId() {
        return unidadobraByUnidadobraId;
    }

    public void setUnidadobraByUnidadobraId(Unidadobra unidadobraByUnidadobraId) {
        this.unidadobraByUnidadobraId = unidadobraByUnidadobraId;
    }

    @ManyToOne
    @JoinColumn(name = "renglonvariante__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Renglonvariante getRenglonvarianteByRenglonvarianteId() {
        return renglonvarianteByRenglonvarianteId;
    }

    public void setRenglonvarianteByRenglonvarianteId(Renglonvariante renglonvarianteByRenglonvarianteId) {
        this.renglonvarianteByRenglonvarianteId = renglonvarianteByRenglonvarianteId;
    }

    @Basic
    @Column(name = "cantrv", nullable = true, precision = 0)
    public Double getCantRv() {
        return cantRv;
    }

    public void setCantRv(Double cantRv) {
        this.cantRv = cantRv;
    }

    @Basic
    @Column(name = "costmat", nullable = true, precision = 0)
    public Double getCostMat() {
        return costMat;
    }

    public void setCostMat(Double costMat) {
        this.costMat = costMat;
    }

    @Basic
    @Column(name = "costmano", nullable = true, precision = 0)
    public Double getCostMano() {
        return costMano;
    }

    public void setCostMano(Double costMano) {
        this.costMano = costMano;
    }

    @Basic
    @Column(name = "costequip", nullable = true, precision = 0)
    public Double getCostEquip() {
        return costEquip;
    }

    public void setCostEquip(Double costEquip) {
        this.costEquip = costEquip;
    }

    @Basic
    @Column(name = "conmat", nullable = true, length = 2)
    public String getConMat() {
        return conMat;
    }

    public void setConMat(String conMat) {
        this.conMat = conMat;
    }


    @Basic
    @Column(name = "salariomn")
    public Double getSalariomn() {
        return salariomn;
    }

    public void setSalariomn(Double salariomn) {
        this.salariomn = salariomn;
    }

    @Basic
    @Column(name = "salariocuc")
    public Double getSalariocuc() {
        return salariocuc;
    }

    public void setSalariocuc(Double salariocuc) {
        this.salariocuc = salariocuc;
    }
}
