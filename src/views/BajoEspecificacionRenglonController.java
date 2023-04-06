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
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BajoEspecificacionRenglonController implements Initializable {

    public Integer idObra;
    /**
     * @param uniddaObraView
     */
    public Obra obraToChek;
    BajoEspecificacionRenglonController bajoEspecificacionRenglonController;
    RenglonVarianteObraController renglonVarianteObraController;
    ObservableList<UORVTableView> rctoaddList;
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
    private List<Recursos> recursosArrayList;
    private List<Suministrossemielaborados> suministrossemielaboradosArrayList;
    private List<Juegoproducto> juegoproductoArrayList;
    private ObservableList<String> listSuministros;
    private Integer idRenglonV;
    private Integer idobraenunidad;
    private Nivelespecifico nivelespecifico;
    private Integer idSuministro;
    private String tipo;
    private String suminiCode;
    private double cantidad;
    private double costo;
    private double importe;
    private String um;
    private double precio;
    private Bajoespecificacionrv bajoespecificacion;
    @FXML
    private JFXButton btn_close;
    private DecimalFormat df;
    private UORVTableView rcToSave;

    public Nivelespecifico getNivelEspecifico(Integer id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            nivelespecifico = session.get(Nivelespecifico.class, id);


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return nivelespecifico;
    }

    private void sobreEscribirSuministroUnidad(Bajoespecificacionrv bajoespecificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacionrv WHERE nivelespecificoId =: idUni AND idsuministro =: idSum AND renglonvarianteId =: idRen");
            query.setParameter("idUni", bajoespecificacion.getNivelespecificoId());
            query.setParameter("idSum", bajoespecificacion.getIdsuministro());
            query.setParameter("idRen", bajoespecificacion.getRenglonvarianteId());

            Bajoespecificacionrv bajo = (Bajoespecificacionrv) query.getSingleResult();
            if (bajo != null) {
                bajo.setCantidad(bajoespecificacion.getCantidad());
                bajo.setCosto(bajoespecificacion.getCosto());
                bajo.setTipo(bajoespecificacion.getTipo());
                session.update(bajo);
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private void sumarCantidadesSuministro(Bajoespecificacionrv bajoespecificacion) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacionrv WHERE nivelespecificoId =: idUni AND idsuministro =: idSum AND renglonvarianteId =: idReng");
            query.setParameter("idUni", bajoespecificacion.getNivelespecificoId());
            query.setParameter("idSum", bajoespecificacion.getIdsuministro());
            query.setParameter("idReng", bajoespecificacion.getRenglonvarianteId());

            Recursos recursos = session.get(Recursos.class, bajoespecificacion.getIdsuministro());

            Bajoespecificacionrv bajo = (Bajoespecificacionrv) query.getSingleResult();
            if (bajo != null) {
                double cantidad = bajo.getCantidad() + bajoespecificacion.getCantidad();
                bajo.setCantidad(cantidad);
                bajo.setCosto(Math.round(recursos.getPreciomn() * 100d) / 100d * cantidad);
                bajo.setTipo(bajoespecificacion.getTipo());
                session.update(bajo);
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void handleLimpiaField(ActionEvent event) {
        Recursos recursos = recursosArrayList.stream().filter(r -> r.toString().equals(field_codigo.getText().trim())).findFirst().orElse(null);
        if (recursos != null) {
            field_codigo.setText(recursos.getCodigo());
            idSuministro = recursos.getId();
            textDedscripcion.setText(recursos.getDescripcion());
            um = recursos.getUm();
            precio = Math.round(recursos.getPreciomn() * 100d) / 100d;
            labelValue.setText(recursos.getPreciomn() + " / " + um);
            idSuministro = recursos.getId();
            tipo = "1";
        }

        Juegoproducto juegoproducto = juegoproductoArrayList.stream().filter(jp -> jp.toString().equals(field_codigo.getText())).findFirst().orElse(null);
        if (juegoproducto != null) {
            field_codigo.setText(juegoproducto.getCodigo());
            idSuministro = juegoproducto.getId();
            textDedscripcion.setText(juegoproducto.getDescripcion());
            um = juegoproducto.getUm();
            precio = Math.round(juegoproducto.getPreciomn() * 100d) / 100d;
            labelValue.setText(juegoproducto.getPreciomn() + " / " + um);
            idSuministro = juegoproducto.getId();
            tipo = "J";
        }

        Suministrossemielaborados suministrossemielaborados = suministrossemielaboradosArrayList.stream().filter(sm -> sm.toString().equals(field_codigo.getText())).findFirst().orElse(null);
        if (suministrossemielaborados != null) {
            field_codigo.setText(suministrossemielaborados.getCodigo());
            idSuministro = suministrossemielaborados.getId();
            textDedscripcion.setText(suministrossemielaborados.getDescripcion());
            um = suministrossemielaborados.getUm();
            precio = Math.round(suministrossemielaborados.getPreciomn() * 100d) / 100d;
            labelValue.setText(suministrossemielaborados.getPreciomn() + " / " + um);
            idSuministro = suministrossemielaborados.getId();
            tipo = "S";
        }
        fieldCant.requestFocus();
    }

    public ObservableList<String> listToSugestionSuministros(Nivelespecifico unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        System.out.println(unidadobra.getObraByObraId().getSalarioBySalarioId().getTag());

        listSuministros = FXCollections.observableArrayList();
        recursosArrayList = new ArrayList<>();
        juegoproductoArrayList = new ArrayList<>();
        suministrossemielaboradosArrayList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (unidadobra.getObraByObraId().getSalarioBySalarioId().getTag().trim().equals("R266")) {
                System.out.println("V1");
                recursosArrayList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece not like 'T%' and pertenece != 'I' and active = 1 and tipo = '1'").getResultList();
                listSuministros.addAll(recursosArrayList.parallelStream().distinct().filter(rec -> rec.getTipo().equals("1")).map(Recursos::toString).collect(Collectors.toList()));
                juegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto WHERE pertenece not like 'T%' and pertenece != 'I'").getResultList();
                listSuministros.addAll(juegoproductoArrayList.parallelStream().distinct().map(Juegoproducto::toString).collect(Collectors.toList()));
                suministrossemielaboradosArrayList = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados WHERE  pertenec not like 'T%' and pertenec != 'I'").getResultList();
                listSuministros.addAll(suministrossemielaboradosArrayList.parallelStream().distinct().map(Suministrossemielaborados::toString).collect(Collectors.toList()));
            } else if (unidadobra.getObraByObraId().getSalarioBySalarioId().getTag().trim().startsWith("T")) {
                System.out.println("V2");
                recursosArrayList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece != 'R266' and pertenece != 'I' and active = 1 and tipo = '1'").getResultList();
                listSuministros.addAll(recursosArrayList.parallelStream().distinct().filter(rec -> rec.getTipo().equals("1")).map(Recursos::toString).collect(Collectors.toList()));
                juegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto WHERE pertenece != 'R266' and pertenece != 'I'").getResultList();
                listSuministros.addAll(juegoproductoArrayList.parallelStream().distinct().map(Juegoproducto::toString).collect(Collectors.toList()));
                suministrossemielaboradosArrayList = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados WHERE pertenec != 'I' and pertenec != 'R266'").getResultList();
                listSuministros.addAll(suministrossemielaboradosArrayList.parallelStream().distinct().map(Suministrossemielaborados::toString).collect(Collectors.toList()));
            }
            recursosArrayList.forEach(item -> {
                System.out.println(item.getId() + " * " + item.getCodigo() + " - " + item.getPertenece() + " *** " + item.getTipo());
            });
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

        bajoEspecificacionRenglonController = this;
        df = new DecimalFormat("#.####");
    }

    public Integer addBajoEspecificacion(Bajoespecificacionrv bajoespecificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idBe = null;
        Bajoespecificacionrv bajo = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(" FROM Bajoespecificacionrv WHERE nivelespecificoId =: unidad AND idsuministro =: suministro AND renglonvarianteId =: idR ").setParameter("unidad", bajoespecificacion.getNivelespecificoId()).setParameter("suministro", bajoespecificacion.getIdsuministro()).setParameter("idR", bajoespecificacion.getRenglonvarianteId());
            try {
                bajo = (Bajoespecificacionrv) query.getSingleResult();
            } catch (NoResultException nre) {
            }
            if (bajo != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(" El Suministro " + field_codigo.getText() + " es parte de la unidad de obra, escoja Sumar si desea incrementar los valores o Sobreescribir si desea desechar los valores anteriores  ");
                ButtonType buttonAgregar = new ButtonType("Sumar");
                ButtonType buttonSobre = new ButtonType("Sobreescribir");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(buttonAgregar, buttonSobre, buttonTypeCancel);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonAgregar) {
                    sumarCantidadesSuministro(bajoespecificacion);
                    renglonVarianteObraController.addElementTableSuministros(bajoespecificacion.getNivelespecificoId(), bajoespecificacion.getRenglonvarianteId());
                } else if (result.get() == buttonSobre) {
                    sobreEscribirSuministroUnidad(bajoespecificacion);
                    renglonVarianteObraController.addElementTableSuministros(bajoespecificacion.getNivelespecificoId(), bajoespecificacion.getRenglonvarianteId());
                }
            } else {
                idBe = (Integer) session.save(bajoespecificacion);
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return idBe;
    }

    private int getActualIndex() {
        int index = 0;
        for (int i = 0; i < rctoaddList.size(); i++) {
            if (rctoaddList.get(i).getCodeRV().trim().equals(labelUoCode.getText().trim())) {
                index = i;
            }
        }
        return index;
    }

    public void handleLeftArrow(ActionEvent event) {
        int index = getActualIndex();
        if (index == 0) {
            rcToSave = rctoaddList.get(index);
            labelUoCode.setText("  " + rcToSave.getCodeRV().trim());
        } else {
            rcToSave = rctoaddList.get(index - 1);
            labelUoCode.setText("  " + rcToSave.getCodeRV().trim());
        }
    }

    public void handleRightArrow(ActionEvent event) {
        int index = getActualIndex();
        if (index == rctoaddList.size()) {
            rcToSave = rctoaddList.get(index);
            labelUoCode.setText("  " + rcToSave.getCodeRV().trim());
        } else {
            rcToSave = rctoaddList.get(index + 1);
            labelUoCode.setText("  " + rcToSave.getCodeRV().trim());
        }
    }

    public void pasarUnidaddeObra(RenglonVarianteObraController controller, UniddaObraView uniddaObraView, UORVTableView uorvTableView) {
        renglonVarianteObraController = controller;
        nivelespecifico = getNivelEspecifico(uniddaObraView.getId());
        idobraenunidad = uniddaObraView.getId();
        idRenglonV = uorvTableView.getId();
        rctoaddList = FXCollections.observableArrayList();
        rctoaddList.addAll(controller.uorvTableViewsList.parallelStream().filter(item -> item.getConMat().trim().equals("0")).collect(Collectors.toList()));
        labelUoCode.setText("  " + uorvTableView.getCodeRV().trim());
        rcToSave = uorvTableView;
        idObra = nivelespecifico.getObraId();
        obraToChek = nivelespecifico.getObraByObraId();
        TextFields.bindAutoCompletion(field_codigo, listToSugestionSuministros(nivelespecifico)).setPrefWidth(500);
    }

    public void handleBajoEspecificacion(ActionEvent event) {
        cantidad = Double.parseDouble(fieldCant.getText());
        //importe = Double.parseDouble(labelValue.getText());
        costo = cantidad * precio;


        bajoespecificacion = new Bajoespecificacionrv();
        bajoespecificacion.setNivelespecificoId(idobraenunidad);
        bajoespecificacion.setRenglonvarianteId(rcToSave.getId());
        bajoespecificacion.setIdsuministro(idSuministro);
        bajoespecificacion.setTipo(tipo);
        bajoespecificacion.setCantidad(cantidad);
        bajoespecificacion.setCosto(costo);

        Integer id = addBajoEspecificacion(bajoespecificacion);
        renglonVarianteObraController.addElementTableSuministros(bajoespecificacion.getNivelespecificoId(), bajoespecificacion.getRenglonvarianteId());
        renglonVarianteObraController.completeValuesNivelEspecifico(bajoespecificacion.getNivelespecificoId());

        if (id != null) {
            clearField();
            field_codigo.requestFocus();
        }
    }

    public void clearField() {
        field_codigo.clear();
        fieldCant.clear();
        labelValue.setText(" ");

        textDedscripcion.clear();
    }


    public void searchView(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searhSuministroFormalRV.fxml"));
            Parent proot = loader.load();
            SearchSuminitrosFormalRVController controller = loader.getController();
            controller.pasController(bajoEspecificacionRenglonController);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
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
        //fieldCant.setText(String.valueOf(cantidadRV));
        tipo = suministrosView.getTipo();

    }

    public void handleBajoAddSum(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nuevoSuminitroFromBajoFormalRV.fxml"));
            Parent proot = loader.load();
            NuevoSuministroFromBajoFormalRVController controller = loader.getController();
            controller.pasarParametros(bajoEspecificacionRenglonController, field_codigo.getText(), textDedscripcion.getText(), um, String.valueOf(precio));


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
