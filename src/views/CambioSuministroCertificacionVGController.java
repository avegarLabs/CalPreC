package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CambioSuministroCertificacionVGController implements Initializable {

    private static SessionFactory sf;
    public Integer idObra;
    public Obra obraToChange;
    CertificarRenglonVarianteController certificarRenglonVarianteController;
    CambioSuministroCertificacionVGController cambioSuministroCertificacionController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXTextField fieldPrecio;
    @FXML
    private javafx.scene.control.TextArea textDedscripcion;
    @FXML
    private Label labelValue;
    @FXML
    private JFXButton btn_exit;
    private Integer idNEsp;
    private Integer idRenglon;
    private Integer idRecToChan;
    private Double cantDisponible;
    private Recursos recursos;
    private Juegoproducto juegoproducto;
    private Suministrossemielaborados suministrossemielaborados;
    private Integer idTransaccion;
    private Integer idnewRec;
    private Double precio;
    private Date desdeDate;
    private Date hastaData;
    private double costMaterial;
    private Double importeSum;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private Bajoespecificacionrv insumoRV;
    private List<Recursos> recursosArrayList;
    private List<Suministrossemielaborados> suministrossemielaboradosArrayList;
    private List<Juegoproducto> juegoproductoArrayList;
    private ObservableList<String> listSuministros;
    private ReportProjectStructureSingelton rep = ReportProjectStructureSingelton.getInstance();
    private String tipo;
    private ReportProjectStructureSingelton reportProjectStructureSingelton = ReportProjectStructureSingelton.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cambioSuministroCertificacionController = this;
    }

    public void handleCloseWindows(ActionEvent event) {

        Stage stage = (Stage) btn_exit.getScene().getWindow();
        stage.close();
    }

    public ObservableList<String> listToSugestionSuministros(Obra obra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        listSuministros = FXCollections.observableArrayList();
        recursosArrayList = new ArrayList<>();
        juegoproductoArrayList = new ArrayList<>();
        suministrossemielaboradosArrayList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (obra.getSalarioBySalarioId().getTag().trim().equals("R266")) {
                System.out.println("V1");
                recursosArrayList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece not like 'T%' and pertenece != 'I' and active = 1 and tipo = '1'").getResultList();
                listSuministros.addAll(recursosArrayList.parallelStream().distinct().filter(rec -> rec.getTipo().equals("1")).map(Recursos::toString).collect(Collectors.toList()));
                juegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto WHERE pertenece not like 'T%' and pertenece != 'I'").getResultList();
                listSuministros.addAll(juegoproductoArrayList.parallelStream().distinct().map(Juegoproducto::toString).collect(Collectors.toList()));
                suministrossemielaboradosArrayList = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados WHERE  pertenec not like 'T%' and pertenec != 'I'").getResultList();
                listSuministros.addAll(suministrossemielaboradosArrayList.parallelStream().distinct().map(Suministrossemielaborados::toString).collect(Collectors.toList()));
            } else if (obra.getSalarioBySalarioId().getTag().trim().startsWith("T")) {
                recursosArrayList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece != 'R266' and pertenece != 'I' and active = 1 and tipo = '1'").getResultList();
                listSuministros.addAll(recursosArrayList.parallelStream().distinct().filter(rec -> rec.getTipo().equals("1")).map(Recursos::toString).collect(Collectors.toList()));
                juegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto WHERE pertenece != 'R266' and pertenece != 'I'").getResultList();
                listSuministros.addAll(juegoproductoArrayList.parallelStream().distinct().map(Juegoproducto::toString).collect(Collectors.toList()));
                suministrossemielaboradosArrayList = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados WHERE pertenec != 'I' and pertenec != 'R266'").getResultList();
                listSuministros.addAll(suministrossemielaboradosArrayList.parallelStream().distinct().map(Suministrossemielaborados::toString).collect(Collectors.toList()));
            }
            tx.commit();
            session.close();
            return listSuministros;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void cargarDatostoChange(int Rec, int idOb) {
        idObra = idOb;
        Obra obra = rep.getObra(idOb);
        idRecToChan = Rec;
        TextFields.bindAutoCompletion(field_codigo, listToSugestionSuministros(obra)).setPrefWidth(500);
    }

    public void handleGetResources(ActionEvent event) {
        Recursos recursos = recursosArrayList.stream().filter(r -> r.toString().equals(field_codigo.getText().trim())).findFirst().orElse(null);
        if (recursos != null) {
            field_codigo.setText(recursos.getCodigo());
            idnewRec = recursos.getId();
            fieldPrecio.setText(String.valueOf(recursos.getPreciomn()));
            labelValue.setText(recursos.getUm());
            textDedscripcion.setText(recursos.getDescripcion());
            ;
            precio = Math.round(recursos.getPreciomn() * 100d) / 100d;
            tipo = "1";
        }

        Juegoproducto juegoproducto = juegoproductoArrayList.stream().filter(jp -> jp.toString().equals(field_codigo.getText())).findFirst().orElse(null);
        if (juegoproducto != null) {
            field_codigo.setText(juegoproducto.getCodigo());
            idnewRec = recursos.getId();
            fieldPrecio.setText(String.valueOf(juegoproducto.getPreciomn()));
            labelValue.setText(juegoproducto.getUm());
            textDedscripcion.setText(juegoproducto.getDescripcion());
            ;
            precio = Math.round(juegoproducto.getPreciomn() * 100d) / 100d;
            tipo = "J";
        }

        Suministrossemielaborados suministrossemielaborados = suministrossemielaboradosArrayList.stream().filter(sm -> sm.toString().equals(field_codigo.getText())).findFirst().orElse(null);
        if (suministrossemielaborados != null) {
            field_codigo.setText(suministrossemielaborados.getCodigo());
            idnewRec = suministrossemielaborados.getId();
            fieldPrecio.setText(String.valueOf(suministrossemielaborados.getPreciomn()));
            labelValue.setText(recursos.getUm());
            textDedscripcion.setText(suministrossemielaborados.getDescripcion());
            ;
            precio = Math.round(suministrossemielaborados.getPreciomn() * 100d) / 100d;
            tipo = "J";
        }
    }

    public void handleSaveChange(ActionEvent event) {
        List<Bajoespecificacionrv> temp = utilCalcSingelton.getBajoespecificacionrv(idObra, idRecToChan);
        System.out.println(temp.size());
        try {
            changeSumInAllObra(temp);
            utilCalcSingelton.showAlert("Suministros cambiado satisfactoriamente", 1);
            handleCloseWindows(event);
        } catch (Exception e) {
            utilCalcSingelton.showAlert("Error al cambiar el recurso", 3);
        }

        /*
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Desea realizar el cambio solo en el nivel especifico actual o cambiarlo en toda la obra?");
        ButtonType buttonAgregar = new ButtonType("Solo aquí");
        ButtonType buttonSobre = new ButtonType("Cambiar todos");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonAgregar, buttonSobre, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonAgregar) {
            cantDisponible = insumoRV.getCantidad();
            deletePlaRec(insumoRV);
            Bajoespecificacionrv bajo = createNewbajo(cantDisponible);
            try {
                int id = savePlaRec(bajo);
                utilCalcSingelton.showAlert("Suministros cambiado satisfactoriamente", 1);
                handleCloseWindows(event);
            } catch (Exception e) {
                utilCalcSingelton.showAlert("Error al cambiar el recurso", 3);
            }
        } else if (result.get() == buttonSobre) {
            List<Bajoespecificacionrv> temp = utilCalcSingelton.getBajoespecificacionrv(idObra, idRecToChan);
            System.out.println(temp.size());
            try {
                changeSumInAllObra(temp);
                utilCalcSingelton.showAlert("Suministros cambiado satisfactoriamente", 1);
                handleCloseWindows(event);
            } catch (Exception e) {
                utilCalcSingelton.showAlert("Error al cambiar el recurso", 3);
            }


        }
*/
    }

    private void changeSumInAllObra(List<Bajoespecificacionrv> temp) {
        List<Bajoespecificacionrv> newResoucesList = new ArrayList<>();
        for (Bajoespecificacionrv bajoespecificacionrv : temp) {
            Bajoespecificacionrv b = createNewbajoGenral(bajoespecificacionrv);
            newResoucesList.add(b);
        }
        utilCalcSingelton.deleteBajoEspecificacionRenglonList(temp);
        utilCalcSingelton.persistBajoespecificacionRenglon(newResoucesList);
    }

    private Bajoespecificacionrv createNewbajo(Double cantDisponible) {
        Bajoespecificacionrv bajoespecificacion = new Bajoespecificacionrv();
        bajoespecificacion.setNivelespecificoId(idNEsp);
        bajoespecificacion.setRenglonvarianteId(idRenglon);
        bajoespecificacion.setIdsuministro(idnewRec);
        bajoespecificacion.setTipo(tipo);
        bajoespecificacion.setCantidad(cantDisponible);
        bajoespecificacion.setCosto(cantDisponible * precio);
        return bajoespecificacion;
    }

    private Bajoespecificacionrv createNewbajoGenral(Bajoespecificacionrv bajoespecificacionrv) {
        Bajoespecificacionrv bajoespecificacion = new Bajoespecificacionrv();
        bajoespecificacion.setNivelespecificoId(bajoespecificacionrv.getNivelespecificoId());
        bajoespecificacion.setRenglonvarianteId(bajoespecificacionrv.getRenglonvarianteId());
        bajoespecificacion.setIdsuministro(idnewRec);
        bajoespecificacion.setTipo(tipo);
        bajoespecificacion.setCantidad(bajoespecificacionrv.getCantidad());
        bajoespecificacion.setCosto(bajoespecificacionrv.getCantidad() * precio);
        return bajoespecificacion;
    }


    private Integer savePlaRec(Bajoespecificacionrv bajoespecificacionrv) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idPlan = null;
        try {
            tx = session.beginTransaction();

            idPlan = (Integer) session.save(bajoespecificacionrv);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return idPlan;
    }

    private void deletePlaRec(Bajoespecificacionrv bajoespecificacionrv) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(bajoespecificacionrv);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void searchView(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searhSuministroToChange.fxml"));
            Parent proot = loader.load();
            SearchSuminitrosToChangeController controller = loader.getController();
            // controller.pasController(cambioSuministroCertificacionController);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initStyle(StageStyle.UTILITY);
            stage.toFront();
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void getSuminitroResult(SuministrosView suministrosView) {
        idnewRec = suministrosView.getId();
        precio = suministrosView.getPreciomn();
        fieldPrecio.setText(String.valueOf(suministrosView.getPreciomn()));
        labelValue.setText(suministrosView.getUm());
        textDedscripcion.setText(suministrosView.getDescripcion());
        field_codigo.setText(suministrosView.getCodigo());
        tipo = suministrosView.getTipo();
    }

    public void handleBajoAddSum(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nuevoSuminitroFromBajoToChange.fxml"));
            Parent proot = loader.load();
            NuevoSuministroFromBajoToChangeController controller = loader.getController();
            //  controller.pasarParametros(cambioSuministroCertificacionController, field_codigo.getText());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initStyle(StageStyle.UTILITY);
            stage.showAndWait();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
