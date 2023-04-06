package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import javax.persistence.Query;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class CrearPlanToMesesController implements Initializable {

    private static SessionFactory sf;
    public ArrayList<Obra> obraArrayList;
    public ObservableList<String> obrasStringObservableList;
    public ArrayList<Empresaconstructora> empresaconstructoraArrayList;
    public ObservableList<String> empresaStringObservableList;
    public ArrayList<Zonas> zonasArrayList;
    public ObservableList<String> zonasStringObservableList;
    public ArrayList<Objetos> objetosArrayList;
    public ObservableList<String> objetoStringObservableList;
    public ArrayList<Nivel> nivelArrayList;
    public ObservableList<String> nivelStringObservableList;
    public ArrayList<Especialidades> especialidadesArrayList;
    public ObservableList<String> especialidadesStringObservableList;
    public ArrayList<Subespecialidades> subespecialidadesArrayList;
    public ObservableList<String> subStringObservableList;
    public ArrayList<Brigadaconstruccion> brigadaconstruccionArrayList;
    public ObservableList<String> brigadaStringObservableList;
    public ArrayList<Grupoconstruccion> grupoconstruccionArrayList;
    public ObservableList<String> grupoStringObservableList;
    public ArrayList<Cuadrillaconstruccion> cuadrillaconstruccionArrayList;
    public ObservableList<String> cuadrillaStringObservableList;
    @FXML
    private JFXDatePicker desdeDateIni;
    @FXML
    private JFXDatePicker desdeDateHasta;
    @FXML
    private JFXButton btn_close;
    @FXML
    private JFXDatePicker paraDateIni;
    @FXML
    private JFXDatePicker paraDateHasta;
    @FXML
    private JFXComboBox<String> obrasCombo;
    @FXML
    private JFXComboBox<String> empresaCombo;
    @FXML
    private JFXComboBox<String> zonasCombo;
    @FXML
    private JFXComboBox<String> objetosCombo;
    @FXML
    private JFXComboBox<String> nivelCombo;
    @FXML
    private JFXComboBox<String> especialidadCombo;
    @FXML
    private JFXComboBox<String> subespecialidadCombo;
    @FXML
    private JFXComboBox<String> brigadaCombo;
    @FXML
    private JFXComboBox<String> grupoCombo;
    @FXML
    private JFXComboBox<String> cuadrillaCombo;
    @FXML
    private JFXCheckBox checkBrigada;
    @FXML
    private JFXCheckBox checkActive;
    private String[] partObras;
    private String[] partZonas;
    private String[] partObj;
    private String[] partNiv;
    private String[] partEsp;
    private String[] partSub;
    private String[] partEmp;
    private String[] partBrig;
    private String[] partGrup;
    private String[] partCuad;

    private String subQObr;
    private String subQZon;
    private String subQObj;
    private String subQNiv;
    private String subQEsp;
    private String subQSub;
    private String subQEmp;
    private String subQBrig;
    private String subQGrup;
    private String subQCuad;

    private String Subquery;


    private Integer niveles;
    private java.sql.Date desdeDate;
    private java.sql.Date hastaData;

    private java.sql.Date paraDesdeDate;
    private java.sql.Date paraHastaData;

    private ArrayList<AjustesPlan> ajustesPlanArrayList;
    private AjustesPlan plan;

    private double disponible;
    private Planificacion planificacion;

    private double costMaterial;
    private double costMano;
    private double costEquipo;
    private double costSalario;
    private double costCUCSalario;

    private double valCM;
    private double valCMan;
    private double valEquip;
    private double valSal;
    private double valCucSal;

    private double valToPlan;
    private Double[] valoresCert;


    public ObservableList<String> listOfObras() {
        obrasStringObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            obraArrayList = (ArrayList<Obra>) session.createQuery(" FROM Obra WHERE tipo != 'RV'").list();
            obraArrayList.forEach(obra -> {
                obrasStringObservableList.add(obra.getId() + " - " + obra.getDescripion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return obrasStringObservableList;
    }


    public ObservableList<String> listOfEmpresa(Integer idObra) {
        empresaStringObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("SELECT emp.id, emp.descripcion FROM Empresaconstructora emp INNER JOIN Empresaobra eo ON emp.id = eo.empresaconstructoraId WHERE eo.obraId =: idOb");
            query.setParameter("idOb", idObra);

            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                empresaStringObservableList.add(row[0].toString() + " - " + row[1].toString());
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresaStringObservableList;
    }

    public ObservableList<String> listOfZonas(Integer idObra) {
        zonasStringObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery(" FROM Zonas WHERE obraId =: idOb");
            query.setParameter("idOb", idObra);

            zonasStringObservableList.add("Todas");
            zonasArrayList = (ArrayList<Zonas>) query.getResultList();
            zonasArrayList.forEach(zonas -> {
                zonasStringObservableList.add(zonas.getId() + " - " + zonas.getDesripcion());
            });


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return zonasStringObservableList;
    }

    public ObservableList<String> listOfObjetos(Integer idZonas) {
        objetoStringObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery(" FROM Objetos WHERE zonasId =: idZon");
            query.setParameter("idZon", idZonas);

            objetoStringObservableList.add("Todos");
            objetosArrayList = (ArrayList<Objetos>) query.getResultList();
            objetosArrayList.forEach(objetos -> {
                objetoStringObservableList.add(objetos.getId() + " - " + objetos.getDescripcion());
            });


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return objetoStringObservableList;
    }


    public ObservableList<String> listOfNiveles(Integer idOb) {
        nivelStringObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery(" FROM Nivel WHERE objetosId =: idObj");
            query.setParameter("idObj", idOb);

            nivelStringObservableList.add("Todos");
            nivelArrayList = (ArrayList<Nivel>) query.getResultList();
            nivelArrayList.forEach(nivel -> {
                nivelStringObservableList.add(nivel.getId() + " - " + nivel.getDescripcion());
            });


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return nivelStringObservableList;
    }


    public ObservableList<String> listOfEspecialidades() {
        especialidadesStringObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            especialidadesStringObservableList.add("Todas");
            especialidadesArrayList = (ArrayList<Especialidades>) session.createQuery(" FROM Especialidades ").list();
            especialidadesArrayList.forEach(especialidades -> {
                especialidadesStringObservableList.add(especialidades.getId() + " - " + especialidades.getDescripcion());
            });


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return especialidadesStringObservableList;
    }

    public ObservableList<String> listOfSubespecialidades(Integer idEsp) {
        subStringObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            Query query = session.createQuery(" FROM Subespecialidades WHERE especialidadesId =: idEs");
            query.setParameter("idEs", idEsp);

            subStringObservableList.add("Todas");
            subespecialidadesArrayList = (ArrayList<Subespecialidades>) query.getResultList();
            subespecialidadesArrayList.forEach(subespecialidades -> {
                subStringObservableList.add(subespecialidades.getId() + " - " + subespecialidades.getDescripcion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return subStringObservableList;
    }

    public ObservableList<String> listOfBrigadas(Integer idEmp) {
        brigadaStringObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            Query query = session.createQuery(" FROM Brigadaconstruccion WHERE empresaconstructoraId =: idEm");
            query.setParameter("idEm", idEmp);


            brigadaconstruccionArrayList = (ArrayList<Brigadaconstruccion>) query.getResultList();
            brigadaconstruccionArrayList.forEach(brigada -> {
                brigadaStringObservableList.add(brigada.getId() + " - " + brigada.getDescripcion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return brigadaStringObservableList;
    }

    public ObservableList<String> listOfGrupo(Integer idBri) {
        grupoStringObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            Query query = session.createQuery(" FROM Grupoconstruccion WHERE brigadaconstruccionId =: idBrig");
            query.setParameter("idBrig", idBri);


            grupoconstruccionArrayList = (ArrayList<Grupoconstruccion>) query.getResultList();
            grupoconstruccionArrayList.forEach(grupo -> {
                grupoStringObservableList.add(grupo.getId() + " - " + grupo.getDescripcion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return grupoStringObservableList;
    }

    public ObservableList<String> listOfCuadrillas(Integer idGru) {
        cuadrillaStringObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            Query query = session.createQuery(" FROM Cuadrillaconstruccion WHERE grupoconstruccionId =: idGrup");
            query.setParameter("idGrup", idGru);

            cuadrillaconstruccionArrayList = (ArrayList<Cuadrillaconstruccion>) query.getResultList();
            cuadrillaconstruccionArrayList.forEach(cuadrilla -> {
                cuadrillaStringObservableList.add(cuadrilla.getId() + " - " + cuadrilla.getDescripcion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cuadrillaStringObservableList;
    }


    public Double[] getValoresCertUO(Integer idUO) {
        valoresCert = new Double[6];

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createSQLQuery(" SELECT * FROM get_values_certificates_unidadobra(:idU)");
            query.setParameter("idU", idUO);


            List<Object[]> recuo = ((NativeQuery) query).list();
            for (Object[] row : recuo) {
                valoresCert[0] = Double.parseDouble(row[0].toString());
                valoresCert[1] = Double.parseDouble(row[1].toString());
                valoresCert[2] = Double.parseDouble(row[2].toString());
                valoresCert[3] = Double.parseDouble(row[3].toString());
                valoresCert[4] = Double.parseDouble(row[4].toString());
                valoresCert[5] = Double.parseDouble(row[5].toString());

            }
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return valoresCert;
    }


    public void handleListofEmpresa(ActionEvent event) {
        partObras = obrasCombo.getValue().split(" - ");
        empresaStringObservableList = FXCollections.observableArrayList();
        empresaStringObservableList = listOfEmpresa(Integer.parseInt(partObras[0]));
        empresaCombo.setItems(empresaStringObservableList);

        zonasStringObservableList = FXCollections.observableArrayList();
        zonasStringObservableList = listOfZonas(Integer.parseInt(partObras[0]));
        zonasCombo.setItems(zonasStringObservableList);
    }

    public void handleListofObjetos(ActionEvent event) {
        if (!zonasCombo.getValue().contentEquals("Todas")) {
            partZonas = zonasCombo.getValue().split(" - ");
            objetoStringObservableList = FXCollections.observableArrayList();
            objetoStringObservableList = listOfObjetos(Integer.parseInt(partZonas[0]));
            objetosCombo.setItems(objetoStringObservableList);
        }
    }

    public void handleListofNiveles(ActionEvent event) {
        if (!objetosCombo.getValue().contentEquals("Todos")) {
            partObj = objetosCombo.getValue().split(" - ");
            nivelStringObservableList = FXCollections.observableArrayList();
            nivelStringObservableList = listOfNiveles(Integer.parseInt(partObj[0]));
            nivelCombo.setItems(nivelStringObservableList);

            especialidadesStringObservableList = FXCollections.observableArrayList();
            especialidadesStringObservableList = listOfEspecialidades();
            especialidadCombo.setItems(especialidadesStringObservableList);
        }
    }

    public void handleListofEspecialidades(ActionEvent event) {

        if (!especialidadCombo.getValue().contentEquals("Todas")) {
            partEsp = especialidadCombo.getValue().split(" - ");
            subStringObservableList = FXCollections.observableArrayList();
            subStringObservableList = listOfSubespecialidades(Integer.parseInt(partEsp[0]));
            subespecialidadCombo.setItems(subStringObservableList);
        }
    }

    public void handleListofBrigada(ActionEvent event) {

        partEmp = empresaCombo.getValue().split(" - ");
        brigadaStringObservableList = FXCollections.observableArrayList();
        brigadaStringObservableList = listOfBrigadas(Integer.parseInt(partEmp[0]));
        brigadaCombo.setItems(brigadaStringObservableList);

    }

    public void handleListofGrupo(ActionEvent event) {

        partBrig = brigadaCombo.getValue().split(" - ");
        grupoStringObservableList = FXCollections.observableArrayList();
        grupoStringObservableList = listOfGrupo(Integer.parseInt(partBrig[0]));
        grupoCombo.setItems(grupoStringObservableList);
    }

    public void handleListofCuadrilla(ActionEvent event) {

        partGrup = grupoCombo.getValue().split(" - ");
        cuadrillaStringObservableList = FXCollections.observableArrayList();
        cuadrillaStringObservableList = listOfCuadrillas(Integer.parseInt(partGrup[0]));
        cuadrillaCombo.setItems(cuadrillaStringObservableList);

    }


    public ArrayList<AjustesPlan> getDatosPlanArrayList(String query) {
        ajustesPlanArrayList = new ArrayList<AjustesPlan>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query1 = session.createQuery(query);

            disponible = 0.0;
            for (Iterator it = ((org.hibernate.query.Query) query1).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();

                Integer UOid = Integer.parseInt(row[0].toString());
                double materialesUO = Double.parseDouble(row[1].toString());
                double manoUO = Double.parseDouble(row[2].toString());
                double equipoUO = Double.parseDouble(row[3].toString());
                double salarioUO = Double.parseDouble(row[4].toString());
                double salarioCUC = Double.parseDouble(row[5].toString());
                double cantUO = Double.parseDouble(row[6].toString());
                double planCant = Double.parseDouble(row[7].toString());
                double plaMateriales = Double.parseDouble(row[8].toString());
                double plaMano = Double.parseDouble(row[9].toString());
                double plaEquip = Double.parseDouble(row[10].toString());
                double plaSalario = Double.parseDouble(row[11].toString());
                double plaCuCSalario = Double.parseDouble(row[12].toString());
                Integer brigada = Integer.parseInt(row[13].toString());
                Integer grupo = Integer.parseInt(row[14].toString());

                if (row[15] != null) {

                    double certCantidad = Double.parseDouble(row[16].toString());
                    disponible = planCant - certCantidad;
                    Integer cuadrilla = Integer.parseInt(row[15].toString());

                    plan = new AjustesPlan(UOid, materialesUO, manoUO, equipoUO, salarioUO, salarioCUC, cantUO, planCant, plaMateriales, plaMano, plaEquip, plaSalario, plaCuCSalario, brigada, grupo, cuadrilla, certCantidad, disponible);
                    ajustesPlanArrayList.add(plan);


                } else if (row[15] == null) {
                    double certCantidad = 0.0;
                    disponible = certCantidad;
                    plan = new AjustesPlan(UOid, materialesUO, manoUO, equipoUO, salarioUO, salarioCUC, cantUO, planCant, plaMateriales, plaMano, plaEquip, plaSalario, plaCuCSalario, brigada, grupo, null, certCantidad, disponible);
                    ajustesPlanArrayList.add(plan);
                }


            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return ajustesPlanArrayList;

    }

    public Integer addPlanificacion(Planificacion planificacion) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer certId = null;
        try {

            tx = session.beginTransaction();

            ArrayList<Planificacion> allPlanes = (ArrayList<Planificacion>) session.createQuery(" FROM Planificacion ").list();
            if (allPlanes.contains(planificacion)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Ya existen volumenes planificados para este periodo");
                alert.showAndWait();
            } else {
                certId = (Integer) session.save(planificacion);
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return certId;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        obrasStringObservableList = FXCollections.observableArrayList();
        obrasStringObservableList = listOfObras();
        obrasCombo.setItems(obrasStringObservableList);

        checkBrigada.setOnMouseClicked(event -> {
            if (checkBrigada.isSelected() == true) {
                brigadaCombo.setDisable(false);
                grupoCombo.setDisable(false);
            } else if (checkBrigada.isSelected() == false) {
                brigadaCombo.setDisable(true);
                grupoCombo.setDisable(true);
            }
        });

        checkActive.setOnMouseClicked(event -> {
            if (checkActive.isSelected() == true) {
                cuadrillaCombo.setDisable(false);
            } else if (checkActive.isSelected() == false) {
                cuadrillaCombo.setDisable(true);
            }
        });

    }

    public void creteNewPlanes(ActionEvent event) {

        StringBuilder query = new StringBuilder();
        niveles = 0;

        Subquery = "SELECT uo.id, uo.costoMaterial, uo.costomano, uo.costoequipo, uo.salario, uo.salariocuc, uo.cantidad, pl.cantidad, SUM(cert.costmaterial), SUM(cert.costmano), SUM(cert.costequipo), SUM(cert.salario), SUM(cert.cucsalario), pl.brigadaconstruccionId, pl.grupoconstruccionId, pl.cuadrillaconstruccionId, SUM(cert.cantidad) FROM Unidadobra uo INNER JOIN  Planificacion pl ON uo.id = pl.unidadobraId INNER JOIN  Certificacion cert ON uo.id  = cert.unidadobraId WHERE ";
        query.append(Subquery);

        subQObr = " uo.obraId = " + partObras[0];

        partEmp = empresaCombo.getValue().split(" - ");
        subQEmp = " AND uo.empresaconstructoraId = " + partEmp[0];


        if (zonasCombo.getValue() != null && zonasCombo.getValue().contentEquals("Todas")) {
            niveles = 1;
        } else if (zonasCombo.getValue() != null && !zonasCombo.getValue().contentEquals("Todas")) {

            subQZon = " AND uo.zonasId = " + partZonas[0];
            niveles = 1;
        }

        if (objetosCombo.getValue() != null && objetosCombo.getValue().contentEquals("Todos")) {
            niveles = 2;
        } else if (objetosCombo.getValue() != null && !objetosCombo.getValue().contentEquals("Todos")) {
            subQObj = " AND uo.objetosId = " + partObj[0];
            niveles = 2;

        }

        if (nivelCombo.getValue() != null && nivelCombo.getValue().contentEquals("Todos")) {
            niveles = 3;

        } else if (nivelCombo.getValue() != null && !nivelCombo.getValue().contentEquals("Todos")) {

            partNiv = nivelCombo.getValue().split(" - ");
            subQNiv = " AND uo.nivelId = " + partNiv[0];
            niveles = 3;
        }

        if (especialidadCombo.getValue() != null && especialidadCombo.getValue().contentEquals("Todas")) {
            niveles = 4;
        } else if (especialidadCombo.getValue() != null && !especialidadCombo.getValue().contentEquals("Todas")) {
            subQEsp = " AND uo.especialidadesId = " + partEsp[0];
            niveles = 4;
        }

        if (subespecialidadCombo.getValue() != null && subespecialidadCombo.getValue().contentEquals("Todas")) {
            niveles = 5;
        } else if (subespecialidadCombo.getValue() != null && !subespecialidadCombo.getValue().contentEquals("Todas")) {
            partSub = subespecialidadCombo.getValue().split(" - ");
            subQSub = " AND uo.subespecialidadesId = " + partSub[0];
            niveles = 5;

        }

        if (checkBrigada.isSelected() == true && checkActive.isSelected() == false) {

            if (brigadaCombo.getValue() != null) {
                partBrig = brigadaCombo.getValue().split(" - ");
            } else if (grupoCombo.getValue() != null) {
                partGrup = grupoCombo.getValue().split(" - ");
            }


        } else if (checkBrigada.isSelected() == true && checkActive.isSelected() == true) {


            if (brigadaCombo.getValue() != null) {
                partBrig = brigadaCombo.getValue().split(" - ");
            } else if (grupoCombo.getValue() != null) {
                partGrup = grupoCombo.getValue().split(" - ");
            } else if (cuadrillaCombo.getValue() != null) {
                partCuad = cuadrillaCombo.getValue().split(" - ");
            }
        }

        query.append(subQObr);

        if (subQZon != null) {
            query.append(subQZon);
        }
        if (subQObj != null) {
            query.append(subQObj);
        }
        if (subQNiv != null) {
            query.append(subQNiv);
        }

        if (subQEsp != null) {
            query.append(subQEsp);
        }

        if (subQSub != null) {
            query.append(subQSub);
        }

        if (subQEmp != null) {
            query.append(subQEmp);
        }

        if (subQBrig != null) {
            query.append(subQBrig);
        }

        if (subQGrup != null) {
            query.append(subQGrup);
        }

        if (subQCuad != null) {
            query.append(subQCuad);
        }


        if (desdeDateIni.getValue() == null || desdeDateHasta.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Debe especificar un intervalo de tiempo para el informe");
            alert.showAndWait();
        } else {
            LocalDate dateDes = desdeDateIni.getValue();
            LocalDate dateHast = desdeDateHasta.getValue();
            desdeDate = Date.valueOf(dateDes);
            hastaData = Date.valueOf(dateHast);
            String timeRestrict = " AND pl.desde >= '" + desdeDate.toString() + "' AND pl.hasta <= '" + hastaData.toString() + "'";
            query.append(timeRestrict);

        }

        ajustesPlanArrayList = new ArrayList<AjustesPlan>();
        ajustesPlanArrayList = getDatosPlanArrayList(query.toString() + " GROUP BY uo.id, uo.costoMaterial, uo.costomano, uo.costoequipo, uo.salario, uo.salariocuc, uo.cantidad, pl.cantidad, pl.cantidad, pl.brigadaconstruccionId, pl.grupoconstruccionId, pl.cuadrillaconstruccionId ");

        crearPlanificacionesToPlan(ajustesPlanArrayList);

    }

    public void crearPlanificacionesToPlan(ArrayList<AjustesPlan> ajustesArrayList) {

        ajustesPlanArrayList = new ArrayList<AjustesPlan>();
        ajustesPlanArrayList = ajustesArrayList;

        valoresCert = new Double[6];
        ajustesPlanArrayList.forEach(datos -> {

            valoresCert = getValoresCertUO(datos.idUnidad);


            double cantidad = datos.getCantidad() - valoresCert[0];
            System.out.println("Cant: " + cantidad);

            valToPlan = 0.0;
            if (datos.getPendientes() > 0.0) {
                valToPlan = datos.getPendientes();
            } else if (datos.getPendientes() == 0.0) {
                valToPlan = datos.getCantidadplan();
            } else if (datos.getPendientes() < 0.0) {
                valToPlan = datos.getCantidadplan();
            }

            if (cantidad >= valToPlan) {

                valCM = 0.0;
                valCMan = 0.0;
                valEquip = 0.0;
                valSal = 0.0;
                valCucSal = 0.0;

                valCM = datos.getMaterialesUO() - valoresCert[1];
                valCMan = datos.getManoUO() - valoresCert[2];
                valEquip = datos.getEquiposUO() - valoresCert[3];
                valSal = datos.getSalarioUO() - valoresCert[4];
                valCucSal = datos.getSalariocucUO() - valoresCert[5];

                costMaterial = 0.0;
                costMano = 0.0;
                costEquipo = 0.0;
                costSalario = 0.0;
                costCUCSalario = 0.0;

                costMaterial = valCM * valToPlan / cantidad;
                costMano = valCMan * valToPlan / cantidad;
                costEquipo = valEquip * valToPlan / cantidad;
                costSalario = valSal * valToPlan / cantidad;
                costCUCSalario = valCucSal * valToPlan / cantidad;

                String pattern = "dd/MM/yyyy";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                LocalDate dateDes = paraDateIni.getValue();
                LocalDate dateHast = paraDateHasta.getValue();
                paraDesdeDate = Date.valueOf(dateDes);
                paraHastaData = Date.valueOf(dateHast);

                planificacion = new Planificacion();
                planificacion.setCostomaterial(Math.round(costMaterial * 100d) / 100d);
                planificacion.setCostomano(Math.round(costMano * 100d) / 100d);
                planificacion.setCostoequipo(Math.round(costEquipo * 100d) / 100d);
                planificacion.setCostosalario(Math.round(costSalario * 100d) / 100d);
                planificacion.setCostsalariocuc(Math.round(costCUCSalario * 100d) / 100d);
                if (checkBrigada.isSelected() == true && checkActive.isSelected() == false) {
                    planificacion.setBrigadaconstruccionId(Integer.parseInt(partBrig[0]));
                    planificacion.setGrupoconstruccionId(Integer.parseInt(partGrup[0]));
                    planificacion.setCuadrillaconstruccionId(null);
                } else if (checkBrigada.isSelected() == true && checkActive.isSelected() == true) {
                    planificacion.setBrigadaconstruccionId(Integer.parseInt(partBrig[0]));
                    planificacion.setGrupoconstruccionId(Integer.parseInt(partGrup[0]));
                    planificacion.setCuadrillaconstruccionId(Integer.parseInt(partCuad[0]));
                } else if (checkBrigada.isSelected() == false && checkActive.isSelected() == false) {
                    planificacion.setBrigadaconstruccionId(datos.getBrigadaId());
                    planificacion.setGrupoconstruccionId(datos.getGrupoId());
                    planificacion.setCuadrillaconstruccionId(datos.getCuadrillaId());
                }

                planificacion.setUnidadobraId(datos.getIdUnidad());
                planificacion.setDesde(paraDesdeDate);
                planificacion.setHasta(paraHastaData);
                planificacion.setCantidad(valToPlan);


                Integer idcert = addPlanificacion(planificacion);
            }
        });


    }


}


