package views;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.CartaLimiteModelView;
import models.ConnectionModel;
import models.Juegorecursos;
import models.Semielaboradosrecursos;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class DesgloseSuministrosController implements Initializable {

    @FXML
    private TableView<CartaLimiteModelView> dataTable;

    @FXML
    private TableColumn<CartaLimiteModelView, StringProperty> code;

    @FXML
    private TableColumn<CartaLimiteModelView, StringProperty> descrip;

    @FXML
    private TableColumn<CartaLimiteModelView, StringProperty> um;

    @FXML
    private TableColumn<CartaLimiteModelView, DoubleProperty> cant;


    private ObservableList<CartaLimiteModelView> componentesObservableList;
    private ArrayList<Juegorecursos> recursosArrayList;
    private ArrayList<Semielaboradosrecursos> recursosSemielaboradosrecursos;

    @FXML
    private Label label_title;


    public ObservableList<CartaLimiteModelView> getComponentesObservableList(int id, String tipo, double cant) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            componentesObservableList = FXCollections.observableArrayList();
            recursosArrayList = new ArrayList<>();
            recursosSemielaboradosrecursos = new ArrayList<>();

            if (tipo.trim().equals("J")) {
                recursosArrayList = (ArrayList<Juegorecursos>) session.createQuery("FROM Juegorecursos WHERE juegoproductoId =: idJ").setParameter("idJ", id).getResultList();
                for (Juegorecursos items : recursosArrayList.parallelStream().filter(juegorecursos -> juegorecursos.getRecursosByRecursosId().getTipo().equals("1")).collect(Collectors.toList())) {
                    double valCant = cant * items.getCantidad();
                    componentesObservableList.add(new CartaLimiteModelView(items.getRecursosId(), items.getRecursosByRecursosId().getCodigo(), items.getRecursosByRecursosId().getDescripcion(), items.getRecursosByRecursosId().getUm(), Math.round(valCant * 10000d) / 10000d, 0.0, items.getRecursosByRecursosId().getPreciomn(), items.getRecursosByRecursosId().getTipo(), 0.0));
                }
            } else if (tipo.trim().equals("S")) {
                recursosSemielaboradosrecursos = (ArrayList<Semielaboradosrecursos>) session.createQuery("FROM Semielaboradosrecursos WHERE suministrossemielaboradosId =: idJ").setParameter("idJ", id).getResultList();
                for (Semielaboradosrecursos items : recursosSemielaboradosrecursos.parallelStream().filter(semielaboradosrecursos -> semielaboradosrecursos.getRecursosByRecursosId().getTipo().equals("1")).collect(Collectors.toList())) {
                    double valCant = cant * items.getCantidad();
                    componentesObservableList.add(new CartaLimiteModelView(items.getRecursosId(), items.getRecursosByRecursosId().getCodigo(), items.getRecursosByRecursosId().getDescripcion(), items.getRecursosByRecursosId().getUm(), Math.round(valCant * 10000d) / 10000d, 0.0, items.getRecursosByRecursosId().getPreciomn(), items.getRecursosByRecursosId().getTipo(), 0.0));
                }
            }

            tx.commit();
            session.close();
            return componentesObservableList;

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Error al tratar de cargar la composición del semielaborado");
            alert.showAndWait();
        }

        return FXCollections.emptyObservableList();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void pasarDatos(int id, String tipo, String codeS, double cantidad) {
        System.out.println("iD" + id + "Tipo: " + tipo + " *** " + codeS + " --- " + cantidad);

        label_title.setText("Composición del Suministro: " + codeS);

        componentesObservableList = getComponentesObservableList(id, tipo, cantidad);
        code.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, StringProperty>("code"));
        code.setPrefWidth(90);
        descrip.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, StringProperty>("descrip"));
        descrip.setPrefWidth(250);
        um.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, StringProperty>("um"));
        um.setPrefWidth(50);
        cant.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, DoubleProperty>("cant"));
        cant.setPrefWidth(90);

        dataTable.setItems(componentesObservableList);

    }


}

