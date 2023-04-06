package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SuministrosInRVUpdateController implements Initializable {

    private static SessionFactory sf;
    @FXML
    public Label temp;
    SuministrosInRVUpdateController suministrosInRVUpdateController;
    RenVarianteToUnidadObraUpdateController renVarianteToUnidadObraUpdateControllersum;
    @FXML
    private Label label_title;
    @FXML
    private TableView<SumRVView> dataTable;
    @FXML
    private TableColumn<SumRVView, StringProperty> code;
    @FXML
    private TableColumn<SumRVView, StringProperty> descrip;
    @FXML
    private TableColumn<SumRVView, StringProperty> um;
    @FXML
    private TableColumn<SumRVView, DoubleProperty> costo;
    @FXML
    private TableColumn<SumRVView, DoubleProperty> cantidad;
    @FXML
    private TableColumn<SumRVView, DoubleProperty> usos;
    private ObservableList<SumRVView> sumRVViewArrayList;
    private SumRVView sumRVView;
    private SumRVView sumRVViewUpdate;
    private ArrayList<Renglonrecursos> renglonrecursosArrayList;
    private ArrayList<Renglonsemielaborados> renglonsemielaboradosArrayList;
    private ArrayList<Renglonjuego> renglonjuegoArrayList;
    private ObservableList<SumRVView> datos;
    private ArrayList<SumRVView> updateList;
    private Integer idRV;
    private double cantid;
    private int obraId;

    private JFXCheckBox checkCode;

    @FXML
    private MenuItem convert;

    @FXML
    private JFXButton close;
    private double cantSemi;

    public ObservableList<SumRVView> getComponentesRenglon(int id) {


        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            /**
             * Capturo los recursos que componen al renglon variante (Incluir a las otras categorias de los suministros: semilelaborados y juegos) y multiplicarlos por la cantidad
             */

            sumRVViewArrayList = FXCollections.observableArrayList();
            renglonrecursosArrayList = new ArrayList<>();
            renglonrecursosArrayList = (ArrayList<Renglonrecursos>) session.createQuery("FROM Renglonrecursos WHERE renglonvarianteId = :id").setParameter("id", id).getResultList();
            for (Renglonrecursos suministro : renglonrecursosArrayList) {
                Recursos sum = session.find(Recursos.class, suministro.getRecursosId());
                if (sum.getTipo().contentEquals("1")) {
                    checkCode = new JFXCheckBox();
                    checkCode.setText(sum.getCodigo());
                    checkCode.setSelected(true);
                    double recCant = suministro.getCantidas() * cantid / suministro.getUsos();
                    sumRVViewArrayList.add(new SumRVView(sum.getId(), checkCode, sum.getDescripcion(), sum.getUm(), Math.round(sum.getPreciomn() * 100d) / 100d, Math.round(recCant * 10000d) / 10000d, suministro.getUsos(), sum.getTipo()));
                }
            }

            /**
             * Capturo los suministros compuestos que componen al renglon variante
             *
             */
            renglonsemielaboradosArrayList = new ArrayList<>();
            renglonsemielaboradosArrayList = (ArrayList<Renglonsemielaborados>) session.createQuery("FROM Renglonsemielaborados WHERE renglonvarianteId = :id").setParameter("id", id).getResultList();
            if (renglonsemielaboradosArrayList != null) {
                for (Renglonsemielaborados semielab : renglonsemielaboradosArrayList) {
                    Suministrossemielaborados sumSemi = session.find(Suministrossemielaborados.class, semielab.getSuministrossemielaboradosId());
                    checkCode = new JFXCheckBox();
                    checkCode.setText(sumSemi.getCodigo());
                    checkCode.setSelected(true);
                    double recCant = semielab.getCantidad() * cantid / semielab.getUsos();
                    sumRVViewArrayList.add(new SumRVView(sumSemi.getId(), checkCode, sumSemi.getDescripcion(), sumSemi.getUm(), Math.round(sumSemi.getPreciomn() * 100d) / 100d, Math.round(recCant * 10000d) / 10000d, semielab.getUsos(), "S"));
                }
            }

            renglonjuegoArrayList = new ArrayList<>();
            renglonjuegoArrayList = (ArrayList<Renglonjuego>) session.createQuery("FROM Renglonjuego WHERE renglonvarianteId = :id").setParameter("id", id).getResultList();
            if (renglonjuegoArrayList != null) {
                for (Renglonjuego renglonjuego : renglonjuegoArrayList) {
                    Juegoproducto juegoproducto = session.find(Juegoproducto.class, renglonjuego.getJuegoproductoId());
                    checkCode = new JFXCheckBox();
                    checkCode.setText(juegoproducto.getCodigo());
                    checkCode.setSelected(true);
                    double recCant = renglonjuego.getCantidad() * cantid / renglonjuego.getUsos();
                    sumRVViewArrayList.add(new SumRVView(juegoproducto.getId(), checkCode, juegoproducto.getDescripcion(), juegoproducto.getUm(), Math.round(juegoproducto.getPreciomn() * 100d) / 100d, Math.round(recCant * 10000d) / 10000d, renglonjuego.getUsos(), "J"));
                }
            }

            tx.commit();
            session.close();
            return sumRVViewArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void loadComponents() {
        code.setCellValueFactory(new PropertyValueFactory<SumRVView, StringProperty>("codigo"));
        descrip.setCellValueFactory(new PropertyValueFactory<SumRVView, StringProperty>("descripcion"));
        um.setCellValueFactory(new PropertyValueFactory<SumRVView, StringProperty>("um"));
        costo.setCellValueFactory(new PropertyValueFactory<SumRVView, DoubleProperty>("costo"));
        cantidad.setCellValueFactory(new PropertyValueFactory<SumRVView, DoubleProperty>("cantidad"));
        usos.setCellValueFactory(new PropertyValueFactory<SumRVView, DoubleProperty>("usos"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        suministrosInRVUpdateController = this;
        loadComponents();

        dataTable.setOnMouseClicked(event -> {
            sumRVView = dataTable.getSelectionModel().getSelectedItem();
            sumRVView.getCodigo().setOnMouseClicked(event1 -> {
                if (!sumRVView.getCodigo().isSelected()) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("SuministroBajoEspecificacionComponentUpdate.fxml"));
                        Parent proot = loader.load();
                        BajoEspecificacionControllerComponetUpdate controller = loader.getController();
                        controller.pasarDatos(suministrosInRVUpdateController, sumRVView, obraId);

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
            });

        });

    }

    public void datosRV(RenVarianteToUnidadObraUpdateController renVarianteToUnidadObraUpdateController, Integer id, double cant, int idObra) {
        idRV = id;
        cantid = cant;
        datos = FXCollections.observableArrayList();
        datos = getComponentesRenglon(idRV);
        dataTable.getItems().setAll(datos);
        obraId = idObra;

        String val = datos.parallelStream().filter(sum -> sum.getUm().trim().equalsIgnoreCase("pt")).map(SumRVView::getUm).findFirst().orElse(" no ");
        if (!val.trim().equals("no")) {
            convert.setDisable(false);
        }
        renVarianteToUnidadObraUpdateControllersum = renVarianteToUnidadObraUpdateController;
    }

    public void saveSumUpdate(SumRVView sumRVView) {
        sumRVView.getCodigo();
        if (sumRVView != null) {
            JFXCheckBox check = new JFXCheckBox();
            check.setText(sumRVView.getCodigo().getText());
            check.setSelected(true);
            dataTable.getItems().add(new SumRVView(sumRVView.getId(), check, sumRVView.getDescripcion(), sumRVView.getUm(), sumRVView.getCosto(), sumRVView.getCantidad(), sumRVView.getUsos(), sumRVView.getTipo()));
        }


    }

    public SumRVView getSum() {

        return sumRVViewUpdate;


    }

    public void saveUpdateResources(javafx.event.ActionEvent event) {

        updateList = new ArrayList<SumRVView>();
        dataTable.getItems().forEach(item -> {
            if (item.getCodigo().isSelected() == true) {
                updateList.add(item);
            }
        });
        renVarianteToUnidadObraUpdateControllersum.createBajoespecificacionListUpdate(updateList);
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    public void handleDesgloseAction(ActionEvent event) {

        sumRVView = dataTable.getSelectionModel().getSelectedItem();

        if (sumRVView.getTipo().contentEquals("S")) {

            sumRVViewArrayList = FXCollections.observableArrayList();
            cantSemi = 0.0;
            cantSemi = sumRVView.getCantidad();
            sumRVViewArrayList = getComponentesInSemielaborados(sumRVView.getId());

            dataTable.getItems().addAll(sumRVViewArrayList);

        } else if (sumRVView.getTipo().contentEquals("J")) {

            sumRVViewArrayList = FXCollections.observableArrayList();
            cantSemi = 0.0;
            cantSemi = sumRVView.getCantidad();
            sumRVViewArrayList = getComponenteInJuegodeProducto(sumRVView.getId());

            dataTable.getItems().addAll(sumRVViewArrayList);
        }
    }

    private ObservableList<SumRVView> getComponentesInSemielaborados(int id) {
        sumRVViewArrayList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Suministrossemielaborados suministro = session.get(Suministrossemielaborados.class, id);
            suministro.getSemielaboradosrecursosById().forEach(items -> {
                if (items.getRecursosByRecursosId().getTipo().contentEquals("1")) {
                    checkCode = new JFXCheckBox();
                    checkCode.setText(items.getRecursosByRecursosId().getCodigo());
                    checkCode.setSelected(true);
                    sumRVView = new SumRVView(items.getRecursosId(), checkCode, items.getRecursosByRecursosId().getDescripcion(), items.getRecursosByRecursosId().getUm(), Math.round(items.getRecursosByRecursosId().getPreciomn() * 100d) / 100d, items.getCantidad() * cantSemi, 1.0, items.getRecursosByRecursosId().getTipo());
                    sumRVViewArrayList.add(sumRVView);
                }
            });
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return sumRVViewArrayList;

    }

    private ObservableList<SumRVView> getComponenteInJuegodeProducto(int id) {
        sumRVViewArrayList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Juegoproducto suministro = session.get(Juegoproducto.class, id);
            suministro.getJuegorecursosById().forEach(items -> {
                if (items.getRecursosByRecursosId().getTipo().contentEquals("1")) {
                    checkCode = new JFXCheckBox();
                    checkCode.setText(items.getRecursosByRecursosId().getCodigo());
                    checkCode.setSelected(true);
                    sumRVView = new SumRVView(items.getRecursosId(), checkCode, items.getRecursosByRecursosId().getDescripcion(), items.getRecursosByRecursosId().getUm(), Math.round(items.getRecursosByRecursosId().getPreciomn() * 100d) / 100d, items.getCantidad() * cantSemi, 1.0, items.getRecursosByRecursosId().getTipo());
                    sumRVViewArrayList.add(sumRVView);
                }
            });
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return sumRVViewArrayList;

    }

    public void handleConvertAction(ActionEvent event) {
        SumRVView sum = dataTable.getSelectionModel().getSelectedItem();
        double valor = sum.getCantidad() / 423.6;

        DecimalFormat df = new DecimalFormat("0.0000");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Conversión de PT a M3");
        alert.setContentText(String.valueOf(sum.getCantidad()) + " PT son: " + df.format(valor) + " M3");
        alert.showAndWait();

    }

    public void handleAgregarRecursosAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SuministroBajoEspecificacionComponentUpdateAdd.fxml"));
            Parent proot = loader.load();
            BajoEspecificacionControllerComponetUpdateAdd controller = loader.getController();
            controller.pasarDatos(suministrosInRVUpdateController, obraId);

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


}
