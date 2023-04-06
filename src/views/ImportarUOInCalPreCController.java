package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
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
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ImportarUOInCalPreCController implements Initializable {

    public ImportarUOInCalPreCController calPreCController;
    @FXML
    public TableView<UniddaObraToIMportView> tableUnidad;
    @FXML
    public TableColumn<UniddaObraToIMportView, JFXCheckBox> uoCode;
    @FXML
    public TableColumn<UniddaObraToIMportView, StringProperty> uodescrip;
    UnidadesObraObraController unidadesController;
    @FXML
    private Label label_title;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXComboBox<String> selectEmpresa;
    @FXML
    private JFXComboBox<String> selectZonas;
    @FXML
    private JFXComboBox<String> selectObjetos;
    @FXML
    private JFXComboBox<String> selectNivel;
    @FXML
    private JFXComboBox<String> selectEspecialidad;
    @FXML
    private JFXComboBox<String> selectSubespecialidad;
    @FXML
    private JFXComboBox<String> selectObra;
    @FXML
    private TextArea textAreaDescri;

    @FXML
    private TableView<UORVTableView> uorvTableViewsList;

    @FXML
    private TableColumn<UORVTableView, StringProperty> rvCode;

    @FXML
    private TableColumn<UORVTableView, DoubleProperty> rvCant;

    @FXML
    private TableColumn<UORVTableView, DoubleProperty> rvCosto;

    @FXML
    private TableView<BajoEspecificacionView> tableSuminist;

    @FXML
    private TableColumn<BajoEspecificacionView, StringProperty> sumCode;

    @FXML
    private TableColumn<BajoEspecificacionView, DoubleProperty> sumCant;

    @FXML
    private TableColumn<BajoEspecificacionView, DoubleProperty> sumCosto;

    @FXML
    private JFXCheckBox flagSuministros;

    @FXML
    private JFXCheckBox flagPlan;

    @FXML
    private JFXCheckBox flagCertificacion;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private TextArea textUODesc;


    private ObservableList<String> obrasList;
    private List<Obra> listObras;
    private ObservableList<String> empresaList;
    private List<Empresaconstructora> listEmpresa;
    private ObservableList<String> zonaList;
    private List<Zonas> listZonas;
    private ObservableList<String> objetosList;
    private List<Objetos> listObjetos;
    private ObservableList<String> nivelesList;
    private List<Nivel> nivelList;
    private ObservableList<String> especialidadList;
    private List<Especialidades> listEspecialidades;
    private ObservableList<String> subespecialidadList;
    private List<Subespecialidades> listSubespecialidades;

    private List<Unidadobra> unidadobraList;
    private ObservableList<UniddaObraToIMportView> uniddaObraViewObservableList;

    private int idObra;
    private int idEmpresa;
    private int idZona;
    private int idObjeto;
    private int idNivel;
    private int idEspecialidad;
    private int idSubespecialidad;
    private ObservableList<UORVTableView> tableViewObservableList;
    private List<Unidadobrarenglon> unidadobrarenglonArrayList;
    private ArrayList<Bajoespecificacion> bajoespecificacionArrayList;
    private ObservableList<BajoEspecificacionView> bajoEspecificacionViewObservableList;
    private int batchSizeUOR = 10;
    private int countUOR;
    private int batchbajo = 10;
    private int countbajo;
    private int batchPlanrec = 20;
    private int countplan;
    private ModelsImportViewSingelton modelsImportViewSingelton;
    private ObservableList<UniddaObraToIMportView> uniddaObraViews;

    public ObservableList<String> getObras() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            obrasList = FXCollections.observableArrayList();
            listObras = session.createQuery("FROM Obra WHERE tipo='UO' ").getResultList();
            obrasList.addAll(listObras.parallelStream().map(obra -> obra.getCodigo() + " - " + obra.getDescripion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return obrasList;
        } catch (Exception he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(he.toString());
            alert.showAndWait();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getEspecialidadList() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            especialidadList = FXCollections.observableArrayList();
            listEspecialidades = session.createQuery("FROM Especialidades ").getResultList();
            especialidadList.addAll(listEspecialidades.parallelStream().map(especialidades -> especialidades.getCodigo() + " - " + especialidades.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return especialidadList;
        } catch (Exception he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(he.toString());
            alert.showAndWait();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getEmpresaList(Obra obra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaList = FXCollections.observableArrayList();
            listEmpresa = new ArrayList<>();
            List<Empresaobra> empresaobraList = session.createQuery("FROM Empresaobra WHERE obraId =: idO ").setParameter("idO", obra.getId()).getResultList();
            for (Empresaobra empresaobra : empresaobraList) {
                listEmpresa.add(empresaobra.getEmpresaconstructoraByEmpresaconstructoraId());
            }
            listEmpresa.size();
            empresaList.addAll(listEmpresa.parallelStream().map(empresaconstructora -> empresaconstructora.getCodigo() + " - " + empresaconstructora.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return empresaList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getZonasList(Obra obra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            zonaList = FXCollections.observableArrayList();
            listZonas = new ArrayList<>();

            Obra obra1 = session.get(Obra.class, obra.getId());
            obra1.getZonasById().size();
            listZonas.addAll(obra1.getZonasById());
            zonaList.addAll(listZonas.parallelStream().map(zonas -> zonas.getCodigo() + " - " + zonas.getDesripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return zonaList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getObjetosList(Zonas zona) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            objetosList = FXCollections.observableArrayList();
            listObjetos = new ArrayList<>();

            Zonas zonas1 = session.get(Zonas.class, zona.getId());
            zonas1.getObjetosById();
            listObjetos.addAll(zonas1.getObjetosById());
            objetosList.addAll(listObjetos.parallelStream().map(objetos -> objetos.getCodigo() + " - " + objetos.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return objetosList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getNivelList(Objetos objetos) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            nivelesList = FXCollections.observableArrayList();
            nivelList = new ArrayList<>();

            Objetos objetos1 = session.get(Objetos.class, objetos.getId());
            objetos1.getNivelsById();
            nivelList.addAll(objetos1.getNivelsById());
            nivelesList.addAll(nivelList.parallelStream().map(nivel -> nivel.getCodigo() + " - " + nivel.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return nivelesList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getSubespecialidadList(Especialidades especialidades) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            subespecialidadList = FXCollections.observableArrayList();
            listSubespecialidades = new ArrayList<>();

            Especialidades especialidades1 = session.get(Especialidades.class, especialidades.getId());
            especialidades1.getSubespecialidadesById().size();
            listSubespecialidades.addAll(especialidades1.getSubespecialidadesById());
            subespecialidadList.addAll(listSubespecialidades.parallelStream().map(subespecialidades -> subespecialidades.getCodigo() + " - " + subespecialidades.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return subespecialidadList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<UniddaObraToIMportView> getUniddaObraViewObservableList(int idObra, int idEmp, int idZonas, int idObjetos, int idNivel, int idEsp, int idSub) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            uniddaObraViewObservableList = FXCollections.observableArrayList();
            unidadobraList = new ArrayList<>();

            unidadobraList = session.createQuery(" FROM Unidadobra WHERE obraId=:idO AND empresaconstructoraId=:idEmp AND zonasId=:idZ AND objetosId=:idOb AND nivelId=:nivId AND especialidadesId=:idEsp AND subespecialidadesId=:idSub").setParameter("idO", idObra).setParameter("idEmp", idEmp).setParameter("idZ", idZonas).setParameter("idOb", idObjetos).setParameter("nivId", idNivel).setParameter("idEsp", idEsp).setParameter("idSub", idSub).getResultList();
            for (Unidadobra unidadobra : unidadobraList) {
                JFXCheckBox jfxCheckBox = new JFXCheckBox(unidadobra.getCodigo());
                uniddaObraViewObservableList.add(new UniddaObraToIMportView(unidadobra.getId(), jfxCheckBox, unidadobra.getDescripcion(), unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId()));
            }


            tx.commit();
            session.close();
            return uniddaObraViewObservableList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<UORVTableView> getUoRVRelation(UniddaObraToIMportView unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            tableViewObservableList = FXCollections.observableArrayList();
            unidadobrarenglonArrayList = (List<Unidadobrarenglon>) session.get(Unidadobra.class, unidadobra.getId()).getUnidadobrarenglonsById();
            tableViewObservableList.addAll(unidadobrarenglonArrayList.parallelStream().map(renglones -> {
                UORVTableView table = new UORVTableView(renglones.getRenglonvarianteId(), renglones.getRenglonvarianteByRenglonvarianteId().getCodigo(), renglones.getCantRv(), renglones.getCostMano() + renglones.getCostEquip(), renglones.getRenglonvarianteByRenglonvarianteId().getDescripcion(), renglones.getRenglonvarianteByRenglonvarianteId().getPeso(), renglones.getRenglonvarianteByRenglonvarianteId().getUm(), renglones.getRenglonvarianteByRenglonvarianteId().getCostomat() + renglones.getRenglonvarianteByRenglonvarianteId().getCostmano() + renglones.getRenglonvarianteByRenglonvarianteId().getCostequip(), "1", renglones.getSalariomn(), renglones.getSalariocuc(), renglones.getConMat(), renglones.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getGrupoByGrupoId().getSobregrupoBySobregrupoId().getDescipcion(), renglones.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getGrupoByGrupoId().getDescripciong(), renglones.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getDescripcionsub());
                return table;
            }).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return tableViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }


        return FXCollections.emptyObservableList();
    }

    public ObservableList<BajoEspecificacionView> getSumBajoEspecificacion(int idUO) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            bajoEspecificacionViewObservableList = FXCollections.observableArrayList();
            bajoespecificacionArrayList = new ArrayList<>();
            bajoespecificacionArrayList = (ArrayList<Bajoespecificacion>) session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: id ").setParameter("id", idUO).getResultList();
            for (Bajoespecificacion item : bajoespecificacionArrayList) {
                if (item.getTipo().contains("1")) {
                    Recursos recursos = session.get(Recursos.class, item.getIdSuministro());
                    bajoEspecificacionViewObservableList.add(new BajoEspecificacionView(item.getId(), item.getUnidadobraId(), item.getIdSuministro(), item.getRenglonvarianteId(), recursos.getCodigo(), recursos.getDescripcion(), Math.round(recursos.getPreciomn() * 100d) / 100d, recursos.getUm(), Math.round(recursos.getPeso() * 100d) / 100d, item.getCantidad(), Math.round(item.getCosto() * 1000d) / 1000d, item.getTipo()));
                } else if (item.getTipo().contains("J")) {
                    Juegoproducto recursos = session.get(Juegoproducto.class, item.getIdSuministro());
                    bajoEspecificacionViewObservableList.add(new BajoEspecificacionView(item.getId(), item.getUnidadobraId(), item.getIdSuministro(), item.getRenglonvarianteId(), recursos.getCodigo(), recursos.getDescripcion(), Math.round(recursos.getPreciomn() * 100d) / 100d, recursos.getUm(), Math.round(recursos.getPeso() * 100d) / 100d, item.getCantidad(), Math.round(item.getCosto() * 1000d) / 1000d, item.getTipo()));

                } else if (item.getTipo().contains("S")) {
                    Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, item.getIdSuministro());
                    bajoEspecificacionViewObservableList.add(new BajoEspecificacionView(item.getId(), item.getUnidadobraId(), item.getIdSuministro(), item.getRenglonvarianteId(), recursos.getCodigo(), recursos.getDescripcion(), Math.round(recursos.getPreciomn() * 100d) / 100d, recursos.getUm(), Math.round(recursos.getPeso() * 100d) / 100d, item.getCantidad(), Math.round(item.getCosto() * 1000d) / 1000d, item.getTipo()));
                }
            }

            tx.commit();
            session.close();
            return bajoEspecificacionViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public List<Unidadobrarenglon> getUnidadobrarenglonArrayList(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        List<Unidadobrarenglon> unidadobrarenglonList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Unidadobra unidadobra1 = session.get(Unidadobra.class, unidadobra.getId());
            unidadobra1.getUnidadobrarenglonsById().size();
            unidadobrarenglonList.addAll(unidadobra1.getUnidadobrarenglonsById());

            tx.commit();
            session.close();
            return unidadobrarenglonList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Planificacion> getUnidaPlanificacions(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        List<Planificacion> planificacionList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            planificacionList = session.createQuery("FROM Planificacion WHERE unidadobraId=: idUn").setParameter("idUn", unidadobra.getId()).getResultList();

            tx.commit();
            session.close();
            return planificacionList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Planrecuo> getPlanrecuoList(Unidadobra unidadobra, Planificacion plan) {
        Session session = ConnectionModel.createAppConnection().openSession();
        List<Planrecuo> planrecuoList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            planrecuoList = session.createQuery("FROM Planrecuo WHERE unidadobraId =: idUn AND planId =: idPl ").setParameter("idUn", unidadobra.getId()).setParameter("idPl", plan.getId()).getResultList();

            tx.commit();
            session.close();
            return planrecuoList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Certificacionrecuo> getCertificacionrecuoList(Unidadobra unidadobra, Certificacion certificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();
        List<Certificacionrecuo> certificacionrecuoList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            certificacionrecuoList = session.createQuery("FROM Certificacionrecuo WHERE unidadobraId =: idUn AND certificacionId =: idPl ").setParameter("idUn", unidadobra.getId()).setParameter("idPl", certificacion.getId()).getResultList();

            tx.commit();
            session.close();
            return certificacionrecuoList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Certificacion> getUnidaCertificacions(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        List<Certificacion> certificacionList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            certificacionList = session.createQuery(" FROM Certificacion WHERE unidadobraId=: idUn").setParameter("idUn", unidadobra.getId()).getResultList();

            tx.commit();
            session.close();
            return certificacionList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Bajoespecificacion> getBajoespecificacionList(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        List<Bajoespecificacion> list = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            list = session.createQuery(" FROM Bajoespecificacion WHERE unidadobraId =: idUn ").setParameter("idUn", unidadobra.getId()).getResultList();

            tx.commit();
            session.close();
            return list;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public int createUnidadesObra(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idUni = null;
        try {
            tx = session.beginTransaction();
            idUni = (Integer) session.save(unidadobra);
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return idUni;
    }

    public int createPlanificacion(Planificacion planificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idUni = null;
        try {
            tx = session.beginTransaction();

            idUni = (Integer) session.save(planificacion);

            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return idUni;
    }

    public int createCertificacionUO(Certificacion certificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idUni = null;
        try {
            tx = session.beginTransaction();
            idUni = (Integer) session.save(certificacion);

            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return idUni;
    }

    public void createUnidadesObraRenglones(List<Unidadobrarenglon> unidadobrarenglonArray) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Unidadobrarenglon unidadobrarenglon : unidadobrarenglonArray) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }
                session.persist(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createBajoespecificacion(List<Bajoespecificacion> bajoespecificacionList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countbajo = 0;
            for (Bajoespecificacion bajoespecificacion : bajoespecificacionList) {
                countbajo++;
                if (countbajo > 0 && countbajo % batchbajo == 0) {
                    session.flush();
                    session.clear();
                }
                session.persist(bajoespecificacion);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los Bajo Especificación");
            alert.showAndWait();
        }
    }

    public void createPlanrecuo(List<Planrecuo> planrecuoList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countplan = 0;
            for (Planrecuo planrecuo : planrecuoList) {
                countplan++;
                if (countplan > 0 && countplan % batchPlanrec == 0) {
                    session.flush();
                    session.clear();
                }
                session.persist(planrecuo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los Bajo Especificación");
            alert.showAndWait();
        }
    }

    public void createCertifrecuo(List<Certificacionrecuo> certificacionrecuos) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countplan = 0;

            for (Certificacionrecuo certificacionrecuo : certificacionrecuos) {
                countplan++;
                if (countplan > 0 && countplan % batchPlanrec == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(certificacionrecuo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los Bajo Especificación");
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        calPreCController = this;

        tableUnidad.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textUODesc.setText(tableUnidad.getSelectionModel().getSelectedItem().getDescripcion().trim());

                rvCode.setCellValueFactory(new PropertyValueFactory<UORVTableView, StringProperty>("codeRV"));
                rvCant.setCellValueFactory(new PropertyValueFactory<UORVTableView, DoubleProperty>("cant"));
                rvCosto.setCellValueFactory(new PropertyValueFactory<UORVTableView, DoubleProperty>("costoTotal"));
                ObservableList<UORVTableView> datosRV = FXCollections.observableArrayList();
                datosRV = getUoRVRelation(tableUnidad.getSelectionModel().getSelectedItem());
                uorvTableViewsList.getItems().setAll(datosRV);

                sumCode.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, StringProperty>("codigo"));
                sumCant.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, DoubleProperty>("cantidadBe"));
                sumCosto.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, DoubleProperty>("costoBe"));

                ObservableList<BajoEspecificacionView> datosBE = FXCollections.observableArrayList();
                datosBE = getSumBajoEspecificacion(tableUnidad.getSelectionModel().getSelectedItem().getId());

                tableSuminist.getItems().setAll(datosBE);
            }
        });

        uorvTableViewsList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            textAreaDescri.setText(uorvTableViewsList.getSelectionModel().getSelectedItem().getSobreG() + "\n" + uorvTableViewsList.getSelectionModel().getSelectedItem().getGrupo() + "\n" + uorvTableViewsList.getSelectionModel().getSelectedItem().getSubGrupo() + "\n" + uorvTableViewsList.getSelectionModel().getSelectedItem().getDescripcion());

        });

        tableSuminist.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            textAreaDescri.setText(tableSuminist.getSelectionModel().getSelectedItem().getDescripcion());
        });


    }

    public void cargarDatos(UnidadesObraObraController unidadesObraObraController, Obra obra, Empresaconstructora empresaconstructoraModel, int idZ, int id1O, int id2N, int id3E, int id4S) {
        selectObra.setItems(getObras());
        idObra = obra.getId();
        idEmpresa = empresaconstructoraModel.getId();
        idZona = idZ;
        idObjeto = id1O;
        idNivel = id2N;
        idEspecialidad = id3E;
        idSubespecialidad = id4S;
        unidadesController = unidadesObraObraController;
        modelsImportViewSingelton = ModelsImportViewSingelton.getInstance();

    }

    public void handleGetEmpresas(ActionEvent event) {
        String[] partObras = selectObra.getValue().split(" - ");
        Obra obra = listObras.parallelStream().filter(obra1 -> obra1.getCodigo().equals(partObras[0])).findFirst().orElse(null);
        selectEmpresa.setItems(getEmpresaList(obra));
        selectZonas.setItems(getZonasList(obra));
        selectEspecialidad.setItems(getEspecialidadList());
    }

    public void hendleGetObjetos(ActionEvent event) {
        String[] partZonas = selectZonas.getValue().split(" - ");
        Zonas zonas = listZonas.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);
        selectObjetos.setItems(getObjetosList(zonas));
    }

    public void handleGetNiveles(ActionEvent event) {
        String[] partObjetos = selectObjetos.getValue().split(" - ");
        Objetos objetos = listObjetos.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);
        selectNivel.setItems(getNivelList(objetos));
    }

    public void handleGetSubespecialidad(ActionEvent event) {
        String[] partEspecialidad = selectEspecialidad.getValue().split(" - ");
        Especialidades especialidades = listEspecialidades.parallelStream().filter(especialidades1 -> especialidades1.getCodigo().equals(partEspecialidad[0])).findFirst().orElse(null);
        selectSubespecialidad.setItems(getSubespecialidadList(especialidades));
    }

    public void handleGetUnidadesObra(ActionEvent event) {
        String[] partObras = selectObra.getValue().split(" - ");
        String[] partEmpresa = selectEmpresa.getValue().split(" - ");
        String[] partZonas = selectZonas.getValue().split(" - ");
        String[] partObjetos = selectObjetos.getValue().split(" - ");
        String[] partNiv = selectNivel.getValue().split(" - ");
        String[] partEspecialidad = selectEspecialidad.getValue().split(" - ");
        String[] partSubesp = selectSubespecialidad.getValue().split(" - ");

        Obra obra = listObras.parallelStream().filter(obra1 -> obra1.getCodigo().equals(partObras[0])).findFirst().orElse(null);
        Empresaconstructora empresa = listEmpresa.parallelStream().filter(empresaconstructora -> empresaconstructora.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);
        Zonas zonas = listZonas.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);
        Objetos objetos = listObjetos.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);
        Nivel nivel = nivelList.parallelStream().filter(nivel1 -> nivel1.getCodigo().equals(partNiv[0])).findFirst().orElse(null);
        Especialidades especialidades = listEspecialidades.parallelStream().filter(especialidades1 -> especialidades1.getCodigo().equals(partEspecialidad[0])).findFirst().orElse(null);
        Subespecialidades subespecialidades = listSubespecialidades.parallelStream().filter(subespecialidades1 -> subespecialidades1.getCodigo().equals(partSubesp[0])).findFirst().orElse(null);

        uoCode.setCellValueFactory(new PropertyValueFactory<UniddaObraToIMportView, JFXCheckBox>("codigo"));
        uodescrip.setCellValueFactory(new PropertyValueFactory<UniddaObraToIMportView, StringProperty>("descripcion"));

        uniddaObraViews = FXCollections.observableArrayList();
        uniddaObraViews = getUniddaObraViewObservableList(obra.getId(), empresa.getId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), subespecialidades.getId());
        tableUnidad.getItems().setAll(uniddaObraViews);
        tableUnidad.setEditable(true);

    }

    private Unidadobra getUnidadToImport(int idU) {
        return unidadobraList.parallelStream().filter(unidadobra -> unidadobra.getId() == idU).findFirst().orElse(null);
    }


    public void handleImportarAction(ActionEvent event) {
        List<UniddaObraToIMportView> selectedUnidad = tableUnidad.getItems().parallelStream().filter(uniddaObraToIMportView -> uniddaObraToIMportView.getCodigo().isSelected()).collect(Collectors.toList());

        for (UniddaObraToIMportView uniddaObraToIMportView : selectedUnidad) {
            if (!modelsImportViewSingelton.getUnidadesToCheckUO(idObra, idEmpresa, idZona, idObjeto, idNivel, idEspecialidad, idSubespecialidad, uniddaObraToIMportView.getCodigo().getText())) {

                Unidadobra unidad = getUnidadToImport(uniddaObraToIMportView.getId());
                List<Unidadobrarenglon> listRenglones = getUnidadobrarenglonArrayList(unidad);
                List<Bajoespecificacion> listBajoespecif = getBajoespecificacionList(unidad);

                Unidadobra unidadobra = confornUnidadObra(idObra, idEmpresa, idZona, idObjeto, idNivel, idEspecialidad, idSubespecialidad, unidad, uniddaObraToIMportView.getCodigo().getText());
                Integer idUni = createUnidadesObra(unidadobra);

                List<Unidadobrarenglon> unidadobrarenglonList = new ArrayList<>();
                for (Unidadobrarenglon listRenglone : listRenglones) {
                    unidadobrarenglonList.add(conformUnidadObraRenglon(idUni, listRenglone));
                }

                createUnidadesObraRenglones(unidadobrarenglonList);

                if (flagSuministros.isSelected() == false) {
                    List<Bajoespecificacion> bajoespecificacionList = new ArrayList<>();
                    for (Bajoespecificacion bajoespecificacion : listBajoespecif) {
                        bajoespecificacionList.add(conformBajoEspecificacion(idUni, bajoespecificacion));
                    }
                    createBajoespecificacion(bajoespecificacionList);
                }

                if (flagPlan.isSelected() == true) {
                    List<Planificacion> planificacions = getUnidaPlanificacions(unidad);
                    for (Planificacion planificacion : planificacions) {
                        List<Planrecuo> planrecuoList = getPlanrecuoList(unidad, planificacion);
                        Integer idPlan = createPlanificacion(confortPlanificacion(idUni, planificacion));

                        List<Planrecuo> listPlanrec = new ArrayList<>();
                        for (Planrecuo planrecuo : planrecuoList) {
                            listPlanrec.add(conformPlarecuo(idUni, idPlan, planrecuo));
                        }
                        createPlanrecuo(listPlanrec);
                    }
                }

                if (flagCertificacion.isSelected() == true) {
                    List<Certificacion> certificacions = getUnidaCertificacions(unidad);
                    for (Certificacion certificacion : certificacions) {
                        List<Certificacionrecuo> certificacionrecuoList = getCertificacionrecuoList(unidadobra, certificacion);
                        Integer idCert = createCertificacionUO(conformCertificacion(idUni, certificacion));

                        List<Certificacionrecuo> listCertificacionrecuos = new ArrayList<>();
                        for (Certificacionrecuo certificacionrecuo : certificacionrecuoList) {
                            listCertificacionrecuos.add(conformCertificacionrecuo(idUni, idCert, certificacionrecuo));
                        }
                        createCertifrecuo(listCertificacionrecuos);
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(uniddaObraToIMportView.getCodigo().getText() + " ya existe en el destino especificado!");
                alert.showAndWait();
            }
        }
        unidadesController.handleCargardatos(event);
    }

    private Certificacion conformCertificacion(Integer idUni, Certificacion planificacion) {
        Certificacion planificacion1 = new Certificacion();
        planificacion1.setUnidadobraId(idUni);
        planificacion1.setBrigadaconstruccionId(planificacion.getBrigadaconstruccionId());
        planificacion1.setGrupoconstruccionId(planificacion.getGrupoconstruccionId());
        planificacion1.setCuadrillaconstruccionId(planificacion.getCuadrillaconstruccionId());
        planificacion1.setDesde(planificacion.getDesde());
        planificacion1.setHasta(planificacion.getHasta());
        planificacion1.setCantidad(planificacion.getCantidad());
        planificacion1.setCostmaterial(planificacion.getCostmaterial());
        planificacion1.setCostmano(planificacion.getCostmano());
        planificacion1.setCostequipo(planificacion.getCostequipo());
        planificacion1.setSalario(planificacion.getSalario());
        planificacion1.setCucsalario(planificacion.getCucsalario());
        return planificacion1;
    }

    private Planrecuo conformPlarecuo(Integer idUni, Integer idPlan, Planrecuo planrecuo) {
        Planrecuo planrecuo1 = new Planrecuo();
        planrecuo1.setUnidadobraId(idUni);
        planrecuo1.setRenglonId(planrecuo.getRenglonId());
        planrecuo1.setRecursoId(planrecuo.getRecursoId());
        planrecuo1.setCantidad(planrecuo.getCantidad());
        planrecuo1.setCosto(planrecuo.getCosto());
        planrecuo1.setPlanId(idPlan);
        planrecuo1.setTipo(planrecuo.getTipo());
        planrecuo1.setFini(planrecuo.getFini());
        planrecuo1.setFfin(planrecuo.getFfin());
        planrecuo1.setCmateriales(planrecuo.getCmateriales());
        planrecuo1.setCmano(planrecuo.getCmano());
        planrecuo1.setCequipo(planrecuo.getCequipo());
        planrecuo1.setSalario(planrecuo.getSalario());
        planrecuo1.setSalariocuc(planrecuo.getSalariocuc());
        return planrecuo1;
    }

    private Certificacionrecuo conformCertificacionrecuo(Integer idUni, Integer idPlan, Certificacionrecuo
            planrecuo) {
        Certificacionrecuo planrecuo1 = new Certificacionrecuo();
        planrecuo1.setUnidadobraId(idUni);
        planrecuo1.setRenglonId(planrecuo.getRenglonId());
        planrecuo1.setRecursoId(planrecuo.getRecursoId());
        planrecuo1.setCantidad(planrecuo.getCantidad());
        planrecuo1.setCosto(planrecuo.getCosto());
        planrecuo1.setCertificacionId(idPlan);
        planrecuo1.setTipo(planrecuo.getTipo());
        planrecuo1.setFini(planrecuo.getFini());
        planrecuo1.setFfin(planrecuo.getFfin());
        planrecuo1.setCmateriales(planrecuo.getCmateriales());
        planrecuo1.setCmano(planrecuo.getCmano());
        planrecuo1.setCequipo(planrecuo.getCequipo());
        planrecuo1.setSalario(planrecuo.getSalario());
        planrecuo1.setSalariocuc(planrecuo.getSalariocuc());
        return planrecuo1;
    }

    private Unidadobra confornUnidadObra(int idOb, int idEm, int idZo, int idObj, int idNi, int idEs,
                                         int idSu, Unidadobra unidad, String code) {

        Unidadobra newUnidad = new Unidadobra();
        newUnidad.setObraId(idOb);
        newUnidad.setEmpresaconstructoraId(idEm);
        newUnidad.setZonasId(idZo);
        newUnidad.setObjetosId(idObj);
        newUnidad.setNivelId(idNi);
        newUnidad.setEspecialidadesId(idEs);
        newUnidad.setSubespecialidadesId(idSu);
        newUnidad.setCodigo(code);
        newUnidad.setDescripcion(unidad.getDescripcion());
        newUnidad.setUm(unidad.getUm());
        newUnidad.setCantidad(unidad.getCantidad());
        newUnidad.setCostototal(unidad.getCostototal());
        newUnidad.setCostoequipo(unidad.getCostoequipo());
        newUnidad.setCostomano(unidad.getCostomano());
        newUnidad.setCostoMaterial(unidad.getCostoMaterial());
        newUnidad.setCostounitario(unidad.getCostounitario());
        newUnidad.setRenglonbase(unidad.getRenglonbase());
        newUnidad.setSalario(unidad.getSalario());
        newUnidad.setSalariocuc(unidad.getSalariocuc());
        newUnidad.setGrupoejecucionId(unidad.getGrupoejecucionId());

        return newUnidad;
    }


    private Unidadobrarenglon conformUnidadObraRenglon(int idUni, Unidadobrarenglon listRenglone) {
        Unidadobrarenglon unidadobrarenglon = new Unidadobrarenglon();
        unidadobrarenglon.setUnidadobraId(idUni);
        unidadobrarenglon.setUnidadobraId(idUni);
        unidadobrarenglon.setRenglonvarianteId(listRenglone.getRenglonvarianteId());
        unidadobrarenglon.setRenglonvarianteId(listRenglone.getRenglonvarianteId());
        unidadobrarenglon.setCantRv(listRenglone.getCantRv());
        unidadobrarenglon.setCostMat(listRenglone.getCostMat());
        unidadobrarenglon.setCostMano(listRenglone.getCostMano());
        unidadobrarenglon.setCostEquip(listRenglone.getCostEquip());
        unidadobrarenglon.setConMat(listRenglone.getConMat());
        unidadobrarenglon.setSalariomn(listRenglone.getSalariomn());
        unidadobrarenglon.setSalariocuc(listRenglone.getSalariocuc());

        return unidadobrarenglon;
    }

    private Bajoespecificacion conformBajoEspecificacion(int idUni, Bajoespecificacion bajoespecificacion) {
        Bajoespecificacion bajoespecificacion1 = new Bajoespecificacion();
        bajoespecificacion1.setUnidadobraId(idUni);
        bajoespecificacion1.setRenglonvarianteId(0);
        bajoespecificacion1.setCantidad(bajoespecificacion.getCantidad());
        bajoespecificacion1.setCosto(bajoespecificacion.getCosto());
        bajoespecificacion1.setTipo(bajoespecificacion.getTipo());
        bajoespecificacion1.setSumrenglon(0);
        bajoespecificacion1.setIdSuministro(bajoespecificacion.getIdSuministro());
        return bajoespecificacion1;
    }

    private Planificacion confortPlanificacion(int idUni, Planificacion planificacion) {

        Planificacion planificacion1 = new Planificacion();
        planificacion1.setUnidadobraId(idUni);
        planificacion1.setBrigadaconstruccionId(planificacion.getBrigadaconstruccionId());
        planificacion1.setGrupoconstruccionId(planificacion.getGrupoconstruccionId());
        planificacion1.setCuadrillaconstruccionId(planificacion.getCuadrillaconstruccionId());
        planificacion1.setDesde(planificacion.getDesde());
        planificacion1.setHasta(planificacion.getHasta());
        planificacion1.setCantidad(planificacion.getCantidad());
        planificacion1.setCostomaterial(planificacion.getCostomaterial());
        planificacion1.setCostomano(planificacion.getCostomano());
        planificacion1.setCostoequipo(planificacion.getCostoequipo());
        planificacion1.setCostosalario(planificacion.getCostosalario());
        planificacion1.setCostsalariocuc(planificacion.getCostsalariocuc());
        return planificacion1;
    }

    public void handleCloseWindows(ActionEvent event) {
        Stage stage = (Stage) btn_add.getScene().getWindow();
        stage.close();
    }

    public void handleChangeCode(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("definirnewCode.fxml"));
            Parent proot = loader.load();
            definirCodeController controller = loader.getController();
            controller.cargarDatos(calPreCController, tableUnidad.getSelectionModel().getSelectedItem());

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

    public void updateResorces(UniddaObraToIMportView old, UniddaObraToIMportView newUnidad) {
        uniddaObraViews.remove(old);
        uniddaObraViews.add(newUnidad);
        tableUnidad.getItems().clear();
        tableUnidad.getItems().setAll(uniddaObraViews);
    }

}
