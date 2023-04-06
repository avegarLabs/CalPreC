package models;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class Unidadobra {
    private int id;
    private String codigo;
    private String descripcion;
    private Double cantidad;
    private Double costototal;
    private Double costounitario;
    private Double salario;
    private Double salariocuc;
    private int especialidadesId;
    private int nivelId;
    private int objetosId;
    private int obraId;
    private int empresaconstructoraId;
    private Subespecialidades subespecialidadesBySubespecialidadesId;
    private Especialidades especialidadesByEspecialidadesId;
    private Nivel nivelByNivelId;
    private Objetos objetosByObjetosId;
    private Zonas zonasByZonasId;
    private Obra obraByObraId;
    private Empresaconstructora empresaconstructoraByEmpresaconstructoraId;
    private Grupoejecucion grupoejecucionByGrupoejecucionId;
    private int zonasId;
    private Integer grupoejecucionId;
    private int subespecialidadesId;
    private String um;
    private String renglonbase;
    private Double costoMaterial;
    private Double costomano;
    private Double costoequipo;
    private Collection<Memoriadescriptiva> memoriadescriptivasId;
    private Collection<Unidadobrarenglon> unidadobrarenglonsById;
    private Collection<Bajoespecificacion> unidadobrabajoespecificacionById;
    private Collection<Certificacion> certificacionsById;
    private Collection<Planificacion> planificacionsById;
    private int idResolucion;

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
    @Column(name = "cantidad")
    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    @Basic
    @Column(name = "costototal")
    public Double getCostototal() {
        return costototal;
    }

    public void setCostototal(Double costototal) {
        this.costototal = costototal;
    }

    @Basic
    @Column(name = "costounitario")
    public Double getCostounitario() {
        return costounitario;
    }

    public void setCostounitario(Double costounitario) {
        this.costounitario = costounitario;
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
    @Column(name = "salariocuc")
    public Double getSalariocuc() {
        return salariocuc;
    }

    public void setSalariocuc(Double salariocuc) {
        this.salariocuc = salariocuc;
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
    @Column(name = "nivel__id")
    public int getNivelId() {
        return nivelId;
    }

    public void setNivelId(int nivelId) {
        this.nivelId = nivelId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unidadobra that = (Unidadobra) o;
        return Objects.equals(codigo, that.codigo) &&
                especialidadesId == that.especialidadesId &&
                subespecialidadesId == that.subespecialidadesId &&
                nivelId == that.nivelId &&
                objetosId == that.objetosId &&
                obraId == that.obraId &&
                zonasId == that.zonasId &&
                empresaconstructoraId == that.empresaconstructoraId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, codigo, descripcion, cantidad, costototal, costounitario, salario, salariocuc, especialidadesId, nivelId, objetosId, obraId, empresaconstructoraId);
    }

    @ManyToOne
    @JoinColumn(name = "subespecialidades__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Subespecialidades getSubespecialidadesBySubespecialidadesId() {
        return subespecialidadesBySubespecialidadesId;
    }

    public void setSubespecialidadesBySubespecialidadesId(Subespecialidades subespecialidadesBySubespecialidadesId) {
        this.subespecialidadesBySubespecialidadesId = subespecialidadesBySubespecialidadesId;
    }

    @ManyToOne
    @JoinColumn(name = "especialidades__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Especialidades getEspecialidadesByEspecialidadesId() {
        return especialidadesByEspecialidadesId;
    }

    public void setEspecialidadesByEspecialidadesId(Especialidades especialidadesByEspecialidadesId) {
        this.especialidadesByEspecialidadesId = especialidadesByEspecialidadesId;
    }

    @ManyToOne
    @JoinColumn(name = "nivel__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Nivel getNivelByNivelId() {
        return nivelByNivelId;
    }

    public void setNivelByNivelId(Nivel nivelByNivelId) {
        this.nivelByNivelId = nivelByNivelId;
    }

    @ManyToOne
    @JoinColumn(name = "objetos__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Objetos getObjetosByObjetosId() {
        return objetosByObjetosId;
    }

    public void setObjetosByObjetosId(Objetos objetosByObjetosId) {
        this.objetosByObjetosId = objetosByObjetosId;
    }

    @ManyToOne
    @JoinColumn(name = "zonas__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Zonas getZonasByZonasId() {
        return zonasByZonasId;
    }

    public void setZonasByZonasId(Zonas zonasByZonasId) {
        this.zonasByZonasId = zonasByZonasId;
    }

    @ManyToOne
    @JoinColumn(name = "obra__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Obra getObraByObraId() {
        return obraByObraId;
    }

    public void setObraByObraId(Obra obraByObraId) {
        this.obraByObraId = obraByObraId;
    }

    @ManyToOne
    @JoinColumn(name = "empresaconstructora__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Empresaconstructora getEmpresaconstructoraByEmpresaconstructoraId() {
        return empresaconstructoraByEmpresaconstructoraId;
    }

    public void setEmpresaconstructoraByEmpresaconstructoraId(Empresaconstructora empresaconstructoraByEmpresaconstructoraId) {
        this.empresaconstructoraByEmpresaconstructoraId = empresaconstructoraByEmpresaconstructoraId;
    }

    @ManyToOne
    @JoinColumn(name = "grupoejecucion__id", referencedColumnName = "id", insertable = false, updatable = false)
    public Grupoejecucion getGrupoejecucionByGrupoejecucionId() {
        return grupoejecucionByGrupoejecucionId;
    }

    public void setGrupoejecucionByGrupoejecucionId(Grupoejecucion grupoejecucionByGrupoejecucionId) {
        this.grupoejecucionByGrupoejecucionId = grupoejecucionByGrupoejecucionId;
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
    @Column(name = "grupoejecucion__id")
    public Integer getGrupoejecucionId() {
        return grupoejecucionId;
    }

    public void setGrupoejecucionId(Integer grupoejecucionId) {
        this.grupoejecucionId = grupoejecucionId;
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
    @Column(name = "um", nullable = true, length = 10)
    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    @Basic
    @Column(name = "renglonbase", nullable = true)
    public String getRenglonbase() {
        return renglonbase;
    }

    public void setRenglonbase(String renglonbase) {
        this.renglonbase = renglonbase;
    }

    @Basic
    @Column(name = "costomaterial")
    public Double getCostoMaterial() {
        return costoMaterial;
    }

    public void setCostoMaterial(Double costoMaterial) {
        this.costoMaterial = costoMaterial;
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

    @OneToMany(mappedBy = "unidadobraByUnidadobraId", cascade = CascadeType.REMOVE)
    public Collection<Memoriadescriptiva> getMemoriadescriptivasId() {
        return memoriadescriptivasId;
    }

    public void setMemoriadescriptivasId(List<Memoriadescriptiva> memoriadescriptivasById) {
        this.memoriadescriptivasId = memoriadescriptivasById;
    }

    @OneToMany(mappedBy = "unidadobraByUnidadobraId")
    public Collection<Unidadobrarenglon> getUnidadobrarenglonsById() {
        return unidadobrarenglonsById;
    }

    public void setUnidadobrarenglonsById(Collection<Unidadobrarenglon> unidadobrarenglonsById) {
        this.unidadobrarenglonsById = unidadobrarenglonsById;
    }


    @OneToMany(mappedBy = "unidadobraByUnidadobraId")
    public Collection<Bajoespecificacion> getUnidadobrabajoespecificacionById() {
        return unidadobrabajoespecificacionById;
    }

    public void setUnidadobrabajoespecificacionById(Collection<Bajoespecificacion> unidadobrabajoespecificacionById) {
        this.unidadobrabajoespecificacionById = unidadobrabajoespecificacionById;
    }


    @OneToMany(mappedBy = "unidadobraByUnidadobraId")
    public Collection<Certificacion> getCertificacionsById() {
        return certificacionsById;
    }

    public void setCertificacionsById(Collection<Certificacion> certificacionsById) {
        this.certificacionsById = certificacionsById;
    }

    @OneToMany(mappedBy = "unidadobraByUnidadobraId")
    public Collection<Planificacion> getPlanificacionsById() {
        return planificacionsById;
    }

    public void setPlanificacionsById(Collection<Planificacion> planificacionsById) {
        this.planificacionsById = planificacionsById;
    }

    @Basic
    @Column(name = "id_resolucion")
    public int getIdResolucion() {
        return idResolucion;
    }

    public void setIdResolucion(int idResolucion) {
        this.idResolucion = idResolucion;
    }
}
