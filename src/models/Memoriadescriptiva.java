package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Memoriadescriptiva {
    private int id;
    private String titulo;
    private String texto;
    private int unidadobraId;
    private Unidadobra unidadobraByUnidadobraId;

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
    @Column(name = "titulo", nullable = false, length = 100)
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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
    @Column(name = "unidadobra__id", nullable = false)
    public int getUnidadobraId() {
        return unidadobraId;
    }

    public void setUnidadobraId(int unidadobraId) {
        this.unidadobraId = unidadobraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Memoriadescriptiva that = (Memoriadescriptiva) o;
        return id == that.id &&
                unidadobraId == that.unidadobraId &&
                Objects.equals(titulo, that.titulo) &&
                Objects.equals(texto, that.texto);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, titulo, texto, unidadobraId);
    }

    @ManyToOne
    @JoinColumn(name = "unidadobra__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Unidadobra getUnidadobraByUnidadobraId() {
        return unidadobraByUnidadobraId;
    }

    public void setUnidadobraByUnidadobraId(Unidadobra unidadobraByUnidadobraId) {
        this.unidadobraByUnidadobraId = unidadobraByUnidadobraId;
    }
}
