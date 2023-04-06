package AccessMigration;

import AccessMigration.model.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseEstructure {

    private static DatabaseEstructure instancia = null;
    public String pathdb;
    public String pathdbRV;
    public String pathRec;
    public ArrayList<UnidadesObraPCW> unidadesObraPCWArrayList;
    public ArrayList<UnidadRnglonesPCW> unidadRnglonesPCWArrayList;
    public ArrayList<RVCreateModel> rvCreateModelArrayList;
    public ArrayList<rvComposition> rvCompositionArrayList;
    public ArrayList<CompoSemiModel> compoSemiModelArrayList;
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    private Statement statement;
    private Connection connection;
    private ResultSet resultSet;
    private ZonasPCW zonasPCW;
    private ArrayList<ObjetosPCW> objetosPCWArrayList;
    private ArrayList<NivelPCW> nivelPCWArrayList;
    private ObraPCW obraPCW;
    private ArrayList<EstructuraPCW> estructuraPCWArrayList;
    private SubEspecialiddaTraduction traduction;
    private ArrayList<BajoEspecificacionPCW> bajoEspecificacionPCWArrayList;
    private ArrayList<SuminstrosPCW> suminstrosPCWArrayList;
    private SuminstrosPCW suminstrosPCW;
    private ArrayList<EspecialidadesSubEstructPCW> especialidadesSubEstructPCWS;


    private DatabaseEstructure() {

    }

    public static DatabaseEstructure getInstance() {

        if (instancia == null) {
            instancia = new DatabaseEstructure();
        }

        return instancia;
    }

    public ObraPCW getObraPCW() {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Obr ");

            while (resultSet.next()) {
                obraPCW = new ObraPCW(resultSet.getNString("Código"), resultSet.getNString("Descripción"), resultSet.getNString("UORV"), resultSet.getNString("Tipo"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return obraPCW;
    }

    public ZonasPCW getZonas(String code) {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Zonas WHERE Zona ='" + code + "' ");

            while (resultSet.next()) {
                zonasPCW = new ZonasPCW(resultSet.getNString("Zona"), resultSet.getNString("Descripción"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return zonasPCW;
    }

    public ArrayList<ObjetosPCW> getObjetos(String code) {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Objetos WHERE Zona ='" + code + "' ");
            objetosPCWArrayList = new ArrayList<>();

            while (resultSet.next()) {

                objetosPCWArrayList.add(new ObjetosPCW(resultSet.getNString("Objeto"), resultSet.getNString("Descripción"), code));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return objetosPCWArrayList;
    }

    public ArrayList<NivelPCW> getNiveles(String codeZona, String code) {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Nivel WHERE Zona ='" + codeZona + "'" + " AND Objeto = '" + code + "'");

            nivelPCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                nivelPCWArrayList.add(new NivelPCW(resultSet.getNString("Nivel"), resultSet.getNString("Descripción"), code, codeZona));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return nivelPCWArrayList;

    }

    public ArrayList<UnidadesObraPCW> getUnidad(EstructuraPCW estructuraPCW) {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Unidad WHERE Empresa = '" + estructuraPCW.getEmpresa() + "'" + " AND Zona ='" + estructuraPCW.getZona() + "'" + " AND Objeto = '" + estructuraPCW.getObjeto() + "'" + " AND Nivel = '" + estructuraPCW.getNivel() + "'" + " AND Especialidad = '" + estructuraPCW.getEspecialidad() + "'" + " AND SubGenérica = '" + estructuraPCW.getSub() + "'");
            unidadesObraPCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                String unidad = resultSet.getNString("UO");
                String descrip = resultSet.getNString("Descripción");
                String um = resultSet.getNString("UM");
                String cantidad = resultSet.getNString("Cantidad");
                String rb = resultSet.getNString("RenglónBase");
                String cmate = resultSet.getNString("CostoMat");
                String cmano = resultSet.getNString("CostoMan");
                String cequip = resultSet.getNString("CostoEq");
                String salario = resultSet.getNString("Salario");
                String salarioCUC = resultSet.getNString("CUCSalario");
                unidadesObraPCWArrayList.add(new UnidadesObraPCW(unidad, descrip, um, Double.parseDouble(cantidad), rb, Double.parseDouble(cmate), Double.parseDouble(cmano), Double.parseDouble(cequip), Double.parseDouble(salario), Double.parseDouble(salarioCUC)));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return unidadesObraPCWArrayList;

    }

    public ArrayList<EstructuraPCW> getEstructuraToUnidadO() {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT DISTINCT Empresa, Zona, Objeto, Nivel, Especialidad, Sub, SubGenérica FROM Unidad ");
            estructuraPCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                String empresa = resultSet.getNString("Empresa");
                String zona = resultSet.getNString("Zona");
                String objeto = resultSet.getNString("Objeto");
                String nivel = resultSet.getNString("Nivel");
                String especialidad = resultSet.getNString("Especialidad");
                String subEsp = resultSet.getNString("Sub");
                String sub = resultSet.getNString("SubGenérica");
                estructuraPCWArrayList.add(new EstructuraPCW(empresa, zona, objeto, nivel, especialidad, subEsp, sub));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return estructuraPCWArrayList;
    }

    public ArrayList<EstructuraPCW> getEstructuraToRV() {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT DISTINCT Empresa, Zona, Objeto, Nivel, Especialidad, Sub, Especificación FROM Renglones ");
            estructuraPCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                String empresa = resultSet.getNString("Empresa");
                String zona = resultSet.getNString("Zona");
                String objeto = resultSet.getNString("Objeto");
                String nivel = resultSet.getNString("Nivel");
                String especialidad = resultSet.getNString("Especialidad");
                String subEsp = resultSet.getNString("Sub");
                String sub = resultSet.getNString("Especificación");
                estructuraPCWArrayList.add(new EstructuraPCW(empresa, zona, objeto, nivel, especialidad, subEsp, sub));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return estructuraPCWArrayList;
    }

    public SubEspecialiddaTraduction getTraduction(String codeEsp, String desC) {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT Sub, Descripción FROM SubEspecialidad WHERE Especialidad = '" + codeEsp + "'" + "AND Descripción = '" + desC + "'");
            unidadRnglonesPCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                String codeSub = resultSet.getNString("Sub");
                String descSub = resultSet.getNString("Descripción");
                traduction = new SubEspecialiddaTraduction(codeSub, descSub);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return traduction;

    }

    public ArrayList<BajoEspecificacionPCW> getBajoEspecificacionPCWArrayList() {
        connection = databaseConnection.accessConnection(pathdb);
        try {

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM EnlaseSum");

            bajoEspecificacionPCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                String empresa = resultSet.getNString("Empresa");
                String zona = resultSet.getNString("Zona");
                String obje = resultSet.getNString("Objeto");
                String niv = resultSet.getNString("Nivel");
                String esp = resultSet.getNString("Especialidad");
                String sub = resultSet.getNString("Sub");
                String uo = resultSet.getNString("UO");
                String suminist = resultSet.getNString("Suministro");
                String cant = resultSet.getNString("Cantidad");
                String cmaterial = resultSet.getNString("CostoMat");
                bajoEspecificacionPCWArrayList.add(new BajoEspecificacionPCW(empresa, zona, obje, niv, esp, sub, uo, suminist, Double.parseDouble(cant), Double.parseDouble(cmaterial)));
            }

        } catch (Exception e) {

        }
        return bajoEspecificacionPCWArrayList;
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

    public ArrayList<UnidadRnglonesPCW> getUnidadRnglonesArrayList() {
        connection = databaseConnection.accessConnection(pathdb);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM EnlaseRv ");
            unidadRnglonesPCWArrayList = new ArrayList<>();
            while (resultSet.next()) {
                String empresa = resultSet.getNString("Empresa");
                String zona = resultSet.getNString("Zona");
                String obje = resultSet.getNString("Objeto");
                String niv = resultSet.getNString("Nivel");
                String esp = resultSet.getNString("Especialidad");
                String sub = resultSet.getNString("Sub");
                String uo = resultSet.getNString("UO");
                String rv = resultSet.getNString("RV");
                String cant = resultSet.getNString("Cantidad");
                String cmaterial = resultSet.getNString("CostoMat");
                String cmano = resultSet.getNString("CostoMan");
                String cequipo = resultSet.getNString("CostoEq");
                String salario = resultSet.getNString("Salario");
                String salariocuc = resultSet.getNString("CUCSalario");
                unidadRnglonesPCWArrayList.add(new UnidadRnglonesPCW(empresa, zona, obje, niv, esp, sub, uo, rv, Double.parseDouble(cant), Double.parseDouble(cmaterial), Double.parseDouble(cmano), Double.parseDouble(cequipo), Double.parseDouble(salario), Double.parseDouble(salariocuc)));

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return unidadRnglonesPCWArrayList;

    }

    public ArrayList<RVCreateModel> getRvCreateModelArrayList() {
        connection = databaseConnection.accessConnectionRVDB(pathdbRV);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM RV");
            rvCreateModelArrayList = new ArrayList<>();
            while (resultSet.next()) {
                String sub = resultSet.getNString("RV");
                String code = resultSet.getNString("Descripción");
                String desc = resultSet.getNString("UM");
                String um = resultSet.getNString("Salario");
                String mat = resultSet.getNString("CostoMat");
                String man = resultSet.getNString("CostoMan");
                String eq = resultSet.getNString("CostoEq");
                String peso = resultSet.getNString("Peso");
                String hh = resultSet.getNString("HH");
                String ho = resultSet.getNString("HO");
                String ha = resultSet.getNString("HA");
                String he = resultSet.getNString("HE");
                String cement = resultSet.getNString("Cemento");
                String arid = resultSet.getNString("Arido");
                ArrayList<rvComposition> componetes = new ArrayList<>();
                componetes = getRvCompositionArrayList(code);
                rvCreateModelArrayList.add(new RVCreateModel(sub, code, desc, um, mat, man, eq, peso, hh, ho, ha, he, cement, arid, componetes));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rvCreateModelArrayList;

    }

    public ArrayList<rvComposition> getRvCompositionArrayList(String code) {
        connection = databaseConnection.accessConnectionRVDB(pathdbRV);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM DMSpuc WHERE RV = '" + code + "'");
            rvCompositionArrayList = new ArrayList<>();
            while (resultSet.next()) {
                String cod = resultSet.getNString("Insumo");
                String tip = resultSet.getNString("Tipo");
                String nor = resultSet.getNString("Cantidad");
                String us = resultSet.getNString("Usos");
                rvCompositionArrayList.add(new rvComposition(cod, tip, Double.parseDouble(nor), Integer.parseInt(us)));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rvCompositionArrayList;
    }

    public ArrayList<CompoSemiModel> getCompoSemiModelArrayList(String code) {
        connection = databaseConnection.accessConnectionRVDB(pathdbRV);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Composición WHERE Suministro = '" + code + "'");
            compoSemiModelArrayList = new ArrayList<>();
            while (resultSet.next()) {
                String cod = resultSet.getNString("Suministro");
                String ins = resultSet.getNString("Insumo");
                String tipo = resultSet.getNString("Tipo");
                String nor = resultSet.getNString("Cantidad");
                String us = resultSet.getNString("Usos");
                compoSemiModelArrayList.add(new CompoSemiModel(cod, ins, tipo, Double.parseDouble(nor), Double.parseDouble(us)));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return compoSemiModelArrayList;
    }

    public ArrayList<EspecialidadesSubEstructPCW> getEspecialidadesSubEstructPCWS() {
        connection = databaseConnection.accessConnectionRVDB(pathdb);
        try {

            String query = " SELECT DISTINCT  Especialidades.Especialidad, Especialidades.Descripción, SubEspecialidad.Sub, SubEspecialidad.Descripción " +
                    " FROM Unidad LEFT JOIN Especialidades ON Unidad.Especialidad = Especialidades.Especialidad AND Unidad.Nivel = Especialidades.Nivel AND Unidad.Objeto = Especialidades.Objeto AND Unidad.Zona = Especialidades.Zona LEFT JOIN SubEspecialidad ON Unidad.Sub = SubEspecialidad.Sub AND Especialidades.Especialidad = SubEspecialidad.Especialidad";

            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);


            especialidadesSubEstructPCWS = new ArrayList<>();

            while (resultSet.next()) {
                String cod = resultSet.getNString("Especialidades.Especialidad");
                String ins = resultSet.getNString("Especialidades.Descripción");
                String tipo = resultSet.getNString("SubEspecialidad.Sub");
                String nor = resultSet.getNString("SubEspecialidad.Descripción");
                especialidadesSubEstructPCWS.add(new EspecialidadesSubEstructPCW(cod, ins, tipo, nor));
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return especialidadesSubEstructPCWS;


    }

}



