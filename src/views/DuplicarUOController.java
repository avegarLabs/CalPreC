package views;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
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

public class DuplicarUOController implements Initializable {

    List<Bajoespecificacion> newSuministrosBajo;
    UnidadesObraObraController unidadesObraObraController;
    @FXML
    private Label name;
    @FXML
    private Label labelname;
    @FXML
    private Label labelEmpresa;
    @FXML
    private Label labelZona;
    @FXML
    private Label labelObjeto;
    @FXML
    private Label labelNivel;
    @FXML
    private Label labelEspecialidad;
    @FXML
    private Label labelSubp;
    @FXML
    private JFXComboBox<String> comEmpresas;
    @FXML
    private JFXComboBox<String> comZonas;
    @FXML
    private JFXComboBox<String> comobjetos;
    @FXML
    private JFXComboBox<String> comsubespecialudad;
    @FXML
    private JFXComboBox<String> comNivel;
    @FXML
    private JFXComboBox<String> comespecialidad;

    @FXML
    private JFXComboBox<String> comboObras;

    @FXML
    private JFXCheckBox rvCheck;
    @FXML
    private JFXCheckBox suminisCheck;
    @FXML
    private JFXCheckBox certCheck;
    @FXML
    private JFXCheckBox planesCheck;
    private List<ObraImpModel> obraImpModelList;
    private ObservableList<String> listObras;

    private List<EmpImpModel> empImpModelList;
    private ObservableList<String> listEmpresa;

    private List<ZonaImpModel> zonaImpModelList;
    private ObservableList<String> zoStringObservableList;

    private List<ObjetoImpModel> objetoImpModelList;
    private ObservableList<String> objetosStringObservableList;

    private List<NivelImpModel> nivelImpModelList;
    private ObservableList<String> nivelStringObservableList;

    private List<EspecialidadImpModel> especialidadImpModelList;
    private ObservableList<String> especialidadesStrings;

    private List<SubespecialidadImpModel> subespecialidadImpModelList;
    private ObservableList<String> subStringObservableList;

    private int count;
    private int batchSize = 10;
    @FXML
    private JFXButton btn_close;
    private Integer idObra;
    private Integer certificacion;
    private Integer plan;
    private List<Unidadobrarenglon> newUnidadobrarenglonList;
    private ArrayList<Unidadobrarenglon> listRenglones;
    private ArrayList<Planificacion> listPlanes;
    private ArrayList<Certificacion> listCertificaciones;
    private ArrayList<Planrecuo> listRecursos;
    private ArrayList<Certificacionrecuo> listRecursosCertif;
    private ArrayList<Bajoespecificacion> listBajoespecificacionArrayList;
    private int inputLength = 7;

    private List<Unidadobra> unidadobraList;

    private ObraImpModel obraModel;
    private ZonaImpModel zonaImpModel;
    private EmpImpModel empImpModel;
    private ObjetoImpModel objetoImpModel;
    private NivelImpModel nivelImpModel;
    private EspecialidadImpModel especialidadImpModel;
    private SubespecialidadImpModel subModel;
    private List<Unidadobra> listUnidadobraList;
    private Unidadobra unidadobraModel;
    private ModelsImportViewSingelton modelsImportViewSingelton;
    private int idNivel;

    private int old;

    @FXML
    private JFXTextField code;

    @FXML
    private JFXTextArea descript;

    public void handleCargarEmpresa(ActionEvent event) {
        empImpModelList = new ArrayList<>();
        obraModel = obraImpModelList.parallelStream().filter(obraImpModel -> obraImpModel.getCode().trim().equals(comboObras.getValue().trim().trim())).findFirst().orElse(null);

        empImpModelList = modelsImportViewSingelton.getEmpresaList(obraModel.getId());
        listEmpresa = FXCollections.observableArrayList();
        listEmpresa.setAll(empImpModelList.parallelStream().map(empImpModel -> empImpModel.getCode()).collect(Collectors.toList()));
        comEmpresas.setItems(listEmpresa);

    }

    public void handleCargarZonas(ActionEvent event) {
        zonaImpModelList = new ArrayList<>();
        zonaImpModelList = modelsImportViewSingelton.getZonasList(obraModel.getId());
        zonaImpModelList.removeIf(zona -> zona.getCode().equals("Todas"));
        zoStringObservableList = FXCollections.observableArrayList();
        zoStringObservableList.setAll(zonaImpModelList.parallelStream().map(zonaImpModel1 -> zonaImpModel1.getCode()).collect(Collectors.toList()));
        comZonas.setItems(zoStringObservableList);
    }

    public void handleCargarObjetos(ActionEvent event) {
        objetoImpModelList = new ArrayList<>();
        zonaImpModel = zonaImpModelList.parallelStream().filter(zonaImpModel1 -> zonaImpModel1.getCode().equals(comZonas.getValue())).findFirst().orElse(null);
        objetoImpModelList = modelsImportViewSingelton.getObjetosList(zonaImpModel.getId());
        objetoImpModelList.removeIf(obj -> obj.getCode().equals("Todos"));

        objetosStringObservableList = FXCollections.observableArrayList();
        objetosStringObservableList.setAll(objetoImpModelList.parallelStream().map(objetoImpModel -> objetoImpModel.getCode()).collect(Collectors.toList()));

        comobjetos.setItems(objetosStringObservableList);
    }

    public void handleCargarNivel(ActionEvent event) {
        nivelImpModelList = new ArrayList<>();
        objetoImpModel = objetoImpModelList.parallelStream().filter(objetoImp -> objetoImp.getCode().equals(comobjetos.getValue())).findFirst().orElse(null);
        nivelImpModelList = modelsImportViewSingelton.getNivelList(objetoImpModel.getId());
        nivelImpModelList.removeIf(nivel -> nivel.getCode().equals("Todos"));

        nivelStringObservableList = FXCollections.observableArrayList();
        nivelStringObservableList.setAll(nivelImpModelList.parallelStream().map(objetoImpModel -> objetoImpModel.getCode()).collect(Collectors.toList()));
        comNivel.setItems(nivelStringObservableList);
    }

    public void handleCargarSubesp(ActionEvent event) {
        subespecialidadImpModelList = new ArrayList<>();
        especialidadImpModel = especialidadImpModelList.parallelStream().filter(objetoImp -> objetoImp.getCode().equals(comespecialidad.getValue())).findFirst().orElse(null);
        subespecialidadImpModelList = modelsImportViewSingelton.getSubespecialidadesObservableList(especialidadImpModel.getId());
        subespecialidadImpModelList.removeIf(sub -> sub.getCode().equals("Todas"));
        subStringObservableList = FXCollections.observableArrayList();
        subStringObservableList.setAll(subespecialidadImpModelList.parallelStream().map(objetoImpModel -> objetoImpModel.getCode()).collect(Collectors.toList()));
        comsubespecialudad.setItems(subStringObservableList);
    }

    public void getUOByCode(KeyEvent event) {
        if (code.getText().length() == 7) {
            Unidadobra uo = listUnidadobraList.parallelStream().filter(unidadobra -> unidadobra.getCodigo().trim().equals(code.getText().trim())).findFirst().orElse(null);
            if (uo == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(code.getText() + " inexistente, revise sus datos");
                alert.showAndWait();
            } else {
                unidadobraModel = null;
                unidadobraModel = uo;
                code.setText(" ");
                code.setText(uo.getCodigo());
                descript.setText(" ");
                descript.setText(uo.getDescripcion());
            }
        }


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modelsImportViewSingelton = ModelsImportViewSingelton.getInstance();
    }

    public void llenaLabels(UnidadesObraObraController controller, Unidadobra unidadobra, List<Unidadobra> list) {
        unidadesObraObraController = controller;
        unidadobraModel = unidadobra;
        labelname.setText(" UO" + unidadobra.getCodigo());

        listUnidadobraList = new ArrayList<>();
        listUnidadobraList = list;

        labelEmpresa.setText(unidadobra.getEmpresaconstructoraByEmpresaconstructoraId().getCodigo() + " - " + unidadobra.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
        labelZona.setText(unidadobra.getZonasByZonasId().getCodigo() + " - " + unidadobra.getZonasByZonasId().getDesripcion());
        labelObjeto.setText(unidadobra.getObjetosByObjetosId().getCodigo() + " - " + unidadobra.getObjetosByObjetosId().getDescripcion());
        labelNivel.setText(unidadobra.getNivelByNivelId().getCodigo() + " - " + unidadobra.getNivelByNivelId().getDescripcion());
        labelEspecialidad.setText(unidadobra.getEspecialidadesByEspecialidadesId().getCodigo() + " - " + unidadobra.getEspecialidadesByEspecialidadesId().getDescripcion());
        labelSubp.setText(unidadobra.getSubespecialidadesBySubespecialidadesId().getCodigo() + " - " + unidadobra.getSubespecialidadesBySubespecialidadesId().getDescripcion());

        idObra = unidadobra.getObraId();
        obraImpModelList = modelsImportViewSingelton.getObrasList();
        listObras = FXCollections.observableArrayList();
        listObras.setAll(obraImpModelList.parallelStream().map(obraImpModel -> obraImpModel.getCode()).collect(Collectors.toList()));
        comboObras.setItems(listObras);

        especialidadesStrings = FXCollections.observableArrayList();
        especialidadImpModelList = modelsImportViewSingelton.getEspecialidadesList();
        especialidadImpModelList.removeIf(esp -> esp.getCode().equals("Todas"));
        especialidadesStrings.setAll(especialidadImpModelList.parallelStream().map(especialidadImpModel -> especialidadImpModel.getCode()).collect(Collectors.toList()));
        comespecialidad.setItems(especialidadesStrings);

        old = 1;
        code.setText(unidadobra.getCodigo());
        descript.setText(unidadobra.getDescripcion());

    }

    public void handleDuplicateUnidad(ActionEvent event) {
        idNivel = nivelImpModelList.parallelStream().filter(nivelImpModel -> nivelImpModel.getCode().equals(comNivel.getValue())).map(NivelImpModel::getId).findFirst().orElse(0);
        empImpModel = empImpModelList.parallelStream().filter(emp -> emp.getCode().equals(comEmpresas.getValue())).findFirst().orElse(null);
        subModel = subespecialidadImpModelList.parallelStream().filter(sub -> sub.getCode().equals(comsubespecialudad.getValue())).findFirst().orElse(null);

        if (!modelsImportViewSingelton.getUnidadesToCheckUO(obraModel.getId(), empImpModel.getId(), zonaImpModel.getId(), objetoImpModel.getId(), idNivel, especialidadImpModel.getId(), subModel.getId(), code.getText())) {

            Unidadobra unidadobra = new Unidadobra();
            unidadobra.setCantidad(unidadobraModel.getCantidad());
            unidadobra.setCodigo(code.getText());
            unidadobra.setDescripcion(descript.getText());
            unidadobra.setUm(unidadobraModel.getUm());
            unidadobra.setCostoequipo(unidadobraModel.getCostoequipo());
            unidadobra.setCostoMaterial(unidadobraModel.getCostoMaterial());
            unidadobra.setCostomano(unidadobraModel.getCostomano());
            unidadobra.setSalario(unidadobraModel.getSalario());
            unidadobra.setSalariocuc(unidadobraModel.getSalariocuc());
            unidadobra.setRenglonbase(unidadobraModel.getRenglonbase());
            unidadobra.setCostounitario(unidadobraModel.getCostounitario());
            unidadobra.setCostototal(unidadobraModel.getCostototal());
            unidadobra.setObraId(obraModel.getId());
            unidadobra.setEmpresaconstructoraId(empImpModel.getId());
            unidadobra.setZonasId(zonaImpModel.getId());
            unidadobra.setObjetosId(objetoImpModel.getId());
            unidadobra.setNivelId(idNivel);
            unidadobra.setEspecialidadesId(especialidadImpModel.getId());
            unidadobra.setSubespecialidadesId(subModel.getId());
            if (unidadobraModel.getGrupoejecucionId() == null) {
                unidadobra.setGrupoejecucionId(1);
            } else {
                unidadobra.setGrupoejecucionId(unidadobraModel.getGrupoejecucionId());
            }

            unidadobra.setIdResolucion(unidadobraModel.getObraByObraId().getSalarioId());
            Integer idUnidad = addNewUnidad(unidadobra);


            if (rvCheck.isSelected() == true) {
                addUnidadObraRV(unidadobraModel, idUnidad);
            }

            if (suminisCheck.isSelected() == true) {
                addBajoSuministos(unidadobraModel.getId(), idUnidad);
            }

            if (planesCheck.isSelected() == true) {
                addPlanes(unidadobraModel, idUnidad);
            }

            if (certCheck.isSelected() == true) {
                addCertificaciones(unidadobraModel, idUnidad);
            }


            if (idUnidad != null) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(unidadobra.getCodigo().trim() + " creada satisfactoriamente!");
                alert.showAndWait();
            }

            code.clear();
            descript.clear();

            unidadesObraObraController.handleCargardatos(event);


        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(code.getText() + " ya existe en el destino especificado!");
            alert.showAndWait();
        }

    }

    private void addCertificaciones(Unidadobra unidadobraModel, Integer idUnidad) {

        listCertificaciones = new ArrayList<>();
        listCertificaciones = listCertificaiones(unidadobraModel.getId());
        listCertificaciones.forEach(items -> {
            listRecursosCertif = new ArrayList<>();
            listRecursosCertif = listCertficacionesUO(unidadobraModel.getId(), items.getId());

            items.setUnidadobraId(idUnidad);
            certificacion = saveCertificcaion(items);
            if (certificacion != null) {
                saverectoCertificacion(listRecursosCertif, idUnidad, certificacion);
            }

        });


    }

    private void addPlanes(Unidadobra unidadobraModel, Integer idUnidad) {

        listPlanes = new ArrayList<Planificacion>();
        listPlanes = listPlanificacion(unidadobraModel.getId());
        listPlanes.forEach(items -> {
            listRecursos = new ArrayList<>();
            listRecursos = listPlanificacionREC(unidadobraModel.getId(), items.getId());

            items.setUnidadobraId(idUnidad);

            plan = savePlan(items);

            if (plan != null) {
                saverectoPlan(listRecursos, idUnidad, plan);
            }

        });
    }

    private void saverectoPlan(ArrayList<Planrecuo> listRecursos, Integer idUnidad, Integer plan) {

        listRecursos.forEach(items -> {
            items.setUnidadobraId(idUnidad);
            items.setPlanId(plan);
            savePlanRec(items);
        });
    }

    private void saverectoCertificacion(ArrayList<Certificacionrecuo> listRecursos, Integer idUnidad, Integer plan) {

        listRecursos.forEach(items -> {
            items.setUnidadobraId(idUnidad);
            items.setCertificacionId(plan);
            saveCErtRec(items);
        });
    }

    private void saveCErtRec(Certificacionrecuo items) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            session.save(items);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private void savePlanRec(Planrecuo items) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            session.save(items);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private Integer savePlan(Planificacion items) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer idP = null;

        try {
            tx = session.beginTransaction();

            idP = (Integer) session.save(items);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return idP;

    }

    private Integer saveCertificcaion(Certificacion items) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer idP = null;

        try {
            tx = session.beginTransaction();

            idP = (Integer) session.save(items);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return idP;

    }

    private void addBajoSuministos(int id, Integer idUnidad) {

        listBajoespecificacionArrayList = new ArrayList<>();
        listBajoespecificacionArrayList = getMateriales(id);
        newSuministrosBajo = new ArrayList<>();
        for (Bajoespecificacion items : listBajoespecificacionArrayList) {
            items.setUnidadobraId(idUnidad);
            newSuministrosBajo.add(items);
        }

        saveMateriales(newSuministrosBajo);


    }

    private void saveMateriales(List<Bajoespecificacion> datosMateriales) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            count = 0;
            for (Bajoespecificacion bajo : datosMateriales) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.save(bajo);
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

    private void addUnidadObraRV(Unidadobra unidad, Integer id) {

        listRenglones = new ArrayList<>();
        listRenglones = listRenglogesUnidad(unidad.getId());
        newUnidadobrarenglonList = new ArrayList<>();

        for (Unidadobrarenglon items : listRenglones) {
            items.setUnidadobraId(id);
            newUnidadobrarenglonList.add(items);
        }

        saveUnidadObraRV(newUnidadobrarenglonList);


    }

    private void saveUnidadObraRV(List<Unidadobrarenglon> datosUnidadRenglon) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            count = 0;
            for (Unidadobrarenglon ur : datosUnidadRenglon) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(ur);
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

    private ArrayList<Unidadobrarenglon> listRenglogesUnidad(Integer idUnidad) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            listRenglones = new ArrayList<>();
            listRenglones = (ArrayList<Unidadobrarenglon>) session.createQuery("FROM Unidadobrarenglon WHERE unidadobraId =: idUn").setParameter("idUn", idUnidad).getResultList();
            tx.commit();
            session.close();
            return listRenglones;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();

    }

    private ArrayList<Planificacion> listPlanificacion(Integer idUnidad) {
        listPlanes = new ArrayList<>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            listPlanes = (ArrayList<Planificacion>) session.createQuery("FROM Planificacion WHERE unidadobraId =: idUn").setParameter("idUn", idUnidad).getResultList();

            tx.commit();
            session.close();
            return listPlanes;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();

    }

    private ArrayList<Certificacion> listCertificaiones(Integer idUnidad) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            listCertificaciones = new ArrayList<>();
            listCertificaciones = (ArrayList<Certificacion>) session.createQuery("FROM Certificacion WHERE unidadobraId =: idUn").setParameter("idUn", idUnidad).getResultList();

            tx.commit();
            session.close();
            return listCertificaciones;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private ArrayList<Planrecuo> listPlanificacionREC(Integer idUnidad, Integer idPlan) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            listRecursos = new ArrayList<>();
            listRecursos = (ArrayList<Planrecuo>) session.createQuery("FROM Planrecuo WHERE unidadobraId =: idUn AND planId =: idPkl").setParameter("idUn", idUnidad).setParameter("idPkl", idPlan).getResultList();

            tx.commit();
            session.close();
            return listRecursos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();

    }

    private ArrayList<Certificacionrecuo> listCertficacionesUO(Integer idUnidad, Integer idPlan) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            listRecursosCertif = new ArrayList<>();
            listRecursosCertif = (ArrayList<Certificacionrecuo>) session.createQuery("FROM Certificacionrecuo WHERE unidadobraId =: idUn AND certificacionId =: idPkl").setParameter("idUn", idUnidad).setParameter("idPkl", idPlan).getResultList();

            tx.commit();
            session.close();
            return listRecursosCertif;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();

    }

    private Integer addNewUnidad(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idU = null;
        try {
            tx = session.beginTransaction();

            idU = (Integer) session.save(unidadobra);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return idU;

    }


    public ArrayList<Bajoespecificacion> getMateriales(Integer idUnidad) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listBajoespecificacionArrayList = new ArrayList<>();
            listBajoespecificacionArrayList = (ArrayList<Bajoespecificacion>) session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idUn").setParameter("idUn", idUnidad).getResultList();

            tx.commit();
            session.close();
            return listBajoespecificacionArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return new ArrayList<>();

    }


    private int getIndex(String cod, int val) {
        int index = 0;
        for (int i = 0; i < listUnidadobraList.size() - 1; i++) {
            if (listUnidadobraList.get(i).getCodigo().equals(code.getText()) && val != listUnidadobraList.size()) {
                index = i;
            }
        }
        return index;
    }


    public void handleNextZonas(ActionEvent event) {
        int ind = getIndex(code.getText(), old);
        unidadobraModel = null;
        unidadobraModel = listUnidadobraList.get(ind + 1);

        code.clear();
        descript.clear();
        code.setText(unidadobraModel.getCodigo());
        descript.setText(unidadobraModel.getDescripcion());

        old = ind;
    }


    public void handleCloseWindows(ActionEvent event) {

        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

}
