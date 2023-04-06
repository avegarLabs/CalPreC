package views;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class DesgloseRVController implements Initializable {

    private static SessionFactory sf;
    @FXML
    private Label label_title;
    @FXML
    private Label labelUM;
    @FXML
    private Label labelCantidad;
    @FXML
    private TextArea labelRenglon;
    @FXML
    private Label totalRV;
    private double totalCostRV;
    private int idRV;
    @FXML
    private TableView<RenglonVarianteUOView> tablaRenglones;
    @FXML
    private TableColumn<RenglonVarianteUOView, StringProperty> codeRV;
    @FXML
    private TableColumn<RenglonVarianteUOView, StringProperty> descripRV;
    @FXML
    private TableColumn<RenglonVarianteUOView, StringProperty> umRV;
    @FXML
    private TableColumn<RenglonVarianteUOView, DoubleProperty> cantRV;
    @FXML
    private TableColumn<RenglonVarianteUOView, DoubleProperty> mateRV;
    @FXML
    private TableColumn<RenglonVarianteUOView, DoubleProperty> manoRV;
    @FXML
    private TableColumn<RenglonVarianteUOView, DoubleProperty> equipRV;
    @FXML
    private TableColumn<RenglonVarianteUOView, DoubleProperty> costRV;
    @FXML
    private TableView<UOMaterialesView> tablaSuministros;
    @FXML
    private TableColumn<UOMaterialesView, StringProperty> codeSum;
    @FXML
    private TableColumn<UOMaterialesView, StringProperty> descripSum;
    @FXML
    private TableColumn<UOMaterialesView, StringProperty> umSum;
    @FXML
    private TableColumn<UOMaterialesView, DoubleProperty> cantSum;
    @FXML
    private TableColumn<UOMaterialesView, DoubleProperty> precioSum;
    @FXML
    private TableColumn<UOMaterialesView, DoubleProperty> costoSUM;
    @FXML
    private TableView<UOManoObraView> tablaManoObra;
    @FXML
    private TableColumn<UOManoObraView, StringProperty> codeMano;
    @FXML
    private TableColumn<UOManoObraView, StringProperty> descripMano;
    @FXML
    private TableColumn<UOManoObraView, StringProperty> umMano;
    @FXML
    private TableColumn<UOManoObraView, StringProperty> grupoMano;
    @FXML
    private TableColumn<UOManoObraView, DoubleProperty> cantMano;
    @FXML
    private TableColumn<UOManoObraView, DoubleProperty> precioMano;
    @FXML
    private TableColumn<UOManoObraView, DoubleProperty> costMano;
    @FXML
    private TableView<UOEquiposView> tablaEquipos;
    @FXML
    private TableColumn<UOEquiposView, StringProperty> codeEquip;
    @FXML
    private TableColumn<UOEquiposView, StringProperty> descripEquip;
    @FXML
    private TableColumn<UOEquiposView, StringProperty> umEquip;
    @FXML
    private TableColumn<UOEquiposView, DoubleProperty> cantEquip;
    @FXML
    private TableColumn<UOEquiposView, DoubleProperty> precioEquip;
    @FXML
    private TableColumn<UOEquiposView, DoubleProperty> costoEquip;
    private Nivelespecifico unidadobra;
    private Renglonvariante renglonbaseuo;

    private ArrayList<Renglonnivelespecifico> unidadobrarenglonArrayList;
    private ObservableList<RenglonVarianteUOView> renglonVarianteUOViewObservableList;
    private RenglonVarianteUOView renglonVarianteUOView;
    private double total;

    private ArrayList<Bajoespecificacionrv> bajoespecificacionArrayList;
    private ObservableList<UOMaterialesView> uoMaterialesViewObservableList;
    private UOMaterialesView uoMaterialesView;

    private ObservableList<UOManoObraView> uoManoObraViewObservableList;
    private ObservableList<UOManoObraView> tempManoObraRenglon;
    private UOManoObraView uoManoObraView;


    private ObservableList<UOEquiposView> uoEquiposViewObservableList;
    private ObservableList<UOEquiposView> tempEquiposRenglon;
    private UOEquiposView uoEquiposView;


    private Nivelespecifico getUnidadObra(int idUO) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadobra = session.get(Nivelespecifico.class, idUO);


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return unidadobra;
    }

    private Renglonvariante getRenglonBase(String renglonbase) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("SELECT rv.id, rv.codigo, rv.descripcion FROM Renglonvariante rv INNER JOIN Unidadobrarenglon uor ON rv.id = uor.renglonvarianteId WHERE rv.codigo =: codeR");
            query.setParameter("codeR", renglonbase);

            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();

                idRV = Integer.parseInt(row[0].toString());
            }

            renglonbaseuo = session.get(Renglonvariante.class, idRV);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return renglonbaseuo;
    }


    private ObservableList<RenglonVarianteUOView> getRenglones(int idUO) {

        renglonVarianteUOViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Renglonnivelespecifico WHERE nivelespecificoId =: idUnidad");
            query.setParameter("idUnidad", idUO);

            totalCostRV = 0.0;
            unidadobrarenglonArrayList = (ArrayList<Renglonnivelespecifico>) ((org.hibernate.query.Query) query).list();
            unidadobrarenglonArrayList.forEach(unidadobrarenglon -> {
                total = unidadobrarenglon.getCostmaterial() + unidadobrarenglon.getCostmano() + unidadobrarenglon.getCostequipo();
                totalCostRV += total;

                renglonVarianteUOView = new RenglonVarianteUOView(unidadobrarenglon.getRenglonvarianteId(), unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getCodigo(), unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getDescripcion(), unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getUm(), Math.round(unidadobrarenglon.getCantidad() * 100d) / 100d, Math.round(unidadobrarenglon.getCostmaterial()) * 100d / 100d, Math.round(unidadobrarenglon.getCostmano() * 100d) / 100d, Math.round(unidadobrarenglon.getCostequipo() * 100d) / 100d, Math.round(total * 100d) / 100d);
                renglonVarianteUOViewObservableList.add(renglonVarianteUOView);
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return renglonVarianteUOViewObservableList;
    }


    private ObservableList<UOMaterialesView> getMateriales(int idUO) {

        uoMaterialesViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            /*
             *Recursos del tipo 1
             */
            Query query = session.createQuery("SELECT bajo.id, rc.codigo, rc.descripcion, rc.um, bajo.cantidad, rc.preciomn, bajo.costo FROM Bajoespecificacionrv bajo INNER JOIN Recursos rc ON bajo.idsuministro = rc.id WHERE bajo.nivelespecificoId =: idUnidad AND bajo.tipo = '1'");
            query.setParameter("idUnidad", idUO);


            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                String idba = row[0].toString();
                String code = row[1].toString();
                String descrip = row[2].toString();
                String um = row[3].toString();
                String cant = row[4].toString();
                String precio = row[5].toString();
                String cost = row[6].toString();

                uoMaterialesView = new UOMaterialesView(Integer.parseInt(idba), code, descrip, um, Double.parseDouble(cant), Math.round(Double.parseDouble(precio) * 100d) / 100d, Math.round(Double.parseDouble(cost) * 100d) / 100d);
                uoMaterialesViewObservableList.add(uoMaterialesView);
            }

            /*
             * Juego de productos
             */
            Query query1 = session.createQuery("SELECT bajo.id, rc.codigo, rc.descripcion, rc.um, bajo.cantidad, rc.preciomn, bajo.costo FROM Bajoespecificacionrv bajo INNER JOIN Juegoproducto rc ON bajo.idsuministro = rc.id WHERE bajo.nivelespecificoId =: idUnidad AND bajo.tipo = 'J'");
            query1.setParameter("idUnidad", idUO);


            for (Iterator it = ((org.hibernate.query.Query) query1).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                String idba = row[0].toString();
                String code = row[1].toString();
                String descrip = row[2].toString();
                String um = row[3].toString();
                String cant = row[4].toString();
                String precio = row[5].toString();
                String cost = row[6].toString();

                uoMaterialesView = new UOMaterialesView(Integer.parseInt(idba), code, descrip, um, Double.parseDouble(cant), Math.round(Double.parseDouble(precio) * 100d) / 100d, Math.round(Double.parseDouble(cost) * 100d) / 100d);
                uoMaterialesViewObservableList.add(uoMaterialesView);
            }

            /*
             * Suministros semielaborados
             */

            Query query2 = session.createQuery("SELECT bajo.id, rc.codigo, rc.descripcion, rc.um, bajo.cantidad, rc.preciomn, bajo.costo FROM Bajoespecificacionrv bajo INNER JOIN Suministrossemielaborados rc ON bajo.idsuministro = rc.id WHERE bajo.nivelespecificoId =: idUnidad AND bajo.tipo = 'S'");
            query2.setParameter("idUnidad", idUO);


            for (Iterator it = ((org.hibernate.query.Query) query2).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                String idba = row[0].toString();
                String code = row[1].toString();
                String descrip = row[2].toString();
                String um = row[3].toString();
                String cant = row[4].toString();
                String precio = row[5].toString();
                String cost = row[6].toString();

                uoMaterialesView = new UOMaterialesView(Integer.parseInt(idba), code, descrip, um, Double.parseDouble(cant), Math.round(Double.parseDouble(precio) * 100d) / 100d, Math.round(Double.parseDouble(cost) * 100d) / 100d);
                uoMaterialesViewObservableList.add(uoMaterialesView);
            }

            /*

            bajoespecificacionArrayList = (ArrayList<Bajoespecificacion>) ((org.hibernate.query.Query) query).list();
            bajoespecificacionArrayList.forEach(bajoespecificacion -> {
                if (bajoespecificacion.getTipo().contentEquals("1")) {
                    Recursos recursos = (Recursos) session.get(Recursos.class, bajoespecificacion.getIdSuministro());
                    uoMaterialesView = new UOMaterialesView(bajoespecificacion.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), bajoespecificacion.getCantidad(), recursos.getPreciomn(), bajoespecificacion.getCosto());
                    uoMaterialesViewObservableList.add(uoMaterialesView);
                }
                if (bajoespecificacion.getTipo().contentEquals("J")) {
                    Juegoproducto recursos = (Juegoproducto) session.get(Juegoproducto.class, bajoespecificacion.getIdSuministro());
                    uoMaterialesView = new UOMaterialesView(bajoespecificacion.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), bajoespecificacion.getCantidad(), recursos.getPreciomn(), bajoespecificacion.getCosto());
                    uoMaterialesViewObservableList.add(uoMaterialesView);
                }
                if (bajoespecificacion.getTipo().contentEquals("S")) {
                    Suministrossemielaborados recursos = (Suministrossemielaborados) session.get(Suministrossemielaborados.class, bajoespecificacion.getIdSuministro());
                    uoMaterialesView = new UOMaterialesView(bajoespecificacion.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), bajoespecificacion.getCantidad(), recursos.getPreciomn(), bajoespecificacion.getCosto());
                    uoMaterialesViewObservableList.add(uoMaterialesView);
                }
            });

*/
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return uoMaterialesViewObservableList;
    }

    private ObservableList<UOManoObraView> getManoObraUO(int id) {
        uoManoObraViewObservableList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            /**
             * FROM
             *   public.renglonvariante
             *   INNER JOIN public.unidadobrarenglon ON (public.renglonvariante.id = public.unidadobrarenglon.renglonvariante__id)
             *   INNER JOIN public.renglonrecursos ON (public.renglonvariante.id = public.renglonrecursos.renglonvariante__id)
             *   INNER JOIN public.recursos ON (public.renglonrecursos.recursos__id = public.recursos.id)
             * WHERE
             *   unidadobrarenglon.unidadobra__id = 25 AND
             *   recursos.tipo = '2'
             *                    uoManoObraView = new UOManoObraView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), recursos.getGrupoescala(), renglonrecursos.getCantidas(), recursos.getPreciomn(), Math.round(total * 100d) / 100d);
             */
            Query query = session.createQuery("SELECT rc.id, rc.codigo, rc.descripcion, rc.um, rc.grupoescala, rc.preciomn, SUM(rvr.cantidas) FROM Renglonvariante  rv INNER JOIN Renglonnivelespecifico uor ON rv.id = uor.renglonvarianteId INNER JOIN Renglonrecursos rvr ON rv.id = rvr.renglonvarianteId INNER JOIN Recursos rc ON rvr.recursosId = rc.id WHERE uor.nivelespecificoId =: idUO AND rc.tipo = '2' GROUP BY rc.id, rc.codigo, rc.descripcion, rc.um, rc.grupoescala, rc.preciomn");
            query.setParameter("idUO", id);

            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                String idman = row[0].toString();
                String code = row[1].toString();
                String descrip = row[2].toString();
                String um = row[3].toString();
                String grup = row[4].toString();
                String precio = row[5].toString();
                String cant = row[6].toString();

                total = Double.parseDouble(cant) * Double.parseDouble(precio);

                uoManoObraView = new UOManoObraView(Integer.parseInt(idman), code, descrip, um, grup, Double.parseDouble(cant), Double.parseDouble(precio), Math.round(total * 100d) / 100d);
                uoManoObraViewObservableList.add(uoManoObraView);
            }
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return uoManoObraViewObservableList;

    }

    /*
        private ObservableList<UOManoObraView> getManoObraRV(int id) {
            tempManoObraRenglon = FXCollections.observableArrayList();

            Session session = ConnectionModel.createAppConnection().openSession();
            SessionSys session = sf.openSession();
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Query query = session.createQuery("FROM Renglonrecursos WHERE renglonvarianteId =: idR");
                query.setParameter("idR", id);

                total = 0.0;
                ArrayList<Renglonrecursos> renglonrecursosArrayList = (ArrayList<Renglonrecursos>) ((org.hibernate.query.Query) query).list();
                renglonrecursosArrayList.forEach(renglonrecursos -> {

                    Recursos recursos = (Recursos) session.get(Recursos.class, renglonrecursos.getRecursosId());
                    if (recursos.getTipo().contentEquals("2")) {
                        total = renglonrecursos.getCantidas() * recursos.getPreciomn();
                        uoManoObraView = new UOManoObraView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), recursos.getGrupoescala(), renglonrecursos.getCantidas(), recursos.getPreciomn(), Math.round(total * 100d) / 100d);
                        tempManoObraRenglon.add(uoManoObraView);
                    }
                });


                tx.commit();
            } catch (HibernateException he) {
                if (tx != null) tx.rollback();
                he.printStackTrace();
            } finally {
                session.close();
            }

            return tempManoObraRenglon;

        }
    */
    private ObservableList<UOEquiposView> getEquiposUO(int id) {

        uoEquiposViewObservableList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("SELECT rc.id, rc.codigo, rc.descripcion, rc.um,  SUM(rvr.cantidas), rc.preciomn FROM Renglonvariante  rv INNER JOIN Renglonnivelespecifico uor ON rv.id = uor.renglonvarianteId INNER JOIN Renglonrecursos rvr ON rv.id = rvr.renglonvarianteId INNER JOIN Recursos rc ON rvr.recursosId = rc.id WHERE uor.nivelespecificoId =: idUO AND rc.tipo = '3' GROUP BY rc.id, rc.codigo, rc.descripcion, rc.um, rc.grupoescala, rc.preciomn");
            query.setParameter("idUO", id);

            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                String idjg = row[0].toString();
                String code = row[1].toString();
                String descrip = row[2].toString();
                String um = row[3].toString();
                String cant = row[4].toString();
                String precio = row[5].toString();

                total = Double.parseDouble(cant) * Double.parseDouble(precio);
                uoEquiposView = new UOEquiposView(Integer.parseInt(idjg), code, descrip, um, Math.round(Double.parseDouble(cant) * 100d) / 100d, Math.round(Double.parseDouble(precio) * 100d / 100d), Math.round(total * 100d) / 100d);


                uoEquiposViewObservableList.add(uoEquiposView);
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return uoEquiposViewObservableList;
    }

    private ObservableList<UOEquiposView> getEquiposInRV(int id) {
        tempEquiposRenglon = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            /*
            Query query = session.createQuery("SELECT rc.id, rc.codigo, rc.descripcion, rc.um, rvc.cantidas, rc.preciomn FROM Renglonrecursos rvc INNER JOIN Recursos rc ON rvc.recursosId = rc.id WHERE rvc.renglonvarianteId =: idv AND rc.tipo like '3'");
            query.setParameter("idv", id);

            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                String idjg = row[0].toString();
                String code = row[1].toString();
                String descrip = row[2].toString();
                String um = row[3].toString();
                String cant = row[4].toString();
                String precio = row[5].toString();

                total = Double.parseDouble(cant) * Double.parseDouble(precio);

                uoEquiposView = new UOEquiposView(Integer.parseInt(idjg), code, descrip, um, Double.parseDouble(cant), Math.round(Double.parseDouble(precio) * 100d / 100d), Math.round(total * 100d) / 100d);
                System.out.println("Equipo " + code);

                tempEquiposRenglon.add(uoEquiposView);

            }
*/

            /*
            recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), renglonrecursos.getCantidas(), recursos.getPreciomn(), Math.round(total * 100d) / 100d)

            query.setParameter("idR", id);

            total = 0.0;
            ArrayList<Renglonrecursos> renglonrecursosArrayList = (ArrayList<Renglonrecursos>) ((org.hibernate.query.Query) query).list();
            renglonrecursosArrayList.forEach(renglonrecursos -> {

                Recursos recursos = (Recursos) session.get(Recursos.class, renglonrecursos.getRecursosId());
                if (recursos.getTipo().contentEquals("3")) {
                    total = renglonrecursos.getCantidas() * recursos.getPreciomn();
                    uoEquiposView = new UOEquiposView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), renglonrecursos.getCantidas(), recursos.getPreciomn(), Math.round(total * 100d) / 100d);
                    tempEquiposRenglon.add(uoEquiposView);
                }
            });

*/
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return tempEquiposRenglon;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void datosRenglonesVariantes() {
        codeRV.setCellValueFactory(new PropertyValueFactory<RenglonVarianteUOView, StringProperty>("codigo"));
        descripRV.setCellValueFactory(new PropertyValueFactory<RenglonVarianteUOView, StringProperty>("descripcion"));
        umRV.setCellValueFactory(new PropertyValueFactory<RenglonVarianteUOView, StringProperty>("um"));
        cantRV.setCellValueFactory(new PropertyValueFactory<RenglonVarianteUOView, DoubleProperty>("cantidad"));
        mateRV.setCellValueFactory(new PropertyValueFactory<RenglonVarianteUOView, DoubleProperty>("costomat"));
        manoRV.setCellValueFactory(new PropertyValueFactory<RenglonVarianteUOView, DoubleProperty>("costmano"));
        equipRV.setCellValueFactory(new PropertyValueFactory<RenglonVarianteUOView, DoubleProperty>("costequip"));
        costRV.setCellValueFactory(new PropertyValueFactory<RenglonVarianteUOView, DoubleProperty>("total"));

    }

    private void datosMateriales() {

        codeSum.setCellValueFactory(new PropertyValueFactory<UOMaterialesView, StringProperty>("codigo"));
        descripSum.setCellValueFactory(new PropertyValueFactory<UOMaterialesView, StringProperty>("descripcion"));
        umSum.setCellValueFactory(new PropertyValueFactory<UOMaterialesView, StringProperty>("um"));
        cantSum.setCellValueFactory(new PropertyValueFactory<UOMaterialesView, DoubleProperty>("cantidad"));
        precioSum.setCellValueFactory(new PropertyValueFactory<UOMaterialesView, DoubleProperty>("precio"));
        costoSUM.setCellValueFactory(new PropertyValueFactory<UOMaterialesView, DoubleProperty>("total"));
    }

    private void datosManodeObra() {

        codeMano.setCellValueFactory(new PropertyValueFactory<UOManoObraView, StringProperty>("codigo"));
        descripMano.setCellValueFactory(new PropertyValueFactory<UOManoObraView, StringProperty>("descripcion"));
        umMano.setCellValueFactory(new PropertyValueFactory<UOManoObraView, StringProperty>("um"));
        grupoMano.setCellValueFactory(new PropertyValueFactory<UOManoObraView, StringProperty>("grupo"));
        cantMano.setCellValueFactory(new PropertyValueFactory<UOManoObraView, DoubleProperty>("cantidad"));
        precioMano.setCellValueFactory(new PropertyValueFactory<UOManoObraView, DoubleProperty>("precio"));
        costMano.setCellValueFactory(new PropertyValueFactory<UOManoObraView, DoubleProperty>("total"));


    }

    private void datosEquipo() {

        codeEquip.setCellValueFactory(new PropertyValueFactory<UOEquiposView, StringProperty>("codigo"));
        descripEquip.setCellValueFactory(new PropertyValueFactory<UOEquiposView, StringProperty>("descripcion"));
        umEquip.setCellValueFactory(new PropertyValueFactory<UOEquiposView, StringProperty>("um"));
        cantEquip.setCellValueFactory(new PropertyValueFactory<UOEquiposView, DoubleProperty>("cantidad"));
        precioEquip.setCellValueFactory(new PropertyValueFactory<UOEquiposView, DoubleProperty>("precio"));
        costoEquip.setCellValueFactory(new PropertyValueFactory<UOEquiposView, DoubleProperty>("total"));
    }


    public void pasarDatos(int idUO) {

        unidadobra = getUnidadObra(idUO);
        // labelUM.setText( " " );
        // labelCantidad.setText(String.valueOf(unidadobra.getCantidad()));

        // renglonbaseuo = getRenglonBase(unidadobra.getRenglonbase());
        // labelRenglon.setText(renglonbaseuo.getCodigo() + " / " + renglonbaseuo.getDescripcion());

        label_title.setText("EstructuraPCW: " + unidadobra.getCodigo() + " - " + unidadobra.getDescripcion());


        datosRenglonesVariantes();
        renglonVarianteUOViewObservableList = FXCollections.observableArrayList();
        renglonVarianteUOViewObservableList = getRenglones(idUO);
        tablaRenglones.getItems().setAll(renglonVarianteUOViewObservableList);
        //totalRV.setText(String.valueOf(Math.round(totalCostRV * 100d) / 10d));


        datosMateriales();
        uoMaterialesViewObservableList = FXCollections.observableArrayList();
        uoMaterialesViewObservableList = getMateriales(idUO);
        tablaSuministros.getItems().setAll(uoMaterialesViewObservableList);

        datosManodeObra();
        uoManoObraViewObservableList = FXCollections.observableArrayList();
        uoManoObraViewObservableList = getManoObraUO(unidadobra.getId());
        tablaManoObra.getItems().setAll(uoManoObraViewObservableList);

        datosEquipo();
        uoEquiposViewObservableList = FXCollections.observableArrayList();
        uoEquiposViewObservableList = getEquiposUO(unidadobra.getId());
        tablaEquipos.getItems().setAll(uoEquiposViewObservableList);


    }


}
