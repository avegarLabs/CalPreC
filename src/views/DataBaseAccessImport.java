package views;

import AccessMigration.DatabaseConnection;
import AccessMigration.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataBaseAccessImport {

    private static DataBaseAccessImport instancia = null;
    public String pathdb;
    public String pathdbRV;
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    private Statement statement;
    private Connection connection;
    private ResultSet resultSet;

    private ObservableList<String> empresas;
    private ObservableList<String> zonas;
    private ObservableList<String> objetos;
    private ObservableList<String> nivel;
    private ObservableList<String> especialidades;
    private ObservableList<String> subespecialidades;
    private ArrayList<UnidadObraPCW> unidadObraPCWArrayList;
    private ArrayList<RenglonVariantePCW> renglonVariantePCWArrayList;
    private ArrayList<SumBajoEspPCW> sumBajoEspPCWArrayList;
    private ArrayList<SumBajoEspRVPCW> sumBajoEspRVPCWArrayList;
    private List<PlanPCW> planPCWList;
    private List<CertificacionPCW> certificacionPCWList;
    private ArrayList<SuminstrosPCW> suminstrosPCWArrayList;
    private SuminstrosPCW suminstrosPCW;

    private DataBaseAccessImport() {

    }

    public static DataBaseAccessImport getInstance() {

        if (instancia == null) {
            instancia = new DataBaseAccessImport();
        }

        return instancia;
    }

    public ObservableList<String> getEmpresasList() {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ConstEmp ");
            empresas = FXCollections.observableArrayList();
            while (resultSet.next()) {
                empresas.add(resultSet.getNString("Empresa") + " - " + resultSet.getNString("Descripción"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return empresas;
    }

    public ObservableList<String> getZonasList() {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Zonas ");
            zonas = FXCollections.observableArrayList();
            while (resultSet.next()) {
                zonas.add(resultSet.getNString("Zona") + " - " + resultSet.getNString("Descripción"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return zonas;
    }

    public ObservableList<String> getObjetosList(String code) {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Objetos WHERE Zona ='" + code + "'");
            objetos = FXCollections.observableArrayList();
            while (resultSet.next()) {
                objetos.add(resultSet.getNString("Objeto") + " - " + resultSet.getNString("Descripción"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return objetos;
    }

    public ObservableList<String> getNivelList(String codeZona, String codeObjeto) {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Nivel WHERE Zona ='" + codeZona + "' AND Objeto ='" + codeObjeto + "'");
            nivel = FXCollections.observableArrayList();
            while (resultSet.next()) {
                nivel.add(resultSet.getNString("Nivel") + " - " + resultSet.getNString("Descripción"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return nivel;
    }

    public ObservableList<String> getEspecialidadesList(String codeZona, String codeObjeto, String codeNivel) {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Especialidades WHERE Zona ='" + codeZona + "' AND Objeto ='" + codeObjeto + "' AND Nivel ='" + codeNivel + "'");
            especialidades = FXCollections.observableArrayList();
            while (resultSet.next()) {
                especialidades.add(resultSet.getNString("Especialidad") + " - " + resultSet.getNString("Descripción"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return especialidades;
    }

    public ObservableList<String> getSubEspecialidadesList(String codeZona, String codeObjeto, String codeNivel, String codeEspe) {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SubEspecialidad WHERE Zona ='" + codeZona + "' AND Objeto ='" + codeObjeto + "' AND Nivel ='" + codeNivel + "'  AND Especialidad ='" + codeEspe + "'");
            subespecialidades = FXCollections.observableArrayList();
            while (resultSet.next()) {
                subespecialidades.add(resultSet.getNString("Sub") + " - " + resultSet.getNString("Descripción"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return subespecialidades;
    }

    public ArrayList<UnidadObraPCW> getUnidadObraPCWArrayList() {

        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Unidad");
            unidadObraPCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                unidadObraPCWArrayList.add(new UnidadObraPCW(resultSet.getNString("Empresa"), resultSet.getNString("Zona"), resultSet.getNString("Objeto"), resultSet.getNString("Nivel"), resultSet.getNString("Especialidad"), resultSet.getNString("Sub"), resultSet.getNString("UO"), resultSet.getNString("Descripción"), resultSet.getNString("UM"), Double.parseDouble(resultSet.getNString("Cantidad")), Double.parseDouble(resultSet.getNString("CostoMat")), Double.parseDouble(resultSet.getNString("CostoMan")), Double.parseDouble(resultSet.getNString("CostoEq")), Double.parseDouble(resultSet.getNString("Salario")), 0.0, resultSet.getNString("RenglónBase")));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return unidadObraPCWArrayList;
    }

    public ArrayList<UnidadObraPCW> getNivelesEspecificosPCWArrayList() {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT Renglones.Empresa, Renglones.Zona, Renglones.Objeto, Renglones.Nivel, Renglones.Especialidad, Renglones.Sub, Renglones.Especificación, Especificación.Descripción, Sum(Renglones.Cantidad) AS SumOfCantidad, Sum(Renglones.CostoMat) AS SumOfCostoMat, Sum(Renglones.CostoMan) AS SumOfCostoMan, Sum(Renglones.CostoEq) AS SumOfCostoEq, Sum(Renglones.Salario) AS SumOfSalario " +
                    "FROM Renglones INNER JOIN Especificación ON (Renglones.Zona = Especificación.Zona) AND (Renglones.Objeto = Especificación.Objeto) AND (Renglones.Nivel = Especificación.Nivel) AND (Renglones.Especialidad = Especificación.Especialidad) AND (Renglones.Especificación = Especificación.Especificación) AND (Renglones.Sub = Especificación.Sub) " +
                    "GROUP BY Renglones.Empresa, Renglones.Zona, Renglones.Objeto, Renglones.Nivel, Renglones.Especialidad, Renglones.Sub, Renglones.Especificación, Especificación.Descripción");
            unidadObraPCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                unidadObraPCWArrayList.add(new UnidadObraPCW(resultSet.getNString("Renglones.Empresa"), resultSet.getNString("Renglones.Zona"), resultSet.getNString("Renglones.Objeto"), resultSet.getNString("Renglones.Nivel"), resultSet.getNString("Renglones.Especialidad"), resultSet.getNString("Renglones.Sub"), resultSet.getNString("Renglones.Especificación"), resultSet.getNString("Especificación.Descripción"), " ", Double.parseDouble(resultSet.getNString("SumOfCantidad")), Double.parseDouble(resultSet.getNString("SumOfCostoMat")), Double.parseDouble(resultSet.getNString("SumOfCostoMan")), Double.parseDouble(resultSet.getNString("SumOfCostoEq")), Double.parseDouble(resultSet.getNString("SumOfSalario")), 0.0, " "));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return unidadObraPCWArrayList;
    }

    public ArrayList<RenglonVariantePCW> getRenglonVariantePCWArrayList() {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM EnlaseRv");
            renglonVariantePCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                renglonVariantePCWArrayList.add(new RenglonVariantePCW(resultSet.getNString("Empresa"), resultSet.getNString("Zona"), resultSet.getNString("Objeto"), resultSet.getNString("Nivel"), resultSet.getNString("Especialidad"), resultSet.getNString("Sub"), resultSet.getNString("UO"), resultSet.getNString("RV"), Double.parseDouble(resultSet.getNString("Cantidad")), Double.parseDouble(resultSet.getNString("CostoMat")), Double.parseDouble(resultSet.getNString("CostoMan")), Double.parseDouble(resultSet.getNString("CostoEq")), Double.parseDouble(resultSet.getNString("Salario")), 0.0));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return renglonVariantePCWArrayList;
    }

    public ArrayList<RenglonVariantePCW> getRenglonVarianteRVPCWArrayList() {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Renglones");
            renglonVariantePCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                renglonVariantePCWArrayList.add(new RenglonVariantePCW(resultSet.getNString("Empresa"), resultSet.getNString("Zona"), resultSet.getNString("Objeto"), resultSet.getNString("Nivel"), resultSet.getNString("Especialidad"), resultSet.getNString("Sub"), resultSet.getNString("Especificación"), resultSet.getNString("RV"), Double.parseDouble(resultSet.getNString("Cantidad")), Double.parseDouble(resultSet.getNString("CostoMat")), Double.parseDouble(resultSet.getNString("CostoMan")), Double.parseDouble(resultSet.getNString("CostoEq")), Double.parseDouble(resultSet.getNString("Salario")), 0.0));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return renglonVariantePCWArrayList;
    }

    public List<SumBajoEspPCW> getSumBajoEspPCWArrayList() {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM EnlaseSum");
            sumBajoEspPCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                sumBajoEspPCWArrayList.add(new SumBajoEspPCW(resultSet.getNString("Empresa"), resultSet.getNString("Zona"), resultSet.getNString("Objeto"), resultSet.getNString("Nivel"), resultSet.getNString("Especialidad"), resultSet.getNString("Sub"), resultSet.getNString("UO"), resultSet.getNString("Suministro"), Double.parseDouble(resultSet.getNString("Cantidad")), Double.parseDouble(resultSet.getNString("CostoMat"))));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sumBajoEspPCWArrayList.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
    }

    public List<PlanPCW> getPlanPCWList() {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM tblUOPlan");
            planPCWList = new ArrayList<>();
            while (resultSet.next()) {
                planPCWList.add(new PlanPCW(resultSet.getNString("Empresa"), resultSet.getNString("Zona"), resultSet.getNString("Objeto"), resultSet.getNString("Nivel"), resultSet.getNString("Especialidad"), resultSet.getNString("Sub"), resultSet.getNString("UO"), resultSet.getNString("Brigada"), resultSet.getNString("Grupo"), resultSet.getNString("Cuadrilla"), resultSet.getNString("Desde").substring(0, 10), resultSet.getNString("Hasta").substring(0, 10), Double.parseDouble(resultSet.getNString("Cantidad")), Double.parseDouble(resultSet.getNString("CostoMat")), Double.parseDouble(resultSet.getNString("CostoMan")), Double.parseDouble(resultSet.getNString("CostoEq")), Double.parseDouble(resultSet.getNString("Salario")), Double.parseDouble(resultSet.getNString("CUCSalario"))));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return planPCWList;
    }

    public List<CertificacionPCW> getCertificacionPCWList() {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM tblUOCert");
            certificacionPCWList = new ArrayList<>();
            while (resultSet.next()) {
                certificacionPCWList.add(new CertificacionPCW(resultSet.getNString("Empresa"), resultSet.getNString("Zona"), resultSet.getNString("Objeto"), resultSet.getNString("Nivel"), resultSet.getNString("Especialidad"), resultSet.getNString("Sub"), resultSet.getNString("UO"), resultSet.getNString("Brigada"), resultSet.getNString("Grupo"), resultSet.getNString("Cuadrilla"), resultSet.getNString("Desde").substring(0, 10), resultSet.getNString("Hasta").substring(0, 10), Double.parseDouble(resultSet.getNString("Cantidad")), Double.parseDouble(resultSet.getNString("CostoMat")), Double.parseDouble(resultSet.getNString("CostoMan")), Double.parseDouble(resultSet.getNString("CostoEq")), Double.parseDouble(resultSet.getNString("Salario")), Double.parseDouble(resultSet.getNString("CUCSalario"))));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return certificacionPCWList;
    }

    public ArrayList<SumBajoEspRVPCW> getSumBajoEspPCWRVArrayList() {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Suministros");
            sumBajoEspRVPCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                sumBajoEspRVPCWArrayList.add(new SumBajoEspRVPCW(resultSet.getNString("Empresa"), resultSet.getNString("Zona"), resultSet.getNString("Objeto"), resultSet.getNString("Nivel"), resultSet.getNString("Especialidad"), resultSet.getNString("Sub"), resultSet.getNString("Especificación"), resultSet.getNString("RV"), resultSet.getNString("Suministro"), Double.parseDouble(resultSet.getNString("Cantidad")), Double.parseDouble(resultSet.getNString("CostoMat"))));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sumBajoEspRVPCWArrayList;
    }

    public ArrayList<SuminstrosPCW> getSuminstrosPCWArrayList() {
        connection = databaseConnection.accessConnection(pathdb);
        try {

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Sumini");

            suminstrosPCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                String code = resultSet.getNString("Código");
                String desc = resultSet.getNString("Descripción");
                String tipo = resultSet.getNString("Tipo");
                String um = resultSet.getNString("UM");
                String peso = resultSet.getNString("Peso");
                String mn = resultSet.getNString("Precio");
                String mlc = resultSet.getNString("MLC");

                suminstrosPCWArrayList.add(new SuminstrosPCW(code, tipo, desc, um, Double.parseDouble(peso), Double.parseDouble(mn), Double.parseDouble(mlc)));
            }

        } catch (Exception e) {

        }
        return suminstrosPCWArrayList;
    }

    public SuminstrosPCW getRecurso(String recCode) {
        connection = databaseConnection.accessConnection(pathdb);
        try {

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM tblDescrip WHERE Código ='" + recCode + "'");

            suminstrosPCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                String code = resultSet.getNString("Código");
                String desc = resultSet.getNString("Descripción");
                String tipo = resultSet.getNString("Tipo");
                String um = resultSet.getNString("UM");
                String peso = resultSet.getNString("Peso");
                String mn = resultSet.getNString("Precio");
                String mlc = resultSet.getNString("MLC");

                suminstrosPCW = new SuminstrosPCW(code, tipo, desc, um, Double.parseDouble(peso), Double.parseDouble(mn), Double.parseDouble(mlc));
            }

        } catch (Exception e) {

        }
        return suminstrosPCW;

    }


}
