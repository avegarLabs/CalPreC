package views;

import com.jfoenix.controls.JFXButton;
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
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class PesupuestoRenglonVarianteController implements Initializable {
    private static SessionFactory sf;
    public PesupuestoRenglonVarianteController renglonVarianteController;
    ReportProjectStructureSingelton singelton = ReportProjectStructureSingelton.getInstance();
    @FXML
    private Label label_title;
    @FXML
    private JFXButton btn_add;
    @FXML
    private Pane content_paneOb;
    @FXML
    private TableView<ObrasView> dataTable;
    @FXML
    private TableColumn<ObrasView, StringProperty> code;
    @FXML
    private TableColumn<ObrasView, StringProperty> descrip;
    @FXML
    private Pane enterPane;
    private ArrayList<Obra> obraArrayList;
    private ObservableList<ObrasView> obrasViewObservableList;
    private ObrasView obrasView;
    private ObservableList<ObrasView> datos;
    @FXML
    private JFXTextField filter;
    private UsuariosDAO usuariosDAO;
    private Integer count;
    @FXML
    private ContextMenu rvMenu;
    private UtilCalcSingelton util = UtilCalcSingelton.getInstance();
    private ReportProjectStructureSingelton utilCalcSingelton = ReportProjectStructureSingelton.getInstance();

    /**
     * Bloques para las funciones de trabajo con la base de datos
     */
    //Llena el combobox de los sobregrupos
    public ObservableList<ObrasView> getPresupustoRVObras() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            obrasViewObservableList = FXCollections.observableArrayList();
            obraArrayList = (ArrayList<Obra>) session.createQuery("FROM Obra where tipo='RV'").list();

            obrasViewObservableList.addAll(obraArrayList.parallelStream().map(obra -> {
                ObrasView obrasView = new ObrasView(obra.getId(), obra.getCodigo(), obra.getTipo(), obra.getDescripion());
                return obrasView;
            }).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return obrasViewObservableList;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private void deleteObras(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Obra obra = session.get(Obra.class, id);
            if (obra != null) {

                int delete1 = session.createQuery("DELETE FROM Empresaobrasalario WHERE obraId =: id").setParameter("id", obra.getId()).executeUpdate();
                int delete2 = session.createQuery("DELETE FROM Empresaobra WHERE obraId =: id").setParameter("id", obra.getId()).executeUpdate();
                int delete3 = session.createQuery("DELETE FROM Empresaobraconcepto WHERE obraId =: id").setParameter("id", obra.getId()).executeUpdate();
                int delete4 = session.createQuery("DELETE FROM Empresaobraconceptoglobal WHERE obraId =: id").setParameter("id", obra.getId()).executeUpdate();
                int dalete5 = session.createQuery("DELETE FROM Coeficientesequipos WHERE obraId =: idOb").setParameter("idOb", obra.getId()).executeUpdate();
                int delete6 = session.createQuery("DELETE FROM Empresaobraconceptoscoeficientes WHERE obraId =: idOb").setParameter("idOb", obra.getId()).executeUpdate();

                deleteRecursosBajo(obra.getId());
                deleteAllCompontes(obra.getId());


            }
            session.delete(obra);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private void deleteRecursosBajo(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            String queryStr = "select bajo.id, bajo.nivelespecifico__id from bajoespecificacionrv bajo inner join nivelespecifico uo on bajo.nivelespecifico__id = uo.id WHERE uo.obra__id =" + id;
            Query query = session.createSQLQuery(queryStr);
            List<Object[]> dataList = query.getResultList();
            for (Object[] row : dataList) {
                int op1 = session.createQuery("Delete from Bajoespecificacionrv WHERE id=:idS").setParameter("idS", Integer.parseInt(row[0].toString())).executeUpdate();
            }


            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    private void deleteAllCompontes(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Nivelespecifico> unidadobras = session.createQuery("FROM Nivelespecifico WHERE obraId =: id").setParameter("id", id).getResultList();
            for (Nivelespecifico uo : unidadobras) {
                int op1 = session.createQuery("DELETE FROM Renglonnivelespecifico WHERE nivelespecificoId =: idU").setParameter("idU", uo.getId()).executeUpdate();
                int op2 = session.createQuery("DELETE FROM Planrenglonvariante WHERE nivelespecificoId =: idU").setParameter("idU", uo.getId()).executeUpdate();
                int op3 = session.createQuery("DELETE FROM Planrecrv WHERE nivelespId =: idU").setParameter("idU", uo.getId()).executeUpdate();
                int op4 = session.createQuery("DELETE FROM CertificacionRenglonVariante WHERE nivelespecificoId =: idU").setParameter("idU", uo.getId()).executeUpdate();
                int op5 = session.createQuery("DELETE FROM Certificacionrecrv WHERE nivelespId =: idU").setParameter("idU", uo.getId()).executeUpdate();

                session.delete(uo);

            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void loadData() {
        code.setCellValueFactory(new PropertyValueFactory<ObrasView, StringProperty>("codigo"));
        descrip.setCellValueFactory(new PropertyValueFactory<ObrasView, StringProperty>("descripion"));
        datos = FXCollections.observableArrayList();
        datos = getPresupustoRVObras();

        FilteredList<ObrasView> filteredData = new FilteredList<ObrasView>(datos, p -> true);

        SortedList<ObrasView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(obrasView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = obrasView.getDescripion().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usuariosDAO = UsuariosDAO.getInstance();
        if (usuariosDAO.usuario.getGruposId() == 1) {
            btn_add.setDisable(false);
        } else if (usuariosDAO.usuario.getGruposId() == 3) {
            btn_add.setDisable(false);
        } else if (usuariosDAO.usuario.getGruposId() == 6) {
            for (MenuItem item : rvMenu.getItems()) {
                item.setDisable(true);
            }
        }

        renglonVarianteController = this;
        loadData();

        dataTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && dataTable.getSelectionModel() != null) {
                obrasView = dataTable.getSelectionModel().getSelectedItem();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("crearUnidadesdeObraenRenglonVariante.fxml"));
                    Parent proot = loader.load();
                    RenglonVarianteObraController controller = loader.getController();
                    controller.definirObra(obrasView);

                    content_paneOb.getChildren().setAll(proot);
/*
                    Stage stage = (Stage) btn_add.getScene().getWindow();
                    stage.close();
*/
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void handleNuevaObraForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevaObraRenglon.fxml"));
            Parent proot = loader.load();
            NuevaObraRenglonController controller = loader.getController();
            controller.passController(renglonVarianteController, "RV");

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadData();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Como mostrar datos sin llamar a otra ventana
     */
    public void handleEditObra(ActionEvent event) {
        obrasView = dataTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarObraRV.fxml"));
            Parent proot = loader.load();
            ActualizarObraRVController controller = loader.getController();
            controller.pasarDatosObra(obrasView);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadData();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleDeleteObra(ActionEvent event) {
        obrasView = dataTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(" Esta seguro de eliminar la obra : " + obrasView.getCodigo() + " - " + obrasView.getDescripion());

        ButtonType buttonAgregar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonAgregar) {
            deleteObras(obrasView.getId());
            alert.setHeaderText("Información");
            alert.setContentText("Obra: " + obrasView.getCodigo() + " eliminada satisfactoriamente!");
            alert.showAndWait();
        }
        loadData();
        if (dataTable.getItems().isEmpty()) {
            dataTable.setVisible(false);
        }
    }

    public void handleDuplicateObraForm(ActionEvent event) {

        Obra obra = obraArrayList.parallelStream().filter(obra1 -> obra1.getId() == dataTable.getSelectionModel().getSelectedItem().getId()).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DuplicarObraRV.fxml"));
            Parent proot = loader.load();
            DuplicarObraRVController controller = loader.getController();
            controller.cargarObra(renglonVarianteController, obra);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


            stage.setOnCloseRequest(event1 -> {
                loadData();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleSuplemantaObra(ActionEvent event) {

        Obra obra = singelton.getObra(dataTable.getSelectionModel().getSelectedItem().getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SuplementarObra.fxml"));
            Parent proot = loader.load();
            SuplementarObra controller = loader.getController();
            controller.pasarDatos(obra);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadData();

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void updateObras(Obra obra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(obra);
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void handleRecalcular(ActionEvent event) {
        Obra obra = utilCalcSingelton.getObra(dataTable.getSelectionModel().getSelectedItem().getId());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("recalcularObraRV.fxml"));
            Parent proot = loader.load();
            RecalculraRVController controller = (RecalculraRVController) loader.getController();
            controller.pasarDatos(obra);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadData();

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleCreateUnidadObra(ActionEvent event) {
        Obra obra = utilCalcSingelton.getObra(dataTable.getSelectionModel().getSelectedItem().getId());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(" Esta seguro que desea convertir el presupuesto: " + obra.getCodigo() + " - " + obra.getDescripion() + " a unidad de obra. Por favor espere, CalPreC informará cuando termine el proceso o haya ocurrido un error! ");
        ButtonType buttonAgregar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonAgregar) {
            obra.setTipo("UO");
            createNivelEspecifico(obra.getNivelespecificosById());
            updateObras(obra);
            deleteAllCompontes(obra.getId());

            alert.setHeaderText("Información");
            alert.setContentText("El presupuesto : " + obra.getCodigo() + " ha sido convertido a unidad de obra ");
            alert.showAndWait();
        }
        loadData();

    }

    private Renglonnivelespecifico getFirstRenglonNivel(List<Renglonnivelespecifico> list) {
        Renglonnivelespecifico renglonnivelespecifico = null;
        for (int i = 0; i < list.size(); i++) {
            renglonnivelespecifico = list.get(0);
        }
        return renglonnivelespecifico;
    }


    private void createNivelEspecifico(Collection<Nivelespecifico> unidadobraCollection) {
        for (Nivelespecifico unidadobra : unidadobraCollection) {
            if (unidadobra.getRenglonnivelespecificosById().size() > 0) {
                System.out.println(unidadobra.getRenglonnivelespecificosById().size());
                Renglonnivelespecifico renglonnivelespecifico = getFirstRenglonNivel(unidadobra.getRenglonnivelespecificosById().parallelStream().collect(Collectors.toList()));
                Renglonvariante renglonvariante = renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId();
                Unidadobra nivelespecifico = new Unidadobra();
                nivelespecifico.setCodigo(unidadobra.getCodigo() + "00000");
                nivelespecifico.setDescripcion(unidadobra.getDescripcion().trim());
                nivelespecifico.setCostoMaterial(unidadobra.getCostomaterial());
                nivelespecifico.setCostomano(unidadobra.getCostomano());
                nivelespecifico.setCostoequipo(unidadobra.getCostoequipo());
                nivelespecifico.setSalario(unidadobra.getSalario());
                nivelespecifico.setSalariocuc(0.0);
                nivelespecifico.setObraId(unidadobra.getObraId());
                nivelespecifico.setEmpresaconstructoraId(unidadobra.getEmpresaconstructoraId());
                nivelespecifico.setZonasId(unidadobra.getZonasId());
                nivelespecifico.setObjetosId(unidadobra.getObjetosId());
                nivelespecifico.setNivelId(unidadobra.getNivelId());
                nivelespecifico.setEspecialidadesId(unidadobra.getEspecialidadesId());
                nivelespecifico.setSubespecialidadesId(unidadobra.getSubespecialidadesId());
                nivelespecifico.setRenglonbase(renglonvariante.getCodigo());
                nivelespecifico.setUm(renglonvariante.getUm());
                double total = unidadobra.getCostomaterial() + unidadobra.getCostomano() + unidadobra.getCostoequipo();
                nivelespecifico.setCostototal(new BigDecimal(String.format("%.2f", total)).doubleValue());
                double unitario = total / renglonnivelespecifico.getCantidad();
                nivelespecifico.setCostounitario(new BigDecimal(String.format("%.2f", unitario)).doubleValue());
                nivelespecifico.setGrupoejecucionId(1);
                nivelespecifico.setIdResolucion(renglonvariante.getRs());
                nivelespecifico.setCantidad(renglonnivelespecifico.getCantidad());

                int idNivel = util.saveUnidadobra(nivelespecifico);

                if (unidadobra.getRenglonnivelespecificosById().size() > 0) {
                    createRenglonesNivelRelations(unidadobra, idNivel);
                }
                if (unidadobra.getBajoespecificacionrvsById().size() > 0) {
                    createBajoEspecificacionRenglonesNivel(unidadobra, idNivel);
                }
            } else {
                Unidadobra nivelespecifico = new Unidadobra();
                nivelespecifico.setCodigo(unidadobra.getCodigo() + "00000");
                nivelespecifico.setDescripcion(unidadobra.getDescripcion().trim());
                nivelespecifico.setCostoMaterial(unidadobra.getCostomaterial());
                nivelespecifico.setCostomano(unidadobra.getCostomano());
                nivelespecifico.setCostoequipo(unidadobra.getCostoequipo());
                nivelespecifico.setSalario(unidadobra.getSalario());
                nivelespecifico.setSalariocuc(0.0);
                nivelespecifico.setObraId(unidadobra.getObraId());
                nivelespecifico.setEmpresaconstructoraId(unidadobra.getEmpresaconstructoraId());
                nivelespecifico.setZonasId(unidadobra.getZonasId());
                nivelespecifico.setObjetosId(unidadobra.getObjetosId());
                nivelespecifico.setNivelId(unidadobra.getNivelId());
                nivelespecifico.setEspecialidadesId(unidadobra.getEspecialidadesId());
                nivelespecifico.setSubespecialidadesId(unidadobra.getSubespecialidadesId());
                nivelespecifico.setIdResolucion(unidadobra.getObraByObraId().getSalarioId());
                nivelespecifico.setCantidad(0);
                int idNivel = util.saveUnidadobra(nivelespecifico);
            }
        }
    }

    private void createBajoEspecificacionRenglonesNivel(Nivelespecifico unidadobra, int idNivel) {
        List<Bajoespecificacion> bajoespecificacionrvList = new ArrayList<>();
        for (Bajoespecificacionrv bajoespecificacion : unidadobra.getBajoespecificacionrvsById()) {
            Bajoespecificacion bajoespecificacionrv = new Bajoespecificacion();
            bajoespecificacionrv.setUnidadobraId(idNivel);
            bajoespecificacionrv.setRenglonvarianteId(0);
            bajoespecificacionrv.setTipo(bajoespecificacion.getTipo());
            bajoespecificacionrv.setCantidad(bajoespecificacion.getCantidad());
            bajoespecificacionrv.setCosto(bajoespecificacion.getCosto());
            bajoespecificacionrv.setIdSuministro(bajoespecificacion.getIdsuministro());
            bajoespecificacionrv.setSumrenglon(0);

            bajoespecificacionrvList.add(bajoespecificacionrv);

        }
        util.persistBajoespecificacion(bajoespecificacionrvList);
    }

    private void createRenglonesNivelRelations(Nivelespecifico unidadobra, int idNivel) {
        List<Unidadobrarenglon> renglonnivelespecificoList = new ArrayList<>();
        for (Renglonnivelespecifico unidadobrarenglon : unidadobra.getRenglonnivelespecificosById()) {
            Unidadobrarenglon rnivel = new Unidadobrarenglon();
            rnivel.setUnidadobraId(idNivel);
            rnivel.setRenglonvarianteId(unidadobrarenglon.getRenglonvarianteId());
            rnivel.setCantRv(unidadobrarenglon.getCantidad());
            rnivel.setConMat(unidadobrarenglon.getConmat());
            rnivel.setSalariomn(unidadobrarenglon.getSalario());
            rnivel.setSalariocuc(0.0);
            rnivel.setCostMat(unidadobrarenglon.getCostmaterial());
            rnivel.setCostEquip(unidadobrarenglon.getCostequipo());
            rnivel.setCostMano(unidadobrarenglon.getCostmano());
            renglonnivelespecificoList.add(rnivel);
        }
        util.persistUnidadesObraRenglones(renglonnivelespecificoList);
    }


}
