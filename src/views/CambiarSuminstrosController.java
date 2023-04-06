package views;

import com.jfoenix.controls.*;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CambiarSuminstrosController implements Initializable {

    private static double cantCertf;
    public double valorEscala;
    public Empresaobrasalario empresaobrasalario;
    public List<Renglonrecursos> listRecursos;
    public double salarioCalc;
    @FXML
    public TableView<UniddaObraToIMportView> tableUnidad;
    @FXML
    public TableColumn<UniddaObraToIMportView, JFXCheckBox> uoCode;
    @FXML
    public TableColumn<UniddaObraToIMportView, StringProperty> uodescrip;
    @FXML
    private JFXRadioButton radioRV;
    @FXML
    private JFXRadioButton radioSuminst;
    @FXML
    private JFXTextField fieldElement;
    @FXML
    private JFXButton btnAceptar;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXTextField fieldElementInt;
    @FXML
    private JFXComboBox<String> comboEmpresa;
    @FXML
    private JFXComboBox<String> comboZonas;
    @FXML
    private JFXComboBox<String> comboObjetos;
    @FXML
    private JFXComboBox<String> comboNiveles;
    @FXML
    private JFXComboBox<String> comboEspecialidad;
    private List<Empresaconstructora> empresaconstructoraList;
    private ObservableList<String> listEmpresas;
    private ArrayList<Zonas> zonasList;
    private ObservableList<String> listZonas;
    private ArrayList<Objetos> objetosList;
    private ObservableList<String> listObjetos;
    private ArrayList<Nivel> nivelList;
    private ObservableList<String> listNivel;
    private List<Especialidades> especialidadesList;
    private ObservableList<String> listEspecialidades;
    private int obraId;
    private int tnorma;
    @FXML
    private Label labelCostoChange;
    @FXML
    private Label labelCostoIn;
    private List<Unidadobrarenglon> unidadobrarenglons;
    private List<Bajoespecificacion> bajoespecificacionList;
    private Renglonvariante renglonvariante;
    private boolean flag;
    private Recursos recursosOrg;
    private Juegoproducto juegoproducto;
    private Suministrossemielaborados suministrossemielaborados;
    private List<Empresaobrasalario> empresaobrasalarioList;
    private List<Unidadobra> listUnidadesObra;
    private int inputLength = 6;
    private Renglonvariante renglontoChange;
    private Recursos rec;
    private Juegoproducto jg;
    private Suministrossemielaborados sel;
    private Renglonvariante getRenglontoIn;
    private Recursos recursosp;
    private Juegoproducto juegoproductop;
    private Suministrossemielaborados suministrossemielaboradosp;
    private int sizeBath = 10;
    private int count = 0;
    private double salario;
    private List<Unidadobrarenglon> unidadobrarenglonList;
    private List<Bajoespecificacion> datosBajoEspecificacionToUpdates;
    private List<Unidadobra> list;
    private List<Subespecialidades> subespecialidadesList;
    private ObservableList<String> listSubespecialidades;
    @FXML
    private JFXComboBox<String> comboSub;
    @FXML
    private TextArea text1;
    @FXML
    private TextArea text2;
    private ObservableList<UniddaObraToIMportView> uniddaObraViewObservableList;
    private List<Unidadobra> unidadobraList;
    private Recursos recurso;
    private Juegoproducto juego;
    private Suministrossemielaborados sumin;
    private int idRec;
    private Recursos rcurso;
    private Juegoproducto jueg;
    private Suministrossemielaborados sum;
    private UtilCalcSingelton utilCalc = UtilCalcSingelton.getInstance();
    private Obra obraSend;
    private int idSumiChange;
    private String r_tipo = "";
    private double r_costo = 0.0;
    private int r_id = 0;
    private double r_precio = 0.0;
    private double cant = 0;
    private double costo = 0;

    private ObservableList<String> getEmpresaconstructoraList(int idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaconstructoraList = new ArrayList<>();
            List<Object[]> empresaObjects = session.createQuery("FROM Empresaconstructora ec LEFT JOIN  Empresaobra eo ON ec.id = eo.empresaconstructoraId WHERE eo.obraId =: idO").setParameter("idO", idObra).getResultList();

            for (Object[] row : empresaObjects) {
                empresaconstructoraList.add((Empresaconstructora) row[0]);
            }
            listEmpresas = FXCollections.observableArrayList();
            listEmpresas.add("Todas");
            listEmpresas.addAll(empresaconstructoraList.parallelStream().map(empresaconstructora -> empresaconstructora.getCodigo() + " - " + empresaconstructora.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return listEmpresas;
        } catch (Exception e) {
        }
        return FXCollections.emptyObservableList();
    }

    private ObservableList<String> getObjetosList(int idZonas) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            objetosList = new ArrayList<>();
            Zonas zon = session.get(Zonas.class, idZonas);
            zon.getObjetosById().size();
            objetosList.addAll(zon.getObjetosById());

            listObjetos = FXCollections.observableArrayList();
            listObjetos.add("Todos");
            listObjetos.addAll(objetosList.parallelStream().map(objetos -> objetos.getCodigo() + " - " + objetos.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return listObjetos;
        } catch (Exception e) {
        }
        return FXCollections.emptyObservableList();
    }

    private ObservableList<String> getNivelList(int idObjetos) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            nivelList = new ArrayList<>();
            Objetos obj = session.get(Objetos.class, idObjetos);
            obj.getNivelespecificosById().size();
            nivelList.addAll(obj.getNivelsById());

            listNivel = FXCollections.observableArrayList();
            listNivel.add("Todos");
            listNivel.addAll(nivelList.parallelStream().map(nivel -> nivel.getCodigo() + " - " + nivel.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return listNivel;
        } catch (Exception e) {
        }
        return FXCollections.emptyObservableList();
    }

    private ObservableList<String> getEspecialiadadesList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            especialidadesList = new ArrayList<>();
            especialidadesList = session.createQuery("FROM Especialidades ").getResultList();
            listEspecialidades = FXCollections.observableArrayList();
            listEspecialidades.addAll(especialidadesList.parallelStream().map(especialidades -> especialidades.getCodigo() + " - " + especialidades.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return listEspecialidades;
        } catch (Exception e) {
        }
        return FXCollections.emptyObservableList();
    }

    public void handleChargeSub(ActionEvent event) {
        if (!comboEspecialidad.getValue().equals("Todas")) {
            String[] partEsp = comboEspecialidad.getValue().split(" - ");
            Especialidades esp = especialidadesList.parallelStream().filter(especialidades -> especialidades.getCodigo().trim().equals(partEsp[0].trim())).findFirst().get();
            System.out.println(esp.getCodigo());
            getSubespecialidad(esp);
        }
    }

    public void getSubespecialidad(Especialidades especialidades) {
        subespecialidadesList = new ArrayList<>();
        subespecialidadesList.addAll(especialidades.getSubespecialidadesById().stream().collect(Collectors.toList()));
        listSubespecialidades = FXCollections.observableArrayList();
        listSubespecialidades.addAll(subespecialidadesList.stream().map(Subespecialidades::toString).collect(Collectors.toList()));
        comboSub.setItems(listSubespecialidades);

    }

    public void handleCargarUnidadObra(ActionEvent event) {
        if (comboEmpresa.getValue() != null && comboZonas.getValue() != null && comboObjetos.getValue() != null && comboNiveles.getValue() != null && comboEspecialidad.getValue() != null && comboSub.getValue() != null) {
            String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
            Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

            String[] partZonas = comboZonas.getValue().split(" - ");
            Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

            String[] partObjetos = comboObjetos.getValue().split(" - ");
            Objetos objetos = objetosList.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);

            String[] partNiveles = comboNiveles.getValue().split(" - ");
            Nivel nivel = nivelList.parallelStream().filter(nivel1 -> nivel1.getCodigo().equals(partNiveles[0])).findFirst().orElse(null);

            String[] parEspecialidades = comboEspecialidad.getValue().split(" - ");
            Especialidades especialidades = especialidadesList.parallelStream().filter(especialidades1 -> especialidades1.getCodigo().equals(parEspecialidades[0])).findFirst().orElse(null);

            Subespecialidades subespecialidades = subespecialidadesList.parallelStream().filter(item -> item.toString().trim().equals(comboSub.getValue().trim())).findFirst().get();

            uniddaObraViewObservableList = FXCollections.observableArrayList();
            uniddaObraViewObservableList = utilCalc.getUniddaObraViewObservableList(obraId, empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), subespecialidades.getId());
            loadDataFromDB(uniddaObraViewObservableList);
        } else {
            utilCalc.showAlert("Complete la estructura del formulario", 2);
        }

    }

    private void loadDataFromDB(ObservableList<UniddaObraToIMportView> dataItemsList) {
        uoCode.setCellValueFactory(new PropertyValueFactory<UniddaObraToIMportView, JFXCheckBox>("codigo"));
        uodescrip.setCellValueFactory(new PropertyValueFactory<UniddaObraToIMportView, StringProperty>("descripcion"));
        tableUnidad.getItems().setAll(dataItemsList);
    }

    private Renglonvariante getRenglon(String obRs) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Object[]> datosRV = session.createQuery("FROM Renglonvariante r LEFT JOIN Unidadobrarenglon ur ON r.id = ur.renglonvarianteId LEFT JOIN Unidadobra uo ON ur.unidadobraId = uo.id WHERE r.codigo =: obraRS AND uo.obraId =: idO").setParameter("obraRS", obRs).setParameter("idO", obraId).getResultList();
            if (datosRV.get(0) != null) {
                renglonvariante = new Renglonvariante();
                renglonvariante = (Renglonvariante) datosRV.get(0)[0];
            }
            tx.commit();
            session.close();
            return renglonvariante;

        } catch (Exception e) {
        }

        return renglonvariante;
    }

    private boolean getFlag(int idSum) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            flag = false;
            List<Object> listRecursos = session.createQuery(" FROM Bajoespecificacion bajo INNER JOIN Unidadobra uo ON bajo.unidadobraId = uo.id WHERE uo.obraId =: idO AND bajo.idSuministro =:idS").setParameter("idO", obraId).setParameter("idS", idSum).getResultList();
            if (listRecursos.size() > 0) {
                flag = true;
            }

            tx.commit();
            session.close();
            return flag;

        } catch (Exception e) {
        }
        return flag;
    }

    private ObservableList<String> getZonasList(int idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            zonasList = new ArrayList<>();
            Obra ob = session.get(Obra.class, idObra);
            ob.getZonasById().size();
            zonasList.addAll(ob.getZonasById());

            listZonas = FXCollections.observableArrayList();
            listZonas.add("Todas");
            listZonas.addAll(zonasList.parallelStream().map(zonas -> zonas.getCodigo() + " - " + zonas.getDesripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return listZonas;
        } catch (Exception e) {
        }
        return FXCollections.emptyObservableList();
    }

    private List<Unidadobrarenglon> getUnidadobrarenglonList(int idRenglon, Empresaconstructora empresaconstructora, Zonas zonas, Objetos objetos, Nivel nivel, Especialidades especialidades) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadobrarenglons = new ArrayList<>();
            List<Object[]> datosUnidadObraRV = session.createQuery("FROM Unidadobrarenglon ur LEFT JOIN Unidadobra uo ON ur.unidadobraId = uo.id WHERE ur.renglonvarianteId =: idRv AND uo.obraId =: idO").setParameter("idRv", idRenglon).setParameter("idO", obraId).getResultList();
            for (Object[] row : datosUnidadObraRV) {
                //   Unidadobrarenglon uor = (Unidadobrarenglon) row[0];
                // if (getCantidadCertificadaByUO(uor.getUnidadobraId()) == 0) {
                if (empresaconstructora == null && zonas == null && objetos == null && nivel == null && especialidades == null) {
                    unidadobrarenglons.add((Unidadobrarenglon) row[0]);
                } else if (empresaconstructora != null && zonas == null && objetos == null && nivel == null && especialidades == null) {
                    Unidadobrarenglon unidadobrarenglon = (Unidadobrarenglon) row[0];
                    if (unidadobrarenglon.getUnidadobraByUnidadobraId().getEmpresaconstructoraByEmpresaconstructoraId().getId() == empresaconstructora.getId()) {
                        unidadobrarenglons.add(unidadobrarenglon);
                    }
                } else if (empresaconstructora != null && zonas != null && objetos == null && nivel == null && especialidades == null) {
                    Unidadobrarenglon unidadobrarenglon = (Unidadobrarenglon) row[0];
                    if (unidadobrarenglon.getUnidadobraByUnidadobraId().getEmpresaconstructoraId() == empresaconstructora.getId() && unidadobrarenglon.getUnidadobraByUnidadobraId().getZonasId() == zonas.getId()) {
                        unidadobrarenglons.add(unidadobrarenglon);
                    }
                } else if (empresaconstructora != null && zonas != null && objetos != null && nivel == null && especialidades == null) {
                    Unidadobrarenglon unidadobrarenglon = (Unidadobrarenglon) row[0];
                    if (unidadobrarenglon.getUnidadobraByUnidadobraId().getEmpresaconstructoraId() == empresaconstructora.getId() && unidadobrarenglon.getUnidadobraByUnidadobraId().getZonasId() == zonas.getId() && unidadobrarenglon.getUnidadobraByUnidadobraId().getObjetosId() == objetos.getId()) {
                        unidadobrarenglons.add(unidadobrarenglon);
                    }
                } else if (empresaconstructora != null && zonas != null && objetos != null && nivel != null && especialidades == null) {
                    Unidadobrarenglon unidadobrarenglon = (Unidadobrarenglon) row[0];
                    if (unidadobrarenglon.getUnidadobraByUnidadobraId().getEmpresaconstructoraId() == empresaconstructora.getId() && unidadobrarenglon.getUnidadobraByUnidadobraId().getZonasId() == zonas.getId() && unidadobrarenglon.getUnidadobraByUnidadobraId().getObjetosId() == objetos.getId() && unidadobrarenglon.getUnidadobraByUnidadobraId().getNivelId() == nivel.getId()) {
                        unidadobrarenglons.add(unidadobrarenglon);
                    }
                } else if (empresaconstructora != null && zonas != null && objetos != null && nivel != null && especialidades != null) {
                    Unidadobrarenglon unidadobrarenglon = (Unidadobrarenglon) row[0];
                    if (unidadobrarenglon.getUnidadobraByUnidadobraId().getEmpresaconstructoraId() == empresaconstructora.getId() && unidadobrarenglon.getUnidadobraByUnidadobraId().getZonasId() == zonas.getId() && unidadobrarenglon.getUnidadobraByUnidadobraId().getObjetosId() == objetos.getId() && unidadobrarenglon.getUnidadobraByUnidadobraId().getNivelId() == nivel.getId() && unidadobrarenglon.getUnidadobraByUnidadobraId().getEspecialidadesId() == especialidades.getId()) {
                        unidadobrarenglons.add(unidadobrarenglon);
                    }
                }
            }
            //  }

            tx.commit();
            session.close();
            return unidadobrarenglons;
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    private List<Bajoespecificacion> getBajoespecificacionListList(int idSum, Empresaconstructora empresaconstructora, Zonas zonas, Objetos objetos, Nivel nivel, Especialidades especialidades, Subespecialidades subespecialidades) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            bajoespecificacionList = new ArrayList<>();
            List<Object[]> datosUnidadBajo = session.createQuery("FROM Bajoespecificacion bajo INNER JOIN Unidadobra uo ON bajo.unidadobraId = uo.id WHERE uo.obraId =: idO  AND bajo.idSuministro =: idS ").setParameter("idS", idSum).setParameter("idO", obraId).getResultList();
            for (Object[] row : datosUnidadBajo) {
                if (empresaconstructora == null && zonas == null && objetos == null && nivel == null && especialidades == null && subespecialidades == null) {
                    bajoespecificacionList.add((Bajoespecificacion) row[0]);
                } else if (empresaconstructora != null && zonas == null && objetos == null && nivel == null && especialidades == null && subespecialidades == null) {
                    Bajoespecificacion bajoespecificacion = (Bajoespecificacion) row[0];
                    Unidadobra unidadobra = session.get(Unidadobra.class, bajoespecificacion.getUnidadobraId());
                    if (unidadobra.getEmpresaconstructoraByEmpresaconstructoraId().getId() == empresaconstructora.getId()) {
                        bajoespecificacionList.add(bajoespecificacion);
                    }
                } else if (empresaconstructora != null && zonas != null && objetos == null && nivel == null && especialidades == null && subespecialidades == null) {
                    Bajoespecificacion bajoespecificacion = (Bajoespecificacion) row[0];
                    Unidadobra unidadobra = session.get(Unidadobra.class, bajoespecificacion.getUnidadobraId());
                    if (unidadobra.getEmpresaconstructoraId() == empresaconstructora.getId() && unidadobra.getZonasId() == zonas.getId()) {
                        bajoespecificacionList.add(bajoespecificacion);
                    }
                } else if (empresaconstructora != null && zonas != null && objetos != null && nivel == null && especialidades == null && subespecialidades == null) {
                    Bajoespecificacion bajoespecificacion = (Bajoespecificacion) row[0];
                    Unidadobra unidadobra = session.get(Unidadobra.class, bajoespecificacion.getUnidadobraId());
                    if (unidadobra.getEmpresaconstructoraId() == empresaconstructora.getId() && unidadobra.getZonasId() == zonas.getId() && unidadobra.getObjetosId() == objetos.getId()) {
                        bajoespecificacionList.add(bajoespecificacion);
                    }
                } else if (empresaconstructora != null && zonas != null && objetos != null && nivel != null && especialidades == null && subespecialidades == null) {
                    Bajoespecificacion bajoespecificacion = (Bajoespecificacion) row[0];
                    Unidadobra unidadobra = session.get(Unidadobra.class, bajoespecificacion.getUnidadobraId());
                    if (unidadobra.getEmpresaconstructoraId() == empresaconstructora.getId() && unidadobra.getZonasId() == zonas.getId() && unidadobra.getObjetosId() == objetos.getId() && unidadobra.getNivelId() == nivel.getId()) {
                        bajoespecificacionList.add(bajoespecificacion);
                    }
                } else if (empresaconstructora != null && zonas != null && objetos != null && nivel != null && especialidades != null && subespecialidades == null) {
                    Bajoespecificacion bajoespecificacion = (Bajoespecificacion) row[0];
                    Unidadobra unidadobra = session.get(Unidadobra.class, bajoespecificacion.getUnidadobraId());
                    if (unidadobra.getEmpresaconstructoraId() == empresaconstructora.getId() && unidadobra.getZonasId() == zonas.getId() && unidadobra.getObjetosId() == objetos.getId() && unidadobra.getNivelId() == nivel.getId() && unidadobra.getEspecialidadesId() == especialidades.getId()) {
                        bajoespecificacionList.add(bajoespecificacion);
                    }
                } else if (empresaconstructora != null && zonas != null && objetos != null && nivel != null && especialidades != null && subespecialidades != null) {
                    Bajoespecificacion bajoespecificacion = (Bajoespecificacion) row[0];
                    Unidadobra unidadobra = session.get(Unidadobra.class, bajoespecificacion.getUnidadobraId());
                    if (unidadobra.getEmpresaconstructoraId() == empresaconstructora.getId() && unidadobra.getZonasId() == zonas.getId() && unidadobra.getObjetosId() == objetos.getId() && unidadobra.getNivelId() == nivel.getId() && unidadobra.getEspecialidadesId() == especialidades.getId() && unidadobra.getSubespecialidadesId() == subespecialidades.getId()) {
                        bajoespecificacionList.add(bajoespecificacion);
                    }
                }
            }
            //  }
            tx.commit();
            session.close();
            return bajoespecificacionList;
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    private Recursos getRecursoById(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            recurso = new Recursos();
            recurso = session.get(Recursos.class, id);

            tx.commit();
            session.close();
            return recurso;

        } catch (Exception e) {
        }
        return recurso;
    }

    private Juegoproducto getJuegoById(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            juego = new Juegoproducto();
            juego = session.get(Juegoproducto.class, id);

            tx.commit();
            session.close();
            return juego;

        } catch (Exception e) {
        }
        return juego;
    }

    private Suministrossemielaborados getSuministrossemielaboradosById(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            sumin = new Suministrossemielaborados();
            sumin = session.get(Suministrossemielaborados.class, id);

            tx.commit();
            session.close();
            return sumin;

        } catch (Exception e) {
        }
        return sumin;
    }

    private Recursos materialByChange(String codeSumini) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            idRec = 0;
            idRec = (Integer) session.createQuery("SELECT DISTINCT bajo.idSuministro FROM Bajoespecificacion bajo INNER JOIN Unidadobra uo ON bajo.unidadobraId = uo.id INNER JOIN Recursos rec On bajo.idSuministro = rec.id WHERE uo.obraId =: obId AND rec.codigo =: code AND bajo.tipo = '1'").setParameter("obId", obraId).setParameter("code", codeSumini).getSingleResult();
            tx.commit();
            session.close();
            return getRecursoById(idRec);

        } catch (Exception e) {
        }
        return recursosOrg;
    }

    private Juegoproducto juegoByChange(String codeSumini) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            int idRec = (Integer) session.createQuery("SELECT DISTINCT bajo.idSuministro FROM Bajoespecificacion bajo INNER JOIN Unidadobra uo ON bajo.unidadobraId = uo.id INNER JOIN Juegoproducto rec On bajo.idSuministro = rec.id WHERE uo.obraId =: obId AND rec.codigo =: code AND bajo.tipo = 'J'").setParameter("obId", obraId).setParameter("code", codeSumini).getSingleResult();
            tx.commit();
            session.close();
            return getJuegoById(idRec);

        } catch (Exception e) {
        }
        return juegoproducto;
    }

    private Suministrossemielaborados suministrossemielaboradosByChange(String codeSumini) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            int idRec = (Integer) session.createQuery("SELECT DISTINCT bajo.idSuministro FROM Bajoespecificacion bajo INNER JOIN Unidadobra uo ON bajo.unidadobraId = uo.id INNER JOIN Suministrossemielaborados rec On bajo.idSuministro = rec.id WHERE uo.obraId =: obId AND rec.codigo =: code AND bajo.tipo = 'S'").setParameter("obId", obraId).setParameter("code", codeSumini).getSingleResult();
            tx.commit();
            session.close();
            return getSuministrossemielaboradosById(idRec);
        } catch (Exception e) {
        }
        return suministrossemielaborados;
    }

    private Renglonvariante getRenglontoChange(String codeR) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            renglonvariante = new Renglonvariante();
            renglonvariante = (Renglonvariante) session.createQuery("FROM Renglonvariante WHERE codigo =: cod AND rs =: obraRS").setParameter("cod", codeR).setParameter("obraRS", tnorma).getSingleResult();

            tx.commit();
            session.close();
            return renglonvariante;

        } catch (Exception e) {
        }

        return renglonvariante;
    }

    private List<Empresaobrasalario> getEmpresaobrasalarioList(int idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaobrasalarioList = new ArrayList<>();
            empresaobrasalarioList = session.createQuery("FROM Empresaobrasalario WHERE obraId =: idO").setParameter("idO", idObra).getResultList();

            tx.commit();
            session.close();
            return empresaobrasalarioList;

        } catch (Exception e) {
        }

        return new ArrayList<>();
    }

    public List<Renglonrecursos> getRecursosinRV(int idR) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Renglonvariante renglon = session.get(Renglonvariante.class, idR);
            renglon.getRenglonrecursosById().size();
            tx.commit();
            session.close();
            return renglon.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().contentEquals("2")).collect(Collectors.toList());

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return new ArrayList<>();

    }

    private double calcSalarioRV(int idReng, int idEmp) {
        listRecursos = new LinkedList<>();
        listRecursos = getRecursosinRV(idReng);
        salarioCalc = 0.0;
        for (Renglonrecursos item : listRecursos) {
            salarioCalc += item.getCantidas() * getValorTarifa(item.getRecursosByRecursosId().getGrupoescala(), idEmp);
        }
        return salarioCalc;
    }

    public Unidadobra getUpdateUnidadObraList(int idUnid) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            Unidadobra un = session.get(Unidadobra.class, idUnid);
            un.getUnidadobrarenglonsById().size();
            double costMano = un.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMano).reduce(0.0, Double::sum);
            double costEqui = un.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostEquip).reduce(0.0, Double::sum);
            double costMat = un.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMat).reduce(0.0, Double::sum);
            double salario = un.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getSalariomn).reduce(0.0, Double::sum);
            double cMaterialBajo = unidadobraBajoList(un.getId()).parallelStream().map(Bajoespecificacion::getCosto).reduce(0.0, Double::sum);
            double total = costMano + costEqui + costMat + cMaterialBajo;
            double unitario = total / un.getCantidad();
            un.setSalario(salario);
            un.setCostototal(total);
            un.setCostoequipo(costEqui);
            un.setCostoMaterial(costMat + cMaterialBajo);
            un.setCostomano(costMano);
            un.setCostounitario(unitario);

            tx.commit();
            session.close();
            return un;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Unidadobra();
    }

    public List<Bajoespecificacion> unidadobraBajoList(int obraId) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Bajoespecificacion> listBajoEspecifiacacion = new ArrayList<>();
            listBajoEspecifiacacion = session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: id ").setParameter("id", obraId).getResultList();

            tx.commit();
            session.close();
            return listBajoEspecifiacacion;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Crear el método para calcular el salario a partir de la lista de empresa obra y salarios.
     */

    private Empresaobrasalario getEmpresaobrasalario(int idEmp) {
        return empresaobrasalarioList.parallelStream().filter(empresaobra -> empresaobra.getEmpresaconstructoraId() == idEmp).findFirst().orElse(new Empresaobrasalario());
    }

    public double getValorTarifa(String grupo, int idEmp) {
        valorEscala = 0;
        empresaobrasalario = new Empresaobrasalario();
        empresaobrasalario = getEmpresaobrasalario(idEmp);

        if (grupo.equals("II")) {
            valorEscala = empresaobrasalario.getIi();
        } else if (grupo.equals("III")) {
            valorEscala = empresaobrasalario.getIii();
        } else if (grupo.equals("IV")) {
            valorEscala = empresaobrasalario.getIv();
        } else if (grupo.equals("V")) {
            valorEscala = empresaobrasalario.getV();
        } else if (grupo.equals("VI")) {
            valorEscala = empresaobrasalario.getVi();
        } else if (grupo.equals("VII")) {
            valorEscala = empresaobrasalario.getVii();
        } else if (grupo.equals("VIII")) {
            valorEscala = empresaobrasalario.getViii();
        } else if (grupo.equals("IX")) {
            valorEscala = empresaobrasalario.getIx();
        } else if (grupo.equals("X")) {
            valorEscala = empresaobrasalario.getX();
        }

        return valorEscala;

    }

    public void keyTypeCodeChange(KeyEvent event) {
        if (radioRV.isSelected() == true) {
            if (fieldElement.getText().length() == inputLength) {
                renglontoChange = getRenglon(fieldElement.getText());
                if (renglontoChange.getUm() != " ") {
                    double cost = renglontoChange.getCostmano() + renglontoChange.getCostequip() + renglontoChange.getCostomat();
                    labelCostoChange.setText(String.valueOf(cost) + "/" + renglontoChange.getUm());
                    text1.setText(renglontoChange.getDescripcion());
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error !!");
                    alert.setContentText("El renglón variante no esta contenido en la obra");
                    alert.showAndWait();
                }
            }
        }
    }

    private Recursos materialByChangeIn(int idRCurso) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            rcurso = session.get(Recursos.class, idRCurso);
            tx.commit();
            session.close();
            return rcurso;
        } catch (Exception e) {
        }
        return rcurso;
    }

    public void keyTypeCodeIn(KeyEvent event) {
        if (radioRV.isSelected() == true) {
            if (fieldElementInt.getText().length() == inputLength) {
                getRenglontoIn = getRenglontoChange(fieldElementInt.getText());
                double cost = getRenglontoIn.getCostmano() + getRenglontoIn.getCostequip() + getRenglontoIn.getCostomat();
                text2.setText(getRenglontoIn.getDescripcion());
                labelCostoIn.setText(String.valueOf(Math.round(cost)) + "/" + getRenglontoIn.getUm());
            }
        }
    }

    private Juegoproducto materialByJuegoOrodIn(int idJueg) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            jueg = session.get(Juegoproducto.class, idJueg);
            tx.commit();
            session.close();
            return jueg;

        } catch (Exception e) {
        }
        return jueg;
    }

    public void getSuministro(ActionEvent event) {
        idSumiChange = 0;
        if (radioSuminst.isSelected()) {
            if (fieldElement.getText().length() > 0) {
                rec = materialByChange(fieldElement.getText());
                if (rec != null) {
                    idSumiChange = rec.getId();
                }
                if (rec != null) {
                    boolean check = getFlag(rec.getId());
                    if (check == true) {
                        labelCostoChange.setText(rec.getPreciomn() + " / " + rec.getUm());
                        text1.setText(rec.getDescripcion());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error !!");
                        alert.setContentText("El recurso no esta contenido en la obra");
                        alert.showAndWait();
                    }
                }

                if (rec == null) {
                    jg = juegoByChange(fieldElement.getText());
                    if (jg != null) {
                        boolean check = getFlag(jg.getId());
                        if (check == true) {
                            labelCostoChange.setText(jg.getPreciomn() + " / " + jg.getUm());
                            text1.setText(jg.getDescripcion());
                            idSumiChange = jueg.getId();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Error !!");
                            alert.setContentText("El recurso no esta contenido en la obra");
                            alert.showAndWait();
                        }
                    }
                }
                if (rec == null && jg == null) {
                    sel = suministrossemielaboradosByChange(fieldElement.getText());
                    if (sel != null) {
                        boolean check = getFlag(sel.getId());
                        if (check == true) {
                            labelCostoChange.setText(sel.getPreciomn() + " / " + sel.getUm());
                            text1.setText(sel.getDescripcion());
                            idSumiChange = sel.getId();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Error !!");
                            alert.setContentText("El recurso no esta contenido en la obra");
                            alert.showAndWait();
                        }
                    }
                }


            }
        }
    }

    public void getSuministroIn(ActionEvent event) {

        if (radioSuminst.isSelected()) {
            SuministrosView suministrosView = utilCalc.suministrosViewObservableList.parallelStream().filter(item -> item.toString().trim().equals(fieldElementInt.getText().trim())).findFirst().get();
            // System.out.println(suministrosView.getDescripcion().trim());
            if (fieldElementInt.getText().length() > 0) {
                recursosp = materialByChangeIn(suministrosView.getId());
                if (recursosp != null) {
                    fieldElementInt.setText(recursosp.getCodigo());
                    labelCostoIn.setText(recursosp.getPreciomn() + " / " + recursosp.getUm());
                    text2.setText(recursosp.getDescripcion());

                } else {
                    recursosp = null;
                }
                if (recursosp == null) {
                    juegoproductop = materialByJuegoOrodIn(suministrosView.getId());
                    if (juegoproductop != null) {
                        fieldElementInt.setText(juegoproductop.getCodigo());
                        labelCostoIn.setText(juegoproductop.getPreciomn() + " / " + juegoproductop.getUm());
                        text2.setText(juegoproductop.getDescripcion());

                    } else {
                        juegoproductop = null;
                    }
                }
                if (recursosp == null && juegoproducto == null) {
                    suministrossemielaboradosp = materialBySuministroSemiIn(suministrosView.getId());
                    if (suministrossemielaboradosp != null) {
                        fieldElementInt.setText(suministrossemielaboradosp.getCodigo());
                        labelCostoIn.setText(suministrossemielaboradosp.getPreciomn() + " / " + suministrossemielaboradosp.getUm());
                        text2.setText(suministrossemielaboradosp.getDescripcion());

                    } else {
                        suministrossemielaboradosp = null;
                    }
                }
            }
        }
    }

    private List<Certificacionrecuo> getAllCertificacRe(int idUO) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Certificacionrecuo> certificacionrecuoList = session.createQuery(" FROM Certificacionrecuo WHERE unidadobraId =: idU").setParameter("idU", idUO).getResultList();
            tx.commit();
            session.close();
            return certificacionrecuoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void updateUnidad(List<Unidadobra> listUnidadObra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;

            for (Unidadobra ur : listUnidadObra) {
                count++;
                if (count > 0 && count % sizeBath == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(ur);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !!");
            alert.setContentText("Error al Actualizar los datos de las unidades de obra");
            alert.showAndWait();
        }

    }

    private void updateUnidadObraRenglons(List<Unidadobrarenglon> listUnidadObraRenglons) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;

            for (Unidadobrarenglon ur : listUnidadObraRenglons) {
                count++;
                if (count > 0 && count % sizeBath == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(ur);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !!");
            alert.setContentText("Error al Actualizar los datos de los renglones variantes");
            alert.showAndWait();
        }

    }

    private void updateUnidadBajo(List<Bajoespecificacion> listUnidadObraRenglons) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;

            for (Bajoespecificacion ur : listUnidadObraRenglons) {
                count++;
                if (count > 0 && count % sizeBath == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(ur);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !!");
            alert.setContentText("Error al Actualizar los datos de los renglones variantes");
            alert.showAndWait();
        }

    }

    private void updateUnidadRenglonBase(int idU, Renglonvariante renglon) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Unidadobra unidadobra = session.get(Unidadobra.class, idU);
            unidadobra.setRenglonbase(renglon.getCodigo());
            session.update(unidadobra);


            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !!");
            alert.setContentText("Error al Actualizar los datos de los renglones variantes");
            alert.showAndWait();
        }

    }

    private void deleteUnidadObraRenglons(List<Unidadobrarenglon> listUnidadObraRenglons) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;

            for (Unidadobrarenglon ur : listUnidadObraRenglons) {
                count++;
                if (count > 0 && count % sizeBath == 0) {
                    session.flush();
                    session.clear();
                }

                session.delete(ur);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !!");
            alert.setContentText("Error al Actualizar los datos de los renglones variantes");
            alert.showAndWait();
        }

    }

    private void deleteUnidadBajo(List<Bajoespecificacion> listUnidadObraRenglons) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;

            for (Bajoespecificacion ur : listUnidadObraRenglons) {
                count++;
                if (count > 0 && count % sizeBath == 0) {
                    session.flush();
                    session.clear();
                }

                session.delete(ur);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !!");
            alert.setContentText("Error al Actualizar los datos de los renglones variantes");
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        radioRV.setOnAction(event -> {
            if (radioRV.isSelected() == true) {
                radioSuminst.setSelected(false);
                fieldElement.setPromptText("Renglón Variante a Cambiar");
                fieldElementInt.setPromptText("Cambiar por");
            }
        });

        radioSuminst.setOnAction(event -> {
            if (radioSuminst.isSelected() == true) {
                radioRV.setSelected(false);
                fieldElement.setPromptText("Suministro a Cambiar");
                fieldElementInt.setPromptText("Cambiar por");

                List<String> listSuministrosSug = utilCalc.getSuministrosViewObservableList(obraSend.getSalarioBySalarioId().getTag()).parallelStream().map(SuministrosView::toString).collect(Collectors.toList());
                TextFields.bindAutoCompletion(fieldElementInt, listSuministrosSug).setPrefWidth(500);

            }
        });
    }

    private Suministrossemielaborados materialBySuministroSemiIn(int idSum) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            sum = session.get(Suministrossemielaborados.class, idSum);
            tx.commit();
            session.close();
            return sum;

        } catch (Exception e) {
        }
        return sum;
    }

    public void pasarDatos(Obra obra) {
        obraSend = obra;
        obraId = obra.getId();
        comboEmpresa.setItems(getEmpresaconstructoraList(obraId));
        comboZonas.setItems(getZonasList(obraId));
        comboEspecialidad.setItems(getEspecialiadadesList());
        tnorma = obra.getSalarioId();
        empresaobrasalarioList = new ArrayList<>();
        empresaobrasalarioList = getEmpresaobrasalarioList(obraId);

    }

    public void handleCargarObjetos(ActionEvent event) {
        if (!comboZonas.getValue().contentEquals("Todas")) {
            String[] partzon = comboZonas.getValue().split(" - ");
            int idZ = zonasList.parallelStream().filter(zonas -> zonas.getCodigo().equals(partzon[0])).findFirst().map(Zonas::getId).orElse(0);
            comboObjetos.setItems(getObjetosList(idZ));
        }
    }

    public void handleCargarNivel(ActionEvent event) {
        if (!comboObjetos.getValue().contentEquals("Todos")) {
            String[] partObj = comboObjetos.getValue().split(" - ");
            int idObj = objetosList.parallelStream().filter(objetos -> objetos.getCodigo().equals(partObj[0])).findFirst().map(Objetos::getId).orElse(0);
            comboNiveles.setItems(getNivelList(idObj));
        }
    }

    private void listaActualizada(List<Unidadobrarenglon> listToChange, Renglonvariante reng) {
        ArrayList<Unidadobrarenglon> unidadobrarenglonArray = new ArrayList<>();
        for (Unidadobrarenglon ur : listToChange) {
            Unidadobrarenglon unidadobrarenglon = new Unidadobrarenglon();
            if (ur.getUnidadobraByUnidadobraId().getRenglonbase().equals(renglontoChange.getCodigo())) {
                updateUnidadRenglonBase(ur.getUnidadobraId(), getRenglontoIn);
            }
            if (ur.getConMat().equals("0 ")) {
                salario = 0.0;
                salario = calcSalarioRV(reng.getId(), ur.getUnidadobraByUnidadobraId().getEmpresaconstructoraId());
                unidadobrarenglon = new Unidadobrarenglon();
                unidadobrarenglon.setRenglonvarianteId(reng.getId());
                unidadobrarenglon.setUnidadobraId(ur.getUnidadobraId());
                unidadobrarenglon.setCantRv(ur.getCantRv());
                unidadobrarenglon.setCostMat(0.0);
                unidadobrarenglon.setCostEquip(ur.getCantRv() * reng.getCostequip());
                unidadobrarenglon.setCostMano(ur.getCantRv() * reng.getCostmano());
                unidadobrarenglon.setSalariomn(ur.getCantRv() * salario);
                unidadobrarenglon.setSalariocuc(0.0);
                unidadobrarenglon.setConMat("0 ");

            } else if (ur.getConMat().equals("1 ")) {
                salario = 0.0;
                salario = calcSalarioRV(reng.getId(), ur.getUnidadobraByUnidadobraId().getEmpresaconstructoraId());
                unidadobrarenglon = new Unidadobrarenglon();
                unidadobrarenglon.setRenglonvarianteId(reng.getId());
                unidadobrarenglon.setUnidadobraId(ur.getUnidadobraId());
                unidadobrarenglon.setCantRv(ur.getCantRv());
                unidadobrarenglon.setCostMat(ur.getCantRv() * reng.getCostomat());
                unidadobrarenglon.setCostEquip(ur.getCantRv() * reng.getCostequip());
                unidadobrarenglon.setCostMano(ur.getCantRv() * reng.getCostmano());
                unidadobrarenglon.setSalariomn(ur.getCantRv() * salario);
                unidadobrarenglon.setSalariocuc(0.0);
                unidadobrarenglon.setConMat("1 ");

            }
            unidadobrarenglonArray.add(unidadobrarenglon);

        }
        deleteUnidadObraRenglons(listToChange);
        updateUnidadObraRenglons(unidadobrarenglonArray);

    }

    private void listaActualizadaBajo(List<Bajoespecificacion> listToChange, String code) {
        ArrayList<Bajoespecificacion> bajoespecificacionArrayList = new ArrayList<>();
        List<Certificacionrecuo> allCertificacionrecuoList = new ArrayList<>();
        for (Bajoespecificacion ur : listToChange) {
            allCertificacionrecuoList.addAll(getAllCertificacRe(ur.getUnidadobraId()));
            Bajoespecificacion bajo = new Bajoespecificacion();
            bajo.setUnidadobraId(ur.getUnidadobraId());
            bajo.setSumrenglon(ur.getSumrenglon());
            bajo.setRenglonvarianteId(ur.getRenglonvarianteId());
            bajo.setCantidad(ur.getCantidad());
            if (recursosp != null) {
                bajo.setIdSuministro(recursosp.getId());
                bajo.setCosto(recursosp.getPreciomn() * ur.getCantidad());
                bajo.setTipo(recursosp.getTipo());
            }
            if (juegoproductop != null) {
                bajo.setIdSuministro(juegoproductop.getId());
                bajo.setCosto(juegoproductop.getPreciomn() * ur.getCantidad());
                bajo.setTipo("J");
            }

            if (suministrossemielaboradosp != null) {
                bajo.setIdSuministro(suministrossemielaboradosp.getId());
                bajo.setCosto(suministrossemielaboradosp.getPreciomn() * ur.getCantidad());
                bajo.setTipo("S");
            }
            bajoespecificacionArrayList.add(bajo);
        }
        deleteUnidadBajo(listToChange);
        updateUnidadBajo(bajoespecificacionArrayList);
        recalculaCostosUO(bajoespecificacionArrayList);
        updateIdRecInCerti(allCertificacionrecuoList);


    }

    private void updateIdRecInCerti(List<Certificacionrecuo> allCertificacionrecuoList) {
        List<Certificacionrecuo> listToUpdate = new ArrayList<>();
        for (Certificacionrecuo certificacionrecuo : allCertificacionrecuoList) {
            if (recursosp != null) {
                certificacionrecuo.setRecursoId(recursosp.getId());
                listToUpdate.add(certificacionrecuo);
            }
            if (juegoproductop != null) {
                certificacionrecuo.setRecursoId(juegoproductop.getId());
                listToUpdate.add(certificacionrecuo);
            }
            if (suministrossemielaboradosp != null) {
                certificacionrecuo.setRecursoId(suministrossemielaboradosp.getId());
                listToUpdate.add(certificacionrecuo);
            }
        }
        updateAllDatacertifi(listToUpdate);

    }

    private void updateAllDatacertifi(List<Certificacionrecuo> listToUpdate) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;

            for (Certificacionrecuo ur : listToUpdate) {
                count++;
                if (count > 0 && count % sizeBath == 0) {
                    session.flush();
                    session.clear();
                }

                session.delete(ur);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !!");
            alert.setContentText("Error al Actualizar los datos de las Certificaciones");
            alert.showAndWait();
        }

    }

    private void recalculaCostosUO(ArrayList<Bajoespecificacion> bajoespecificacionArrayList) {
        listUnidadesObra = new ArrayList<>();
        for (Bajoespecificacion bajoespecificacion : bajoespecificacionArrayList) {
            listUnidadesObra.add(getUpdateUnidadObraList(bajoespecificacion.getUnidadobraId()));
        }

        updateUnidad(listUnidadesObra);
    }

    public void handleChangeElement(ActionEvent event) {

        if (radioRV.isSelected() == true) {
            try {

                if (comboEmpresa.getValue().trim().equals("Todas") && comboZonas.getValue() == null && comboObjetos.getValue() == null && comboNiveles.getValue() == null && comboEspecialidad.getValue() == null) {
                    unidadobrarenglonList = new ArrayList<>();
                    unidadobrarenglonList = getUnidadobrarenglonList(renglontoChange.getId(), null, null, null, null, null);
                    listaActualizada(unidadobrarenglonList, getRenglontoIn);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Renglón Variante reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && comboZonas.getValue() == null && comboObjetos.getValue() == null && comboNiveles.getValue() == null && comboEspecialidad.getValue() == null) {
                    unidadobrarenglonList = new ArrayList<>();
                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);
                    unidadobrarenglonList = getUnidadobrarenglonList(renglontoChange.getId(), empresaconstructora, null, null, null, null);
                    listaActualizada(unidadobrarenglonList, getRenglontoIn);
                    // updateAllUnidadesObra(obraId);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Renglón Variante reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null && comboNiveles.getValue() == null && comboEspecialidad.getValue() == null) {
                    unidadobrarenglonList = new ArrayList<>();
                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);
                    unidadobrarenglonList = getUnidadobrarenglonList(renglontoChange.getId(), empresaconstructora, null, null, null, null);
                    listaActualizada(unidadobrarenglonList, getRenglontoIn);
                    // updateAllUnidadesObra(obraId);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Renglón Variante reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null && comboNiveles.getValue() == null && comboEspecialidad.getValue() == null) {
                    unidadobrarenglonList = new ArrayList<>();
                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);
                    unidadobrarenglonList = getUnidadobrarenglonList(renglontoChange.getId(), empresaconstructora, zonas, null, null, null);
                    listaActualizada(unidadobrarenglonList, getRenglontoIn);
                    //updateAllUnidadesObra(obraId);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Renglón Variante reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && comboObjetos.getValue().equals("Todos") && comboNiveles.getValue() == null && comboEspecialidad.getValue() == null) {
                    unidadobrarenglonList = new ArrayList<>();
                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);
                    unidadobrarenglonList = getUnidadobrarenglonList(renglontoChange.getId(), empresaconstructora, zonas, null, null, null);
                    listaActualizada(unidadobrarenglonList, getRenglontoIn);
                    // updateAllUnidadesObra(obraId);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Renglón Variante reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && !comboObjetos.getValue().equals("Todos") && comboNiveles.getValue() == null && comboEspecialidad.getValue() == null) {
                    unidadobrarenglonList = new ArrayList<>();
                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

                    String[] partObjetos = comboObjetos.getValue().split(" - ");
                    Objetos objetos = objetosList.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);
                    unidadobrarenglonList = getUnidadobrarenglonList(renglontoChange.getId(), empresaconstructora, zonas, objetos, null, null);
                    listaActualizada(unidadobrarenglonList, getRenglontoIn);
                    // updateAllUnidadesObra(obraId);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Renglón Variante reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && !comboObjetos.getValue().equals("Todos") && comboNiveles.getValue().equals("Todos") && comboEspecialidad.getValue() == null) {
                    unidadobrarenglonList = new ArrayList<>();
                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

                    String[] partObjetos = comboObjetos.getValue().split(" - ");
                    Objetos objetos = objetosList.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);
                    unidadobrarenglonList = getUnidadobrarenglonList(renglontoChange.getId(), empresaconstructora, zonas, objetos, null, null);
                    listaActualizada(unidadobrarenglonList, getRenglontoIn);
                    // updateAllUnidadesObra(obraId);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Renglón Variante reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && !comboObjetos.getValue().equals("Todos") && !comboNiveles.getValue().equals("Todos") && comboEspecialidad.getValue() == null) {
                    unidadobrarenglonList = new ArrayList<>();
                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

                    String[] partObjetos = comboObjetos.getValue().split(" - ");
                    Objetos objetos = objetosList.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);

                    String[] partNiveles = comboNiveles.getValue().split(" - ");
                    Nivel nivel = nivelList.parallelStream().filter(nivel1 -> nivel1.getCodigo().equals(partNiveles[0])).findFirst().orElse(null);
                    unidadobrarenglonList = getUnidadobrarenglonList(renglontoChange.getId(), empresaconstructora, zonas, objetos, nivel, null);
                    listaActualizada(unidadobrarenglonList, getRenglontoIn);
                    // updateAllUnidadesObra(obraId);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Renglón Variante reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && !comboObjetos.getValue().equals("Todos") && !comboNiveles.getValue().equals("Todos") && comboEspecialidad.getValue().equals("Todas")) {
                    unidadobrarenglonList = new ArrayList<>();
                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

                    String[] partObjetos = comboObjetos.getValue().split(" - ");
                    Objetos objetos = objetosList.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);

                    String[] partNiveles = comboNiveles.getValue().split(" - ");
                    Nivel nivel = nivelList.parallelStream().filter(nivel1 -> nivel1.getCodigo().equals(partNiveles[0])).findFirst().orElse(null);
                    unidadobrarenglonList = getUnidadobrarenglonList(renglontoChange.getId(), empresaconstructora, zonas, objetos, nivel, null);
                    listaActualizada(unidadobrarenglonList, getRenglontoIn);
                    //updateAllUnidadesObra(obraId);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Renglón Variante reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && !comboObjetos.getValue().equals("Todos") && !comboNiveles.getValue().equals("Todos") && !comboEspecialidad.getValue().equals("Todas")) {
                    unidadobrarenglonList = new ArrayList<>();
                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

                    String[] partObjetos = comboObjetos.getValue().split(" - ");
                    Objetos objetos = objetosList.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);

                    String[] partNiveles = comboNiveles.getValue().split(" - ");
                    Nivel nivel = nivelList.parallelStream().filter(nivel1 -> nivel1.getCodigo().equals(partNiveles[0])).findFirst().orElse(null);

                    String[] parEspecialidades = comboEspecialidad.getValue().split(" - ");
                    Especialidades especialidades = especialidadesList.parallelStream().filter(especialidades1 -> especialidades1.getCodigo().equals(parEspecialidades[0])).findFirst().orElse(null);

                    unidadobrarenglonList = getUnidadobrarenglonList(renglontoChange.getId(), empresaconstructora, zonas, objetos, nivel, especialidades);
                    listaActualizada(unidadobrarenglonList, getRenglontoIn);
                    // updateAllUnidadesObra(obraId);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Renglón Variante reemplazado");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText("Ocurrió un error al cambiar el renglón variante");
                alert.showAndWait();
            }
        }

        if (radioSuminst.isSelected() == true) {
            try {
                System.out.println(idSumiChange);
                if (comboEmpresa.getValue().trim().equals("Todas") && comboZonas.getValue() == null && comboObjetos.getValue() == null && comboNiveles.getValue() == null && comboEspecialidad.getValue() == null && comboSub.getValue() == null) {
                    datosBajoEspecificacionToUpdates = new ArrayList<>();
                    datosBajoEspecificacionToUpdates = getBajoespecificacionListList(idSumiChange, null, null, null, null, null, null);
                    listaActualizadaBajo(datosBajoEspecificacionToUpdates, fieldElementInt.getText());
                    updateCartaLimite(idSumiChange);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Suministro reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && comboZonas.getValue() == null && comboObjetos.getValue() == null && comboNiveles.getValue() == null && comboEspecialidad.getValue() == null && comboSub.getValue() == null) {
                    datosBajoEspecificacionToUpdates = new ArrayList<>();
                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);
                    datosBajoEspecificacionToUpdates = getBajoespecificacionListList(idSumiChange, empresaconstructora, null, null, null, null, null);
                    listaActualizadaBajo(datosBajoEspecificacionToUpdates, fieldElementInt.getText());
                    updateCartaLimiteEmpresa(idSumiChange, empresaconstructora.getId());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Suministro reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null && comboNiveles.getValue() == null && comboEspecialidad.getValue() == null && comboSub.getValue() == null) {
                    datosBajoEspecificacionToUpdates = new ArrayList<>();

                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    datosBajoEspecificacionToUpdates = getBajoespecificacionListList(idSumiChange, empresaconstructora, null, null, null, null, null);
                    listaActualizadaBajo(datosBajoEspecificacionToUpdates, fieldElementInt.getText());
                    updateCartaLimiteEmpresa(idSumiChange, empresaconstructora.getId());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Suministro reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null && comboNiveles.getValue() == null && comboEspecialidad.getValue() == null && comboSub.getValue() == null) {
                    datosBajoEspecificacionToUpdates = new ArrayList<>();

                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

                    datosBajoEspecificacionToUpdates = getBajoespecificacionListList(idSumiChange, empresaconstructora, zonas, null, null, null, null);
                    listaActualizadaBajo(datosBajoEspecificacionToUpdates, fieldElementInt.getText());
                    updateCartaLimiteEmpresa(idSumiChange, empresaconstructora.getId());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Suministro reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && comboObjetos.getValue().equals("Todos") && comboNiveles.getValue() == null && comboEspecialidad.getValue() == null && comboSub.getValue() == null) {
                    datosBajoEspecificacionToUpdates = new ArrayList<>();

                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

                    datosBajoEspecificacionToUpdates = getBajoespecificacionListList(idSumiChange, empresaconstructora, zonas, null, null, null, null);
                    listaActualizadaBajo(datosBajoEspecificacionToUpdates, fieldElementInt.getText());
                    updateCartaLimiteEmpresa(idSumiChange, empresaconstructora.getId());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Suministro reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && !comboObjetos.getValue().equals("Todos") && comboNiveles.getValue() == null && comboEspecialidad.getValue() == null && comboSub.getValue() == null) {
                    datosBajoEspecificacionToUpdates = new ArrayList<>();

                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

                    String[] partObjetos = comboObjetos.getValue().split(" - ");
                    Objetos objetos = objetosList.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);

                    datosBajoEspecificacionToUpdates = getBajoespecificacionListList(idSumiChange, empresaconstructora, zonas, objetos, null, null, null);
                    listaActualizadaBajo(datosBajoEspecificacionToUpdates, fieldElementInt.getText());
                    updateCartaLimiteEmpresa(idSumiChange, empresaconstructora.getId());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Suministro reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && !comboObjetos.getValue().equals("Todos") && comboNiveles.getValue().equals("Todos") && comboEspecialidad.getValue() == null && comboSub.getValue() == null) {
                    datosBajoEspecificacionToUpdates = new ArrayList<>();

                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

                    String[] partObjetos = comboObjetos.getValue().split(" - ");
                    Objetos objetos = objetosList.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);

                    datosBajoEspecificacionToUpdates = getBajoespecificacionListList(idSumiChange, empresaconstructora, zonas, objetos, null, null, null);
                    listaActualizadaBajo(datosBajoEspecificacionToUpdates, fieldElementInt.getText());
                    updateCartaLimiteEmpresa(idSumiChange, empresaconstructora.getId());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Suministro reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && !comboObjetos.getValue().equals("Todos") && !comboNiveles.getValue().equals("Todos") && comboEspecialidad.getValue() == null && comboSub.getValue() == null) {
                    datosBajoEspecificacionToUpdates = new ArrayList<>();

                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

                    String[] partObjetos = comboObjetos.getValue().split(" - ");
                    Objetos objetos = objetosList.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);

                    String[] partNiveles = comboNiveles.getValue().split(" - ");
                    Nivel nivel = nivelList.parallelStream().filter(nivel1 -> nivel1.getCodigo().equals(partNiveles[0])).findFirst().orElse(null);

                    datosBajoEspecificacionToUpdates = getBajoespecificacionListList(idSumiChange, empresaconstructora, zonas, objetos, nivel, null, null);
                    listaActualizadaBajo(datosBajoEspecificacionToUpdates, fieldElementInt.getText());
                    updateCartaLimiteEmpresa(idSumiChange, empresaconstructora.getId());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Suministro reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && !comboObjetos.getValue().equals("Todos") && !comboNiveles.getValue().equals("Todos") && comboEspecialidad.getValue().equals("Todas") && comboSub.getValue() == null) {
                    datosBajoEspecificacionToUpdates = new ArrayList<>();

                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

                    String[] partObjetos = comboObjetos.getValue().split(" - ");
                    Objetos objetos = objetosList.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);

                    String[] partNiveles = comboNiveles.getValue().split(" - ");
                    Nivel nivel = nivelList.parallelStream().filter(nivel1 -> nivel1.getCodigo().equals(partNiveles[0])).findFirst().orElse(null);

                    datosBajoEspecificacionToUpdates = getBajoespecificacionListList(idSumiChange, empresaconstructora, zonas, objetos, nivel, null, null);
                    listaActualizadaBajo(datosBajoEspecificacionToUpdates, fieldElementInt.getText());
                    updateCartaLimiteEmpresa(idSumiChange, empresaconstructora.getId());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Suministro reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && !comboObjetos.getValue().equals("Todos") && !comboNiveles.getValue().equals("Todos") && !comboEspecialidad.getValue().equals("Todas") && comboSub.getValue() == null) {
                    datosBajoEspecificacionToUpdates = new ArrayList<>();

                    String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                    Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                    String[] partZonas = comboZonas.getValue().split(" - ");
                    Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

                    String[] partObjetos = comboObjetos.getValue().split(" - ");
                    Objetos objetos = objetosList.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);

                    String[] partNiveles = comboNiveles.getValue().split(" - ");
                    Nivel nivel = nivelList.parallelStream().filter(nivel1 -> nivel1.getCodigo().equals(partNiveles[0])).findFirst().orElse(null);

                    String[] parEspecialidades = comboEspecialidad.getValue().split(" - ");
                    Especialidades especialidades = especialidadesList.parallelStream().filter(especialidades1 -> especialidades1.getCodigo().equals(parEspecialidades[0])).findFirst().orElse(null);

                    datosBajoEspecificacionToUpdates = getBajoespecificacionListList(idSumiChange, empresaconstructora, zonas, objetos, nivel, especialidades, null);
                    listaActualizadaBajo(datosBajoEspecificacionToUpdates, fieldElementInt.getText());
                    updateCartaLimiteEmpresa(idSumiChange, empresaconstructora.getId());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText("Suministro reemplazado");
                    alert.showAndWait();
                } else if (!comboEmpresa.getValue().trim().equals("Todas") && !comboZonas.getValue().equals("Todas") && !comboObjetos.getValue().equals("Todos") && !comboNiveles.getValue().equals("Todos") && !comboEspecialidad.getValue().equals("Todas") && comboSub.getValue() != null) {
                    if (tableUnidad.getItems().size() > 0) {
                        ObservableList<UniddaObraToIMportView> selectedUOList = FXCollections.observableArrayList();
                        selectedUOList.addAll(tableUnidad.getItems().parallelStream().filter(item -> item.getCodigo().isSelected() == true).collect(Collectors.toList()));
                        try {
                            changeSuminInUO(selectedUOList, idSumiChange);
                            utilCalc.showAlert("Suministro reemplazado", 1);
                        } catch (Exception e) {
                            utilCalc.showAlert("Error al reemplazar el suministro", 3);
                        }
                    } else {
                        datosBajoEspecificacionToUpdates = new ArrayList<>();
                        String[] partEmpresa = comboEmpresa.getValue().trim().split(" - ");
                        Empresaconstructora empresaconstructora = empresaconstructoraList.parallelStream().filter(empresa -> empresa.getCodigo().equals(partEmpresa[0])).findFirst().orElse(null);

                        String[] partZonas = comboZonas.getValue().split(" - ");
                        Zonas zonas = zonasList.parallelStream().filter(zonas1 -> zonas1.getCodigo().equals(partZonas[0])).findFirst().orElse(null);

                        String[] partObjetos = comboObjetos.getValue().split(" - ");
                        Objetos objetos = objetosList.parallelStream().filter(objetos1 -> objetos1.getCodigo().equals(partObjetos[0])).findFirst().orElse(null);

                        String[] partNiveles = comboNiveles.getValue().split(" - ");
                        Nivel nivel = nivelList.parallelStream().filter(nivel1 -> nivel1.getCodigo().equals(partNiveles[0])).findFirst().orElse(null);

                        String[] parEspecialidades = comboEspecialidad.getValue().split(" - ");
                        Especialidades especialidades = especialidadesList.parallelStream().filter(especialidades1 -> especialidades1.getCodigo().equals(parEspecialidades[0])).findFirst().orElse(null);

                        Subespecialidades subespecialidades = subespecialidadesList.parallelStream().filter(item -> item.toString().trim().equals(comboSub.getValue().trim())).findFirst().get();

                        datosBajoEspecificacionToUpdates = getBajoespecificacionListList(idSumiChange, empresaconstructora, zonas, objetos, nivel, especialidades, subespecialidades);
                        listaActualizadaBajo(datosBajoEspecificacionToUpdates, fieldElementInt.getText());
                        updateCartaLimiteEmpresa(idSumiChange, empresaconstructora.getId());
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("OK!");
                        alert.setContentText("Suministro reemplazado");
                        alert.showAndWait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText("Ocurrió un error al cambiar el suministros");
                alert.showAndWait();
            }
        }

    }

    private void changeSuminInUO(ObservableList<UniddaObraToIMportView> selectedUOList, int idSumiChange) {
        ArrayList<Bajoespecificacion> bajoespecificacionArrayList = new ArrayList<>();
        List<Certificacionrecuo> allCertificacionrecuoList = new ArrayList<>();
        List<Bajoespecificacion> toDeleteList = new ArrayList<>();
        for (UniddaObraToIMportView tableUO : selectedUOList) {
            Unidadobra unidadobra = utilCalc.getUnidadobra(tableUO.getId());
            Bajoespecificacion bajoespecificacion = unidadobra.getUnidadobrabajoespecificacionById().parallelStream().filter(item -> item.getIdSuministro() == idSumiChange).findFirst().get();

            allCertificacionrecuoList.addAll(getAllCertificacRe(tableUO.getId()));
            if (recursosp != null) {
                r_id = recursosp.getId();
                r_costo = recursosp.getPreciomn() * bajoespecificacion.getCantidad();
                r_tipo = recursosp.getTipo();
                r_precio = recursosp.getPreciomn();
            }
            if (juegoproductop != null) {
                r_id = juegoproductop.getId();
                r_costo = juegoproductop.getPreciomn() * bajoespecificacion.getCantidad();
                r_tipo = "J";
                r_precio = juegoproductop.getPreciomn();
            }

            if (suministrossemielaboradosp != null) {
                r_id = suministrossemielaboradosp.getId();
                r_costo = suministrossemielaboradosp.getPreciomn() * bajoespecificacion.getCantidad();
                r_tipo = "S";
                r_precio = suministrossemielaboradosp.getPreciomn();
            }
            Bajoespecificacion bajo = new Bajoespecificacion();
            bajo.setUnidadobraId(bajoespecificacion.getUnidadobraId());
            bajo.setSumrenglon(bajoespecificacion.getSumrenglon());
            bajo.setRenglonvarianteId(bajoespecificacion.getRenglonvarianteId());
            bajo.setIdSuministro(r_id);
            bajo.setCosto(r_costo);
            bajo.setTipo(r_tipo);
            bajo.setCantidad(bajoespecificacion.getCantidad());
            if (IsPartOfInUO(unidadobra, bajo)) {
                updateSuministroRep(bajo);
                toDeleteList.add(bajoespecificacion);
            } else {
                bajoespecificacionArrayList.add(bajo);
                toDeleteList.add(bajoespecificacion);
            }
        }
        deleteUnidadBajo(toDeleteList);
        updateUnidadBajo(bajoespecificacionArrayList);
        recalculaCostosUO(bajoespecificacionArrayList);
        updateIdRecInCerti(allCertificacionrecuoList);
    }

    private boolean IsPartOfInUO(Unidadobra unidadobra, Bajoespecificacion bajo) {
        boolean flag = false;
        Bajoespecificacion bajoespecificacion = unidadobra.getUnidadobrabajoespecificacionById().parallelStream().filter(item -> item.getIdSuministro() == bajo.getIdSuministro()).findFirst().get();
        if (bajoespecificacion != null) {
            flag = true;
        }
        return flag;
    }


    private void updateCartaLimite(int sum) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            if (recursosp != null) {
                int op1 = session.createQuery(" UPDATE Despachoscl SET suministroId =: newS WHERE suministroId =: oldS ").setParameter("newS", recursosp.getId()).setParameter("oldS", sum).executeUpdate();
            }

            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !!");
            alert.setContentText("Error al Actualizar los datos de la Carta Limite");
            alert.showAndWait();
        }
    }

    private void updateCartaLimiteEmpresa(int sum, int idEmpresa) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            if (recursosp != null) {
                int op1 = session.createQuery(" UPDATE Despachoscl SET suministroId =: newS WHERE suministroId =: oldS AND empresaId =: idEm ").setParameter("newS", recursosp.getId()).setParameter("oldS", sum).setParameter("idEm", idEmpresa).executeUpdate();
            }

            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !!");
            alert.setContentText("Error al Actualizar los datos de la Carta Limite");
            alert.showAndWait();
        }
    }


    private void updateSuministroRep(Bajoespecificacion bajoespec) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Bajoespecificacion bajo = (Bajoespecificacion) session.createQuery("FROM Bajoespecificacion WHERE idSuministro =: idS AND unidadobraId =: idU").setParameter("idS", bajoespec.getIdSuministro()).setParameter("idU", bajoespec.getUnidadobraId()).getSingleResult();
            System.out.println(" ****** " + bajoespec.getCantidad() + " ***** ");
            cant = bajo.getCantidad() + bajoespec.getCantidad();
            costo = r_precio * cant;
            System.out.println(" ****** " + cant + " ***** ");
            bajo.setCosto(costo);
            bajo.setCantidad(cant);
            session.update(bajo);
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !!");
            alert.setContentText("Error al Actualizar los datos de la Carta Limite");
            alert.showAndWait();
        }
    }

    /*
    private void updateAllUnidadesObra(int obraId) {
        list = new ArrayList<>();
        list = unidadobraArrayList(obraId);
        updateUnidad(list);

    }

*/
    public void handleClose(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }


}
