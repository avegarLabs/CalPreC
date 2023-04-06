package views;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RenglonesUOController implements Initializable {

    private static SessionFactory sf;
    RenVarianteToUnidadObraController renVarianteToUnidadObraControllerHelp;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField filter;
    @FXML
    private Pane content_pane;
    @FXML
    private TableView<RenglonVarianteView> dataTable;
    @FXML
    private TableColumn<RenglonVarianteView, StringProperty> code;
    @FXML
    private TableColumn<RenglonVarianteView, StringProperty> descrip;
    @FXML
    private TableColumn<RenglonVarianteView, StringProperty> um;
    @FXML
    private TableColumn<RenglonVarianteView, DoubleProperty> costomat;
    @FXML
    private TableColumn<RenglonVarianteView, DoubleProperty> costomano;
    @FXML
    private TableColumn<RenglonVarianteView, DoubleProperty> costoEqui;
    @FXML
    private TableColumn<RenglonVarianteView, DoubleProperty> salario;
    @FXML
    private JFXComboBox<String> comboSobregrupo;
    @FXML
    private JFXComboBox<String> comboGrupo;
    @FXML
    private JFXComboBox<String> comboSubgrupo;
    private ArrayList<Sobregrupo> sobregrupoArrayList;
    private ObservableList<String> sobregrupoArrayListString;
    private Sobregrupo sobregrupo;
    private ArrayList<Grupo> grupoArrayList;
    private ObservableList<String> grupoArrayListString;
    private Grupo grupo;
    private ArrayList<Subgrupo> subgrupoArrayList;
    private ObservableList<String> subgrupoArrayListString;
    private Subgrupo subgrupo;
    private List<Renglonvariante> renglonvarianteArrayList;
    private ObservableList<RenglonVarianteView> renglonVarianteViewObservableList;
    private RenglonVarianteView renglonVarianteView;
    private ArrayList<Renglonrecursos> renglonrecursosArrayList;
    private ObservableList<ManoObraRenglonVariante> manoobraList;
    private double importe;
    private double valGrupo;
    private double sal;

    private String codeRV;

    private Renglonvariante renglonvariante;

    private Runtime garbage;
    private Query qrenglon;
    private Double rvSal;
    private Usuarios user;
    private Integer norma;

    /**
     * Bloques para las funciones de trabajo con la base de datos
     */
    //Llena el combobox de los sobregrupos
    public ObservableList<String> getSobregrupos() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            sobregrupoArrayListString = FXCollections.observableArrayList();
            sobregrupoArrayList = (ArrayList<Sobregrupo>) session.createQuery("FROM Sobregrupo ").list();
            sobregrupoArrayListString.addAll(sobregrupoArrayList.parallelStream().map(sobregrupo -> sobregrupo.getCodigo() + " - " + sobregrupo.getDescipcion()).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return sobregrupoArrayListString;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    //Llena el combobox de los grupos apartir del sobregrupo seleccionado
    public ObservableList<String> getGrupos(Sobregrupo sobregrupo) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            grupoArrayListString = FXCollections.observableArrayList();
            grupoArrayList = (ArrayList<Grupo>) session.createQuery("FROM Grupo WHERE sobregrupoId = :id").setParameter("id", sobregrupo.getId()).getResultList();
            grupoArrayListString.addAll(grupoArrayList.parallelStream().map(grupo -> grupo.getCodigog() + " - " + grupo.getDescripciong()).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return grupoArrayListString;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private ObservableList<String> getSubgrupos(Grupo grupo) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            subgrupoArrayListString = FXCollections.observableArrayList();
            subgrupoArrayList = (ArrayList<Subgrupo>) session.createQuery("FROM Subgrupo WHERE grupoId = :id").setParameter("id", grupo.getId()).getResultList();
            subgrupoArrayListString.addAll(subgrupoArrayList.parallelStream().map(sub -> sub.getCodigosub() + " - " + sub.getDescripcionsub()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return subgrupoArrayListString;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    @FXML
    public void handleLLenaGrupoList(ActionEvent event) {
        String code = comboSobregrupo.getValue().substring(0, 2);
        sobregrupoArrayList.forEach(sobre -> {
            if (sobre.getCodigo().contentEquals(code)) {
                sobregrupo = sobre;
            }
        });
        comboGrupo.setItems(getGrupos(sobregrupo));
        comboGrupo.setEditable(true);
        TextFields.bindAutoCompletion(comboGrupo.getEditor(), comboGrupo.getItems()).setPrefWidth(comboGrupo.getPrefWidth());
    }

    @FXML
    public void handleLlenaSubGrupoList(ActionEvent event) {

        String codeg = comboGrupo.getValue().substring(0, 3);
        grupoArrayList.forEach(group -> {
            if (group.getCodigog().contentEquals(codeg)) {
                grupo = group;
            }
        });
        comboSubgrupo.setItems(getSubgrupos(grupo));
        comboSubgrupo.setEditable(true);
        TextFields.bindAutoCompletion(comboSubgrupo.getEditor(), comboSubgrupo.getItems()).setPrefWidth(comboSubgrupo.getPrefWidth());
    }

    private double getImporteEscala(String grupo) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Salario salario = session.get(Salario.class, 1);
            if (grupo.contentEquals("II")) {
                importe = salario.getIi();
            }
            if (grupo.contentEquals("III")) {
                importe = salario.getIii();
            }
            if (grupo.contentEquals("IV")) {
                importe = salario.getIv();
            }
            if (grupo.contentEquals("V")) {
                importe = salario.getV();
            }
            if (grupo.contentEquals("VI")) {
                importe = salario.getVi();
            }
            if (grupo.contentEquals("VII")) {
                importe = salario.getVii();
            }
            if (grupo.contentEquals("VIII")) {
                importe = salario.getViii();
            }
            if (grupo.contentEquals("IX")) {
                importe = salario.getIx();
            }
            if (grupo.contentEquals("X")) {
                importe = salario.getX();
            }
            if (grupo.contentEquals("XI")) {
                importe = salario.getXi();
            }
            if (grupo.contentEquals("XII")) {
                importe = salario.getXii();
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return importe;
    }

    private ObservableList<ManoObraRenglonVariante> getManoObraRenglon(int idRv) {

        manoobraList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Renglonrecursos WHERE renglonvarianteId = :id");
            query.setParameter("id", idRv);
            renglonrecursosArrayList = (ArrayList<Renglonrecursos>) ((org.hibernate.query.Query) query).list();
            manoobraList.addAll(renglonrecursosArrayList.parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().contentEquals("2")).map(recursos -> {
                ManoObraRenglonVariante manoObraRenglonVariante = new ManoObraRenglonVariante(recursos.getRecursosByRecursosId().getCodigo(), recursos.getRecursosByRecursosId().getGrupoescala(), recursos.getCantidas(), recursos.getRecursosByRecursosId().getDescripcion());
                return manoObraRenglonVariante;
            }).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return manoobraList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();

    }

    public double calcSalRV(ObservableList<ManoObraRenglonVariante> manoObra) {
        manoObra.forEach(mano -> {
            valGrupo = getImporteEscala(mano.getEscala());
            importe = mano.getNorma() * valGrupo;
            sal = Math.round(importe * 100d) / 100d;
        });

        return sal;
    }

    private ObservableList<RenglonVarianteView> getRenglonesVariantes(Subgrupo subgrupo) {

        renglonVarianteViewObservableList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            rvSal = 0.0;
            renglonvarianteArrayList = new ArrayList<>();
            renglonvarianteArrayList = session.createQuery("FROM Renglonvariante WHERE subgrupoId = :id AND rs =: norm ").setParameter("id", subgrupo.getId()).setParameter("norm", norma).getResultList();
            renglonvarianteArrayList.forEach(rv -> {
                if (rv.getSalario() != null) {
                    rvSal = rv.getSalario();
                } else {
                    rvSal = 0.0;
                }

                renglonVarianteView = new RenglonVarianteView(rv.getId(), rv.getCodigo(), rv.getDescripcion(), rv.getUm(), Math.round(rv.getCostomat() * 100d) / 100d, Math.round(rv.getCostmano() * 100d) / 100d, Math.round(rv.getCostequip() * 100d) / 100d, rvSal);
                renglonVarianteViewObservableList.add(renglonVarianteView);
            });


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return renglonVarianteViewObservableList;

    }

    private void loadDatos() {

        code.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, StringProperty>("codigo"));
        descrip.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, StringProperty>("descripcion"));
        um.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, StringProperty>("um"));
        costomat.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, DoubleProperty>("costomat"));
        costomano.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, DoubleProperty>("costmano"));
        costoEqui.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, DoubleProperty>("costequip"));
        salario.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, DoubleProperty>("salario"));

    }

    @FXML
    public void handleLlenaTablaRenglonVariantes(ActionEvent event) {
        String codeSub = comboSubgrupo.getValue().substring(0, 4);

        subgrupoArrayList.forEach(sub -> {
            if (sub.getCodigosub().contentEquals(codeSub)) {
                subgrupo = sub;
            }
        });

        ObservableList<RenglonVarianteView> datos = getRenglonesVariantes(subgrupo);
        dataTable.getItems().setAll(datos);
        loadDatos();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboSobregrupo.setItems(getSobregrupos());
        comboSobregrupo.setEditable(true);
        TextFields.bindAutoCompletion(comboSobregrupo.getEditor(), comboSobregrupo.getItems()).setPrefWidth(comboSobregrupo.getPrefWidth());

        if (renglonVarianteViewObservableList != null) {
            FilteredList<RenglonVarianteView> filteredData = new FilteredList<RenglonVarianteView>(renglonVarianteViewObservableList, p -> true);

            SortedList<RenglonVarianteView> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

            dataTable.setItems(sortedData);

            filter.textProperty().addListener((prop, old, text) -> {
                filteredData.setPredicate(suministrosView -> {
                    if (text == null || text.isEmpty()) {
                        return true;
                    }

                    String descrip = suministrosView.getDescripcion().toLowerCase();
                    return descrip.contains(text.toLowerCase());
                });
            });
        }


        dataTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && dataTable.getSelectionModel() != null) {

                renglonVarianteView = dataTable.getSelectionModel().getSelectedItem();
                renglonvarianteArrayList.forEach(renglonvariante1 -> {
                    if (renglonvariante1.getId() == renglonVarianteView.getId()) {
                        renglonvariante = renglonvariante1;
                        renVarianteToUnidadObraControllerHelp.getRenglonVarianteFronHelp(renglonvariante);
                        garbage = Runtime.getRuntime();
                        garbage.gc();
                        Stage stage = (Stage) anchor.getScene().getWindow();
                        stage.close();


                    }
                });


            }
        });


    }

    @FXML
    public void handleViewComposicionRenglonVariante() {
        renglonVarianteView = dataTable.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ComposicionRV.fxml"));
            Parent proot = loader.load();
            ComposicionRenglonVarianteController composicionRenglonVarianteController = loader.getController();
            composicionRenglonVarianteController.LlenarDatos(renglonVarianteView, user, null);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void code() {

        System.out.println(codeRV);

    }

    public void pasarDatos(RenVarianteToUnidadObraController renVarianteToUnidadObraController, Integer tnorma) {
        norma = tnorma;
        renVarianteToUnidadObraControllerHelp = renVarianteToUnidadObraController;

    }
}
