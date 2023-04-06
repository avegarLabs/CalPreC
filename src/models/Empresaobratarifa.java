package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Empresaobratarifa {
    private int id;
    private int obraId;
    private int empresaconstructoraId;
    private int tarifaId;
    private String grupo;
    private double escala;
    private double tarifa;
    private Double incremento;
    private double tarifaInc;

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
    @Column(name = "empresaconstructora_id", nullable = false)
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Basic
    @Column(name = "obra_id", nullable = false)
    public int getObraId() {
        return obraId;
    }

    public void setObraId(int obraId) {
        this.obraId = obraId;
    }

    @Basic
    @Column(name = "tarifa_id", nullable = false)
    public int getTarifaId() {
        return tarifaId;
    }

    public void setTarifaId(int salarioId) {
        this.tarifaId = salarioId;
    }

    @Basic
    @Column(name = "grupo", nullable = false, length = 5)
    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }


    @Basic
    @Column(name = "escala", nullable = true, precision = 0)
    public double getEscala() {
        return escala;
    }

    public void setEscala(double escala) {
        this.escala = escala;
    }

    @Basic
    @Column(name = "tarifa", nullable = true, precision = 0)
    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double iii) {
        this.tarifa = iii;
    }


    @Basic
    @Column(name = "tarifa_inc", nullable = true, precision = 0)
    public double getTarifaInc() {
        return tarifaInc;
    }

    public void setTarifaInc(double iii) {
        this.tarifaInc = iii;
    }


    @Basic
    @Column(name = "tasa_incremento", nullable = true, precision = 0)
    public Double getIncremento() {
        return incremento;
    }

    public void setIncremento(Double iii) {
        this.incremento = iii;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresaobratarifa that = (Empresaobratarifa) o;
        return id == that.id && obraId == that.obraId && empresaconstructoraId == that.empresaconstructoraId && tarifaId == that.tarifaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, obraId, empresaconstructoraId, tarifaId);
    }
}
