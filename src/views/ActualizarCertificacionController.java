package views;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ActualizarCertificacionController implements Initializable {

    private static SessionFactory sf;
    @FXML
    private JFXComboBox<String> combo_Brigada;
    @FXML
    private JFXComboBox<String> combogrupo;
    @FXML
    private JFXComboBox<String> combocuadrilla;
    @FXML
    private JFXTextField fieldcodigouo;
    @FXML
    private JFXTextField fieldcantidad;
    @FXML
    private Label labelDispo;
    @FXML
    private JFXDatePicker dateini;
    @FXML
    private JFXDatePicker datehasta;
    private ArrayList<Unidadobra> unidadobraArrayList;
    private ObservableList<String> listUO;
    private ArrayList<Brigadaconstruccion> brigadaconstruccionsArrayList;
    private ArrayList<Grupoconstruccion> grupoconstruccionArrayList;
    private ArrayList<Cuadrillaconstruccion> cuadrillaconstruccionsArrayList;
    private ObservableList<String> brigadasList;
    private ObservableList<String> grupoList;
    private ObservableList<String> cuadrilaList;
    private Unidadobra unidadobraModel;
    private int unidadObraId;
    private int brigadaId;
    private int grupoId;
    private int cuadrillaId;
    private int idCertif;
    private String code;
    private double costMaterial;
    private double costMaterialCert;
    private double costMano;
    private double costManoCert;
    private double costEquipo;
    private double costEquipoCert;
    private double costSalario;
    private double costSalarioCert;
    private double costCUCSalario;
    private double costCUCSalarioCert;
    private double valCM;
    private double valCMan;
    private double valEquip;
    private double valSal;
    private double valCucSal;
    private Date desde;
    private Date hasta;
    private double cantidadCert;
    private Double[] certValues;
    private Certificacion certificacion;
    private ArrayList<Certificacion> certificacionArrayList;

    private ObservableList<String> getUnidadesObra(int idObra, int idEmp, int idZona, int idObj, int idNiv, int idEsp, int idSub) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listUO = FXCollections.observableArrayList();
            Query query = session.createQuery("FROM Unidadobra where obraId = :idObra and empresaconstructoraId =:idEmp AND zonasId = :idZona and objetosId =: idObjeto and nivelId =: idNivel and especialidadesId =: idEspecilidad and subespecialidadesId =:idSub");
            query.setParameter("idObra", idObra);
            query.setParameter("idEmp", idEmp);
            query.setParameter("idZona", idZona);
            query.setParameter("idObjeto", idObj);
            query.setParameter("idNivel", idNiv);
            query.setParameter("idEspecilidad", idEsp);
            query.setParameter("idSub", idSub);
            unidadobraArrayList = (ArrayList<Unidadobra>) ((org.hibernate.query.Query) query).getResultList();
            listUO.addAll(unidadobraArrayList.parallelStream().map(uo -> uo.getCodigo() + " " + uo.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return listUO;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ArrayList<Certificacion> getCertificaciones() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            certificacionArrayList = new ArrayList<Certificacion>();
            certificacionArrayList = (ArrayList<Certificacion>) session.createQuery("FROM Certificacion ").getResultList();

            tx.commit();
            session.close();
            return certificacionArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public Certificacion getCertificaciones(int idCer) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            certificacion = session.get(Certificacion.class, idCer);
            tx.commit();
            session.close();
            return certificacion;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return certificacion;
    }

    private Unidadobra getUnidadesObra(String codeig) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            unidadobraModel = (Unidadobra) session.createQuery("FROM Unidadobra WHERE codigo =: codg ").setParameter("codg", codeig).getSingleResult();

            tx.commit();
            session.close();
            return unidadobraModel;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return unidadobraModel;
    }


    public ObservableList<String> listOfBrigadas(int idEmp) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            brigadasList = FXCollections.observableArrayList();
            brigadaconstruccionsArrayList = (ArrayList<Brigadaconstruccion>) session.createQuery("FROM Brigadaconstruccion WHERE empresaconstructoraId =: empId").setParameter("empId", idEmp).getResultList();
            brigadasList.addAll(brigadaconstruccionsArrayList.parallelStream().map(brigadaconstruccion -> brigadaconstruccion.getCodigo() + " - " + brigadaconstruccion.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return brigadasList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }


    public ObservableList<String> listOfGrupos(int idBrigada) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            grupoList = FXCollections.observableArrayList();
            grupoconstruccionArrayList = (ArrayList<Grupoconstruccion>) session.createQuery("FROM Grupoconstruccion WHERE brigadaconstruccionId =: briId").setParameter("briId", idBrigada).getResultList();
            grupoList.addAll(grupoconstruccionArrayList.parallelStream().map(grup -> grup.getCodigo() + " - " + grup.getDescripcion()).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return grupoList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> listOfCuadrilla(int idGrupo) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            cuadrilaList = FXCollections.observableArrayList();
            cuadrillaconstruccionsArrayList = (ArrayList<Cuadrillaconstruccion>) session.createQuery("FROM Cuadrillaconstruccion WHERE grupoconstruccionId =: grupId").setParameter("grupId", idGrupo).getResultList();
            cuadrilaList.addAll(cuadrillaconstruccionsArrayList.parallelStream().map(cuadrillaconstruccion -> cuadrillaconstruccion.getCodigo() + " - " + cuadrillaconstruccion.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return cuadrilaList;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void UpdateCertificacion(Certificacion cer) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            certificacion = session.get(Certificacion.class, idCertif);
            if (certificacion != null) {
                certificacion.setCostmaterial(cer.getCostmaterial());
                certificacion.setCostmano(cer.getCostmano());
                certificacion.setCostequipo(cer.getCostequipo());
                certificacion.setSalario(cer.getSalario());
                certificacion.setCucsalario(cer.getCucsalario());
                certificacion.setBrigadaconstruccionId(cer.getBrigadaconstruccionId());
                certificacion.setGrupoconstruccionId(cer.getGrupoconstruccionId());
                certificacion.setCuadrillaconstruccionId(cer.getCuadrillaconstruccionId());
                certificacion.setUnidadobraId(cer.getUnidadobraId());
                certificacion.setDesde(cer.getDesde());
                certificacion.setHasta(cer.getHasta());
                certificacion.setCantidad(cer.getCantidad());
                session.update(certificacion);
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public Double getCantCertificada(int idUnidadObra) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            cantidadCert = 0.0;
            Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacion WHERE unidadobraId =: id").setParameter("id", idUnidadObra);
            if (query.getSingleResult() != null) {
                cantidadCert = (Double) query.getSingleResult();
            }

            tx.commit();
            session.close();
            return cantidadCert;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return 0.0;
    }


    public void cargarCertificacion(int idCertific, String codeUO) {


        certificacion = getCertificaciones(idCertific);
        idCertif = certificacion.getId();

        unidadobraModel = getUnidadesObra(codeUO);


        brigadasList = listOfBrigadas(unidadobraModel.getEmpresaconstructoraId());
        grupoList = listOfGrupos(certificacion.getBrigadaconstruccionId());
        brigadaId = certificacion.getBrigadaconstruccionId();
        combogrupo.setItems(grupoList);
        grupoId = certificacion.getGrupoconstruccionId();
        cuadrilaList = listOfCuadrilla(certificacion.getGrupoconstruccionId());
        cuadrillaId = certificacion.getCuadrillaconstruccionId();
        combocuadrilla.setItems(cuadrilaList);

        combo_Brigada.setItems(brigadasList);

        cantidadCert = 0.0;
        cantidadCert = getCantCertificada(unidadobraModel.getId());


        fieldcodigouo.setText(unidadobraModel.getCodigo());
        fieldcantidad.setText(String.valueOf(certificacion.getCantidad()));
        labelDispo.setText(String.valueOf(unidadobraModel.getCantidad() - cantidadCert));

/*
        cantidadCert = 0.0;
        cantidadCert = 0.0;
        costMaterialCert = 0.0;
        costManoCert = 0.0;
        costEquipoCert = 0.0;
        costCUCSalarioCert = 0.0;
        costSalarioCert = 0.0;

        certificacionArrayList.forEach(cert -> {
            if (cert.getUnidadobraId() == certificacion.getUnidadobraId()) {
                cantidadCert += cert.getCantidad();
                costMaterialCert += cert.getCostmaterial();
                costManoCert += cert.getCostmano();
                costEquipoCert += cert.getCostequipo();
                costCUCSalarioCert += cert.getCucsalario();
                costSalarioCert += cert.getSalario();

            }
        });
*/

/*
        String selectedModel = brigadaconstruccionsArrayList.parallelStream().filter(brigadaconstruccion -> brigadaconstruccion.getId() == certificacion.getBrigadaconstruccionId()).findFirst().map(brc -> brc.getCodigo() + " - " + brc.getDescripcion()).orElse(null);
        combo_Brigada.getSelectionModel().select(selectedModel);
*/
        brigadaconstruccionsArrayList.forEach(brigadaconstruccion -> {
            if (brigadaconstruccion.getId() == certificacion.getBrigadaconstruccionId()) {
                combo_Brigada.getSelectionModel().select(brigadaconstruccion.getCodigo() + " - " + brigadaconstruccion.getDescripcion());
            }
        });

        grupoconstruccionArrayList.forEach(grupoconstruccion -> {
            if (grupoconstruccion.getId() == certificacion.getGrupoconstruccionId()) {
                combogrupo.getSelectionModel().select(grupoconstruccion.getCodigo() + " - " + grupoconstruccion.getDescripcion());
            }
        });

        cuadrillaconstruccionsArrayList.forEach(cuadrillaconstruccion -> {
            if (cuadrillaconstruccion.getId() == certificacion.getCuadrillaconstruccionId()) {
                combocuadrilla.getSelectionModel().select(cuadrillaconstruccion.getCodigo() + " - " + cuadrillaconstruccion.getDescripcion());
            }
        });

        LocalDate dateDesde = certificacion.getDesde().toLocalDate();
        LocalDate dateHasta = certificacion.getHasta().toLocalDate();
        dateini.setValue(dateDesde);
        datehasta.setValue(dateHasta);
    }


    public void handleDefineUO(ActionEvent event) {

        String[] partes = fieldcodigouo.getText().split(" - ");
        String code = partes[0];
        fieldcodigouo.clear();
        fieldcodigouo.setText(code);

        unidadobraArrayList.forEach(unidadobra -> {
            if (unidadobra.getCodigo().contentEquals(code)) {
                unidadobraModel = unidadobra;

            }
        });
        cantidadCert = 0.0;
        costMaterialCert = 0.0;
        costManoCert = 0.0;
        costEquipoCert = 0.0;
        costCUCSalarioCert = 0.0;
        costSalarioCert = 0.0;
        certificacionArrayList.forEach(certific -> {
            if (certific.getUnidadobraId() == unidadobraModel.getId()) {
                cantidadCert += certific.getCantidad();
                costMaterialCert += certific.getCostmaterial();
                costManoCert += certific.getCostmano();
                costEquipoCert += certific.getCostequipo();
                costCUCSalarioCert += certific.getCucsalario();
                costSalarioCert += certific.getSalario();
            }
        });
        double val = unidadobraModel.getCantidad() - cantidadCert;
        if (val == 0.0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Cantidad a certificar no disponible!");
            alert.showAndWait();
        } else {
            labelDispo.setText(String.valueOf(Math.round(val * 100d) / 100d));
        }
    }

    public void handleLlenaGrupoList(ActionEvent event) {
        String[] codeBriPart = combo_Brigada.getValue().split(" - ");
        String code = codeBriPart[0];

        brigadaconstruccionsArrayList.forEach(brigadaconstruccion -> {
            if (brigadaconstruccion.getCodigo().contentEquals(code)) {
                brigadaId = brigadaconstruccion.getId();
                grupoList = listOfGrupos(brigadaconstruccion.getId());
                combogrupo.setItems(grupoList);
            }

        });
    }

    public void handleLlenaCuadrillaList(ActionEvent event) {
        String[] codeGrupPart = combogrupo.getValue().split(" - ");
        String code = codeGrupPart[0];

        grupoconstruccionArrayList.forEach(grupoconstruccion -> {
            if (grupoconstruccion.getCodigo().contentEquals(code)) {
                grupoId = grupoconstruccion.getId();
                cuadrilaList = listOfCuadrilla(grupoconstruccion.getId());
                combocuadrilla.setItems(cuadrilaList);
            }

        });
    }


    public Double[] getValoresCertificados(int idUnidadObra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            certValues = new Double[5];
            List<Object[]> datosCertificaciones = session.createQuery("SELECT SUM(costmaterial), SUM(costmano), SUM(costequipo), SUM(salario), SUM(cucsalario) FROM Certificacion WHERE unidadobraId =: id").setParameter("id", idUnidadObra).getResultList();
            for (Object[] row : datosCertificaciones) {

                if (row[0] != null) {
                    certValues[0] = Double.parseDouble(row[0].toString());
                } else {
                    certValues[0] = 0.0;
                }
                if (row[1] != null) {
                    certValues[1] = Double.parseDouble(row[1].toString());
                } else {
                    certValues[1] = 0.0;
                }
                if (row[2] != null) {
                    certValues[2] = Double.parseDouble(row[2].toString());
                } else {
                    certValues[2] = 0.0;
                }
                if (row[3] != null) {
                    certValues[3] = Double.parseDouble(row[3].toString());
                } else {
                    certValues[3] = 0.0;
                }

                if (row[4] != null) {
                    certValues[4] = Double.parseDouble(row[4].toString());
                } else {
                    certValues[4] = 0.0;
                }


            }

            tx.commit();
            session.close();
            return certValues;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Double[5];
    }


    public void handleAcatualizarCertificacion(ActionEvent event) {


        if (Double.parseDouble(fieldcantidad.getText()) > Double.parseDouble(labelDispo.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Cantidad a certificar mayor que la cantidad disponible!");
            alert.showAndWait();
        } else if (combo_Brigada.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Debe especificar una Brigada de Contrucción!");
            alert.showAndWait();
        } else if (combogrupo.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Debe especificar un Grupo de Contrucción!");
            alert.showAndWait();
        } else if (combocuadrilla.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Debe especificar una Cuadrilla de Contrucción!");
            alert.showAndWait();
        } else if (dateini.getValue() == null || datehasta.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Debe especificar periódo de tiempo para la certificación!");
            alert.showAndWait();
        } else {

            String[] codeCuadriPart = combocuadrilla.getValue().split(" - ");
            String code = codeCuadriPart[0];

            unidadObraId = unidadobraModel.getId();

            cuadrillaconstruccionsArrayList.forEach(cuadrillaconstruccion -> {
                if (cuadrillaconstruccion.getCodigo().contentEquals(code)) {
                    cuadrillaId = cuadrillaconstruccion.getId();
                }
            });

            valCM = 0.0;
            valCMan = 0.0;
            valEquip = 0.0;
            valSal = 0.0;
            valCucSal = 0.0;

            costMaterial = 0.0;
            costMano = 0.0;
            costEquipo = 0.0;
            costSalario = 0.0;
            costCUCSalario = 0.0;

            certValues = getValoresCertificados(unidadObraId);
            double mat = unidadobraModel.getCostoMaterial();
            System.out.println("Materiales " + mat);

            valCM = unidadobraModel.getCostoMaterial() - certValues[0];
            valCMan = unidadobraModel.getCostomano() - certValues[1];
            valEquip = unidadobraModel.getCostoequipo() - certValues[2];
            valSal = unidadobraModel.getSalario() - certValues[3];
            valCucSal = unidadobraModel.getSalariocuc() - certValues[4];

            costMaterial = valCM * Double.parseDouble(fieldcantidad.getText()) / unidadobraModel.getCantidad();
            costMano = valCMan * Double.parseDouble(fieldcantidad.getText()) / unidadobraModel.getCantidad();
            costEquipo = valEquip * Double.parseDouble(fieldcantidad.getText()) / unidadobraModel.getCantidad();
            costSalario = valSal * Double.parseDouble(fieldcantidad.getText()) / unidadobraModel.getCantidad();
            costCUCSalario = valCucSal * Double.parseDouble(fieldcantidad.getText()) / unidadobraModel.getCantidad();

            String pattern = "dd/MM/yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            LocalDate dateDes = dateini.getValue();
            LocalDate dateHast = datehasta.getValue();
            desde = Date.valueOf(dateDes);
            hasta = Date.valueOf(dateHast);


            certificacion = new Certificacion();
            certificacion.setCostmaterial(Math.round(costMaterial * 100d) / 100d);
            certificacion.setCostmano(Math.round(costMano * 100d) / 100d);
            certificacion.setCostequipo(Math.round(costEquipo * 100d) / 100d);
            certificacion.setSalario(Math.round(costSalario * 100d) / 100d);
            certificacion.setCucsalario(Math.round(costCUCSalario * 100d) / 100d);
            certificacion.setBrigadaconstruccionId(brigadaId);
            certificacion.setGrupoconstruccionId(grupoId);
            certificacion.setCuadrillaconstruccionId(cuadrillaId);
            certificacion.setUnidadobraId(unidadObraId);
            certificacion.setDesde(desde);
            certificacion.setHasta(hasta);
            certificacion.setCantidad(Double.parseDouble(fieldcantidad.getText()));


            UpdateCertificacion(certificacion);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Datos insertados satisfactoriamente!");
            alert.showAndWait();


            fieldcodigouo.clear();
            labelDispo.setText(" ");
            fieldcantidad.clear();


        }

    }

}
