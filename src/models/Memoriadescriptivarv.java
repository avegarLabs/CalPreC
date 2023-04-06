package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Memoriadescriptivarv {
    private int id;
    private String texto;
    private String titulo;
    private int nivelespecificoId;
    private Nivelespecifico nivelespecificoByNivelespecificoId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "texto", nullable = false, length = 1000)
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Basic
    @Column(name = "titulo", nullable = false, length = 100)
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Basic
    @Column(name = "nivelespecifico__id", nullable = false)
    public int getNivelespecificoId() {
        return nivelespecificoId;
    }

    public void setNivelespecificoId(int nivelespecificoId) {
        this.nivelespecificoId = nivelespecificoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Memoriadescriptivarv that = (Memoriadescriptivarv) o;
        return id == that.id &&
                nivelespecificoId == that.nivelespecificoId &&
                Objects.equals(texto, that.texto) &&
                Objects.equals(titulo, that.titulo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, texto, titulo, nivelespecificoId);
    }

    @ManyToOne
    @JoinColumn(name = "nivelespecifico__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Nivelespecifico getNivelespecificoByNivelespecificoId() {
        return nivelespecificoByNivelespecificoId;
    }

    public void setNivelespecificoByNivelespecificoId(Nivelespecifico nivelespecificoByNivelespecificoId) {
        this.nivelespecificoByNivelespecificoId = nivelespecificoByNivelespecificoId;
    }
}
