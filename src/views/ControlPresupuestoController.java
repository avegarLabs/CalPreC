package views;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.*;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import models.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class ControlPresupuestoController implements Initializable {


    private static SessionFactory sf;
    Task tarea;
    private ReportProjectStructureSingelton reportProjectStructureSingelton;
    @FXML
    private Label label_title;
    @FXML
    private BorderPane mainpane;
    @FXML
    private VBox resumePane;
    private Label empresa;
    private Label zona;
    private Label objeto;
    @FXML
    private JFXTreeView listobras;
    private TreeItem treeItem;
    private ObservableList<ObrasView> obraObservableArrayList;
    private ArrayList<Obra> obraArrayList;
    private Obra obraModel;
    private ObrasView obrasView;
    private EstructuraObra estructuraObra;
    private ObservableList<ControlPresupuestoView> controlPresupuestoViewObservableList;
    private ControlPresupuestoView controlPresupuestoView;
    private ObservableList<ControlPresupuestoRVView> controlPresupuestoRVViewObservableList;
    private ControlPresupuestoRVView controlPresupuestoRVView;
    private ObservableList<ControlPresupuestoUORVView> controlPresupuestoUORVViewObservableList;
    private ControlPresupuestoUORVView controlPresupuestoUORVView;
    private ArrayList<EstructuraObra> estructuraObraArrayList;
    private ArrayList<Unidadobra> unidadobraArrayList;
    private ArrayList<Certificacion> certificacionArrayList;
    @FXML
    private JFXCheckBox valUo;
    @FXML
    private JFXCheckBox valRV;
    private ObrasController obrasController;
    private Label item;
    private double volumen;
    private double valEjecutado;
    private double porciento;
    private double pendiente;
    private double quedan;
    private Map parametros;
    private LocalDate date;
    private Empresa empresaSa;
    @FXML
    private JFXCheckBox uoCheck;
    @FXML
    private JFXCheckBox rvCheck;
    private String conceptName;
    private Integer nivel;
    private StringBuilder empresaBuilder;
    private StringBuilder zonaBuilder;
    private StringBuilder objetoBuilder;
    private StringBuilder nivelBuilder;
    private StringBuilder especialidadBuilder;
    private StringBuilder subEspBuilder;
    private StringBuilder groupBuilder;
    /**
     * para las unidades de obra
     */
    private double cantcertRV;
    /**
     * para las unidades de obra
     */
    private double cantSalarioCertRV;
    /**
     * para las unidades de obra
     */
    private double cantHHRV;
    private ProgressDialog stage;

    private ObservableList<String> listEmpresas;
    private ObservableList<String> listZonas;
    private ObservableList<String> listObjetos;
    private ObservableList<String> listNiveles;
    private ObservableList<String> listSubEspecialidades;

    @FXML
    private JFXComboBox<String> comboGroupByEmpresa;

    @FXML
    private JFXComboBox<String> comboGroupByZonas;

    @FXML
    private JFXComboBox<String> comboGroupByObjetos;

    @FXML
    private JFXComboBox<String> comboGroupByNiveles;

    @FXML
    private JFXComboBox<String> comboGroupByEspecialidades;

    @FXML
    private JFXComboBox<String> comboGroupBySubEspecialidades;

    @FXML
    private JFXButton clearComboEmpresa;

    @FXML
    private JFXButton clearComboZona;

    @FXML
    private JFXButton clearComboObjeto;

    @FXML
    private JFXButton clearComboNivel;

    @FXML
    private JFXButton clearComboEsp;

    @FXML
    private JFXButton clearComboSubEsp;


    public String createWhereClause() {
        StringBuilder whereClause = new StringBuilder();
        String tb = "uo";
        if (!uoCheck.isSelected() && !valUo.isSelected() && rvCheck.isSelected() && valRV.isSelected()) {
            tb = "ne";
        }
        if (comboGroupByEmpresa.getValue() != null) {
            nivel = 1;
            whereClause.append(" AND ").append(tb).append(".empresaconstructora__id = ").append(reportProjectStructureSingelton.getIdEmpresaByToString(comboGroupByEmpresa.getValue()));
        }

        if (comboGroupByZonas.getValue() != null && !comboGroupByZonas.getValue().contentEquals("Todas")) {
            nivel = 2;
            whereClause.append(" AND ").append(tb).append(".zonas__id = ").append(reportProjectStructureSingelton.getIdZonasByToString(comboGroupByZonas.getValue()));
        }

        if (comboGroupByObjetos.getValue() != null && !comboGroupByObjetos.getValue().contentEquals("Todos")) {
            nivel = 3;
            whereClause.append(" AND ").append(tb).append(".objetos__id = ").append(reportProjectStructureSingelton.getIdObjetosByToString(comboGroupByObjetos.getValue()));
        }

        if (comboGroupByNiveles.getValue() != null && !comboGroupByNiveles.getValue().contentEquals("Todos")) {
            Integer id;
            try {
                id = reportProjectStructureSingelton.getIdObjetosByToString(comboGroupByNiveles.getValue());
            } catch (NoSuchElementException e) {
                id = 0;
            }
            if (id != 0) {
                nivel = 4;
                whereClause.append(" AND ").append(tb).append(".nivel__id = ").append(id);
            }
        }

        if (comboGroupByEspecialidades.getValue() != null && !comboGroupByEspecialidades.getValue().contentEquals("Todas")) {
            Integer id;
            try {
                id = reportProjectStructureSingelton.getIdEspecialidasByToString(comboGroupByEspecialidades.getValue());
            } catch (NoSuchElementException e) {
                id = 0;
            }
            if (id != 0) {
                nivel = 5;
                whereClause.append(" AND ").append(tb).append(".especialidades__id = ").append(id);
            }
        }

        if (comboGroupBySubEspecialidades.getValue() != null && !comboGroupBySubEspecialidades.getValue().contentEquals("Todas")) {
            Integer id;
            try {
                id = reportProjectStructureSingelton.getIdSubespecialidadesByToString(comboGroupBySubEspecialidades.getValue());
            } catch (NoSuchElementException e) {
                id = 0;
            }
            if (id != 0) {
                nivel = 6;
                whereClause.append(" AND ").append(tb).append(".subespecialidades__id = ").append(id);
            }
        }
        return whereClause.toString();
    }

    public Empresa getEmpresa() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            empresaSa = session.get(Empresa.class, 1);


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresaSa;
    }

    public ObservableList<ObrasView> gatAllObras() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        obraObservableArrayList = FXCollections.observableArrayList();
        try {
            tx = session.beginTransaction();
            System.out.println("Ejecutando consulta... ini");
            obraArrayList = (ArrayList<Obra>) session.createQuery("FROM Obra where tipo='UO'").getResultList();
            System.out.println("Ejecutando consulta... fin");
            for (Obra obra : obraArrayList) {
                obraObservableArrayList.add(new ObrasView(obra.getId(), obra.getCodigo(), obra.getTipo(), obra.getDescripion()));
            }

            tx.commit();
            session.close();
            return obraObservableArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<ObrasView> gatAllObrasRV() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            obraObservableArrayList = FXCollections.observableArrayList();

            obraArrayList = (ArrayList<Obra>) session.createQuery("FROM Obra where tipo='RV'").getResultList();
            for (Obra obra : obraArrayList) {
                obraObservableArrayList.add(new ObrasView(obra.getId(), obra.getCodigo(), obra.getTipo(), obra.getDescripion()));
            }

            tx.commit();
            session.close();
            return obraObservableArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<ControlPresupuestoView> getUnidadObra(int IdObra) {

        String whereId = createWhereClause();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            controlPresupuestoViewObservableList = FXCollections.observableArrayList();

            // String que = "SELECT ec.codigo as codeEmp, ec.descripcion as empDesc, zon.codigo as codeZon, zon.desripcion as zonDesc, ob.codigo as obCode, ob.descripcion as obDes, niv.codigo as nivCode, niv.descripcion as nivDesc, esp.codigo as espCode, esp.descripcion as espDes,  sub.codigo as codeSub, sub.descripcion as subdesc, uo.codigo, uo.descripcion, uo.um, uo.cantidad, uo.costototal, SUM(uor.cantRv * rvr.cantidas) AS HH, uo.salario, SUM(DISTINCT cert.cantidad) AS cant, SUM(DISTINCT cert.salario) AS salar FROM Unidadobra uo LEFT JOIN Certificacion cert ON uo.id = cert.unidadobra__id INNER JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobra__id INNER JOIN Renglonrecursos rvr ON rvr.renglonvariante__id = uor.renglonvariante__id INNER JOIN Recursos rec ON rec.id = rvr.recursos__id INNER JOIN Empresaconstructora ec ON uo.empresaconstructora__id = ec.id INNER JOIN Zonas zon ON uo.zonas__id = zon.id INNER JOIN Objetos ob ON uo.objetos__id = ob.id INNER JOIN Nivel niv ON uo.nivel__id = niv.id INNER JOIN Especialidades esp ON uo.especialidades__id = esp.id INNER JOIN Subespecialidades sub ON uo.subespecialidades__id = sub.id WHERE uo.obra__id = " + IdObra + " AND rec.tipo = '2' GROUP BY ec.codigo, ec.descripcion, zon.codigo, zon.desripcion, ob.codigo, ob.descripcion, niv.codigo, niv.descripcion, esp.codigo, esp.descripcion, sub.codigo, sub.descripcion, uo.codigo, uo.descripcion, uo.um, uo.cantidad, uo.costototal, uo.salario";
            String que = "SELECT ec.codigo as codeEmp, ec.descripcion as empDesc, zon.codigo as codeZon, zon.desripcion as zonDesc, ob.codigo as obCode, ob.descripcion as obDes, niv.codigo as nivCode, niv.descripcion as nivDesc, esp.codigo as espCode, esp.descripcion as espDes,  sub.codigo as codeSub, sub.descripcion as subdesc, uo.codigo, uo.descripcion, uo.um, uo.cantidad, uo.costototal, SUM(uor.cantRv * rvr.cantidas) AS HH, uo.salario, SUM(DISTINCT cert.cantidad) AS cant, SUM(DISTINCT cert.salario) AS salar FROM Unidadobra uo LEFT JOIN Certificacion cert ON uo.id = cert.unidadobra__id INNER JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobra__id INNER JOIN Renglonrecursos rvr ON rvr.renglonvariante__id = uor.renglonvariante__id INNER JOIN Recursos rec ON rec.id = rvr.recursos__id INNER JOIN Empresaconstructora ec ON uo.empresaconstructora__id = ec.id INNER JOIN Zonas zon ON uo.zonas__id = zon.id INNER JOIN Objetos ob ON uo.objetos__id = ob.id INNER JOIN Nivel niv ON uo.nivel__id = niv.id INNER JOIN Especialidades esp ON uo.especialidades__id = esp.id INNER JOIN Subespecialidades sub ON uo.subespecialidades__id = sub.id WHERE uo.obra__id = " + IdObra + " AND rec.tipo = '2' " + whereId + " GROUP BY ec.codigo, ec.descripcion, zon.codigo, zon.desripcion, ob.codigo, ob.descripcion, niv.codigo, niv.descripcion, esp.codigo, esp.descripcion, sub.codigo, sub.descripcion, uo.codigo, uo.descripcion, uo.um, uo.cantidad, uo.costototal, uo.salario";
            javax.persistence.Query query = session.createSQLQuery(que);
            List<Object[]> empData = ((NativeQuery) query).list();
            for (Object[] row : empData) {
                empresaBuilder = new StringBuilder().append(row[0].toString()).append(" ").append(row[1].toString());
                zonaBuilder = new StringBuilder().append(row[2].toString()).append(" ").append(row[3].toString());
                objetoBuilder = new StringBuilder().append(row[4].toString()).append(" ").append(row[5].toString());
                nivelBuilder = new StringBuilder().append(row[6].toString()).append(" ").append(row[7].toString());
                especialidadBuilder = new StringBuilder().append(row[8].toString()).append(" ").append(row[9].toString());
                subEspBuilder = new StringBuilder().append(row[10].toString()).append(" ").append(row[11].toString());

                if (row[14] != null) {
                    // controlPresupuestoViewObservableList.add(new ControlPresupuestoView(empresaBuilder.toString(), zonaBuilder.toString(), objetoBuilder.toString(), nivelBuilder.toString(), especialidadBuilder.toString(), subEspBuilder.toString(), row[12].toString(), row[13].toString(), row[14].toString(), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));

                    if (row[16] == null || row[19] == null) {
                        valEjecutado = 0.0;
                    } else {
                        valEjecutado = Double.parseDouble(row[16].toString()) * Double.parseDouble(row[19].toString()) / Double.parseDouble(row[15].toString());
                    }
                    if (valEjecutado == 0.0) {
                        pendiente = Double.parseDouble(row[16].toString());
                    } else {
                        pendiente = Double.parseDouble(row[16].toString()) - valEjecutado;
                    }
                    double horascert = 0.0;
                    if (row[17] == null || row[19] == null) {
                        horascert = 0.0;
                    } else {
                        horascert = Double.parseDouble(row[17].toString()) * Double.parseDouble(row[19].toString()) / Double.parseDouble(row[15].toString());
                    }
                    double horasPend = 0.0;
                    if (row[17] == null) {
                        horasPend = 0.0;
                    } else {
                        horasPend = Double.parseDouble(row[17].toString()) - horascert;
                    }
                    double salariopend = 0.0;
                    if (row[18] == null || row[20] == null) {
                        salariopend = 0.0;
                    } else {
                        salariopend = Double.parseDouble(row[18].toString()) - Double.parseDouble(row[20].toString());
                    }
                    if (row[19] == null) {
                        porciento = 0.0;
                    } else {
                        porciento = Double.parseDouble(row[19].toString()) / Double.parseDouble(row[15].toString()) * 100;
                    }
                    double val1 = 0.0;
                    double val2 = 0.0;
                    double val3 = 0.0;
                    double val4 = 0.0;
                    double val5 = 0.0;
                    if (row[16] != null) {
                        val1 = Double.parseDouble(row[16].toString());
                    }
                    if (row[17] != null) {
                        val2 = Double.parseDouble(row[17].toString());
                    }
                    if (row[18] != null) {
                        val3 = Double.parseDouble(row[18].toString());
                    }
                    if (row[19] != null) {
                        val4 = Double.parseDouble(row[19].toString());
                    }
                    if (row[20] != null) {
                        val5 = Double.parseDouble(row[20].toString());
                    }

                    controlPresupuestoViewObservableList.add(new ControlPresupuestoView(empresaBuilder.toString(), zonaBuilder.toString(), objetoBuilder.toString(), nivelBuilder.toString(), especialidadBuilder.toString(), subEspBuilder.toString(), row[12].toString(), row[13].toString(), row[14].toString(), Double.parseDouble(row[15].toString()), val1, val2, val3, val4, valEjecutado, pendiente, horasPend, salariopend, porciento));
                }
            }
            tx.commit();
            session.close();
            return controlPresupuestoViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return FXCollections.emptyObservableList();
    }

    public ObservableList<ControlPresupuestoUORVView> getUnidadObraRV(int IdObra) {
        String whereId = createWhereClause();
        controlPresupuestoRVViewObservableList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            String que = "SELECT DISTINCT ec.codigo as codeEmp, ec.descripcion as empDesc, zon.codigo as codeZon, zon.desripcion as zonDesc, ob.codigo as obCode, ob.descripcion as obDes, niv.codigo as nivCode, niv.descripcion as nivDesc, esp.codigo as espCode, esp.descripcion as espDes,  sub.codigo as codeSub, sub.descripcion as subdesc, uo.codigo as  uocode, uo.descripcion as uodescr, uo.um as uoum, SUM(cert.cantidad), rv.codigo, rv.descripcion, rv.um, uor.cantRv, uor.costmano, uor.costequip, uor.salariomn, uo.id as iduo, rv.id as rvid FROM Unidadobra uo INNER JOIN Certificacion cert ON uo.id = cert.unidadobra__id INNER JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobra__id INNER JOIN Renglonvariante rv ON uor.renglonvariante__id = rv.id INNER JOIN Empresaconstructora ec ON uo.empresaconstructora__id = ec.id INNER JOIN Zonas zon ON uo.zonas__id = zon.id INNER JOIN Objetos ob ON uo.objetos__id = ob.id INNER JOIN Nivel niv ON uo.nivel__id = niv.id INNER JOIN Especialidades esp ON uo.especialidades__id = esp.id INNER JOIN Subespecialidades sub ON uo.subespecialidades__id = sub.id WHERE uo.obra__id = " + IdObra + whereId + " GROUP BY ec.codigo, ec.descripcion, zon.codigo, zon.desripcion, ob.codigo, ob.descripcion, niv.codigo, niv.descripcion, esp.codigo, esp.descripcion, sub.codigo, sub.descripcion, uo.codigo, uo.descripcion, uo.um,  rv.codigo, rv.descripcion, rv.um, uor.cantRv, uor.costmano, uor.costequip, uor.salariomn, uo.id, rv.id  ORDER BY uo.id";
            // String que = "SELECT DISTINCT ec.codigo as codeEmp, ec.descripcion as empDesc, zon.codigo as codeZon, zon.desripcion as zonDesc, ob.codigo as obCode, ob.descripcion as obDes, niv.codigo as nivCode, niv.descripcion as nivDesc, esp.codigo as espCode, esp.descripcion as espDes,  sub.codigo as codeSub, sub.descripcion as subdesc, uo.codigo as  uocode, uo.descripcion as uodescr, uo.um as uoum, SUM(cert.cantidad), rv.codigo, rv.descripcion, rv.um, uor.cantRv, uor.costmano, uor.costequip, uor.salariomn, uo.id as iduo, rv.id as rvid FROM Unidadobra uo INNER JOIN Certificacion cert ON uo.id = cert.unidadobra__id INNER JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobra__id INNER JOIN Renglonvariante rv ON uor.renglonvariante__id = rv.id INNER JOIN Empresaconstructora ec ON uo.empresaconstructora__id = ec.id INNER JOIN Zonas zon ON uo.zonas__id = zon.id INNER JOIN Objetos ob ON uo.objetos__id = ob.id INNER JOIN Nivel niv ON uo.nivel__id = niv.id INNER JOIN Especialidades esp ON uo.especialidades__id = esp.id INNER JOIN Subespecialidades sub ON uo.subespecialidades__id = sub.id WHERE uo.obra__id = " + IdObra + whereId + " GROUP BY ec.codigo, ec.descripcion, zon.codigo, zon.desripcion, ob.codigo, ob.descripcion, niv.codigo, niv.descripcion, esp.codigo, esp.descripcion, sub.codigo, sub.descripcion, uo.codigo, uo.descripcion, uo.um,  rv.codigo, rv.descripcion, rv.um, uor.cantRv, uor.costmano, uor.costequip, uor.salariomn, uo.id, rv.id  ORDER BY uo.id";
            javax.persistence.Query query = session.createSQLQuery(que);
            List<Object[]> empData = ((NativeQuery) query).list();
            for (Object[] row : empData) {
                empresaBuilder = new StringBuilder().append(row[0].toString()).append(" ").append(row[1].toString());
                zonaBuilder = new StringBuilder().append(row[2].toString()).append(" ").append(row[3].toString());
                objetoBuilder = new StringBuilder().append(row[4].toString()).append(" ").append(row[5].toString());
                nivelBuilder = new StringBuilder().append(row[6].toString()).append(" ").append(row[7].toString());
                especialidadBuilder = new StringBuilder().append(row[8].toString()).append(" ").append(row[9].toString());
                subEspBuilder = new StringBuilder().append(row[10].toString()).append(" ").append(row[11].toString());
                groupBuilder = new StringBuilder().append(row[12].toString().trim()).append(" ").append(row[13].toString().trim()).append("      ").append(row[15].toString().trim()).append(" / ").append(row[14].toString());

                double total = Double.parseDouble(row[20].toString()) + Double.parseDouble(row[21].toString());
                Double horas = getHorasRVinUO(Integer.parseInt(row[23].toString()), Integer.parseInt(row[24].toString()));
                double cantCert = cantidadCertRV(Integer.parseInt(row[23].toString()), Integer.parseInt(row[24].toString()));
                double salariocert = cantSalarioCert(Integer.parseInt(row[23].toString()), Integer.parseInt(row[24].toString()));

                valEjecutado = total * cantCert / Double.parseDouble(row[19].toString());
                pendiente = total - valEjecutado;

                double horascert = horas * cantCert / Double.parseDouble(row[19].toString());
                double horasPend = horas - horascert;
                double salariopend = Double.parseDouble(row[22].toString()) - salariocert;
                porciento = cantCert / Double.parseDouble(row[19].toString()) * 100;
                controlPresupuestoUORVViewObservableList.add(new ControlPresupuestoUORVView(empresaBuilder.toString(), zonaBuilder.toString(), objetoBuilder.toString(), nivelBuilder.toString(), especialidadBuilder.toString(), subEspBuilder.toString(), groupBuilder.toString(), row[13].toString(), row[14].toString(), Double.parseDouble(row[15].toString()), row[16].toString(), row[17].toString(), row[18].toString(), Double.parseDouble(row[19].toString()), total, horas, Double.parseDouble(row[22].toString()), cantCert, valEjecutado, pendiente, horasPend, salariopend, porciento));
            }
            tx.commit();
            session.close();
            return controlPresupuestoUORVViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return FXCollections.emptyObservableList();
    }

    private Double cantidadCertRV(int parseInt, int parseInt1) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("SELECT SUM(cantidad) FROM  Certificacionrecuo WHERE unidadobraId =: idU AND renglonId =: idR").setParameter("idU", parseInt).setParameter("idR", parseInt1);

            if (query.getResultList().get(0) != null) {
                cantcertRV = (Double) query.getResultList().get(0);
            } else {
                cantcertRV = 0.0;
            }

            tx.commit();
            session.close();
            return cantcertRV;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return 0.0;
    }

    /**
     * para los renglones variantes
     */

    private Double cantidadCertRVinRV(int parseInt, int parseInt1) {

        cantcertRV = 0.0;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            javax.persistence.Query query = session.createQuery("SELECT SUM(cantidad) FROM  CertificacionRenglonVariante WHERE nivelespecificoId =: idU AND renglonvarianteId =: idR ").setParameter("idU", parseInt).setParameter("idR", parseInt1);

            if (!query.getResultList().isEmpty()) {
                cantcertRV = (Double) query.getResultList().get(0);
            } else {
                cantcertRV = 0.0;
            }

            tx.commit();
            session.close();
            return cantcertRV;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return 0.0;
    }

    private Double cantSalarioCert(int parseInt, int parseInt1) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            javax.persistence.Query query = session.createQuery("SELECT SUM(salario) FROM  Certificacionrecuo WHERE unidadobraId =: idU AND renglonId =: idR").setParameter("idU", parseInt).setParameter("idR", parseInt1);

            if (query.getResultList().get(0) != null) {
                cantSalarioCertRV = (Double) query.getResultList().get(0);
            } else {
                cantSalarioCertRV = 0.0;
            }

            tx.commit();
            session.close();
            return cantSalarioCertRV;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return 0.0;
    }

    /**
     * para los renglones variantes
     */

    private Double cantSalarioCertinRV(int parseInt, int parseInt1) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            javax.persistence.Query query = session.createQuery("SELECT SUM(salario) FROM  Certificacionrecrv WHERE nivelespId =: idU AND renglonId =: idR").setParameter("idU", parseInt).setParameter("idR", parseInt1);

            if (query.getResultList().get(0) != null) {
                cantSalarioCertRV = (Double) query.getResultList().get(0);
            } else {
                cantSalarioCertRV = 0.0;
            }

            tx.commit();
            session.close();
            return cantSalarioCertRV;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return 0.0;
    }

    private Double getHorasRVinUO(int idUO, int idRV) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            javax.persistence.Query query = session.createQuery("SELECT SUM(uor.cantRv * rvr.cantidas) FROM Unidadobrarenglon uor INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Renglonvariante rv ON uor.renglonvarianteId = rv.id INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE uor.unidadobraId =: idU AND uor.renglonvarianteId =: idR AND rec.tipo = '2'").setParameter("idU", idUO).setParameter("idR", idRV);
            cantHHRV = 0.0;
            if (query.getResultList().get(0) != null) {
                cantHHRV = (Double) query.getResultList().get(0);
            } else {
                cantHHRV = 0.0;
            }

            tx.commit();
            session.close();
            return cantHHRV;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return 0.0;

    }

    /**
     * para los renglones variantes
     */

    private Double getHorasRVinUORV(int idUO, int idRV) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            cantHHRV = 0.0;
            javax.persistence.Query query = session.createQuery("SELECT SUM(uor.cantidad * rvr.cantidas) FROM Renglonnivelespecifico uor INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Renglonvariante rv ON uor.renglonvarianteId = rv.id INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE uor.nivelespecificoId =: idU AND uor.renglonvarianteId =: idR AND rec.tipo = '2'").setParameter("idU", idUO).setParameter("idR", idRV);

            if (query.getResultList().get(0) != null) {
                cantHHRV = (Double) query.getResultList().get(0);
            } else {
                cantHHRV = 0.0;
            }
            tx.commit();
            session.close();
            return cantHHRV;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return 0.0;
    }

    public ObservableList<ControlPresupuestoUORVView> getRenglonVariante(int IdObra) {

        String whereId = createWhereClause();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            controlPresupuestoRVViewObservableList = FXCollections.observableArrayList();
            // String que = "SELECT DISTINCT ec.codigo as codeEmp, ec.descripcion as empDesc, zon.codigo as codeZon, zon.desripcion as zonDesc, ob.codigo as obCode, ob.descripcion as obDes, niv.codigo as nivCode, niv.descripcion as nivDesc, esp.codigo as espCode, esp.descripcion as espDes,  sub.codigo as codeSub, sub.descripcion as subdesc, uo.codigo as  uocode, uo.descripcion as uodescr, SUM(cert.cantidad), rv.codigo, rv.descripcion, rv.um, uor.cantidad, uo.costomaterial, uor.costmano, uor.costequipo, uor.salario, uo.id as iduo, rv.id as rvid FROM Nivelespecifico uo INNER JOIN certificacionrenglonvariante cert ON uo.id = cert.nivelespecifico__id INNER JOIN Renglonnivelespecifico uor ON uo.id = uor.nivelespecifico__id INNER JOIN Renglonvariante rv ON uor.renglonvariante__id = rv.id INNER JOIN Empresaconstructora ec ON uo.empresaconstructora__id = ec.id INNER JOIN Zonas zon ON uo.zonas__id = zon.id INNER JOIN Objetos ob ON uo.objetos__id = ob.id INNER JOIN Nivel niv ON uo.nivel__id = niv.id INNER JOIN Especialidades esp ON uo.especialidades__id = esp.id INNER JOIN Subespecialidades sub ON uo.subespecialidades__id = sub.id WHERE uo.obra__id = " + IdObra + " GROUP BY ec.codigo, ec.descripcion, zon.codigo, zon.desripcion, ob.codigo, ob.descripcion, niv.codigo, niv.descripcion, esp.codigo, esp.descripcion, sub.codigo, sub.descripcion, uo.codigo, uo.descripcion, rv.codigo, rv.descripcion, rv.um, uor.cantidad, uor.costmaterial, uor.costmano, uor.costequipo, uor.salario, uo.id, rv.id  ORDER BY uo.id ";
            String que = "SELECT DISTINCT ec.codigo as codeEmp, ec.descripcion as empDesc, zon.codigo as codeZon, zon.desripcion as zonDesc, ob.codigo as obCode, ob.descripcion as obDes, niv.codigo as nivCode, niv.descripcion as nivDesc, esp.codigo as espCode, esp.descripcion as espDes,  sub.codigo as codeSub, sub.descripcion as subdesc, ne.codigo as  uocode, ne.descripcion as uodescr, SUM(cert.cantidad), rv.codigo, rv.descripcion, rv.um, uor.cantidad, ne.costomaterial, uor.costmano, uor.costequipo, uor.salario, ne.id as iduo, rv.id as rvid FROM Nivelespecifico ne INNER JOIN certificacionrenglonvariante cert ON ne.id = cert.nivelespecifico__id INNER JOIN Renglonnivelespecifico uor ON ne.id = uor.nivelespecifico__id INNER JOIN Renglonvariante rv ON uor.renglonvariante__id = rv.id INNER JOIN Empresaconstructora ec ON ne.empresaconstructora__id = ec.id INNER JOIN Zonas zon ON ne.zonas__id = zon.id INNER JOIN Objetos ob ON ne.objetos__id = ob.id INNER JOIN Nivel niv ON ne.nivel__id = niv.id INNER JOIN Especialidades esp ON ne.especialidades__id = esp.id INNER JOIN Subespecialidades sub ON ne.subespecialidades__id = sub.id WHERE ne.obra__id = " + IdObra + whereId + " GROUP BY ec.codigo, ec.descripcion, zon.codigo, zon.desripcion, ob.codigo, ob.descripcion, niv.codigo, niv.descripcion, esp.codigo, esp.descripcion, sub.codigo, sub.descripcion, ne.codigo, ne.descripcion, rv.codigo, rv.descripcion, rv.um, uor.cantidad, uor.costmaterial, uor.costmano, uor.costequipo, uor.salario, ne.id, rv.id  ORDER BY ne.id";
            System.out.println(que);
            javax.persistence.Query query = session.createSQLQuery(que);
            List<Object[]> empData = ((NativeQuery) query).list();
            for (Object[] row : empData) {
                empresaBuilder = new StringBuilder().append(row[0].toString()).append(" ").append(row[1].toString());
                zonaBuilder = new StringBuilder().append(row[2].toString()).append(" ").append(row[3].toString());
                objetoBuilder = new StringBuilder().append(row[4].toString()).append(" ").append(row[5].toString());
                nivelBuilder = new StringBuilder().append(row[6].toString()).append(" ").append(row[7].toString());
                especialidadBuilder = new StringBuilder().append(row[8].toString()).append(" ").append(row[9].toString());
                subEspBuilder = new StringBuilder().append(row[10].toString()).append(" ").append(row[11].toString());
                groupBuilder = new StringBuilder().append(row[12].toString()).append(" ").append(row[13].toString());

                double total = Double.parseDouble(row[19].toString()) + Double.parseDouble(row[20].toString()) + Double.parseDouble(row[21].toString());
                double horas = getHorasRVinUORV(Integer.parseInt(row[23].toString()), Integer.parseInt(row[24].toString()));
                double cantCert = cantidadCertRVinRV(Integer.parseInt(row[23].toString()), Integer.parseInt(row[24].toString()));
                double salariocert = cantSalarioCertinRV(Integer.parseInt(row[23].toString()), Integer.parseInt(row[24].toString()));

                valEjecutado = total * cantCert / Double.parseDouble(row[18].toString());
                pendiente = total - valEjecutado;

                double horascert = horas * cantCert / Double.parseDouble(row[18].toString());
                double horasPend = horas - horascert;
                double salariopend = Double.parseDouble(row[22].toString()) - salariocert;
                porciento = cantCert / Double.parseDouble(row[18].toString()) * 100;
                controlPresupuestoUORVViewObservableList.add(new ControlPresupuestoUORVView(empresaBuilder.toString(), zonaBuilder.toString(), objetoBuilder.toString(), nivelBuilder.toString(), especialidadBuilder.toString(), subEspBuilder.toString(), groupBuilder.toString(), row[13].toString(), " ", null, row[15].toString(), row[16].toString(), row[17].toString(), Double.parseDouble(row[18].toString()), total, horas, Double.parseDouble(row[22].toString()), cantCert, valEjecutado, pendiente, horasPend, salariopend, porciento));
            }

            tx.commit();
            session.close();
            return controlPresupuestoUORVViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public DynamicReport createControlPresupReport(Integer niveles) throws ClassNotFoundException {

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(ar.com.fdvs.dj.domain.constants.Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
        titleStyle.setBorderBottom(Border.PEN_2_POINT());

        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style group = new Style();
        group.setFont(new Font(10, Font._FONT_ARIAL, true));


        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = new Integer(20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(new Integer(15)).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setTemplateFile("templete/report8LandScape.jrxml");


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(group).setHeaderStyle(group)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(40)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(40)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subEspecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columnauo = ColumnBuilder.getNew()
                .setColumnProperty("uo", String.class.getName())
                .setTitle("Código").setWidth(15)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("descrip", String.class.getName())
                .setTitle("Descripción").setWidth(30)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(5)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCantidad = ColumnBuilder.getNew()
                .setColumnProperty("cant", Double.class.getName())
                .setTitle("Cant.").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCosto = ColumnBuilder.getNew()
                .setColumnProperty("total", Double.class.getName())
                .setTitle("C. Total").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnahh = ColumnBuilder.getNew()
                .setColumnProperty("hh", Double.class.getName())
                .setTitle("H/H").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnasalario = ColumnBuilder.getNew()
                .setColumnProperty("salario", Double.class.getName())
                .setTitle("Salario").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnaavence = ColumnBuilder.getNew()
                .setColumnProperty("avance", Double.class.getName())
                .setTitle("Certif.").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnavalorEjecutado = ColumnBuilder.getNew()
                .setColumnProperty("valorEjecutado", Double.class.getName())
                .setTitle("Ejecutado").setWidth(13).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnaValporEjec = ColumnBuilder.getNew()
                .setColumnProperty("porEjecutarCost", Double.class.getName())
                .setTitle("Pend(Monto)").setWidth(17).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaporHH = ColumnBuilder.getNew()
                .setColumnProperty("porEjecutarHH", Double.class.getName())
                .setTitle("Pend(H/H)").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaporSal = ColumnBuilder.getNew()
                .setColumnProperty("porEjecutarSalario", Double.class.getName())
                .setTitle("Pend(Sal)").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaPorcientoEJc = ColumnBuilder.getNew()
                .setColumnProperty("porciento", Double.class.getName())
                .setTitle("%").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        drb.addColumn(columnauo);
        drb.addColumn(columnadescrip);
        drb.addColumn(columnaum);
        drb.addColumn(columnaCantidad);
        drb.addColumn(columnaCosto);
        drb.addColumn(columnahh);
        drb.addColumn(columnasalario);
        drb.addColumn(columnaavence);
        drb.addColumn(columnavalorEjecutado);
        drb.addColumn(columnaValporEjec);
        drb.addColumn(columnaporHH);
        drb.addColumn(columnaporSal);
        drb.addColumn(columnaPorcientoEJc);


        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        DJGroupLabel glabel1 = new DJGroupLabel("Total", glabelStyle);
        DJGroupLabel glabel2 = new DJGroupLabel("H/H", glabelStyle);
        DJGroupLabel glabel3 = new DJGroupLabel("Salario", glabelStyle);
        DJGroupLabel glabel4 = new DJGroupLabel("Ejecutado", glabelStyle);
        DJGroupLabel glabel5 = new DJGroupLabel("Pend(Monto)", glabelStyle);
        DJGroupLabel glabel6 = new DJGroupLabel("Pend(H/H)", glabelStyle);
        DJGroupLabel glabel7 = new DJGroupLabel("Pend(sal)", glabelStyle);


        if (niveles == 1) {
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(columngruopEmpresa);
            drb.addGroup(g1);


        } else if (niveles == 2) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g2);


        } else if (niveles == 3) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g2);


            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g3);


        } else if (niveles == 4) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g4);

        } else if (niveles == 5) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g5);

        } else if (niveles == 6) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g4);
            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g5);
            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();
            drb.addGroup(g6);
        }

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;


    }

    public DynamicReport createControlPresupReportUORV(Integer niveles) throws ClassNotFoundException {

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(ar.com.fdvs.dj.domain.constants.Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
        titleStyle.setBorderBottom(Border.PEN_2_POINT());

        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style group = new Style();
        group.setFont(new Font(10, Font._FONT_ARIAL, true));


        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = new Integer(20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(new Integer(15)).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setTemplateFile("templete/report8LandScape.jrxml");


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(group).setHeaderStyle(group)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(40)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("Nivel: ").setWidth(40)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subEspecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columnauo = ColumnBuilder.getNew()
                .setColumnProperty("uo", String.class.getName())
                .setTitle(" UO: ").setWidth(30)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columnarvcode = ColumnBuilder.getNew()
                .setColumnProperty("rvCode", String.class.getName())
                .setTitle("Código").setWidth(15)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnarvdescrip = ColumnBuilder.getNew()
                .setColumnProperty("rvDescrip", String.class.getName())
                .setTitle("Descripción").setWidth(30)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnarvum = ColumnBuilder.getNew()
                .setColumnProperty("rvUm", String.class.getName())
                .setTitle("UM").setWidth(5)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnarvCantidad = ColumnBuilder.getNew()
                .setColumnProperty("rvCantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCosto = ColumnBuilder.getNew()
                .setColumnProperty("total", Double.class.getName())
                .setTitle("C. Total").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnahh = ColumnBuilder.getNew()
                .setColumnProperty("hh", Double.class.getName())
                .setTitle("H/H").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnasalario = ColumnBuilder.getNew()
                .setColumnProperty("salario", Double.class.getName())
                .setTitle("Salario").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnaavence = ColumnBuilder.getNew()
                .setColumnProperty("avance", Double.class.getName())
                .setTitle("Certif.").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnavalorEjecutado = ColumnBuilder.getNew()
                .setColumnProperty("valorEjecutado", Double.class.getName())
                .setTitle("Ejecutado").setWidth(13).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnaValporEjec = ColumnBuilder.getNew()
                .setColumnProperty("porEjecutarCost", Double.class.getName())
                .setTitle("Pend(Monto)").setWidth(17).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaporHH = ColumnBuilder.getNew()
                .setColumnProperty("porEjecutarHH", Double.class.getName())
                .setTitle("Pend(H/H)").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaporSal = ColumnBuilder.getNew()
                .setColumnProperty("porEjecutarSalario", Double.class.getName())
                .setTitle("Pend(Sal)").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaPorcientoEJc = ColumnBuilder.getNew()
                .setColumnProperty("porciento", Double.class.getName())
                .setTitle("%").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        drb.addColumn(columnarvcode);
        drb.addColumn(columnarvdescrip);
        drb.addColumn(columnarvum);
        drb.addColumn(columnarvCantidad);
        drb.addColumn(columnaCosto);
        drb.addColumn(columnahh);
        drb.addColumn(columnasalario);
        drb.addColumn(columnaavence);
        drb.addColumn(columnavalorEjecutado);
        drb.addColumn(columnaValporEjec);
        drb.addColumn(columnaporHH);
        drb.addColumn(columnaporSal);
        drb.addColumn(columnaPorcientoEJc);


        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        DJGroupLabel glabel1 = new DJGroupLabel("Total", glabelStyle);
        DJGroupLabel glabel2 = new DJGroupLabel("H/H", glabelStyle);
        DJGroupLabel glabel3 = new DJGroupLabel("Salario", glabelStyle);
        DJGroupLabel glabel4 = new DJGroupLabel("Ejecutado", glabelStyle);
        DJGroupLabel glabel5 = new DJGroupLabel("Pend(Monto)", glabelStyle);
        DJGroupLabel glabel6 = new DJGroupLabel("Pend(H/H)", glabelStyle);
        DJGroupLabel glabel7 = new DJGroupLabel("Pend(sal)", glabelStyle);

        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.LEFT).setBorderBottom(Border.THIN())
                .setVerticalAlign(VerticalAlign.BOTTOM)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();

        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresa");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("zona");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("objeto");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("nivel");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Es = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("especialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3S = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("subEspecialidad");
            }
        }, glabelStyle2, null);


        if (niveles == 1) {
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addColumn(columngruopEmpresa);
            drb.addGroup(g1);

            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columnauo)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(columnauo);
            drb.addGroup(g2);

        } else if (niveles == 2) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columnauo)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(columnauo);
            drb.addGroup(g3);


        } else if (niveles == 3) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g2);


            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columnauo)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(columnauo);
            drb.addGroup(g4);


        } else if (niveles == 4) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columnauo)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(columnauo);
            drb.addGroup(g5);

        } else if (niveles == 5) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g5);

            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columnauo)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(columnauo);
            drb.addGroup(g6);

        } else if (niveles == 6) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g4);
            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g5);
            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3S)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g6);

            GroupBuilder gb7 = new GroupBuilder();
            DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columnauo)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(columnauo);
            drb.addGroup(g7);
        }

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;
    }

    public DynamicReport createControlPresupReportRVinRV(Integer niveles) throws ClassNotFoundException {

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(ar.com.fdvs.dj.domain.constants.Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(18, Font._FONT_ARIAL, true));
        titleStyle.setBorderBottom(Border.PEN_2_POINT());

        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style group = new Style();
        group.setFont(new Font(12, Font._FONT_ARIAL, true));


        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = new Integer(20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(new Integer(15)).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setTemplateFile("templete/reportControlPresupusto.jrxml");


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(60)
                .setStyle(group).setHeaderStyle(group)
                .build();


        AbstractColumn columngruopZona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(40)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columngruopObj = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(50)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columngruopNiv = ColumnBuilder.getNew()
                .setColumnProperty("nivel", String.class.getName())
                .setTitle("NivelPCW: ").setWidth(40)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columngruoesp = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(90)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columngruopsup = ColumnBuilder.getNew()
                .setColumnProperty("subEspecialidad", String.class.getName())
                .setTitle("Subespecialidad: ").setWidth(110)
                .setStyle(group).setHeaderStyle(group)
                .build();

        AbstractColumn columnauo = ColumnBuilder.getNew()
                .setColumnProperty("uo", String.class.getName())
                .setTitle(" Especifica: ").setWidth(60)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnarvcode = ColumnBuilder.getNew()
                .setColumnProperty("rvCode", String.class.getName())
                .setTitle("Código").setWidth(15)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnarvdescrip = ColumnBuilder.getNew()
                .setColumnProperty("rvDescrip", String.class.getName())
                .setTitle("Descripción").setWidth(30)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnarvum = ColumnBuilder.getNew()
                .setColumnProperty("rvUm", String.class.getName())
                .setTitle("UM").setWidth(7)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnarvCantidad = ColumnBuilder.getNew()
                .setColumnProperty("rvCantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(12).setPattern("0.000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCosto = ColumnBuilder.getNew()
                .setColumnProperty("total", Double.class.getName())
                .setTitle("C. Total").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnahh = ColumnBuilder.getNew()
                .setColumnProperty("hh", Double.class.getName())
                .setTitle("H/H").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnasalario = ColumnBuilder.getNew()
                .setColumnProperty("salario", Double.class.getName())
                .setTitle("Salario").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnaavence = ColumnBuilder.getNew()
                .setColumnProperty("avance", Double.class.getName())
                .setTitle("Certif.").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnavalorEjecutado = ColumnBuilder.getNew()
                .setColumnProperty("valorEjecutado", Double.class.getName())
                .setTitle("Ejecutado").setWidth(13).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnaValporEjec = ColumnBuilder.getNew()
                .setColumnProperty("porEjecutarCost", Double.class.getName())
                .setTitle("Pend(Monto)").setWidth(17).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaporHH = ColumnBuilder.getNew()
                .setColumnProperty("porEjecutarHH", Double.class.getName())
                .setTitle("Pend(H/H)").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaporSal = ColumnBuilder.getNew()
                .setColumnProperty("porEjecutarSalario", Double.class.getName())
                .setTitle("Pend(Sal)").setWidth(15).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaPorcientoEJc = ColumnBuilder.getNew()
                .setColumnProperty("porciento", Double.class.getName())
                .setTitle("%").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        drb.addColumn(columnarvcode);
        drb.addColumn(columnarvdescrip);
        drb.addColumn(columnarvum);
        drb.addColumn(columnarvCantidad);
        drb.addColumn(columnaCosto);
        drb.addColumn(columnahh);
        drb.addColumn(columnasalario);
        drb.addColumn(columnaavence);
        drb.addColumn(columnavalorEjecutado);
        drb.addColumn(columnaValporEjec);
        drb.addColumn(columnaporHH);
        drb.addColumn(columnaporSal);
        drb.addColumn(columnaPorcientoEJc);


        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        DJGroupLabel glabel1 = new DJGroupLabel("Total", glabelStyle);
        DJGroupLabel glabel2 = new DJGroupLabel("H/H", glabelStyle);
        DJGroupLabel glabel3 = new DJGroupLabel("Salario", glabelStyle);
        DJGroupLabel glabel4 = new DJGroupLabel("Ejecutado", glabelStyle);
        DJGroupLabel glabel5 = new DJGroupLabel("Pend(Monto)", glabelStyle);
        DJGroupLabel glabel6 = new DJGroupLabel("Pend(H/H)", glabelStyle);
        DJGroupLabel glabel7 = new DJGroupLabel("Pend(sal)", glabelStyle);

        Style glabelStyle2 = new StyleBuilder(false).setFont(Font.ARIAL_SMALL_BOLD)
                .setHorizontalAlign(HorizontalAlign.LEFT).setBorderBottom(Border.THIN())
                .setVerticalAlign(VerticalAlign.BOTTOM)
                .setPadding(0)
                .setStretchWithOverflow(false)
                .build();

        DJGroupLabel glabel3E = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("empresa");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Z = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("zona");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3O = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("objeto");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3N = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("nivel");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3Es = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("especialidad");
            }
        }, glabelStyle2, null);

        DJGroupLabel glabel3S = new DJGroupLabel(new StringExpression() {
            @Override
            public Object evaluate(Map fields, Map variables, Map parameters) {
                return "Total: " + fields.get("subespecialidad");
            }
        }, glabelStyle2, null);


        if (niveles == 1) {
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addColumn(columngruopEmpresa);
            drb.addGroup(g1);

            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columnauo)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(columnauo);
            drb.addGroup(g2);

        } else if (niveles == 2) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columnauo)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(columnauo);
            drb.addGroup(g3);


        } else if (niveles == 3) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g2);


            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columnauo)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(columnauo);
            drb.addGroup(g4);


        } else if (niveles == 4) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columnauo)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(columnauo);
            drb.addGroup(g5);

        } else if (niveles == 5) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);

            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g4);

            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g5);

            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columnauo)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(columnauo);
            drb.addGroup(g6);

        } else if (niveles == 6) {
            drb.addColumn(columngruopEmpresa);
            drb.addColumn(columngruopZona);
            drb.addColumn(columngruopObj);
            drb.addColumn(columngruopNiv);
            drb.addColumn(columngruoesp);
            drb.addColumn(columngruopsup);
            GroupBuilder gb1 = new GroupBuilder();
            DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3E)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g1);


            GroupBuilder gb2 = new GroupBuilder();
            DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columngruopZona)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Z)
                    .setFooterVariablesHeight(12)
                    .build();

            drb.addGroup(g2);

            GroupBuilder gb3 = new GroupBuilder();
            DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) columngruopObj)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3O)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g3);

            GroupBuilder gb4 = new GroupBuilder();
            DJGroup g4 = gb4.setCriteriaColumn((PropertyColumn) columngruopNiv)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3N)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g4);
            GroupBuilder gb5 = new GroupBuilder();
            DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) columngruoesp)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3Es)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g5);
            GroupBuilder gb6 = new GroupBuilder();
            DJGroup g6 = gb6.setCriteriaColumn((PropertyColumn) columngruopsup)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .setFooterLabel(glabel3S)
                    .setFooterVariablesHeight(12)
                    .build();
            drb.addGroup(g6);

            GroupBuilder gb7 = new GroupBuilder();
            DJGroup g7 = gb7.setCriteriaColumn((PropertyColumn) columnauo)
                    .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables, null, glabel1)
                    .addFooterVariable(columnahh, DJCalculation.SUM, headerVariables, null, glabel2)
                    .addFooterVariable(columnasalario, DJCalculation.SUM, headerVariables, null, glabel3)
                    .addFooterVariable(columnavalorEjecutado, DJCalculation.SUM, headerVariables, null, glabel4)
                    .addFooterVariable(columnaValporEjec, DJCalculation.SUM, headerVariables, null, glabel5)
                    .addFooterVariable(columnaporHH, DJCalculation.SUM, headerVariables, null, glabel6)
                    .addFooterVariable(columnaporSal, DJCalculation.SUM, headerVariables, null, glabel7)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                    .build();

            drb.addColumn(columnauo);
            drb.addGroup(g7);
        }

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;
    }

    public ArrayList<TreeItem> getObras() {

        obraObservableArrayList = gatAllObras();
        ArrayList<TreeItem> treeItems = new ArrayList<>();

        for (ObrasView obra : obraObservableArrayList) {
            TreeItem<String> item = new TreeItem(obra.getDescripion());
            treeItems.add(item);
        }

        return treeItems;

    }

    public ArrayList<TreeItem> getObrasRV() {

        obraObservableArrayList = gatAllObrasRV();
        ArrayList<TreeItem> treeItems = new ArrayList<>();

        for (ObrasView obra : obraObservableArrayList) {
            TreeItem<String> item = new TreeItem(obra.getDescripion());
            treeItems.add(item);
        }

        return treeItems;

    }

    public Task createTimeMesage() {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                //for (int i = 0; i < ; i++) {
                Thread.sleep(2000);
                //  updateProgress(i + 1, val);
                //}
                return true;
            }
        };
    }

    public Task createTime(Integer val) {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                for (int i = 0; i < 100; i++) {
                    Thread.sleep(100);
                    updateProgress(i + 1, 100);
                }
                return true;
            }
        };
    }

    public void handleGenerarReporte(ActionEvent event) {
        if (uoCheck.isSelected() && valUo.isSelected() && !valRV.isSelected()) {
            tarea = createTimeMesage();
            ProgressDialog dialog = new ProgressDialog(tarea);
            dialog.setContentText("Calculando componentes de la obra...");
            dialog.setTitle("Espere...");
            new Thread(tarea).start();
            dialog.showAndWait();

            controlPresupuestoViewObservableList = FXCollections.observableArrayList();
            controlPresupuestoViewObservableList = getUnidadObra(obraModel.getId());
            if (controlPresupuestoViewObservableList == null || controlPresupuestoViewObservableList.size() <= 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Reporte");
                alert.setHeaderText("No hay registros que cumplan con los criterios seleccionados");
                alert.showAndWait();
                return;
            }

            tarea = createTime(controlPresupuestoViewObservableList.size());
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando el reporte...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

            date = LocalDate.now();
            parametros = new HashMap<>();
            parametros.put("obraName", obraModel.getCodigo() + " " + obraModel.getDescripion());
            parametros.put("fecha", DateTimeFormatter.ofPattern("dd/MM/YYYY").format(date));
            parametros.put("reportName", "Control del Presupuesto hasta: " + date.toString());
            parametros.put("empresa", empresaSa.getNombre());
            parametros.put("comercial", empresaSa.getComercial());
            parametros.put("image", "templete/logoReport.jpg");
            try {
                DynamicReport dr1 = createControlPresupReport(nivel);
                JRDataSource ds1 = new JRBeanCollectionDataSource(controlPresupuestoViewObservableList);
                JasperReport jr1 = DynamicJasperHelper.generateJasperReport(dr1, new ClassicLayoutManager(), parametros);
                JasperPrint jp1 = JasperFillManager.fillReport(jr1, parametros, ds1);
                JasperViewer.viewReport(jp1, false);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }

        if (uoCheck.isSelected() && valUo.isSelected() && valRV.isSelected()) {
            tarea = createTimeMesage();
            ProgressDialog dialog = new ProgressDialog(tarea);
            dialog.setContentText("Calculando componentes de la obra...");
            dialog.setTitle("Espere...");
            new Thread(tarea).start();
            dialog.showAndWait();

            controlPresupuestoUORVViewObservableList = FXCollections.observableArrayList();
            controlPresupuestoUORVViewObservableList = getUnidadObraRV(obraModel.getId());

            if (controlPresupuestoUORVViewObservableList.size() > 0) {
                tarea = createTime(controlPresupuestoUORVViewObservableList.size());
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando el reporte...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

                date = LocalDate.now();
                parametros = new HashMap<>();
                parametros.put("obraName", obraModel.getCodigo() + " " + obraModel.getDescripion());
                parametros.put("fecha", DateTimeFormatter.ofPattern("dd/MM/YYYY").format(date));
                parametros.put("reportName", "Control del Presupuesto hasta: " + date.toString());
                parametros.put("empresa", empresaSa.getNombre());
                parametros.put("comercial", empresaSa.getComercial());
                parametros.put("image", "templete/logoReport.jpg");

                try {
                    DynamicReport dr1 = createControlPresupReportUORV(nivel);
                    JRDataSource ds1 = new JRBeanCollectionDataSource(controlPresupuestoUORVViewObservableList);
                    JasperReport jr1 = DynamicJasperHelper.generateJasperReport(dr1, new ClassicLayoutManager(), parametros);
                    JasperPrint jp1 = JasperFillManager.fillReport(jr1, parametros, ds1);
                    JasperViewer.viewReport(jp1, false);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (JRException e) {
                    e.printStackTrace();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Reporte");
                alert.setHeaderText("No hay registros que cumplan con los criterios seleccionados");
                alert.showAndWait();
            }
        }

        if (rvCheck.isSelected() && !valUo.isSelected() && valRV.isSelected()) {
            tarea = createTimeMesage();
            ProgressDialog dialog = new ProgressDialog(tarea);
            dialog.setContentText("Calculando componentes de la obra...");
            dialog.setTitle("Espere...");
            new Thread(tarea).start();
            dialog.showAndWait();

            controlPresupuestoUORVViewObservableList = FXCollections.observableArrayList();
            controlPresupuestoUORVViewObservableList = getRenglonVariante(obraModel.getId());

            if (controlPresupuestoUORVViewObservableList.size() > 0) {

                tarea = createTime(controlPresupuestoUORVViewObservableList.size());
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando el reporte...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

                date = LocalDate.now();
                parametros = new HashMap<>();
                parametros.put("obraName", obraModel.getCodigo() + " " + obraModel.getDescripion());
                parametros.put("fecha", DateTimeFormatter.ofPattern("dd/MM/YYYY").format(date));
                parametros.put("reportName", "Control del Presupuesto hasta: " + date.toString());
                parametros.put("empresa", empresaSa.getNombre());
                parametros.put("comercial", empresaSa.getComercial());
                parametros.put("image", "templete/logoReport.jpg");

                try {
                    DynamicReport dr1 = createControlPresupReportRVinRV(nivel);
                    JRDataSource ds1 = new JRBeanCollectionDataSource(controlPresupuestoUORVViewObservableList);
                    JasperReport jr1 = DynamicJasperHelper.generateJasperReport(dr1, new ClassicLayoutManager(), parametros);
                    JasperPrint jp1 = JasperFillManager.fillReport(jr1, parametros, ds1);
                    JasperViewer.viewReport(jp1, false);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (JRException e) {
                    e.printStackTrace();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Reporte");
                alert.setHeaderText("No hay registros que cumplan con los criterios seleccionados");
                alert.showAndWait();
            }
        }
    }

    public void clearAllCombos() {
        comboGroupByEmpresa.getSelectionModel().clearSelection();
        comboGroupByZonas.getSelectionModel().clearSelection();
        comboGroupByObjetos.getSelectionModel().clearSelection();
        comboGroupByNiveles.getSelectionModel().clearSelection();
        comboGroupByEspecialidades.getSelectionModel().clearSelection();
        comboGroupBySubEspecialidades.getSelectionModel().clearSelection();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        reportProjectStructureSingelton = ReportProjectStructureSingelton.getInstance();

        empresaSa = getEmpresa();

        uoCheck.setSelected(true);

        treeItem = new TreeItem("Obras");
        treeItem.getChildren().addAll(getObras());
        listobras.setRoot(treeItem);

        uoCheck.setOnAction(event -> {
            if (uoCheck.isSelected() == true) {
                clearAllCombos();
                rvCheck.setSelected(false);
                treeItem.getChildren().clear();
                treeItem = new TreeItem("Obras");
                treeItem.getChildren().addAll(getObras());
                listobras.setRoot(treeItem);
            }

        });

        rvCheck.setOnAction(event -> {
            if (rvCheck.isSelected() == true) {
                clearAllCombos();
                uoCheck.setSelected(false);
                treeItem.getChildren().clear();
                treeItem = new TreeItem("Renglones Variantes");

                treeItem.getChildren().addAll(getObrasRV());
                listobras.setRoot(treeItem);
            }
        });

        comboGroupByZonas.setOnAction(event -> {
            String selectedZona = comboGroupByZonas.getSelectionModel().getSelectedItem();
            reportProjectStructureSingelton.zonasArrayList.forEach((zona) -> {
                String strDesc = zona.getCodigo() + " " + zona.getDesripcion();
                if (strDesc.equals(selectedZona)) {
                    listObjetos = reportProjectStructureSingelton.getObjetosList(zona.getId());
                    comboGroupByObjetos.setItems(listObjetos);
                }
            });
        });

        comboGroupByObjetos.setOnAction(event -> {
            String selectedObj = comboGroupByObjetos.getSelectionModel().getSelectedItem();
            reportProjectStructureSingelton.objetosArrayList.forEach((objeto) -> {
                String strDesc = objeto.getCodigo() + " " + objeto.getDescripcion();
                if (strDesc.equals(selectedObj)) {
                    listNiveles = reportProjectStructureSingelton.getNivelList(objeto.getId());
                    comboGroupByNiveles.setItems(listNiveles);
                }
            });
        });

        comboGroupByEspecialidades.setOnAction(event -> {
            String selectedEsp = comboGroupByEspecialidades.getSelectionModel().getSelectedItem();
            reportProjectStructureSingelton.especialidadesArrayList.forEach((especialidad) -> {
                String strDesc = especialidad.getCodigo() + " " + especialidad.getDescripcion();
                if (strDesc.equals(selectedEsp)) {
                    listSubEspecialidades = reportProjectStructureSingelton.getSubespecialidadesObservableList(especialidad.getId());
                    comboGroupBySubEspecialidades.setItems(listSubEspecialidades);
                }
            });
        });

        ObservableList<String> niveles = FXCollections.observableArrayList();
        niveles.add("Empresa");
        niveles.add("Zona");
        niveles.add("Objeto");
        niveles.add("Nivel");
        niveles.add("Especialidad");
        niveles.add("Subespecialidad");

        listobras.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {

            @Override
            public void changed(ObservableValue<? extends TreeItem<String>>
                                        observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                TreeItem<String> seleted = newValue;
                for (Obra obra : obraArrayList) {
                    if (obra.getDescripion().contentEquals(seleted.getValue())) {
                        obraModel = obra;
                    }
                }
                /*
                 * cargar datos en los comboBox
                 * */
                listEmpresas = reportProjectStructureSingelton.getEmpresaList(obraModel.getId());
                comboGroupByEmpresa.setItems(listEmpresas);

                listZonas = reportProjectStructureSingelton.getZonasList(obraModel.getId());
                comboGroupByZonas.setItems(listZonas);

                comboGroupByObjetos.getSelectionModel().clearSelection();

                comboGroupByNiveles.getSelectionModel().clearSelection();

                ObservableList<String> espList = reportProjectStructureSingelton.getEspecialidadesList();
                comboGroupByEspecialidades.setItems(espList);
                comboGroupByEspecialidades.getSelectionModel().clearSelection();

                comboGroupBySubEspecialidades.getSelectionModel().clearSelection();

            }
        });

        nivel = 0;

        clearComboEmpresa.setOnMouseClicked(event -> {
            comboGroupByEmpresa.getSelectionModel().clearSelection();
        });

        clearComboZona.setOnMouseClicked(event -> {
            comboGroupByZonas.getSelectionModel().clearSelection();
        });

        clearComboObjeto.setOnMouseClicked(event -> {
            comboGroupByObjetos.getSelectionModel().clearSelection();
        });

        clearComboNivel.setOnMouseClicked(event -> {
            comboGroupByNiveles.getSelectionModel().clearSelection();
        });

        clearComboEsp.setOnMouseClicked(event -> {
            comboGroupByEspecialidades.getSelectionModel().clearSelection();
        });

        clearComboSubEsp.setOnMouseClicked(event -> {
            comboGroupBySubEspecialidades.getSelectionModel().clearSelection();
        });
    }
}

