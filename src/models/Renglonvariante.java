package models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Renglonvariante {
    private int id;
    private String descripcion;
    private String especificaciones;
    private String um;
    private double peso;
    private double preciomn;
    private double preciomlc;
    private double costomat;
    private double costmano;
    private double costequip;
    private double ho;
    private double ha;
    private double he;
    private double cemento;
    private double arido;
    private double asfalto;
    private double carga;
    private double prefab;
    private double mermaprod;
    private double mermatrans;
    private double mermamanip;
    private double otrasmermas;
    private double hh;
    private String codigo;
    private int subgrupoId;
    private List<Renglonrecursos> renglonrecursosById;
    private List<Renglonsemielaborados> renglonsemielaboradosById;
    private Subgrupo subgrupoBySubgrupoId;
    private List<Unidadobrarenglon> unidadobrarenglonsById;
    private List<Renglonjuego> renglonjuegosById;
    private List<Renglonnivelespecifico> renglonnivelespecificosById;
    private Double salario;
    private List<Planrenglonvariante> planrenglonvariantesById;
    private int rs;

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
    @Column(name = "descripcion", nullable = false, length = 250)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "especificaciones", length = 2500)
    public String getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(String descripcion) {
        this.especificaciones = descripcion;
    }

    @Basic
    @Column(name = "um", nullable = false, length = 10)
    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    @Basic
    @Column(name = "peso", nullable = false, precision = 0)
    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    @Basic
    @Column(name = "preciomn", nullable = false, precision = 0)
    public double getPreciomn() {
        return preciomn;
    }

    public void setPreciomn(double preciomn) {
        this.preciomn = preciomn;
    }

    @Basic
    @Column(name = "preciomlc", nullable = false, precision = 0)
    public double getPreciomlc() {
        return preciomlc;
    }

    public void setPreciomlc(double preciomlc) {
        this.preciomlc = preciomlc;
    }

    @Basic
    @Column(name = "costomat", nullable = false, precision = 0)
    public double getCostomat() {
        return costomat;
    }

    public void setCostomat(double costomat) {
        this.costomat = costomat;
    }

    @Basic
    @Column(name = "costmano", nullable = false, precision = 0)
    public double getCostmano() {
        return costmano;
    }

    public void setCostmano(double costmano) {
        this.costmano = costmano;
    }

    @Basic
    @Column(name = "costequip", nullable = false, precision = 0)
    public double getCostequip() {
        return costequip;
    }

    public void setCostequip(double costequip) {
        this.costequip = costequip;
    }

    @Basic
    @Column(name = "ho", nullable = false, precision = 0)
    public double getHo() {
        return ho;
    }

    public void setHo(double ho) {
        this.ho = ho;
    }

    @Basic
    @Column(name = "ha", nullable = false, precision = 0)
    public double getHa() {
        return ha;
    }

    public void setHa(double ha) {
        this.ha = ha;
    }

    @Basic
    @Column(name = "he", nullable = false, precision = 0)
    public double getHe() {
        return he;
    }

    public void setHe(double he) {
        this.he = he;
    }

    @Basic
    @Column(name = "cemento", nullable = false, precision = 0)
    public double getCemento() {
        return cemento;
    }

    public void setCemento(double cemento) {
        this.cemento = cemento;
    }

    @Basic
    @Column(name = "arido", nullable = false, precision = 0)
    public double getArido() {
        return arido;
    }

    public void setArido(double arido) {
        this.arido = arido;
    }

    @Basic
    @Column(name = "asfalto", nullable = false, precision = 0)
    public double getAsfalto() {
        return asfalto;
    }

    public void setAsfalto(double asfalto) {
        this.asfalto = asfalto;
    }

    @Basic
    @Column(name = "carga", nullable = false, precision = 0)
    public double getCarga() {
        return carga;
    }

    public void setCarga(double carga) {
        this.carga = carga;
    }

    @Basic
    @Column(name = "prefab", nullable = false, precision = 0)
    public double getPrefab() {
        return prefab;
    }

    public void setPrefab(double prefab) {
        this.prefab = prefab;
    }

    @Basic
    @Column(name = "mermaprod", nullable = false, precision = 0)
    public double getMermaprod() {
        return mermaprod;
    }

    public void setMermaprod(double mermaprod) {
        this.mermaprod = mermaprod;
    }

    @Basic
    @Column(name = "mermatrans", nullable = false, precision = 0)
    public double getMermatrans() {
        return mermatrans;
    }

    public void setMermatrans(double mermatrans) {
        this.mermatrans = mermatrans;
    }

    @Basic
    @Column(name = "mermamanip", nullable = false, precision = 0)
    public double getMermamanip() {
        return mermamanip;
    }

    public void setMermamanip(double mermamanip) {
        this.mermamanip = mermamanip;
    }

    @Basic
    @Column(name = "otrasmermas", nullable = false, precision = 0)
    public double getOtrasmermas() {
        return otrasmermas;
    }

    public void setOtrasmermas(double otrasmermas) {
        this.otrasmermas = otrasmermas;
    }

    @Basic
    @Column(name = "hh", nullable = false, precision = 0)
    public double getHh() {
        return hh;
    }

    public void setHh(Double hh) {
        this.hh = hh;
    }

    public void setHh(double hh) {
        this.hh = hh;
    }

    @Basic
    @Column(name = "codigo", nullable = false, length = 10)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "subgrupo__id", nullable = false, length = -1)
    public int getSubgrupoId() {
        return subgrupoId;
    }

    public void setSubgrupoId(int subgrupoId) {
        this.subgrupoId = subgrupoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Renglonvariante that = (Renglonvariante) o;
        return id == that.id &&
                Double.compare(that.peso, peso) == 0 &&
                Double.compare(that.preciomn, preciomn) == 0 &&
                Double.compare(that.preciomlc, preciomlc) == 0 &&
                Double.compare(that.costomat, costomat) == 0 &&
                Double.compare(that.costmano, costmano) == 0 &&
                Double.compare(that.costequip, costequip) == 0 &&
                Double.compare(that.ho, ho) == 0 &&
                Double.compare(that.ha, ha) == 0 &&
                Double.compare(that.he, he) == 0 &&
                Double.compare(that.cemento, cemento) == 0 &&
                Double.compare(that.arido, arido) == 0 &&
                Double.compare(that.asfalto, asfalto) == 0 &&
                Double.compare(that.carga, carga) == 0 &&
                Double.compare(that.prefab, prefab) == 0 &&
                Double.compare(that.mermaprod, mermaprod) == 0 &&
                Double.compare(that.mermatrans, mermatrans) == 0 &&
                Double.compare(that.mermamanip, mermamanip) == 0 &&
                Double.compare(that.otrasmermas, otrasmermas) == 0 &&
                Double.compare(that.hh, hh) == 0 &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(um, that.um) &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(subgrupoId, that.subgrupoId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, descripcion, um, peso, preciomn, preciomlc, costomat, costmano, costequip, ho, ha, he, cemento, arido, asfalto, carga, prefab, mermaprod, mermatrans, mermamanip, otrasmermas, codigo, subgrupoId);
    }

    @OneToMany(mappedBy = "renglonvarianteByRenglonvarianteId")
    public List<Renglonrecursos> getRenglonrecursosById() {
        return renglonrecursosById;
    }

    public void setRenglonrecursosById(List<Renglonrecursos> renglonrecursosById) {
        this.renglonrecursosById = renglonrecursosById;
    }

    @OneToMany(mappedBy = "renglonvarianteByRenglonvarianteId")
    public List<Renglonsemielaborados> getRenglonsemielaboradosById() {
        return renglonsemielaboradosById;
    }

    public void setRenglonsemielaboradosById(List<Renglonsemielaborados> renglonsemielaboradosById) {
        this.renglonsemielaboradosById = renglonsemielaboradosById;
    }

    @ManyToOne
    @JoinColumn(name = "subgrupo__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Subgrupo getSubgrupoBySubgrupoId() {
        return subgrupoBySubgrupoId;
    }

    public void setSubgrupoBySubgrupoId(Subgrupo subgrupoBySubgrupoId) {
        this.subgrupoBySubgrupoId = subgrupoBySubgrupoId;
    }

    @OneToMany(mappedBy = "renglonvarianteByRenglonvarianteId")
    public List<Unidadobrarenglon> getUnidadobrarenglonsById() {
        return unidadobrarenglonsById;
    }

    public void setUnidadobrarenglonsById(List<Unidadobrarenglon> unidadobrarenglonsById) {
        this.unidadobrarenglonsById = unidadobrarenglonsById;
    }


    @OneToMany(mappedBy = "renglonvarianteByRenglonvarianteId")
    public List<Renglonjuego> getRenglonjuegosById() {
        return renglonjuegosById;
    }

    public void setRenglonjuegosById(List<Renglonjuego> renglonjuegosById) {
        this.renglonjuegosById = renglonjuegosById;
    }

    @OneToMany(mappedBy = "renglonvarianteByRenglonvarianteId")
    public List<Renglonnivelespecifico> getRenglonnivelespecificosById() {
        return renglonnivelespecificosById;
    }

    public void setRenglonnivelespecificosById(List<Renglonnivelespecifico> renglonnivelespecificosById) {
        this.renglonnivelespecificosById = renglonnivelespecificosById;
    }

    @Basic
    @Column(name = "salario")
    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    @OneToMany(mappedBy = "renglonvarianteByRenglonvarianteId")
    public List<Planrenglonvariante> getPlanrenglonvariantesById() {
        return planrenglonvariantesById;
    }

    public void setPlanrenglonvariantesById(List<Planrenglonvariante> planrenglonvariantesById) {
        this.planrenglonvariantesById = planrenglonvariantesById;
    }

    @Basic
    @Column(name = "rs")
    public int getRs() {
        return rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }
}
