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

public class DuplicarNivelEspecificoController implements Initializable {

    List<Bajoespecificacionrv> newSuministrosBajo;
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
    private JFXTextField code;
    @FXML
    private JFXTextArea descript;
    @FXML
    private JFXCheckBox rvCheck;
    @FXML
    private JFXCheckBox suminisCheck;
    @FXML
    private JFXCheckBox certCheck;
    @FXML
    private JFXCheckBox planesCheck;
    private ObservableList<String> empresas;
    private ObservableList<String> zonas;
    private ObservableList<String> objetos;
    private ObservableList<String> niveles;
    private ObservableList<String> especialidades;
    private ObservableList<String> subespecialidades;
    private Empresaconstructora empresaconstructora;
    private Zonas zonasModel;
    private Objetos objetosModel;
    private Nivel nivelModel;
    private Especialidades especialidadesModel;
    private Subespecialidades subespecialidadesModel;
    private Nivelespecifico unidadobraModel;
    private Runtime garbache;
    private int count;
    private int batchSize = 10;
    @FXML
    private JFXButton btn_close;
    private Integer idObra;
    private Integer certificacion;
    private Integer plan;
    private List<Renglonnivelespecifico> newUnidadobrarenglonList;
    private ArrayList<Renglonnivelespecifico> listRenglones;
    private ArrayList<Planrenglonvariante> listPlanes;
    private ArrayList<CertificacionRenglonVariante> listCertificaciones;
    private ArrayList<Planrecrv> listRecursos;
    private ArrayList<Certificacionrecrv> listRecursosCertif;
    private ArrayList<Bajoespecificacionrv> listBajoespecificacionArrayList;
    private int inputLength = 7;
    private List<Nivelespecifico> unidadobraList;

    public ObservableList<String> getListEmpresas(Integer idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresas = FXCollections.observableArrayList();
            ArrayList<Empresaobra> empresaconstructoras = (ArrayList<Empresaobra>) session.createQuery("FROM Empresaobra where obraId = :id ").setParameter("id", idObra).getResultList();
            empresas.addAll(empresaconstructoras.parallelStream().map(empresaobra -> empresaobra.getEmpresaconstructoraByEmpresaconstructoraId().getCodigo() + " - " + empresaobra.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return empresas;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getListZonas(Integer idObra) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            zonas = FXCollections.observableArrayList();
            ArrayList<Zonas> datosZonas = new ArrayList<>();
            datosZonas = (ArrayList<Zonas>) session.createQuery("FROM Zonas where obraId = :id ").setParameter("id", idObra).getResultList();
            zonas.addAll(datosZonas.parallelStream().map(zon -> zon.getCodigo() + " - " + zon.getDesripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return zonas;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getListObjetos(String codeZona) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            objetos = FXCollections.observableArrayList();
            zonasModel = (Zonas) session.createQuery("FROM Zonas WHERE codigo =: code and obraId =: idO").setParameter("code", codeZona).setParameter("idO", idObra).getResultList().get(0);
            objetos.addAll(zonasModel.getObjetosById().parallelStream().map(item -> item.getCodigo() + " - " + item.getDescripcion()).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return objetos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getListNiveles(String codeObjeto) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            niveles = FXCollections.observableArrayList();
            objetosModel = (Objetos) session.createQuery("FROM Objetos WHERE codigo =: code and zonasId =: idZon").setParameter("code", codeObjeto).setParameter("idZon", zonasModel.getId()).getResultList().get(0);
            niveles.addAll(objetosModel.getNivelsById().parallelStream().map(items -> items.getCodigo() + " - " + items.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return niveles;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getListEspecialidades() {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            especialidades = FXCollections.observableArrayList();
            ArrayList<Especialidades> especialidadesArrayList = (ArrayList<Especialidades>) session.createQuery("FROM Especialidades ").getResultList();
            especialidades.addAll(especialidadesArrayList.parallelStream().map(items -> items.getCodigo() + " - " + items.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return especialidades;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList();
    }

    public ObservableList<String> getListSubespecialidades(String codeEspe) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            subespecialidades = FXCollections.observableArrayList();
            especialidadesModel = (Especialidades) session.createQuery("FROM Especialidades WHERE codigo =: code").setParameter("code", codeEspe).getResultList().get(0);
            subespecialidades.addAll(especialidadesModel.getSubespecialidadesById().parallelStream().map(items -> items.getCodigo() + " - " + items.getDescripcion()).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return subespecialidades;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public List<Nivelespecifico> getListNivel(int idO, int idemp, int idZ, int idOb, int idN, int idEs, int idSub) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadobraList = new ArrayList<>();
            unidadobraList = session.createQuery("FROM Nivelespecifico WHERE obraId=:obra and empresaconstructoraId =: em and zonasId=:zon and objetosId=:onj and nivelId=:ni and especialidadesId=:esp and subespecialidadesId=:sub").setParameter("obra", idO).setParameter("em", idemp).setParameter("zon", idZ).setParameter("onj", idOb).setParameter("ni", idN).setParameter("esp", idEs).setParameter("sub", idSub).getResultList();
            tx.commit();
            session.close();
            return unidadobraList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comespecialidad.setItems(getListEspecialidades());
    }

    public void llenaLabels(Nivelespecifico nivelespecifico) {

        unidadobraModel = nivelespecifico;

        labelname.setText(" Nivel: " + nivelespecifico.getCodigo());

        labelEmpresa.setText(nivelespecifico.getEmpresaconstructoraByEmpresaconstructoraId().getCodigo() + " - " + nivelespecifico.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
        labelZona.setText(nivelespecifico.getZonasByZonasId().getCodigo() + " - " + nivelespecifico.getZonasByZonasId().getDesripcion());
        labelObjeto.setText(nivelespecifico.getObjetosByObjetosId().getCodigo() + " - " + nivelespecifico.getObjetosByObjetosId().getDescripcion());
        labelNivel.setText(nivelespecifico.getNivelByNivelId().getCodigo() + " - " + nivelespecifico.getNivelByNivelId().getDescripcion());
        labelEspecialidad.setText(nivelespecifico.getEspecialidadesByEspecialidadesId().getCodigo() + " - " + nivelespecifico.getEspecialidadesByEspecialidadesId().getDescripcion());
        labelSubp.setText(nivelespecifico.getSubespecialidadesBySubespecialidadesId().getCodigo() + " - " + nivelespecifico.getSubespecialidadesBySubespecialidadesId().getDescripcion());

        idObra = nivelespecifico.getObraId();
        comEmpresas.setItems(getListEmpresas(nivelespecifico.getObraId()));

        code.setText(nivelespecifico.getCodigo());
        descript.setText(nivelespecifico.getDescripcion());


    }

    public void handleZonas(ActionEvent event) {

        comZonas.setItems(getListZonas(idObra));
    }

    public void handleObjetos(ActionEvent event) {

        String[] partZon = comZonas.getValue().split(" - ");
        comobjetos.setItems(getListObjetos(partZon[0]));
    }

    public void handleNiveles(ActionEvent event) {

        String[] partZon = comobjetos.getValue().split(" - ");
        comNivel.setItems(getListNiveles(partZon[0]));
    }

    public void handleSubespe(ActionEvent event) {

        String[] partZon = comespecialidad.getValue().split(" - ");
        comsubespecialudad.setItems(getListSubespecialidades(partZon[0]));
    }


    public void handleDuplicateUnidad(ActionEvent event) {

        String[] partEmp = comEmpresas.getValue().split(" - ");
        empresaconstructora = getEmpresa(partEmp[0]);

        String[] partNivel = comNivel.getValue().split(" - ");
        nivelModel = getNivel(partNivel[0]);

        String[] partSub = comsubespecialudad.getValue().split(" - ");
        subespecialidadesModel = getSubespecialidad(partSub[0]);

        if (getNivelEspecifico(idObra, empresaconstructora.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadesModel.getId(), code.getText()) == null) {

            Nivelespecifico unidadobra = new Nivelespecifico();
            unidadobra.setCodigo(code.getText());
            unidadobra.setDescripcion(descript.getText());
            unidadobra.setCostoequipo(unidadobraModel.getCostoequipo());
            unidadobra.setCostomaterial(unidadobraModel.getCostomaterial());
            unidadobra.setCostomano(unidadobraModel.getCostomano());
            unidadobra.setSalario(unidadobraModel.getSalario());
            unidadobra.setObraId(unidadobraModel.getObraId());
            unidadobra.setEmpresaconstructoraId(empresaconstructora.getId());
            unidadobra.setZonasId(zonasModel.getId());
            unidadobra.setObjetosId(objetosModel.getId());
            unidadobra.setNivelId(nivelModel.getId());
            unidadobra.setEspecialidadesId(especialidadesModel.getId());
            unidadobra.setSubespecialidadesId(subespecialidadesModel.getId());
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
                alert.setHeaderText(unidadobra.getDescripcion() + " creada satisfactoriamente!");
                alert.showAndWait();
            }

            code.clear();
            descript.clear();

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(code.getText() + " ya existe en el destino especificado!");
            alert.showAndWait();
        }
    }

    private void addCertificaciones(Nivelespecifico unidadobraModel, Integer idUnidad) {

        listCertificaciones = new ArrayList<>();
        listCertificaciones = listCertificaiones(unidadobraModel.getId());
        for (CertificacionRenglonVariante items : listCertificaciones) {
            listRecursosCertif = new ArrayList<>();
            listRecursosCertif = listCertficacionesUO(unidadobraModel.getId(), items.getId());

            items.setNivelespecificoId(idUnidad);
            certificacion = saveCertificcaion(items);
            if (certificacion != null) {
                saverectoCertificacion(listRecursosCertif, idUnidad, certificacion);
            }

        }
    }


    private void addPlanes(Nivelespecifico unidadobraModel, Integer idUnidad) {

        listPlanes = new ArrayList<Planrenglonvariante>();
        listPlanes = listPlanificacion(unidadobraModel.getId());
        for (Planrenglonvariante items : listPlanes) {

            listRecursos = new ArrayList<>();
            listRecursos = listPlanificacionREC(unidadobraModel.getId(), items.getId());

            items.setNivelespecificoId(idUnidad);

            plan = savePlan(items);

            if (plan != null) {
                saverectoPlan(listRecursos, idUnidad, plan);
            }

        }
    }

    private void saverectoPlan(ArrayList<Planrecrv> listRecursos, Integer idUnidad, Integer plan) {

        listRecursos.forEach(items -> {
            items.setNivelespId(idUnidad);
            items.setPlanId(plan);
            savePlanRec(items);
        });
    }

    private void saverectoCertificacion(ArrayList<Certificacionrecrv> listRecursos, Integer idUnidad, Integer plan) {

        listRecursos.forEach(items -> {
            items.setNivelespId(idUnidad);
            items.setCertificacionId(plan);
            saveCErtRec(items);
        });
    }

    private void saveCErtRec(Certificacionrecrv items) {

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

    private void savePlanRec(Planrecrv items) {

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

    private Integer savePlan(Planrenglonvariante items) {

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

    private Integer saveCertificcaion(CertificacionRenglonVariante items) {

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
        for (Bajoespecificacionrv items : listBajoespecificacionArrayList) {
            items.setNivelespecificoId(idUnidad);
            newSuministrosBajo.add(items);
        }

        saveMateriales(newSuministrosBajo);


    }

    private void saveMateriales(List<Bajoespecificacionrv> datosMateriales) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            count = 0;
            for (Bajoespecificacionrv bajo : datosMateriales) {
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

    private void addUnidadObraRV(Nivelespecifico unidad, Integer id) {

        listRenglones = new ArrayList<>();
        listRenglones = listRenglogesUnidad(unidad.getId());
        newUnidadobrarenglonList = new ArrayList<>();

        for (Renglonnivelespecifico items : listRenglones) {
            items.setNivelespecificoId(id);
            newUnidadobrarenglonList.add(items);
        }

        saveUnidadObraRV(newUnidadobrarenglonList);


    }

    private Nivelespecifico getNivelEspecifico(int idO, int idEm, int idZon, int idOb, int idNi, int idEs, int idSub, String code) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Nivelespecifico> datos = session.createQuery(" FROM Nivelespecifico WHERE obraId =: idOb AND empresaconstructoraId =: idEm AND zonasId =: idZo AND objetosId =: idObj AND nivelId =: idNi AND especialidadesId=: idEs AND subespecialidadesId =: idSu ").setParameter("idOb", idO).setParameter("idEm", idEm).setParameter("idZo", idZon).setParameter("idObj", idOb).setParameter("idNi", idNi).setParameter("idEs", idEs).setParameter("idSu", idSub).getResultList();
            Nivelespecifico nivelespecifico = datos.parallelStream().filter(nivel -> nivel.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
            tx.commit();
            session.close();
            return nivelespecifico;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Nivelespecifico();
    }

    private void saveUnidadObraRV(List<Renglonnivelespecifico> datosUnidadRenglon) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (Renglonnivelespecifico ur : datosUnidadRenglon) {
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

    private ArrayList<Renglonnivelespecifico> listRenglogesUnidad(Integer idUnidad) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            listRenglones = new ArrayList<>();
            listRenglones = (ArrayList<Renglonnivelespecifico>) session.createQuery("FROM Renglonnivelespecifico WHERE nivelespecificoId =: idUn").setParameter("idUn", idUnidad).getResultList();

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

    private ArrayList<Planrenglonvariante> listPlanificacion(Integer idUnidad) {
        listPlanes = new ArrayList<>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            listPlanes = (ArrayList<Planrenglonvariante>) session.createQuery("FROM Planrenglonvariante WHERE nivelespecificoId =: idUn").setParameter("idUn", idUnidad).getResultList();

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

    private ArrayList<CertificacionRenglonVariante> listCertificaiones(Integer idUnidad) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            listCertificaciones = new ArrayList<>();
            listCertificaciones = (ArrayList<CertificacionRenglonVariante>) session.createQuery("FROM CertificacionRenglonVariante WHERE  nivelespecificoId=: idUn").setParameter("idUn", idUnidad).getResultList();

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

    private ArrayList<Planrecrv> listPlanificacionREC(Integer idUnidad, Integer idPlan) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            listRecursos = new ArrayList<>();
            listRecursos = (ArrayList<Planrecrv>) session.createQuery("FROM Planrecrv WHERE nivelespId =: idUn AND planId =: idPkl").setParameter("idUn", idUnidad).setParameter("idPkl", idPlan).getResultList();

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

    private ArrayList<Certificacionrecrv> listCertficacionesUO(Integer idUnidad, Integer idPlan) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            listRecursosCertif = new ArrayList<>();
            listRecursosCertif = (ArrayList<Certificacionrecrv>) session.createQuery("FROM Certificacionrecrv WHERE nivelespId =: idUn AND certificacionId =: idPkl").setParameter("idUn", idUnidad).setParameter("idPkl", idPlan).getResultList();

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

    private Integer addNewUnidad(Nivelespecifico unidadobra) {

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

    public Empresaconstructora getEmpresa(String code) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            empresaconstructora = (Empresaconstructora) session.createQuery("FROM Empresaconstructora WHERE codigo =: code").setParameter("code", code).getResultList().get(0);
            tx.commit();
            session.close();
            return empresaconstructora;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return empresaconstructora;

    }

    public Nivel getNivel(String code) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            nivelModel = (Nivel) session.createQuery("FROM Nivel WHERE codigo =: code AND objetosId =:idOb ").setParameter("code", code).setParameter("idOb", objetosModel.getId()).getResultList().get(0);
            tx.commit();
            session.close();
            return nivelModel;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return nivelModel;

    }

    public Subespecialidades getSubespecialidad(String code) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            subespecialidadesModel = (Subespecialidades) session.createQuery("FROM Subespecialidades WHERE codigo =: code AND especialidadesId =: idE ").setParameter("code", code).setParameter("idE", especialidadesModel.getId()).getResultList().get(0);
            tx.commit();
            session.close();
            return subespecialidadesModel;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return subespecialidadesModel;

    }

    public ArrayList<Bajoespecificacionrv> getMateriales(Integer idUnidad) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listBajoespecificacionArrayList = new ArrayList<>();
            listBajoespecificacionArrayList = (ArrayList<Bajoespecificacionrv>) session.createQuery("FROM Bajoespecificacionrv WHERE nivelespecificoId =: idUn").setParameter("idUn", idUnidad).getResultList();

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

    public void keyTypeCode(KeyEvent event) {
        if (code.getText().length() == inputLength) {
            descript.requestFocus();
        }
    }


    public void handleCloseWindows(ActionEvent event) {

        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

}
