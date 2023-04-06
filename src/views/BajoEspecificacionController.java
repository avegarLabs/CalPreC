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
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BajoEspecificacionController implements Initializable {
    private static SessionFactory sf;
    public Integer idObra;
    /**
     * @param uniddaObraView
     */
    public Obra obraToCheck;
    BajoEspecificacionController bajoEspecificacionController;
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
    private Integer idRenglonV;
    private Integer idobraenunidad;
    private Unidadobra unidadobra;
    private Integer idSuministro;
    private String tipo;
    private String suminiCode;
    private Double cantidad;
    private Double costo;
    private Double importe;
    private String um;
    private double precio;
    private Bajoespecificacion bajoespecificacion;
    private Runtime garbache;
    @FXML
    private JFXButton btn_exit;
    private Recursos rec;
    private Juegoproducto juego;
    private Suministrossemielaborados suministrossemie;
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
        Bajoespecificacion bajo = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(" FROM Bajoespecificacion WHERE unidadobraId =: unidad AND idSuministro =: suministro").setParameter("unidad", bajoespecificacion.getUnidadobraId()).setParameter("suministro", bajoespecificacion.getIdSuministro());
            try {
                bajo = (Bajoespecificacion) query.getSingleResult();
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
                        unidadesObraObraController.addElementTableSuministros(bajoespecificacion.getUnidadobraId());
                    } else if (result.get() == buttonSobre) {
                        sobreEscribirSuministroUnidad(bajoespecificacion);
                        unidadesObraObraController.addElementTableSuministros(bajoespecificacion.getUnidadobraId());
                    }
                } else {
                    idBe = (Integer) session.save(bajoespecificacion);
                }
            } catch (NoResultException e) {
                idBe = (Integer) session.save(bajoespecificacion);
            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return idBe;
    }

    private void sobreEscribirSuministroUnidad(Bajoespecificacion bajoespecificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idUni AND idSuministro =: idRv").setParameter("idUni", bajoespecificacion.getUnidadobraId()).setParameter("idRv", bajoespecificacion.getIdSuministro());

            Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
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

    private void sumarCantidadesSuministro(Bajoespecificacion bajoespecificacion) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idUni AND idSuministro =: idRv").setParameter("idUni", bajoespecificacion.getUnidadobraId()).setParameter("idRv", bajoespecificacion.getIdSuministro());

            Recursos recursos = session.get(Recursos.class, bajoespecificacion.getIdSuministro());

            Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
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

    public ObservableList<String> listToSugestionSuministros(Unidadobra unidadobra) {
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
            } else if (unidadobra.getObraByObraId().getSalarioBySalarioId().getTag().trim().startsWith("T") && !obraToCheck.getSalarioBySalarioId().getTag().equals("T15")) {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bajoEspecificacionController = this;
        fieldCant.requestFocus();
        df = new DecimalFormat("#.####");

    }

    public void pasarUnidaddeObra(UnidadesObraObraController unidadesObra, UniddaObraView uniddaObraView) {
        System.out.println("ytytytytyy");
        unidadobra = getUnidadobra(uniddaObraView.getId());
        idobraenunidad = uniddaObraView.getId();

        labelUoCode.setText("UO: " + uniddaObraView.getCodigo());
        idObra = unidadobra.getObraId();
        obraToCheck = unidadobra.getObraByObraId();

        TextFields.bindAutoCompletion(field_codigo, listToSugestionSuministros(unidadobra)).setPrefWidth(500);
        unidadesObraObraController = unidadesObra;

    }

    public void handleBajoEspecificacion(ActionEvent event) {
        cantidad = Double.parseDouble(fieldCant.getText());

        if (cantidad == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("De indicar la cantidad del material");
            alert.showAndWait();

        } else {

            if (rec != null && juego == null && suministrossemie == null) {
                bajoespecificacion = new Bajoespecificacion();
                bajoespecificacion.setUnidadobraId(idobraenunidad);
                bajoespecificacion.setIdSuministro(rec.getId());
                bajoespecificacion.setTipo(rec.getTipo());
                bajoespecificacion.setCantidad(cantidad);
                bajoespecificacion.setCosto(cantidad * rec.getPreciomn());
                bajoespecificacion.setSumrenglon(0);
            } else if (rec == null && juego != null && suministrossemie == null) {
                bajoespecificacion = new Bajoespecificacion();
                bajoespecificacion.setUnidadobraId(idobraenunidad);
                bajoespecificacion.setIdSuministro(juego.getId());
                bajoespecificacion.setTipo("J");
                bajoespecificacion.setCantidad(cantidad);
                bajoespecificacion.setCosto(cantidad * juego.getPreciomn());
                bajoespecificacion.setSumrenglon(0);
            } else if (rec == null && juego == null && suministrossemie != null) {
                bajoespecificacion = new Bajoespecificacion();
                bajoespecificacion.setUnidadobraId(idobraenunidad);
                bajoespecificacion.setIdSuministro(suministrossemie.getId());
                bajoespecificacion.setTipo("S");
                bajoespecificacion.setCantidad(cantidad);
                bajoespecificacion.setCosto(cantidad * suministrossemie.getPreciomn());
                bajoespecificacion.setSumrenglon(0);
            }
            Integer id = addBajoEspecificacion(bajoespecificacion);

            unidadesObraObraController.addElementTableSuministros(idobraenunidad);
            unidadesObraObraController.calcularElementosUO();

            field_codigo.clear();
            fieldCant.clear();
            labelValue.setText(" ");

            textDedscripcion.clear();
            garbache = Runtime.getRuntime();
            garbache.gc();
            field_codigo.requestFocus();


        }

    }

    public void searchView(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searhSuministroFormal.fxml"));
            Parent proot = loader.load();
            SearchSuminitrosFormalController controller = loader.getController();
            controller.pasController(bajoEspecificacionController);


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
        ObservableList<String> datos = FXCollections.observableArrayList();
        datos = listToSugestionSuministros(unidadobra);
        if (suministrosView.getTipo().contentEquals("1")) {
            rec = null;
            rec = recursosArrayList.parallelStream().filter(recursos -> recursos.getCodigo().trim().equals(suministrosView.getCodigo().trim())).findFirst().orElse(null);
            field_codigo.setText(rec.getCodigo());
            textDedscripcion.setText(rec.getDescripcion());
            labelValue.setText(rec.getPreciomn() + " / " + rec.getUm());
        } else if (suministrosView.getTipo().equals("J")) {
            juego = null;
            juego = juegoproductoArrayList.parallelStream().filter(juegoproducto -> juegoproducto.getCodigo().trim().equals(suministrosView.getCodigo())).findFirst().orElse(null);
            field_codigo.setText(juego.getCodigo());
            textDedscripcion.setText(juego.getDescripcion());
            labelValue.setText(juego.getPreciomn() + " / " + juego.getUm());
        } else if (suministrosView.getTipo().contentEquals("S")) {
            suministrossemie = null;
            suministrossemie = suministrossemielaboradosArrayList.parallelStream().filter(suministrossemielaborados -> suministrossemielaborados.getCodigo().trim().equals(suministrosView.getCodigo().trim())).findFirst().orElse(null);
            field_codigo.setText(suministrossemie.getCodigo());
            textDedscripcion.setText(suministrossemie.getDescripcion());
            labelValue.setText(suministrossemie.getPreciomn() + " / " + suministrossemie.getUm());
        }


    }

    public void handleBajoAddSum(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nuevoSuminitroFromBajoFormal.fxml"));
            Parent proot = loader.load();
            NuevoSuministroFromBajoFormalController controller = loader.getController();
            controller.pasarParametros(bajoEspecificacionController, field_codigo.getText(), textDedscripcion.getText(), um, String.valueOf(precio));


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
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
