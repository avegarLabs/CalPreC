package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BajoEspecificacionRenglonCambiarController implements Initializable {


    RenglonVarianteObraController renglonVarianteObraController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField fielNewSum;
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
    private List<Recursos> recursosArrayList;
    private List<Suministrossemielaborados> suministrossemielaboradosArrayList;
    private List<Juegoproducto> juegoproductoArrayList;
    private ObservableList<String> listSuministros;

    private Integer idObra;
    private Integer idRenglonV;
    private Integer idobraenunidad;
    private Nivelespecifico unidadobra;

    private Integer idSuministro;
    private Integer idSuministroCambiado;

    private String tipo;
    private String suminiCode;

    private double cantidad;
    private double costo;
    private double importe;

    private Bajoespecificacion bajoespecificacion;
    private DecimalFormat df;

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
        String[] code = fielNewSum.getText().split(" -");
        suminiCode = String.format(code[0]);

        fielNewSum.clear();
        fielNewSum.setText(suminiCode);

        Recursos recursos = recursosArrayList.stream().filter(r -> r.getCodigo().equals(suminiCode)).findFirst().orElse(null);
        if (recursos != null) {
            idSuministro = recursos.getId();
            textDedscripcion.setText(recursos.getDescripcion());
            labelValue.setText(String.valueOf(recursos.getPreciomn()));
            idSuministro = recursos.getId();
            tipo = "1";
        }

        Juegoproducto juegoproducto = juegoproductoArrayList.stream().filter(jp -> jp.getCodigo().equals(suminiCode)).findFirst().orElse(null);
        if (juegoproducto != null) {
            idSuministro = juegoproducto.getId();
            textDedscripcion.setText(juegoproducto.getDescripcion());
            labelValue.setText(String.valueOf(juegoproducto.getPreciomn()));
            idSuministro = juegoproducto.getId();
            tipo = "J";

        }

        Suministrossemielaborados suministrossemielaborados = suministrossemielaboradosArrayList.stream().filter(sm -> sm.getCodigo().equals(suminiCode)).findFirst().orElse(null);
        if (suministrossemielaborados != null) {
            idSuministro = suministrossemielaborados.getId();
            textDedscripcion.setText(suministrossemielaborados.getDescripcion());
            labelValue.setText(String.valueOf(suministrossemielaborados.getPreciomn()));
            idSuministro = suministrossemielaborados.getId();
            tipo = "S";

        }

        fieldCant.requestFocus();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        df = new DecimalFormat("#.####");


    }

    /**
     * @param uniddaObraView
     */

    public void pasarUnidaddeObra(RenglonVarianteObraController controller, UniddaObraView uniddaObraView, UORVTableView uorvTableView, BajoEspecificacionView bajoEspecificacionView) {

        renglonVarianteObraController = controller;
        unidadobra = getUnidadobra(uniddaObraView.getId());
        idobraenunidad = uniddaObraView.getId();
        idRenglonV = uorvTableView.getId();

        labelUoCode.setText("UO: " + uniddaObraView.getCodigo());
        idObra = unidadobra.getObraId();

        listToSugestionSuministros(unidadobra);
        TextFields.bindAutoCompletion(fielNewSum, listSuministros).setPrefWidth(500);

        field_codigo.setText(bajoEspecificacionView.getCodigo());
        fieldCant.setText(String.valueOf(bajoEspecificacionView.getCantidadBe()));
        labelValue.setText(String.valueOf(bajoEspecificacionView.getPrecio()));
        idSuministroCambiado = bajoEspecificacionView.getId();


    }

    public void handleBajoEspecificacion(ActionEvent event) {
        cantidad = Double.parseDouble(fieldCant.getText());
        importe = Double.parseDouble(labelValue.getText());
        costo = cantidad * importe;

        bajoespecificacion = new Bajoespecificacion();
        bajoespecificacion.setUnidadobraId(idobraenunidad);
        bajoespecificacion.setRenglonvarianteId(idRenglonV);
        bajoespecificacion.setIdSuministro(idSuministro);
        bajoespecificacion.setSumrenglon(idSuministroCambiado);
        bajoespecificacion.setTipo(tipo);
        bajoespecificacion.setCantidad(cantidad);
        bajoespecificacion.setCosto(costo);

        Integer id = addBajoEspecificacion(bajoespecificacion);

        if (id != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Datos insertados satisfactoriamente!");
            alert.showAndWait();


        }


        field_codigo.clear();
        fieldCant.clear();
        labelValue.setText(" ");

        textDedscripcion.clear();


    }
}
