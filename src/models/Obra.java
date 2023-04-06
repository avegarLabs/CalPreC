package models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Obra implements Serializable {
    private int id;
    private String codigo;
    private String tipo;
    private String descripion;
    private int entidadId;
    private int inversionistaId;
    private int salarioId;
    private Integer tipoobraId;
    private int tarifaId;
    private Collection<Empresaobra> empresaobrasById;
    private Entidad entidadByEntidadId;
    private Inversionista inversionistaByInversionistaId;
    private Salario salarioBySalarioId;
    private TarifaSalarial tarifaSalarialByTarifa;
    private Tipoobra tipoobraByTipoobraId;
    private Collection<Zonas> zonasById;
    private Collection<Unidadobra> unidadobrasById;
    private Collection<Empresaobraconcepto> empresaobraconceptosById;
    private Collection<Empresaobrasubconcepto> empresaobrasubconceptosById;
    private Collection<Empresaobrasubsubconcepto> empresaobrasubsubconceptosById;
    private Collection<Empresaobrasubsubsubconcepto> empresaobrasubsubsubconceptoCollectionById;
    private Collection<Nivelespecifico> nivelespecificosById;
    private Integer defcostmat;
    private Collection<Coeficientesequipos> coeficientesequiposById;

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
    @Column(name = "codigo", nullable = false, length = 6)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "tipo", nullable = false, length = 3)
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Basic
    @Column(name = "descripion", nullable = false, length = 100)
    public String getDescripion() {
        return descripion;
    }

    public void setDescripion(String descripion) {
        this.descripion = descripion;
    }

    @Basic
    @Column(name = "entidad__id", nullable = false)
    public int getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(int entidadId) {
        this.entidadId = entidadId;
    }

    @Basic
    @Column(name = "inversionista__id", nullable = false)
    public int getInversionistaId() {
        return inversionistaId;
    }

    public void setInversionistaId(int inversionistaId) {
        this.inversionistaId = inversionistaId;
    }

    @Basic
    @Column(name = "salario__id", nullable = false)
    public int getSalarioId() {
        return salarioId;
    }

    public void setSalarioId(int salarioId) {
        this.salarioId = salarioId;
    }

    @Basic
    @Column(name = "tipoobra__id", nullable = true)
    public Integer getTipoobraId() {
        return tipoobraId;
    }

    public void setTipoobraId(Integer tipoobraId) {
        this.tipoobraId = tipoobraId;
    }

    @Basic
    @Column(name = "tarifa__id")
    public int getTarifaId() {
        return tarifaId;
    }

    public void setTarifaId(int tarifaId) {
        this.tarifaId = tarifaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Obra obra = (Obra) o;
        return id == obra.id &&
                entidadId == obra.entidadId &&
                inversionistaId == obra.inversionistaId &&
                salarioId == obra.salarioId &&
                Objects.equals(codigo, obra.codigo) &&
                Objects.equals(tipo, obra.tipo) &&
                Objects.equals(descripion, obra.descripion) &&
                Objects.equals(tipoobraId, obra.tipoobraId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, tipo, descripion, entidadId, inversionistaId, salarioId, tipoobraId);
    }

    @OneToMany(mappedBy = "obraByObraId", cascade = CascadeType.REMOVE)
    public Collection<Empresaobra> getEmpresaobrasById() {
        return empresaobrasById;
    }

    public void setEmpresaobrasById(Collection<Empresaobra> empresaobrasById) {
        this.empresaobrasById = empresaobrasById;
    }

    @ManyToOne
    @JoinColumn(name = "entidad__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Entidad getEntidadByEntidadId() {
        return entidadByEntidadId;
    }

    public void setEntidadByEntidadId(Entidad entidadByEntidadId) {
        this.entidadByEntidadId = entidadByEntidadId;
    }

    @ManyToOne
    @JoinColumn(name = "inversionista__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Inversionista getInversionistaByInversionistaId() {
        return inversionistaByInversionistaId;
    }

    public void setInversionistaByInversionistaId(Inversionista inversionistaByInversionistaId) {
        this.inversionistaByInversionistaId = inversionistaByInversionistaId;
    }

    @ManyToOne
    @JoinColumn(name = "salario__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Salario getSalarioBySalarioId() {
        return salarioBySalarioId;
    }

    public void setSalarioBySalarioId(Salario salarioBySalarioId) {
        this.salarioBySalarioId = salarioBySalarioId;
    }

    @ManyToOne
    @JoinColumn(name = "tarifa__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public TarifaSalarial getTarifaSalarialByTarifa() {
        return tarifaSalarialByTarifa;
    }

    public void setTarifaSalarialByTarifa(TarifaSalarial tarifaSalarialByTarifa) {
        this.tarifaSalarialByTarifa = tarifaSalarialByTarifa;
    }


    @ManyToOne
    @JoinColumn(name = "tipoobra__id", referencedColumnName = "id", updatable = false, insertable = false)
    public Tipoobra getTipoobraByTipoobraId() {
        return tipoobraByTipoobraId;
    }

    public void setTipoobraByTipoobraId(Tipoobra tipoobraByTipoobraId) {
        this.tipoobraByTipoobraId = tipoobraByTipoobraId;
    }

    @OneToMany(mappedBy = "obraByObraId", cascade = CascadeType.REMOVE)
    public Collection<Zonas> getZonasById() {
        return zonasById;
    }

    public void setZonasById(Collection<Zonas> zonasById) {
        this.zonasById = zonasById;
    }

    @OneToMany(mappedBy = "obraByObraId", cascade = CascadeType.REMOVE)
    public Collection<Unidadobra> getUnidadobrasById() {
        return unidadobrasById;
    }

    public void setUnidadobrasById(Collection<Unidadobra> unidadobrasById) {
        this.unidadobrasById = unidadobrasById;
    }

    @OneToMany(mappedBy = "obraByObraId", cascade = CascadeType.REMOVE)
    public Collection<Empresaobraconcepto> getEmpresaobraconceptosById() {
        return empresaobraconceptosById;
    }

    public void setEmpresaobraconceptosById(Collection<Empresaobraconcepto> empresaobraconceptosById) {
        this.empresaobraconceptosById = empresaobraconceptosById;
    }

    @OneToMany(mappedBy = "obraByObraId", cascade = CascadeType.REMOVE)
    public Collection<Empresaobrasubconcepto> getEmpresaobrasubconceptosById() {
        return empresaobrasubconceptosById;
    }

    public void setEmpresaobrasubconceptosById(Collection<Empresaobrasubconcepto> empresaobrasubconceptosById) {
        this.empresaobrasubconceptosById = empresaobrasubconceptosById;
    }

    @OneToMany(mappedBy = "obraByObraId", cascade = CascadeType.REMOVE)
    public Collection<Empresaobrasubsubconcepto> getEmpresaobrasubsubconceptosById() {
        return empresaobrasubsubconceptosById;
    }

    public void setEmpresaobrasubsubconceptosById(Collection<Empresaobrasubsubconcepto> empresaobrasubconceptosById) {
        this.empresaobrasubsubconceptosById = empresaobrasubconceptosById;
    }

    @OneToMany(mappedBy = "obraByObraId", cascade = CascadeType.REMOVE)
    public Collection<Empresaobrasubsubsubconcepto> getEmpresaobrasubsubsubconceptoCollectionById() {
        return empresaobrasubsubsubconceptoCollectionById;
    }

    public void setEmpresaobrasubsubsubconceptoCollectionById(Collection<Empresaobrasubsubsubconcepto> empresaobrasubconceptosById) {
        this.empresaobrasubsubsubconceptoCollectionById = empresaobrasubconceptosById;
    }

    @OneToMany(mappedBy = "obraByObraId", cascade = CascadeType.REMOVE)
    public Collection<Nivelespecifico> getNivelespecificosById() {
        return nivelespecificosById;
    }

    public void setNivelespecificosById(Collection<Nivelespecifico> nivelespecificosById) {
        this.nivelespecificosById = nivelespecificosById;
    }

    @Basic
    @Column(name = "defcostmat")
    public Integer getDefcostmat() {
        return defcostmat;
    }

    public void setDefcostmat(Integer defcostmat) {
        this.defcostmat = defcostmat;
    }

    @OneToMany(mappedBy = "obraByObraId")
    public Collection<Coeficientesequipos> getCoeficientesequiposById() {
        return coeficientesequiposById;
    }

    public void setCoeficientesequiposById(Collection<Coeficientesequipos> coeficientesequiposById) {
        this.coeficientesequiposById = coeficientesequiposById;
    }

    @Override
    public String toString() {
        return codigo + " " + descripion;
    }
}
