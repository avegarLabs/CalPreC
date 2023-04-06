package AccessMigration.model;

import java.util.Objects;

public class EspAndSubRelation {

    public String codEspPCW;
    public String codeEsp;
    public int idEsp;
    public String descEs;
    public String codSubPCW;
    public String codeSub;
    public int idSub;
    public String descSub;

    public EspAndSubRelation(String codEspPCW, String codeEsp, int idEsp, String descEs, String codSubPCW, String codeSub, int idSub, String descSub) {
        this.codEspPCW = codEspPCW;
        this.codeEsp = codeEsp;
        this.idEsp = idEsp;
        this.descEs = descEs;
        this.codSubPCW = codSubPCW;
        this.codeSub = codeSub;
        this.idSub = idSub;
        this.descSub = descSub;
    }


    public String getCodEspPCW() {
        return codEspPCW;
    }

    public void setCodEspPCW(String codEspPCW) {
        this.codEspPCW = codEspPCW;
    }

    public String getCodeEsp() {
        return codeEsp;
    }

    public void setCodeEsp(String codeEsp) {
        this.codeEsp = codeEsp;
    }

    public int getIdEsp() {
        return idEsp;
    }

    public void setIdEsp(int idEsp) {
        this.idEsp = idEsp;
    }

    public String getDescEs() {
        return descEs;
    }

    public void setDescEs(String descEs) {
        this.descEs = descEs;
    }

    public String getCodSubPCW() {
        return codSubPCW;
    }

    public void setCodSubPCW(String codSubPCW) {
        this.codSubPCW = codSubPCW;
    }

    public String getCodeSub() {
        return codeSub;
    }

    public void setCodeSub(String codeSub) {
        this.codeSub = codeSub;
    }

    public int getIdSub() {
        return idSub;
    }

    public void setIdSub(int idSub) {
        this.idSub = idSub;
    }

    public String getDescSub() {
        return descSub;
    }

    public void setDescSub(String descSub) {
        this.descSub = descSub;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EspAndSubRelation that = (EspAndSubRelation) o;
        return idEsp == that.idEsp &&
                idSub == that.idSub &&
                Objects.equals(codEspPCW, that.codEspPCW) &&
                Objects.equals(codSubPCW, that.codSubPCW);

    }


}
