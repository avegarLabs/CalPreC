package views;

import com.jfoenix.controls.JFXToolbar;
import com.jfoenix.controls.JFXTreeView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Especifica;
import models.Subgenerica;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AgrupacionEspecificaSuplementarController implements Initializable {

    private static SessionFactory sf;
    public SuplementarUnidadObraController nuevaController;
    @FXML
    private AnchorPane paneContent;
    private ArrayList<Subgenerica> subgenericaArrayList;
    private ArrayList<TreeItem> itemArrayList;

    private JFXTreeView treeView;
    private TreeItem treeItem;

    private String[] part;

    @FXML
    private JFXToolbar barTool;

    private Runtime garbage;


    public ArrayList<Subgenerica> itemSubgen(String code) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            subgenericaArrayList = new ArrayList<>();
            subgenericaArrayList = (ArrayList<Subgenerica>) session.createQuery(" FROM Subgenerica WHERE espcode =: codeE").setParameter("codeE", code).getResultList();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return subgenericaArrayList;
    }

    public ArrayList<TreeItem> itemSubEspecifica(Subgenerica subgenerica) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            itemArrayList = new ArrayList<TreeItem>();
            List<Especifica> listSub = session.get(Subgenerica.class, subgenerica.getId()).getEspecificasById();
            for (Especifica row : listSub) {
                itemArrayList.add(new TreeItem(row.getCodigo() + " " + row.getDescripcion()));
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


    public TreeItem createNode(Subgenerica subgenerica) {
        treeItem = new TreeItem(subgenerica.getCodigo() + " " + subgenerica.getDescripcion());
        treeItem.getChildren().addAll(itemSubEspecifica(subgenerica));

        return treeItem;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    public void pasarEspecialidad(SuplementarUnidadObraController controller, String codeEsp) {

        nuevaController = controller;

        treeView = new JFXTreeView();
        treeView.setPrefWidth(paneContent.getPrefWidth() - 10);
        treeView.setPrefHeight(paneContent.getPrefHeight() - 10);


        ArrayList<Subgenerica> datos = new ArrayList<>();
        datos = itemSubgen(codeEsp);

        ArrayList<TreeItem> list = new ArrayList<>();

        for (Subgenerica data : datos) {
            list.add(createNode(data));
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

                garbage = Runtime.getRuntime();
                garbage.gc();

                itemArrayList = null;
                subgenericaArrayList = null;
                treeItem = null;
                Stage stage = (Stage) paneContent.getScene().getWindow();
                stage.close();


            }
        });

    }
}
