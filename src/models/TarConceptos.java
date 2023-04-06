package models;

import javax.persistence.*;

@Entity
public class TarConceptos {
    private int id;
    private String descripcion;
    private double valor;
    private int tarifaId;
    private TarifaSalarial conceptsByEscala;


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
    @Column(name = "descripcion", nullable = false, length = 255)
    public String getDescripcion() {
        return descripcion;
    }


    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "valor", precision = 0)
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

    public void setTarifaId(int tarifaId) {
        this.tarifaId = tarifaId;
    }

    @ManyToOne
    @JoinColumn(name = "tarifa__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public TarifaSalarial getConceptsByEscala() {
        return conceptsByEscala;
    }

    public void setConceptsByEscala(TarifaSalarial conceptsByEscala) {
        this.conceptsByEscala = conceptsByEscala;
    }

}
