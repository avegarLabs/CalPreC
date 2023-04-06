package views;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class NuevoComponenteController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     @FXML private JFXTextField field_codigo;


     @FXML private Label labelDecrip;

     @FXML private JFXTextArea labelDescrip;

     @FXML private JFXTextField fieldUsos;

     private static SessionFactory sf;

     private ObservableList<ComponentesSuministrosCompuestosView> observableList;
     private ObservableList<ComponentesSuministrosCompuestosView> datos;
     //cambiarlo despues a los componetes propios
     private ArrayList<Manodeobra> manoArrayList;
     private ArrayList<Suministros> suministrosArrayList;
     private ComponentesSuministrosCompuestosView compuestosView;

     @Override public void initialize(URL location, ResourceBundle resources) {

     }

     /**
      * Metodo para llenar la lista del autocomplete con los suministros y las mano de obra

     public ObservableList<ComponentesSuministrosCompuestosView> getInsumos() {


     observableList = FXCollections.observableArrayList();

     Session session = ConnectionModel.createAppConnection().openSession();
     DecimalFormat df = new DecimalFormat("0.00");
     SessionSys session = sf.openSession();
     Transaction tx = null;
     try {
     tx = session.beginTransaction();
     suministrosArrayList = (ArrayList<Suministros>) session.createQuery("FROM Suministros").list();
     suministrosArrayList.forEach(sum -> {
     compuestosView = new ComponentesSuministrosCompuestosView(sum.getCodigo(), sum.getDescripcion(), sum.getUm(), String.valueOf(df.format(sum.getPreciomn())), String.valueOf(df.format(sum.getPreciomlc())), "0", "0");
     observableList.add(compuestosView);


     });
     manoArrayList = (ArrayList<Manoobra>) session.createQuery("FROM Manoobra ").list();
     manoArrayList.forEach(manoobra -> {

     compuestosView = new ComponentesSuministrosCompuestosView(manoobra.getCodigo(), manoobra.getDescripcion(), manoobra.getUm(), String.valueOf(df.format(manoobra.getPreciomn())), String.valueOf(df.format(manoobra.getPreciomlc())), "0", "0");
     observableList.add(compuestosView);


     });

     tx.commit();
     } catch (HibernateException he) {
     if (tx != null) tx.rollback();
     he.printStackTrace();
     } finally {
     session.close();
     }
     return observableList;
     }


     public void handleLlenaLabel(ActionEvent event) {

     datos.forEach(insumo -> {
     if (insumo.getCodigo().contentEquals(field_codigo.getText())) {
     labelDecrip.setText(insumo.getDescripcion());
     }
     });

     }

     @Override public void initialize(URL location, ResourceBundle resources) {
     datos = FXCollections.observableArrayList();
     datos = getInsumos();
     }
     /*
     //bescar como pasar datos de un controlador a otro sin llamar la vista
     public void handleNewInsumoComponet(ActionEvent event) {
     try {
     datos.forEach(dat -> {
     if (dat.getCodigo().contentEquals(field_codigo.getText())) {
     compuestosView = dat;
     dat.setUso(fieldUsos.getText());
     dat.setCantidad(fieldCantidad.getText());
     }
     });


     } catch (Exception ex) {
     ex.printStackTrace();
     }
     }

     public void setNewComponet (ComponentesSuministrosCompuestosView suministroscompuestos){
     this.compuestosView = suministroscompuestos;
     }
     */
}
