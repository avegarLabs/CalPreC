package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class SuministrosController implements Initializable {

    private static SessionFactory sf;
    public SuministrosController suministrosController;
    @FXML
    public JFXComboBox<String> comboTarifas;
    CreateLogFile createLogFile = new CreateLogFile();
    @FXML
    private Label label_title;
    @FXML
    private TableView<SuministrosView> dataTable;
    @FXML
    private TableColumn<SuministrosView, StringProperty> code;
    @FXML
    private TableColumn<SuministrosView, StringProperty> descrip;
    @FXML
    private TableColumn<SuministrosView, StringProperty> um;
    @FXML
    private TableColumn<SuministrosView, StringProperty> mn;
    @FXML
    private TableColumn<SuministrosView, JFXCheckBox> active;
    @FXML
    private TableColumn<SuministrosView, StringProperty> peso;
    @FXML
    private TableView<SuministroPropioView> datatable_sump;
    @FXML
    private Pane content_pane;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXButton btn_import;
    @FXML
    private JFXTextField filter;
    @FXML
    private JFXCheckBox checkprecons;
    @FXML
    private JFXCheckBox checkagregados;
    private SuministrosView suministroView;
    private ArrayList<Recursos> listSuministros;
    private ObservableList<SuministrosView> suministrosViewsList;
    @FXML
    private MenuItem option1;
    @FXML
    private MenuItem option2;
    @FXML
    private MenuItem option21;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    @FXML
    private JFXButton btn_status;


    /**
     * Bloques de métodos que continen operaciones de bases de datos
     */

    //Este metodo devuelve un la lista con todos loe elementos de la table suministros del precons
    public ObservableList<SuministrosView> getPreconsSuministros() {

        Session session = ConnectionModel.createAppConnection().openSession();
        DecimalFormat df = new DecimalFormat("#0.00");

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listSuministros = new ArrayList<Recursos>();
            suministrosViewsList = FXCollections.observableArrayList();

            listSuministros = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE tipo = '1'").getResultList();
            for (Recursos suminist : listSuministros) {
                suministrosViewsList.add(new SuministrosView(suminist.getId(), suminist.getCodigo(), suminist.getDescripcion(), suminist.getUm(), suminist.getPeso(), Math.round(suminist.getPreciomn() * 100d) / 100d, 0.0, suminist.getGrupoescala(), suminist.getTipo(), suminist.getPertenece(), 0.0, utilCalcSingelton.createCheckBox(suminist.getActive())));
            }
            tx.commit();
            session.close();
            return suministrosViewsList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }


    public void deleteSuministro(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (!utilCalcSingelton.getBAjoBajoespecificacionList(id) && !utilCalcSingelton.getBAjoBajoespecificacionrvList(id)) {
                Recursos suministro = session.get(Recursos.class, id);
                session.delete(suministro);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText(" El suministros a eliminar esta asociado a un determinado elemento del presupuesto!!!");
                alert.showAndWait();
            }
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List<SuministrosView> getSuminitrsosDuplicados() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createSQLQuery("SELECT r.codigo, r.descripcion, count(r.codigo) FROM recursos r INNER JOIN bajoespecificacion b on r.id = b.id_suministro WHERE r.pertenece = 'R266' AND r.pertenece != 'I' GROUP BY r.codigo, r.descripcion HAVING COUNT(r.codigo)>1 ");
            List<Object[]> dataList = query.getResultList();
            List<SuministrosView> allDuplicados = new ArrayList<>();
            for (Object[] objects : dataList) {
                System.out.println(" **** " + objects[1].toString());
                allDuplicados.add(new SuministrosView(0, objects[0].toString(), objects[1].toString(), " ", 0.0, 0.0, 0.0, "I", "1", "D", 0.0, new JFXCheckBox()));
            }
            tx.commit();
            session.close();
            return allDuplicados;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public void loadData() {
        code.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descrip.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("descripcion"));
        descrip.setPrefWidth(550);
        um.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("um"));
        um.setPrefWidth(50);
        mn.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("preciomn"));
        mn.setPrefWidth(100);
        peso.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("peso"));
        peso.setPrefWidth(100);
        active.setCellValueFactory(new PropertyValueFactory<SuministrosView, JFXCheckBox>("active"));
        peso.setPrefWidth(100);

        suministrosViewsList = FXCollections.observableArrayList();
        suministrosViewsList = getPreconsSuministros();

        FilteredList<SuministrosView> filteredData = new FilteredList<SuministrosView>(suministrosViewsList, p -> true);

        SortedList<SuministrosView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(suministrosView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = suministrosView.toString().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        suministrosController = this;
        ObservableList<String> tarifasLst = FXCollections.observableList(utilCalcSingelton.salarioList.stream().map(Salario::getDescripcion).collect(Collectors.toList()));
        comboTarifas.setItems(tarifasLst);

        TableView.TableViewSelectionModel selectionModel = dataTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);

    }


    public void getCheckUser(Usuarios usuarios) {

        loadData();

        if (usuarios.getGruposId() == 1) {
            btn_add.setDisable(false);
            option1.setDisable(false);
            option2.setDisable(false);
            option21.setDisable(false);
            btn_import.setDisable(false);
            btn_status.setDisable(false);

        }
    }

    public void handleAgregarAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nuevoSuminitroAdmin.fxml"));
            Parent proot = loader.load();

            NuevoSuministroAdminController controller = loader.getController();
            controller.pasarParametros(suministrosController, null);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleSuministrosPropioAction(ActionEvent event) {
        suministroView = dataTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateSuminitroGeneral.fxml"));
            Parent proot = loader.load();
            ActualizarSuministroGeneralController actualizarSuministroController = loader.getController();
            actualizarSuministroController.pasarParametros(suministrosController, suministroView);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void handleEliminarSuministros(ActionEvent event) {
        suministroView = dataTable.getSelectionModel().getSelectedItem();
        deleteSuministro(suministroView.getId());
        loadData();

    }

    public void closeWindows(ActionEvent event) {
        Stage stage = (Stage) btn_add.getScene().getWindow();
        stage.close();
    }

    public void ImportSuminitros(ActionEvent event) {

        if (comboTarifas.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Indique la Resolución para la cual creará los suminitros");
            alert.showAndWait();
        } else {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarSuminitrosTableGeneral.fxml"));
                Parent proot = loader.load();
                ImportarSuministrosGeneralController controller = loader.getController();

                String tag = utilCalcSingelton.salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals(comboTarifas.getValue().trim())).findFirst().map(Salario::getTag).get();
                controller.passPerteneceSUM(tag, suministrosController);

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void handleCleanViewByResolt(ActionEvent event) {
        String tag = utilCalcSingelton.salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals(comboTarifas.getValue().trim())).findFirst().map(Salario::getTag).get();
        code.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descrip.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("descripcion"));
        descrip.setPrefWidth(550);
        um.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("um"));
        um.setPrefWidth(50);
        mn.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("preciomn"));
        mn.setPrefWidth(100);
        peso.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("peso"));
        peso.setPrefWidth(100);

        active.setCellValueFactory(new PropertyValueFactory<SuministrosView, JFXCheckBox>("active"));
        peso.setPrefWidth(100);

        FilteredList<SuministrosView> filteredData = new FilteredList<>(reloadSuministros(tag.trim()), p -> true);

        SortedList<SuministrosView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(suministrosView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = suministrosView.toString().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });
    }

    private ObservableList<SuministrosView> reloadSuministros(String tipo) {
        ObservableList<SuministrosView> suministrosViewObservableList = FXCollections.observableArrayList();
        suministrosViewObservableList.addAll(suministrosViewsList.parallelStream().filter(suministrosView -> suministrosView.getPertene().trim().equals(tipo)).collect(Collectors.toList()));
        return suministrosViewObservableList;
    }

    public void HandleDeleteDuplicados(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Información");
        alert.setContentText("CalPreC, eliminará y unificará los códigos de los recursos repetidos, por favor espere a que termine el proceso!!!");
        alert.showAndWait();

        List<SuministrosView> duplicates = getSuminitrsosDuplicados();
        List<Recursos> toDeleteList = new ArrayList<>();
        List<Recursos> toChange = new ArrayList<>();
        for (SuministrosView duplicate : duplicates) {
            List<Recursos> list = getItemsByCodeDuplicate(duplicate.getCodigo()).parallelStream().filter(item -> item.getPertenece().trim().equals("R266") || !item.getPertenece().trim().equals("I")).collect(Collectors.toList());
            for (Recursos recursos : list) {
                if (utilCalcSingelton.getBajoespecificacionUOList(recursos.getId()).size() > 0 || utilCalcSingelton.getBajoespecificacionRVList(recursos.getId()).size() > 0) {
                    System.out.println("To Change Suministro Insumo: " + recursos.getCodigo());
                    toChange.add(recursos);
                    //  } else if (recursos.getSemielaboradosrecursosById().size() > 0 || recursos.getJuegorecursosById().size() > 0 || recursos.getRenglonrecursosById().size() > 0) {
                    //    System.out.println("Suministro Insumo: " + recursos.getCodigo());
                } else {
                    toDeleteList.add(recursos);
                }


            }
        }
        viewResultToSearch(toChange, toDeleteList);

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Proceso Terminado \n" + "Unificad : " + toChange.size() + " \n" + "Eliminados: " + toDeleteList.size());
        alert.showAndWait();
    }

    private void viewResultToSearch(List<Recursos> toChange, List<Recursos> toDeleteList) {
        if (toDeleteList.size() > 0) {
            utilCalcSingelton.deleterecursosList(toDeleteList);
        }
        if (toChange.size() > 0) {
            changeRecursosList(toChange);
        }
    }

    private void changeRecursosList(List<Recursos> toChange) {
        List<Recursos> globalList = new ArrayList<>();
        List<Recursos> deleteList = new ArrayList<>();
        for (Recursos recursos : toChange) {
            if (globalList.size() == 0) {
                globalList.add(recursos);
            } else if (globalList.size() > 0 && !globalList.contains(recursos)) {
                globalList.add(recursos);
            }
        }
        utilCalcSingelton.deleterecursosList(deleteList);
        filterRecursosBajoEspecificacionByItem(globalList);
    }

    private void filterRecursosBajoEspecificacionByItem(List<Recursos> globalList) {
        for (Recursos recursos : globalList) {
            updaterecursosBajo(recursos, utilCalcSingelton.getBajoespecificacionUOByCodeList(recursos.getCodigo().trim()));
            updaterecursosBajoRV(recursos, utilCalcSingelton.getBajoespecificacionRVByCodeList(recursos.getCodigo().trim()));
        }
    }

    private void updaterecursosBajoRV(Recursos
                                              recursos, List<Bajoespecificacionrv> bajoespecificacionRVByCodeList) {
        List<Bajoespecificacionrv> updateList = new ArrayList<>();
        for (Bajoespecificacionrv bajoespecificacionrv : bajoespecificacionRVByCodeList) {
            bajoespecificacionrv.setIdsuministro(recursos.getId());
            bajoespecificacionrv.setCosto(bajoespecificacionrv.getCantidad() * recursos.getPreciomn());
            updateList.add(bajoespecificacionrv);
        }
        utilCalcSingelton.updateRecurBajoRVList(updateList);
    }

    private void updaterecursosBajo(Recursos recursos, List<Bajoespecificacion> bajoespecificacionUOByCodeList) {
        List<Bajoespecificacion> updateList = new ArrayList<>();
        for (Bajoespecificacion bajoespecificacion : bajoespecificacionUOByCodeList) {
            bajoespecificacion.setIdSuministro(recursos.getId());
            bajoespecificacion.setCosto(bajoespecificacion.getCantidad() * recursos.getPreciomn());
            updateList.add(bajoespecificacion);
        }
        utilCalcSingelton.updateRecurBajoUOList(updateList);
    }

    public List<Recursos> getItemsByCodeDuplicate(String code) {
        return listSuministros.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).collect(Collectors.toList());

    }

    public void actionHandleUpdateState(ActionEvent event) {
        ObservableList<SuministrosView> list = FXCollections.observableArrayList();
        list = dataTable.getSelectionModel().getSelectedItems();
        List<Recursos> tempToUpdate = new ArrayList<>();
        for (SuministrosView suministrosView : list) {
            Recursos recursos = getRecursosById(suministrosView.getId());
            if (recursos.getActive() == 1) {
                recursos.setActive(0);
            } else if (recursos.getActive() == 0) {
                recursos.setActive(1);
            }
            tempToUpdate.add(recursos);
        }

        try {
            utilCalcSingelton.updateRecursosState(tempToUpdate);
            utilCalcSingelton.showAlert("Estado de los suministros actualizado!!!", 1);
            loadData();
        } catch (Exception e) {
            utilCalcSingelton.showAlert("Error al actualizar el estado de los suministros: " + e.getMessage(), 1);
        }


    }

    private Recursos getRecursosById(int idRec) {
        return listSuministros.parallelStream().filter(item -> item.getId() == idRec).findFirst().get();
    }


}