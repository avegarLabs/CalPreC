package models;

public class GroupToReport {

    public Integer idEmpresa;
    public String empresaName;
    public String obraSalarioTag;

    public GroupToReport(Integer idEmpresa, String empresaName, String tag) {
        this.idEmpresa = idEmpresa;
        this.empresaName = empresaName;
        this.obraSalarioTag = tag;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getEmpresaName() {
        return empresaName;
    }

    public void setEmpresaName(String empresaName) {
        this.empresaName = empresaName;
    }

    public String getObraSalarioTag() {
        return obraSalarioTag;
    }

    public void setObraSalarioTag(String obraSalarioTag) {
        this.obraSalarioTag = obraSalarioTag;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GroupToReport that = (GroupToReport) obj;
        return idEmpresa == that.idEmpresa;

    }
}
