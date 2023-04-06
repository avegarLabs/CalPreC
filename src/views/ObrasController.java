package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;

import java.net.URL;
import java.util.*;

public class ObrasController implements Initializable {
    public ObrasController obrasController;
    @FXML
    private Label label_title;
    @FXML
    private JFXButton btn_add;
    @FXML
    private Pane content_paneOb;
    @FXML
    private TableView<Obra> dataTableObra;
    @FXML
    private TableColumn<Obra, String> code;
    @FXML
    private TableColumn<Obra, String> descrip;
    @FXML
    private BorderPane enterPane;
    private ArrayList<Obra> obraArrayList;
    private ObservableList<Obra> obrasViewObservableList;
    private ObrasView obrasView;
    @FXML
    private JFXTextField filter;

    private Runtime garbache;

    private UsuariosDAO usuariosDAO;
    private UtilCalcSingelton utilCalcSingelton;

    @FXML
    private ContextMenu obraMenu;

    /**
     * Bloques para las funciones de trabajo con la base de datos
     */
    private Statistics statistics;
    private Obra obratemp;
    private UtilCalcSingelton postreSqlOperation = UtilCalcSingelton.getInstance();

    public ObservableList<Obra> getPresupustoObras() {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            obrasViewObservableList = FXCollections.observableArrayList();
            obraArrayList = new ArrayList<Obra>();
            obraArrayList = (ArrayList<Obra>) session.createQuery("FROM Obra WHERE tipo = 'UO'").getResultList();
            obrasViewObservableList.addAll(obraArrayList);
            tx.commit();
            session.close();
            return obrasViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList();

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

            String queryStr = "select bajo.id, bajo.unidadobra_id from Bajoespecificacion bajo inner join Unidadobra uo on bajo.unidadobra_id = uo.id WHERE uo.obra__id =" + id;
            List<Object[]> dataList = session.createSQLQuery(queryStr).getResultList();
            for (Object[] row : dataList) {
                int op1 = session.createQuery("Delete from Bajoespecificacion WHERE id=:idS").setParameter("idS", Integer.parseInt(row[0].toString())).executeUpdate();
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

    private Obra getObra(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            obratemp = null;
            obratemp = session.get(Obra.class, id);
            tx.commit();
            session.close();
            return obratemp;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return obratemp;
    }

    private void deleteAllCompontes(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Unidadobra> unidadobras = session.createQuery("FROM Unidadobra WHERE obraId =: id").setParameter("id", id).getResultList();
            for (Unidadobra uo : unidadobras) {
                int op1 = session.createQuery("DELETE FROM Unidadobrarenglon WHERE unidadobraId =: idU").setParameter("idU", uo.getId()).executeUpdate();
                int op2 = session.createQuery("DELETE FROM Planificacion WHERE unidadobraId =: idU").setParameter("idU", uo.getId()).executeUpdate();
                int op3 = session.createQuery("DELETE FROM Planrecuo WHERE unidadobraId =: idU").setParameter("idU", uo.getId()).executeUpdate();
                int op4 = session.createQuery("DELETE FROM Certificacion WHERE unidadobraId =: idU").setParameter("idU", uo.getId()).executeUpdate();
                int op5 = session.createQuery("DELETE FROM Certificacionrecuo WHERE unidadobraId =: idU").setParameter("idU", uo.getId()).executeUpdate();

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
        code.setCellValueFactory(new PropertyValueFactory<Obra, String>("codigo"));
        descrip.setCellValueFactory(new PropertyValueFactory<Obra, String>("descripion"));
        obrasViewObservableList = FXCollections.observableArrayList();
        obrasViewObservableList = getPresupustoObras();

        FilteredList<Obra> filteredData = new FilteredList<Obra>(obrasViewObservableList, p -> true);

        SortedList<Obra> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTableObra.comparatorProperty());

        dataTableObra.setItems(sortedData);

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
        utilCalcSingelton = UtilCalcSingelton.getInstance();
        if (usuariosDAO.usuario.getGruposId() == 1) {
            btn_add.setDisable(false);
        } else if (usuariosDAO.usuario.getGruposId() == 2) {
            btn_add.setDisable(false);
        } else if (usuariosDAO.usuario.getGruposId() == 6) {
            for (MenuItem item : obraMenu.getItems()) {
                item.setDisable(true);
            }
        }

        loadData();
        obrasController = this;
        dataTableObra.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && dataTableObra.getSelectionModel() != null) {
                Obra obra = dataTableObra.getSelectionModel().getSelectedItem();
                utilCalcSingelton.cleanUOinObra(obra);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("crearUnidadesdeObraenObras.fxml"));
                    Parent proot = loader.load();
                    UnidadesObraObraController controller = loader.getController();
                    controller.definirObra(obra);
                    content_paneOb.getChildren().setAll(proot);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });


    }


    public void handleNuevaObraForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevaObra.fxml"));
            Parent proot = loader.load();
            NuevaObraController controller = loader.getController();
            controller.passController(obrasController, "UO");


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


            stage.setOnCloseRequest(event1 -> {
                loadData();
                garbache = Runtime.getRuntime();
                garbache.gc();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    /**
     * Como mostrar datos sin llamar a otra ventana
     */
    public void handleEditObra(ActionEvent event) {

        Obra obra = dataTableObra.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarObra.fxml"));
            Parent proot = loader.load();
            ActualizarObraController controller = loader.getController();
            controller.pasarDatosObra(obra);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.toFront();
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadData();

            });

        } catch (Exception ex) {
            ex.printStackTrace();


        }

    }

    public void handleDeleteObra(ActionEvent event) {
        Obra obra = dataTableObra.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(" Esta seguro de eliminar la obra : " + obra.getCodigo() + " - " + obra.getDescripion());

        ButtonType buttonAgregar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonAgregar) {
            deleteObras(obra.getId());
            alert.setHeaderText("Información");
            alert.setContentText("Obra: " + obra.getCodigo() + " eliminada satisfactoriamente!");
            alert.showAndWait();
        }
        loadData();
    }

    public void handleDuplicateObraForm(ActionEvent event) {

        Obra obra = getObra(dataTableObra.getSelectionModel().getSelectedItem().getId());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DuplicarObra.fxml"));
            Parent proot = loader.load();
            DuplicarObraController controller = loader.getController();
            controller.cargarObra(obrasController, obra);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


            stage.setOnCloseRequest(event1 -> {
                loadData();
                garbache = Runtime.getRuntime();
                garbache.gc();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void handleRecalcular(ActionEvent event) {
        Obra obra = getObra(dataTableObra.getSelectionModel().getSelectedItem().getId());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("recalcularObra.fxml"));
            Parent proot = loader.load();
            RecalculraController controller = (RecalculraController) loader.getController();
            controller.pasarDatos(obra);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadData();
                garbache = Runtime.getRuntime();
                garbache.gc();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void handleSuplemantaObra(ActionEvent event) {

        Obra obra = getObra(dataTableObra.getSelectionModel().getSelectedItem().getId());

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
                garbache = Runtime.getRuntime();
                garbache.gc();
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

    public void handleCreateObraRC(ActionEvent event) {

        Obra obra = dataTableObra.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(" Esta seguro que desea convertir el presupuesto: " + obra.getCodigo() + " - " + obra.getDescripion() + " a renglón constructivo, Por favor espere, CalPreC informará cuando termine el proceso o haya ocurrido un error!");
        ButtonType buttonAgregar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonAgregar) {
            obra.setTipo("RV");
            createNivelEspecifico(obra.getUnidadobrasById());
            updateObras(obra);
            deleteAllCompontes(obra.getId());

            alert.setHeaderText("Información");
            alert.setContentText("El presupuesto : " + obra.getCodigo() + " ha sido convertido, los recursos bajo especificación se creados fueron asignados al correspondiente renglón base de cada unidad de obra ");
            alert.showAndWait();
        }
        loadData();
    }

    private void createNivelEspecifico(Collection<Unidadobra> unidadobraCollection) {
        for (Unidadobra unidadobra : unidadobraCollection) {
            Nivelespecifico nivelespecifico = new Nivelespecifico();
            nivelespecifico.setCodigo(unidadobra.getCodigo().trim().substring(4, 6));
            nivelespecifico.setDescripcion(unidadobra.getDescripcion().trim());
            nivelespecifico.setCostomaterial(unidadobra.getCostoMaterial());
            nivelespecifico.setCostomano(unidadobra.getCostomano());
            nivelespecifico.setCostoequipo(unidadobra.getCostoequipo());
            nivelespecifico.setSalario(unidadobra.getSalario());
            nivelespecifico.setObraId(unidadobra.getObraId());
            nivelespecifico.setEmpresaconstructoraId(unidadobra.getEmpresaconstructoraId());
            nivelespecifico.setZonasId(unidadobra.getZonasId());
            nivelespecifico.setObjetosId(unidadobra.getObjetosId());
            nivelespecifico.setNivelId(unidadobra.getNivelId());
            nivelespecifico.setEspecialidadesId(unidadobra.getEspecialidadesId());
            nivelespecifico.setSubespecialidadesId(unidadobra.getSubespecialidadesId());

            int idNivel = postreSqlOperation.saveNivelEspecifico(nivelespecifico);

            if (unidadobra.getUnidadobrarenglonsById().size() > 0) {
                createRenglonesNivelRelations(unidadobra, idNivel);
            }
            if (unidadobra.getUnidadobrabajoespecificacionById().size() > 0) {
                createBajoEspecificacionRenglonesNivel(unidadobra, idNivel);
            }
        }
    }

    private void createBajoEspecificacionRenglonesNivel(Unidadobra unidadobra, int idNivel) {
        Renglonvariante renglonvariante = utilCalcSingelton.renglonvarianteList.parallelStream().filter(item -> item.getCodigo().trim().equals(unidadobra.getRenglonbase().trim()) && item.getRs() == unidadobra.getObraByObraId().getSalarioId()).findFirst().get();
        List<Bajoespecificacionrv> bajoespecificacionrvList = new ArrayList<>();
        for (Bajoespecificacion bajoespecificacion : unidadobra.getUnidadobrabajoespecificacionById()) {
            Bajoespecificacionrv bajoespecificacionrv = new Bajoespecificacionrv();
            bajoespecificacionrv.setNivelespecificoId(idNivel);
            bajoespecificacionrv.setRenglonvarianteId(renglonvariante.getId());
            bajoespecificacionrv.setTipo(bajoespecificacion.getTipo());
            bajoespecificacionrv.setCantidad(bajoespecificacion.getCantidad());
            bajoespecificacionrv.setCosto(bajoespecificacion.getCosto());
            bajoespecificacionrv.setIdsuministro(bajoespecificacion.getIdSuministro());
            bajoespecificacionrvList.add(bajoespecificacionrv);
        }

        utilCalcSingelton.persistBajoespecificacionRenglon(bajoespecificacionrvList);

    }

    private void createRenglonesNivelRelations(Unidadobra unidadobra, int idNivel) {
        List<Renglonnivelespecifico> renglonnivelespecificoList = new ArrayList<>();
        for (Unidadobrarenglon unidadobrarenglon : unidadobra.getUnidadobrarenglonsById()) {
            Renglonnivelespecifico rnivel = new Renglonnivelespecifico();
            rnivel.setNivelespecificoId(idNivel);
            rnivel.setRenglonvarianteId(unidadobrarenglon.getRenglonvarianteId());
            rnivel.setCantidad(unidadobrarenglon.getCantRv());
            rnivel.setConmat(unidadobrarenglon.getConMat());
            rnivel.setSalario(unidadobrarenglon.getSalariomn());
            rnivel.setSalariocuc(0.0);
            rnivel.setCostmaterial(unidadobrarenglon.getCostMat());
            rnivel.setCostequipo(unidadobrarenglon.getCostEquip());
            rnivel.setCostmano(unidadobrarenglon.getCostEquip());
            renglonnivelespecificoList.add(rnivel);
        }
        utilCalcSingelton.persistNivelRenglones(renglonnivelespecificoList);
    }

    public void handleExportToITE(ActionEvent event) {
        Obra obra = getObra(dataTableObra.getSelectionModel().getSelectedItem().getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DatosITES.fxml"));
            Parent proot = loader.load();
            DatosITESController controller = loader.getController();
            controller.pasarDatos(obra);
            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.setResizable(false);
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
