package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

public class ActualizarConceptoEmpresaObraController implements Initializable {

    public ActualizarConceptoEmpresaObraController actualizarConceptoEmpresaObraController;

    public ArrayList<Empresagastos> empresagastosArrayList;
    @FXML
    public JFXTextField valor;
    public double salario;
    ReportProjectStructureSingelton structureSingelton = ReportProjectStructureSingelton.getInstance();
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    @FXML
    private JFXTextField concepto;
    @FXML
    private JFXTextField corficiente;
    private Integer idEmpresa;
    private Integer idConcepto;
    private Integer idObra;
    private ArrayList<Unidadobrarenglon> unidadobrarenglonArrayList;
    private ArrayList<Unidadobra> unidadobraArrayList;
    private ObraEmpresaNiveleEspecificosCostos datosCostos;
    private int count;
    @FXML
    private JFXButton btnList;
    private Obra obra;
    private List<Conceptosgasto> conceptosgastoList;
    private Conceptosgasto conceptosgasto;

    public void updateDatosRengVarianteObra(List<Unidadobrarenglon> unidadobrarenglonList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (Unidadobrarenglon datos : unidadobrarenglonList) {
                count++;
                int batchSizeRV = 50;
                if (count > 0 && count % batchSizeRV == 0) {
                    session.flush();
                    session.clear();
                }
                session.update(datos);
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

    /**
     * para recalcular en el caso de las unidades de obra
     */
    private ArrayList<Empresagastos> getAllGastosValores(int idEmp) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresagastosArrayList = new ArrayList<>();
            empresagastosArrayList = (ArrayList<Empresagastos>) session.createQuery("FROM Empresagastos WHERE empresaconstructoraId =: empId ORDER BY conceptosgastoId").setParameter("empId", idEmp).getResultList();

            tx.commit();
            session.close();
            return empresagastosArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return new ArrayList<>();
    }

    public ArrayList<Unidadobra> getListUnidadObra(int idObra) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            unidadobraArrayList = new ArrayList<>();
            trx = session.beginTransaction();
            unidadobraArrayList = (ArrayList<Unidadobra>) session.createQuery("FROM Unidadobra WHERE obraId =: idO").setParameter("idO", idObra).getResultList();

            trx.commit();
            session.close();
            return unidadobraArrayList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return new ArrayList<>();
    }

    public ArrayList<Unidadobrarenglon> getUnidadRenglon(int idObra, int idEmpr) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            unidadobrarenglonArrayList = new ArrayList<>();
            List<Unidadobra> unidadobraList = new ArrayList<>();
            unidadobraList = obra.getUnidadobrasById().parallelStream().filter(unidadobra -> unidadobra.getEmpresaconstructoraId() == idEmpr).collect(Collectors.toList());
            for (Unidadobra unidadobra : unidadobraList) {
                unidadobrarenglonArrayList.addAll(unidadobra.getUnidadobrarenglonsById());
            }
            trx.commit();
            session.close();
            return unidadobrarenglonArrayList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public void insertValorestoGastos(ArrayList<Empresaobraconcepto> empresaobraconceptoArrayList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (Empresaobraconcepto datos : empresaobraconceptoArrayList) {
                count++;
                int batchSize = 20;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.saveOrUpdate(datos);
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

    public List<Conceptosgasto> getListGastos(int idResol) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Conceptosgasto> list = new ArrayList<>();
            list = session.createQuery("FROM Conceptosgasto WHERE pertence =: id").setParameter("id", idResol).getResultList();
            tx.commit();
            session.close();
            return list;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    /**
     * Metodo para calcular los conceptos fundamentales (mano de obra, materiales y equipo). esta a nivel de obra.
     * a pertir de estos se calcularan todos los demas conceptos de gastos
     */


    private ObraEmpresaNiveleEspecificosCostos getDatosConceptos(int idO, int idEmp) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<Unidadobra> unidadobras = new ArrayList<>();
            unidadobras = obra.getUnidadobrasById().parallelStream().filter(unidadobra -> unidadobra.getEmpresaconstructoraId() == idEmp).collect(Collectors.toList());
            double costMat = unidadobras.parallelStream().map(Unidadobra::getCostoMaterial).reduce(0.0, Double::sum);
            double costMnoRV = unidadobras.parallelStream().map(Unidadobra::getCostomano).reduce(0.0, Double::sum);
            double costEquipRV = unidadobras.parallelStream().map(Unidadobra::getCostoequipo).reduce(0.0, Double::sum);
            datosCostos = new ObraEmpresaNiveleEspecificosCostos(idO, idEmp, Math.round(costMat * 100d) / 100d, Math.round(costMnoRV * 100d) / 100d, Math.round(costEquipRV * 100d) / 100d);

            tx.commit();
            session.close();
            return datosCostos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();

        return datosCostos;

    }

    private ObraEmpresaNiveleEspecificosCostos getDatosConceptosRV(int idO, int idEmp) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<Nivelespecifico> unidadobras = new ArrayList<>();
            unidadobras = obra.getNivelespecificosById().parallelStream().filter(unidadobra -> unidadobra.getEmpresaconstructoraId() == idEmp).collect(Collectors.toList());
            double costMat = unidadobras.parallelStream().map(Nivelespecifico::getCostomaterial).reduce(0.0, Double::sum);
            double costMnoRV = unidadobras.parallelStream().map(Nivelespecifico::getCostomano).reduce(0.0, Double::sum);
            double costEquipRV = unidadobras.parallelStream().map(Nivelespecifico::getCostoequipo).reduce(0.0, Double::sum);
            datosCostos = new ObraEmpresaNiveleEspecificosCostos(idO, idEmp, Math.round(costMat * 100d) / 100d, Math.round(costMnoRV * 100d) / 100d, Math.round(costEquipRV * 100d) / 100d);

            tx.commit();
            session.close();
            return datosCostos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();

        return datosCostos;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        actualizarConceptoEmpresaObraController = this;
    }

    private double getValorEmpresaObraConcepto(int idEmpresa, int idCon) {
        return obra.getEmpresaobraconceptosById().parallelStream().filter(item -> item.getEmpresaconstructoraId() == idEmpresa && item.getConceptosgastoId() == idCon).map(Empresaobraconcepto::getValor).findFirst().orElse(0.0);
    }

    private double getSalarioEmpresaObraConcepto(int idEmpresa, int idCon) {
        return obra.getEmpresaobraconceptosById().parallelStream().filter(item -> item.getEmpresaconstructoraId() == idEmpresa && item.getConceptosgastoId() == idCon).map(Empresaobraconcepto::getSalario).findFirst().orElse(0.0);
    }

    private double calcConcept10(double concepto9, Integer tipo) {
        double val = 0.0;
        double tempValue = 0.0;
        if (tipo == 1) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 2) {
            val = 0.04 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 3) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 4) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 5) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 6) {
            val = 0.02 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 7) {
            val = 0.02 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 8) {
            val = 0.04 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 9) {
            val = 0.02 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 10) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 13) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 14) {
            val = 0.02 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 15) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 16) {
            val = 0.06 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 17) {
            val = 0.01 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        }

        return tempValue;
    }

    private void updateEmpresaObraConceptoCoeficiente(int idEmp, int idConcepto, int idObra, double coef) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            Empresaobraconceptoscoeficientes eocc = new Empresaobraconceptoscoeficientes();
            eocc.setObraId(idObra);
            eocc.setEmpresaconstructoraId(idEmp);
            eocc.setConceptosgastoId(idConcepto);
            eocc.setCoeficiente(coef);
            session.saveOrUpdate(eocc);

            trx.commit();
            session.close();

        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void getDatosConceptos(GastosIndView indView) {
        conceptosgastoList = new ArrayList<>();
        obra = structureSingelton.getObra(indView.getIdObra());
        idEmpresa = indView.getIdEmpresa();
        idConcepto = indView.getIdConcepto();
        idObra = obra.getId();
        conceptosgastoList = getListGastos(obra.getSalarioId());
        concepto.setText(indView.getConcepto());
        corficiente.setText(String.valueOf(indView.getCoeficiente()));
        if (Double.parseDouble(indView.getValor()) != 0.0) {
            valor.setText(String.valueOf(indView.getValor()));
            valor.setEditable(false);
        } else if (Double.parseDouble(indView.getValor()) == 0.0) {
            valor.setText(String.valueOf(indView.getValor()));
        }
        conceptosgasto = conceptosgastoList.parallelStream().filter(conceptosgasto -> conceptosgasto.getId() == idConcepto).findFirst().orElse(null);
        if (conceptosgasto.getSubConceptosById().size() > 0) {
            btnList.setDisable(false);
            valor.setEditable(false);
        } else {
            btnList.setDisable(true);
            valor.setEditable(true);
        }

    }

    public void handleModificarConceptos(ActionEvent event) {
        // updateEmpresaConcepto(idEmpresa, idConcepto);
        updateEmpresaObraConceptoCoeficiente(idEmpresa, idConcepto, idObra, Double.parseDouble(corficiente.getText()));

        if (!valor.getText().equals("0.0")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Se recalcularán los valores dentro de la obra a partir del coeficiente afectado, espere por favor!! ");
            alert.showAndWait();

            if (obra.getTipo().trim().equals("UO")) {
                datosCostos = getDatosConceptos(idObra, idEmpresa);
            } else if (obra.getTipo().trim().equals("RV")) {
                datosCostos = getDatosConceptosRV(idObra, idEmpresa);
            }

            empresagastosArrayList = getAllGastosValores(idEmpresa);

            try {
                calcValoresdeGastos(empresagastosArrayList, obra, idEmpresa);
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setHeaderText("Información");
                alert1.setContentText("Datos actualizados");
                alert1.showAndWait();

            } catch (Exception ex) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setHeaderText("Información");
                alert1.setContentText("Error al actualizar los datos");
                alert1.showAndWait();
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setHeaderText("Información");
            alert1.setContentText("Datos actualizados");
            alert1.showAndWait();
        }

    }

    public void calcValoresdeGastos(ArrayList<Empresagastos> empresagastosArrayList, Obra obraSend, int idEmpresa) {
        ArrayList<Empresaobraconcepto> listEmpresaObraConcepto = new ArrayList<>();
        if (obraSend.getSalarioId() == 1) {
            double rest = 0.0;
            double concepto1 = datosCostos.getCostoMateriales();
            double concepto2 = datosCostos.getCostoMano();
            double concepto3 = datosCostos.getCostEquipo();
            double concepto4 = 0.0;
            double concepto5 = 0.0;
            double concepto6 = concepto1 + concepto2 + concepto3 + concepto4 + concepto5;
            double concepto7 = 0.0;
            double concepto8 = concepto7;
            double concepto9 = concepto6 + concepto8;
            double concepto10 = 0.0;
            double concepto11 = 0.0;
            double concepto12 = 0.0;
            double concepto13 = 0.0;
            double concepto14 = 0.0;
            double concepto15 = 0.0;
            double concepto16 = 0.0;
            double concepto17 = concepto10 + concepto11 + concepto12 + concepto13 + concepto14 + concepto15 + concepto16;
            double concepto18 = concepto9 + concepto17;
            rest = concepto9 - concepto1;
            double concepto19 = 0.2 * rest;
            double concepto20 = concepto18 + concepto19;
            for (Empresagastos empG : empresagastosArrayList) {
                if (empG.getConceptosgastoId() == 1) {
                    concepto1 = datosCostos.getCostoMateriales();
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto1, 0));
                } else if (empG.getConceptosgastoId() == 2) {
                    concepto2 = datosCostos.getCostoMano();
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto2, 0));
                } else if (empG.getConceptosgastoId() == 3) {
                    concepto3 = datosCostos.getCostEquipo();
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto3, 0));
                } else if (empG.getConceptosgastoId() == 4) {
                    if (empG.getPorciento() == 1.0) {
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto4, 0));
                    } else {
                        concepto4 = concepto1 + concepto2 + concepto3;
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto4, 0));
                    }
                } else if (empG.getConceptosgastoId() == 5) {
                    if (empG.getPorciento() == 1.0) {
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto5, 0));
                    } else {
                        rest = empG.getPorciento() / 100;
                        concepto5 = rest * concepto1;
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto5, 0));
                    }
                } else if (empG.getConceptosgastoId() == 6) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto6, 0));
                } else if (empG.getConceptosgastoId() == 7) {
                    if (empG.getPorciento() == 1.0) {
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto7, 0));
                    } else {
                        rest = empG.getPorciento() / 100;
                        concepto7 = rest * concepto6;
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto7, 0));
                    }
                } else if (empG.getConceptosgastoId() == 8) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto8, 0));
                } else if (empG.getConceptosgastoId() == 9) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto9, 0));
                } else if (empG.getConceptosgastoId() == 10) {
                    if (empG.getCalcular() == 1) {
                        double valFt = calcConcept10(concepto9, obraSend.getTipoobraId());
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), valFt, 0));
                    } else {
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto10, 0));
                    }
                } else if (empG.getConceptosgastoId() == 11) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto11, 0));
                } else if (empG.getConceptosgastoId() == 12) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto12, 0));
                } else if (empG.getConceptosgastoId() == 13) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto13, 0));
                } else if (empG.getConceptosgastoId() == 14) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto14, 0));
                } else if (empG.getConceptosgastoId() == 15) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto15, 0));
                } else if (empG.getConceptosgastoId() == 16) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto16, 0));
                } else if (empG.getConceptosgastoId() == 17) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto17, 0));
                } else if (empG.getConceptosgastoId() == 18) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto18, 0));
                } else if (empG.getConceptosgastoId() == 19) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto19, 0));
                } else if (empG.getConceptosgastoId() == 20) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, empG.getConceptosgastoId(), concepto20, 0));
                }
            }
        } else if (obraSend.getSalarioId() == 3 || obraSend.getSalarioId() == 2) {
            double rest = 0.0;
            double conceptoA1 = datosCostos.getCostoMateriales();
            double conceptoA2 = datosCostos.getCostoMano();
            double conceptoA3 = 0.0;
            if (conceptosgasto.getDescripcion().trim().equals("Uso de Equipos")) {
                conceptoA3 = Double.parseDouble(valor.getText());
            } else {
                conceptoA3 = getValorEmpresaObraConcepto(idEmpresa, conceptosgastoList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Uso de Equipos")).findFirst().map(Conceptosgasto::getId).get());
            }
            System.out.println("Equipos: " + conceptoA3);
            double conceptoA4 = 0.0;
            System.out.println("CDMO: " + conceptoA2);
            double cmoe = 0.0;
            if (obraSend.getTipo().trim().equals("UO")) {
                cmoe = utilCalcSingelton.getManodeObraEquipo(obraSend);
            } else if (obraSend.getTipo().trim().equals("RV")) {
                cmoe = utilCalcSingelton.getManodeObraEquipoRV(obraSend);
            }
            System.out.println("CMOE " + cmoe);
            double salarioC4 = 0.0;
            if (salario != 0.0 && getSalarioEmpresaObraConcepto(idEmpresa, conceptosgastoList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Otros Gastos Directos del Proceso Productivo")).findFirst().map(Conceptosgasto::getId).get()) == 0.0) {
                salarioC4 = salario;
            } else if (salario != 0.0 && getSalarioEmpresaObraConcepto(idEmpresa, conceptosgastoList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Otros Gastos Directos del Proceso Productivo")).findFirst().map(Conceptosgasto::getId).get()) != salario) {
                salarioC4 = salario;
            } else if (salario == 0.0 && getSalarioEmpresaObraConcepto(idEmpresa, conceptosgastoList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Otros Gastos Directos del Proceso Productivo")).findFirst().map(Conceptosgasto::getId).get()) != 0.0) {
                salarioC4 = getSalarioEmpresaObraConcepto(idEmpresa, conceptosgastoList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Otros Gastos Directos del Proceso Productivo")).findFirst().map(Conceptosgasto::getId).get());
            }
            System.out.println("SALRIO" + salarioC4);
            if (conceptosgasto.getDescripcion().trim().equals("Otros Gastos Directos del Proceso Productivo")) {
                conceptoA4 = Double.parseDouble(valor.getText());
            } else {
                conceptoA4 = getValorEmpresaObraConcepto(idEmpresa, conceptosgastoList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Otros Gastos Directos del Proceso Productivo")).findFirst().map(Conceptosgasto::getId).get());
            }
            double conceptoA5 = conceptoA1 + conceptoA2 + conceptoA3 + conceptoA4;
            double conceptoA6 = 0.0;
            if (conceptosgasto.getDescripcion().trim().equals("Gastos Asociados a la Producción de la Obra")) {
                conceptoA6 = Double.parseDouble(valor.getText());
            } else {
                conceptoA6 = getValorEmpresaObraConcepto(idEmpresa, conceptosgastoList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Gastos Asociados a la Producción de la Obra")).findFirst().map(Conceptosgasto::getId).get());
                System.out.println("C6: " + conceptoA6);
            }
            double conceptoA7 = conceptoA5 + conceptoA6;
            double conceptoA8 = 0.0;
            if (conceptosgasto.getDescripcion().trim().equals("Gastos Generales y de Administración")) {
                conceptoA8 = Double.parseDouble(valor.getText());
            } else {
                conceptoA8 = getValorEmpresaObraConcepto(idEmpresa, conceptosgastoList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Gastos Generales y de Administración")).findFirst().map(Conceptosgasto::getId).get());
                System.out.println("C8" + conceptoA8);
            }
            double calc = conceptoA2 + cmoe + salarioC4;
            System.out.println("COTA: " + calc);
            double calSimple = conceptoA6 + conceptoA8;
            double conceptoA9 = 0.0;
            if (calSimple > calc) {
                conceptoA9 = calc;
            } else if (calSimple == 0.0) {
                conceptoA9 = calc;
            } else {
                conceptoA9 = calSimple;
            }
            System.out.println("Concepto Simple: " + calc);
            System.out.println("Concepto 9 " + conceptoA9);
            double conceptoA10 = 0.0;
            if (conceptosgasto.getDescripcion().trim().equals("Otros Conceptos de Gastos")) {
                conceptoA10 = Double.parseDouble(valor.getText());
            } else {
                conceptoA10 = getValorEmpresaObraConcepto(idEmpresa, conceptosgastoList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Otros Conceptos de Gastos")).findFirst().map(Conceptosgasto::getId).get());

            }
            double conceptoA11 = 0.0;
            if (conceptosgasto.getDescripcion().trim().equals("Gastos Financieros")) {
                conceptoA11 = Double.parseDouble(valor.getText());
            } else {
                conceptoA11 = getValorEmpresaObraConcepto(idEmpresa, conceptosgastoList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Gastos Financieros")).findFirst().map(Conceptosgasto::getId).get());
            }
            double conceptoA12 = 0.0;
            if (conceptosgasto.getDescripcion().trim().equals("Gastos Tributarios")) {
                conceptoA12 = Double.parseDouble(valor.getText());
            } else {
                conceptoA12 = getValorEmpresaObraConcepto(idEmpresa, conceptosgastoList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Otros Conceptos de Gastos")).findFirst().map(Conceptosgasto::getId).get());
            }
            double conceptoA13 = conceptoA8 + conceptoA10 + conceptoA11 + conceptoA12;
            double conceptoA14 = conceptoA5 + conceptoA9 + conceptoA10 + conceptoA11 + conceptoA12;
            double conceptoA15 = 0.0;
            rest = conceptoA14 - conceptoA1 - conceptoA13;
            conceptoA15 = 0.15 * rest;
            double conceptoA16 = 0.0;
            if (conceptosgasto.getDescripcion().trim().equals("Impuestos sobre ventas autorizados por MFP")) {
                conceptoA16 = Double.parseDouble(valor.getText());
            } else {
                conceptoA16 = getValorEmpresaObraConcepto(idEmpresa, conceptosgastoList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Impuestos sobre ventas autorizados por MFP")).findFirst().map(Conceptosgasto::getId).get());
            }
            double conceptoA17 = conceptoA14 + conceptoA15 + conceptoA16;

            for (Conceptosgasto cg : conceptosgastoList) {
                if (cg.getDescripcion().trim().equals("Materiales")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA1, 0));
                } else if (cg.getDescripcion().trim().equals("Mano de Obra")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA2, 0));
                } else if (cg.getDescripcion().trim().equals("Uso de Equipos")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA3, 0));
                } else if (cg.getDescripcion().trim().equals("Otros Gastos Directos del Proceso Productivo")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA4, salarioC4));
                } else if (cg.getDescripcion().trim().equals("Costos Directos de Producción")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA5, 0));
                } else if (cg.getDescripcion().trim().equals("Gastos Asociados a la Producción de la Obra")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA6, 0));
                } else if (cg.getDescripcion().trim().equals("Total de Costos y Gastos de Producción de la Obra")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA7, 0));
                } else if (cg.getDescripcion().trim().equals("Gastos Generales y de Administración")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA8, 0));
                } else if (cg.getDescripcion().trim().equals("Gastos Indirectos")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA9, 0));
                } else if (cg.getDescripcion().trim().equals("Otros Conceptos de Gastos")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA10, 0));
                } else if (cg.getDescripcion().trim().equals("Gastos Financieros")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA11, 0));
                } else if (cg.getDescripcion().trim().equals("Gastos Tributarios")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA12, 0));
                } else if (cg.getDescripcion().trim().equals("Total de Gastos de la Obra")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA13, 0));
                } else if (cg.getDescripcion().trim().equals("Total de Costos y Gastos")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA14, 0));
                } else if (cg.getDescripcion().trim().equals("Impuestos sobre ventas autorizados por MFP")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA16, 0));
                } else if (cg.getDescripcion().trim().equals("Utilidad")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA15, 0));
                } else if (cg.getDescripcion().trim().equals("Precio del Servicio de Construcción y Montaje")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(idEmpresa, idObra, cg.getId(), conceptoA17, 0));
                }
            }
            insertValorestoGastos(listEmpresaObraConcepto);
        }

    }

    public void handleDesglozarConcepto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListValoresSbConceptos.fxml"));
            Parent proot = loader.load();

            ListValoresConceptosController controller = loader.getController();
            controller.loadData(actualizarConceptoEmpresaObraController, conceptosgasto, obra, idEmpresa);

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


}
