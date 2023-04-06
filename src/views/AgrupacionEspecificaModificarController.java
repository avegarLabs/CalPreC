package views;

import com.jfoenix.controls.JFXTreeView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Subgenerica;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class AgrupacionEspecificaModificarController implements Initializable {

    private static SessionFactory sf;
    public ActualizarUnidadObraController nuevaController;
    @FXML
    private AnchorPane paneContent;
    private ArrayList<Subgenerica> subgenericaArrayList;
    private ArrayList<TreeItem> itemArrayList;

    private JFXTreeView treeView;
    private TreeItem treeItem;

    private String[] part;


    public ArrayList<TreeItem> itemSubgen(String code) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery(" FROM Subgenerica WHERE espcode =: codeE");
            query.setParameter("codeE", code);

            subgenericaArrayList = (ArrayList<Subgenerica>) query.getResultList();
            itemArrayList = new ArrayList<TreeItem>();
            for (Subgenerica item : subgenericaArrayList) {
                treeItem = new TreeItem(item.getCodigo() + " " + item.getDescripcion());
                itemArrayList.add(treeItem);
            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return itemArrayList;
    }

    public ArrayList<TreeItem> itemSubEspecifica(String codeSub) {
        itemArrayList = new ArrayList<TreeItem>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("SELECT esp.codigo, esp.descripcion FROM Especifica esp INNER JOIn Subgenerica sub ON esp.subgenericaId = sub.id WHERE sub.codigo =: code");
            query.setParameter("code", codeSub);

            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                String code = row[0].toString();
                String descrip = row[1].toString();

                treeItem = new TreeItem(code + " " + descrip);
                itemArrayList.add(treeItem);

            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return itemArrayList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    public void pasarEspecialidad(ActualizarUnidadObraController controller, String codeEsp) {

        nuevaController = controller;

        treeView = new JFXTreeView();
        treeView.setPrefWidth(paneContent.getPrefWidth() - 10);
        treeView.setPrefHeight(paneContent.getPrefHeight() - 10);

        itemArrayList = new ArrayList<TreeItem>();
        itemArrayList = itemSubgen(codeEsp);

        ArrayList<TreeItem> list = new ArrayList<>();

        for (TreeItem data : itemArrayList) {
            part = data.getValue().toString().split(" ");
            data.getChildren().addAll(itemSubEspecifica(part[0]));
            list.add(data);
        }


        TreeItem rootItem = new TreeItem("Específicas");
        rootItem.getChildren().addAll(list);
        treeView.setRoot(rootItem);

        treeView.setShowRoot(false);
        paneContent.getChildren().add(treeView);


        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                TreeItem<String> seleted = newValue;

                String codesp = seleted.getValue().substring(0, 4);
                nuevaController.unidadCode(codesp);

                Stage stage = (Stage) paneContent.getScene().getWindow();
                stage.close();


            }
        });

    }

    public void hadleClose(ActionEvent event) {
        Stage stage = (Stage) paneContent.getScene().getWindow();
        stage.close();

    }
}
