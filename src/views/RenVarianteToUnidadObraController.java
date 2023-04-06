package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RenVarianteToUnidadObraController implements Initializable {


    public static List<Empresaobratarifa> empresaobratarifaList;
    public Integer idObra;
    public double valorEscala;
    public double salarioCalc;
    public double coeficienteEquipo;
    public double coeficienteMano;
    public List<Renglonrecursos> listRecursos;
    public Integer Tnorma;
    public Unidadobra unidadobraO;
    RenVarianteToUnidadObraController renVarianteToUnidadObraController;
    UnidadesObraObraController unidadesObraObraController;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    ReportProjectStructureSingelton structureSingelton = ReportProjectStructureSingelton.getInstance();
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField fieldCant;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXButton btn_exit;
    @FXML
    private Label labelUoCode;
    @FXML
    private TextArea textDedscripcion;
    @FXML
    private Label labelValue;
    @FXML
    private JFXButton btnSum;
    @FXML
    private JFXButton btnHoja;
    @FXML
    private JFXButton btnHelp;
    @FXML
    private JFXCheckBox checkTipoCosto;
    private ArrayList<Renglonvariante> renglonvarianteArrayList;
    private ObservableList<String> listRenglonVariante;
    private double costMat;
    private double costMano;
    private double costeEquip;
    private double costTotal;
    private double salario;
    private Unidadobrarenglon nivelrenglon;
    private Integer idUO;
    private Integer idRV;
    private double cant;
    private String pattern;
    private String umRev;
    private Integer idbajo;
    private Integer tipoC;
    private ArrayList<Bajoespecificacion> bajoespecificacionArrayList;
    private Bajoespecificacion bajoespecificacion;
    private Renglonvariante renglonvariante;
    private Runtime garbage;
    private Double cantidad;
    private int inputLength = 6;
    private double val;
    private TarifaSalarial tarifaSalarial;

    public void sobreEscribirRenglonUnidad(Unidadobrarenglon unidadobrarenglon) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Unidadobrarenglon WHERE unidadobraId =: idUni AND renglonvarianteId =: idRv").setParameter("idUni", unidadobrarenglon.getUnidadobraId()).setParameter("idRv", unidadobrarenglon.getRenglonvarianteId());

            Unidadobrarenglon unidadobrareng = (Unidadobrarenglon) query.getSingleResult();
            if (unidadobrareng != null) {
                unidadobrareng.setCantRv(unidadobrarenglon.getCantRv());
                unidadobrareng.setCostMat(unidadobrarenglon.getCostMat());
                unidadobrareng.setCostMano(unidadobrarenglon.getCostMano());
                unidadobrareng.setCostEquip(unidadobrarenglon.getCostEquip());
                unidadobrareng.setSalariomn(unidadobrarenglon.getSalariomn());
                unidadobrareng.setSalariocuc(unidadobrarenglon.getSalariocuc());
                unidadobrareng.setConMat(unidadobrarenglon.getConMat());
                session.update(unidadobrareng);
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

    private void sumarCantidadesRV(Unidadobrarenglon renglonnivelespecifico) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            cantidad = 0.0;
            Query query = session.createQuery("FROM Unidadobrarenglon WHERE unidadobraId =: idUni AND renglonvarianteId =: idRv").setParameter("idUni", renglonnivelespecifico.getUnidadobraId()).setParameter("idRv", renglonnivelespecifico.getRenglonvarianteId());
            Unidadobrarenglon unidadobrareng = (Unidadobrarenglon) query.getSingleResult();
            if (unidadobrareng != null) {
                cantidad = unidadobrareng.getCantRv() + renglonnivelespecifico.getCantRv();
                unidadobrareng.setCantRv(cantidad);
                if (renglonnivelespecifico.getConMat() == "0 ") {
                    unidadobrareng.setCostMat(0.0);
                } else {
                    unidadobrareng.setCostMat(renglonvariante.getCostomat() * cantidad);
                }
                unidadobrareng.setCostMano(renglonvariante.getCostmano() * cantidad * coeficienteMano);
                unidadobrareng.setCostEquip(renglonvariante.getCostequip() * cantidad * coeficienteEquipo);
                unidadobrareng.setSalariomn(salario * cantidad);
                unidadobrareng.setSalariocuc(0.0);
                unidadobrareng.setConMat(renglonnivelespecifico.getConMat());
                session.update(unidadobrareng);
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

    public void addRVtoNivelEspecifico(Unidadobrarenglon renglonnivelespecifico) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;

        try {
            trx = session.beginTransaction();
            session.persist(renglonnivelespecifico);
            trx.commit();
        } catch (PersistenceException pe) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(" El Renglón Variante " + field_codigo.getText() + " es parte de la unidad de obra, escoja Sumar si desea incrementar los valores o Sobreescribir si desea desechar los valores anteriores  ");

            ButtonType buttonAgregar = new ButtonType("Sumar");
            ButtonType buttonSobre = new ButtonType("Sobreescribir");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonAgregar, buttonSobre, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == buttonAgregar) {

                sumarCantidadesRV(renglonnivelespecifico);

                unidadesObraObraController.addElementTableRV(unidadobraO);

            } else if (result.get() == buttonSobre) {

                sobreEscribirRenglonUnidad(renglonnivelespecifico);
                unidadesObraObraController.addElementTableRV(unidadobraO);

            }


        } finally {
            session.close();
        }

    }

    public Renglonvariante rvToSugestion(String code, int tag) {
        return utilCalcSingelton.renglonvarianteList.parallelStream().filter(renglon -> renglon.getCodigo().trim().equals(code) && renglon.getRs() == tag).findFirst().orElse(null);

    }

    public void handleTipoCosto(ActionEvent event) {
        val = 0.0;
        if (checkTipoCosto.isSelected() == true) {
            costTotal = renglonvariante.getCostmano() + renglonvariante.getCostequip();
            val = Math.round(costTotal * 100d) / 100d;
            labelValue.setText(val + " / " + renglonvariante.getUm());
        } else if (checkTipoCosto.isSelected() == false) {
            if (renglonvariante != null) {
                costTotal = renglonvariante.getCostomat() + renglonvariante.getCostmano() + renglonvariante.getCostequip();
                val = Math.round(costTotal * 100d) / 100d;
                labelValue.setText(val + " / " + renglonvariante.getUm());
            }
        }
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == inputLength) {
            Tnorma = unidadobraO.getObraByObraId().getSalarioId();
            renglonvariante = rvToSugestion(field_codigo.getText(), unidadobraO.getObraByObraId().getSalarioId());
            if (renglonvariante == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setHeaderText("Renglón Variante no encontrado!!");
                alert.showAndWait();
            }
            textDedscripcion.setText(renglonvariante.getDescripcion());
            if (checkTipoCosto.isSelected() == true) {
                costTotal = Math.round(renglonvariante.getCostmano() * 100d) / 100d + Math.round(renglonvariante.getCostequip() * 100d) / 100d;
                labelValue.setText(costTotal + " / " + renglonvariante.getUm());
            } else {
                costTotal = Math.round(renglonvariante.getCostomat() * 100d) / 100d + Math.round(renglonvariante.getCostmano() * 100d) / 100d + Math.round(renglonvariante.getCostequip() * 100d) / 100d;
                labelValue.setText(costTotal + " / " + renglonvariante.getUm());
            }
            salario = utilCalcSingelton.calcSalarioRV(renglonvariante);
            labelValue.setText(Math.round(costTotal * 100d) / 100d + " / " + renglonvariante.getUm());
            fieldCant.requestFocus();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        renVarianteToUnidadObraController = this;

    }

    public void addUnidadObra(UnidadesObraObraController unidadesObraO, Unidadobra unidadobra) {

        unidadobraO = unidadobra;
        idObra = unidadobra.getObraId();
        labelUoCode.setText("UO: " + unidadobra.getCodigo());
        idUO = unidadobra.getId();

        coeficienteMano = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == unidadobra.getObraId() && eocc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 2).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
        coeficienteEquipo = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == unidadobra.getObraId() && eocc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 3).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);

        Tnorma = unidadobra.getObraByObraId().getSalarioId();

        checkTipoCosto.setSelected(true);

        unidadesObraObraController = unidadesObraO;
        tarifaSalarial = unidadobra.getObraByObraId().getTarifaSalarialByTarifa();
        empresaobratarifaList = new ArrayList<>();
        empresaobratarifaList = utilCalcSingelton.getEmpresaobratarifaList(unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId(), unidadobra.getObraByObraId().getTarifaId());

    }


    public void handleAddRenglonVariante(ActionEvent event) {

        if (fieldCant.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Debe indicar la cantidad del renglón variante!");
            alert.showAndWait();
        } else {

            nivelrenglon = new Unidadobrarenglon();
            nivelrenglon.setUnidadobraId(idUO);
            nivelrenglon.setRenglonvarianteId(renglonvariante.getId());
            nivelrenglon.setCantRv(Double.parseDouble(fieldCant.getText()));

            // double costoMano = new BigDecimal(String.format("%.2f", utilCalcSingelton.calcCostoManoRVinEmpresaObra(renglonvariante))).doubleValue();
            double costoMano = utilCalcSingelton.calcCostoManoRVinEmpresaObra(renglonvariante);
            // double costoEq = new BigDecimal(String.format("%.2f", renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum))).doubleValue();
            double costoEq = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double costMater = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double costMaterSemi = renglonvariante.getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double costMaterJueg = renglonvariante.getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);

            if (checkTipoCosto.isSelected() == true) {
                nivelrenglon.setConMat("0 ");
                nivelrenglon.setCostMat(0.0);
            } else {
                // double mat = new BigDecimal(String.format("%.2f", costMater + costMaterSemi + costMaterJueg)).doubleValue();
                double mat = costMater + costMaterSemi + costMaterJueg;
                nivelrenglon.setConMat("1 ");
                nivelrenglon.setCostMat(mat * Double.parseDouble(fieldCant.getText()));
            }
            nivelrenglon.setCostMano(costoMano * Double.parseDouble(fieldCant.getText()) * coeficienteMano);
            nivelrenglon.setCostEquip(costoEq * Double.parseDouble(fieldCant.getText()) * coeficienteEquipo);
            //nivelrenglon.setSalariomn(new BigDecimal(String.format("%.2f", salario)).doubleValue() * Double.parseDouble(fieldCant.getText()));
            nivelrenglon.setSalariomn(salario * Double.parseDouble(fieldCant.getText()));
            nivelrenglon.setSalariocuc(0.0);
            addRVtoNivelEspecifico(nivelrenglon);


            if (checkTipoCosto.isSelected() && bajoespecificacionArrayList != null) {
                for (Bajoespecificacion bajo : bajoespecificacionArrayList) {
                    idbajo = addBajoEspecificacion(bajo);
                }
                bajoespecificacionArrayList.clear();
            }

            clearField();
            unidadesObraObraController.addElementTableRV(unidadobraO);
            field_codigo.requestFocus();


        }

    }

    public void clearField() {
        field_codigo.clear();
        fieldCant.clear();
        labelValue.setText(" ");
        textDedscripcion.clear();
    }

    public void handleSumRVView(ActionEvent event) {

        if (fieldCant.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Debe especificar la cantidad de Renglón Variante a utilizar!");
            alert.showAndWait();
        } else {
            cant = Double.valueOf(fieldCant.getText());

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("suministrosInRV.fxml"));
                Parent proot = loader.load();
                SuministrosInRVController controller = loader.getController();
                controller.datosRV(renVarianteToUnidadObraController, renglonvariante.getId(), cant, idObra);


                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setResizable(false);
                stage.toFront();
                stage.show();


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void handleAyudaView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RenglonesVariantesUO.fxml"));
            Parent proot = loader.load();

            RenglonesUOController controller = loader.getController();
            controller.pasarDatos(renVarianteToUnidadObraController, unidadobraO.getObraByObraId().getSalarioId());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleHojadeCalculo(ActionEvent event) {
        if (!field_codigo.getText().isEmpty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("HojadeCalculo.fxml"));
                Parent proot = loader.load();
                HojadeCalcauloController controller = loader.getController();
                controller.LlenarDatosRV(renglonvariante, unidadobraO.getObraByObraId(), unidadobraO.getEmpresaconstructoraId());


                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.show();


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Debe especificar el código del Renglón Variante!");
            alert.showAndWait();
        }
    }

    public void getRenglonVarianteFronHelp(Renglonvariante renglonvarianteHelp) {

        field_codigo.clear();
        renglonvariante = renglonvarianteHelp;
        field_codigo.setText(renglonvariante.getCodigo());
        textDedscripcion.setText(renglonvariante.getDescripcion());

        salario = utilCalcSingelton.calcSalarioRV(renglonvariante);

        if (checkTipoCosto.isSelected() == false) {
            costTotal = renglonvariante.getCostomat() + renglonvariante.getCostmano() + renglonvariante.getCostequip();
            labelValue.setText(costTotal + " / " + renglonvariante.getUm());
        } else {
            costTotal = renglonvariante.getCostmano() + renglonvariante.getCostequip();
            labelValue.setText(costTotal + " / " + renglonvariante.getUm());
        }

    }

    public Integer addBajoEspecificacion(Bajoespecificacion bajoespecificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer idBe = null;
        Bajoespecificacion bajo = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(" FROM Bajoespecificacion WHERE unidadobraId =: unidad AND idSuministro =: suministro").setParameter("unidad", bajoespecificacion.getUnidadobraId()).setParameter("suministro", bajoespecificacion.getIdSuministro());

            try {
                bajo = (Bajoespecificacion) query.getSingleResult();
            } catch (NoResultException nre) {
            }
            if (bajo != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(" El Suministro " + field_codigo.getText() + " es parte de la unidad de obra, escoja Sumar si desea incrementar los valores o Sobreescribir si desea desechar los valores anteriores  ");

                ButtonType buttonAgregar = new ButtonType("Sumar");
                ButtonType buttonSobre = new ButtonType("Sobreescribir");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonAgregar, buttonSobre, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == buttonAgregar) {

                    sumarCantidadesSuministro(bajoespecificacion);
                    unidadesObraObraController.addElementTableSuministros(bajoespecificacion.getUnidadobraId());

                } else if (result.get() == buttonSobre) {

                    sobreEscribirSuministroUnidad(bajoespecificacion);
                    unidadesObraObraController.addElementTableSuministros(bajoespecificacion.getUnidadobraId());

                }

            } else {
                idBe = (Integer) session.save(bajoespecificacion);
            }


            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return idBe;

    }

    private void sobreEscribirSuministroUnidad(Bajoespecificacion bajoespecificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idUni AND idSuministro =: idRv").setParameter("idUni", bajoespecificacion.getUnidadobraId()).setParameter("idRv", bajoespecificacion.getIdSuministro());

            Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
            if (bajo != null) {
                bajo.setCantidad(bajoespecificacion.getCantidad());
                bajo.setCosto(bajoespecificacion.getCosto());
                bajo.setTipo(bajoespecificacion.getTipo());
                session.update(bajo);
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

    private void sumarCantidadesSuministro(Bajoespecificacion bajoespecificacion) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            cantidad = 0.0;
            Query query = session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idUni AND idSuministro =: idRv").setParameter("idUni", bajoespecificacion.getUnidadobraId()).setParameter("idRv", bajoespecificacion.getIdSuministro());

            if (bajoespecificacion.getTipo().trim().equals("1")) {
                Recursos recursos = session.get(Recursos.class, bajoespecificacion.getIdSuministro());
                Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
                if (bajo != null) {
                    double cantidad = bajo.getCantidad() + bajoespecificacion.getCantidad();
                    bajo.setCantidad(cantidad);
                    bajo.setCosto(Math.round(recursos.getPreciomn() * 100d) / 100d * cantidad);
                    bajo.setTipo(bajoespecificacion.getTipo());
                    session.update(bajo);
                }
            } else if (bajoespecificacion.getTipo().trim().equals("S")) {
                Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, bajoespecificacion.getIdSuministro());
                Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
                if (bajo != null) {
                    double cantidad = bajo.getCantidad() + bajoespecificacion.getCantidad();
                    bajo.setCantidad(cantidad);
                    bajo.setCosto(Math.round(recursos.getPreciomn() * 100d) / 100d * cantidad);
                    bajo.setTipo(bajoespecificacion.getTipo());
                    session.update(bajo);
                }
            } else if (bajoespecificacion.getTipo().trim().equals("J")) {
                Juegoproducto recursos = session.get(Juegoproducto.class, bajoespecificacion.getIdSuministro());
                Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
                if (bajo != null) {
                    double cantidad = bajo.getCantidad() + bajoespecificacion.getCantidad();
                    bajo.setCantidad(cantidad);
                    bajo.setCosto(Math.round(recursos.getPreciomn() * 100d) / 100d * cantidad);
                    bajo.setTipo(bajoespecificacion.getTipo());
                    session.update(bajo);
                }
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void createBajoespecificacionList(ArrayList<SumRVView> listOfSuminitrso) {

        bajoespecificacionArrayList = new ArrayList<Bajoespecificacion>();

        if (listOfSuminitrso != null) {
            for (SumRVView sumRVView : listOfSuminitrso) {
                bajoespecificacion = new Bajoespecificacion();
                bajoespecificacion.setUnidadobraId(idUO);
                bajoespecificacion.setIdSuministro(sumRVView.getId());
                bajoespecificacion.setTipo(sumRVView.getTipo());
                bajoespecificacion.setCantidad(sumRVView.getCantidad());
                bajoespecificacion.setCosto(Math.round(sumRVView.getCosto() * sumRVView.getCantidad() * 100d) / 100d);
                bajoespecificacion.setRenglonvarianteId(0);
                bajoespecificacion.setSumrenglon(0);
                bajoespecificacionArrayList.add(bajoespecificacion);
            }
        }


    }

    public void handleSearhRenglones(ActionEvent event) {
        pattern = field_codigo.getText();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searhRVUO.fxml"));
            Parent proot = loader.load();
            SearchRVUOController controller = loader.getController();
            controller.setRVDatos(renVarianteToUnidadObraController, pattern, unidadobraO.getObraByObraId().getSalarioId());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleCloseRVtoUO(ActionEvent event) {

        unidadesObraObraController.calcularElementosUO();
        garbage = Runtime.getRuntime();
        garbage.gc();
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        stage.close();

    }


}
