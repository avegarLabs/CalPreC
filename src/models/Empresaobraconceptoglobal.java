package models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Empresaobraconceptoglobal {
    private int id;
    private int obraId;
    private int conceptoId;
    private double valor;
    private Date fecha;
    private String pbase;
    private String descripcion;

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
    @Column(name = "obra_id", nullable = false)
    public int getObraId() {
        return obraId;
    }

    public void setObraId(int obraId) {
        this.obraId = obraId;
    }

    @Basic
    @Column(name = "concepto_id", nullable = false)
    public int getConceptoId() {
        return conceptoId;
    }

    public void setConceptoId(int conceptoId) {
        this.conceptoId = conceptoId;
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
    @Column(name = "fecha", nullable = false)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Basic
    @Column(name = "pbase", nullable = false, length = -1)
    public String getPbase() {
        return pbase;
    }

    public void setPbase(String pbase) {
        this.pbase = pbase;
    }

    @Basic
    @Column(name = "descripcion", nullable = true, length = 10)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresaobraconceptoglobal that = (Empresaobraconceptoglobal) o;
        return id == that.id &&
                obraId == that.obraId &&
                conceptoId == that.conceptoId &&
                Double.compare(that.valor, valor) == 0 &&
                Objects.equals(fecha, that.fecha) &&
                Objects.equals(pbase, that.pbase) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, obraId, conceptoId, valor, fecha, pbase, descripcion);
    }
}
