package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NuevaUnidadObraController implements Initializable {

    private static SessionFactory sf;
    public NuevaUnidadObraController nuevaUnidadObraController;
    public UnidadesObraObraController obraObraController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_descripcion;
    @FXML
    private JFXComboBox<String> comboGrupos;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXButton btn_close;
    private Obra obra;
    private Integer idEmp;
    private Integer idZon;
    private Integer idObj;
    private Integer idNiv;
    private Integer idEsp;
    private Integer idSub;
    private Integer inputLength = 7;
    private ArrayList<Grupoejecucion> grupoejecucionArrayList;
    private ObservableList<String> listGrupos;
    private ArrayList<Unidadobra> listCheck;
    private Grupoejecucion grupoejecucion;
    private Especialidades especialidades;
    private Runtime runtime;
    private ReportProjectStructureSingelton utilCalcSingelton = ReportProjectStructureSingelton.getInstance();

    public ArrayList<Unidadobra> getUnidadObraList() {

        listCheck = new ArrayList<>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            listCheck = (ArrayList<Unidadobra>) session.createQuery("From Unidadobra ").list();

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listCheck;
    }

    public ObservableList<String> getListGrupos() {
        listGrupos = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            grupoejecucionArrayList = (ArrayList<Grupoejecucion>) session.createQuery("FROM Grupoejecucion ").getResultList();
            for (Grupoejecucion grupoejecucion1 : grupoejecucionArrayList) {
                listGrupos.add(grupoejecucion1.getCodigo() + " - " + grupoejecucion1.getDescripcion());
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listGrupos;
    }

    public Especialidades getEspecialidades(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            especialidades = session.get(Especialidades.class, id);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return especialidades;
    }

    public Integer AddUnidadObra(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer unidId = null;
        try {
            tx = session.beginTransaction();

            unidId = (Integer) session.save(unidadobra);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return unidId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listCheck = new ArrayList<>();
        listCheck = getUnidadObraList();
        comboGrupos.setItems(getListGrupos());

        nuevaUnidadObraController = this;

    }

    @FXML
    public void addNewUnidad(ActionEvent event) {
        if (comboGrupos.getValue() != null) {
            String code = comboGrupos.getValue().substring(0, 3);
            grupoejecucion = grupoejecucionArrayList.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
        } else if (comboGrupos.getValue() == null) {
            grupoejecucion = grupoejecucionArrayList.get(0);
        }

        if (field_codigo.getText().trim().length() == 7 && !field_codigo.getText().trim().contains(" ")) {
            Unidadobra newUnidad = new Unidadobra();
            newUnidad.setCodigo(field_codigo.getText());
            newUnidad.setDescripcion(field_descripcion.getText());
            newUnidad.setCantidad(0.0);
            newUnidad.setSalariocuc(0.0);
            newUnidad.setObraId(obra.getId());
            newUnidad.setEmpresaconstructoraId(idEmp);
            newUnidad.setZonasId(idZon);
            newUnidad.setObjetosId(idObj);
            newUnidad.setNivelId(idNiv);
            newUnidad.setEspecialidadesId(idEsp);
            newUnidad.setSubespecialidadesId(idSub);
            newUnidad.setGrupoejecucionId(grupoejecucion.getId());
            newUnidad.setIdResolucion(obra.getSalarioId());

            Unidadobra unidadobra = listCheck.stream().filter(uni -> uni.getCodigo().equals(newUnidad.getCodigo()) && uni.getZonasId() == newUnidad.getZonasId() && uni.getObjetosId() == newUnidad.getObjetosId() && uni.getNivelId() == newUnidad.getNivelId()).findFirst().orElse(null);

            if (unidadobra != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Información");
                alert.setContentText("El código de la unidad de obra ya existe!!");
                alert.showAndWait();
            } else {
                Integer id = AddUnidadObra(newUnidad);

                if (id != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Información");
                    alert.setContentText(newUnidad.getDescripcion() + " creada satisfactoriamente!");
                    alert.showAndWait();

                    obraObraController.handleCargardatos(event);
                    obraObraController.obra = utilCalcSingelton.getObra(obra.getId());

                }

                cleanField();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("El código de la unidad de obra debe tener 7 dígitos");
            alert.showAndWait();
        }
    }


    public void cleanField() {
        field_codigo.clear();
        field_descripcion.clear();
        comboGrupos.getSelectionModel().clearSelection();
    }


    public void pasarParametros(UnidadesObraObraController unidadesObraObraController, Obra obraParameter, int idEmpresa, int idZona, int idObjetos, int idNivel, int idEspecialidad, int idSubespecialidad) {
        obra = obraParameter;
        idEmp = idEmpresa;
        idZon = idZona;
        idObj = idObjetos;
        idNiv = idNivel;
        idEsp = idEspecialidad;
        idSub = idSubespecialidad;

        especialidades = getEspecialidades(idEsp);
        obraObraController = unidadesObraObraController;


    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == inputLength) {
            field_descripcion.requestFocus();
        }
    }

    public void showGenericAsociation(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AgrupacionEspecifica.fxml"));
            Parent proot = loader.load();

            AgrupacionEspecificaController controller = loader.getController();
            controller.pasarEspecialidad(nuevaUnidadObraController, especialidades.getCodigo());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();

            runtime = Runtime.getRuntime();
            runtime.gc();

            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void unidadCode(String code) {

        field_codigo.setText(code);

    }

    public void handleCloseWindows(ActionEvent event) {

        runtime = Runtime.getRuntime();
        runtime.gc();

        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();

    }
}
