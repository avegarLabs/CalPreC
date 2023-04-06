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
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Especialidades;
import models.Grupoejecucion;
import models.Unidadobra;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ActualizarUnidadObraController implements Initializable {

    private static SessionFactory sf;
    public ActualizarUnidadObraController nuevaUnidadObraController;
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
    private Integer IdObra;
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
    private Unidadobra unidadobraToChange;

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
            listGrupos.addAll(grupoejecucionArrayList.parallelStream().map(grupoejecucion1 -> grupoejecucion1.getCodigo() + " - " + grupoejecucion1.getDescripcion()).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return listGrupos;
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


    public Integer actualizarUnidadObra(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer unidId = null;
        try {
            tx = session.beginTransaction();


            Unidadobra unidad = session.get(Unidadobra.class, unidadobra.getId());
            if (unidad != null) {

                unidad.setCodigo(field_codigo.getText());
                unidad.setDescripcion(field_descripcion.getText());
                unidad.setGrupoejecucionId(grupoejecucion.getId());

                session.update(unidad);
            }


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

        String code = comboGrupos.getValue().substring(0, 3);

        grupoejecucionArrayList.forEach(grupoejecucion1 -> {
            if (grupoejecucion1.getCodigo().contentEquals(code)) {
                grupoejecucion = grupoejecucion1;
            }
        });


        if (field_codigo.getText().length() == 7) {

            actualizarUnidadObra(unidadobraToChange);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Datos actualizados satisfactoriamente!");
            alert.showAndWait();


            cleanField();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("El código de la unidad de obra debe contener 7 dígitos!");
            alert.showAndWait();
        }

    }


    public void cleanField() {
        field_codigo.clear();
        field_descripcion.clear();
        comboGrupos.getSelectionModel().clearSelection();
    }

    public void pasarParametros(Unidadobra unidadobra) {

        field_codigo.setText(unidadobra.getCodigo());
        field_descripcion.setText(unidadobra.getDescripcion());
        if (unidadobra.getGrupoejecucionId() == null) {
            comboGrupos.getSelectionModel().select(listGrupos.get(0));

        } else {
            comboGrupos.getSelectionModel().select(unidadobra.getGrupoejecucionByGrupoejecucionId().getCodigo() + " - " + unidadobra.getGrupoejecucionByGrupoejecucionId().getDescripcion());
        }
        especialidades = unidadobra.getEspecialidadesByEspecialidadesId();
        unidadobraToChange = unidadobra;
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == inputLength) {
            field_descripcion.requestFocus();
        }
    }

    public void showGenericAsociation(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AgrupacionEspecificaModificar.fxml"));
            Parent proot = loader.load();

            AgrupacionEspecificaModificarController controller = loader.getController();
            controller.pasarEspecialidad(nuevaUnidadObraController, especialidades.getCodigo());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.toFront();
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void unidadCode(String code) {

        field_codigo.setText(code);

    }

    public void handleCloseWindows(ActionEvent event) {

        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();

    }
}
