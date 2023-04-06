package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class Empresaconstructora {
    protected Collection<Brigadaconstruccion> brigadaconstruccionsById;
    protected Collection<Empresaobra> empresaobrasById;
    private int id;
    private String codigo;
    private String descripcion;
    private double cuenta731;
    private double cuenta822;
    private double pbruta;
    private List<Empresagastos> empresagastosById;
    private Collection<Unidadobra> unidadobrasById;
    private Collection<Empresaobraconcepto> empresaobraconceptosById;
    private Collection<Empresaobrasubconcepto> empresaobrasubconceptosById;
    private Collection<Empresaobrasubsubconcepto> empresaobrasubsubconceptosById;
    private Collection<Empresaobrasubsubsubconcepto> empresaobrasubsubsubconceptoCollectionById;
    private Collection<Nivelespecifico> nivelespecificosById;
    private Collection<Coeficientesequipos> coeficientesequiposById;

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
    @Column(name = "codigo", nullable = false, length = 5)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "descripcion", nullable = false, length = 50)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "cuenta731", nullable = false, precision = 0)
    public double getCuenta731() {
        return cuenta731;
    }

    public void setCuenta731(double cuenta731) {
        this.cuenta731 = cuenta731;
    }

    @Basic
    @Column(name = "cuenta822", nullable = false, precision = 0)
    public double getCuenta822() {
        return cuenta822;
    }

    public void setCuenta822(double cuenta822) {
        this.cuenta822 = cuenta822;
    }

    @Basic
    @Column(name = "pbruta", nullable = false, precision = 0)
    public double getPbruta() {
        return pbruta;
    }

    public void setPbruta(double pbruta) {
        this.pbruta = pbruta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresaconstructora that = (Empresaconstructora) o;
        return id == that.id &&
                Double.compare(that.cuenta731, cuenta731) == 0 &&
                Double.compare(that.cuenta822, cuenta822) == 0 &&
                Double.compare(that.pbruta, pbruta) == 0 &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, descripcion, cuenta731, cuenta822, pbruta);
    }

    @OneToMany(targetEntity = Brigadaconstruccion.class)
    public Collection<Brigadaconstruccion> getBrigadaconstruccionsById() {
        return brigadaconstruccionsById;
    }

    public void setBrigadaconstruccionsById(Collection<Brigadaconstruccion> brigadaconstruccionsById) {
        this.brigadaconstruccionsById = brigadaconstruccionsById;
    }

    @OneToMany(mappedBy = "empresaconstructoraByEmpresaconstructoraId")
    public Collection<Empresaobra> getEmpresaobrasById() {
        return empresaobrasById;
    }

    public void setEmpresaobrasById(Collection<Empresaobra> empresaobrasById) {
        this.empresaobrasById = empresaobrasById;
    }

    public void addBrigadas(Brigadaconstruccion brigadaconstruccion) {
        brigadaconstruccion.setEmpresaconstructoraByEmpresaconstructoraId(this);
        this.getBrigadaconstruccionsById().add(brigadaconstruccion);
    }

    @OneToMany(mappedBy = "empresaconstructoraByEmpresaconstructoraId")
    public List<Empresagastos> getEmpresagastosById() {
        return empresagastosById;
    }

    public void setEmpresagastosById(List<Empresagastos> empresagastosById) {
        this.empresagastosById = empresagastosById;
    }

    @OneToMany(mappedBy = "empresaconstructoraByEmpresaconstructoraId")
    public Collection<Unidadobra> getUnidadobrasById() {
        return unidadobrasById;
    }

    public void setUnidadobrasById(Collection<Unidadobra> unidadobrasById) {
        this.unidadobrasById = unidadobrasById;
    }

    @OneToMany(mappedBy = "empresaconstructoraByEmpresaconstructoraId")
    public Collection<Empresaobraconcepto> getEmpresaobraconceptosById() {
        return empresaobraconceptosById;
    }

    public void setEmpresaobraconceptosById(Collection<Empresaobraconcepto> empresaobraconceptosById) {
        this.empresaobraconceptosById = empresaobraconceptosById;
    }

    @OneToMany(mappedBy = "empresaconstructoraByEmpresaconstructoraId")
    public Collection<Empresaobrasubconcepto> getEmpresaobrasubconceptosById() {
        return empresaobrasubconceptosById;
    }

    public void setEmpresaobrasubconceptosById(Collection<Empresaobrasubconcepto> empresaobrasubconceptosById) {
        this.empresaobrasubconceptosById = empresaobrasubconceptosById;
    }

    @OneToMany(mappedBy = "empresaconstructoraById")
    public Collection<Empresaobrasubsubconcepto> getEmpresaobrasubsubconceptosById() {
        return empresaobrasubsubconceptosById;
    }

    public void setEmpresaobrasubsubconceptosById(Collection<Empresaobrasubsubconcepto> empresaobrasubsubconceptosById) {
        this.empresaobrasubsubconceptosById = empresaobrasubsubconceptosById;
    }

    @OneToMany(mappedBy = "empresaconstructoraById")
    public Collection<Empresaobrasubsubsubconcepto> getEmpresaobrasubsubsubconceptoCollectionById() {
        return empresaobrasubsubsubconceptoCollectionById;
    }

    public void setEmpresaobrasubsubsubconceptoCollectionById(Collection<Empresaobrasubsubsubconcepto> empresaobrasubsubconceptosById) {
        this.empresaobrasubsubsubconceptoCollectionById = empresaobrasubsubconceptosById;
    }


    @OneToMany(mappedBy = "empresaconstructoraByEmpresaconstructoraId")
    public Collection<Nivelespecifico> getNivelespecificosById() {
        return nivelespecificosById;
    }

    public void setNivelespecificosById(Collection<Nivelespecifico> nivelespecificosById) {
        this.nivelespecificosById = nivelespecificosById;
    }

    @OneToMany(mappedBy = "empresaconstructoraByEmpresaconstructoraId")
    public Collection<Coeficientesequipos> getCoeficientesequiposById() {
        return coeficientesequiposById;
    }

    public void setCoeficientesequiposById(Collection<Coeficientesequipos> coeficientesequiposById) {
        this.coeficientesequiposById = coeficientesequiposById;
    }

    @Override
    public String toString() {
        return codigo + " " + descripcion;
    }
}
