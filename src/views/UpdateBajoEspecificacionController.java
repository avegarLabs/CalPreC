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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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

public class UpdateBajoEspecificacionController implements Initializable {

    private static SessionFactory sf;
    public Integer idObra;
    /**
     * @param uniddaObraView
     */
    public Obra obraToCheck;
    UpdateBajoEspecificacionController updateBajoEspecificacionController;
    UnidadesObraObraController unidadesObraObraController;
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
    private ArrayList<Recursos> recursosArrayList;
    private ArrayList<Suministrossemielaborados> suministrossemielaboradosArrayList;
    private ArrayList<Juegoproducto> juegoproductoArrayList;
    private ObservableList<String> listSuministros;
    private Integer idobraenunidad;
    private Unidadobra unidadobra;
    private Integer idSuministro;
    private Integer idBajo;
    private String tipo;
    private String suminiCode;
    private Double cantidad;
    private Double costo;
    private Double importe;
    private String um;
    private double precio;
    @FXML
    private JFXButton btn_exit;
    private Bajoespecificacion bajoespecificacion;
    private BajoEspecificacionView bajoEspecificacionView;
    private Recursos rec;
    private Juegoproducto juego;
    private Suministrossemielaborados suministrossemie;
    private DecimalFormat df;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

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

    public BajoEspecificacionView getBajoespecificacion(Integer id) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            bajoespecificacion = session.get(Bajoespecificacion.class, id);
            if (bajoespecificacion.getTipo().contentEquals("1")) {
                Recursos recursos = session.get(Recursos.class, bajoespecificacion.getIdSuministro());
                bajoEspecificacionView = new BajoEspecificacionView(bajoespecificacion.getId(), bajoespecificacion.getUnidadobraId(), bajoespecificacion.getIdSuministro(), 0, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), bajoespecificacion.getCantidad(), Math.round(bajoespecificacion.getCosto() * 100d) / 100d, bajoespecificacion.getTipo());
            }

            if (bajoespecificacion.getTipo().contentEquals("J")) {
                Juegoproducto recursos = session.get(Juegoproducto.class, bajoespecificacion.getIdSuministro());
                bajoEspecificacionView = new BajoEspecificacionView(bajoespecificacion.getId(), bajoespecificacion.getUnidadobraId(), bajoespecificacion.getIdSuministro(), 0, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), bajoespecificacion.getCantidad(), Math.round(bajoespecificacion.getCosto() * 100d) / 100d, bajoespecificacion.getTipo());
            }

            if (bajoespecificacion.getTipo().contentEquals("S")) {
                Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, bajoespecificacion.getIdSuministro());
                bajoEspecificacionView = new BajoEspecificacionView(bajoespecificacion.getId(), bajoespecificacion.getUnidadobraId(), bajoespecificacion.getIdSuministro(), 0, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), bajoespecificacion.getCantidad(), Math.round(bajoespecificacion.getCosto() * 100d) / 100d, bajoespecificacion.getTipo());
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return bajoEspecificacionView;
    }

    public void updateBajoEspecificacion(Bajoespecificacion bajoespecifi, int idBajoEspe) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Bajoespecificacion bajoespecificacionToUpdate = session.get(Bajoespecificacion.class, idBajoEspe);
            bajoespecificacionToUpdate.setIdSuministro(bajoespecifi.getIdSuministro());
            bajoespecificacionToUpdate.setCosto(bajoespecifi.getCosto());
            bajoespecificacionToUpdate.setCantidad(bajoespecifi.getCantidad());
            bajoespecificacionToUpdate.setTipo(bajoespecifi.getTipo());
            session.update(bajoespecificacionToUpdate);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void handleLimpiaField(ActionEvent event) {
        rec = null;
        juego = null;
        suministrossemie = null;

        rec = recursosArrayList.stream().filter(r -> r.toString().trim().equals(field_codigo.getText().trim())).findFirst().orElse(null);
        juego = juegoproductoArrayList.stream().filter(j -> j.toString().trim().equals(field_codigo.getText().trim())).findFirst().orElse(null);
        suministrossemie = suministrossemielaboradosArrayList.stream().filter(s -> s.toString().trim().equals(field_codigo.getText().trim())).findFirst().orElse(null);

        if (rec != null && juego == null && suministrossemie == null) {
            field_codigo.setText(rec.getCodigo());
            textDedscripcion.setText(rec.getDescripcion());
            labelValue.setText(rec.getPreciomn() + " / " + rec.getUm());
        } else if (rec == null && juego != null && suministrossemie == null) {
            field_codigo.setText(juego.getCodigo());
            textDedscripcion.setText(juego.getDescripcion());
            labelValue.setText(juego.getPreciomn() + " / " + juego.getUm());
        } else if (rec == null && juego == null && suministrossemie != null) {
            field_codigo.setText(suministrossemie.getCodigo());
            textDedscripcion.setText(suministrossemie.getDescripcion());
            labelValue.setText(suministrossemie.getPreciomn() + " / " + suministrossemie.getUm());
        }
        fieldCant.requestFocus();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateBajoEspecificacionController = this;
        fieldCant.requestFocus();
        df = new DecimalFormat("#.####");

    }

    public ObservableList<String> listToSugestionSuministros(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        listSuministros = FXCollections.observableArrayList();
        recursosArrayList = new ArrayList<>();
        juegoproductoArrayList = new ArrayList<>();
        suministrossemielaboradosArrayList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (obraToCheck.getSalarioBySalarioId().getTag().trim().equals("R266")) {
                recursosArrayList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece not like 'T%' and pertenece != 'I' and active = 1").getResultList();
                listSuministros.addAll(recursosArrayList.parallelStream().distinct().filter(rec -> rec.getTipo().equals("1")).map(Recursos::toString).collect(Collectors.toList()));
                juegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto WHERE pertenece not like 'T%' and pertenece != 'I'").getResultList();
                listSuministros.addAll(juegoproductoArrayList.parallelStream().distinct().map(Juegoproducto::toString).collect(Collectors.toList()));
                suministrossemielaboradosArrayList = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados WHERE  pertenec not like 'T%' and pertenec != 'I'").getResultList();
                listSuministros.addAll(suministrossemielaboradosArrayList.parallelStream().distinct().map(Suministrossemielaborados::toString).collect(Collectors.toList()));
            } else if (obraToCheck.getSalarioBySalarioId().getTag().trim().startsWith("T") && !obraToCheck.getSalarioBySalarioId().getTag().equals("T15")) {
                recursosArrayList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece != 'R266' and pertenece != 'I' and active = 1").getResultList();
                listSuministros.addAll(recursosArrayList.parallelStream().distinct().filter(rec -> rec.getTipo().equals("1")).map(Recursos::toString).collect(Collectors.toList()));
                juegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto WHERE pertenece != 'R266' and pertenece != 'I'").getResultList();
                listSuministros.addAll(juegoproductoArrayList.parallelStream().distinct().map(Juegoproducto::toString).collect(Collectors.toList()));
                suministrossemielaboradosArrayList = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados WHERE pertenec != 'I' and pertenec != 'R266'").getResultList();
                listSuministros.addAll(suministrossemielaboradosArrayList.parallelStream().distinct().map(Suministrossemielaborados::toString).collect(Collectors.toList()));
            } else if (obraToCheck.getSalarioBySalarioId().getTag().equals("T15")) {
                recursosArrayList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece != 'I' and active = 1").getResultList();
                listSuministros.addAll(recursosArrayList.parallelStream().distinct().filter(rec -> rec.getTipo().equals("1")).map(Recursos::toString).collect(Collectors.toList()));
                juegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto WHERE  pertenece != 'I'").getResultList();
                listSuministros.addAll(juegoproductoArrayList.parallelStream().distinct().map(Juegoproducto::toString).collect(Collectors.toList()));
                suministrossemielaboradosArrayList = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados WHERE pertenec != 'I' ").getResultList();
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

    public void pasarUnidaddeObra(UnidadesObraObraController unidadesObra, UniddaObraView uniddaObraView, int idBajoE) {

        unidadobra = getUnidadobra(uniddaObraView.getId());
        idobraenunidad = unidadobra.getId();

        labelUoCode.setText("UO: " + uniddaObraView.getCodigo());
        idObra = unidadobra.getObraId();
        obraToCheck = unidadobra.getObraByObraId();
        TextFields.bindAutoCompletion(field_codigo, listToSugestionSuministros(unidadobra)).setPrefWidth(500);

        bajoEspecificacionView = getBajoespecificacion(idBajoE);
        idBajo = bajoEspecificacionView.getId();
        um = bajoEspecificacionView.getUm();
        field_codigo.setText(bajoEspecificacionView.getCodigo());
        fieldCant.setText(String.valueOf(bajoEspecificacionView.getCantidadBe()));
        labelValue.setText(bajoEspecificacionView.getPrecio() + " / " + um);
        precio = bajoEspecificacionView.getPrecio();
        textDedscripcion.setText(bajoEspecificacionView.getDescripcion());

        unidadesObraObraController = unidadesObra;
    }

    public Bajoespecificacion detectedBEElemente(String code, String desc, double prec) {
        bajoespecificacion = new Bajoespecificacion();
        var recTemp = recursosArrayList.stream().filter(r -> r.getCodigo().equals(code.trim()) && r.getDescripcion().trim().equals(desc.trim()) && r.getPreciomn() == prec).findFirst().orElse(null);
        if (recTemp != null) {
            bajoespecificacion.setUnidadobraId(unidadobra.getId());
            bajoespecificacion.setIdSuministro(recTemp.getId());
            bajoespecificacion.setTipo("1");
            bajoespecificacion.setCantidad(Double.parseDouble(fieldCant.getText()));
            bajoespecificacion.setCosto(Double.parseDouble(fieldCant.getText()) * recTemp.getPreciomn());
            bajoespecificacion.setSumrenglon(0);
        }
        var juegoTemp = juegoproductoArrayList.stream().filter(j -> j.getCodigo().equals(code.trim()) && j.getDescripcion().trim().equals(desc.trim()) && j.getPreciomn() == prec).findFirst().orElse(null);
        if (juegoTemp != null) {
            bajoespecificacion.setUnidadobraId(unidadobra.getId());
            bajoespecificacion.setIdSuministro(juegoTemp.getId());
            bajoespecificacion.setTipo("J");
            bajoespecificacion.setCantidad(Double.parseDouble(fieldCant.getText()));
            bajoespecificacion.setCosto(Double.parseDouble(fieldCant.getText()) * juegoTemp.getPreciomn());
            bajoespecificacion.setSumrenglon(0);
        }
        var suministrossemieTemp = suministrossemielaboradosArrayList.stream().filter(s -> s.getCodigo().equals(code.trim()) && s.getDescripcion().trim().equals(desc.trim()) && s.getPreciomn() == prec).findFirst().orElse(null);
        if (suministrossemieTemp != null) {
            bajoespecificacion.setUnidadobraId(unidadobra.getId());
            bajoespecificacion.setIdSuministro(suministrossemieTemp.getId());
            bajoespecificacion.setTipo("S");
            bajoespecificacion.setCantidad(Double.parseDouble(fieldCant.getText()));
            bajoespecificacion.setCosto(Double.parseDouble(fieldCant.getText()) * suministrossemieTemp.getPreciomn());
            bajoespecificacion.setSumrenglon(0);
        }

        return bajoespecificacion;
    }

    public void handleUpdateBajoEspecificacion(ActionEvent event) {

        String code = field_codigo.getText();
        Double cant = Double.parseDouble(fieldCant.getText());
        String[] partPrecio = labelValue.getText().trim().split(" / ");
        double prec = Double.parseDouble(partPrecio[0]);
        String desc = textDedscripcion.getText();


        if (cant == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("De indicar la cantidad del material");
            alert.showAndWait();

        } else {
            try {
                updateBajoEspecificacion(detectedBEElemente(code, desc, prec), idBajo);
            } catch (Exception e) {
                utilCalcSingelton.showAlert("Error: \n" + e.getMessage(), 2);
            }

            unidadesObraObraController.addElementTableSuministros(unidadobra.getId());
            unidadesObraObraController.calcularElementosUO();

            Stage stage = (Stage) btn_exit.getScene().getWindow();
            stage.close();
        }


    }


    public void searchView(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searhSuministroFormalUpdate.fxml"));
            Parent proot = loader.load();
            SearchSuminitrosFormalUpdateController controller = loader.getController();
            controller.pasController(updateBajoEspecificacionController);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.toFront();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nuevoSuminitroFromBajoFormalUpdate.fxml"));
            Parent proot = loader.load();
            NuevoSuministroFromBajoFormalUpdateController controller = loader.getController();
            controller.pasarParametros(updateBajoEspecificacionController, field_codigo.getText(), textDedscripcion.getText(), um, String.valueOf(precio));


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleCloseRVtoUO(ActionEvent event) {

        unidadesObraObraController.calcularElementosUO();
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        stage.close();
    }


}
