package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class NormasInd {
    private int id;
    private String codenorna;
    private int indcadorId;
    private IndicadorGrup indicadorGrupbyIndicador;

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
    @Column(name = "codenorna", nullable = false, length = 6)
    public String getCodenorna() {
        return codenorna;
    }

    public void setCodenorna(String codenorna) {
        this.codenorna = codenorna;
    }


    @Basic
    @Column(name = "indcadorId", nullable = false)
    public int getIndcadorId() {
        return indcadorId;
    }

    public void setIndcadorId(int indcadorId) {
        this.indcadorId = indcadorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NormasInd grupo = (NormasInd) o;
        return id == grupo.id &&
                indcadorId == grupo.indcadorId &&
                Objects.equals(codenorna, grupo.codenorna);
    }


    @ManyToOne
    @JoinColumn(name = "indcadorId", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public IndicadorGrup getIndicadorGrupbyIndicador() {
        return indicadorGrupbyIndicador;
    }

    public void setIndicadorGrupbyIndicador(IndicadorGrup indicadorGrup) {
        this.indicadorGrupbyIndicador = indicadorGrup;
    }


}
