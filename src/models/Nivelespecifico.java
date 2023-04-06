package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Nivelespecifico {
    private int id;
    private String codigo;
    private String descripcion;
    private int obraId;
    private int empresaconstructoraId;
    private int zonasId;
    private int objetosId;
    private int nivelId;
    private int especialidadesId;
    private int subespecialidadesId;
    private Double costomaterial;
    private Double costomano;
    private Double costoequipo;
    private Double salario;
    private Collection<Bajoespecificacionrv> bajoespecificacionrvsById;
    private Obra obraByObraId;
    private Empresaconstructora empresaconstructoraByEmpresaconstructoraId;
    private Zonas zonasByZonasId;
    private Objetos objetosByObjetosId;
    private Nivel nivelByNivelId;
    private Especialidades especialidadesByEspecialidadesId;
    private Subespecialidades subespecialidadesBySubespecialidadesId;
    private Collection<Renglonnivelespecifico> renglonnivelespecificosById;
    private Collection<Memoriadescriptivarv> memoriadescriptivarvsById;
    private Collection<Planrenglonvariante> planrenglonvariantesById;
    private Collection<CertificacionRenglonVariante> certificacionglonvariantesById;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "codigo")
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "obra__id")
    public int getObraId() {
        return obraId;
    }

    public void setObraId(int obraId) {
        this.obraId = obraId;
    }

    @Basic
    @Column(name = "empresaconstructora__id")
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Basic
    @Column(name = "zonas__id")
    public int getZonasId() {
        return zonasId;
    }

    public void setZonasId(int zonasId) {
        this.zonasId = zonasId;
    }

    @Basic
    @Column(name = "objetos__id")
    public int getObjetosId() {
        return objetosId;
    }

    public void setObjetosId(int objetosId) {
        this.objetosId = objetosId;
    }

    @Basic
    @Column(name = "nivel__id")
    public int getNivelId() {
        return nivelId;
    }

    public void setNivelId(int nivelId) {
        this.nivelId = nivelId;
    }

    @Basic
    @Column(name = "especialidades__id")
    public int getEspecialidadesId() {
        return especialidadesId;
    }

    public void setEspecialidadesId(int especialidadesId) {
        this.especialidadesId = especialidadesId;
    }

    @Basic
    @Column(name = "subespecialidades__id")
    public int getSubespecialidadesId() {
        return subespecialidadesId;
    }

    public void setSubespecialidadesId(int subespecialidadesId) {
        this.subespecialidadesId = subespecialidadesId;
    }

    @Basic
    @Column(name = "costomaterial")
    public Double getCostomaterial() {
        return costomaterial;
    }

    public void setCostomaterial(Double costomaterial) {
        this.costomaterial = costomaterial;
    }

    @Basic
    @Column(name = "costomano")
    public Double getCostomano() {
        return costomano;
    }

    public void setCostomano(Double costomano) {
        this.costomano = costomano;
    }

    @Basic
    @Column(name = "costoequipo")
    public Double getCostoequipo() {
        return costoequipo;
    }

    public void setCostoequipo(Double costoequipo) {
        this.costoequipo = costoequipo;
    }

    @Basic
    @Column(name = "salario")
    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nivelespecifico that = (Nivelespecifico) o;
        return id == that.id &&
                obraId == that.obraId &&
                empresaconstructoraId == that.empresaconstructoraId &&
                zonasId == that.zonasId &&
                objetosId == that.objetosId &&
                nivelId == that.nivelId &&
                especialidadesId == that.especialidadesId &&
                subespecialidadesId == that.subespecialidadesId &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(costomaterial, that.costomaterial) &&
                Objects.equals(costomano, that.costomano) &&
                Objects.equals(costoequipo, that.costoequipo) &&
                Objects.equals(salario, that.salario);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, descripcion, obraId, empresaconstructoraId, zonasId, objetosId, nivelId, especialidadesId, subespecialidadesId, costomaterial, costomano, costoequipo, salario);
    }

    @OneToMany(mappedBy = "nivelespecificoByNivelespecificoId", cascade = CascadeType.REMOVE)
    public Collection<Bajoespecificacionrv> getBajoespecificacionrvsById() {
        return bajoespecificacionrvsById;
    }

    public void setBajoespecificacionrvsById(Collection<Bajoespecificacionrv> bajoespecificacionrvsById) {
        this.bajoespecificacionrvsById = bajoespecificacionrvsById;
    }

    @ManyToOne
    @JoinColumn(name = "obra__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Obra getObraByObraId() {
        return obraByObraId;
    }

    public void setObraByObraId(Obra obraByObraId) {
        this.obraByObraId = obraByObraId;
    }

    @ManyToOne
    @JoinColumn(name = "empresaconstructora__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Empresaconstructora getEmpresaconstructoraByEmpresaconstructoraId() {
        return empresaconstructoraByEmpresaconstructoraId;
    }

    public void setEmpresaconstructoraByEmpresaconstructoraId(Empresaconstructora empresaconstructoraByEmpresaconstructoraId) {
        this.empresaconstructoraByEmpresaconstructoraId = empresaconstructoraByEmpresaconstructoraId;
    }

    @ManyToOne
    @JoinColumn(name = "zonas__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Zonas getZonasByZonasId() {
        return zonasByZonasId;
    }

    public void setZonasByZonasId(Zonas zonasByZonasId) {
        this.zonasByZonasId = zonasByZonasId;
    }

    @ManyToOne
    @JoinColumn(name = "objetos__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Objetos getObjetosByObjetosId() {
        return objetosByObjetosId;
    }

    public void setObjetosByObjetosId(Objetos objetosByObjetosId) {
        this.objetosByObjetosId = objetosByObjetosId;
    }

    @ManyToOne
    @JoinColumn(name = "nivel__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Nivel getNivelByNivelId() {
        return nivelByNivelId;
    }

    public void setNivelByNivelId(Nivel nivelByNivelId) {
        this.nivelByNivelId = nivelByNivelId;
    }

    @ManyToOne
    @JoinColumn(name = "especialidades__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Especialidades getEspecialidadesByEspecialidadesId() {
        return especialidadesByEspecialidadesId;
    }

    public void setEspecialidadesByEspecialidadesId(Especialidades especialidadesByEspecialidadesId) {
        this.especialidadesByEspecialidadesId = especialidadesByEspecialidadesId;
    }

    @ManyToOne
    @JoinColumn(name = "subespecialidades__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Subespecialidades getSubespecialidadesBySubespecialidadesId() {
        return subespecialidadesBySubespecialidadesId;
    }

    public void setSubespecialidadesBySubespecialidadesId(Subespecialidades subespecialidadesBySubespecialidadesId) {
        this.subespecialidadesBySubespecialidadesId = subespecialidadesBySubespecialidadesId;
    }

    @OneToMany(mappedBy = "nivelespecificoByNivelespecificoId", cascade = CascadeType.REMOVE)
    public Collection<Renglonnivelespecifico> getRenglonnivelespecificosById() {
        return renglonnivelespecificosById;
    }

    public void setRenglonnivelespecificosById(Collection<Renglonnivelespecifico> renglonnivelespecificosById) {
        this.renglonnivelespecificosById = renglonnivelespecificosById;
    }

    @OneToMany(mappedBy = "nivelespecificoByNivelespecificoId", cascade = CascadeType.REMOVE)
    public Collection<Memoriadescriptivarv> getMemoriadescriptivarvsById() {
        return memoriadescriptivarvsById;
    }

    public void setMemoriadescriptivarvsById(Collection<Memoriadescriptivarv> memoriadescriptivarvsById) {
        this.memoriadescriptivarvsById = memoriadescriptivarvsById;
    }

    @OneToMany(mappedBy = "nivelespecificoByNivelespecificoId", cascade = CascadeType.REMOVE)
    public Collection<Planrenglonvariante> getPlanrenglonvariantesById() {
        return planrenglonvariantesById;
    }

    public void setPlanrenglonvariantesById(Collection<Planrenglonvariante> planrenglonvariantesById) {
        this.planrenglonvariantesById = planrenglonvariantesById;
    }

    @OneToMany(mappedBy = "nivelespecificoByNivelespecificoId", cascade = CascadeType.REMOVE)
    public Collection<CertificacionRenglonVariante> getCertificacionglonvariantesById() {
        return certificacionglonvariantesById;
    }

    public void setCertificacionglonvariantesById(Collection<CertificacionRenglonVariante> planrenglonvariantesById) {
        this.certificacionglonvariantesById = planrenglonvariantesById;
    }

}
