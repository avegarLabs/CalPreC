package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.*;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ImportarCatalogoController implements Initializable {

    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    Task tarea;
    @FXML
    private JFXButton btnClose;
    @FXML
    private JFXTextField catalogo;
    @FXML
    private JFXCheckBox chInsumos;
    @FXML
    private JFXCheckBox chJuegos;
    @FXML
    private JFXCheckBox chSemielaborados;
    @FXML
    private JFXCheckBox chCompJuegos;
    @FXML
    private JFXCheckBox chCompSemi;
    @FXML
    private JFXCheckBox chRenglones;
    @FXML
    private JFXCheckBox chCompRenglones;
    private String tag;
    private Integer idCat;
    private ProgressDialog stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        utilCalcSingelton.alterSequenceRCTreeG1();
        utilCalcSingelton.alterSequenceRCTreeG2();
        utilCalcSingelton.alterSequenceRCTreeG3();
        utilCalcSingelton.reviewRCTree();
    }

    public void handleActionDeclareCatalogo(ActionEvent event) {
        if (!catalogo.getText().isEmpty()) {
            Salario cat = new Salario();
            cat.setDescripcion(catalogo.getText().trim());
            cat.setAntiguedad(0.0);
            cat.setVacaciones(0.0);
            cat.setNomina(0.0);
            cat.setSeguro(0.0);
            cat.setAutesp(0.0);
            cat.setIi(0.0);
            cat.setIii(0.0);
            cat.setIv(0.0);
            cat.setV(0.0);
            cat.setVi(0.0);
            cat.setVii(0.0);
            cat.setViii(0.0);
            cat.setIx(0.0);
            cat.setX(0.0);
            cat.setXi(0.0);
            cat.setXii(0.0);
            cat.setXiii(0.0);
            cat.setXiv(0.0);
            cat.setXv(0.0);
            cat.setXvi(0.0);
            cat.setGtii(0.0);
            cat.setGtiii(0.0);
            cat.setGtiv(0.0);
            cat.setGtv(0.0);
            cat.setGtvi(0.0);
            cat.setGtvii(0.0);
            cat.setGtviii(0.0);
            cat.setGtix(0.0);
            cat.setGtx(0.0);
            cat.setGtxi(0.0);
            cat.setGtxii(0.0);
            cat.setGtxiii(0.0);
            cat.setGtxiv(0.0);
            cat.setGtxv(0.0);
            cat.setGtxvi(0.0);
            cat.setTag("T" + catalogo.getText().trim().charAt(0));

            idCat = addCatalogo(cat);
            if (idCat == null) {
                utilCalcSingelton.showAlert("Error al agregar el nuevo catálogo", 2);
            } else {
                utilCalcSingelton.showAlert("Catálogo creado satisfactoriamene!", 1);
                tag = cat.getTag().trim() + idCat.toString().toString();
                updateCatalogo(idCat, tag);
                chInsumos.setSelected(true);
                chInsumos.setDisable(false);
            }
        } else {
            utilCalcSingelton.showAlert("Complete el campo descripción!", 2);
        }

    }

    private Integer addCatalogo(Salario salario) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idSal = null;
        try {
            tx = session.beginTransaction();
            idSal = (Integer) session.save(salario);
            tx.commit();
            session.close();
            return idSal;
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return idSal;
    }

    private void updateCatalogo(int idCat, String tag) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Salario sal = (Salario) session.get(Salario.class, idCat);
            sal.setTag(tag);
            session.update(sal);
            tx.commit();
            session.close();
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void addCompoRenglonesToCatalogo(JSONObject jsonObject) {
        List<Renglonrecursos> tempRC = new ArrayList<>();
        List<Renglonjuego> tempRJ = new ArrayList<>();
        List<Renglonsemielaborados> tempRS = new ArrayList<>();
        JSONArray estructuraRenglon = (JSONArray) jsonObject.get("RV_ESTRUCTURA_OK");
        for (Object sum : estructuraRenglon) {
            JSONObject item = (JSONObject) sum;
            if (item.get("Tipo").toString().trim().equals("1") || item.get("Tipo").toString().trim().equals("2") || item.get("Tipo").toString().trim().equals("3")) {
                Renglonrecursos rc = createdRenglonRecurso(item);
                tempRC.add(rc);
            } else if (item.get("Tipo").toString().trim().equals("J")) {
                Renglonjuego rj = createdRenglonJuego(item);
                tempRJ.add(rj);
            } else if (item.get("Tipo").toString().trim().equals("S")) {
                Renglonsemielaborados rs = createdRengloSemi(item);
                tempRS.add(rs);

            }

        }
        utilCalcSingelton.saveRenglonesRecursosList(tempRC);
        utilCalcSingelton.saveRenglonesJuegoList(tempRJ);
        utilCalcSingelton.saveRenglonesSemielaboradosList(tempRS);
    }

    private Renglonsemielaborados createdRengloSemi(JSONObject item) {
        Renglonsemielaborados renglonsemielaborados = new Renglonsemielaborados();
        renglonsemielaborados.setRenglonvarianteId(getRenglonesbyCodeInFile(item.get("Renglon").toString().trim()).getId());
        renglonsemielaborados.setSuministrossemielaboradosId(getSemielaboradosbyCodeInFile(item.get("Insumo").toString().trim()).getId());
        renglonsemielaborados.setCantidad(Double.parseDouble(item.get("Norma").toString().trim()));
        renglonsemielaborados.setUsos(Double.parseDouble(item.get("Usos").toString().trim()));
        return renglonsemielaborados;
    }

    private Renglonjuego createdRenglonJuego(JSONObject item) {
        Renglonjuego renglonjuego = new Renglonjuego();
        renglonjuego.setRenglonvarianteId(getRenglonesbyCodeInFile(item.get("Renglon").toString().trim()).getId());
        renglonjuego.setJuegoproductoId(getJuegobyCodeInFile(item.get("Insumo").toString().trim()).getId());
        renglonjuego.setCantidad(Double.parseDouble(item.get("Norma").toString().trim()));
        renglonjuego.setUsos(Double.parseDouble(item.get("Usos").toString().trim()));
        return renglonjuego;
    }

    private Renglonrecursos createdRenglonRecurso(JSONObject item) {
        Renglonrecursos renglonrecursos = new Renglonrecursos();
        renglonrecursos.setRenglonvarianteId(getRenglonesbyCodeInFile(item.get("Renglon").toString().trim()).getId());
        renglonrecursos.setRecursosId(getRecursosbyCodeInFile(item.get("Insumo").toString().trim()).getId());
        renglonrecursos.setCantidas(Double.parseDouble(item.get("Norma").toString().trim()));
        renglonrecursos.setUsos(Double.parseDouble(item.get("Usos").toString().trim()));
        return renglonrecursos;
    }

    private void addRenglonesToCatalogo(JSONObject jsonObject, String tag) {
        List<Renglonvariante> tempRenglonesVariantes = new ArrayList<>();
        JSONArray renglonesList = (JSONArray) jsonObject.get("Renglones_x0020_Variantes");
        for (Object sum : renglonesList) {
            JSONObject item = (JSONObject) sum;
            Renglonvariante renglonvariante = createdRenglones(item, tag);
            tempRenglonesVariantes.add(renglonvariante);
        }
        utilCalcSingelton.saveRenglonesList(tempRenglonesVariantes);
    }

    private Renglonvariante createdRenglones(JSONObject item, String tag) {
        Renglonvariante rv = new Renglonvariante();
        rv.setCodigo(item.get("Codigo_x0020_RV").toString().trim());
        rv.setDescripcion(item.get("Descripcion").toString().trim());
        rv.setUm(item.get("Unidad_x0020_de_x0020_medida").toString().trim());
        rv.setCostomat(Double.parseDouble(item.get("Costo_x0020_de_x0020_materiales").toString().trim()));
        rv.setCostmano(Double.parseDouble(item.get("Costo_x0020_de_x0020_Mano_x0020_de_x0020_obra").toString().trim()));
        rv.setCostequip(Double.parseDouble(item.get("Costo_x0020_de_x0020_equipos").toString().trim()));

        rv.setHh(Double.parseDouble(item.get("Horas_x0020_hombre").toString().trim()));
        rv.setHa(Double.parseDouble(item.get("Horas_x0020_ayudante").toString().trim()));
        rv.setHe(Double.parseDouble(item.get("Horas_x0020_equipos").toString().trim()));
        rv.setHo(Double.parseDouble(item.get("Horas_x0020_operario").toString().trim()));

        rv.setPeso(Double.parseDouble(item.get("Peso").toString().trim()));
        rv.setCarga(Double.parseDouble(item.get("Carga").toString().trim()));
        rv.setCemento(Double.parseDouble(item.get("Cemento").toString().trim()));
        rv.setAsfalto(Double.parseDouble(item.get("Asfalto").toString().trim()));
        rv.setArido(Double.parseDouble(item.get("Aridos").toString().trim()));
        rv.setPrefab(Double.parseDouble(item.get("Prefabricado").toString().trim()));
        rv.setPreciomlc(0.0);
        rv.setPreciomn(0.0);
        rv.setRs(idCat);
        rv.setSalario(0.0);
        rv.setSubgrupoId(getSubgrupobyCodeInFile(item.get("Codigo_x0020_RV").toString().trim().substring(0, 4)).getId());
        return rv;
    }

    private void addCompSemiProductos(JSONObject jsonObject) {
        List<Semielaboradosrecursos> tempSemiRecursos = new ArrayList<>();
        JSONArray estructuraSemiProd = (JSONArray) jsonObject.get("Semielaborados_Insumos");
        for (Object sum : estructuraSemiProd) {
            JSONObject item = (JSONObject) sum;
            Semielaboradosrecursos semirec = createdCompSemielaborados(item);
            tempSemiRecursos.add(semirec);
        }
        utilCalcSingelton.saveSemielaboradosRecursosList(tempSemiRecursos);
    }

    private Semielaboradosrecursos createdCompSemielaborados(JSONObject item) {
        Semielaboradosrecursos semielaboradosrecursos = new Semielaboradosrecursos();
        semielaboradosrecursos.setSuministrossemielaboradosId(getSemielaboradosbyCodeInFile(item.get("Codigo").toString().trim()).getId());
        semielaboradosrecursos.setRecursosId(getRecursosbyCodeInFile(item.get("Insumo").toString().trim()).getId());
        semielaboradosrecursos.setCantidad(Double.parseDouble(item.get("Norma").toString().trim()));
        semielaboradosrecursos.setUsos(Double.parseDouble(item.get("Usos").toString().trim()));
        return semielaboradosrecursos;
    }

    private void addCompJuegoProductos(JSONObject jsonObject) {
        List<Juegorecursos> tempJuegoRecursos = new ArrayList<>();
        JSONArray estructuraJuegoProd = (JSONArray) jsonObject.get("ContenidoJuegoProductos");
        for (Object sum : estructuraJuegoProd) {
            JSONObject item = (JSONObject) sum;
            Juegorecursos juegorecursos = createdCompJuego(item);
            tempJuegoRecursos.add(juegorecursos);
        }
        utilCalcSingelton.saveJuegoRecursosList(tempJuegoRecursos);
    }

    private Juegorecursos createdCompJuego(JSONObject item) {
        Juegorecursos juegorecursos = new Juegorecursos();
        juegorecursos.setJuegoproductoId(getJuegobyCodeInFile(item.get("Codigo").toString().trim()).getId());
        juegorecursos.setRecursosId(getRecursosbyCodeInFile(item.get("Insumo").toString().trim()).getId());
        juegorecursos.setCantidad(Double.parseDouble(item.get("Norma").toString().trim()));
        return juegorecursos;
    }

    private void addSemielaboradoToCatalogo(JSONObject jsonObject, String tag) {
        List<Suministrossemielaborados> tempSemielaborados = new ArrayList<>();
        JSONArray semielaboradoslist = (JSONArray) jsonObject.get("Semielaborados");
        for (Object sum : semielaboradoslist) {
            JSONObject item = (JSONObject) sum;
            Suministrossemielaborados semielab = createdSumisemi(item, tag);
            tempSemielaborados.add(semielab);
        }
        utilCalcSingelton.saveSemielaboradosList(tempSemielaborados);
    }

    private Suministrossemielaborados createdSumisemi(JSONObject item, String tag) {
        Suministrossemielaborados suministrossemielaborados = new Suministrossemielaborados();
        suministrossemielaborados.setCodigo(item.get("Codigo").toString().trim());
        suministrossemielaborados.setDescripcion(item.get("Descripcion").toString().trim());
        suministrossemielaborados.setUm(item.get("UM").toString().trim());
        suministrossemielaborados.setPreciomn(Double.parseDouble(item.get("Precio").toString().trim()));
        suministrossemielaborados.setPeso(Double.parseDouble(item.get("Peso").toString().trim()));
        suministrossemielaborados.setMermaprod(0.0);
        suministrossemielaborados.setMermamanip(0.0);
        suministrossemielaborados.setMermatrans(0.0);
        suministrossemielaborados.setOtrasmermas(0.0);
        suministrossemielaborados.setCarga(0.0);
        suministrossemielaborados.setCemento(0.0);
        suministrossemielaborados.setArido(0.0);
        suministrossemielaborados.setAsfalto(0.0);
        suministrossemielaborados.setCostequip(0.0);
        suministrossemielaborados.setCostmano(0.0);
        suministrossemielaborados.setCostomat(0.0);
        suministrossemielaborados.setHa(0.0);
        suministrossemielaborados.setHo(0.0);
        suministrossemielaborados.setHe(0.0);
        suministrossemielaborados.setPertenec(tag);
        return suministrossemielaborados;
    }

    private void addJuegoToCatalogo(JSONObject jsonObject, String tag) {
        List<Juegoproducto> tempJuegos = new ArrayList<>();
        JSONArray juegolist = (JSONArray) jsonObject.get("JuegoProductos");
        for (Object sum : juegolist) {
            JSONObject item = (JSONObject) sum;
            Juegoproducto juego = createdJuego(item, tag);
            tempJuegos.add(juego);
        }
        utilCalcSingelton.savejuegoList(tempJuegos);
    }

    private void addInsumosToCatalogo(JSONObject jsonObject, String tag) {
        List<Recursos> tempRecursos = new ArrayList<>();
        JSONArray insumolist = (JSONArray) jsonObject.get("Insumos");
        for (Object sumini : insumolist) {
            JSONObject item = (JSONObject) sumini;
            Recursos rec = createdRecursos(item, tag);
            tempRecursos.add(rec);
        }
        utilCalcSingelton.saverecursosList(tempRecursos);
    }

    private Recursos createdRecursos(JSONObject item, String tag) {
        Recursos sumuni = new Recursos();
        sumuni.setCodigo(item.get("Codigo").toString().trim());
        sumuni.setDescripcion(item.get("Descripcion").toString().trim());
        sumuni.setUm(item.get("UM").toString().trim());
        sumuni.setPeso(Double.parseDouble(item.get("Peso").toString().trim()));
        sumuni.setPreciomn(Double.parseDouble(item.get("Precio").toString().trim()));
        sumuni.setPreciomlc(0.0);
        sumuni.setMermaprod(0.0);
        sumuni.setMermatrans(0.0);
        sumuni.setMermamanip(0.0);
        sumuni.setOtrasmermas(0.0);
        sumuni.setPertenece(tag);
        sumuni.setTipo(item.get("Tipo").toString().trim());
        sumuni.setGrupoescala(item.get("GrupoEscala").toString().trim());
        sumuni.setCostomat(0.0);
        sumuni.setCostmano(0.0);
        sumuni.setCostequip(0.0);
        sumuni.setHa(0.0);
        sumuni.setHe(0.0);
        sumuni.setHo(0.0);
        sumuni.setCemento(0.0);
        sumuni.setArido(0.0);
        sumuni.setAsfalto(0.0);
        sumuni.setCarga(0.0);
        sumuni.setPrefab(0.0);
        sumuni.setCet(0.0);
        sumuni.setCpo(0.0);
        sumuni.setCpe(0.0);
        sumuni.setOtra(0.0);
        sumuni.setActive(1);
        return sumuni;
    }

    private Juegoproducto createdJuego(JSONObject item, String tag) {
        Juegoproducto juegoproducto = new Juegoproducto();
        juegoproducto.setCodigo(item.get("Codigo").toString().trim());
        juegoproducto.setDescripcion(item.get("Descripcion").toString().trim());
        juegoproducto.setUm("jg");
        juegoproducto.setPeso(Double.parseDouble(item.get("Peso").toString().trim()));
        juegoproducto.setPreciomn(Double.parseDouble(item.get("Precio").toString().trim()));
        juegoproducto.setPreciomlc(0.0);
        juegoproducto.setMermaprod(0.0);
        juegoproducto.setMermamanip(0.0);
        juegoproducto.setMermatrans(0.0);
        juegoproducto.setOtrasmermas(0.0);
        juegoproducto.setCarga(0.0);
        juegoproducto.setCemento(0.0);
        juegoproducto.setArido(0.0);
        juegoproducto.setAsfalto(0.0);
        juegoproducto.setCostequip(0.0);
        juegoproducto.setCostmano(0.0);
        juegoproducto.setCostomat(0.0);
        juegoproducto.setHa(0.0);
        juegoproducto.setHo(0.0);
        juegoproducto.setHe(0.0);
        juegoproducto.setPertenece(tag);
        return juegoproducto;
    }

    private Recursos getRecursosbyCodeInFile(String code) {
        return utilCalcSingelton.catListResources.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).findFirst().get();
    }

    private Juegoproducto getJuegobyCodeInFile(String code) {
        return utilCalcSingelton.catListJuegos.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).findFirst().get();
    }

    private Suministrossemielaborados getSemielaboradosbyCodeInFile(String code) {
        return utilCalcSingelton.catListSemielaborados.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).findFirst().get();
    }

    private Subgrupo getSubgrupobyCodeInFile(String code) {
        return utilCalcSingelton.catSubgrupoList.parallelStream().filter(item -> item.getCodigosub().trim().equals(code.trim())).findFirst().get();
    }

    private Renglonvariante getRenglonesbyCodeInFile(String code) {
        return utilCalcSingelton.catRenglonesList.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).findFirst().get();
    }

    @FXML
    void handleClose(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void handleFileChargeAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione el archivo .json");
        File file = fileChooser.showOpenDialog(null);

        if (file != null && file.getAbsolutePath().endsWith(".json")) {
            JSONParser parser = new JSONParser();
            try {
                FileReader fileReader = new FileReader(file.getAbsolutePath());
                Object object = parser.parse(fileReader);

                JSONObject jsonObject = (JSONObject) object;
                if (chInsumos.isSelected() && !chInsumos.isDisable()) {
                    try {
                        addInsumosToCatalogo(jsonObject, tag);
                        showWaitData("Importando datos desde el fichero seleccionado!");
                        utilCalcSingelton.showAlert("Insumos Importados Satisfactoriamente!!!", 1);
                        chInsumos.setDisable(true);
                        utilCalcSingelton.getElementToCompElements("I", tag);
                        chJuegos.setSelected(true);
                        chJuegos.setDisable(false);
                    } catch (Exception e) {
                        utilCalcSingelton.showAlert("Error al importar los Insumos", 3);
                    }
                }
                if (chJuegos.isSelected() && !chJuegos.isDisable()) {
                    try {
                        addJuegoToCatalogo(jsonObject, tag);
                        showWaitData("Importando datos desde el fichero seleccionado!");
                        utilCalcSingelton.showAlert("Juegos de Productos Importados Satisfactoriamente!!!", 1);
                        chJuegos.setDisable(true);
                        utilCalcSingelton.getElementToCompElements("J", tag);
                        chSemielaborados.setSelected(true);
                        chSemielaborados.setDisable(false);
                    } catch (Exception e) {
                        utilCalcSingelton.showAlert("Error al importar los Datos", 3);
                    }
                }
                if (chSemielaborados.isSelected() && !chSemielaborados.isDisable()) {
                    try {
                        addSemielaboradoToCatalogo(jsonObject, tag);
                        showWaitData("Importando datos desde el fichero seleccionado!");
                        utilCalcSingelton.showAlert("Suministros Semielaborados Importados Satisfactoriamente!!!", 1);
                        chSemielaborados.setDisable(true);
                        utilCalcSingelton.getElementToCompElements("S", tag);
                        chCompJuegos.setSelected(true);
                        chCompJuegos.setDisable(false);
                    } catch (Exception e) {
                        utilCalcSingelton.showAlert("Error al importar los Datos", 3);
                    }
                }
                if (chCompJuegos.isSelected() && !chCompJuegos.isDisable()) {
                    try {
                        addCompJuegoProductos(jsonObject);
                        showWaitData("Importando datos desde el fichero seleccionado!");
                        utilCalcSingelton.showAlert("Suministros Composición de los Juegos de Productos Importados Satisfactoriamente!!!", 1);
                        chCompJuegos.setDisable(true);
                        chCompSemi.setSelected(true);
                        chCompSemi.setDisable(false);
                    } catch (Exception e) {
                        utilCalcSingelton.showAlert("Error al importar los Datos", 3);
                    }
                }

                if (chCompSemi.isSelected() && !chCompSemi.isDisable()) {
                    try {
                        addCompSemiProductos(jsonObject);
                        showWaitData("Importando datos desde el fichero seleccionado!");
                        utilCalcSingelton.showAlert("Suministros Composición de los Semielaborados Importados Satisfactoriamente!!!", 1);
                        chCompSemi.setDisable(true);
                        utilCalcSingelton.getElementToCompElements("G", tag);
                        chRenglones.setSelected(true);
                        chRenglones.setDisable(false);
                    } catch (Exception e) {
                        utilCalcSingelton.showAlert("Error al importar los Datos", 3);
                    }
                }

                if (chRenglones.isSelected() && !chRenglones.isDisable()) {
                    try {
                        addRenglonesToCatalogo(jsonObject, tag);
                        showWaitData("Importando datos desde el fichero seleccionado!");
                        utilCalcSingelton.showAlert("Renglones Constructivos Importados Satisfactoriamente!!!", 1);
                        chRenglones.setDisable(true);
                        utilCalcSingelton.getElementToCompElements("R", idCat.toString());
                        chCompRenglones.setSelected(true);
                        chCompRenglones.setDisable(false);
                    } catch (Exception e) {
                        utilCalcSingelton.showAlert("Error al importar los Datos", 3);
                    }
                }

                if (chCompRenglones.isSelected() && !chCompRenglones.isDisable()) {
                    try {
                        addCompoRenglonesToCatalogo(jsonObject);
                        showWaitData("Importando datos desde el fichero seleccionado!");
                        utilCalcSingelton.showAlert("Composición de los Renglones Constructivos Importados Satisfactoriamente!!!", 1);
                        chCompRenglones.setDisable(true);

                        showStateMessage("Seleccione la ubicación del fichero de comprobación");
                        utilCalcSingelton.showRVEstructure(idCat);
                    } catch (Exception e) {
                        utilCalcSingelton.showAlert("Error al importar los Datos", 3);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Información");
            alert.setContentText("Fichero incorrecto, cargue un fichero .json");
            alert.showAndWait();
        }

    }

    private void showStateMessage(String message) {
        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText(message);
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();
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

    private void showWaitData(String message) {
        tarea = createTime(100);
        stage = new ProgressDialog(tarea);
        stage.setContentText(message);
        stage.setTitle("Espere...");
        new Thread(tarea).start();
        stage.showAndWait();
    }

    public Task createTime(Integer val) {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                for (int i = 0; i < val; i++) {
                    Thread.sleep(val / 2);
                    updateProgress(i + 1, val);
                }
                return true;
            }
        };
    }


}