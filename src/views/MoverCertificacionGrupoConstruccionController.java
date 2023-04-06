package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MoverCertificacionGrupoConstruccionController implements Initializable {

    private static SessionFactory sf;
    @FXML
    private JFXComboBox<String> combo_grupo;
    @FXML
    private JFXButton btnClose;
    @FXML
    private JFXCheckBox checkPlan;
    @FXML
    private JFXCheckBox checkCertifocacion;
    @FXML
    private JFXCheckBox checkIntervalo;
    @FXML
    private JFXDatePicker dateIni;
    @FXML
    private JFXDatePicker dateFin;
    @FXML
    private JFXComboBox<String> combo_obras;
    @FXML
    private JFXComboBox<String> combo_brigada;
    private ArrayList<Brigadaconstruccion> brigadaconstruccionArrayList;
    private ArrayList<Grupoconstruccion> grupoconstruccionArrayList;
    private ArrayList<Obra> obraArrayList;

    private ObservableList<String> grObservableList;
    private ObservableList<String> brigadaList;
    private ObservableList<String> obrasStrings;

    private List<Planificacion> planificacionList;
    private List<Certificacion> certificacionList;


    private int idgrupo;
    private Brigadaconstruccion brigada;


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

    private Obra obra;
    private Zonas zonas;
    private Objetos objetos;
    private Nivel niveles;
    private Especialidades especialidades;
    private Subespecialidades subespecialidades;

    private Grupoconstruccion grupoconst;
    private Brigadaconstruccion brigadaconstruccion;
    private int batchPlanrec = 5;
    private int countplan;

    /**
     * Bloques para las funciones de trabajo con la base de datos
     */


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

    public List<Planificacion> getPlanificacionBrigadaList(Grupoconstruccion grupo, Obra obrap, Zonas zonasp, Objetos objetosp, Nivel nivelp, Especialidades especialidadesp, Subespecialidades subespecialidadesp) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            planificacionList = new ArrayList<>();
            if (checkIntervalo.isSelected() == false) {
                planificacionList = session.createQuery(" FROM Planificacion ").getResultList();
            } else if (checkIntervalo.isSelected()) {
                LocalDate dateDes = dateIni.getValue();
                LocalDate dateHast = dateFin.getValue();
                planificacionList = session.createQuery(" FROM Planificacion WHERE desde =: dateDesde AND hasta =: dateHasta ").setParameter("dateDesde", Date.valueOf(dateDes)).setParameter("dateHasta", Date.valueOf(dateHast)).getResultList();
            }


            List<Planificacion> cutPlanificacionList = new ArrayList<>();
            if (obrap != null && zonasp == null && objetosp == null && nivelp == null && especialidadesp == null && subespecialidadesp == null) {
                cutPlanificacionList = planificacionList.parallelStream().filter(planificacion -> planificacion.getUnidadobraByUnidadobraId().getObraId() == obrap.getId() && planificacion.getGrupoconstruccionId() == grupo.getId()).collect(Collectors.toList());
            } else if (obrap != null && zonasp != null && objetosp == null && nivelp == null && especialidadesp == null && subespecialidadesp == null) {
                cutPlanificacionList = planificacionList.parallelStream().filter(planificacion -> planificacion.getUnidadobraByUnidadobraId().getObraId() == obrap.getId() && planificacion.getUnidadobraByUnidadobraId().getZonasByZonasId().getId() == zonasp.getId() && planificacion.getGrupoconstruccionId() == grupo.getId()).collect(Collectors.toList());
            } else if (obrap != null && zonasp != null && objetosp != null && nivelp == null && especialidadesp == null && subespecialidadesp == null) {
                cutPlanificacionList = planificacionList.parallelStream().filter(planificacion -> planificacion.getUnidadobraByUnidadobraId().getObraId() == obrap.getId() && planificacion.getUnidadobraByUnidadobraId().getZonasByZonasId().getId() == zonasp.getId() && planificacion.getUnidadobraByUnidadobraId().getObjetosByObjetosId().getId() == objetosp.getId() && planificacion.getGrupoconstruccionId() == grupo.getId()).collect(Collectors.toList());
            } else if (obrap != null && zonasp != null && objetosp != null && nivelp != null && especialidadesp == null && subespecialidadesp == null) {
                cutPlanificacionList = planificacionList.parallelStream().filter(planificacion -> planificacion.getUnidadobraByUnidadobraId().getObraId() == obrap.getId() && planificacion.getUnidadobraByUnidadobraId().getZonasByZonasId().getId() == zonasp.getId() && planificacion.getUnidadobraByUnidadobraId().getObjetosByObjetosId().getId() == objetosp.getId() && planificacion.getUnidadobraByUnidadobraId().getNivelByNivelId().getId() == nivelp.getId() && planificacion.getGrupoconstruccionId() == grupo.getId()).collect(Collectors.toList());
            } else if (obrap != null && zonasp != null && objetosp != null && nivelp != null && especialidadesp != null && subespecialidadesp == null) {
                cutPlanificacionList = planificacionList.parallelStream().filter(planificacion -> planificacion.getUnidadobraByUnidadobraId().getObraId() == obrap.getId() && planificacion.getUnidadobraByUnidadobraId().getZonasByZonasId().getId() == zonasp.getId() && planificacion.getUnidadobraByUnidadobraId().getObjetosByObjetosId().getId() == objetosp.getId() && planificacion.getUnidadobraByUnidadobraId().getNivelByNivelId().getId() == nivelp.getId() && planificacion.getUnidadobraByUnidadobraId().getEspecialidadesByEspecialidadesId().getId() == especialidadesp.getId() && planificacion.getGrupoconstruccionId() == grupo.getId()).collect(Collectors.toList());
            } else if (obrap != null && zonasp != null && objetosp != null && nivelp != null && especialidadesp != null && subespecialidadesp != null) {
                cutPlanificacionList = planificacionList.parallelStream().filter(planificacion -> planificacion.getUnidadobraByUnidadobraId().getObraId() == obrap.getId() && planificacion.getUnidadobraByUnidadobraId().getZonasByZonasId().getId() == zonasp.getId() && planificacion.getUnidadobraByUnidadobraId().getObjetosByObjetosId().getId() == objetosp.getId() && planificacion.getUnidadobraByUnidadobraId().getNivelByNivelId().getId() == nivelp.getId() && planificacion.getUnidadobraByUnidadobraId().getEspecialidadesByEspecialidadesId().getId() == especialidadesp.getId() && planificacion.getUnidadobraByUnidadobraId().getSubespecialidadesBySubespecialidadesId().getId() == subespecialidadesp.getId() && planificacion.getGrupoconstruccionId() == grupo.getId()).collect(Collectors.toList());
            }

            tx.commit();
            session.close();
            return cutPlanificacionList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Certificacion> getCertificacionBrigadaList(Grupoconstruccion grupo, Obra obrap, Zonas zonasp, Objetos objetosp, Nivel nivelp, Especialidades especialidadesp, Subespecialidades subespecialidadesp) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            certificacionList = new ArrayList<>();
            if (checkIntervalo.isSelected() == false) {
                certificacionList = session.createQuery(" FROM Certificacion ").getResultList();
            } else if (checkIntervalo.isSelected()) {
                LocalDate dateDes = dateIni.getValue();
                LocalDate dateHast = dateFin.getValue();
                certificacionList = session.createQuery(" FROM Certificacion WHERE desde =: dateDesde AND hasta =: dateHasta ").setParameter("dateDesde", Date.valueOf(dateDes)).setParameter("dateHasta", Date.valueOf(dateHast)).getResultList();
            }

            List<Certificacion> cutPlanificacionList = new ArrayList<>();
            if (obrap != null && zonasp == null && objetosp == null && nivelp == null && especialidadesp == null && subespecialidadesp == null) {
                cutPlanificacionList = certificacionList.parallelStream().filter(planificacion -> planificacion.getUnidadobraByUnidadobraId().getObraId() == obrap.getId() && planificacion.getGrupoconstruccionId() == grupo.getId()).collect(Collectors.toList());
            } else if (obrap != null && zonasp != null && objetosp == null && nivelp == null && especialidadesp == null && subespecialidadesp == null) {
                cutPlanificacionList = certificacionList.parallelStream().filter(planificacion -> planificacion.getUnidadobraByUnidadobraId().getObraId() == obrap.getId() && planificacion.getUnidadobraByUnidadobraId().getZonasByZonasId().getId() == zonasp.getId() && planificacion.getGrupoconstruccionId() == grupo.getId()).collect(Collectors.toList());
            } else if (obrap != null && zonasp != null && objetosp != null && nivelp == null && especialidadesp == null && subespecialidadesp == null) {
                cutPlanificacionList = certificacionList.parallelStream().filter(planificacion -> planificacion.getUnidadobraByUnidadobraId().getObraId() == obrap.getId() && planificacion.getUnidadobraByUnidadobraId().getZonasByZonasId().getId() == zonasp.getId() && planificacion.getUnidadobraByUnidadobraId().getObjetosByObjetosId().getId() == objetosp.getId() && planificacion.getGrupoconstruccionId() == grupo.getId()).collect(Collectors.toList());
            } else if (obrap != null && zonasp != null && objetosp != null && nivelp != null && especialidadesp == null && subespecialidadesp == null) {
                cutPlanificacionList = certificacionList.parallelStream().filter(planificacion -> planificacion.getUnidadobraByUnidadobraId().getObraId() == obrap.getId() && planificacion.getUnidadobraByUnidadobraId().getZonasByZonasId().getId() == zonasp.getId() && planificacion.getUnidadobraByUnidadobraId().getObjetosByObjetosId().getId() == objetosp.getId() && planificacion.getUnidadobraByUnidadobraId().getNivelByNivelId().getId() == nivelp.getId() && planificacion.getGrupoconstruccionId() == grupo.getId()).collect(Collectors.toList());
            } else if (obrap != null && zonasp != null && objetosp != null && nivelp != null && especialidadesp != null && subespecialidadesp == null) {
                cutPlanificacionList = certificacionList.parallelStream().filter(planificacion -> planificacion.getUnidadobraByUnidadobraId().getObraId() == obrap.getId() && planificacion.getUnidadobraByUnidadobraId().getZonasByZonasId().getId() == zonasp.getId() && planificacion.getUnidadobraByUnidadobraId().getObjetosByObjetosId().getId() == objetosp.getId() && planificacion.getUnidadobraByUnidadobraId().getNivelByNivelId().getId() == nivelp.getId() && planificacion.getUnidadobraByUnidadobraId().getEspecialidadesByEspecialidadesId().getId() == especialidadesp.getId() && planificacion.getGrupoconstruccionId() == grupo.getId()).collect(Collectors.toList());
            } else if (obrap != null && zonasp != null && objetosp != null && nivelp != null && especialidadesp != null && subespecialidadesp != null) {
                cutPlanificacionList = certificacionList.parallelStream().filter(planificacion -> planificacion.getUnidadobraByUnidadobraId().getObraId() == obrap.getId() && planificacion.getUnidadobraByUnidadobraId().getZonasByZonasId().getId() == zonasp.getId() && planificacion.getUnidadobraByUnidadobraId().getObjetosByObjetosId().getId() == objetosp.getId() && planificacion.getUnidadobraByUnidadobraId().getNivelByNivelId().getId() == nivelp.getId() && planificacion.getUnidadobraByUnidadobraId().getEspecialidadesByEspecialidadesId().getId() == especialidadesp.getId() && planificacion.getUnidadobraByUnidadobraId().getSubespecialidadesBySubespecialidadesId().getId() == subespecialidadesp.getId() && planificacion.getGrupoconstruccionId() == grupo.getId()).collect(Collectors.toList());
            }

            tx.commit();
            session.close();
            return cutPlanificacionList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public ObservableList<String> getObrasStrings() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            obraArrayList = new ArrayList<>();
            obrasStrings = FXCollections.observableArrayList();

            obraArrayList = (ArrayList<Obra>) session.createQuery("FROM Obra ").getResultList();
            obrasStrings.addAll(obraArrayList.parallelStream().map(obra -> obra.getCodigo() + " - " + obra.getDescripion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return obrasStrings;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getBrigadaConstruccionForm(int idEmpresa) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            brigadaconstruccionArrayList = new ArrayList<Brigadaconstruccion>();
            brigadaList = FXCollections.observableArrayList();

            brigadaconstruccionArrayList = (ArrayList<Brigadaconstruccion>) session.createQuery("FROM Brigadaconstruccion WHERE empresaconstructoraId=: idEmp ").setParameter("idEmp", idEmpresa).getResultList();
            brigadaList.addAll(brigadaconstruccionArrayList.parallelStream().map(brigadaconstruccion -> brigadaconstruccion.getCodigo() + " - " + brigadaconstruccion.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return brigadaList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getGrupoConstruccion(int idBrigada) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            grupoconstruccionArrayList = new ArrayList<>();
            grObservableList = FXCollections.observableArrayList();

            grupoconstruccionArrayList = (ArrayList<Grupoconstruccion>) session.createQuery("FROM Grupoconstruccion WHERE brigadaconstruccionId =: idBrig ").setParameter("idBrig", idBrigada).getResultList();
            grObservableList.addAll(grupoconstruccionArrayList.parallelStream().map(brigadaconstruccion -> brigadaconstruccion.getCodigo() + " - " + brigadaconstruccion.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return grObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void updatePlanificaciones(List<Planificacion> planrecuoList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countplan = 0;

            for (Planificacion planrecuo : planrecuoList) {
                countplan++;
                if (countplan > 0 && countplan % batchPlanrec == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(planrecuo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al actualizar");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    public void updateCertificacionList(List<Certificacion> certificacionList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countplan = 0;

            for (Certificacion certificacion : certificacionList) {
                countplan++;
                if (countplan > 0 && countplan % batchPlanrec == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(certificacion);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al actualizar");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    public void handlegetZonas(ActionEvent event) {
        String[] partObras = combo_obras.getValue().split(" - ");
        obra = obraArrayList.parallelStream().filter(obra1 -> obra1.getCodigo().equals(partObras[0])).findFirst().orElse(null);
        selectZonas.setItems(getZonasList(obra));
        selectEspecialidad.setItems(getEspecialidadList());
    }

    public void hendleGetObjetos(ActionEvent event) {
        String[] partZonas = selectZonas.getValue().split(" - ");
        zonas = listZonas.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);
        selectObjetos.setItems(getObjetosList(zonas));
    }

    public void handleGetNiveles(ActionEvent event) {
        String[] partObjetos = selectObjetos.getValue().split(" - ");
        objetos = listObjetos.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);
        selectNivel.setItems(getNivelList(objetos));
    }

    public void handleGetSubespecialidad(ActionEvent event) {
        String[] partEspecialidad = selectEspecialidad.getValue().split(" - ");
        especialidades = listEspecialidades.parallelStream().filter(especialidades1 -> especialidades1.getCodigo().equals(partEspecialidad[0])).findFirst().orElse(null);
        selectSubespecialidad.setItems(getSubespecialidadList(especialidades));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        checkIntervalo.setOnMouseClicked(event -> {
            if (checkIntervalo.isSelected()) {
                dateIni.setDisable(false);
                dateFin.setDisable(false);
            } else {
                dateIni.setDisable(true);
                dateFin.setDisable(true);
            }
        });


    }

    public void pasarParametros(Grupoconstruccion grupoconstruccion, int idEmp) {
        combo_obras.setItems(getObrasStrings());
        combo_brigada.setItems(getBrigadaConstruccionForm(idEmp));
        grupoconst = grupoconstruccion;

    }

    public void handleChangeGrupo(ActionEvent event) {
        String[] partBrigada = combo_brigada.getValue().split(" - ");
        brigadaconstruccion = brigadaconstruccionArrayList.parallelStream().filter(brigada -> brigada.getCodigo().equals(partBrigada[0])).findFirst().orElse(null);
        combo_grupo.setItems(getGrupoConstruccion(brigadaconstruccion.getId()));
    }

    @FXML
    public void handeleUpdateAction(ActionEvent event) {
        List<Planificacion> selectedPlanificaciones = new ArrayList<>();
        List<Certificacion> selectedCertificaciones = new ArrayList<>();

        String[] partGrupo = combo_grupo.getValue().split(" - ");
        Grupoconstruccion grupoToCam = grupoconstruccionArrayList.parallelStream().filter(grupoconstruccion -> grupoconstruccion.getCodigo().equals(partGrupo[0])).findFirst().orElse(null);

        if (combo_obras.getValue() != null && selectZonas.getValue() == null && selectObjetos.getValue() == null && selectNivel.getValue() == null && selectEspecialidad.getValue() == null && selectSubespecialidad.getValue() == null) {
            if (checkPlan.isSelected()) {
                selectedPlanificaciones = getPlanificacionBrigadaList(grupoconst, obra, null, null, null, null, null);
            }
            if (checkCertifocacion.isSelected()) {
                selectedCertificaciones = getCertificacionBrigadaList(grupoconst, obra, null, null, null, null, null);
            }
        } else if (combo_obras.getValue() != null && selectZonas.getValue() != null && selectObjetos.getValue() == null && selectNivel.getValue() == null && selectEspecialidad.getValue() == null && selectSubespecialidad.getValue() == null) {
            if (checkPlan.isSelected()) {
                selectedPlanificaciones = getPlanificacionBrigadaList(grupoconst, obra, zonas, null, null, null, null);
            }
            if (checkCertifocacion.isSelected()) {
                selectedCertificaciones = getCertificacionBrigadaList(grupoconst, obra, zonas, null, null, null, null);
            }
        } else if (combo_obras.getValue() != null && selectZonas.getValue() != null && selectObjetos.getValue() != null && selectNivel.getValue() == null && selectEspecialidad.getValue() == null && selectSubespecialidad.getValue() == null) {
            if (checkPlan.isSelected()) {
                selectedPlanificaciones = getPlanificacionBrigadaList(grupoconst, obra, zonas, objetos, null, null, null);
            }
            if (checkCertifocacion.isSelected()) {
                selectedCertificaciones = getCertificacionBrigadaList(grupoconst, obra, zonas, objetos, null, null, null);
            }
        } else if (combo_obras.getValue() != null && selectZonas.getValue() != null && selectObjetos.getValue() != null && selectNivel.getValue() != null && selectEspecialidad.getValue() == null && selectSubespecialidad.getValue() == null) {
            if (checkPlan.isSelected()) {
                selectedPlanificaciones = getPlanificacionBrigadaList(grupoconst, obra, zonas, objetos, niveles, null, null);
            }
            if (checkCertifocacion.isSelected()) {
                selectedCertificaciones = getCertificacionBrigadaList(grupoconst, obra, zonas, objetos, niveles, null, null);
            }
        } else if (combo_obras.getValue() != null && selectZonas.getValue() != null && selectObjetos.getValue() != null && selectNivel.getValue() != null && selectEspecialidad.getValue() != null && selectSubespecialidad.getValue() == null) {
            if (checkPlan.isSelected()) {
                selectedPlanificaciones = getPlanificacionBrigadaList(grupoconst, obra, zonas, objetos, niveles, especialidades, null);
            }
            if (checkCertifocacion.isSelected()) {
                selectedCertificaciones = getCertificacionBrigadaList(grupoconst, obra, zonas, objetos, niveles, especialidades, null);
            }
        } else if (combo_obras.getValue() != null && selectZonas.getValue() != null && selectObjetos.getValue() != null && selectNivel.getValue() != null && selectEspecialidad.getValue() != null && selectSubespecialidad.getValue() != null) {
            if (checkPlan.isSelected()) {
                selectedPlanificaciones = getPlanificacionBrigadaList(grupoconst, obra, zonas, objetos, niveles, especialidades, subespecialidades);
            }
            if (checkCertifocacion.isSelected()) {
                selectedCertificaciones = getCertificacionBrigadaList(grupoconst, obra, zonas, objetos, niveles, especialidades, subespecialidades);
            }
        }

        List<Planificacion> updatesPlanificaciones = new ArrayList<>();
        for (Planificacion selected : selectedPlanificaciones) {
            selected.setBrigadaconstruccionId(brigadaconstruccion.getId());
            selected.setGrupoconstruccionId(grupoToCam.getId());
            updatesPlanificaciones.add(selected);
        }

        List<Certificacion> updatesCertificacionList = new ArrayList<>();
        for (Certificacion selectedC : selectedCertificaciones) {
            selectedC.setBrigadaconstruccionId(brigadaconstruccion.getId());
            selectedC.setGrupoconstruccionId(grupoToCam.getId());
            updatesCertificacionList.add(selectedC);
        }

        try {
            updatePlanificaciones(updatesPlanificaciones);
            updateCertificacionList(updatesCertificacionList);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Datos Correctos");
            alert.setContentText("Las operaciones del grupo seleccionados fueron asignados al: " + combo_grupo.getValue());
            alert.showAndWait();


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al actualizar");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }


    }

    public void handleClose(ActionEvent event) {

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();


    }

}
