package views;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.ConnectionModel;
import models.MemoriaView;
import models.Memoriadescriptiva;
import models.UniddaObraView;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MemoriaDescriptivaController implements Initializable {

    private static SessionFactory sf;
    @FXML
    private JFXTextField fieldTitulo;
    @FXML
    private JFXTextArea fieldTexto;
    @FXML
    private TableView<MemoriaView> tableTitulos;
    @FXML
    private TableColumn<MemoriaView, StringProperty> titulo;
    @FXML
    private Label labelUo;
    private int idUO;
    private Memoriadescriptiva memoriadescriptiva;
    private ArrayList<Memoriadescriptiva> memoriadescriptivaArrayList;
    private ObservableList<MemoriaView> memoriaViewObservableList;
    private MemoriaView memoriaView;

    public Integer addMemoria(Memoriadescriptiva memoria) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer memo = null;
        try {
            tx = session.beginTransaction();

            memo = (Integer) session.save(memoria);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return memo;
    }

    public ObservableList<MemoriaView> getMemoriasDescriptiva(int idObra) {
        memoriaViewObservableList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Memoriadescriptiva WHERE unidadobraId =: idUO");
            query.setParameter("idUO", idObra);
            memoriadescriptivaArrayList = (ArrayList<Memoriadescriptiva>) ((org.hibernate.query.Query) query).list();

            memoriadescriptivaArrayList.forEach(memoria -> {
                memoriaView = new MemoriaView(memoria.getId(), memoria.getTitulo(), memoria.getTexto());
                memoriaViewObservableList.add(memoriaView);

            });
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return memoriaViewObservableList;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tableTitulos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            fieldTitulo.setText(tableTitulos.getSelectionModel().getSelectedItem().getTitulo());
            fieldTexto.setText(tableTitulos.getSelectionModel().getSelectedItem().getTexto());

        });

    }


    public void handleSaveMemoria(ActionEvent event) {
        memoriadescriptiva = new Memoriadescriptiva();
        memoriadescriptiva.setTitulo(fieldTitulo.getText());
        memoriadescriptiva.setTexto(fieldTexto.getText());
        memoriadescriptiva.setUnidadobraId(idUO);

        Integer id = addMemoria(memoriadescriptiva);

        if (id != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Información salvada!");
            alert.showAndWait();
        }

        fieldTexto.clear();
        fieldTitulo.clear();

        memoriaViewObservableList = FXCollections.observableArrayList();
        memoriaViewObservableList = getMemoriasDescriptiva(idUO);
        loadTitulos();
        tableTitulos.getItems().setAll(memoriaViewObservableList);

    }

    public void loadTitulos() {
        titulo.setCellValueFactory(new PropertyValueFactory<MemoriaView, StringProperty>("titulo"));
    }

    public void definirUOToMemoria(UniddaObraView uniddaObraView) {
        idUO = uniddaObraView.getId();
        labelUo.setText(uniddaObraView.getCodigo());

        memoriaViewObservableList = FXCollections.observableArrayList();
        memoriaViewObservableList = getMemoriasDescriptiva(idUO);
        loadTitulos();
        tableTitulos.getItems().setAll(memoriaViewObservableList);


    }
}
