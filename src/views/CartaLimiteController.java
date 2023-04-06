package views;

import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import ar.com.fdvs.dj.util.customexpression.RecordsInPageCustomExpression;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Tuple;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class CartaLimiteController implements Initializable {

    public CartaLimiteController cartaLimiteController;

    @FXML
    private TableView<CartaLimiteModelView> dataTable;
    public Usuarios user;
    @FXML
    private TableColumn<CartaLimiteModelView, String> code;
    @FXML
    private TableColumn<CartaLimiteModelView, String> descrip;
    @FXML
    private TableColumn<CartaLimiteModelView, String> um;
    @FXML
    private TableColumn<CartaLimiteModelView, Double> cant;
    @FXML
    private TableColumn<CartaLimiteModelView, Double> dispo;

    @FXML
    private Label label_title;

    @FXML
    private JFXTextField filter;

    @FXML
    private JFXComboBox<String> empresaSelect;

    @FXML
    private JFXComboBox<String> zonaSelect;

    @FXML
    private JFXComboBox<String> objetosSelect;

    @FXML
    private JFXComboBox<String> especialidadesSelect;


    @FXML
    private JFXCheckBox obraUO;

    @FXML
    private JFXCheckBox obraRV;

    @FXML
    private JFXComboBox<String> obrasSelect;

    @FXML
    private JFXCheckBox checkCost;

    @FXML
    private JFXCheckBox checkGeneral;

    @FXML
    private JFXCheckBox checkZon;

    @FXML
    private JFXComboBox<String> comboReport;

    @FXML
    private ContextMenu clOptions;

    @FXML
    private JFXButton btnDespachar;

    @FXML
    private JFXButton btnCheck;


    private ObservableList<String> obrasList;
    private ObservableList<String> empresaList;
    private ObservableList<String> zonasList;
    private ObservableList<String> objetosList;
    private ObservableList<String> especialidadList;
    private ObservableList<CartaLimiteModelView> cartaLimiteModelViewObservableList;

    private ArrayList<Obra> obraArrayList;
    private ArrayList<Zonas> zonasArrayList;
    private ArrayList<Objetos> objetosArrayList;
    private ArrayList<Empresaobra> empresaobraArrayListArrayList;

    private ObservableList<CartaLimiteModelView> componentesObservableList;
    private ArrayList<Juegorecursos> recursosArrayList;
    private ArrayList<Semielaboradosrecursos> recursosSemielaboradosrecursos;
    @FXML
    private TableColumn<CartaLimiteModelView, Double> precio;

    private int checkVal;
    private boolean checkType;

    private ReportProjectStructureSingelton reportProjectStructureSingelton = ReportProjectStructureSingelton.getInstance();


    private ObservableList<String> getObrasList(int val) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            obraArrayList = new ArrayList<>();
            obrasList = FXCollections.observableArrayList();
            obraArrayList = (ArrayList<Obra>) session.createQuery("FROM Obra ").getResultList();

            if (val == 1) {
                obrasList.addAll(obraArrayList.parallelStream().filter(o -> o.getTipo().equals("UO")).map(obra -> obra.getId() + " - " + obra.getDescripion()).collect(Collectors.toList()));
            } else if (val == 2) {
                obrasList.addAll(obraArrayList.parallelStream().filter(o -> o.getTipo().equals("RV")).map(obra -> obra.getId() + " - " + obra.getDescripion()).collect(Collectors.toList()));
            }

            tx.commit();
            session.close();
            return obrasList;
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Error al tratar de cargar las obras");
            alert.showAndWait();
        }
        return FXCollections.emptyObservableList();
    }


    private Objetos getObjetos(int idOb) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Objetos objetos = session.get(Objetos.class, idOb);

            tx.commit();
            session.close();
            return objetos;
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Error al tratar de cargar las zonas");
            alert.showAndWait();
        }
        return new Objetos();
    }

    private Especialidades getEspecialidad(int idOb) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Especialidades especialidades = session.get(Especialidades.class, idOb);

            tx.commit();
            session.close();
            return especialidades;
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Error al tratar de cargar las zonas");
            alert.showAndWait();
        }
        return new Especialidades();
    }


    private ObservableList<String> getObjetosList(int idZona, int idObra, int idEmp, boolean type) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            objetosArrayList = new ArrayList<>();
            objetosList = FXCollections.observableArrayList();


            if (obraUO.isSelected() == true) {
                if (type == true) {
                    objetosArrayList = (ArrayList<Objetos>) session.createQuery("FROM Objetos WHERE zonasId =: idZ").setParameter("idZ", idZona).getResultList();
                    objetosList.addAll(objetosArrayList.parallelStream().map(objetos -> objetos.getId() + " - " + objetos.getDescripcion()).collect(Collectors.toList()));
                } else if (type == false) {
                    List<Object[]> dataObjetcts = session.createQuery("SELECT DISTINCT obj.id, obj.descripcion FROM Unidadobra uo LEFT JOIN Objetos obj ON uo.objetosId = obj.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEm").setParameter("idOb", idObra).setParameter("idEm", idEmp).getResultList();
                    for (Object[] row : dataObjetcts) {
                        objetosList.addAll(row[0].toString() + " - " + row[1].toString());
                    }
                }
            }

            if (obraRV.isSelected() == true) {
                if (type == true) {
                    objetosArrayList = (ArrayList<Objetos>) session.createQuery("FROM Objetos WHERE zonasId =: idZ").setParameter("idZ", idZona).getResultList();
                    objetosList.addAll(objetosArrayList.parallelStream().map(objetos -> objetos.getId() + " - " + objetos.getDescripcion()).collect(Collectors.toList()));
                } else if (type == false) {
                    List<Object[]> dataObjetcts = session.createQuery("SELECT DISTINCT obj.id, obj.descripcion FROM Nivelespecifico uo LEFT JOIN Objetos obj ON uo.objetosId = obj.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEm").setParameter("idOb", idObra).setParameter("idEm", idEmp).getResultList();
                    for (Object[] row : dataObjetcts) {
                        objetosList.addAll(row[0].toString() + " - " + row[1].toString());
                    }
                }
            }
            tx.commit();
            session.close();
            return objetosList;
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Error al tratar de cargar las zonas");
            alert.showAndWait();
        }
        return FXCollections.emptyObservableList();
    }


    private ObservableList<String> getEspecialidadList(int idObra, int idEmp, int idZon, int idObj, boolean check) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            especialidadList = FXCollections.observableArrayList();


            if (obraUO.isSelected() == true) {
                if (check == false) {
                    List<Object[]> dataObjetcts = session.createQuery("SELECT DISTINCT esp.id, esp.descripcion FROM Unidadobra uo LEFT JOIN Especialidades esp ON uo.especialidadesId = esp.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEm AND uo.objetosId =: idObj ").setParameter("idOb", idObra).setParameter("idEm", idEmp).setParameter("idObj", idObj).getResultList();
                    for (Object[] row : dataObjetcts) {
                        especialidadList.addAll(row[0].toString() + " - " + row[1].toString());
                    }
                } else if (check == true) {
                    List<Object[]> dataObjetcts = session.createQuery("SELECT DISTINCT esp.id, esp.descripcion FROM Unidadobra uo LEFT JOIN Especialidades esp ON uo.especialidadesId = esp.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEm AND  uo.zonasId =: idZ AND uo.objetosId =: idObj ").setParameter("idOb", idObra).setParameter("idEm", idEmp).setParameter("idZ", idZon).setParameter("idObj", idObj).getResultList();
                    for (Object[] row : dataObjetcts) {
                        especialidadList.addAll(row[0].toString() + " - " + row[1].toString());
                    }
                }
            }
            if (obraRV.isSelected() == true) {
                if (check == false) {
                    List<Object[]> dataObjetcts = session.createQuery("SELECT DISTINCT esp.id, esp.descripcion FROM Nivelespecifico uo LEFT JOIN Especialidades esp ON uo.especialidadesId = esp.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEm AND uo.objetosId =: idObj ").setParameter("idOb", idObra).setParameter("idEm", idEmp).setParameter("idObj", idObj).getResultList();
                    for (Object[] row : dataObjetcts) {
                        especialidadList.addAll(row[0].toString() + " - " + row[1].toString());
                    }
                } else if (check == true) {
                    List<Object[]> dataObjetcts = session.createQuery("SELECT DISTINCT esp.id, esp.descripcion FROM Nivelespecifico uo LEFT JOIN Especialidades esp ON uo.especialidadesId = esp.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEm AND  uo.zonasId =: idZ AND uo.objetosId =: idObj ").setParameter("idOb", idObra).setParameter("idEm", idEmp).setParameter("idZ", idZon).setParameter("idObj", idObj).getResultList();
                    for (Object[] row : dataObjetcts) {
                        especialidadList.addAll(row[0].toString() + " - " + row[1].toString());
                    }
                }
            }

            tx.commit();
            session.close();
            return especialidadList;
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Error al tratar de cargar las especialidades");
            alert.showAndWait();
        }
        return FXCollections.emptyObservableList();
    }

    private ArrayList<Despachoscl> despachosclEntityArrayList;

    public ArrayList<Despachoscl> getDespachosclEntityArrayList(int idObra) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            ArrayList<Despachoscl> despachoscls = new ArrayList<>();
            despachoscls = (ArrayList<Despachoscl>) session.createQuery(" FROM Despachoscl WHERE obraId =: idO").setParameter("idO", idObra).getResultList();

            tx.commit();
            session.close();
            return despachoscls;
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Error al tratar de cargar las obras");
            alert.showAndWait();
        }
        return new ArrayList<>();
    }

    public void handleGetEmpresas(ActionEvent event) {
        Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(obrasSelect.getValue().trim())).findFirst().get();
        empresaSelect.getItems().clear();
        empresaSelect.setItems(reportProjectStructureSingelton.getEmpresaList(obra.getId()));
    }


    public void handleGetObjetsFromZones(ActionEvent event) {
        Zonas zonas = reportProjectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(zonaSelect.getValue().trim())).findFirst().get();
        ObservableList<String> objet = FXCollections.observableArrayList();
        objet = reportProjectStructureSingelton.getObjetosList(zonas.getId());
        objetosSelect.setItems(getObjetosList(zonas.getId(), 1, 1, true));
    }

    public void handleGetObjetctFromEmpresa(ActionEvent event) {
        Empresaconstructora empresaconstructora = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(empresaSelect.getValue().trim())).findFirst().get();
        Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(obrasSelect.getValue().trim())).findFirst().get();

        objetosSelect.getItems().clear();
        objetosSelect.setItems(getObjetosList(1, obra.getId(), empresaconstructora.getId(), false));
    }

    private List<DespachosGeneralReport> despachosGeneralReportList;

    public void handleGetEspecialidades(ActionEvent event) {

        if (checkZon.isSelected() == true) {
            Empresaconstructora empresaconstructora = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(empresaSelect.getValue().trim())).findFirst().get();
            Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(obrasSelect.getValue().trim())).findFirst().get();
            Zonas zonas = reportProjectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(zonaSelect.getValue().trim())).findFirst().get();

            String[] partObj = objetosSelect.getValue().split(" - ");

            especialidadesSelect.getItems().clear();
            especialidadesSelect.setItems(getEspecialidadList(obra.getId(), empresaconstructora.getId(), zonas.getId(), Integer.parseInt(partObj[0]), true));

        } else if (checkZon.isSelected() == false) {
            Empresaconstructora empresaconstructora = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(empresaSelect.getValue().trim())).findFirst().get();
            Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(obrasSelect.getValue().trim())).findFirst().get();
            // Zonas zonas = reportProjectStructureSingelton.zonasArrayList.parallelStream().filter(item-> item.toString().trim().equals(zonaSelect.getValue().trim())).findFirst().get();
            String[] partObj = objetosSelect.getValue().split(" - ");

            especialidadesSelect.getItems().clear();
            especialidadesSelect.setItems(getEspecialidadList(obra.getId(), empresaconstructora.getId(), 1, Integer.parseInt(partObj[0]), true));

        }

    }

    private double getValDespachado(int idO, int idEm, int idZona, int idObje, int idEsp, int idSum) {
        return despachosclEntityArrayList.parallelStream().filter(despachos -> despachos.getObraId() == idO && despachos.getEmpresaId() == idEm && despachos.getZonaId() == idZona && despachos.getObjetoId() == idObje && despachos.getEspecialidadId() == idEsp && despachos.getSuministroId() == idSum).map(Despachoscl::getCantidad).reduce(0.0, Double::sum);
    }

    private UsuariosDAO usuariosDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usuariosDAO = UsuariosDAO.getInstance();
        cartaLimiteController = this;

        ObservableList<String> obrasList = FXCollections.observableArrayList();
        obrasList = reportProjectStructureSingelton.getObrasList();

        ObservableList<String> obrasListUO = FXCollections.observableArrayList();
        obrasListUO.addAll(reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.getTipo().trim().equals("UO")).map(Obra::toString).collect(Collectors.toList()));
        obrasSelect.setItems(obrasListUO);
        obraUO.setSelected(true);

        obraRV.setOnMouseClicked(event -> {
            if (obraRV.isSelected() == true) {
                obraUO.setSelected(false);
                ObservableList<String> obrasListRV = FXCollections.observableArrayList();
                obrasListRV.addAll(reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.getTipo().trim().equals("RV")).map(Obra::toString).collect(Collectors.toList()));
                obrasSelect.setItems(obrasListRV);
            } else if (obraRV.isSelected() == false) {
                obrasSelect.getItems().clear();
            }
        });

        obraUO.setOnMouseClicked(event -> {
            if (obraUO.isSelected() == true) {
                obraRV.setSelected(false);
                obrasSelect.getItems().clear();
                obrasSelect.setItems(obrasListUO);
            } else if (obraUO.isSelected() == false) {
                obrasSelect.getItems().clear();
            }
        });


        checkZon.setOnMouseClicked(event -> {
            if (checkZon.isSelected() == true) {
                zonaSelect.setDisable(false);
                Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(obrasSelect.getValue().trim())).findFirst().get();
                zonaSelect.setItems(reportProjectStructureSingelton.getZonasList(obra.getId()));
            } else if (checkZon.isSelected() == false) {
                zonaSelect.setDisable(true);
            }
        });

        checkCost.setOnMouseClicked(event -> {
            if (checkCost.isSelected() == true && dataTable.getItems().isEmpty() == false) {
                precio.setVisible(true);
                precio.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, Double>("cost"));
                precio.setPrefWidth(100);

            } else if (checkCost.isSelected() == false && dataTable.getItems().isEmpty() == false) {
                precio.setVisible(false);
            }
        });

        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.add("Despachos General");
        observableList.add("Carta Límite");
        observableList.add("Carta Limite General");
        comboReport.setItems(observableList);

        if (usuariosDAO.usuario.getGruposId() == 6) {
            for (MenuItem item : clOptions.getItems()) {
                item.setDisable(true);
            }

            btnCheck.setDisable(true);
            btnDespachar.setDisable(true);

        }

    }

    public void handleExportToExcel(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar exportación");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichero (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Datos Carta Limite");

            Map<String, Object[]> data = new HashMap<>();
            int countrow = 1;

            if (checkCost.isSelected() == false) {
                data.put("1", new Object[]{"Código", "Descripción", "UM", "Cantidad", "Disponible"});
                for (CartaLimiteModelView cl : dataTable.getItems()) {
                    countrow++;
                    Object[] datoscl = new Object[5];
                    datoscl[0] = cl.getCode();
                    datoscl[1] = cl.getDescrip();
                    datoscl[2] = cl.getUm();
                    datoscl[3] = cl.getCant();
                    datoscl[4] = cl.getDisp();
                    data.put(String.valueOf(countrow++), datoscl);
                }
            } else if (checkCost.isSelected() == true) {
                data.put("1", new Object[]{"Código", "Descripción", "UM", "Cantidad", "Costo"});
                for (CartaLimiteModelView cl : dataTable.getItems()) {
                    countrow++;
                    Object[] datoscl = new Object[5];
                    datoscl[0] = cl.getCode();
                    datoscl[1] = cl.getDescrip();
                    datoscl[2] = cl.getUm();
                    datoscl[3] = cl.getCant();
                    datoscl[4] = cl.getCost();
                    data.put(String.valueOf(countrow++), datoscl);
                }
            }

            Set<String> keySet = data.keySet();
            List<String> sortedList = new ArrayList<>();
            sortedList = myOrderList(keySet);


            int rownum = 0;
            for (String key : sortedList) {

                Row row = sheet.createRow(rownum++);
                Object[] objects = data.get(key);
                int cellnum = 0;
                for (Object obj : objects) {
                    Cell cell = row.createCell(cellnum++);
                    if (obj instanceof String)
                        cell.setCellValue((String) obj);
                    else if (obj instanceof Double)
                        cell.setCellValue((Double) obj);
                }
            }
            try {
                // this Writes the workbook gfgcontribute
                FileOutputStream out = new FileOutputStream(new File(file.getAbsolutePath()));
                workbook.write(out);
                out.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Trabajo Terminado");
                alert.setContentText("Fichero dispobible en: " + file.getAbsolutePath());
                alert.showAndWait();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private List<String> myOrderList(Set<String> keySet) {
        List<Integer> mappepList = keySet.parallelStream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        mappepList.sort((o1, o2) -> o1.compareTo(o2));
        return mappepList.parallelStream().map(integer -> String.valueOf(integer)).collect(Collectors.toList());
    }

    public void handleGetMateriales(ActionEvent event) {
        Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(obrasSelect.getValue().trim())).findFirst().get();
        despachosclEntityArrayList = new ArrayList<>();
        despachosclEntityArrayList = getDespachosclEntityArrayList(obra.getId());

        if (checkZon.isSelected()) {
            Empresaconstructora empresaconstructora = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(empresaSelect.getValue().trim())).findFirst().get();
            // Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item-> item.toString().trim().equals(obrasSelect.getValue().trim())).findFirst().get();
            Zonas zonas = reportProjectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(zonaSelect.getValue().trim())).findFirst().get();
            String[] partObj = objetosSelect.getValue().split(" - ");
            String[] partEsp = especialidadesSelect.getValue().split(" - ");
            dataTable.getItems().removeAll();

            cartaLimiteModelViewObservableList = FXCollections.observableArrayList();
            cartaLimiteModelViewObservableList = getRecursos(obra.getId(), empresaconstructora.getId(), zonas.getId(), Integer.parseInt(partObj[0]), Integer.parseInt(partEsp[0]), true);


            code.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, String>("code"));
            code.setPrefWidth(100);
            descrip.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, String>("descrip"));
            descrip.setPrefWidth(550);
            um.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, String>("um"));
            um.setPrefWidth(50);
            cant.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, Double>("cant"));
            dispo.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, Double>("disp"));
            cant.setPrefWidth(100);
            if (checkCost.isSelected() == true) {
                precio.setVisible(true);
                precio.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, Double>("cost"));
                precio.setPrefWidth(100);
            } else if (checkCost.isSelected() == false) {
                precio.setVisible(false);
            }
            FilteredList<CartaLimiteModelView> filteredData = new FilteredList<CartaLimiteModelView>(cartaLimiteModelViewObservableList, p -> true);

            SortedList<CartaLimiteModelView> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(dataTable.comparatorProperty());
            dataTable.getItems().removeAll();
            dataTable.setItems(sortedData);

            filter.textProperty().addListener((prop, old, text) -> {
                filteredData.setPredicate(suministrosView -> {
                    if (text == null || text.isEmpty()) {
                        return true;
                    }

                    String descrip = suministrosView.getDescrip().toLowerCase();
                    return descrip.contains(text.toLowerCase());
                });
            });


        } else if (!checkZon.isSelected()) {
            Empresaconstructora empresaconstructora = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(empresaSelect.getValue().trim())).findFirst().get();
            String[] partObj = objetosSelect.getValue().split(" - ");
            String[] partEsp = especialidadesSelect.getValue().split(" - ");
            cartaLimiteModelViewObservableList = FXCollections.observableArrayList();
            dataTable.getItems().removeAll();
            cartaLimiteModelViewObservableList = getRecursos(obra.getId(), empresaconstructora.getId(), 1, Integer.parseInt(partObj[0]), Integer.parseInt(partEsp[0]), false);

            code.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, String>("code"));
            code.setPrefWidth(100);
            descrip.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, String>("descrip"));
            descrip.setPrefWidth(550);
            um.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, String>("um"));
            um.setPrefWidth(50);
            cant.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, Double>("cant"));
            dispo.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, Double>("disp"));
            cant.setPrefWidth(100);
            if (checkCost.isSelected() == true) {
                precio.setVisible(true);
                precio.setCellValueFactory(new PropertyValueFactory<CartaLimiteModelView, Double>("cost"));
                precio.setPrefWidth(100);
            } else if (checkCost.isSelected() == false) {
                precio.setVisible(false);
            }

            FilteredList<CartaLimiteModelView> filteredData = new FilteredList<CartaLimiteModelView>(cartaLimiteModelViewObservableList, p -> true);

            SortedList<CartaLimiteModelView> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

            dataTable.setItems(sortedData);

            filter.textProperty().addListener((prop, old, text) -> {
                filteredData.setPredicate(suministrosView -> {
                    if (text == null || text.isEmpty()) {
                        return true;
                    }

                    String descrip = suministrosView.getDescrip().toLowerCase();
                    return descrip.contains(text.toLowerCase());
                });
            });

        }

    }

    public void desglosaSuministro(ActionEvent event) {
        if (dataTable.getItems().size() > 0) {
            ObservableList<CartaLimiteModelView> fulListToDesglose = FXCollections.observableArrayList();
            for (CartaLimiteModelView item : dataTable.getItems()) {
                fulListToDesglose.add(item);
                if (item.getTipo().equals("J")) {
                    fulListToDesglose.addAll(getComponentesObservableList(item));
                } else if (item.getTipo().equals("S")) {
                    fulListToDesglose.addAll(getComponentesObservableList(item));
                }
            }

            dataTable.setItems(fulListToDesglose);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!!");
            alert.setContentText("Esta operación debe realizarse con suministros eb la tabla");
            alert.showAndWait();
        }
    }

    private ObservableList<CartaLimiteModelView> getRecursos(int idOb, int idEm, int idZona, int idObje, int idEsp, boolean state) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            cartaLimiteModelViewObservableList = FXCollections.observableArrayList();
            if (obraUO.isSelected() == true) {
                if (!state) {
                    List<Object[]> datosCartaLimite = session.createQuery("SELECT bajo.idSuministro, SUM(bajo.cantidad), SUM(bajo.costo), bajo.tipo FROM Unidadobra uo LEFT JOIN Bajoespecificacion bajo ON uo.id = bajo.unidadobraId WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEm AND uo.objetosId =: idObj AND uo.especialidadesId =: idEsp GROUP BY bajo.idSuministro, bajo.tipo").setParameter("idOb", idOb).setParameter("idEm", idEm).setParameter("idObj", idObje).setParameter("idEsp", idEsp).getResultList();
                    for (Object[] row : datosCartaLimite) {
                        if (row[0] != null) {
                            if (row[3].toString().trim().equals("1")) {
                                Recursos recursos = session.get(Recursos.class, Integer.parseInt(row[0].toString()));
                                double despachos = getValDespachado(idOb, idEm, idZona, idObje, idEsp, Integer.parseInt(row[0].toString()));
                                double dispo = Double.parseDouble(row[1].toString()) - despachos;
                                if (recursos != null) {
                                    cartaLimiteModelViewObservableList.add(new CartaLimiteModelView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Math.round(Double.parseDouble(row[1].toString()) * 10000d) / 10000d, Math.round(dispo * 10000d) / 10000d, Double.parseDouble(row[2].toString()), row[3].toString(), recursos.getPreciomn()));
                                }
                            } else if (row[3].toString().trim().equals("J")) {
                                Juegoproducto recursos = session.get(Juegoproducto.class, Integer.parseInt(row[0].toString()));
                                double despachos = getValDespachado(idOb, idEm, idZona, idObje, idEsp, Integer.parseInt(row[0].toString()));
                                double dispo = Double.parseDouble(row[1].toString()) - despachos;
                                if (recursos != null) {
                                    cartaLimiteModelViewObservableList.add(new CartaLimiteModelView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Math.round(Double.parseDouble(row[1].toString()) * 10000d) / 10000d, Math.round(dispo * 10000d) / 10000d, Double.parseDouble(row[2].toString()), row[3].toString(), recursos.getPreciomn()));
                                }
                            } else if (row[3].toString().trim().equals("S")) {
                                Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, Integer.parseInt(row[0].toString()));
                                double despachos = getValDespachado(idOb, idEm, idZona, idObje, idEsp, Integer.parseInt(row[0].toString()));
                                double dispo = Double.parseDouble(row[1].toString()) - despachos;
                                if (recursos != null) {
                                    cartaLimiteModelViewObservableList.add(new CartaLimiteModelView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Math.round(Double.parseDouble(row[1].toString()) * 10000d) / 10000d, Math.round(dispo * 10000d) / 10000d, Double.parseDouble(row[2].toString()), row[3].toString(), recursos.getPreciomn()));
                                }
                            }
                        }

                    }
                } else if (state) {
                    List<Object[]> datosCartaLimite = session.createQuery("SELECT bajo.idSuministro, SUM(bajo.cantidad), SUM(bajo.costo), bajo.tipo FROM Unidadobra uo LEFT JOIN Bajoespecificacion bajo ON uo.id = bajo.unidadobraId WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEm AND uo.zonasId =: idZ AND uo.objetosId =: idObj AND uo.especialidadesId =: idEsp GROUP BY bajo.idSuministro, bajo.tipo").setParameter("idOb", idOb).setParameter("idEm", idEm).setParameter("idZ", idZona).setParameter("idObj", idObje).setParameter("idEsp", idEsp).getResultList();
                    for (Object[] row : datosCartaLimite) {
                        if (row[0] != null && row[3] != null) {
                            if (row[3].toString().trim().equals("1")) {
                                Recursos recursos = session.get(Recursos.class, Integer.parseInt(row[0].toString()));
                                double despachos = getValDespachado(idOb, idEm, idZona, idObje, idEsp, Integer.parseInt(row[0].toString()));
                                double dispo = Double.parseDouble(row[1].toString()) - despachos;
                                if (recursos != null) {
                                    cartaLimiteModelViewObservableList.add(new CartaLimiteModelView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Math.round(Double.parseDouble(row[1].toString()) * 10000d) / 10000d, Math.round(dispo * 10000d) / 10000d, Double.parseDouble(row[2].toString()), row[3].toString(), recursos.getPreciomn()));
                                }
                            } else if (row[3].toString().trim().equals("J")) {
                                Juegoproducto recursos = session.get(Juegoproducto.class, Integer.parseInt(row[0].toString()));
                                double despachos = getValDespachado(idOb, idEm, idZona, idObje, idEsp, Integer.parseInt(row[0].toString()));
                                double dispo = Double.parseDouble(row[1].toString()) - despachos;
                                if (recursos != null) {
                                    cartaLimiteModelViewObservableList.add(new CartaLimiteModelView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Math.round(Double.parseDouble(row[1].toString()) * 10000d) / 10000d, Math.round(dispo * 10000d) / 10000d, Double.parseDouble(row[2].toString()), row[3].toString(), recursos.getPreciomn()));
                                }
                            } else if (row[3].toString().trim().equals("S")) {
                                Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, Integer.parseInt(row[0].toString()));
                                double despachos = getValDespachado(idOb, idEm, idZona, idObje, idEsp, Integer.parseInt(row[0].toString()));
                                double dispo = Double.parseDouble(row[1].toString()) - despachos;
                                if (recursos != null) {
                                    cartaLimiteModelViewObservableList.add(new CartaLimiteModelView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Math.round(Double.parseDouble(row[1].toString()) * 10000d) / 10000d, Math.round(dispo * 10000d) / 10000d, Double.parseDouble(row[2].toString()), row[3].toString(), recursos.getPreciomn()));
                                }
                            }
                        }

                    }
                }

            }

            if (obraRV.isSelected() == true) {
                if (state == false) {
                    List<Object[]> datosCartaLimite = session.createQuery("SELECT bajo.idsuministro, SUM(bajo.cantidad), SUM(bajo.costo), bajo.tipo FROM Nivelespecifico uo LEFT JOIN Bajoespecificacionrv bajo ON uo.id = bajo.nivelespecificoId WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEm AND uo.objetosId =: idObj AND uo.especialidadesId =: idEsp GROUP BY bajo.idsuministro, bajo.tipo").setParameter("idOb", idOb).setParameter("idEm", idEm).setParameter("idObj", idObje).setParameter("idEsp", idEsp).getResultList();
                    for (Object[] row : datosCartaLimite) {
                        if (row[0] != null) {
                            if (row[3].toString().trim().equals("1")) {
                                Recursos recursos = session.get(Recursos.class, Integer.parseInt(row[0].toString()));
                                double despachos = getValDespachado(idOb, idEm, idZona, idObje, idEsp, Integer.parseInt(row[0].toString()));
                                double dispo = Double.parseDouble(row[1].toString()) - despachos;
                                if (recursos != null) {
                                    cartaLimiteModelViewObservableList.add(new CartaLimiteModelView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Math.round(Double.parseDouble(row[1].toString()) * 10000d) / 1000d, Math.round(dispo * 10000d) / 10000d, Double.parseDouble(row[2].toString()), row[3].toString(), recursos.getPreciomn()));
                                }
                            } else if (row[3].toString().trim().equals("J")) {
                                Juegoproducto recursos = session.get(Juegoproducto.class, Integer.parseInt(row[0].toString()));
                                double despachos = getValDespachado(idOb, idEm, idZona, idObje, idEsp, Integer.parseInt(row[0].toString()));
                                double dispo = Double.parseDouble(row[1].toString()) - despachos;
                                if (recursos != null) {
                                    cartaLimiteModelViewObservableList.add(new CartaLimiteModelView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Math.round(Double.parseDouble(row[1].toString()) * 10000d) / 1000d, Math.round(dispo * 10000d) / 10000d, Double.parseDouble(row[2].toString()), row[3].toString(), recursos.getPreciomn()));
                                }
                            } else if (row[3].toString().trim().equals("S")) {
                                Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, Integer.parseInt(row[0].toString()));
                                double despachos = getValDespachado(idOb, idEm, idZona, idObje, idEsp, Integer.parseInt(row[0].toString()));
                                double dispo = Double.parseDouble(row[1].toString()) - despachos;
                                if (recursos != null) {
                                    cartaLimiteModelViewObservableList.add(new CartaLimiteModelView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Math.round(Double.parseDouble(row[1].toString()) * 10000d) / 1000d, Math.round(dispo * 10000d) / 10000d, Double.parseDouble(row[2].toString()), row[3].toString(), recursos.getPreciomn()));
                                }

                            }
                        }
                    }
                } else if (state == true) {
                    List<Object[]> datosCartaLimite = session.createQuery("SELECT bajo.idsuministro, bajo.cantidad, bajo.costo, bajo.tipo FROM Nivelespecifico uo LEFT JOIN Bajoespecificacionrv bajo ON uo.id = bajo.nivelespecificoId WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEm AND uo.zonasId =: idZ AND uo.objetosId =: idObj AND uo.especialidadesId =: idEsp GROUP BY bajo.idsuministro, bajo.tipo").setParameter("idOb", idOb).setParameter("idEm", idEm).setParameter("idZ", idZona).setParameter("idObj", idObje).setParameter("idEsp", idEsp).getResultList();
                    for (Object[] row : datosCartaLimite) {
                        if (row[0] != null) {
                            if (row[3].toString().trim().equals("1")) {
                                Recursos recursos = session.get(Recursos.class, Integer.parseInt(row[0].toString()));
                                double despachos = getValDespachado(idOb, idEm, idZona, idObje, idEsp, Integer.parseInt(row[0].toString()));
                                double dispo = Double.parseDouble(row[1].toString()) - despachos;
                                if (recursos != null) {
                                    cartaLimiteModelViewObservableList.add(new CartaLimiteModelView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Math.round(Double.parseDouble(row[1].toString()) * 10000d) / 1000d, Math.round(dispo * 10000d) / 10000d, Double.parseDouble(row[2].toString()), row[3].toString(), recursos.getPreciomn()));
                                }
                            } else if (row[3].toString().trim().equals("J")) {
                                Juegoproducto recursos = session.get(Juegoproducto.class, Integer.parseInt(row[0].toString()));
                                double despachos = getValDespachado(idOb, idEm, idZona, idObje, idEsp, Integer.parseInt(row[0].toString()));
                                double dispo = Double.parseDouble(row[1].toString()) - despachos;
                                if (recursos != null) {
                                    cartaLimiteModelViewObservableList.add(new CartaLimiteModelView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Math.round(Double.parseDouble(row[1].toString()) * 10000d) / 1000d, Math.round(dispo * 10000d) / 10000d, Double.parseDouble(row[2].toString()), row[3].toString(), recursos.getPreciomn()));
                                }
                            } else if (row[3].toString().trim().equals("S")) {
                                Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, Integer.parseInt(row[0].toString()));
                                double despachos = getValDespachado(idOb, idEm, idZona, idObje, idEsp, Integer.parseInt(row[0].toString()));
                                double dispo = Double.parseDouble(row[1].toString()) - despachos;
                                if (recursos != null) {
                                    cartaLimiteModelViewObservableList.add(new CartaLimiteModelView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Math.round(Double.parseDouble(row[1].toString()) * 10000d) / 1000d, Math.round(dispo * 10000d) / 10000d, Double.parseDouble(row[2].toString()), row[3].toString(), recursos.getPreciomn()));
                                }
                            }
                        }
                    }
                }
            }
            tx.commit();
            session.close();
            return cartaLimiteModelViewObservableList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FXCollections.emptyObservableList();
    }

    public void handleDespachoForm(ActionEvent event) {
        Empresaconstructora empresaconstructora = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(empresaSelect.getValue().trim())).findFirst().get();
        Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(obrasSelect.getValue().trim())).findFirst().get();
        Zonas zonas = reportProjectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(zonaSelect.getValue().trim())).findFirst().get();

        String[] partObj = objetosSelect.getValue().split(" - ");
        String[] partEsp = especialidadesSelect.getValue().split(" - ");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Despachos.fxml"));
            Parent proot = loader.load();
            DespachosController controller = loader.getController();
            controller.pasarController(cartaLimiteModelViewObservableList.parallelStream().collect(Collectors.toList()), obra.getId(), empresaconstructora.getId(), zonas.getId(), Integer.parseInt(partObj[0]), Integer.parseInt(partEsp[0]), despachosclEntityArrayList, cartaLimiteController);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(proot));
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void checkUsuario(Usuarios usuario) {
        user = usuario;
    }

    public ObservableList<CartaLimiteModelView> getComponentesObservableList(CartaLimiteModelView cartaLimiteModelView) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            componentesObservableList = FXCollections.observableArrayList();
            recursosArrayList = new ArrayList<>();
            recursosSemielaboradosrecursos = new ArrayList<>();

            if (cartaLimiteModelView.getTipo().equals("J")) {
                recursosArrayList = (ArrayList<Juegorecursos>) session.createQuery("FROM Juegorecursos WHERE juegoproductoId =: idJ").setParameter("idJ", cartaLimiteModelView.getId()).getResultList();
                for (Juegorecursos items : recursosArrayList.parallelStream().filter(juegorecursos -> juegorecursos.getRecursosByRecursosId().getTipo().equals("1")).collect(Collectors.toList())) {
                    double valCant = cartaLimiteModelView.getCant() * items.getCantidad();
                    componentesObservableList.add(new CartaLimiteModelView(items.getRecursosId(), "*" + items.getRecursosByRecursosId().getCodigo(), items.getRecursosByRecursosId().getDescripcion(), items.getRecursosByRecursosId().getUm(), Math.round(valCant * 10000d) / 10000d, 0.0, items.getRecursosByRecursosId().getPreciomn(), items.getRecursosByRecursosId().getTipo(), 0.0));
                }
            } else if (cartaLimiteModelView.getTipo().equals("S")) {
                recursosSemielaboradosrecursos = (ArrayList<Semielaboradosrecursos>) session.createQuery("FROM Semielaboradosrecursos WHERE suministrossemielaboradosId =: idJ").setParameter("idJ", cartaLimiteModelView.getId()).getResultList();
                for (Semielaboradosrecursos items : recursosSemielaboradosrecursos.parallelStream().filter(semielaboradosrecursos -> semielaboradosrecursos.getRecursosByRecursosId().getTipo().equals("1")).collect(Collectors.toList())) {
                    double valCant = cartaLimiteModelView.getCant() * items.getCantidad();
                    componentesObservableList.add(new CartaLimiteModelView(items.getRecursosId(), "*" + items.getRecursosByRecursosId().getCodigo(), items.getRecursosByRecursosId().getDescripcion(), items.getRecursosByRecursosId().getUm(), Math.round(valCant * 10000d) / 10000d, 0.0, items.getRecursosByRecursosId().getPreciomn(), items.getRecursosByRecursosId().getTipo(), 0.0));
                }
            }

            tx.commit();
            session.close();
            return componentesObservableList;

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Error al tratar de cargar la composición del semielaborado");
            alert.showAndWait();
        }

        return FXCollections.emptyObservableList();
    }

    public void handleVerDespachos(ActionEvent event) {

        try {
            Empresaconstructora empresaconstructora = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(empresaSelect.getValue().trim())).findFirst().get();
            //  Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item-> item.toString().trim().equals(obrasSelect.getValue().trim())).findFirst().get();
            Zonas zonas = reportProjectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(zonaSelect.getValue().trim())).findFirst().get();

            String[] partObj = objetosSelect.getValue().split(" - ");
            String[] partEsp = especialidadesSelect.getValue().split(" - ");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("VerDespachos.fxml"));
            Parent proot = loader.load();
            VerDespachosController controller = loader.getController();
            controller.cargarDespacho(despachosclEntityArrayList.parallelStream().filter(des -> des.getEmpresaId() == empresaconstructora.getId() && des.getZonaId() == zonas.getId() && des.getObjetoId() == Integer.parseInt(partObj[0]) && des.getEspecialidadId() == Integer.parseInt(partEsp[0]) && des.getSuministroId() == dataTable.getSelectionModel().getSelectedItem().getId()).collect(Collectors.toList()));

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(proot));
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleValidarDespachoForm(ActionEvent event) {

        //Empresaconstructora empresaconstructora = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item-> item.toString().trim().equals(empresaSelect.getValue().trim())).findFirst().get();
        Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(obrasSelect.getValue().trim())).findFirst().get();
        //Zonas zonas = reportProjectStructureSingelton.zonasArrayList.parallelStream().filter(item-> item.toString().trim().equals(zonaSelect.getValue().trim())).findFirst().get();

        despachosclEntityArrayList = new ArrayList<>();
        despachosclEntityArrayList = getDespachosclEntityArrayList(obra.getId());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ValidarDespachos.fxml"));
            Parent proot = loader.load();
            ValidarDespachosController controller = loader.getController();
            controller.cargarDespacho(cartaLimiteController, despachosclEntityArrayList);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(proot));
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //observableList.add("Carta Límite");
    //observableList.add("Carta Límite General");
    public void handleDoReport(ActionEvent event) throws Exception {
        //Empresaconstructora empresaconstructora = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item-> item.toString().trim().equals(empresaSelect.getValue().trim())).findFirst().get();
        Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(obrasSelect.getValue().trim())).findFirst().get();
        //Zonas zonas = reportProjectStructureSingelton.zonasArrayList.parallelStream().filter(item-> item.toString().trim().equals(zonaSelect.getValue().trim())).findFirst().get();

        despachosclEntityArrayList = new ArrayList<>();
        despachosclEntityArrayList = getDespachosclEntityArrayList(obra.getId());
        if (comboReport.getValue().trim().equals("Despachos General")) {
            despachosGeneralReportList = new ArrayList<>();
            despachosGeneralReportList = getDespachosGeneral(obra.getId());
            despachosGeneralReportList.sort(Comparator.comparing(DespachosGeneralReport::getEmpresa).thenComparing(DespachosGeneralReport::getZona).thenComparing(DespachosGeneralReport::getObjeto).thenComparing(DespachosGeneralReport::getObjeto).thenComparing(DespachosGeneralReport::getEspecialidad));

            LocalDate date = LocalDate.now();
            Map parametros = new HashMap<>();

            Empresa empresa = reportProjectStructureSingelton.getEmpresa();
            parametros.put("obraName", obra.getCodigo() + " " + obra.getDescripion());
            parametros.put("fecha", DateTimeFormatter.ofPattern("dd/MM/YYYY").format(date));
            parametros.put("empresa", empresa.getNombre());
            parametros.put("comercial", empresa.getComercial());
            parametros.put("image", "templete/logoReport.jpg");
            parametros.put("reportName", "Despachos de la Carta Limite");


            try {
                DynamicReport dr = createReportGeneral();
                JRDataSource ds = new JRBeanCollectionDataSource(despachosGeneralReportList);
                JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
                JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);
                JasperViewer.viewReport(jp, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (comboReport.getValue().trim().equals("Carta Límite")) {

            if (dataTable.getItems().size() == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");
                alert.setContentText("Muestre los datos para la carta límite en al tabla");
                alert.showAndWait();
            } else {

                Empresaconstructora empresaconstructora = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(empresaSelect.getValue().trim())).findFirst().get();
                //Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item-> item.toString().trim().equals(obrasSelect.getValue().trim())).findFirst().get();
                Zonas zonas = reportProjectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(zonaSelect.getValue().trim())).findFirst().get();

                String[] partObj = objetosSelect.getValue().split(" - ");
                Objetos ob = getObjetos(Integer.parseInt(partObj[0]));
                String[] partEsp = especialidadesSelect.getValue().split(" - ");
                Especialidades especialidades = getEspecialidad(Integer.parseInt(partEsp[0]));

                Map parametros = new HashMap<>();
                List<String> list = new ArrayList<>();
                list.add("hola");
                Empresa empresa = reportProjectStructureSingelton.getEmpresa();
                parametros.put("empresa", empresa.getNombre());
                parametros.put("image", "templete/logoReport.jpg");

                parametros.put("numero", "No. CL" + obra.getId() + empresaconstructora.getCodigo().trim() + zonas.getCodigo().trim() + ob.getCodigo().trim() + especialidades.getCodigo().trim());
                parametros.put("obra", "Obra: " + obra.toString().trim());
                parametros.put("constructor", "Constructor: " + empresaconstructora.toString().trim());
                parametros.put("objeto", "Obj: " + ob.toString().trim());
                parametros.put("zona", "Zon: " + zonas.toString().trim());
                parametros.put("especialidad", "Esp: " + especialidades.toString().trim());
                parametros.put("title", "Carta Límite");
                parametros.put("contr", "No. de Contrato:");
                parametros.put("mep", "Código MEP:");
                parametros.put("statistics", list);

                List<ClPrintModel> clPrintModelList = new ArrayList<>();
                for (CartaLimiteModelView item : dataTable.getItems()) {
                    clPrintModelList.add(new ClPrintModel(item.getCode(), item.getDescrip(), item.getUm(), item.getCant(), " "));
                }


                try {
                    DynamicReport dr = createReportCartaLimiteSingle();
                    JRDataSource ds = new JRBeanCollectionDataSource(clPrintModelList);
                    JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
                    JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);
                    JasperViewer.viewReport(jp, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (comboReport.getValue().trim().equals("Carta Limite General")) {
            Empresaconstructora empresaconstructora = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(empresaSelect.getValue().trim())).findFirst().get();
            despachosGeneralReportList = new ArrayList<>();
            despachosGeneralReportList = getDespachosGeneralEmpresa(obra.getId(), empresaconstructora.getId());
            despachosGeneralReportList.sort(Comparator.comparing(DespachosGeneralReport::getEmpresa).thenComparing(DespachosGeneralReport::getZona).thenComparing(DespachosGeneralReport::getObjeto).thenComparing(DespachosGeneralReport::getObjeto).thenComparing(DespachosGeneralReport::getEspecialidad));

            List<CLGenericModel> clGenericModelList = new ArrayList<>();
            List<ClGlobalList> globalLists = new ArrayList<>();
            for (DespachosGeneralReport despachosGeneralReport : despachosGeneralReportList) {
                clGenericModelList.add(new CLGenericModel(obra.toString(), despachosGeneralReport.getEmpresa(), despachosGeneralReport.getZona(), despachosGeneralReport.getObjeto(), despachosGeneralReport.getEspecialidad()));
            }
            for (CLGenericModel clGenericModel : clGenericModelList.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                globalLists.add(createClGlobalReport(clGenericModel, despachosGeneralReportList));
            }


            List<JasperPrint> jasperPrintList = new ArrayList<>();
            for (ClGlobalList conveniosReportModel : globalLists) {

                Map parametros = new HashMap<>();
                List<String> list = new ArrayList<>();
                list.add("hola");
                Empresa empresa = reportProjectStructureSingelton.getEmpresa();
                parametros.put("empresa", empresa.getNombre());
                parametros.put("image", "templete/logoReport.jpg");

                parametros.put("numero", "No. CL" + obra.getId() + empresaconstructora.getCodigo().trim() + conveniosReportModel.getZona().trim().substring(0, 3) + conveniosReportModel.getObjeto().trim().substring(0, 2) + conveniosReportModel.getEspecialidad().trim().substring(0, 2));
                parametros.put("obra", "Obra: " + obra.toString().trim());
                parametros.put("constructor", "Constructor: " + empresaconstructora.toString().trim());
                parametros.put("objeto", "Obj: " + conveniosReportModel.getObjeto().trim());
                parametros.put("zona", "Zon: " + conveniosReportModel.getZona().trim());
                parametros.put("especialidad", "Esp: " + conveniosReportModel.getEspecialidad().trim());
                parametros.put("title", "Carta Límite");
                parametros.put("contr", "No. de Contrato:");
                parametros.put("mep", "Código MEP:");

                DynamicReport dr = createReportCartaLimiteMultiple();
                JRDataSource ds = new JRBeanCollectionDataSource(conveniosReportModel.getClPrintModelList());
                JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
                JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);
                jasperPrintList.add(jp);
            }


            //JasperViewer.viewReport(jasperPrintList, false);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar informe de convenios");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Convenios (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {

                try {
                    JRPdfExporter exporter = new JRPdfExporter();
                    exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList)); //Set as export input my list with JasperPrint s
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file.getAbsolutePath())); //or any other out stream
                    SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
                    exporter.setConfiguration(configuration);
                    exporter.exportReport();


                    try {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(file);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setHeaderText("Información");
                            alert.setContentText("Es imposible abrir la vista de impresión para el reporte del convenio, para solucionar este inconveniente su reporte ha sido guardado en: " + file.getAbsolutePath());
                            alert.showAndWait();
                        }

                    } catch (IOException e) {

                    }
                } catch (JRException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private ClGlobalList createClGlobalReport(CLGenericModel clGenericModel, List<DespachosGeneralReport> despachosGeneralReportList) {
        List<ClPrintModel> listaDeRecursos = new ArrayList<>();
        for (DespachosGeneralReport despachosGeneralReport : despachosGeneralReportList) {
            if (despachosGeneralReport.getEmpresa().trim().equals(clGenericModel.getEmpresa().trim()) && despachosGeneralReport.getZona().trim().equals(clGenericModel.getZona().trim()) && despachosGeneralReport.getObjeto().trim().equals(clGenericModel.getObjeto().trim()) && despachosGeneralReport.getEspecialidad().trim().equals(clGenericModel.getEspecialidad().trim())) {
                listaDeRecursos.add(new ClPrintModel(despachosGeneralReport.getCodeSum(), despachosGeneralReport.getDescrip(), despachosGeneralReport.getUm(), despachosGeneralReport.getCant(), " "));
            }
        }
        return new ClGlobalList(clGenericModel.getObra(), clGenericModel.getEmpresa(), clGenericModel.getZona(), clGenericModel.getObjeto(), clGenericModel.getEspecialidad(), listaDeRecursos);
    }

    private List<DespachosGeneralReport> getDespachosGeneral(int parseInt) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<DespachosGeneralReport> despachosListGeneral = new ArrayList<>();
            List<Tuple> datosList = session.createQuery("SELECT ec.id as ide, ec.codigo as ecCo, ec.descripcion as ecDes, zon.id as idZ, zon.codigo as codeZ, zon.desripcion as descZ, obj.id as idOb, obj.codigo as codeOb, obj.descripcion as objDes, esp.id as idE, esp.codigo as codEs, esp.descripcion as espDes, bajo.idSuministro, SUM(bajo.cantidad), SUM(bajo.costo), bajo.tipo FROM Unidadobra uo LEFT JOIN Empresaconstructora ec ON uo.empresaconstructoraId = ec.id LEFT JOIN Zonas zon ON uo.zonasId = zon.id LEFT JOIN Objetos obj ON uo.objetosId = obj.id LEFT JOIN Especialidades esp ON uo.especialidadesId = esp.id LEFT JOIN Bajoespecificacion bajo ON uo.id = bajo.unidadobraId WHERE uo.obraId =: idOb GROUP BY ec.id , ec.codigo, ec.descripcion, zon.id, zon.codigo, zon.desripcion, obj.id, obj.codigo, obj.descripcion, esp.id, esp.codigo, esp.descripcion, bajo.idSuministro, bajo.tipo", Tuple.class).setParameter("idOb", parseInt).getResultList();
            for (Tuple tuple : datosList) {
                if (tuple.get(15) != null && tuple.get(15).toString().trim().equals("1")) {
                    Recursos recursos = session.get(Recursos.class, Integer.parseInt(tuple.get(12).toString()));
                    if (recursos != null) {
                        double despachos = getValDespachado(parseInt, Integer.parseInt(tuple.get(0).toString()), Integer.parseInt(tuple.get(3).toString()), Integer.parseInt(tuple.get(6).toString()), Integer.parseInt(tuple.get(9).toString()), Integer.parseInt(tuple.get(12).toString()));
                        double dispo = Double.parseDouble(tuple.get(13).toString()) - despachos;
                        despachosListGeneral.add(new DespachosGeneralReport(tuple.get(1).toString() + " " + tuple.get(2).toString(), tuple.get(4).toString() + " " + tuple.get(5).toString(), tuple.get(7).toString() + " " + tuple.get(8).toString(), tuple.get(10).toString() + " " + tuple.get(11).toString(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Double.parseDouble(tuple.get(13).toString()), Math.round(dispo * 10000d) / 10000d));
                    }
                }
            }
            tx.commit();
            session.close();
            despachosListGeneral.size();
            return despachosListGeneral;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<DespachosGeneralReport> getDespachosGeneralEmpresa(int parseInt, int idEmpr) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<DespachosGeneralReport> despachosListGeneral = new ArrayList<>();
            List<Tuple> datosList = session.createQuery("SELECT ec.id as ide, ec.codigo as ecCo, ec.descripcion as ecDes, zon.id as idZ, zon.codigo as codeZ, zon.desripcion as descZ, obj.id as idOb, obj.codigo as codeOb, obj.descripcion as objDes, esp.id as idE, esp.codigo as codEs, esp.descripcion as espDes, bajo.idSuministro, SUM(bajo.cantidad), SUM(bajo.costo), bajo.tipo FROM Unidadobra uo LEFT JOIN Empresaconstructora ec ON uo.empresaconstructoraId = ec.id LEFT JOIN Zonas zon ON uo.zonasId = zon.id LEFT JOIN Objetos obj ON uo.objetosId = obj.id LEFT JOIN Especialidades esp ON uo.especialidadesId = esp.id LEFT JOIN Bajoespecificacion bajo ON uo.id = bajo.unidadobraId WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp GROUP BY ec.id , ec.codigo, ec.descripcion, zon.id, zon.codigo, zon.desripcion, obj.id, obj.codigo, obj.descripcion, esp.id, esp.codigo, esp.descripcion, bajo.idSuministro, bajo.tipo", Tuple.class).setParameter("idOb", parseInt).setParameter("idEmp", idEmpr).getResultList();
            for (Tuple tuple : datosList) {
                if (tuple.get(15) != null && tuple.get(15).toString().trim().equals("1")) {
                    Recursos recursos = session.get(Recursos.class, Integer.parseInt(tuple.get(12).toString()));
                    if (recursos != null) {
                        double despachos = getValDespachado(parseInt, Integer.parseInt(tuple.get(0).toString()), Integer.parseInt(tuple.get(3).toString()), Integer.parseInt(tuple.get(6).toString()), Integer.parseInt(tuple.get(9).toString()), Integer.parseInt(tuple.get(12).toString()));
                        double dispo = Double.parseDouble(tuple.get(13).toString()) - despachos;
                        despachosListGeneral.add(new DespachosGeneralReport(tuple.get(1).toString() + " " + tuple.get(2).toString(), tuple.get(4).toString() + " " + tuple.get(5).toString(), tuple.get(7).toString() + " " + tuple.get(8).toString(), tuple.get(10).toString() + " " + tuple.get(11).toString(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), Double.parseDouble(tuple.get(13).toString()), Math.round(dispo * 10000d) / 10000d));
                    }
                }
            }
            tx.commit();
            session.close();
            despachosListGeneral.size();
            return despachosListGeneral;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }


    public DynamicReport createReportGeneral() {

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
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

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
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/report8Letter.jrxml");


        AbstractColumn colempresa = ColumnBuilder.getNew()
                .setColumnProperty("empresa", String.class.getName())
                .setTitle("Empresa: ").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn colzona = ColumnBuilder.getNew()
                .setColumnProperty("zona", String.class.getName())
                .setTitle("Zona: ").setWidth(35)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn colobjeto = ColumnBuilder.getNew()
                .setColumnProperty("objeto", String.class.getName())
                .setTitle("Objeto: ").setWidth(40)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn colespecialidad = ColumnBuilder.getNew()
                .setColumnProperty("especialidad", String.class.getName())
                .setTitle("Especialidad: ").setWidth(70)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnacode = ColumnBuilder.getNew()
                .setColumnProperty("codeSum", String.class.getName())
                .setTitle("Código").setWidth(20)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadesc = ColumnBuilder.getNew()
                .setColumnProperty("descrip", String.class.getName())
                .setTitle("Descripción").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacant = ColumnBuilder.getNew()
                .setColumnProperty("cant", Double.class.getName())
                .setTitle("Cantidad").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacostUnitario = ColumnBuilder.getNew()
                .setColumnProperty("dispo", Double.class.getName())
                .setTitle("Disponible").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        GroupBuilder gb1 = new GroupBuilder();
        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) colempresa)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        GroupBuilder gb2 = new GroupBuilder();
        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) colzona)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        GroupBuilder gb3 = new GroupBuilder();
        DJGroup g3 = gb3.setCriteriaColumn((PropertyColumn) colobjeto)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        GroupBuilder gb5 = new GroupBuilder();
        DJGroup g5 = gb5.setCriteriaColumn((PropertyColumn) colespecialidad)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)
                .build();

        drb.addColumn(colempresa);
        drb.addColumn(colzona);
        drb.addColumn(colobjeto);
        drb.addColumn(colespecialidad);

        drb.addGroup(g1);
        drb.addGroup(g2);
        drb.addGroup(g3);
        drb.addGroup(g5);

        drb.addColumn(columnacode);
        drb.addColumn(columnadesc);
        drb.addColumn(columnaum);
        drb.addColumn(columnacant);
        drb.addColumn(columnacostUnitario);


        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }

    public DynamicReport createReportCartaLimiteSingle() throws Exception {

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);
        detailStyle.setBorderTop(Border.PEN_1_POINT());
        detailStyle.setBorderLeft(Border.PEN_1_POINT());
        detailStyle.setBorderRight(Border.PEN_1_POINT());
        detailStyle.setBorderBottom(Border.PEN_1_POINT());

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
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(8, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = new Integer(20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(new Integer(15)).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/report8LetterCartaLimiteSing.jrxml");


        AbstractColumn columnaCustomExpression = ColumnBuilder.getNew()
                .setCustomExpression(new RecordsInPageCustomExpression())
                .setTitle("No.").setWidth(4)
                .setStyle(detailStyle).setHeaderStyle(headerStyle).build();

        AbstractColumn columnadesc = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacant = ColumnBuilder.getNew()
                .setColumnProperty("cant", Double.class.getName())
                .setTitle("Cantidad").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnobs = ColumnBuilder.getNew()
                .setColumnProperty("obser", String.class.getName())
                .setTitle("Observaciones").setWidth(20)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        DynamicReport drFooterSubreport3 = createHeaderSubreport();
        drb.addConcatenatedReport(drFooterSubreport3, new ClassicLayoutManager(), "statistics", DJConstants.DATA_SOURCE_ORIGIN_PARAMETER, DJConstants.DATA_SOURCE_TYPE_COLLECTION, false);


        drb.addColumn(columnaCustomExpression);
        drb.addColumn(columnadesc);
        drb.addColumn(columnaum);
        drb.addColumn(columnacant);
        drb.addColumn(columnobs);

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }


    public DynamicReport createReportCartaLimiteMultiple() throws Exception {

        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);
        detailStyle.setBorderTop(Border.PEN_1_POINT());
        detailStyle.setBorderLeft(Border.PEN_1_POINT());
        detailStyle.setBorderRight(Border.PEN_1_POINT());
        detailStyle.setBorderBottom(Border.PEN_1_POINT());

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
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(8, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = new Integer(20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(new Integer(15)).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/report8LetterCartaLimiteSing.jrxml");


        AbstractColumn columnaCustomExpression = ColumnBuilder.getNew()
                .setCustomExpression(new RecordsInPageCustomExpression())
                .setTitle("No.").setWidth(4)
                .setStyle(detailStyle).setHeaderStyle(headerStyle).build();

        AbstractColumn columnadesc = ColumnBuilder.getNew()
                .setColumnProperty("descripcion", String.class.getName())
                .setTitle("Descripción").setWidth(50)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(8)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnacant = ColumnBuilder.getNew()
                .setColumnProperty("cant", Double.class.getName())
                .setTitle("Cantidad").setWidth(14).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnobs = ColumnBuilder.getNew()
                .setColumnProperty("obser", String.class.getName())
                .setTitle("Observaciones").setWidth(20)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        drb.addColumn(columnaCustomExpression);
        drb.addColumn(columnadesc);
        drb.addColumn(columnaum);
        drb.addColumn(columnacant);
        drb.addColumn(columnobs);

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();
        return dr;

    }


    private DynamicReport createHeaderSubreport() throws Exception {
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
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);


        Style titleStyle = new Style();
        titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
        titleStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);

        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();

        Integer margin = (10);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(15).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setTemplateFile("templete/firmas.jrxml")
                .setUseFullPageWidth(true)
                .setOddRowBackgroundStyle(oddRowStyle);


        DynamicReport dr = drb.build();
        return dr;
    }


}






