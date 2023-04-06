package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.*;

public class ActualizarCoeficientesEquipoController implements Initializable {
    private static SessionFactory sf;
    @FXML
    private JFXTextField obraField;
    @FXML
    private JFXTextField empresaField;
    @FXML
    private JFXTextField equipoField;
    @FXML
    private JFXTextField field_cpo;
    @FXML
    private JFXTextField field_cpe;
    @FXML
    private JFXTextField field_CET;
    @FXML
    private JFXTextField field_otras;
    @FXML
    private JFXButton btn_close;
    @FXML
    private JFXTextField salario;
    private int idEquipo;

    private Empresaconstructora empresaconst;
    private Obra obr;
    private Recursos recurso;

    private Coeficientesequipos coeficiente;
    private List<EquiposView> datosEquiposViewsList;
    private List<Coeficientesequipos> datosCoeficientesequiposList;
    private int batchSize = 20;
    private int count;

    /**
     * Method to UPDATE EquipoPropio
     */
    public void updateCoeficientedeEquipo(Coeficientesequipos coeficientesequipos) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            session.saveOrUpdate(coeficientesequipos);

            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void updateCoeficientesListEquipos(List<Coeficientesequipos> equiposViewList) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            count = 0;
            for (Coeficientesequipos ce : equiposViewList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }

                session.saveOrUpdate(ce);
            }

            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Recursos getEquipo(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Recursos rec = (Recursos) session.get(Recursos.class, id);
            tx.commit();
            session.close();
            return rec;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return new Recursos();
    }

    public Coeficientesequipos getEquipoCoeficientes(int idObra, int idEmp, int idEquipo) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            coeficiente = new Coeficientesequipos();
            List<Coeficientesequipos> datos = session.createQuery("FROM Coeficientesequipos WHERE obraId =: idOb AND empresaconstructoraId =: idEm AND recursosId =: idRec").setParameter("idOb", idObra).setParameter("idEm", idEmp).setParameter("idRec", idEquipo).getResultList();
            if (!datos.isEmpty()) {
                coeficiente = datos.get(0);
            }
            tx.commit();
            session.close();
            return coeficiente;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return new Coeficientesequipos();
    }


    @FXML
    void updateEquipoAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("¿Desea aplicar estos coeficientes a todos los equipos de la empresa en esta obra?");

        ButtonType buttonAgregar = new ButtonType("Actual");
        ButtonType buttonSobre = new ButtonType("A todos");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonAgregar, buttonSobre, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonAgregar) {
            Coeficientesequipos coeficientesequipos = new Coeficientesequipos();
            coeficientesequipos.setObraId(obr.getId());
            coeficientesequipos.setEmpresaconstructoraId(empresaconst.getId());
            coeficientesequipos.setRecursosId(recurso.getId());
            coeficientesequipos.setCpo(Double.parseDouble(field_cpo.getText()));
            coeficientesequipos.setCpe(Double.parseDouble(field_cpe.getText()));
            coeficientesequipos.setCet(Double.parseDouble(field_CET.getText()));
            coeficientesequipos.setOtra(Double.parseDouble(field_otras.getText()));

            updateCoeficientedeEquipo(coeficientesequipos);
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setContentText("Coeficientes actualizados!");
            alert1.showAndWait();

            Stage stage = (Stage) btn_close.getScene().getWindow();
            stage.close();

        } else if (result.get() == buttonSobre) {
            datosCoeficientesequiposList = new LinkedList<>();
            for (EquiposView view : datosEquiposViewsList) {
                Coeficientesequipos coeficientesequipos = new Coeficientesequipos();
                coeficientesequipos.setObraId(obr.getId());
                coeficientesequipos.setEmpresaconstructoraId(empresaconst.getId());
                coeficientesequipos.setRecursosId(view.getId());
                coeficientesequipos.setCpo(Double.parseDouble(field_cpo.getText()));
                coeficientesequipos.setCpe(Double.parseDouble(field_cpe.getText()));
                coeficientesequipos.setCet(Double.parseDouble(field_CET.getText()));
                coeficientesequipos.setOtra(Double.parseDouble(field_otras.getText()));
                datosCoeficientesequiposList.add(coeficientesequipos);
            }

            updateCoeficientesListEquipos(datosCoeficientesequiposList);
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setContentText("Coeficientes actualizados!");
            alert1.showAndWait();
            Stage stage = (Stage) btn_close.getScene().getWindow();
            stage.close();
        }


    }

    public void updateSalario(ActionEvent event) {
        Coeficientesequipos coeficientesequipos = new Coeficientesequipos();
        coeficientesequipos.setObraId(obr.getId());
        coeficientesequipos.setEmpresaconstructoraId(empresaconst.getId());
        coeficientesequipos.setRecursosId(recurso.getId());
        coeficientesequipos.setCpo(Double.parseDouble(field_cpo.getText()));
        coeficientesequipos.setCpe(Double.parseDouble(field_cpe.getText()));
        coeficientesequipos.setCet(Double.parseDouble(field_CET.getText()));
        coeficientesequipos.setOtra(Double.parseDouble(field_otras.getText()));
        coeficientesequipos.setSalario(Double.parseDouble(salario.getText()));

        updateCoeficientedeEquipo(coeficientesequipos);
        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        alert1.setContentText("Dato del Salario Actualizado");
        alert1.showAndWait();

        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void pasarParametros(EquiposView equipo, Empresaconstructora empresaconstructora, Obra obra, List<EquiposView> datos) {
        datosEquiposViewsList = new ArrayList<>();
        datosEquiposViewsList = datos;
        recurso = getEquipo(equipo.getId());
        empresaconst = empresaconstructora;
        obr = obra;
        coeficiente = getEquipoCoeficientes(obra.getId(), empresaconstructora.getId(), equipo.getId());

        empresaField.setText(empresaconstructora.getCodigo() + " " + empresaconstructora.getDescripcion());
        obraField.setText(obra.getCodigo() + " " + obra.getDescripion());
        equipoField.setText(equipo.getCodigo());

        if (coeficiente != null) {
            field_cpo.setText(String.valueOf(coeficiente.getCpo()));
            field_cpe.setText(String.valueOf(coeficiente.getCpe()));
            field_CET.setText(String.valueOf(coeficiente.getCet()));
            field_otras.setText(String.valueOf(coeficiente.getOtra()));
            salario.setText(String.valueOf(coeficiente.getSalario()));
        } else {
            field_cpo.setText(String.valueOf(recurso.getCpo()));
            field_cpe.setText(String.valueOf(recurso.getCpe()));
            field_CET.setText(String.valueOf(recurso.getCet()));
            field_otras.setText(String.valueOf(recurso.getOtra()));
            salario.setText("0.0");
        }

    }


    public void handleCloseWindow(ActionEvent event) {

        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();

    }


}
