package views;

import com.jfoenix.controls.*;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class NuevaObraRenglonController implements Initializable {

    private static SessionFactory sf;
    @FXML
    public JFXComboBox<String> combo_entidad;
    @FXML
    public JFXComboBox<String> combo_inversionista;
    public ReportProjectStructureSingelton structureSingelton = ReportProjectStructureSingelton.getInstance();
    PesupuestoRenglonVarianteController obrasControllerTemp;
    NuevaObraRenglonController nuevaObraController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXComboBox<String> combo_tipoObra;
    @FXML
    private JFXComboBox<String> combo_Salario;
    @FXML
    private JFXComboBox<String> combo_normas;
    @FXML
    private TableColumn<NewObrasFormTable, JFXCheckBox> empresaCode;
    @FXML
    private TableColumn<NewObrasFormTable, StringProperty> empresaDescrip;
    @FXML
    private ObservableList<String> listEntidad;
    private ObservableList<String> listInversionista;
    private ObservableList<String> listTipoObra;
    private ObservableList<String> listSalario;

    private ArrayList<Entidad> entidadArrayList;
    private ArrayList<Inversionista> inversionistaArrayList;
    private ArrayList<Tipoobra> tipoobrasArrayList;
    private ArrayList<Salario> salarioArrayList;
    private ArrayList<Empresaconstructora> empresaconstructoraArrayList;
    private ObservableList<NewObrasFormTable> obrasFormTableObservableList;
    private NewObrasFormTable newObrasFormTable;

    private JFXCheckBox checkBox;
    @FXML
    private TableView<NewObrasFormTable> tableEmpresa;
    private ObservableList<NewObrasFormTable> datos;

    private Obra newObra;
    // private Entidad entidad;
    private Empresaobra newEmpresaobra;

    private Empresaobrasalario empresaobrasalario;
    private JFXCheckBox obra_state;
    private Salario salario;
    private List<Empresaobra> empresaobraList;
    private TarifaSalarialRepository tarifaSalarialRepository = TarifaSalarialRepository.getInstance();


    @FXML
    private JFXButton btn_aceptar;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private ObrasView obrasView;
    @FXML
    private JFXCheckBox include;

    private Runtime garbage;

    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    private StringBuilder entidadBuil;
    private Integer inputLength = 6;
    private Entidad entidad;
    private Inversionista entidad1;
    private Tipoobra entidad2;
    private Salario entidad3;
    private ArrayList<Integer> selectedEmpresa;
    private List<Empresagastos> empresagastosList;
    private List<Empresaobraconceptoscoeficientes> empresaobraconceptoscoeficientesList;
    private int count;
    private int batchSize = 10;
    private ObservableList<TarifaSalarial> tarifaSalarialList;
/*
    public ObservableList<String> getListSalario() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            listSalario = FXCollections.observableArrayList();
            salarioArrayList = (ArrayList<Salario>) session.createQuery("FROM Salario ").getResultList();
            listSalario.addAll(salarioArrayList.parallelStream().map(sal -> sal.getDescripcion()).collect(toList()));

            tx.commit();
            session.close();
            return listSalario;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

 */
    private ObservableList<String> tarifaSalarialStringList;
    private String tipoObra;

    public ObservableList<String> getEntidadesList() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listEntidad = FXCollections.observableArrayList();
            entidadArrayList = (ArrayList<Entidad>) session.createQuery("FROM Entidad ").getResultList();
            listEntidad.addAll(entidadArrayList.parallelStream().map(ent -> ent.getCodigo() + " - " + ent.getDescripcion()).collect(toList()));
            tx.commit();
            session.close();
            return listEntidad;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getListInversionista() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            listInversionista = FXCollections.observableArrayList();
            inversionistaArrayList = (ArrayList<Inversionista>) session.createQuery("FROM Inversionista ").getResultList();
            listInversionista.addAll(inversionistaArrayList.parallelStream().map(inv -> inv.getCodigo() + " - " + inv.getDescripcion()).collect(toList()));

            tx.commit();
            session.close();
            return listInversionista;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getListTipoObra() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            listTipoObra = FXCollections.observableArrayList();
            tipoobrasArrayList = (ArrayList<Tipoobra>) session.createQuery("FROM Tipoobra ").getResultList();
            listTipoObra.addAll(tipoobrasArrayList.parallelStream().map(tip -> tip.getCodigo() + " - " + tip.getDescripcion()).collect(toList()));

            tx.commit();
            session.close();
            return listTipoObra;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<NewObrasFormTable> getListEmpresas() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            obrasFormTableObservableList = FXCollections.observableArrayList();
            empresaconstructoraArrayList = (ArrayList<Empresaconstructora>) session.createQuery("FROM Empresaconstructora ").list();
            for (Empresaconstructora empresas : empresaconstructoraArrayList) {
                checkBox = new JFXCheckBox();
                checkBox.setText(empresas.getCodigo());
                newObrasFormTable = new NewObrasFormTable(empresas.getId(), checkBox, empresas.getDescripcion());
                obrasFormTableObservableList.add(newObrasFormTable);
            }

            tx.commit();
            session.close();
            return obrasFormTableObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void AddNuevaObraEmpresaSalario(Empresaobrasalario empresaobrasalario) {

        Session ompOsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;

        try {
            trx = ompOsession.beginTransaction();
            ompOsession.persist(empresaobrasalario);
            trx.commit();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            ompOsession.close();
        }

    }

    /**
     * Metodo para agregar una nuerva obra
     */
    public Integer AddNuevaObra(Obra obra) {

        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer obId = null;

        try {
            trx = obsession.beginTransaction();
            obId = (Integer) obsession.save(obra);
            trx.commit();
            obsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
        return obId;
    }

    public void AddNuevaObraEmpresa(Empresaobra empresaobra) {

        Session ompOsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;

        try {
            trx = ompOsession.beginTransaction();
            ompOsession.persist(empresaobra);
            trx.commit();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            ompOsession.close();
        }

    }

    public void insertEmpresaobraconceptoscoeficientes(List<Empresaobraconceptoscoeficientes> empresaobraconceptoscoefList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (Empresaobraconceptoscoeficientes datos : empresaobraconceptoscoefList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.persist(datos);
            }

            tx.commit();
            session.close();
            garbage = Runtime.getRuntime();
            garbage.gc();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void loadDatosEmpresas() {
        empresaCode.setCellValueFactory(new PropertyValueFactory<NewObrasFormTable, JFXCheckBox>("empresa"));
        empresaDescrip.setCellValueFactory(new PropertyValueFactory<NewObrasFormTable, StringProperty>("descripcion"));
    }

    private ObservableList<String> getTarifasSalariales() {
        tarifaSalarialStringList = FXCollections.observableArrayList();
        tarifaSalarialStringList.addAll(tarifaSalarialRepository.tarifaSalarialObservableList.stream().map(TarifaSalarial::getCode).collect(Collectors.toSet()));
        return tarifaSalarialStringList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nuevaObraController = this;
    }

    public void passController(PesupuestoRenglonVarianteController obrasController, String tipo) {
        obrasControllerTemp = obrasController;
        tipoObra = tipo;
        tarifaSalarialList = FXCollections.observableArrayList();
        listSalario = FXCollections.observableArrayList();
        if (tarifaSalarialRepository.getAllTarifasSalarial() == null) {
            tarifaSalarialList = tarifaSalarialRepository.getAllTarifasSalarial();
        }
        loadDatosEmpresas();
        datos = FXCollections.observableArrayList();
        datos = getListEmpresas();
        tableEmpresa.getItems().setAll(datos);
        combo_entidad.setItems(getEntidadesList());
        combo_inversionista.setItems(getListInversionista());
        combo_tipoObra.setItems(getListTipoObra());
        listSalario.addAll(utilCalcSingelton.salarioList.stream().map(Salario::getDescripcion).collect(Collectors.toList()));
        combo_normas.setItems(listSalario);
        combo_Salario.setItems(getTarifasSalariales());
        empresagastosList = new ArrayList<>();
        empresagastosList = structureSingelton.getEmpresagastosList();
    }

    public void handleAddNweObra(ActionEvent event) {

        try {

            String[] codeEntidad = combo_entidad.getValue().split(" - ");
            String[] codeInvesionista = combo_inversionista.getValue().split(" - ");
            String[] codeTipo = combo_tipoObra.getValue().split(" - ");
            String codeSalario = combo_normas.getValue();

            newObra = new Obra();
            newObra.setCodigo(field_codigo.getText());
            newObra.setDescripion(text_descripcion.getText());
            newObra.setTipo(tipoObra);

            entidad = entidadArrayList.stream().filter(e -> e.getCodigo().contentEquals(codeEntidad[0])).findFirst().orElse(null);
            newObra.setEntidadId(entidad.getId());

            entidad1 = inversionistaArrayList.stream().filter(i -> i.getCodigo().contentEquals(codeInvesionista[0])).findFirst().orElse(null);
            newObra.setInversionistaId(entidad1.getId());


            entidad2 = tipoobrasArrayList.stream().filter(t -> t.getCodigo().contentEquals(codeTipo[0])).findFirst().orElse(null);
            newObra.setTipoobraId(entidad2.getId());


            entidad3 = utilCalcSingelton.salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals(codeSalario)).findFirst().get();
            newObra.setSalarioId(entidad3.getId());

            var tarifa = tarifaSalarialRepository.tarifaSalarialObservableList.parallelStream().filter(item -> item.getCode().trim().equals(combo_Salario.getValue().trim())).findFirst().get();
            newObra.setTarifaId(tarifa.getId());

            List<NewObrasFormTable> selectedEmpresa = tableEmpresa.getItems().parallelStream().filter(e -> e.getEmpresa().isSelected() == true).collect(toList());
            newObra.setDefcostmat(0);


            Integer id = AddNuevaObra(newObra);
            for (NewObrasFormTable emp : selectedEmpresa) {
                newEmpresaobra = new Empresaobra();
                newEmpresaobra.setEmpresaconstructoraId(emp.getId());
                newEmpresaobra.setObraId(id);

                AddNuevaObraEmpresa(newEmpresaobra);
                addNewEmpresaObraTarifa(id, emp.getId(), tarifa);

                List<Empresagastos> empresagastos = new ArrayList<>();
                empresagastos = empresagastosList.parallelStream().filter(eg -> eg.getEmpresaconstructoraId() == emp.getId()).collect(toList());
                empresaobraconceptoscoeficientesList = new ArrayList<>();
                empresaobraconceptoscoeficientesList = empresagastos.parallelStream().map(emg -> {
                    Empresaobraconceptoscoeficientes eocc = new Empresaobraconceptoscoeficientes();
                    eocc.setEmpresaconstructoraId(emp.getId());
                    eocc.setConceptosgastoId(emg.getConceptosgastoId());
                    eocc.setObraId(newObra.getId());
                    eocc.setCoeficiente(emg.getCoeficiente());
                    return eocc;
                }).collect(toList());
                insertEmpresaobraconceptoscoeficientes(empresaobraconceptoscoeficientesList);
            }


            if (id != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("ObraPCW " + newObra.getCodigo() + " creada satisfactoriamente!");
                alert.showAndWait();

                obrasView = new ObrasView(newObra.getId(), newObra.getCodigo(), newObra.getTipo(), newObra.getDescripion());
            }

        } catch (Exception ex) {
            ex.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error: " + NuevaObraController.class.getName());
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
        obrasControllerTemp.loadData();
        garbage = Runtime.getRuntime();
        garbage.gc();

        Stage stage = (Stage) btn_aceptar.getScene().getWindow();
        stage.close();

    }

    private void addNewEmpresaObraTarifa(int idObra, int idEmpresa, TarifaSalarial tarifa) {
        List<Empresaobratarifa> empresaobratarifaList = new ArrayList<>();
        for (GruposEscalas gruposEscalas : tarifa.getGruposEscalasCollection()) {
            Empresaobratarifa empresaobratarifa = new Empresaobratarifa();
            empresaobratarifa.setEmpresaconstructoraId(idEmpresa);
            empresaobratarifa.setObraId(idObra);
            empresaobratarifa.setTarifaId(tarifa.getId());
            empresaobratarifa.setGrupo(gruposEscalas.getGrupo());
            empresaobratarifa.setEscala(gruposEscalas.getValorBase());
            //empresaobratarifa.setTarifaInc(0.0);
            empresaobratarifa.setTarifa(gruposEscalas.getValor());
            empresaobratarifaList.add(empresaobratarifa);
        }
        utilCalcSingelton.createEmpresaobratarifa(empresaobratarifaList);
    }

    public void handleAddEmpresa(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevaEmpresa.fxml"));
            Parent proot = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadDatosEmpresas();
                datos = FXCollections.observableArrayList();
                datos = getListEmpresas();
                tableEmpresa.getItems().setAll(datos);

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == inputLength) {
            text_descripcion.requestFocus();
        }
    }


    public void handleAddEntidad(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InsertarEntidadRV.fxml"));
            Parent proot = loader.load();
            InsertarEntidadRVController controller = loader.getController();
            controller.pasarParametros(nuevaObraController);


            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(proot));
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleAddInversionista(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InsertarInversionistaRV.fxml"));
            Parent proot = loader.load();
            InsertarInversionistaRVController controller = loader.getController();
            controller.pasarParametros(nuevaObraController);


            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(proot));
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
