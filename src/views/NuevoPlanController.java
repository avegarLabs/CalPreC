package views;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import models.*;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NuevoPlanController implements Initializable {

    private static SessionFactory sf;
    Task tarea;
    @FXML
    private JFXComboBox<String> combo_Brigada;
    @FXML
    private JFXComboBox<String> combogrupo;
    @FXML
    private JFXComboBox<String> combocuadrilla;
    @FXML
    private JFXTextField fieldcodigouo;
    @FXML
    private JFXTextField fieldcantidad;
    @FXML
    private Label labelDispo;
    @FXML
    private JFXDatePicker dateini;
    @FXML
    private JFXDatePicker datehasta;
    private ArrayList<Unidadobra> unidadobraArrayList;
    private ObservableList<String> listUO;
    private ArrayList<Brigadaconstruccion> brigadaconstruccionsArrayList;
    private ArrayList<Grupoconstruccion> grupoconstruccionArrayList;
    private ArrayList<Cuadrillaconstruccion> cuadrillaconstruccionsArrayList;
    private ObservableList<String> brigadasList;
    private ObservableList<String> grupoList;
    private ObservableList<String> cuadrilaList;
    private Unidadobra unidadobraModel;
    private int unidadObraId;
    private int brigadaId;
    private int grupoId;
    private int cuadrillaId;
    private double costMaterial;
    private double costMaterialCert;
    private double costMano;
    private double costManoCert;
    private double costEquipo;
    private double costEquipoCert;
    private double costSalario;
    private double costSalarioCert;
    private double costCUCSalario;
    private double costCUCSalarioCert;
    private double valCM;
    private double valCMan;
    private double valEquip;
    private double valSal;
    private double valCucSal;
    private java.sql.Date desde;
    private java.sql.Date hasta;
    private double cantidadPlan;
    private ArrayList<Planrecuo> planrecuoArrayList;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXCheckBox checkControl;
    private Integer inputLength = 7;
    private Planificacion planificacion;
    private ArrayList<Planificacion> planificacionsArrayList;
    private Double[] valoresCert;
    private Runtime garbache;
    private Brigadaconstruccion brigadaCont;
    private Grupoconstruccion grupoconst;
    private Double valToPlan;
    private Cuadrillaconstruccion cuadrillaConst;

    private ArrayList<Unidadobra> getUnidadesObra(int idObra, int idEmp, int idZona, int idObj, int idNiv, int idEsp, int idSub) {

        unidadobraArrayList = new ArrayList<Unidadobra>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            unidadobraArrayList = (ArrayList<Unidadobra>) session.createQuery("FROM Unidadobra where obraId = :idObra and empresaconstructoraId =:idEmp AND zonasId = :idZona and objetosId =: idObjeto and nivelId =: idNivel and especialidadesId =: idEspecilidad and subespecialidadesId =:idSub").setParameter("idObra", idObra).setParameter("idEmp", idEmp).setParameter("idZona", idZona).setParameter("idObjeto", idObj).setParameter("idNivel", idNiv).setParameter("idEspecilidad", idEsp).setParameter("idSub", idSub).getResultList();

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return unidadobraArrayList;
    }

    public Double[] getValoresCertUO(Integer idUO) {
        valoresCert = new Double[6];

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Object[]> recuo = session.createSQLQuery(" SELECT * FROM get_values_certificates_unidadobra(:idU)").setParameter("idU", idUO).getResultList();
            for (Object[] row : recuo) {
                if (row[0] == null) {
                    valoresCert[0] = 0.0;

                } else {
                    valoresCert[0] = Double.parseDouble(row[0].toString());
                }

                if (row[1] == null) {
                    valoresCert[1] = 0.0;
                } else {
                    valoresCert[1] = Double.parseDouble(row[1].toString());
                }

                if (row[2] == null) {
                    valoresCert[2] = 0.0;
                } else {
                    valoresCert[2] = Double.parseDouble(row[2].toString());
                }

                if (row[3] == null) {
                    valoresCert[3] = 0.0;
                } else {
                    valoresCert[3] = Double.parseDouble(row[3].toString());
                }

                if (row[4] == null) {
                    valoresCert[4] = 0.0;
                } else {
                    valoresCert[4] = Double.parseDouble(row[4].toString());
                }

                if (row[5] == null) {
                    valoresCert[5] = 0.0;
                } else {
                    valoresCert[5] = Double.parseDouble(row[5].toString());
                }

            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return valoresCert;
    }

    /**
     * public ArrayList<Planificacion> getPlanificaciones() {
     * planificacionsArrayList = new ArrayList<Planificacion>();
     * <p>
     * try {
     * * } catch (Throwable ex) {
     * System.err.println("Failed to create sessionFactory object." + ex);
     * throw new ExceptionInInitializerError(ex);
     * }
     * <p>
     * Transaction tx = null;
     * try {
     * tx = session.beginTransaction();
     * <p>
     * planificacionsArrayList = (ArrayList<Planificacion>) session.createQuery("FROM Planificacion ").list();
     * <p>
     * tx.commit();
     * } catch (HibernateException he) {
     * if (tx != null) tx.rollback();
     * he.printStackTrace();
     * } finally {
     * session.close();
     * }
     * return planificacionsArrayList;
     * }
     */
    public ObservableList<String> listOfBrigadas(int idEmp) {
        brigadasList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Brigadaconstruccion WHERE empresaconstructoraId =: empId");
            query.setParameter("empId", idEmp);
            brigadaconstruccionsArrayList = (ArrayList<Brigadaconstruccion>) ((org.hibernate.query.Query) query).list();
            for (Brigadaconstruccion brigadaconstruccion : brigadaconstruccionsArrayList) {
                brigadasList.add(brigadaconstruccion.getCodigo() + " - " + brigadaconstruccion.getDescripcion());
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return brigadasList;
    }

    public ObservableList<String> listOfGrupos(int idBrigada) {
        grupoList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Grupoconstruccion WHERE brigadaconstruccionId =: briId");
            query.setParameter("briId", idBrigada);
            grupoconstruccionArrayList = (ArrayList<Grupoconstruccion>) ((org.hibernate.query.Query) query).list();
            for (Grupoconstruccion grupoconstruccion : grupoconstruccionArrayList) {
                grupoList.add(grupoconstruccion.getCodigo() + " - " + grupoconstruccion.getDescripcion());
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return grupoList;
    }

    public ObservableList<String> listOfCuadrilla(int idGrupo) {
        cuadrilaList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Cuadrillaconstruccion WHERE grupoconstruccionId =: grupId");
            query.setParameter("grupId", idGrupo);
            cuadrillaconstruccionsArrayList = (ArrayList<Cuadrillaconstruccion>) ((org.hibernate.query.Query) query).list();
            for (Cuadrillaconstruccion cuadrillaconstruccion : cuadrillaconstruccionsArrayList) {
                cuadrilaList.add(cuadrillaconstruccion.getCodigo() + " - " + cuadrillaconstruccion.getDescripcion());
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cuadrilaList;
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
        checkControl.setOnMouseClicked(event -> {
            if (checkControl.isSelected() == true) {
                combocuadrilla.setDisable(false);
            } else {
                combocuadrilla.setDisable(true);
            }
        });

    }

    public void cargarUnidadesObra(int idObra, int idEmp, int idZona, int idObj, int idNiv, int idEsp, int idSub) {

        unidadobraArrayList = new ArrayList<Unidadobra>();
        unidadobraArrayList = getUnidadesObra(idObra, idEmp, idZona, idObj, idNiv, idEsp, idSub);

        brigadasList = FXCollections.observableArrayList();
        brigadasList = listOfBrigadas(idEmp);
        combo_Brigada.setItems(brigadasList);

    }

    public void keyTypeCode(KeyEvent event) {
        if (fieldcodigouo.getText().length() == inputLength) {
            for (Unidadobra unidadobra : unidadobraArrayList) {
                if (unidadobra.getCodigo().contentEquals(fieldcodigouo.getText())) {
                    unidadobraModel = unidadobra;

                    valoresCert = new Double[6];
                    valoresCert = getValoresCertUO(unidadobra.getId());
                    costMaterialCert = 0.0;
                    costManoCert = 0.0;
                    costEquipoCert = 0.0;
                    costCUCSalarioCert = 0.0;
                    costSalarioCert = 0.0;
                    costMaterialCert = valoresCert[1];
                    costManoCert = valoresCert[2];
                    costEquipoCert = valoresCert[3];
                    costCUCSalarioCert = valoresCert[5];
                    costSalarioCert = valoresCert[4];

                    double val = unidadobra.getCantidad() - valoresCert[0];
                    if (val > 0) {
                        labelDispo.setText(String.valueOf(val));
                    } else if (val == 0) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Cantidad a certificar no disponible!");
                        alert.showAndWait();
                        btnAdd.setDisable(true);
                    }

                }
            }
        }

    }

    public void handleLlenaGrupoList(ActionEvent event) {
        String[] codeBriPart = combo_Brigada.getValue().split(" - ");

        brigadaCont = brigadaconstruccionsArrayList.stream().filter(b -> b.getCodigo().contentEquals(codeBriPart[0])).findFirst().orElse(null);
        grupoList = listOfGrupos(brigadaCont.getId());
        combogrupo.setItems(grupoList);

        garbache = Runtime.getRuntime();
        garbache.gc();

    }

    public void handleLlenaCuadrillaList(ActionEvent event) {
        String[] codeGrupPart = combogrupo.getValue().split(" - ");

        grupoconst = grupoconstruccionArrayList.stream().filter(g -> g.getCodigo().contentEquals(codeGrupPart[0])).findFirst().orElse(null);
        cuadrilaList = listOfCuadrilla(grupoconst.getId());
        combocuadrilla.setItems(cuadrilaList);

        garbache = Runtime.getRuntime();
        garbache.gc();

    }

    public Task createTimeMesage() {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                //for (int i = 0; i < ; i++) {
                Thread.sleep(1000);
                //  updateProgress(i + 1, val);
                //}
                return true;
            }
        };
    }

    public void handleAddPlan(ActionEvent event) {

        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Planificando elementos en la unidad de obra... espere");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        if (Double.parseDouble(fieldcantidad.getText()) > Double.parseDouble(labelDispo.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Cantidad a certificar mayor que la cantidad disponible!");
            alert.showAndWait();

        }
        if (checkControl.isSelected() == false) {
            if (combo_Brigada.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar una Brigada de Contrucción!");
                alert.showAndWait();
            }

            unidadObraId = unidadobraModel.getId();

            valCM = 0.0;
            valCMan = 0.0;
            valEquip = 0.0;
            valSal = 0.0;
            valCucSal = 0.0;

            valCM = unidadobraModel.getCostoMaterial() - costMaterialCert;
            valCMan = unidadobraModel.getCostomano() - costManoCert;
            valEquip = unidadobraModel.getCostoequipo() - costEquipoCert;
            valSal = unidadobraModel.getSalario() - costSalarioCert;
            valCucSal = unidadobraModel.getSalariocuc() - costCUCSalarioCert;


            costMaterial = 0.0;
            costMano = 0.0;
            costEquipo = 0.0;
            costSalario = 0.0;
            costCUCSalario = 0.0;

            valToPlan = 0.0;
            valToPlan = unidadobraModel.getCantidad() - valoresCert[0];


            costMaterial = valCM * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
            costMano = valCMan * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
            costEquipo = valEquip * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
            costSalario = valSal * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
            costCUCSalario = valCucSal * Double.parseDouble(fieldcantidad.getText()) / valToPlan;


            String pattern = "dd/MM/yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            LocalDate dateDes = dateini.getValue();
            LocalDate dateHast = datehasta.getValue();
            desde = Date.valueOf(dateDes);
            hasta = Date.valueOf(dateHast);


            planificacion = new Planificacion();
            planificacion.setCostomaterial(Math.round(costMaterial * 100d) / 100d);
            planificacion.setCostomano(Math.round(costMano * 100d) / 100d);
            planificacion.setCostoequipo(Math.round(costEquipo * 100d) / 100d);
            planificacion.setCostosalario(Math.round(costSalario * 100d) / 100d);
            planificacion.setCostsalariocuc(Math.round(costCUCSalario * 100d) / 100d);
            planificacion.setBrigadaconstruccionId(brigadaCont.getId());
            planificacion.setGrupoconstruccionId(grupoconst.getId());
            planificacion.setCuadrillaconstruccionId(null);
            planificacion.setUnidadobraId(unidadObraId);
            planificacion.setDesde(desde);
            planificacion.setHasta(hasta);
            planificacion.setCantidad(Double.parseDouble(fieldcantidad.getText()));


            Integer idcert = addPlanificacion(planificacion);

            Thread t1 = new PlanificarUORenglonesThread(unidadObraId, idcert, desde, hasta, Double.parseDouble(fieldcantidad.getText()), valToPlan);
            t1.start();

            Thread t2 = new PlanificarRecursosRenglonesThread(unidadObraId, idcert, desde, hasta, Double.parseDouble(fieldcantidad.getText()), valToPlan);
            t2.start();


            if (idcert != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Datos insertados satisfactoriamente!");
                alert.showAndWait();
            }

            fieldcantidad.clear();

            valoresCert = new Double[6];
            valoresCert = getValoresCertUO(unidadObraId);
            costMaterialCert = 0.0;
            costManoCert = 0.0;
            costEquipoCert = 0.0;
            costCUCSalarioCert = 0.0;
            costSalarioCert = 0.0;
            costMaterialCert = valoresCert[1];
            costManoCert = valoresCert[2];
            costEquipoCert = valoresCert[3];
            costCUCSalarioCert = valoresCert[5];
            costSalarioCert = valoresCert[4];


            double val = unidadobraModel.getCantidad() - valoresCert[0];
            labelDispo.setText(String.valueOf(val));
            if (val == 0) {
                btnAdd.setDisable(true);
            }

        } else {
            if (combo_Brigada.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar una Brigada de Contrucción!");
                alert.showAndWait();
            } else if (combogrupo.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar un Grupo de Contrucción!");
                alert.showAndWait();
            } else if (combocuadrilla.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar una Cuadrilla de Contrucción!");
                alert.showAndWait();
            } else if (dateini.getValue() == null || datehasta.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar periódo de tiempo para la certificación!");
                alert.showAndWait();
            }

            String[] codeCuadriPart = combocuadrilla.getValue().split(" - ");


            unidadObraId = unidadobraModel.getId();
            cuadrillaConst = cuadrillaconstruccionsArrayList.stream().filter(c -> c.getCodigo().contentEquals(codeCuadriPart[0])).findFirst().orElse(null);


            valCM = 0.0;
            valCMan = 0.0;
            valEquip = 0.0;
            valSal = 0.0;
            valCucSal = 0.0;

            valCM = unidadobraModel.getCostoMaterial() - costMaterialCert;
            valCMan = unidadobraModel.getCostomano() - costManoCert;
            valEquip = unidadobraModel.getCostoequipo() - costEquipoCert;
            valSal = unidadobraModel.getSalario() - costSalarioCert;
            valCucSal = unidadobraModel.getSalariocuc() - costCUCSalarioCert;

            costMaterial = 0.0;
            costMano = 0.0;
            costEquipo = 0.0;
            costSalario = 0.0;
            costCUCSalario = 0.0;

            valToPlan = 0.0;
            valToPlan = unidadobraModel.getCantidad() - valoresCert[0];


            costMaterial = valCM * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
            costMano = valCMan * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
            costEquipo = valEquip * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
            costSalario = valSal * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
            costCUCSalario = valCucSal * Double.parseDouble(fieldcantidad.getText()) / valToPlan;

            String pattern = "dd/MM/yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            LocalDate dateDes = dateini.getValue();
            LocalDate dateHast = datehasta.getValue();
            desde = Date.valueOf(dateDes);
            hasta = Date.valueOf(dateHast);


            planificacion = new Planificacion();
            planificacion.setCostomaterial(Math.round(costMaterial * 100d) / 100d);
            planificacion.setCostomano(Math.round(costMano * 100d) / 100d);
            planificacion.setCostoequipo(Math.round(costEquipo * 100d) / 100d);
            planificacion.setCostosalario(Math.round(costSalario * 100d) / 100d);
            planificacion.setCostsalariocuc(Math.round(costCUCSalario * 100d) / 100d);
            planificacion.setBrigadaconstruccionId(brigadaCont.getId());
            planificacion.setGrupoconstruccionId(grupoconst.getId());
            planificacion.setCuadrillaconstruccionId(cuadrillaConst.getId());
            planificacion.setUnidadobraId(unidadObraId);
            planificacion.setDesde(desde);
            planificacion.setHasta(hasta);
            planificacion.setCantidad(Double.parseDouble(fieldcantidad.getText()));


            Integer idcert = addPlanificacion(planificacion);

            Thread t1 = new PlanificarUORenglonesThread(unidadObraId, idcert, desde, hasta, Double.parseDouble(fieldcantidad.getText()), valToPlan);
            t1.start();

            Thread t2 = new PlanificarRecursosRenglonesThread(unidadObraId, idcert, desde, hasta, Double.parseDouble(fieldcantidad.getText()), valToPlan);
            t2.start();

            if (idcert != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Datos insertados satisfactoriamente!");
                alert.showAndWait();
            }

            fieldcantidad.clear();

            valoresCert = new Double[6];
            valoresCert = getValoresCertUO(unidadObraId);
            costMaterialCert = 0.0;
            costManoCert = 0.0;
            costEquipoCert = 0.0;
            costCUCSalarioCert = 0.0;
            costSalarioCert = 0.0;
            costMaterialCert = valoresCert[1];
            costManoCert = valoresCert[2];
            costEquipoCert = valoresCert[3];
            costCUCSalarioCert = valoresCert[5];
            costSalarioCert = valoresCert[4];


            double val = unidadobraModel.getCantidad() - valoresCert[0];
            labelDispo.setText(String.valueOf(val));
            if (val == 0) {
                btnAdd.setDisable(true);
            }

        }


    }

}
