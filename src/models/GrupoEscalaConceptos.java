package models;

import javax.persistence.*;

@Entity
public class GrupoEscalaConceptos {
    private int id;
    private int grupoId;
    private int conceptoId;
    private double valor;
    private int tipo;
    private GruposEscalas gruposByEscala;
    private TarConceptosBases conceptsByEscala;


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
    @Column(name = "grupo__id", nullable = false)
    public int getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(int grupoid) {
        this.grupoId = grupoid;
    }

    @Basic
    @Column(name = "concepto__id", nullable = false)
    public int getConceptoId() {
        return conceptoId;
    }

    public void setConceptoId(int conceptoid) {
        this.conceptoId = conceptoid;
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
    @Column(name = "tipo", nullable = false)
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @ManyToOne
    @JoinColumn(name = "grupo__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public GruposEscalas getGruposByEscala() {
        return gruposByEscala;
    }

    public void setGruposByEscala(GruposEscalas conceptsByEscala) {
        this.gruposByEscala = conceptsByEscala;
    }

    @ManyToOne
    @JoinColumn(name = "concepto__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public TarConceptosBases getConceptsByEscala() {
        return conceptsByEscala;
    }

    public void setConceptsByEscala(TarConceptosBases conceptsByEscala) {
        this.conceptsByEscala = conceptsByEscala;
    }


}
