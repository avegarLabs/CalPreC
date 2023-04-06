package models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Registro {
    private int id;
    private String tipo;
    private Date fecha;
    private String code;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "tipo", nullable = true, length = 20)
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Basic
    @Column(name = "fecha", nullable = true)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registro registro = (Registro) o;
        return id == registro.id &&
                Objects.equals(tipo, registro.tipo) &&
                Objects.equals(fecha, registro.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipo, fecha);
    }

    @Basic
    @Column(name = "code", nullable = true, length = 25)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
