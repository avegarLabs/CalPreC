package views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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

public class ActualizarPlanController implements Initializable {

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
    private double cantidadPlan;
    private int idPlanf;
    private Planificacion planificacion;


    private Double[] valoresCert;

    @FXML
    private JFXCheckBox checkCombo;

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

    public Double[] getValoresCert(Integer idUO, Integer idBrigada, Integer idgrupo, Integer cuadrillaId) {
        valoresCert = new Double[6];

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

    public Double[] getValoresCertsinCuad(Integer idUO, Integer idBrigada, Integer idgrupo) {
        valoresCert = new Double[6];

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

    public void updatePlanificacion(Planificacion planif) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(" FROM Planificacion WHERE id =: idPlan");
            query.setParameter("idPlan", idPlanf);

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

            if (planificacion != null) {
                planificacion.setCostomaterial(planif.getCostomaterial());
                planificacion.setCostomano(planif.getCostomano());
                planificacion.setCostoequipo(planif.getCostoequipo());
                planificacion.setCostosalario(planif.getCostosalario());
                planificacion.setCostsalariocuc(planif.getCostsalariocuc());
                planificacion.setBrigadaconstruccionId(planif.getBrigadaconstruccionId());
                planificacion.setGrupoconstruccionId(planif.getGrupoconstruccionId());
                planificacion.setCuadrillaconstruccionId(planif.getCuadrillaconstruccionId());
                planificacion.setUnidadobraId(planif.getUnidadobraId());
                planificacion.setDesde(planif.getDesde());
                planificacion.setHasta(planif.getHasta());
                planificacion.setCantidad(planif.getCantidad());
                session.update(planificacion);

            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        checkCombo.setOnMouseClicked(event -> {
            if (checkCombo.isSelected() == true) {
                combocuadrilla.setDisable(false);
                cuadrilaList = listOfCuadrilla(planificacion.getGrupoconstruccionId());
                combocuadrilla.setItems(cuadrilaList);
            } else {
                combocuadrilla.setDisable(true);
            }

        });

    }

    public void cargarUnidadesObra(PlanificacionUnidadObraView planificacionView, String codeUO) {

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

        valoresCert = new Double[6];
        if (cuadrillaId != null) {
            valoresCert = getValoresCert(unidadobraModel.getId(), brigadaId, grupoId, cuadrillaId);
        } else if (cuadrillaId == null) {
            valoresCert = getValoresCertsinCuad(unidadobraModel.getId(), brigadaId, grupoId);

        }

        if (cuadrillaId != null) {
            cuadrilaList = listOfCuadrilla(planificacion.getGrupoconstruccionId());
            combocuadrilla.setItems(cuadrilaList);
            combocuadrilla.getSelectionModel().select(planificacion.getCuadrillaconstruccionByCuadrillaconstruccionId().getCodigo() + " - " + planificacion.getCuadrillaconstruccionByCuadrillaconstruccionId().getDescripcion());
        }

        fieldcantidad.setText(String.valueOf(planificacion.getCantidad()));

        labelDispo.setText(String.valueOf(unidadobraModel.getCantidad() - valoresCert[0]));
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


        if (Double.parseDouble(fieldcantidad.getText()) > Double.parseDouble(labelDispo.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Cantidad a certificar mayor que la cantidad disponible!");
            alert.showAndWait();
        }

        if (checkCombo.isSelected() == false) {

            unidadObraId = unidadobraModel.getId();
/*
            cuadrillaconstruccionsArrayList.forEach(cuadrillaconstruccion -> {
                if (cuadrillaconstruccion.getCodigo().contentEquals(code)) {
                    cuadrillaId = cuadrillaconstruccion.getId();
                }
            });
*/
            valCM = 0.0;
            valCMan = 0.0;
            valEquip = 0.0;
            valSal = 0.0;
            valCucSal = 0.0;

            valCM = unidadobraModel.getCostoMaterial() - valoresCert[1];
            valCMan = unidadobraModel.getCostomano() - valoresCert[2];
            valEquip = unidadobraModel.getCostoequipo() - valoresCert[3];
            valSal = unidadobraModel.getSalario() - valoresCert[4];
            valCucSal = unidadobraModel.getSalariocuc() - valoresCert[5];

            costMaterial = 0.0;
            costMano = 0.0;
            costEquipo = 0.0;
            costSalario = 0.0;
            costCUCSalario = 0.0;

            double valToPlan = 0.0;
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
            planificacion.setBrigadaconstruccionId(brigadaId);
            planificacion.setGrupoconstruccionId(grupoId);
            planificacion.setCuadrillaconstruccionId(null);
            planificacion.setUnidadobraId(unidadObraId);
            planificacion.setDesde(desde);
            planificacion.setHasta(hasta);
            planificacion.setCantidad(Double.parseDouble(fieldcantidad.getText()));

            System.out.println("Costo Material: " + planificacion.getCostomaterial());

            updatePlanificacion(planificacion);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Datos insertados satisfactoriamente!");
            alert.showAndWait();

        } else if (checkCombo.isSelected() == true) {

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

            valCM = unidadobraModel.getCostoMaterial() - valoresCert[1];
            valCMan = unidadobraModel.getCostomano() - valoresCert[2];
            valEquip = unidadobraModel.getCostoequipo() - valoresCert[3];
            valSal = unidadobraModel.getSalario() - valoresCert[4];
            valCucSal = unidadobraModel.getSalariocuc() - valoresCert[5];

            costMaterial = 0.0;
            costMano = 0.0;
            costEquipo = 0.0;
            costSalario = 0.0;
            costCUCSalario = 0.0;

            double valToPlan = 0.0;
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
            planificacion.setBrigadaconstruccionId(brigadaId);
            planificacion.setGrupoconstruccionId(grupoId);
            planificacion.setCuadrillaconstruccionId(cuadrillaId);
            planificacion.setUnidadobraId(unidadObraId);
            planificacion.setDesde(desde);
            planificacion.setHasta(hasta);
            planificacion.setCantidad(Double.parseDouble(fieldcantidad.getText()));


            updatePlanificacion(planificacion);

        }

    }

}


