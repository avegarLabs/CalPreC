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

public class UpdateBajoEspecificacionRenglonController implements Initializable {

    private static SessionFactory sf;
    public Integer idObra;
    /**
     * @param uniddaObraView
     */
    public Obra obraToCheck;
    UpdateBajoEspecificacionRenglonController updateBajoEspecificacionController;
    RenglonVarianteObraController renglonVarianteObraController;
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
    private Nivelespecifico unidadobra;
    private Integer idSuministro;
    private Integer idBajo;
    private String tipo;
    private String suminiCode;
    private Double cantidad;
    private double costo;
    private double importe;
    private String um;
    private double precio;
    private Bajoespecificacionrv bajoespecificacion;
    private BajoEspecificacionView bajoEspecificacionView;
    @FXML
    private JFXButton btn_close;
    private DecimalFormat df;
    private Bajoespecificacionrv bajoespecificacionRV;
    private Recursos rec;
    private Juegoproducto juego;
    private Suministrossemielaborados suministrossemie;
    private int idReng;

    public Nivelespecifico getUnidadobra(Integer id) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadobra = session.get(Nivelespecifico.class, id);


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
            bajoespecificacion = session.get(Bajoespecificacionrv.class, id);
            if (bajoespecificacion.getTipo().contentEquals("1")) {
                Recursos recursos = session.get(Recursos.class, bajoespecificacion.getIdsuministro());
                bajoEspecificacionView = new BajoEspecificacionView(bajoespecificacion.getId(), bajoespecificacion.getNivelespecificoId(), bajoespecificacion.getIdsuministro(), 0, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), bajoespecificacion.getCantidad(), Math.round(bajoespecificacion.getCosto() * 100d) / 100d, bajoespecificacion.getTipo());

            }

            if (bajoespecificacion.getTipo().contentEquals("J")) {
                Juegoproducto recursos = session.get(Juegoproducto.class, bajoespecificacion.getIdsuministro());
                bajoEspecificacionView = new BajoEspecificacionView(bajoespecificacion.getId(), bajoespecificacion.getNivelespecificoId(), bajoespecificacion.getIdsuministro(), 0, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), bajoespecificacion.getCantidad(), Math.round(bajoespecificacion.getCosto() * 100d) / 100d, bajoespecificacion.getTipo());
            }

            if (bajoespecificacion.getTipo().contentEquals("S")) {
                Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, bajoespecificacion.getIdsuministro());
                bajoEspecificacionView = new BajoEspecificacionView(bajoespecificacion.getId(), bajoespecificacion.getNivelespecificoId(), bajoespecificacion.getIdsuministro(), 0, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), bajoespecificacion.getCantidad(), Math.round(bajoespecificacion.getCosto() * 100d) / 100d, bajoespecificacion.getTipo());
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

    public void updateBajoEspecificacion(Bajoespecificacionrv bajoespecifi, int idBajoEspe) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Bajoespecificacionrv bajoesp = session.get(Bajoespecificacionrv.class, idBajoEspe);
            bajoesp.setIdsuministro(bajoespecifi.getIdsuministro());
            bajoesp.setCosto(bajoespecifi.getCosto());
            bajoesp.setCantidad(bajoespecifi.getCantidad());
            bajoesp.setTipo(bajoespecifi.getTipo());
            session.update(bajoesp);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        df = new DecimalFormat("#.####");
        updateBajoEspecificacionController = this;
    }

    public ObservableList<String> listToSugestionSuministros(Nivelespecifico unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        listSuministros = FXCollections.observableArrayList();
        recursosArrayList = new ArrayList<>();
        juegoproductoArrayList = new ArrayList<>();
        suministrossemielaboradosArrayList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (unidadobra.getObraByObraId().getSalarioBySalarioId().getTag().trim().equals("R266")) {
                recursosArrayList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece not like 'T%' and pertenece != 'I' and active = 1").getResultList();
                listSuministros.addAll(recursosArrayList.parallelStream().distinct().filter(rec -> rec.getTipo().equals("1")).map(Recursos::toString).collect(Collectors.toList()));
                juegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto WHERE pertenece not like 'T%' and pertenece != 'I'").getResultList();
                listSuministros.addAll(juegoproductoArrayList.parallelStream().distinct().map(Juegoproducto::toString).collect(Collectors.toList()));
                suministrossemielaboradosArrayList = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados WHERE  pertenec not like 'T%' and pertenec != 'I'").getResultList();
                listSuministros.addAll(suministrossemielaboradosArrayList.parallelStream().distinct().map(Suministrossemielaborados::toString).collect(Collectors.toList()));
            } else if (unidadobra.getObraByObraId().getSalarioBySalarioId().getTag().trim().startsWith("T")) {
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

    public Bajoespecificacionrv getBajoespecificacionOK(Integer id) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Bajoespecificacionrv bajoOK = session.get(Bajoespecificacionrv.class, id);

            tx.commit();
            session.close();
            return bajoOK;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Bajoespecificacionrv();
    }

    public void pasarUnidaddeObra(RenglonVarianteObraController controller, UniddaObraView uniddaObraView, int idBajoE) {


        renglonVarianteObraController = controller;
        unidadobra = getUnidadobra(uniddaObraView.getId());
        idobraenunidad = uniddaObraView.getId();

        labelUoCode.setText("Nivel Esp: " + uniddaObraView.getCodigo());
        idObra = unidadobra.getObraId();
        obraToCheck = unidadobra.getObraByObraId();

        TextFields.bindAutoCompletion(field_codigo, listToSugestionSuministros(unidadobra)).setPrefWidth(500);

        bajoEspecificacionView = getBajoespecificacion(idBajoE);
        bajoespecificacionRV = getBajoespecificacionOK(idBajoE);
        idBajo = bajoEspecificacionView.getId();
        um = bajoEspecificacionView.getUm();
        field_codigo.setText(bajoEspecificacionView.getCodigo());
        fieldCant.setText(String.valueOf(bajoEspecificacionView.getCantidadBe()));
        labelValue.setText(bajoEspecificacionView.getPrecio() + " / " + um);
        precio = bajoEspecificacionView.getPrecio();
        textDedscripcion.setText(bajoEspecificacionView.getDescripcion());
        idSuministro = bajoEspecificacionView.getIdSum();
        tipo = bajoEspecificacionView.getTipo();
        idReng = bajoEspecificacionView.getIdRV();
    }

    public void handleUpdateBajoEspecificacion(ActionEvent event) {

        String code = field_codigo.getText();
        cantidad = Double.parseDouble(fieldCant.getText());
        String[] partPrecio = labelValue.getText().trim().split(" / ");
        double prec = Double.parseDouble(partPrecio[0]);
        rec = null;
        juego = null;
        suministrossemie = null;

        rec = recursosArrayList.stream().filter(r -> r.getCodigo().equals(code.trim()) && r.getDescripcion().trim().equals(textDedscripcion.getText().trim()) && r.getPreciomn() == prec).findFirst().orElse(null);
        juego = juegoproductoArrayList.stream().filter(j -> j.getCodigo().equals(code.trim()) && j.getDescripcion().trim().equals(textDedscripcion.getText().trim()) && j.getPreciomn() == prec).findFirst().orElse(null);
        suministrossemie = suministrossemielaboradosArrayList.stream().filter(s -> s.getCodigo().equals(code.trim()) && s.getDescripcion().trim().equals(textDedscripcion.getText().trim()) && s.getPreciomn() == prec).findFirst().orElse(null);

        if (cantidad == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("De indicar la cantidad del material");
            alert.showAndWait();

        } else {
//            System.out.println(rec.getCodigo() + " - " + rec.getCodigo() + rec.getDescripcion() + " : " + rec.getPreciomn());
            if (rec != null && juego == null && suministrossemie == null) {
                bajoespecificacion = new Bajoespecificacionrv();
                bajoespecificacion.setNivelespecificoId(idobraenunidad);
                bajoespecificacion.setIdsuministro(rec.getId());
                bajoespecificacion.setRenglonvarianteId(idReng);
                bajoespecificacion.setTipo(rec.getTipo());
                bajoespecificacion.setCantidad(cantidad);
                bajoespecificacion.setCosto(cantidad * rec.getPreciomn());
            } else if (rec == null && juego != null && suministrossemie == null) {
                bajoespecificacion = new Bajoespecificacionrv();
                bajoespecificacion.setNivelespecificoId(idobraenunidad);
                bajoespecificacion.setIdsuministro(juego.getId());
                bajoespecificacion.setRenglonvarianteId(idReng);
                bajoespecificacion.setTipo("J");
                bajoespecificacion.setCantidad(cantidad);
                bajoespecificacion.setCosto(cantidad * juego.getPreciomn());
            } else if (rec == null && juego == null && suministrossemie != null) {
                bajoespecificacion = new Bajoespecificacionrv();
                bajoespecificacion.setNivelespecificoId(idobraenunidad);
                bajoespecificacion.setIdsuministro(suministrossemie.getId());
                bajoespecificacion.setRenglonvarianteId(idReng);
                bajoespecificacion.setTipo("S");
                bajoespecificacion.setCantidad(cantidad);
                bajoespecificacion.setCosto(cantidad * suministrossemie.getPreciomn());
            }

            updateBajoEspecificacion(bajoespecificacion, idBajo);
            renglonVarianteObraController.addElementTableSuministros(bajoespecificacionRV.getNivelespecificoId(), bajoespecificacionRV.getRenglonvarianteId());
            renglonVarianteObraController.completeValuesNivelEspecifico(bajoespecificacionRV.getNivelespecificoId());

            clearFilds();

        }
    }

    public void clearFilds() {
        field_codigo.clear();
        fieldCant.clear();
        labelValue.setText(" ");

        textDedscripcion.clear();

    }


    public void searchView(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searhSuministroFormalUpdateRV.fxml"));
            Parent proot = loader.load();
            SearchSuminitrosFormalUpdateRVController controller = loader.getController();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nuevoSuminitroFromBajoFormalUpdateRV.fxml"));
            Parent proot = loader.load();
            NuevoSuministroFromBajoFormalUpdateRVController controller = loader.getController();
            controller.pasarParametros(updateBajoEspecificacionController, field_codigo.getText(), textDedscripcion.getText(), um, String.valueOf(precio));


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
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
