package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BajoEspecificacionControllerComponetUpdateAdd implements Initializable {

    private static SessionFactory sf;
    public Integer idObra;
    public Obra obraToChek;
    SuministrosInRVUpdateController suministrosInRVUpdateController;
    BajoEspecificacionControllerComponetUpdateAdd bajoEspecificacionControllerComponetUpdate;
    ReportProjectStructureSingelton util = ReportProjectStructureSingelton.getInstance();
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXButton btn_add;
    @FXML
    private Label labelUoCode;
    @FXML
    private JFXTextField fieldCant;
    @FXML
    private TextArea textDedscripcion;
    @FXML
    private Label labelValue;
    @FXML
    private JFXButton btnSum;
    @FXML
    private VBox box;
    private ArrayList<Recursos> recursosArrayList;
    private ArrayList<Suministrossemielaborados> suministrossemielaboradosArrayList;
    private ArrayList<Juegoproducto> juegoproductoArrayList;
    private ObservableList<String> listSuministros;
    private Integer idRenglonV;
    private Integer idobraenunidad;
    private Unidadobra unidadobra;
    private Integer idSuministro;
    private String tipo;
    private String suminiCode;
    private String um;
    private double cantidad;
    private double costo;
    private double importe;
    private double precio;
    private SumRVView newSum;
    private Bajoespecificacion bajoespecificacion;
    @FXML
    private JFXButton btn_close;
    private DecimalFormat df;

    public Unidadobra getUnidadobra(Integer id) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadobra = session.get(Unidadobra.class, id);


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return unidadobra;
    }

    public Integer addBajoEspecificacion(Bajoespecificacion bajoespecificacion) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer idBe = null;


        try {
            tx = session.beginTransaction();
            idBe = (Integer) session.save(bajoespecificacion);


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return idBe;
    }

    public void handleLimpiaField(ActionEvent event) {
        String[] code = field_codigo.getText().split(" -");
        suminiCode = String.format(code[0]);


        field_codigo.clear();
        field_codigo.setText(suminiCode);
        recursosArrayList.forEach(recursos -> {
            if (recursos.getCodigo().trim().equals(suminiCode)) {
                idSuministro = recursos.getId();
                textDedscripcion.setText(recursos.getDescripcion());
                labelValue.setText(String.valueOf(recursos.getPreciomn()));
                idSuministro = recursos.getId();
                um = recursos.getUm();
                precio = recursos.getPreciomn();
                tipo = "1";
            }

        });

        // if (textDedscripcion.getText().isEmpty()) {
        juegoproductoArrayList.forEach(juegoproducto -> {
            if (juegoproducto.getCodigo().trim().equals(suminiCode)) {
                idSuministro = juegoproducto.getId();
                textDedscripcion.setText(juegoproducto.getDescripcion());
                labelValue.setText(String.valueOf(juegoproducto.getPreciomn()));
                idSuministro = juegoproducto.getId();
                um = juegoproducto.getUm();
                precio = juegoproducto.getPreciomn();
                tipo = "J";
            }
        });
        //  }

        // if (textDedscripcion.getText().isEmpty()) {
        suministrossemielaboradosArrayList.forEach(suministrossemielaborados -> {
            if (suministrossemielaborados.getCodigo().trim().equals(suminiCode)) {
                idSuministro = suministrossemielaborados.getId();
                textDedscripcion.setText(suministrossemielaborados.getDescripcion());
                labelValue.setText(String.valueOf(suministrossemielaborados.getPreciomn()));
                idSuministro = suministrossemielaborados.getId();
                um = suministrossemielaborados.getUm();
                precio = suministrossemielaborados.getPreciomn();
                tipo = "S";
            }
        });
        // }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bajoEspecificacionControllerComponetUpdate = this;
        df = new DecimalFormat("#.####");
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
                recursosArrayList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece not like 'T%' and pertenece != 'I' and active = 1").getResultList();
                listSuministros.addAll(recursosArrayList.parallelStream().distinct().filter(rec -> rec.getTipo().equals("1")).map(Recursos::toString).collect(Collectors.toList()));
                juegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto WHERE pertenece not like 'T%' and pertenece != 'I'").getResultList();
                listSuministros.addAll(juegoproductoArrayList.parallelStream().distinct().map(Juegoproducto::toString).collect(Collectors.toList()));
                suministrossemielaboradosArrayList = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados WHERE  pertenec not like 'T%' and pertenec != 'I'").getResultList();
                listSuministros.addAll(suministrossemielaboradosArrayList.parallelStream().distinct().map(Suministrossemielaborados::toString).collect(Collectors.toList()));
            } else if (obra.getSalarioBySalarioId().getTag().trim().startsWith("T")) {
                recursosArrayList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece != 'R266' and pertenece != 'I' and active = 1").getResultList();
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

    public void pasarDatos(SuministrosInRVUpdateController suministrosInRVController, int idOb) {

        suministrosInRVUpdateController = suministrosInRVController;
        labelUoCode.setText("Agregar suminitro");


        idObra = idOb;
        obraToChek = util.getObra(idOb);
        listToSugestionSuministros(obraToChek);
        TextFields.bindAutoCompletion(field_codigo, listSuministros).setPrefWidth(500);

    }


    public void handleBajoEspecificacion(ActionEvent event) {
        if (fieldCant.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Debe especificar la cantidad del Suministro");
            alert.showAndWait();
        } else {

            cantidad = Double.parseDouble(fieldCant.getText()) * 100d / 100d;
            //importe = Double.parseDouble(labelValue.getText()) * 100d / 100d;
            costo = precio;

            JFXCheckBox checkBox = new JFXCheckBox(field_codigo.getText());
            checkBox.setSelected(true);
            //juegoproducto.getUm(), Math.round(juegoproducto.getPreciomn()*100d)/100d, cantid*renglonjuego.getCantidad(), renglonjuego.getUsos()
            newSum = new SumRVView(idSuministro, checkBox, textDedscripcion.getText(), um, Math.round(costo * 100d) / 100d, Double.parseDouble(fieldCant.getText()), 1.0, tipo);


            suministrosInRVUpdateController.saveSumUpdate(newSum);

            Stage stage = (Stage) btn_add.getScene().getWindow();
            stage.close();
        }

    }


    public void searchView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searhSuministroUpdateAdd.fxml"));
            Parent proot = loader.load();
            SearchSuminitrosUpdateControllerAdd controller = loader.getController();
            controller.pasController(bajoEspecificacionControllerComponetUpdate);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void getSuminitroResult(SuministrosView suministrosView) {

        field_codigo.setText(suministrosView.getCodigo());
        idSuministro = suministrosView.getId();
        textDedscripcion.setText(suministrosView.getDescripcion());
        um = suministrosView.getUm();

        labelValue.setText(suministrosView.getPreciomn() + " / " + um);
        precio = suministrosView.getPreciomn();

        tipo = suministrosView.getTipo();

    }


    public void handleBajoAddSum(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nuevoSuminitroFromBajoEspUPDATEAdd.fxml"));
            Parent proot = loader.load();
            NuevoSuministroFromBajoUpdateControllerAdd controller = loader.getController();
            controller.pasarParametros(bajoEspecificacionControllerComponetUpdate, field_codigo.getText(), textDedscripcion.getText(), um, String.valueOf(precio));


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void handleCloseWindows(ActionEvent event) {
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }


}
