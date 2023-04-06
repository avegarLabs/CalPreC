package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class GruposEscalas {
    private int id;
    private String grupo;
    private double valorBase;
    private double valor;
    private int tarifaId;
    private TarifaSalarial grupoByEscala;

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "grupo", nullable = false, length = 5)
    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String descripcion) {
        this.grupo = descripcion;
    }

    @Basic
    @Column(name = "valobase", precision = 0)
    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valor) {
        this.valorBase = valor;
    }

    @Basic
    @Column(name = "valor", nullable = false, precision = 0)
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Basic
    @Column(name = "tarifa__id", nullable = false)
    public int getTarifaId() {
        return tarifaId;
    }

    public void setTarifaId(int salarioId) {
        this.tarifaId = salarioId;
    }


    @ManyToOne
    @JoinColumn(name = "tarifa__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public TarifaSalarial getGrupoByEscala() {
        return grupoByEscala;
    }

    public void setGrupoByEscala(TarifaSalarial grupoByEscala) {
        this.grupoByEscala = grupoByEscala;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GruposEscalas that = (GruposEscalas) o;
        return id == that.id && Double.compare(that.valor, valor) == 0 && tarifaId == that.tarifaId && Objects.equals(grupo, that.grupo) && Objects.equals(grupoByEscala, that.grupoByEscala);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grupo, valor, tarifaId, grupoByEscala);
    }
}
