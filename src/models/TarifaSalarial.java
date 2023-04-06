package models;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class TarifaSalarial {
    private int id;
    private String code;
    private String descripcion;
    private double autEspecial;
    private double antiguedad;
    private double vacaciones;
    private double nomina;
    private double segSocial;
    private String moneda;

    private Collection<GruposEscalas> gruposEscalasCollection;
    private Collection<Obra> obrasById;
    private Collection<TarConceptos> tarConceptosCollection;

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
    @Column(name = "code", nullable = false, length = 5)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
    @Column(name = "autespecial", precision = 0)
    public double getAutEspecial() {
        return autEspecial;
    }

    public void setAutEspecial(double valor) {
        this.autEspecial = valor;
    }

    @Basic
    @Column(name = "antiguedad", precision = 0)
    public double getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(double valor) {
        this.antiguedad = valor;
    }

    @Basic
    @Column(name = "vacaciones", precision = 0)
    public double getVacaciones() {
        return vacaciones;
    }

    public void setVacaciones(double valor) {
        this.vacaciones = valor;
    }

    @Basic
    @Column(name = "nomina", precision = 0)
    public double getNomina() {
        return nomina;
    }

    public void setNomina(double valor) {
        this.nomina = valor;
    }

    @Basic
    @Column(name = "segsocial", precision = 0)
    public double getSegSocial() {
        return segSocial;
    }

    public void setSegSocial(double valor) {
        this.segSocial = valor;
    }

    @OneToMany(mappedBy = "grupoByEscala")
    public Collection<GruposEscalas> getGruposEscalasCollection() {
        return gruposEscalasCollection;
    }

    public void setGruposEscalasCollection(Collection<GruposEscalas> obrasById) {
        this.gruposEscalasCollection = obrasById;
    }

    @OneToMany(mappedBy = "tarifaSalarialByTarifa")
    public Collection<Obra> getObrasById() {
        return obrasById;
    }

    public void setObrasById(Collection<Obra> obrasById) {
        this.obrasById = obrasById;
    }

    @Basic
    @Column(name = "moneda", nullable = true, length = 5)
    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    @OneToMany(mappedBy = "conceptsByEscala")
    public Collection<TarConceptos> getTarConceptosCollection() {
        return tarConceptosCollection;
    }

    public void setTarConceptosCollection(Collection<TarConceptos> tarConceptos) {
        this.tarConceptosCollection = tarConceptos;
    }
}
