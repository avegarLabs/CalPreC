package views;

import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.ConnectionModel;
import models.InsumosSearchView;
import models.Renglonrecursos;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class SearchResponseInsumoController implements Initializable {

    @FXML
    private TableView<InsumosSearchView> dataTable;

    @FXML
    private TableColumn<InsumosSearchView, StringProperty> zona;

    @FXML
    private TableColumn<InsumosSearchView, StringProperty> objeto;

    @FXML
    private TableColumn<InsumosSearchView, StringProperty> nivel;

    @FXML
    private TableColumn<InsumosSearchView, StringProperty> especialidad;

    @FXML
    private TableColumn<InsumosSearchView, StringProperty> subespecialidad;

    @FXML
    private TableColumn<InsumosSearchView, StringProperty> unidad;

    @FXML
    private TableColumn<InsumosSearchView, StringProperty> sumi;

    @FXML
    private TableColumn<InsumosSearchView, StringProperty> reng;

    @FXML
    private TableColumn<InsumosSearchView, DoubleProperty> cant;

    @FXML
    private TableColumn<InsumosSearchView, DoubleProperty> costo;

    @FXML
    private Label label_title;


    @FXML
    private JFXCheckBox checkCantidad;

    @FXML
    private JFXCheckBox checkCosto;


    private ObservableList<InsumosSearchView> viewObservableList;

    private double costoTotal;
    private double canti;

    public ObservableList<InsumosSearchView> getViewObservableList(int idOb, String codigo) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

/*
                List<Object[]> datosSum = session.createQuery("SELECT zon.codigo as zonC, obj.codigo as objC, niv.codigo as nivC, esp.codigo as espC, sub.codigo as subC, uo.codigo as uoC, bajo.cantidad, bajo.id FROM Bajoespecificacion bajo LEFT JOIN Unidadobra uo ON bajo.unidadobraId = uo.id LEFT JOIN Zonas zon ON uo.zonasId = zon.id LEFT JOIN Objetos obj ON uo.objetosId = obj.id LEFT JOIN Nivel niv ON uo.nivelId = niv.id LEFT JOIN Especialidades esp ON uo.especialidadesId = esp.id LEFT JOIN Subespecialidades sub ON uo.subespecialidadesId = sub.id LEFT JOIN Suministrossemielaborados rec ON bajo.idSuministro = rec.id AND bajo.tipo = 'S' WHERE uo.obraId =: idO GROUP BY zon.codigo, obj.codigo, niv.codigo, esp.codigo, sub.codigo, uo.codigo, bajo.cantidad, bajo.id").setParameter("idO", idOb).getResultList();
                for (Object[] row : datosSum) {
                    costoTotal = 0.0;
                    canti = 0.0;
                    Semielaboradosrecursos semielaboradosrecursos = (Semielaboradosrecursos) session.createQuery("FROM Semielaboradosrecursos WHERE suministrossemielaboradosId =: id ").setParameter("id", Integer.parseInt(row[7].toString())).getSingleResult();
                    if(semielaboradosrecursos.getRecursosByRecursosId().getCodigo().equals(codigo)) {
                        canti = Double.parseDouble(row[6].toString()) * semielaboradosrecursos.getCantidad();
                        costoTotal = canti * semielaboradosrecursos.getRecursosByRecursosId().getPreciomn();
                        viewObservableList.add(new InsumosSearchView(row[0].toString(), row[1].toString(), row[2].toString(), row[3].toString(), row[4].toString(), row[5].toString(), semielaboradosrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getCodigo(), " ", canti , costoTotal ));
                    }
                }
*/
            List<Object[]> datosSum1 = session.createQuery("SELECT zon.codigo as zonC, obj.codigo as objC, niv.codigo as nivC, esp.codigo as espC, sub.codigo as subC, uo.codigo as uoC, bajo.id, bajo.cantRv FROM Unidadobrarenglon bajo LEFT JOIN Unidadobra uo ON bajo.unidadobraId = uo.id LEFT JOIN Zonas zon ON uo.zonasId = zon.id LEFT JOIN Objetos obj ON uo.objetosId = obj.id LEFT JOIN Nivel niv ON uo.nivelId = niv.id LEFT JOIN Especialidades esp ON uo.especialidadesId = esp.id LEFT JOIN Subespecialidades sub ON uo.subespecialidadesId = sub.id LEFT JOIN Renglonvariante rec ON  bajo.renglonvarianteId = rec.id WHERE uo.obraId =: idO GROUP BY zon.codigo, obj.codigo, niv.codigo, esp.codigo, sub.codigo, uo.codigo, bajo.id, bajo.cantRv").setParameter("idO", idOb).getResultList();
            for (Object[] row : datosSum1) {
                canti = 0.0;
                costoTotal = 0.0;
                Renglonrecursos renglonrecursos = (Renglonrecursos) session.createQuery("FROM Renglonrecursos WHERE renglonvarianteId =: idR").setParameter("idR", Integer.parseInt(row[6].toString())).getSingleResult();
                if (renglonrecursos.getRecursosByRecursosId().getCodigo().equals(codigo)) {
                    canti = renglonrecursos.getCantidas() * Double.parseDouble(row[7].toString());
                    costoTotal = canti * renglonrecursos.getRecursosByRecursosId().getPreciomn();
                    viewObservableList.add(new InsumosSearchView(row[0].toString(), row[1].toString(), row[2].toString(), row[3].toString(), row[4].toString(), row[5].toString(), " ", renglonrecursos.getRenglonvarianteByRenglonvarianteId().getCodigo(), canti, costoTotal));
                }
            }

            tx.commit();
            session.close();
            return viewObservableList;

        } catch (Exception ex) {

        }

        return FXCollections.emptyObservableList();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        checkCantidad.setOnMouseClicked(event -> {
            if (checkCantidad.isSelected() == true) {
                cant.setVisible(true);
            } else if (checkCantidad.isSelected() == false) {
                cant.setVisible(false);
            }
        });

        checkCosto.setOnMouseClicked(event -> {
            if (checkCosto.isSelected() == true) {
                costo.setVisible(true);
            } else if (checkCosto.isSelected() == false) {
                costo.setVisible(false);
            }
        });

    }


    public void pasarDatos(int id, String codeS) {


        viewObservableList = FXCollections.observableArrayList();
        viewObservableList = getViewObservableList(id, codeS);
        zona.setCellValueFactory(new PropertyValueFactory<InsumosSearchView, StringProperty>("zona"));
        objeto.setCellValueFactory(new PropertyValueFactory<InsumosSearchView, StringProperty>("objeto"));
        nivel.setCellValueFactory(new PropertyValueFactory<InsumosSearchView, StringProperty>("nivel"));
        especialidad.setCellValueFactory(new PropertyValueFactory<InsumosSearchView, StringProperty>("especialidad"));
        subespecialidad.setCellValueFactory(new PropertyValueFactory<InsumosSearchView, StringProperty>("subespecialidad"));
        unidad.setCellValueFactory(new PropertyValueFactory<InsumosSearchView, StringProperty>("unidadObra"));
        sumi.setCellValueFactory(new PropertyValueFactory<InsumosSearchView, StringProperty>("suministro"));
        reng.setCellValueFactory(new PropertyValueFactory<InsumosSearchView, StringProperty>("renglon"));
        cant.setCellValueFactory(new PropertyValueFactory<InsumosSearchView, DoubleProperty>("cant"));
        costo.setCellValueFactory(new PropertyValueFactory<InsumosSearchView, DoubleProperty>("cost"));

        dataTable.setItems(viewObservableList);


    }


}

