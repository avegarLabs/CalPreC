package models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Despachoscl {
    private int id;
    private Integer obraId;
    private Integer empresaId;
    private Integer zonaId;
    private Integer objetoId;
    private Integer especialidadId;
    private Integer suministroId;
    private String vale;
    private Double cantidad;
    private Date fecha;
    private String usuario;
    private String especialista;
    private String estado;
    private String valido;

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
    @Column(name = "obra_id")
    public Integer getObraId() {
        return obraId;
    }

    public void setObraId(Integer obraId) {
        this.obraId = obraId;
    }

    @Basic
    @Column(name = "empresa_id")
    public Integer getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Integer empresaId) {
        this.empresaId = empresaId;
    }

    @Basic
    @Column(name = "zona_id")
    public Integer getZonaId() {
        return zonaId;
    }

    public void setZonaId(Integer zonaId) {
        this.zonaId = zonaId;
    }

    @Basic
    @Column(name = "objeto_id")
    public Integer getObjetoId() {
        return objetoId;
    }

    public void setObjetoId(Integer objetoId) {
        this.objetoId = objetoId;
    }

    @Basic
    @Column(name = "especialidad_id")
    public Integer getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(Integer especialidadId) {
        this.especialidadId = especialidadId;
    }

    @Basic
    @Column(name = "suministro_id")
    public Integer getSuministroId() {
        return suministroId;
    }

    public void setSuministroId(Integer suministroId) {
        this.suministroId = suministroId;
    }

    @Basic
    @Column(name = "vale")
    public String getVale() {
        return vale;
    }

    public void setVale(String vale) {
        this.vale = vale;
    }

    @Basic
    @Column(name = "cantidad")
    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    @Basic
    @Column(name = "fecha")
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Basic
    @Column(name = "usuario")
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Basic
    @Column(name = "especialista")
    public String getEspecialista() {
        return especialista;
    }

    public void setEspecialista(String especialista) {
        this.especialista = especialista;
    }

    @Basic
    @Column(name = "estado")
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Basic
    @Column(name = "valido")
    public String getValido() {
        return valido;
    }

    public void setValido(String valido) {
        this.valido = valido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Despachoscl that = (Despachoscl) o;
        return id == that.id &&
                Objects.equals(obraId, that.obraId) &&
                Objects.equals(empresaId, that.empresaId) &&
                Objects.equals(zonaId, that.zonaId) &&
                Objects.equals(objetoId, that.objetoId) &&
                Objects.equals(especialidadId, that.especialidadId) &&
                Objects.equals(suministroId, that.suministroId) &&
                Objects.equals(vale, that.vale) &&
                Objects.equals(cantidad, that.cantidad) &&
                Objects.equals(fecha, that.fecha) &&
                Objects.equals(usuario, that.usuario) &&
                Objects.equals(especialista, that.especialista) &&
                Objects.equals(estado, that.estado) &&
                Objects.equals(valido, that.valido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, obraId, empresaId, zonaId, objetoId, especialidadId, suministroId, vale, cantidad, fecha, usuario, especialista, estado, valido);
    }
}
