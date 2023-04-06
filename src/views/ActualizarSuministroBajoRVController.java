package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ActualizarSuministroBajoRVController implements Initializable {

    private static SessionFactory sf;
    public ActualizarObraRVController suministrosCont;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_um;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXTextField field_peso;
    @FXML
    private JFXTextField field_precioTotal;
    @FXML
    private JFXTextField field_preciomlc;
    @FXML
    private JFXTextField text_mermaprod;
    @FXML
    private JFXTextField text_manipulacion;
    @FXML
    private JFXTextField text_transporte;
    @FXML
    private JFXTextField text_otras;
    private int id;

    private Recursos recurso;

    @FXML
    private JFXButton btnClose;
    private double costBajo;
    private List<Bajoespecificacionrv> bajoespecificacionList;
    /*
     * Method to get Unidad de obra
     */
    private List<Nivelespecifico> unidadobraList;


    /*
     * Method to get list of bajo especificacion tu update
     */
    private int idObra;
    private double costMat;
    private double bajo;
    private double costoMaterial = 0.0;
    private int batchSize = 10;
    private int count;
    private int batchSizeUO = 10;
    private int countUO;

    public Recursos getRecursos(Integer id) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            recurso = session.get(Recursos.class, id);

            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return recurso;

    }

    public double getCostoBajo(int idUO) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Bajoespecificacion> list = session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =:id").setParameter("id", idUO).getResultList();
            if (!list.isEmpty()) {
                costBajo = list.parallelStream().map(Bajoespecificacion::getCosto).reduce(0.0, Double::sum);
            } else {
                costBajo = 0.0;
            }
            tx.commit();
            session.close();
            return costBajo;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return costBajo;

    }

    /**
     * private List<Bajoespecificacionrv> getBajoespecificacionList(int idSum, int idOb) {
     * Session session = ConnectionModel.createAppConnection().openSession();
     * <p>
     * Transaction tx = null;
     * try {
     * tx = session.beginTransaction();
     * bajoespecificacionList = new ArrayList<>();
     * List<Object[]> datosBajo = session.createQuery(" FROM Bajoespecificacionrv b LEFT JOIN Nivelespecifico uo ON b.nivelespecificoId = uo.id WHERE b.idsuministro =: idS AND uo.obraId =: idO").setParameter("idS", idSum).setParameter("idO", idOb).getResultList();
     * int count = 0;
     * for (Object[] row : datosBajo) {
     * Bajoespecificacionrv b = (Bajoespecificacionrv) row[count++];
     * bajoespecificacionList.add(b);
     * }
     * tx.commit();
     * session.close();
     * return bajoespecificacionList;
     * } catch (HibernateException e) {
     * if (tx != null) tx.rollback();
     * e.printStackTrace();
     * } finally {
     * session.close();
     * }
     * return new ArrayList<>();
     * }
     */
    private List<Nivelespecifico> getUnidadobraList(int idSum, int idOb) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadobraList = new ArrayList<>();
            List<Object[]> datosListUnidad = session.createQuery("FROM Nivelespecifico uo LEFT JOIN Bajoespecificacionrv b ON uo.id = b.nivelespecificoId WHERE b.idsuministro =: idS AND uo.obraId =: idO").setParameter("idS", idSum).setParameter("idO", idOb).getResultList();
            int count = 0;
            for (Object[] row : datosListUnidad) {
                Nivelespecifico b = (Nivelespecifico) row[count++];
                unidadobraList.add(b);
            }
            tx.commit();
            session.close();
            return unidadobraList;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    /**
     * Method to UPDATE SuministroPropio
     */

    public void updateSuministrosPropio(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Recursos updateSuministros = session.get(Recursos.class, id);
            updateSuministros.setCodigo(field_codigo.getText());
            updateSuministros.setDescripcion(text_descripcion.getText());
            updateSuministros.setUm(field_um.getText());
            updateSuministros.setPeso(Double.parseDouble(field_peso.getText()));
            updateSuministros.setPreciomn(Double.parseDouble(field_precioTotal.getText()));
            updateSuministros.setPreciomlc(0.0);
            updateSuministros.setMermaprod(Double.parseDouble(text_mermaprod.getText()));
            updateSuministros.setMermatrans(Double.parseDouble(text_transporte.getText()));
            updateSuministros.setMermamanip(Double.parseDouble(text_manipulacion.getText()));
            updateSuministros.setOtrasmermas(Double.parseDouble(text_otras.getText()));
            session.update(updateSuministros);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void pasarParametros(ActualizarObraRVController controller, SuministrosView suministros) {
        suministrosCont = controller;

        idObra = 0;
        idObra = controller.obra.getId();
        recurso = new Recursos();
        recurso = getRecursos(suministros.getId());

        field_codigo.setText(recurso.getCodigo());
        field_peso.setText(String.valueOf(recurso.getPeso()));
        //field_preciomlc.setText(String.valueOf(recurso.getPreciomlc()));
        field_precioTotal.setText(String.valueOf(recurso.getPreciomn()));
        field_um.setText(recurso.getUm());
        text_descripcion.setText(recurso.getDescripcion());
        text_manipulacion.setText(String.valueOf(recurso.getMermamanip()));
        text_mermaprod.setText(String.valueOf(recurso.getMermaprod()));
        text_transporte.setText(String.valueOf(recurso.getMermatrans()));
        text_otras.setText(String.valueOf(recurso.getOtrasmermas()));
        id = recurso.getId();

    }

    public void clearFields() {

        field_codigo.clear();
        text_descripcion.clear();
        field_um.clear();
        field_peso.clear();
        field_precioTotal.clear();
        //field_preciomlc.clear();
        text_mermaprod.clear();
        text_transporte.clear();
        text_manipulacion.clear();
        text_otras.clear();

    }

    public void updateSuminitrosAction(ActionEvent event) {

        updateSuministrosPropio(id);
        List<Bajoespecificacionrv> list = new ArrayList<>();

        // list = getBajoespecificacionList(id, idObra);
        if (list.size() > 0) {
            updateListBajoEspecificacion(list);
            List<Nivelespecifico> listUnid = new ArrayList<>();
            listUnid = getUnidadobraList(id, idObra);
            setUpdateValores(listUnid);
        }
        suministrosCont.loadDatosSuministros();
        clearFields();

    }


    private void setUpdateValores(List<Nivelespecifico> unidadobraList) {

        for (Nivelespecifico unidadobra : unidadobraList) {
            updateUnidadObra(unidadobra);
        }

    }

    private void updateUnidadObra(Nivelespecifico unidad) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Nivelespecifico unidadobra = session.get(Nivelespecifico.class, unidad.getId());
            unidadobra.getRenglonnivelespecificosById().size();
            if (unidadobra.getRenglonnivelespecificosById().size() > 0) {
                // double costtMaterialInRV = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getCostmaterial).reduce(0.0, Double::sum);
                // double costMano = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getCostmano).reduce(0.0, Double::sum);
                //double costEquip = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getCostequipo).reduce(0.0, Double::sum);
                double salario = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getSalario).reduce(0.0, Double::sum);
                double costoMaterialBajo = getListSuminsitrosBajoEspecificacion(unidadobra).parallelStream().map(Bajoespecificacionrv::getCosto).reduce(0.0, Double::sum);

                // unidadobra.setCosto(Math.round(total * 100d) / 100d);
                unidadobra.setCostomaterial(costoMaterialBajo);
                //unidadobra.setCostomano(Math.round(costMano * 100d) / 100d);
                //unidadobra.setCostoequipo(Math.round(costEquip * 100d) / 100d);
                unidadobra.setSalario(salario);
                session.update(unidadobra);
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

    private List<Bajoespecificacionrv> getListSuminsitrosBajoEspecificacion(Nivelespecifico unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Bajoespecificacionrv> bajoespecificacionList = new ArrayList<>();
            bajoespecificacionList = session.createQuery(" FROM Bajoespecificacionrv WHERE nivelespecificoId =: idU ").setParameter("idU", unidadobra.getId()).getResultList();

            tx.commit();
            session.close();
            return bajoespecificacionList;
        } catch (
                Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


    private void updateListBajoEspecificacion(List<Bajoespecificacionrv> list) {
        List<Bajoespecificacionrv> list1BajoEsp = new ArrayList<>();

        for (Bajoespecificacionrv b : list) {
            costoMaterial = getCostoMaterialRecurso(b.getIdsuministro());
            b.setCosto(b.getCantidad() * costoMaterial);
            list1BajoEsp.add(b);
        }

        updateCostoMaterialRecursos(list1BajoEsp);

    }

    private void updateCostoMaterialRecursos(List<Bajoespecificacionrv> list1BajoEsp) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;

            for (Bajoespecificacionrv b : list1BajoEsp) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(b);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!!!");
            alert.setContentText("Error al actualizar los datos del suministro actualizado");
            alert.showAndWait();
        }
    }

    /*
    private void updateUnidadObraCost(List<Unidadobra> unidadobraList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUO = 0;

            for (Unidadobra uo : unidadobraList) {
                countUO++;
                if (countUO > 0 && countUO % batchSizeUO == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(uo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!!!");
            alert.setContentText("Error al actualizar el costo material de las unidades de obra");
            alert.showAndWait();
        }
    }
*/
    private double getCostoMaterialRecurso(int idSuministro) {
        return getRecursos(idSuministro).getPreciomn();
    }


    public void handleClose(ActionEvent event) {

        suministrosCont.loadDatosSuministros();
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

    }
}
