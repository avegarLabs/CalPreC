package views;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
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
import java.util.List;
import java.util.ResourceBundle;

public class AjustarPlanController implements Initializable {

    private static SessionFactory sf;
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
    private JFXButton btn_close;
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
    private Integer unidadObraId;
    private Integer brigadaId;
    private Integer grupoId;
    private Integer cuadrillaId;
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
    private Date desde;
    private Date hasta;
    private Integer idPlanf;
    private double cantidadPlan;
    private Double[] certValues;


    private Planificacion planificacion;
    private ArrayList<Planificacion> planificacionsArrayList;


    @FXML
    private JFXCheckBox ckeckbox;


    private ObservableList<String> getUnidadesObra(int idObra, int idEmp, int idZona, int idObj, int idNiv, int idEsp, int idSub) {

        listUO = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Unidadobra where obraId = :idObra and empresaconstructoraId =:idEmp AND zonasId = :idZona and objetosId =: idObjeto and nivelId =: idNivel and especialidadesId =: idEspecilidad and subespecialidadesId =:idSub");
            query.setParameter("idObra", idObra);
            query.setParameter("idEmp", idEmp);
            query.setParameter("idZona", idZona);
            query.setParameter("idObjeto", idObj);
            query.setParameter("idNivel", idNiv);
            query.setParameter("idEspecilidad", idEsp);
            query.setParameter("idSub", idSub);
            unidadobraArrayList = (ArrayList<Unidadobra>) ((org.hibernate.query.Query) query).list();
            unidadobraArrayList.forEach(unidadobra -> {
                listUO.add(unidadobra.getCodigo() + " - " + unidadobra.getDescripcion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listUO;
    }

    public ArrayList<Planificacion> getPlanificaciones() {
        planificacionsArrayList = new ArrayList<Planificacion>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            planificacionsArrayList = (ArrayList<Planificacion>) session.createQuery("FROM Planificacion ").list();

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return planificacionsArrayList;
    }

    public ObservableList<String> listOfBrigadas(int idEmp) {
        brigadasList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Brigadaconstruccion WHERE empresaconstructoraId =: empId");
            query.setParameter("empId", idEmp);
            brigadaconstruccionsArrayList = (ArrayList<Brigadaconstruccion>) ((org.hibernate.query.Query) query).list();
            brigadaconstruccionsArrayList.forEach(brigadaconstruccion -> {
                brigadasList.add(brigadaconstruccion.getCodigo() + " - " + brigadaconstruccion.getDescripcion());
            });

            tx.commit();
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
            grupoconstruccionArrayList.forEach(grupoconstruccion -> {
                grupoList.add(grupoconstruccion.getCodigo() + " - " + grupoconstruccion.getDescripcion());
            });

            tx.commit();
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
            cuadrillaconstruccionsArrayList.forEach(cuadrillaconstruccion -> {
                cuadrilaList.add(cuadrillaconstruccion.getCodigo() + " - " + cuadrillaconstruccion.getDescripcion());
            });

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
            certId = (Integer) session.save(planificacion);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return certId;
    }


    /**
     * Para determinar la cantidad certificad
     */

    public Double getCantCertificada(int idUnidadObra) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            cantidadPlan = 0.0;
            Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacion WHERE unidadobraId =: id");
            query.setParameter("id", idUnidadObra);
            if (query.getSingleResult() != null) {
                cantidadPlan = (Double) query.getSingleResult();
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cantidadPlan;
    }

    /**
     * Para determinar los valoeres certificados tras ajustes
     */

    public Double[] getValoresCert(Integer idUO, Integer idBrigada, Integer idgrupo, Integer cuadrillaId) {
        certValues = new Double[6];

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createSQLQuery(" SELECT * FROM get_values_certificates_unidad_obra(:idU, :idBrig, :idGru, :idCuad)");
            query.setParameter("idU", idUO);
            query.setParameter("idBrig", idBrigada);
            query.setParameter("idGru", idgrupo);
            query.setParameter("idCuad", cuadrillaId);

            List<Object[]> recuo = ((NativeQuery) query).list();
            for (Object[] row : recuo) {
                certValues[0] = Double.parseDouble(row[0].toString());
                certValues[1] = Double.parseDouble(row[1].toString());
                certValues[2] = Double.parseDouble(row[2].toString());
                certValues[3] = Double.parseDouble(row[3].toString());
                certValues[4] = Double.parseDouble(row[4].toString());
                certValues[5] = Double.parseDouble(row[5].toString());

            }
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return certValues;
    }

    public Double[] getValoresCertsinCuad(Integer idUO, Integer idBrigada, Integer idgrupo) {
        certValues = new Double[6];

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createSQLQuery(" SELECT * FROM get_values_certificates_unidad_obra_not_cuad(:idU, :idBrig, :idGru)");
            query.setParameter("idU", idUO);
            query.setParameter("idBrig", idBrigada);
            query.setParameter("idGru", idgrupo);


            List<Object[]> recuo = ((NativeQuery) query).list();
            for (Object[] row : recuo) {
                certValues[0] = Double.parseDouble(row[0].toString());
                certValues[1] = Double.parseDouble(row[1].toString());
                certValues[2] = Double.parseDouble(row[2].toString());
                certValues[3] = Double.parseDouble(row[3].toString());
                certValues[4] = Double.parseDouble(row[4].toString());
                certValues[5] = Double.parseDouble(row[5].toString());

            }
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return certValues;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ckeckbox.setOnMouseClicked(event -> {
            if (ckeckbox.isSelected() == true) {
                combocuadrilla.setDisable(false);
            } else {
                combocuadrilla.setDisable(true);
            }
        });

    }

    public Planificacion getPlanificaciones(Integer id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery(" FROM Planificacion WHERE id =: idPlan");
            query.setParameter("idPlan", id);

            ArrayList<Planificacion> datos = (ArrayList<Planificacion>) query.getResultList();
            datos.forEach(dat -> {

                if (dat.getCuadrillaconstruccionId() == null) {
                    planificacion = new Planificacion();
                    planificacion = dat;
                    planificacion.setCuadrillaconstruccionId(null);

                } else {
                    planificacion = new Planificacion();
                    planificacion = dat;
                }
            });


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return planificacion;
    }

    private Unidadobra getUnidadesObra(String codeUO) {

        listUO = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Unidadobra WHERE codigo =: code");
            query.setParameter("code", codeUO);

            unidadobraModel = (Unidadobra) query.getSingleResult();

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return unidadobraModel;
    }


    public void cargarUnidadesObra(PlanificacionUnidadObraView planificacionView, String codeUO, double cant) {


        planificacion = getPlanificaciones(planificacionView.getId());
        idPlanf = planificacionView.getId();

        unidadobraModel = getUnidadesObra(codeUO);

        combo_Brigada.setItems(listOfBrigadas(unidadobraModel.getEmpresaconstructoraId()));
        combogrupo.setItems(listOfGrupos(planificacion.getBrigadaconstruccionId()));

        combo_Brigada.getSelectionModel().select(planificacion.getBrigadaconstruccionByBrigadaconstruccionId().getCodigo() + " - " + planificacion.getBrigadaconstruccionByBrigadaconstruccionId().getDescripcion());
        combogrupo.getSelectionModel().select(planificacion.getGrupoconstruccionByGrupoconstruccionId().getCodigo() + " - " + planificacion.getGrupoconstruccionByGrupoconstruccionId().getDescripcion());

        brigadaId = planificacion.getBrigadaconstruccionId();
        grupoId = planificacion.getGrupoconstruccionId();
        cuadrillaId = planificacion.getCuadrillaconstruccionId();

        certValues = new Double[6];
        if (cuadrillaId != null) {
            certValues = getValoresCert(unidadobraModel.getId(), brigadaId, grupoId, cuadrillaId);
            combocuadrilla.setDisable(false);
        } else if (cuadrillaId == null) {
            certValues = getValoresCertsinCuad(unidadobraModel.getId(), brigadaId, grupoId);

        }

        if (cuadrillaId != null) {
            cuadrilaList = listOfCuadrilla(planificacion.getGrupoconstruccionId());
            combocuadrilla.setItems(cuadrilaList);
            combocuadrilla.getSelectionModel().select(planificacion.getCuadrillaconstruccionByCuadrillaconstruccionId().getCodigo() + " - " + planificacion.getCuadrillaconstruccionByCuadrillaconstruccionId().getDescripcion());
        }

        fieldcantidad.setText(String.valueOf(cant));

//        labelDispo.setText(String.valueOf(cant));
        fieldcodigouo.setText(unidadobraModel.getCodigo());

        LocalDate dateDesde = planificacion.getDesde().toLocalDate();
        LocalDate dateHasta = planificacion.getHasta().toLocalDate();
        dateini.setValue(dateDesde);
        datehasta.setValue(dateHasta);

    }


    public void handleLlenaGrupoList(ActionEvent event) {
        String[] codeBriPart = combo_Brigada.getValue().split(" - ");
        String code = codeBriPart[0];

        brigadaconstruccionsArrayList.forEach(brigadaconstruccion -> {
            if (brigadaconstruccion.getCodigo().contentEquals(code)) {
                brigadaId = brigadaconstruccion.getId();
                grupoList = listOfGrupos(brigadaconstruccion.getId());
                combogrupo.setItems(grupoList);
            }

        });
    }

    public void handleLlenaCuadrillaList(ActionEvent event) {
        String[] codeGrupPart = combogrupo.getValue().split(" - ");
        String code = codeGrupPart[0];

        grupoconstruccionArrayList.forEach(grupoconstruccion -> {
            if (grupoconstruccion.getCodigo().contentEquals(code)) {
                grupoId = grupoconstruccion.getId();
                cuadrilaList = listOfCuadrilla(grupoconstruccion.getId());
                combocuadrilla.setItems(cuadrilaList);
            }

        });
    }


    public void handleAddPlan(ActionEvent event) {

        if (ckeckbox.isSelected() == false) {
            if (combo_Brigada.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar una Brigada de Contrucción!");
                alert.showAndWait();
            }

//            String[] codeCuadriPart = combocuadrilla.getValue().split(" - ");
            //          String code = codeCuadriPart[0];

            unidadObraId = unidadobraModel.getId();

            //        cuadrillaconstruccionsArrayList.forEach(cuadrillaconstruccion -> {
            //          if (cuadrillaconstruccion.getCodigo().contentEquals(code)) {
            //            cuadrillaId = cuadrillaconstruccion.getId();
            //      }
            // });
            valCM = 0.0;
            valCMan = 0.0;
            valEquip = 0.0;
            valSal = 0.0;
            valCucSal = 0.0;

            valCM = unidadobraModel.getCostoMaterial() - certValues[1];
            valCMan = unidadobraModel.getCostomano() - certValues[2];
            valEquip = unidadobraModel.getCostoequipo() - certValues[3];
            valSal = unidadobraModel.getSalario() - certValues[4];
            valCucSal = unidadobraModel.getSalariocuc() - certValues[5];

            costMaterial = 0.0;
            costMano = 0.0;
            costEquipo = 0.0;
            costSalario = 0.0;
            costCUCSalario = 0.0;

            double valToPlan = 0.0;
            valToPlan = unidadobraModel.getCantidad() - certValues[0];


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
            planificacion.setBrigadaconstruccionId(brigadaId);
            planificacion.setGrupoconstruccionId(grupoId);
            planificacion.setCuadrillaconstruccionId(null);
            planificacion.setUnidadobraId(unidadObraId);
            planificacion.setDesde(desde);
            planificacion.setHasta(hasta);
            planificacion.setCantidad(Double.parseDouble(fieldcantidad.getText()));

            Integer idcert = addPlanificacion(planificacion);

            if (idcert != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Datos insertados satisfactoriamente!");
                alert.showAndWait();
            }

            fieldcodigouo.clear();
            labelDispo.setText(" ");
            fieldcantidad.clear();

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
            String code = codeCuadriPart[0];

            unidadObraId = unidadobraModel.getId();

            cuadrillaconstruccionsArrayList.forEach(cuadrillaconstruccion -> {
                if (cuadrillaconstruccion.getCodigo().contentEquals(code)) {
                    cuadrillaId = cuadrillaconstruccion.getId();
                }
            });
            valCM = 0.0;
            valCMan = 0.0;
            valEquip = 0.0;
            valSal = 0.0;
            valCucSal = 0.0;

            valCM = unidadobraModel.getCostoMaterial() - certValues[1];
            valCMan = unidadobraModel.getCostomano() - certValues[2];
            valEquip = unidadobraModel.getCostoequipo() - certValues[3];
            valSal = unidadobraModel.getSalario() - certValues[4];
            valCucSal = unidadobraModel.getSalariocuc() - certValues[5];

            costMaterial = 0.0;
            costMano = 0.0;
            costEquipo = 0.0;
            costSalario = 0.0;
            costCUCSalario = 0.0;

            double valToPlan = 0.0;
            valToPlan = unidadobraModel.getCantidad() - certValues[0];


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
            planificacion.setBrigadaconstruccionId(brigadaId);
            planificacion.setGrupoconstruccionId(grupoId);
            planificacion.setCuadrillaconstruccionId(cuadrillaId);
            planificacion.setUnidadobraId(unidadObraId);
            planificacion.setDesde(desde);
            planificacion.setHasta(hasta);
            planificacion.setCantidad(Double.parseDouble(fieldcantidad.getText()));


            Integer idcert = addPlanificacion(planificacion);

            if (idcert != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Datos insertados satisfactoriamente!");
                alert.showAndWait();
            }

            fieldcodigouo.clear();
            labelDispo.setText(" ");
            fieldcantidad.clear();
        }
    }


    public void handleCancelarOperaciones(ActionEvent event) {

        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();

    }

}
