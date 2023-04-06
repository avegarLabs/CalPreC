package views;

import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.ConceptosGastosView;
import models.Conceptosgasto;
import models.ConnectionModel;
import models.Empresaconstructora;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ConceptosController implements Initializable {

    private static SessionFactory sf;
    public ConceptosController conceptosController;
    @FXML
    private TableView<ConceptosGastosView> dataTable;
    @FXML
    private TableColumn<ConceptosGastosView, IntegerProperty> code;
    @FXML
    private TableColumn<ConceptosGastosView, StringProperty> descrip;
    @FXML
    private TableColumn<ConceptosGastosView, DoubleProperty> coeficiente;
    @FXML
    private TableColumn<ConceptosGastosView, StringProperty> formula;
    @FXML
    private TableColumn<ConceptosGastosView, JFXCheckBox> calculado;
    @FXML
    private Label label_title;
    private JFXCheckBox checkBox;
    private String formulat;
    private ArrayList<Conceptosgasto> conceptosgastoArrayList;
    private ObservableList<ConceptosGastosView> conceptosGastosViewsArrayList;
    private ConceptosGastosView concepto;
    private Empresaconstructora empresa;
    private ObservableList<ConceptosGastosView> datos;
    /**
     * Este metodo mostrará todos los elemntos de gastos cde acuerdo un empresa constructora
     * En esta prueba se mostraran
     */
    private StringBuilder formredef;

    public ObservableList<ConceptosGastosView> getConceptosGastos(int idEmpresa) {

        conceptosGastosViewsArrayList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Object[]> concepts = session.createQuery("SELECT cg.id, cg.code, cg.descripcion, eg.coeficiente, eg.formula, eg.calcular, eg.porciento, cg.pertence FROM Empresagastos eg INNER JOIN Conceptosgasto cg ON eg.conceptosgastoId = cg.id WHERE eg.empresaconstructoraId =: idE ORDER BY  cg.id").setParameter("idE", idEmpresa).getResultList();

            for (Object[] row : concepts) {
                if (row[3].toString() == " ") {
                    formulat = " ";
                } else {
                    formulat = row[3].toString();
                }


                if (!row[5].toString().contentEquals("1.0")) {
                    formredef = new StringBuilder().append(row[5].toString()).append("%").append(row[3].toString().trim());
                    formulat = formredef.toString();
                }

                if (row[4].toString().contentEquals("0")) {
                    checkBox = new JFXCheckBox();
                    checkBox.setSelected(false);

                } else {
                    checkBox = new JFXCheckBox();
                    checkBox.setSelected(true);
                }
                concepto = new ConceptosGastosView(Integer.parseInt(row[0].toString()), row[1].toString(), row[2].toString(), Double.parseDouble(row[3].toString()), formulat, Double.parseDouble(row[6].toString()), checkBox, Integer.parseInt(row[7].toString()));
                conceptosGastosViewsArrayList.add(concepto);


            }


            tx.commit();
            session.close();
        } catch (
                HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return conceptosGastosViewsArrayList;
    }

    public void loadData() {
        code.setCellValueFactory(new PropertyValueFactory<ConceptosGastosView, IntegerProperty>("id"));
        descrip.setCellValueFactory(new PropertyValueFactory<ConceptosGastosView, StringProperty>("descripcion"));
        coeficiente.setCellValueFactory(new PropertyValueFactory<ConceptosGastosView, DoubleProperty>("coeficiente"));
        formula.setCellValueFactory(new PropertyValueFactory<ConceptosGastosView, StringProperty>("formula"));
        calculado.setCellValueFactory(new PropertyValueFactory<ConceptosGastosView, JFXCheckBox>("calc"));

        datos = FXCollections.observableArrayList();
        datos = getConceptosGastos(empresa.getId());
        dataTable.getItems().setAll(datos);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        conceptosController = this;

        dataTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && dataTable.getSelectionModel() != null) {
                concepto = dataTable.getSelectionModel().getSelectedItem();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarFormula.fxml"));
                    Parent proot = loader.load();
                    ActualizarFormulaController controller = loader.getController();
                    controller.cargarDatos(conceptosController, concepto, empresa.getId());

                    Stage stage = new Stage();
                    stage.setScene(new Scene(proot));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.toFront();
                    stage.show();


                    stage.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


    }


    public void cargarEmpresa(Empresaconstructora empresaconstructora) {

        empresa = empresaconstructora;

        loadData();
    }
}
