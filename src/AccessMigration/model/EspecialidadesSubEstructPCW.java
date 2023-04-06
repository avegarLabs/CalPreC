package AccessMigration.model;

import java.util.Objects;

public class EspecialidadesSubEstructPCW {

    public String codeEsp;
    public String descEsp;
    public String codeSub;
    public String descSub;

    public EspecialidadesSubEstructPCW(String codeEsp, String descEsp, String codeSub, String descSub) {
        this.codeEsp = codeEsp;
        this.descEsp = descEsp;
        this.codeSub = codeSub;
        this.descSub = descSub;
    }

    public String getCodeEsp() {
        return codeEsp;
    }

    public void setCodeEsp(String codeEsp) {
        this.codeEsp = codeEsp;
    }

    public String getDescEsp() {
        return descEsp;
    }

    public void setDescEsp(String descEsp) {
        this.descEsp = descEsp;
    }

    public String getCodeSub() {
        return codeSub;
    }

    public void setCodeSub(String codeSub) {
        this.codeSub = codeSub;
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
        EspecialidadesSubEstructPCW that = (EspecialidadesSubEstructPCW) o;
        return Objects.equals(codeEsp, that.codeEsp) &&
                Objects.equals(codeSub, that.codeSub) &&
                Objects.equals(descSub, that.descSub);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeEsp, codeSub, descSub);
    }
}
