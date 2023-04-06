package models;

import java.util.Comparator;
import java.util.List;

public class ConveniosReportModel {

    public static Comparator<ConveniosReportModel> StuConvaniosComparator = new Comparator<ConveniosReportModel>() {
        public int compare(ConveniosReportModel s1, ConveniosReportModel s2) {
            String StudentName1 = s1.getBrigada().toUpperCase() + " " + s1.getGrupo().toUpperCase() + " " + s1.getCuadrilla().toUpperCase();
            String StudentName2 = s1.getBrigada().toUpperCase() + " " + s1.getGrupo().toUpperCase() + " " + s1.getCuadrilla().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };
    public Integer idEmpresa;
    public String nameEmp;
    public Integer idBrigada;
    public String brigada;
    public Integer idGrupo;
    public String grupo;
    public Integer idCuadrilla;
    public String cuadrilla;
    public List<TableConvenios> datosTableConveniosArrayList;
    public List<UOPlanCertConvModel> datosUoPlanCertConvModelArrayList;
    public List<RecursosConvenioModel> datosRecursosConvenioModelArrayList;

    public ConveniosReportModel(Integer idEmpresa, String nameEmp, Integer idBrigada, String brigada, Integer idGrupo, String grupo, Integer idCuadrilla, String cuadrilla, List<TableConvenios> datosTableConveniosArrayList, List<UOPlanCertConvModel> datosUoPlanCertConvModelArrayList, List<RecursosConvenioModel> datosRecursosConvenioModelArrayList) {
        this.idEmpresa = idEmpresa;
        this.nameEmp = nameEmp;
        this.idBrigada = idBrigada;
        this.brigada = brigada;
        this.idGrupo = idGrupo;
        this.grupo = grupo;
        this.idCuadrilla = idCuadrilla;
        this.cuadrilla = cuadrilla;
        this.datosTableConveniosArrayList = datosTableConveniosArrayList;
        this.datosUoPlanCertConvModelArrayList = datosUoPlanCertConvModelArrayList;
        this.datosRecursosConvenioModelArrayList = datosRecursosConvenioModelArrayList;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNameEmp() {
        return nameEmp;
    }

    public void setNameEmp(String nameEmp) {
        this.nameEmp = nameEmp;
    }

    public Integer getIdBrigada() {
        return idBrigada;
    }

    public void setIdBrigada(Integer idBrigada) {
        this.idBrigada = idBrigada;
    }

    public String getBrigada() {
        return brigada;
    }

    public void setBrigada(String brigada) {
        this.brigada = brigada;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public Integer getIdCuadrilla() {
        return idCuadrilla;
    }

    public void setIdCuadrilla(Integer idCuadrilla) {
        this.idCuadrilla = idCuadrilla;
    }

    public String getCuadrilla() {
        return cuadrilla;
    }

    public void setCuadrilla(String cuadrilla) {
        this.cuadrilla = cuadrilla;
    }

    public List<TableConvenios> getDatosTableConveniosArrayList() {
        return datosTableConveniosArrayList;
    }

    public void setDatosTableConveniosArrayList(List<TableConvenios> datosTableConveniosArrayList) {
        this.datosTableConveniosArrayList = datosTableConveniosArrayList;
    }

    public List<UOPlanCertConvModel> getDatosUoPlanCertConvModelArrayList() {
        return datosUoPlanCertConvModelArrayList;
    }

    public void setDatosUoPlanCertConvModelArrayList(List<UOPlanCertConvModel> datosUoPlanCertConvModelArrayList) {
        this.datosUoPlanCertConvModelArrayList = datosUoPlanCertConvModelArrayList;
    }

    public List<RecursosConvenioModel> getDatosRecursosConvenioModelArrayList() {
        return datosRecursosConvenioModelArrayList;
    }

    public void setDatosRecursosConvenioModelArrayList(List<RecursosConvenioModel> datosRecursosConvenioModelArrayList) {
        this.datosRecursosConvenioModelArrayList = datosRecursosConvenioModelArrayList;
    }
}
