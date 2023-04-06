package models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "unidadobra", schema = "public", catalog = "pcwindb")
public class UnidadobraEntity {
    private String renglonbase;

    @Basic
    @Column(name = "renglonbase")
    public String getRenglonbase() {
        return renglonbase;
    }

    public void setRenglonbase(String renglonbase) {
        this.renglonbase = renglonbase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnidadobraEntity that = (UnidadobraEntity) o;
        return Objects.equals(renglonbase, that.renglonbase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(renglonbase);
    }
}
