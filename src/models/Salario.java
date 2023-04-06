package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Salario {
    private int id;
    private String descripcion;
    private double ii;
    private double iii;
    private double iv;
    private double v;
    private double vi;
    private double vii;
    private double viii;
    private double ix;
    private double x;
    private double xi;
    private double xii;
    private double xiii;
    private double xiv;
    private double xv;
    private double xvi;
    private double autesp;
    private double antiguedad;
    private double vacaciones;
    private double nomina;
    private double seguro;
    private double gtii;
    private double gtiii;
    private double gtiv;
    private double gtv;
    private double gtvi;
    private double gtvii;
    private double gtviii;
    private double gtix;
    private double gtx;
    private double gtxi;
    private double gtxii;
    private double gtxiii;
    private double gtxiv;
    private double gtxv;
    private double gtxvi;

    private Collection<Obra> obrasById;
    private String tag;

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
    @Column(name = "descripcion", nullable = false, length = -1)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "ii", nullable = false, precision = 0)
    public double getIi() {
        return ii;
    }

    public void setIi(double ii) {
        this.ii = ii;
    }

    @Basic
    @Column(name = "iii", nullable = false, precision = 0)
    public double getIii() {
        return iii;
    }

    public void setIii(double iii) {
        this.iii = iii;
    }

    @Basic
    @Column(name = "iv", nullable = false, precision = 0)
    public double getIv() {
        return iv;
    }

    public void setIv(double iv) {
        this.iv = iv;
    }

    @Basic
    @Column(name = "v", nullable = false, precision = 0)
    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    @Basic
    @Column(name = "vi", nullable = false, precision = 0)
    public double getVi() {
        return vi;
    }

    public void setVi(double vi) {
        this.vi = vi;
    }

    @Basic
    @Column(name = "vii", nullable = false, precision = 0)
    public double getVii() {
        return vii;
    }

    public void setVii(double vii) {
        this.vii = vii;
    }

    @Basic
    @Column(name = "viii", nullable = false, precision = 0)
    public double getViii() {
        return viii;
    }

    public void setViii(double viii) {
        this.viii = viii;
    }

    @Basic
    @Column(name = "ix", nullable = false, precision = 0)
    public double getIx() {
        return ix;
    }

    public void setIx(double ix) {
        this.ix = ix;
    }

    @Basic
    @Column(name = "x", nullable = false, precision = 0)
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Basic
    @Column(name = "xi", nullable = false, precision = 0)
    public double getXi() {
        return xi;
    }

    public void setXi(double xi) {
        this.xi = xi;
    }

    @Basic
    @Column(name = "xii", nullable = false, precision = 0)
    public double getXii() {
        return xii;
    }

    public void setXii(double xii) {
        this.xii = xii;
    }

    @Basic
    @Column(name = "xiii", nullable = false, precision = 0)
    public double getXiii() {
        return xiii;
    }

    public void setXiii(double xiii) {
        this.xiii = xiii;
    }

    @Basic
    @Column(name = "xiv", nullable = false, precision = 0)
    public double getXiv() {
        return xiv;
    }

    public void setXiv(double xiv) {
        this.xiv = xiv;
    }

    @Basic
    @Column(name = "xv", nullable = false, precision = 0)
    public double getXv() {
        return xv;
    }

    public void setXv(double xv) {
        this.xv = xv;
    }

    @Basic
    @Column(name = "xvi", nullable = false, precision = 0)
    public double getXvi() {
        return xvi;
    }

    public void setXvi(double xvi) {
        this.xvi = xvi;
    }

    @Basic
    @Column(name = "autesp", nullable = false, precision = 0)
    public double getAutesp() {
        return autesp;
    }

    public void setAutesp(double autesp) {
        this.autesp = autesp;
    }

    @Basic
    @Column(name = "antiguedad", nullable = false, precision = 0)
    public double getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(double antiguedad) {
        this.antiguedad = antiguedad;
    }

    @Basic
    @Column(name = "vacaciones", nullable = false, precision = 0)
    public double getVacaciones() {
        return vacaciones;
    }

    public void setVacaciones(double vacaciones) {
        this.vacaciones = vacaciones;
    }

    @Basic
    @Column(name = "nomina", nullable = false, precision = 0)
    public double getNomina() {
        return nomina;
    }

    public void setNomina(double nomina) {
        this.nomina = nomina;
    }

    @Basic
    @Column(name = "seguro", nullable = false, precision = 0)
    public double getSeguro() {
        return seguro;
    }

    public void setSeguro(double seguro) {
        this.seguro = seguro;
    }

    @Basic
    @Column(name = "gtii", precision = 0)
    public double getGtii() {
        return gtii;
    }

    public void setGtii(double gtii) {
        this.gtii = gtii;
    }

    @Basic
    @Column(name = "gtiii", precision = 0)
    public double getGtiii() {
        return gtiii;
    }

    public void setGtiii(double gtiii) {
        this.gtiii = gtiii;
    }

    @Basic
    @Column(name = "gtiv", precision = 0)
    public double getGtiv() {
        return gtiv;
    }

    public void setGtiv(double gtiv) {
        this.gtiv = gtiv;
    }

    @Basic
    @Column(name = "gtv", precision = 0)
    public double getGtv() {
        return gtv;
    }

    public void setGtv(double gtv) {
        this.gtv = gtv;
    }

    @Basic
    @Column(name = "gtvi", precision = 0)
    public double getGtvi() {
        return gtvi;
    }

    public void setGtvi(double gtvi) {
        this.gtvi = gtvi;
    }

    @Basic
    @Column(name = "gtvii", precision = 0)
    public double getGtvii() {
        return gtvii;
    }

    public void setGtvii(double gtvii) {
        this.gtvii = gtvii;
    }

    @Basic
    @Column(name = "gtviii", precision = 0)
    public double getGtviii() {
        return gtviii;
    }

    public void setGtviii(double gtviii) {
        this.gtviii = gtviii;
    }

    @Basic
    @Column(name = "gtix", precision = 0)
    public double getGtix() {
        return gtix;
    }

    public void setGtix(double gtix) {
        this.gtix = gtix;
    }

    @Basic
    @Column(name = "gtx", precision = 0)
    public double getGtx() {
        return gtx;
    }

    public void setGtx(double gtx) {
        this.gtx = gtx;
    }

    @Basic
    @Column(name = "gtxi", precision = 0)
    public double getGtxi() {
        return gtxi;
    }

    public void setGtxi(double gtxi) {
        this.gtxi = gtxi;
    }

    @Basic
    @Column(name = "gtxii", precision = 0)
    public double getGtxii() {
        return gtxii;
    }

    public void setGtxii(double gtxii) {
        this.gtxii = gtxii;
    }

    @Basic
    @Column(name = "gtxiii", precision = 0)
    public double getGtxiii() {
        return gtxiii;
    }

    public void setGtxiii(double gtxiii) {
        this.gtxiii = gtxiii;
    }

    @Basic
    @Column(name = "gtxiv", precision = 0)
    public double getGtxiv() {
        return gtxiv;
    }

    public void setGtxiv(double gtxiv) {
        this.gtxiv = gtxiv;
    }

    @Basic
    @Column(name = "gtxv", precision = 0)
    public double getGtxv() {
        return gtxv;
    }

    public void setGtxv(double gtxv) {
        this.gtxv = gtxv;
    }

    @Basic
    @Column(name = "gtxvi", precision = 0)
    public double getGtxvi() {
        return gtxvi;
    }

    public void setGtxvi(double gtxvi) {
        this.gtxvi = gtxvi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salario salario = (Salario) o;
        return id == salario.id &&
                Double.compare(salario.ii, ii) == 0 &&
                Double.compare(salario.iii, iii) == 0 &&
                Double.compare(salario.iv, iv) == 0 &&
                Double.compare(salario.v, v) == 0 &&
                Double.compare(salario.vi, vi) == 0 &&
                Double.compare(salario.vii, vii) == 0 &&
                Double.compare(salario.viii, viii) == 0 &&
                Double.compare(salario.ix, ix) == 0 &&
                Double.compare(salario.x, x) == 0 &&
                Double.compare(salario.xi, xi) == 0 &&
                Double.compare(salario.xii, xii) == 0 &&
                Double.compare(salario.xiii, xiii) == 0 &&
                Double.compare(salario.xiv, xiv) == 0 &&
                Double.compare(salario.xv, xv) == 0 &&
                Double.compare(salario.xvi, xvi) == 0 &&
                Double.compare(salario.autesp, autesp) == 0 &&
                Double.compare(salario.antiguedad, antiguedad) == 0 &&
                Double.compare(salario.vacaciones, vacaciones) == 0 &&
                Double.compare(salario.nomina, nomina) == 0 &&
                Double.compare(salario.seguro, seguro) == 0 &&
                Double.compare(salario.gtii, gtii) == 0 &&
                Double.compare(salario.gtiii, gtiii) == 0 &&
                Double.compare(salario.gtiv, gtiv) == 0 &&
                Double.compare(salario.gtv, gtv) == 0 &&
                Double.compare(salario.gtvi, gtvi) == 0 &&
                Double.compare(salario.gtvii, gtvii) == 0 &&
                Double.compare(salario.gtviii, gtviii) == 0 &&
                Double.compare(salario.gtix, gtix) == 0 &&
                Double.compare(salario.gtx, gtx) == 0 &&
                Double.compare(salario.gtxi, gtxi) == 0 &&
                Double.compare(salario.gtxii, gtxii) == 0 &&
                Double.compare(salario.gtxiii, gtxiii) == 0 &&
                Double.compare(salario.gtxiv, gtxiv) == 0 &&
                Double.compare(salario.gtxv, gtxv) == 0 &&
                Double.compare(salario.gtxvi, gtxvi) == 0 &&
                Objects.equals(descripcion, salario.descripcion) &&
                Objects.equals(obrasById, salario.obrasById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descripcion, ii, iii, iv, v, vi, vii, viii, ix, x, xi, xii, xiii, xiv, xv, xvi, autesp, antiguedad, vacaciones, nomina, seguro, gtii, gtiii, gtiv, gtv, gtvi, gtvii, gtviii, gtix, gtx, gtxi, gtxii, gtxiii, gtxiv, gtxv, gtxvi, obrasById);
    }

    @OneToMany(mappedBy = "salarioBySalarioId")
    public Collection<Obra> getObrasById() {
        return obrasById;
    }

    public void setObrasById(Collection<Obra> obrasById) {
        this.obrasById = obrasById;
    }

    @Basic
    @Column(name = "tag", nullable = true, length = 5)
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
