package models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class Recursos {
    private int id;
    private String descripcion;
    private String tipo;
    private String codigo;
    private String um;
    private double peso;
    private double preciomn;
    private Double preciomlc;
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
    private Double mermaprod;
    private Double mermatrans;
    private Double mermamanip;
    private Double otrasmermas;
    private String grupoescala;
    private List<Juegorecursos> juegorecursosById;
    private List<Renglonrecursos> renglonrecursosById;
    private List<Semielaboradosrecursos> semielaboradosrecursosById;
    private String pertenece;
    private Double cpo;
    private Double cpe;
    private Double cet;
    private Double otra;
    private Collection<Coeficientesequipos> coeficientesequiposById;
    private Double salario;
    private int active;

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
    @Column(name = "tipo", nullable = false, length = 3)
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Basic
    @Column(name = "codigo", nullable = false, length = 50)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
    @Column(name = "preciomlc", nullable = true, precision = 0)
    public Double getPreciomlc() {
        return preciomlc;
    }

    public void setPreciomlc(Double preciomlc) {
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
    @Column(name = "mermaprod", nullable = true, precision = 0)
    public Double getMermaprod() {
        return mermaprod;
    }

    public void setMermaprod(Double mermaprod) {
        this.mermaprod = mermaprod;
    }

    @Basic
    @Column(name = "mermatrans", nullable = true, precision = 0)
    public Double getMermatrans() {
        return mermatrans;
    }

    public void setMermatrans(Double mermatrans) {
        this.mermatrans = mermatrans;
    }

    @Basic
    @Column(name = "mermamanip", nullable = true, precision = 0)
    public Double getMermamanip() {
        return mermamanip;
    }

    public void setMermamanip(Double mermamanip) {
        this.mermamanip = mermamanip;
    }

    @Basic
    @Column(name = "otrasmermas", nullable = true, precision = 0)
    public Double getOtrasmermas() {
        return otrasmermas;
    }

    public void setOtrasmermas(Double otrasmermas) {
        this.otrasmermas = otrasmermas;
    }

    @Basic
    @Column(name = "grupoescala", nullable = false, length = 7)
    public String getGrupoescala() {
        return grupoescala;
    }

    public void setGrupoescala(String grupoescala) {
        this.grupoescala = grupoescala;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recursos recursos = (Recursos) o;
        return Objects.equals(codigo, recursos.codigo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, descripcion, tipo, codigo, um, peso, preciomn, preciomlc, costomat, costmano, costequip, ho, ha, he, cemento, arido, asfalto, carga, prefab, mermaprod, mermatrans, mermamanip, otrasmermas, grupoescala);
    }

    @OneToMany(mappedBy = "recursosByRecursosId", cascade = CascadeType.ALL)
    public List<Juegorecursos> getJuegorecursosById() {
        return juegorecursosById;
    }

    public void setJuegorecursosById(List<Juegorecursos> juegorecursosById) {
        this.juegorecursosById = juegorecursosById;
    }

    @OneToMany(mappedBy = "recursosByRecursosId", cascade = CascadeType.ALL)
    public List<Renglonrecursos> getRenglonrecursosById() {
        return renglonrecursosById;
    }

    public void setRenglonrecursosById(List<Renglonrecursos> renglonrecursosById) {
        this.renglonrecursosById = renglonrecursosById;
    }

    @OneToMany(mappedBy = "recursosByRecursosId", cascade = CascadeType.ALL)
    public List<Semielaboradosrecursos> getSemielaboradosrecursosById() {
        return semielaboradosrecursosById;
    }

    public void setSemielaboradosrecursosById(List<Semielaboradosrecursos> semielaboradosrecursosById) {
        this.semielaboradosrecursosById = semielaboradosrecursosById;
    }

    @Basic
    @Column(name = "pertenece", nullable = true, length = 5)
    public String getPertenece() {
        return pertenece;
    }

    public void setPertenece(String pertenece) {
        this.pertenece = pertenece;
    }

    @Basic
    @Column(name = "cpo", nullable = true, precision = 0)
    public Double getCpo() {
        return cpo;
    }

    public void setCpo(Double cpo) {
        this.cpo = cpo;
    }

    @Basic
    @Column(name = "cpe", nullable = true, precision = 0)
    public Double getCpe() {
        return cpe;
    }

    public void setCpe(Double cpe) {
        this.cpe = cpe;
    }

    @Basic
    @Column(name = "cet", nullable = true, precision = 0)
    public Double getCet() {
        return cet;
    }

    public void setCet(Double cet) {
        this.cet = cet;
    }

    @Basic
    @Column(name = "otra", nullable = true, precision = 0)
    public Double getOtra() {
        return otra;
    }

    public void setOtra(Double otra) {
        this.otra = otra;
    }

    @OneToMany(mappedBy = "recursosByRecursosId")
    public Collection<Coeficientesequipos> getCoeficientesequiposById() {
        return coeficientesequiposById;
    }

    public void setCoeficientesequiposById(Collection<Coeficientesequipos> coeficientesequiposById) {
        this.coeficientesequiposById = coeficientesequiposById;
    }

    @Override
    public String toString() {
        return codigo + " " + descripcion.trim() + "(" + new BigDecimal(String.format("%.2f", preciomn)).doubleValue() + "/" + um + ")";
    }

    @Basic
    @Column(name = "salario")
    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }


    @Basic
    @Column(name = "active")
    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
