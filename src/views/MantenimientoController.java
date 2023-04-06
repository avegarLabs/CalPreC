package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MantenimientoController implements Initializable {

    List<Recursos> duplicatesList;
    @FXML
    private JFXButton btnAction;
    @FXML
    private JFXCheckBox checDuplicates;
    private List<Recursos> recursosList;
    private List<Juegorecursos> juegorecursosList;
    private List<Semielaboradosrecursos> sumiSemielaboradosrecursosList;
    private int count;
    private int batchSize = 150;
    @FXML
    private ProgressIndicator indicator;
    private List<Bajoespecificacion> fullListBajoEspecificacion;
    private Map<String, List<Recursos>> codeBajoList;
    private Map<String, List<Recursos>> codeToChangeList;

    private List<Recursos> getFindAllRecursos() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            recursosList = session.createQuery("FROM Recursos ").getResultList();
            tx.commit();
            session.close();
            return recursosList;

        } catch (NoResultException re) {
        } finally {
            session.close();
        }
        return Collections.emptyList();
    }

    private List<Juegorecursos> getFindAllJuegoRecursos() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            juegorecursosList = session.createQuery("FROM Juegorecursos ").getResultList();
            tx.commit();
            session.close();
            return juegorecursosList;

        } catch (NoResultException re) {
        } finally {
            session.close();
        }
        return Collections.emptyList();
    }

    private List<Semielaboradosrecursos> getFindAllSemielaboradosRecursos() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            sumiSemielaboradosrecursosList = session.createQuery("FROM Semielaboradosrecursos ").getResultList();
            tx.commit();
            session.close();
            return sumiSemielaboradosrecursosList;

        } catch (NoResultException re) {
        } finally {
            session.close();
        }
        return Collections.emptyList();
    }

    private List<Bajoespecificacion> getListRecordsByCodeRecursos(String code) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Tuple> tupleList = session.createQuery(" FROM Bajoespecificacion bajo INNER JOIN Recursos rec ON bajo.idSuministro = rec.id WHERE rec.codigo =: coder ", Tuple.class).setParameter("coder", code).getResultList();
            tx.commit();
            session.close();
            List<Bajoespecificacion> bajoespecificacionList = new ArrayList<>();
            for (Tuple tuple : tupleList) {
                bajoespecificacionList.add((Bajoespecificacion) tuple.get(0));
            }
            return bajoespecificacionList;

        } catch (NoResultException re) {
        } finally {
            session.close();
        }
        return Collections.emptyList();
    }

    private List<Bajoespecificacion> findAllBajoespecificacionList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            fullListBajoEspecificacion = session.createQuery(" FROM Bajoespecificacion ").getResultList();
            tx.commit();
            session.close();
            return fullListBajoEspecificacion;
        } catch (NoResultException re) {
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private List<Bajoespecificacion> getListRecordsByIdRecursos(int idRecursos) {
        return fullListBajoEspecificacion.parallelStream().filter(item -> item.getIdSuministro() == idRecursos).collect(Collectors.toList());
    }

    public void deleteSuministro(List<Recursos> recursosArrayList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (Recursos recursos : recursosArrayList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.delete(recursos);
            }
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al eliminar los suminitros");
            alert.setHeaderText(he.getMessage());
            alert.showAndWait();
        } finally {
            session.close();
        }
    }

    public void updateSuministroBajos(List<Bajoespecificacion> recursosArrayList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (Bajoespecificacion recursos : recursosArrayList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.update(recursos);
            }
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al eliminar los suminitros");
            alert.setHeaderText(he.getMessage());
            alert.showAndWait();
        } finally {
            session.close();
        }
    }

    private List<Recursos> recursosListSameCode(Recursos recursos) {
        return recursosList.parallelStream().filter(item -> item.getCodigo().trim().equals(recursos.getCodigo().trim())).collect(Collectors.toList());
    }

    private List<Juegorecursos> recursosJurgoRecSameId(Recursos recursos) {
        return juegorecursosList.parallelStream().filter(item -> item.getRecursosId() == recursos.getId()).collect(Collectors.toList());
    }

    private List<Semielaboradosrecursos> recursosSemiRecSameId(Recursos recursos) {
        return sumiSemielaboradosrecursosList.parallelStream().filter(item -> item.getRecursosId() == recursos.getId()).collect(Collectors.toList());
    }

    private void getDuplicatesCodes(List<Recursos> fullList) {
        duplicatesList = new ArrayList<>();
        duplicatesList = fullList.parallelStream().filter(item -> Collections.frequency(fullList, item) > 1 && !item.getPertenece().trim().equals("I") && !item.getPertenece().trim().equals("R266")).collect(Collectors.toList());
        if (duplicatesList.size() > 0) {
            Map<String, List<Recursos>> codeListToChange = new HashMap<>();
            codeListToChange = getAllSuministrosBajoEspecificacionToChange(duplicatesList);
            if (codeListToChange.size() > 0) {
                List<Bajoespecificacion> bajoespecificacionList = new ArrayList<>();
                for (Map.Entry<String, List<Recursos>> entry : codeListToChange.entrySet()) {
                    System.out.println("Analizando rec1: " + entry.getKey());
                    Recursos rec = entry.getValue().get(0);
                    List<Bajoespecificacion> datosList = getListRecordsByCodeRecursos(entry.getKey().trim());
                    if (datosList.size() > 0) {
                        bajoespecificacionList.addAll(updateRecBajoList(rec, datosList));
                    }
                }
                updateSuministroBajos(bajoespecificacionList);
            }
            checDuplicates.setSelected(true);
        }
    }


    private List<Bajoespecificacion> updateRecBajoList(Recursos rec, List<Bajoespecificacion> datosList) {
        List<Bajoespecificacion> updateList = new ArrayList<>();
        for (Bajoespecificacion bajoespecificacion : datosList) {
            bajoespecificacion.setIdSuministro(rec.getId());
            updateList.add(bajoespecificacion);
        }
        return updateList;
    }

    private List<Recursos> analisisListDuplicates(List<Recursos> recursosListOK) {
        List<Recursos> recursosListDeleteFirst = new ArrayList<>();
        List<Recursos> recursosListParaCambiar = new ArrayList<>();
        for (Recursos recursos : recursosListOK) {
            if (getListRecordsByIdRecursos(recursos.getId()).size() == 0 && recursosJurgoRecSameId(recursos).size() == 0 && recursosSemiRecSameId(recursos).size() == 0) {
                recursosListDeleteFirst.add(recursos);
            } else {
                recursosListParaCambiar.add(recursos);
            }
        }
        deleteSuministro(recursosListDeleteFirst);

        return recursosListParaCambiar;

    }

    private Map<String, List<Recursos>> getAllSuministrosBajoEspecificacionToChange(List<Recursos> listDupli) {
        codeBajoList = new HashMap<>();
        List<Recursos> tempList = new ArrayList<>();
        for (Recursos recursos : listDupli) {
            codeBajoList.put(recursos.getCodigo(), recursosListSameCode(recursos));
        }
        codeToChangeList = new HashMap<>();
        for (Map.Entry<String, List<Recursos>> entry : codeBajoList.entrySet()) {
            tempList = analisisListDuplicates(entry.getValue());
            if (tempList.size() > 0) {
                codeToChangeList.put(entry.getKey(), tempList);
            }
        }
        return codeToChangeList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void DBMantenimiento() {
        List<Recursos> list = getFindAllRecursos();
        List<Juegorecursos> juegorecursos = getFindAllJuegoRecursos();
        List<Semielaboradosrecursos> semielaboradosrecursosList = getFindAllSemielaboradosRecursos();
        List<Bajoespecificacion> bajoespecificacions = findAllBajoespecificacionList();
        getDuplicatesCodes(list);

    }


}

