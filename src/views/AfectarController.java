package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AfectarController implements Initializable {
    private static double mult;
    UnidadesObraObraController unidadesObraObraController;
    @FXML
    private JFXRadioButton sumCnt;
    @FXML
    private JFXRadioButton restCnt;
    @FXML
    private JFXTextField rvFieldcant;
    @FXML
    private JFXRadioButton radioAll;
    @FXML
    private JFXRadioButton radioOne;
    @FXML
    private JFXTextField rvfieldCode;
    @FXML
    private JFXRadioButton sumSumin;
    @FXML
    private JFXRadioButton restSumin;
    @FXML
    private JFXTextField cantSumin;
    @FXML
    private JFXRadioButton sumiAll;
    @FXML
    private JFXRadioButton sumiOne;
    @FXML
    private JFXTextField codeSumin;
    @FXML
    private JFXButton btnAceptar;
    @FXML
    private JFXButton btnCancel;
    private List<Renglonrecursos> listRecursos;
    private double salarioCalc;
    private ObservableList<UORVTableView> datosRenglones;
    private ObservableList<BajoEspecificacionView> datosSuministros;
    private Unidadobra unidad;
    private Empresaobrasalario empresaobrasalario;
    private double sumBajo;
    private int batchSizeUOR = 5;
    private int countUOR;
    private int batchSizeBajo = 5;
    private int countBajo;
    private Renglonvariante renglon;
    private Renglonvariante renglonvariante;
    private Unidadobrarenglon unidadobrarenglon;
    private double valorEscala;
    private List<Unidadobrarenglon> unidadobrarenglonList;
    private double cant;
    private List<Bajoespecificacion> bajoespecificacionList;
    private Unidadobrarenglon urv;
    private Bajoespecificacion bajo;
    private Recursos recursos;
    private List<Recursos> recursosList;
    private List<Juegoproducto> juegoproductoList;
    private List<Suministrossemielaborados> suministrossemielaboradosList;
    private Bajoespecificacion bajoespecific;
    private double precio;

    private static double getParte(double cantidad, double valor) {
        mult = 0.0;
        mult = valor * cantidad;
        return mult / 100;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        sumCnt.setOnAction(event -> {
            if (sumCnt.isSelected() == true) {
                restCnt.setSelected(false);
            }
        });

        restCnt.setOnAction(event -> {
            if (restCnt.isSelected() == true) {
                sumCnt.setSelected(false);
            }
        });

        radioAll.setOnAction(event -> {
            if (radioAll.isSelected() == true) {
                radioOne.setSelected(false);
                rvfieldCode.setDisable(true);
            }
        });

        radioOne.setOnAction(event -> {
            if (radioOne.isSelected() == true) {
                radioAll.setSelected(false);
                rvfieldCode.setDisable(false);
            }
        });

        sumSumin.setOnAction(event -> {
            if (sumSumin.isSelected() == true) {
                restSumin.setSelected(false);
            }
        });

        restSumin.setOnAction(event -> {
            if (restSumin.isSelected() == true) {
                sumSumin.setSelected(false);
            }
        });

        sumiAll.setOnAction(event -> {
            if (sumiAll.isSelected() == true) {
                sumiOne.setSelected(false);
                codeSumin.setDisable(true);
            }
        });

        sumiOne.setOnAction(event -> {
            if (sumiOne.isSelected() == true) {
                sumiAll.setSelected(false);
                codeSumin.setDisable(false);
            }
        });

    }

    public void pasarDatos(UnidadesObraObraController unidadesController, Unidadobra unidadobra, ObservableList<UORVTableView> renglonesList, ObservableList<BajoEspecificacionView> suministrosList) {
        unidadesObraObraController = unidadesController;
        datosRenglones = FXCollections.observableArrayList();
        datosSuministros = FXCollections.observableArrayList();
        unidad = null;
        datosRenglones = renglonesList;
        datosSuministros = suministrosList;
        unidad = unidadobra;

        empresaobrasalario = getEmpresaobrasalario(unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId());

    }

    public Double getSumBajo(Unidadobra unid) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            sumBajo = 0.0;
            Double costMano = (Double) session.createQuery("SELECT SUM(costo) FROM Bajoespecificacion WHERE unidadobraId =: id ").setParameter("id", unid.getId()).getSingleResult();

            if (costMano != null) {
                sumBajo = costMano;
            }

            tx.commit();
            session.close();
            return sumBajo;

        } catch (Exception e) {

        }
        return sumBajo;
    }

    public void updateUnidadObra(Unidadobra unid) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Unidadobra update = session.get(Unidadobra.class, unid.getId());
            update.getUnidadobrarenglonsById().size();
            double costMano = update.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMano).reduce(0.0, Double::sum);
            double costEqui = update.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostEquip).reduce(0.0, Double::sum);
            double costMater = update.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMat).reduce(0.0, Double::sum);
            double salario = update.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getSalariomn).reduce(0.0, Double::sum);
            double costMateBajo = getSumBajo(unid);
            double cant = update.getUnidadobrarenglonsById().parallelStream().filter(unidadobraren -> unidadobraren.getRenglonvarianteByRenglonvarianteId().getCodigo().contentEquals(unid.getRenglonbase())).map(Unidadobrarenglon::getCantRv).findFirst().orElse(0.0);

            update.setCantidad(cant);
            update.setCostoMaterial(costMater + costMateBajo);
            update.setCostomano(costMano);
            update.setCostoequipo(costEqui);
            update.setSalario(salario);

            double total = costMater + costMateBajo + costMano + costEqui;
            double unit = total / cant;
            update.setCostototal(total);
            update.setCostounitario(unit);

            session.update(update);


            tx.commit();
            session.close();

        } catch (Exception e) {

        }

    }

    public Unidadobra getUnidadobra(Unidadobra unidad) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Unidadobra update = session.get(Unidadobra.class, unidad.getId());

            tx.commit();
            session.close();
            return update;

        } catch (Exception e) {

        }
        return new Unidadobra();
    }

    public void updateUnidadesObraRenglones(List<Unidadobrarenglon> unidadobrarenglonArray) {

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

                session.update(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateBajoEspecificacion(List<Bajoespecificacion> bajoespecificacionArray) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countBajo = 0;

            for (Bajoespecificacion bajoUpdate : bajoespecificacionArray) {
                countBajo++;
                if (countBajo > 0 && countBajo % batchSizeBajo == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(bajoUpdate);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Empresaobrasalario getEmpresaobrasalario(int idO, int idEmpresa) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaobrasalario = null;
            empresaobrasalario = (Empresaobrasalario) session.createQuery(" FROM Empresaobrasalario WHERE obraId =: idObra AND empresaconstructoraId =: idEmp").setParameter("idObra", idO).setParameter("idEmp", idEmpresa).getSingleResult();

            tx.commit();
            session.close();
            return empresaobrasalario;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return empresaobrasalario;
    }

    public Renglonvariante getRenglonvariante(int idRv) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            renglon = null;
            renglon = session.get(Renglonvariante.class, idRv);

            tx.commit();
            session.close();
            return renglon;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Renglonvariante();
    }

    public List<Renglonrecursos> getRecursosinRV(int idR) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listRecursos = new LinkedList<>();
            renglonvariante = session.get(Renglonvariante.class, idR);
            listRecursos = renglonvariante.getRenglonrecursosById();
            listRecursos.size();

            tx.commit();
            session.close();
            return listRecursos.parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().contentEquals("2")).collect(Collectors.toList());
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return new ArrayList<>();

    }

    private Unidadobrarenglon getUORV(int idUO, int idRV) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            unidadobrarenglon = null;
            unidadobrarenglon = (Unidadobrarenglon) session.createQuery("FROM Unidadobrarenglon WHERE unidadobraId =: idU AND renglonvarianteId =:idR").setParameter("idU", idUO).setParameter("idR", idRV).getSingleResult();

            tx.commit();
            session.close();
            return unidadobrarenglon;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Unidadobrarenglon();
    }

    public double getValorTarifa(String grupo) {
        valorEscala = 0;

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

    private double calcSalarioRV(int idReng) {
        listRecursos = new LinkedList<>();
        listRecursos = getRecursosinRV(idReng);
        salarioCalc = 0.0;
        for (Renglonrecursos item : listRecursos) {
            salarioCalc += item.getCantidas() * getValorTarifa(item.getRecursosByRecursosId().getGrupoescala());
        }
        return salarioCalc;
    }

    private Unidadobrarenglon createUnidadObraRenglonToUpdate(int idUnidad, int idRv, double newCant) {
        urv = null;
        urv = getUORV(idUnidad, idRv);
        renglon = null;
        renglon = getRenglonvariante(idRv);
        urv.setCantRv(newCant);
        urv.setCantRv(newCant);
        if (urv.getConMat().trim().equals("0")) {
            urv.setCostEquip(renglon.getCostequip() * newCant);
            urv.setCostMano(renglon.getCostmano() * newCant);
            urv.setSalariomn(calcSalarioRV(idRv) * newCant);
        } else if (urv.getConMat().trim().equals("1")) {
            urv.setCostMat(renglon.getCostomat() * newCant);
            urv.setCostEquip(renglon.getCostequip() * newCant);
            urv.setCostMano(renglon.getCostmano() * newCant);
            urv.setSalariomn(calcSalarioRV(idRv) * newCant);
        }
        return urv;
    }

    private Bajoespecificacion getBajoespecific(int idU, int idR) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            bajo = null;
            bajo = (Bajoespecificacion) session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idU AND idSuministro =:idR").setParameter("idU", idU).setParameter("idR", idR).getSingleResult();

            tx.commit();
            session.close();
            return bajo;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Bajoespecificacion();
    }

    private List<Recursos> getRecursos() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            recursosList = new ArrayList<>();
            recursosList = session.createQuery("FROM Recursos WHERE tipo ='1'").getResultList();
            tx.commit();
            session.close();
            return recursosList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private List<Juegoproducto> getJuegoproductoList() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            juegoproductoList = new ArrayList<>();
            juegoproductoList = session.createQuery("FROM Juegoproducto").getResultList();
            tx.commit();
            session.close();
            return juegoproductoList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private List<Suministrossemielaborados> getSuministrossemielaboradosList() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            suministrossemielaboradosList = new ArrayList<>();
            suministrossemielaboradosList = session.createQuery("FROM Suministrossemielaborados ").getResultList();
            tx.commit();
            session.close();
            return suministrossemielaboradosList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private Bajoespecificacion createBajoEspecificacion(int idUn, int idSum, double newCant) {

        bajoespecific = null;
        bajoespecific = getBajoespecific(idUn, idSum);
        if (bajoespecific.getTipo().contentEquals("1")) {
            precio = 0.0;
            precio = getRecursos().parallelStream().filter(recursos1 -> recursos1.getId() == idSum).map(Recursos::getPreciomn).findFirst().orElse(0.0);

        } else if (bajoespecific.getTipo().contentEquals("J")) {
            precio = 0.0;
            precio = getJuegoproductoList().parallelStream().filter(recursos1 -> recursos1.getId() == idSum).map(Juegoproducto::getPreciomn).findFirst().orElse(0.0);

        } else if (bajoespecific.getTipo().contentEquals("S")) {
            precio = 0.0;
            precio = getSuministrossemielaboradosList().parallelStream().filter(recursos1 -> recursos1.getId() == idSum).map(Suministrossemielaborados::getPreciomn).findFirst().orElse(0.0);

        }
        bajoespecific.setCantidad(newCant);
        bajoespecific.setCosto(newCant * precio);
        return bajoespecific;
    }

    public void calcular(ActionEvent event) {
        if (sumCnt.isSelected() == true && radioAll.isSelected() == true && sumiAll.isSelected() == true && sumSumin.isSelected()) {
            if (rvFieldcant.getText() != null) {
                unidadobrarenglonList = new ArrayList<>();
                for (UORVTableView rv : datosRenglones) {
                    cant = 0.0;
                    cant = getParte(rv.getCant(), Double.parseDouble(rvFieldcant.getText().trim())) + rv.getCant();
                    unidadobrarenglonList.add(createUnidadObraRenglonToUpdate(unidad.getId(), rv.getId(), cant));
                }
                updateUnidadesObraRenglones(unidadobrarenglonList);
                rvFieldcant.setText(" ");
            }
            if (cantSumin.getText() != null) {
                bajoespecificacionList = new ArrayList<>();
                for (BajoEspecificacionView bajo : datosSuministros) {
                    cant = 0;
                    cant = getParte(bajo.getCantidadBe(), Double.parseDouble(cantSumin.getText().trim())) + bajo.getCantidadBe();
                    bajoespecificacionList.add(createBajoEspecificacion(unidad.getId(), bajo.getIdSum(), cant));
                }
                updateBajoEspecificacion(bajoespecificacionList);
                cantSumin.setText(" ");
            }
        }

        if (restCnt.isSelected() == true && radioAll.isSelected() == true && sumiAll.isSelected() == true && restSumin.isSelected() == true) {
            if (rvFieldcant.getText() != null) {
                unidadobrarenglonList = new ArrayList<>();
                for (UORVTableView rv : datosRenglones) {
                    double val = Double.parseDouble(rvFieldcant.getText().trim());
                    double part = getParte(rv.getCant(), val);
                    double calc = rv.getCant() - part;
                    unidadobrarenglonList.add(createUnidadObraRenglonToUpdate(unidad.getId(), rv.getId(), calc));
                }
                updateUnidadesObraRenglones(unidadobrarenglonList);
                rvFieldcant.setText(" ");
            }
            if (cantSumin.getText() != null) {
                bajoespecificacionList = new ArrayList<>();
                for (BajoEspecificacionView bajo : datosSuministros) {
                    cant = 0;
                    cant = bajo.getCantidadBe() - getParte(bajo.getCantidadBe(), Double.parseDouble(cantSumin.getText().trim()));
                    bajoespecificacionList.add(createBajoEspecificacion(unidad.getId(), bajo.getIdSum(), cant));
                }
                updateBajoEspecificacion(bajoespecificacionList);
                cantSumin.setText(" ");
            }

        }

        if (sumCnt.isSelected() == true && radioOne.isSelected() == true) {
            unidadobrarenglonList = new ArrayList<>();
            UORVTableView rev = null;
            for (UORVTableView datosRenglone : datosRenglones) {
                if (datosRenglone.getCodeRV().startsWith("*")) {
                    if (datosRenglone.getCodeRV().substring(1, 7).equals(rvfieldCode.getText().trim())) {
                        rev = datosRenglone;
                    }
                } else {
                    if (datosRenglone.getCodeRV().equals(rvfieldCode.getText().trim())) {
                        rev = datosRenglone;
                    }
                }
            }
            if (rev != null) {
                cant = 0;
                cant = getParte(rev.getCant(), Double.parseDouble(rvFieldcant.getText().trim())) + rev.getCant();
                unidadobrarenglonList.add(createUnidadObraRenglonToUpdate(unidad.getId(), rev.getId(), cant));
                updateUnidadesObraRenglones(unidadobrarenglonList);
            }
            rvFieldcant.setText(" ");
        } else if (restCnt.isSelected() == true && radioOne.isSelected() == true) {
            UORVTableView rev = null;
            unidadobrarenglonList = new ArrayList<>();
            for (UORVTableView datosRenglone : datosRenglones) {
                if (datosRenglone.getCodeRV().startsWith("*")) {
                    if (datosRenglone.getCodeRV().substring(1, 7).equals(rvfieldCode.getText().trim())) {
                        rev = datosRenglone;
                    }
                } else {
                    if (datosRenglone.getCodeRV().equals(rvfieldCode.getText().trim())) {
                        rev = datosRenglone;
                    }
                }
            }
            if (rev != null) {
                double val = Double.parseDouble(rvFieldcant.getText().trim());
                double part = getParte(rev.getCant(), val);
                double calc = rev.getCant() - part;
                unidadobrarenglonList.add(createUnidadObraRenglonToUpdate(unidad.getId(), rev.getId(), calc));
                updateUnidadesObraRenglones(unidadobrarenglonList);
            }
            rvFieldcant.setText(" ");
        }

        if (sumiOne.isSelected() == true && sumSumin.isSelected() == true) {
            bajoespecificacionList = new ArrayList<>();
            BajoEspecificacionView baj = datosSuministros.parallelStream().filter(uorvTableView -> uorvTableView.getCodigo().trim().equals(codeSumin.getText())).findFirst().orElse(null);
            if (baj != null) {
                cant = 0;
                cant = getParte(baj.getCantidadBe(), Double.parseDouble(cantSumin.getText().trim())) + baj.getCantidadBe();
                bajoespecificacionList.add(createBajoEspecificacion(unidad.getId(), baj.getIdSum(), cant));
                updateBajoEspecificacion(bajoespecificacionList);
            }
            cantSumin.setText(" ");
        } else if (sumiOne.isSelected() == true && restSumin.isSelected() == true) {
            bajoespecificacionList = new ArrayList<>();
            BajoEspecificacionView baj = datosSuministros.parallelStream().filter(uorvTableView -> uorvTableView.getCodigo().trim().equals(codeSumin.getText())).findFirst().orElse(null);
            if (baj != null) {
                cant = 0;
                cant = baj.getCantidadBe() - getParte(baj.getCantidadBe(), Double.parseDouble(cantSumin.getText().trim()));
                bajoespecificacionList.add(createBajoEspecificacion(unidad.getId(), baj.getIdSum(), cant));
                updateBajoEspecificacion(bajoespecificacionList);
            }
            cantSumin.setText(" ");
        }
        updateUnidadObra(unidad);
        Unidadobra unidadobra = getUnidadobra(unidad);

        unidadesObraObraController.tableRenglones.getItems().clear();
        unidadesObraObraController.tableSuministros.getItems().clear();

        unidadesObraObraController.labelcostU.setText(" ");
        unidadesObraObraController.labelCosT.setText(" ");
        unidadesObraObraController.labelSal.setText(" ");
        unidadesObraObraController.labelCant.setText(" ");
        unidadesObraObraController.labelRVCant.setText(" ");
        unidadesObraObraController.labelPeso.setText(" ");
        unidadesObraObraController.labelRVum.setText(" ");
        unidadesObraObraController.textDescrp.setText(" ");

        unidadesObraObraController.addElementTableRV(unidadobra);//procedure store
        unidadesObraObraController.addElementTableSuministros(unidadobra.getId());//procedure store

        unidadesObraObraController.labelCant.setText(String.valueOf(unidadobra.getCantidad()));
        unidadesObraObraController.labelUM.setText(unidadobra.getUm());
        unidadesObraObraController.labelcostU.setText(String.valueOf(Math.round(unidadobra.getCostounitario() * 100d) / 100d));
        unidadesObraObraController.labelCosT.setText(String.valueOf(Math.round(unidadobra.getCostototal() * 100d) / 100d));
        unidadesObraObraController.sumCostoTotal = Math.round(unidadobra.getCostototal() * 100d) / 100d;
        unidadesObraObraController.labelSal.setText(String.valueOf(Math.round(unidadobra.getSalario() * 100d) / 100d));

        datosRenglones.removeAll();
        datosRenglones = unidadesObraObraController.getUoRVRelation(unidadobra);

        datosSuministros.removeAll();
        datosSuministros = unidadesObraObraController.getSumBajoEspecificacion(unidadobra.getId());
    }


    public void handleBtnClose(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

}
