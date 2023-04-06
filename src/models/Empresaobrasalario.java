package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(EmpresaobrasalarioPK.class)
public class Empresaobrasalario {
    private int empresaconstructoraId;
    private int obraId;
    private int salarioId;

    private double ii;
    private double iii;
    private double iv;
    private double v;
    private double vi;
    private double vii;
    private double viii;
    private double ix;
    private double x;
    private double xii;
    private double xiii;
    private double xiv;
    private double xv;
    private double xvi;
    private double autoesp;
    private double antig;
    private double vacaiones;
    private double nomina;
    private double seguro;
    private double xi;
    private Double gtii;
    private Double gtiii;
    private Double gtiv;
    private Double gtv;
    private Double gtvi;
    private Double gtvii;
    private Double gtviii;
    private Double gtix;
    private Double gtx;
    private Double gtxi;
    private Double gtxii;
    private Double gtxiii;
    private Double gtxiv;
    private Double gtxv;
    private Double gtxvi;

    @Id
    @Column(name = "empresaconstructora_id", nullable = false)
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Id
    @Column(name = "obra_id", nullable = false)
    public int getObraId() {
        return obraId;
    }

    public void setObraId(int obraId) {
        this.obraId = obraId;
    }

    @Id
    @Column(name = "salario_id", nullable = false)
    public int getSalarioId() {
        return salarioId;
    }

    public void setSalarioId(int salarioId) {
        this.salarioId = salarioId;
    }


    @Basic
    @Column(name = "ii", nullable = true, precision = 0)
    public double getIi() {
        return ii;
    }

    public void setIi(double ii) {
        this.ii = ii;
    }

    @Basic
    @Column(name = "iii", nullable = true, precision = 0)
    public double getIii() {
        return iii;
    }

    public void setIii(double iii) {
        this.iii = iii;
    }

    @Basic
    @Column(name = "iv", nullable = true, precision = 0)
    public double getIv() {
        return iv;
    }

    public void setIv(double iv) {
        this.iv = iv;
    }

    @Basic
    @Column(name = "v", nullable = true, precision = 0)
    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    @Basic
    @Column(name = "vi", nullable = true, precision = 0)
    public double getVi() {
        return vi;
    }

    public void setVi(double vi) {
        this.vi = vi;
    }

    @Basic
    @Column(name = "vii", nullable = true, precision = 0)
    public double getVii() {
        return vii;
    }

    public void setVii(double vii) {
        this.vii = vii;
    }

    @Basic
    @Column(name = "viii", nullable = true, precision = 0)
    public double getViii() {
        return viii;
    }

    public void setViii(double viii) {
        this.viii = viii;
    }

    @Basic
    @Column(name = "ix", nullable = true, precision = 0)
    public double getIx() {
        return ix;
    }

    public void setIx(double ix) {
        this.ix = ix;
    }

    @Basic
    @Column(name = "x", nullable = true, precision = 0)
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Basic
    @Column(name = "xii", nullable = true, precision = 0)
    public double getXii() {
        return xii;
    }

    public void setXii(double xii) {
        this.xii = xii;
    }

    @Basic
    @Column(name = "xiii", nullable = true, precision = 0)
    public double getXiii() {
        return xiii;
    }

    public void setXiii(double xiii) {
        this.xiii = xiii;
    }

    @Basic
    @Column(name = "xiv", nullable = true, precision = 0)
    public double getXiv() {
        return xiv;
    }

    public void setXiv(double xiv) {
        this.xiv = xiv;
    }

    @Basic
    @Column(name = "xv", nullable = true, precision = 0)
    public double getXv() {
        return xv;
    }

    public void setXv(double xv) {
        this.xv = xv;
    }

    @Basic
    @Column(name = "xvi", nullable = true, precision = 0)
    public double getXvi() {
        return xvi;
    }

    public void setXvi(double xvi) {
        this.xvi = xvi;
    }

    @Basic
    @Column(name = "autoesp", nullable = true, precision = 0)
    public double getAutoesp() {
        return autoesp;
    }

    public void setAutoesp(double autoesp) {
        this.autoesp = autoesp;
    }

    @Basic
    @Column(name = "antig", nullable = true, precision = 0)
    public double getAntig() {
        return antig;
    }

    public void setAntig(double antig) {
        this.antig = antig;
    }

    @Basic
    @Column(name = "vacaiones", nullable = true, precision = 0)
    public double getVacaiones() {
        return vacaiones;
    }

    public void setVacaiones(double vacaiones) {
        this.vacaiones = vacaiones;
    }

    @Basic
    @Column(name = "nomina", nullable = true, precision = 0)
    public double getNomina() {
        return nomina;
    }

    public void setNomina(double nomina) {
        this.nomina = nomina;
    }

    @Basic
    @Column(name = "seguro", nullable = true, precision = 0)
    public double getSeguro() {
        return seguro;
    }

    public void setSeguro(double seguro) {
        this.seguro = seguro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresaobrasalario that = (Empresaobrasalario) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                obraId == that.obraId &&
                salarioId == that.salarioId &&
                Double.compare(that.ii, ii) == 0 &&
                Double.compare(that.iii, iii) == 0 &&
                Double.compare(that.iv, iv) == 0 &&
                Double.compare(that.v, v) == 0 &&
                Double.compare(that.vi, vi) == 0 &&
                Double.compare(that.vii, vii) == 0 &&
                Double.compare(that.viii, viii) == 0 &&
                Double.compare(that.ix, ix) == 0 &&
                Double.compare(that.x, x) == 0 &&
                Double.compare(that.xii, xii) == 0 &&
                Double.compare(that.xiii, xiii) == 0 &&
                Double.compare(that.xiv, xiv) == 0 &&
                Double.compare(that.xv, xv) == 0 &&
                Double.compare(that.xvi, xvi) == 0 &&
                Double.compare(that.autoesp, autoesp) == 0 &&
                Double.compare(that.antig, antig) == 0 &&
                Double.compare(that.vacaiones, vacaiones) == 0 &&
                Double.compare(that.nomina, nomina) == 0 &&
                Objects.equals(seguro, that.seguro);
    }

    @Override
    public int hashCode() {

        return Objects.hash(empresaconstructoraId, obraId, salarioId, ii, iii, iv, v, vi, vii, viii, ix, x, xii, xiii, xiv, xv, xvi, autoesp, antig, vacaiones, nomina, seguro);
    }

    @Basic
    @Column(name = "xi", nullable = true, precision = 0)
    public double getXi() {
        return xi;
    }

    public void setXi(double xi) {
        this.xi = xi;
    }

    @Basic
    @Column(name = "gtii")
    public Double getGtii() {
        return gtii;
    }

    public void setGtii(Double gtii) {
        this.gtii = gtii;
    }

    @Basic
    @Column(name = "gtiii")
    public Double getGtiii() {
        return gtiii;
    }

    public void setGtiii(Double gtiii) {
        this.gtiii = gtiii;
    }

    @Basic
    @Column(name = "gtiv")
    public Double getGtiv() {
        return gtiv;
    }

    public void setGtiv(Double gtiv) {
        this.gtiv = gtiv;
    }

    @Basic
    @Column(name = "gtv")
    public Double getGtv() {
        return gtv;
    }

    public void setGtv(Double gtv) {
        this.gtv = gtv;
    }

    @Basic
    @Column(name = "gtvi")
    public Double getGtvi() {
        return gtvi;
    }

    public void setGtvi(Double gtvi) {
        this.gtvi = gtvi;
    }

    @Basic
    @Column(name = "gtvii")
    public Double getGtvii() {
        return gtvii;
    }

    public void setGtvii(Double gtvii) {
        this.gtvii = gtvii;
    }

    @Basic
    @Column(name = "gtviii")
    public Double getGtviii() {
        return gtviii;
    }

    public void setGtviii(Double gtviii) {
        this.gtviii = gtviii;
    }

    @Basic
    @Column(name = "gtix")
    public Double getGtix() {
        return gtix;
    }

    public void setGtix(Double gtix) {
        this.gtix = gtix;
    }

    @Basic
    @Column(name = "gtx")
    public Double getGtx() {
        return gtx;
    }

    public void setGtx(Double gtx) {
        this.gtx = gtx;
    }

    @Basic
    @Column(name = "gtxi")
    public Double getGtxi() {
        return gtxi;
    }

    public void setGtxi(Double gtxi) {
        this.gtxi = gtxi;
    }

    @Basic
    @Column(name = "gtxii")
    public Double getGtxii() {
        return gtxii;
    }

    public void setGtxii(Double gtxii) {
        this.gtxii = gtxii;
    }

    @Basic
    @Column(name = "gtxiii")
    public Double getGtxiii() {
        return gtxiii;
    }

    public void setGtxiii(Double gtxiii) {
        this.gtxiii = gtxiii;
    }

    @Basic
    @Column(name = "gtxiv")
    public Double getGtxiv() {
        return gtxiv;
    }

    public void setGtxiv(Double gtxiv) {
        this.gtxiv = gtxiv;
    }

    @Basic
    @Column(name = "gtxv")
    public Double getGtxv() {
        return gtxv;
    }

    public void setGtxv(Double gtxv) {
        this.gtxv = gtxv;
    }

    @Basic
    @Column(name = "gtxvi")
    public Double getGtxvi() {
        return gtxvi;
    }

    public void setGtxvi(Double gtxvi) {
        this.gtxvi = gtxvi;
    }


}
