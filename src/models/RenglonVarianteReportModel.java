package models;

public class RenglonVarianteReportModel implements Comparable<RenglonVarianteReportModel> {
    public Integer idEmpesa;
    public String empresa;
    public Integer idZona;
    public String zona;
    public Integer idObjeto;
    public String objeto;
    public Integer idNivel;
    public String nivel;
    public Integer idEspecialidad;
    public String especialidad;
    public Integer idSub;
    public String subespecialidad;
    public Integer idUnidad;
    public String unidad;
    public String descripUnidad;
    public String uoUM;
    public double cantUO;
    public double uoTotal;
    public int id;
    public String codigo;
    public String descripcion;
    public String um;
    public double cantidad;
    public double material;
    public double mano;
    public double equip;
    public double costMat;
    public double costMano;
    public double costEquip;
    public double total;
    public String codeZona;
    public String codeObjeto;
    public String codeNivel;
    public String codeEspecialidad;
    public String codeSub;

    public RenglonVarianteReportModel(Integer idEmpesa, String empresa, Integer idZona, String zona, Integer idObjeto, String objeto, Integer idNivel, String nivel, Integer idEspecialidad, String especialidad, Integer idSub, String subespecialidad, Integer idUnidad, String unidad, String descripUnidad, String uoUM, double cantUO, double uoTotal, int id, String codigo, String descripcion, String um, double cantidad, double material, double mano, double equip, double costMat, double costMano, double costEquip, double total, String codeZona, String codeObjeto, String codeNivel, String codeEspecialidad, String codeSub) {
        this.idEmpesa = idEmpesa;
        this.empresa = empresa;
        this.idZona = idZona;
        this.zona = zona;
        this.idObjeto = idObjeto;
        this.objeto = objeto;
        this.idNivel = idNivel;
        this.nivel = nivel;
        this.idEspecialidad = idEspecialidad;
        this.especialidad = especialidad;
        this.idSub = idSub;
        this.subespecialidad = subespecialidad;
        this.idUnidad = idUnidad;
        this.unidad = unidad;
        this.descripUnidad = descripUnidad;
        this.uoUM = uoUM;
        this.cantUO = cantUO;
        this.uoTotal = uoTotal;
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.um = um;
        this.cantidad = cantidad;
        this.material = material;
        this.mano = mano;
        this.equip = equip;
        this.costMat = costMat;
        this.costMano = costMano;
        this.costEquip = costEquip;
        this.total = total;
        this.codeZona = codeZona;
        this.codeObjeto = codeObjeto;
        this.codeNivel = codeNivel;
        this.codeEspecialidad = codeEspecialidad;
        this.codeSub = codeSub;
    }

    public Integer getIdEmpesa() {
        return idEmpesa;
    }

    public void setIdEmpesa(Integer idEmpesa) {
        this.idEmpesa = idEmpesa;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public Integer getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Integer idObjeto) {
        this.idObjeto = idObjeto;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public Integer getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Integer idNivel) {
        this.idNivel = idNivel;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(Integer idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Integer getIdSub() {
        return idSub;
    }

    public void setIdSub(Integer idSub) {
        this.idSub = idSub;
    }

    public String getSubespecialidad() {
        return subespecialidad;
    }

    public void setSubespecialidad(String subespecialidad) {
        this.subespecialidad = subespecialidad;
    }

    public Integer getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(Integer idUnidad) {
        this.idUnidad = idUnidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getDescripUnidad() {
        return descripUnidad;
    }

    public void setDescripUnidad(String descripUnidad) {
        this.descripUnidad = descripUnidad;
    }

    public String getUoUM() {
        return uoUM;
    }

    public void setUoUM(String uoUM) {
        this.uoUM = uoUM;
    }

    public double getCantUO() {
        return cantUO;
    }

    public void setCantUO(double cantUO) {
        this.cantUO = cantUO;
    }

    public double getUoTotal() {
        return uoTotal;
    }

    public void setUoTotal(double uoTotal) {
        this.uoTotal = uoTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getMaterial() {
        return material;
    }

    public void setMaterial(double material) {
        this.material = material;
    }

    public double getMano() {
        return mano;
    }

    public void setMano(double mano) {
        this.mano = mano;
    }

    public double getEquip() {
        return equip;
    }

    public void setEquip(double equip) {
        this.equip = equip;
    }

    public double getCostMat() {
        return costMat;
    }

    public void setCostMat(double costMat) {
        this.costMat = costMat;
    }

    public double getCostMano() {
        return costMano;
    }

    public void setCostMano(double costMano) {
        this.costMano = costMano;
    }

    public double getCostEquip() {
        return costEquip;
    }

    public void setCostEquip(double costEquip) {
        this.costEquip = costEquip;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCodeZona() {
        return codeZona;
    }

    public void setCodeZona(String codeZona) {
        this.codeZona = codeZona;
    }

    public String getCodeObjeto() {
        return codeObjeto;
    }

    public void setCodeObjeto(String codeObjeto) {
        this.codeObjeto = codeObjeto;
    }

    public String getCodeNivel() {
        return codeNivel;
    }

    public void setCodeNivel(String codeNivel) {
        this.codeNivel = codeNivel;
    }

    public String getCodeEspecialidad() {
        return codeEspecialidad;
    }

    public void setCodeEspecialidad(String codeEspecialidad) {
        this.codeEspecialidad = codeEspecialidad;
    }

    public String getCodeSub() {
        return codeSub;
    }

    public void setCodeSub(String codeSub) {
        this.codeSub = codeSub;
    }

    public int compareTo(RenglonVarianteReportModel o) {
        if (o.getIdUnidad() > idUnidad) {
            return -1;
        } else if (o.getIdUnidad() > idUnidad) {
            return 0;
        } else {
            return 1;
        }

    }


}