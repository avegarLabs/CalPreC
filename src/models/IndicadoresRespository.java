package models;

import Reports.CalForReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class IndicadoresRespository {

    private static List<String> list = new ArrayList<>(Arrays.asList("II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI"));
    public List<Obra> obrasList;
    public List<IndicadorGrup> indicadorGrupList;
    public ObservableList<TableIndicadorNorma> ObservableListList;
    public List<NormasInd> normasIndList;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    public List<Obra> getObrasList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            obrasList = session.createQuery("FROM Obra WHERE tipo = 'UO'").getResultList();
            tx.commit();
            session.close();
            return obrasList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return Collections.emptyList();
    }

    public List<IndicadorGrup> getIndicadoresGroupList(int idPert) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            indicadorGrupList = session.createQuery("FROM IndicadorGrup WHERE pertenece =: idPe ").setParameter("idPe", idPert).getResultList();
            tx.commit();
            session.close();
            return indicadorGrupList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return Collections.emptyList();
    }

    public ObservableList<TableIndicadorNorma> getNormasGroupList(int idInd) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ObservableListList = FXCollections.observableArrayList();
            normasIndList = session.createQuery("FROM NormasInd WHERE indcadorId =: idI").setParameter("idI", idInd).getResultList();
            for (NormasInd normasInd : normasIndList) {
                Renglonvariante r = utilCalcSingelton.renglonvarianteList.parallelStream().filter(item -> item.getCodigo().trim().equals(normasInd.getCodenorna()) && item.getRs() == normasInd.getIndicadorGrupbyIndicador().getPertenece()).findFirst().get();
                ObservableListList.add(new TableIndicadorNorma(r.getCodigo(), r.getDescripcion(), r.getUm()));
            }
            tx.commit();
            session.close();
            return ObservableListList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private Renglonvariante finfRenglonVariantebyId(int idReng) {
        return utilCalcSingelton.renglonvarianteList.parallelStream().filter(item -> item.getId() == idReng).findFirst().get();

    }

    public List<Tuple> getDataTocreateModel(String query) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Tuple> datos = session.createQuery(query, Tuple.class).getResultList();
            tx.commit();
            session.close();
            return datos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private List<RenglonesIndModel> listRebglonesByGroup(IndicadorGrup indicadorGrup, List<Tuple> listOfRenfloges) {
        List<RenglonesIndModel> tempList = new ArrayList<>();
        for (NormasInd normasInd : indicadorGrup.getNormasById()) {
            for (Tuple listOfRenfloge : listOfRenfloges) {
                Renglonvariante renglonvariante = finfRenglonVariantebyId(Integer.parseInt(listOfRenfloge.get(0).toString()));
                double costoMano = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("2")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                double costoEq = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                double total = new BigDecimal(String.format("%.4f", Double.parseDouble(listOfRenfloge.get(1).toString()))).doubleValue() * costoMano + new BigDecimal(String.format("%.4f", Double.parseDouble(listOfRenfloge.get(1).toString()))).doubleValue() * costoEq;
                if (renglonvariante.getCodigo().trim().equals(normasInd.getCodenorna().trim())) {
                    // tempList.add(new RenglonesIndModel(renglonvariante.getSubgrupoBySubgrupoId().getGrupoByGrupoId().getCodigog() + " " + renglonvariante.getSubgrupoBySubgrupoId().getGrupoByGrupoId().getDescripciong(), renglonvariante.getSubgrupoBySubgrupoId().getCodigosub() + " " + renglonvariante.getSubgrupoBySubgrupoId().getDescripcionsub(), renglonvariante.getCodigo(), renglonvariante.getDescripcion(), renglonvariante.getUm(), new BigDecimal(String.format("%.4f", Double.parseDouble(listOfRenfloge.get(1).toString()))).doubleValue(), new BigDecimal(String.format("%.2f", total)).doubleValue()));

                }
            }
        }
        return tempList;
    }

    //para el calculo de los renglones variantes (Presupuesto)
    public ObservableList<RenglonesIndModel> getRenglonesToIndicators(String query) {
        ObservableList<RenglonesIndModel> renglonesIndModels = FXCollections.observableArrayList();
        List<Tuple> datos = getDataTocreateModel(query);
        for (Tuple dato : datos) {
            Unidadobra unidadobra = utilCalcSingelton.getUnidadobra(Integer.parseInt(dato.get(3).toString()));
            Renglonvariante renglonvariante = utilCalcSingelton.getRenglonvariante(Integer.parseInt(dato.get(0).toString()));
            RenglonesIndModel indicador = new RenglonesIndModel(unidadobra.getEmpresaconstructoraByEmpresaconstructoraId().getCodigo() + " " + unidadobra.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion().trim(), unidadobra.getZonasByZonasId().getCodigo() + " " + unidadobra.getZonasByZonasId().getDesripcion().trim(), unidadobra.getObjetosByObjetosId().getCodigo() + " " + unidadobra.getObjetosByObjetosId().getDescripcion().trim(), unidadobra.getNivelByNivelId().getCodigo() + " " + unidadobra.getNivelByNivelId().getDescripcion().trim(), unidadobra.getEspecialidadesByEspecialidadesId().getCodigo() + " " + unidadobra.getEspecialidadesByEspecialidadesId().getDescripcion().trim(), unidadobra.getCodigo().trim() + " " + unidadobra.getDescripcion().trim(), renglonvariante.getCodigo().trim(), renglonvariante.getDescripcion().trim(), renglonvariante.getUm(), new BigDecimal(String.format("%.4f", Double.parseDouble(dato.get(1).toString()))).doubleValue(), new BigDecimal(String.format("%.2f", Double.parseDouble(dato.get(2).toString()))).doubleValue());
            renglonesIndModels.add(indicador);
        }
        return renglonesIndModels;
    }

    //para el calculo de los renglones variantes (Plan)
    public ObservableList<RenglonesIndModel> getRenglonesToIndicatorsPlan(String query) {
        ObservableList<RenglonesIndModel> renglonesIndModels = FXCollections.observableArrayList();
        List<Tuple> datos = getDataTocreateModel(query);
        for (IndicadorGrup indicadorGrup : indicadorGrupList) {
            List<RenglonesIndModel> temp = FXCollections.observableArrayList();
            temp = listRebglonesByGroup(indicadorGrup, datos);
            double cant = temp.parallelStream().map(RenglonesIndModel::getVol).reduce(0.0, Double::sum);
            double costo = temp.parallelStream().map(RenglonesIndModel::getCosto).reduce(0.0, Double::sum);
            //  RenglonesIndModel indicador = new RenglonesIndModel(" ", " ", " ", indicadorGrup.getDescipcion(), " ", new BigDecimal(String.format("%.4f", cant)).doubleValue(), new BigDecimal(String.format("%.2f", costo)).doubleValue());
            // renglonesIndModels.add(indicador);
            renglonesIndModels.addAll(temp);
        }
        return renglonesIndModels;
    }

    //para el calculo de los renglones variantes (Pendientes)
    public ObservableList<RenglonesIndModel> getRenglonesToIndicatorsCertificacionPendientes(String valquery) {
        ObservableList<RenglonesIndModel> renglonesIndModels = FXCollections.observableArrayList();
        List<Tuple> datos = getDataTocreateModel(valquery);
        for (IndicadorGrup indicadorGrup : indicadorGrupList) {
            List<RenglonesIndModel> temp = FXCollections.observableArrayList();
            temp = listRebglonesByGroup(indicadorGrup, datos);
            double cant = temp.parallelStream().map(RenglonesIndModel::getVol).reduce(0.0, Double::sum);
            double costo = temp.parallelStream().map(RenglonesIndModel::getCosto).reduce(0.0, Double::sum);
            //  RenglonesIndModel indicador = new RenglonesIndModel(" ", " ", " ", indicadorGrup.getDescipcion(), " ", new BigDecimal(String.format("%.4f", cant)).doubleValue(), new BigDecimal(String.format("%.2f", costo)).doubleValue());
            // renglonesIndModels.add(indicador);
            renglonesIndModels.addAll(temp);
        }
        return renglonesIndModels;
    }

    private List<Tuple> getSetctedListGroupTableList(List<Tuple> list, String val) {
        return list.parallelStream().filter(tuple -> tuple.get(6).toString().trim().equals(val.trim())).collect(Collectors.toList());
    }

    // Para el calculo de la mano de obra (Presupuesto)
    public ObservableList<InsumoIndicadores> getCalcManoObra(String query) {
        ObservableList<InsumoIndicadores> insumoIndicadoresObservableList = FXCollections.observableArrayList();
        List<Tuple> datos = getDataTocreateModel(query);
        for (Tuple dato : datos) {
            insumoIndicadoresObservableList.add(new InsumoIndicadores(dato.get(7).toString().trim() + " " + dato.get(8).toString().trim(), dato.get(9).toString().trim() + " " + dato.get(10).toString().trim(), dato.get(11).toString().trim() + " " + dato.get(12).toString().trim(), dato.get(13).toString().trim() + " " + dato.get(14).toString().trim(), dato.get(15).toString().trim() + " " + dato.get(16).toString().trim(), dato.get(1).toString(), dato.get(2).toString(), dato.get(3).toString(), dato.get(6).toString(), new BigDecimal(String.format("%.4f", Double.parseDouble(dato.get(5).toString()))).doubleValue(), 0.0));
        }
        return insumoIndicadoresObservableList;
    }

    // Para el calculo de la mano de obra (Plan)
    public ObservableList<InsumoIndicadores> getCalcManoObraPlan(String query) {
        ObservableList<InsumoIndicadores> insumoIndicadoresObservableList = FXCollections.observableArrayList();
        List<Tuple> datos = getDataTocreateModel(query);
        /*
        for (String list : list) {
            List<Tuple> datosList = getSetctedListGroupTableList(datos, list);
            if (datosList.size() > 0) {
                double costoTotal = datosList.parallelStream().mapToDouble(tuple -> Double.parseDouble(tuple.get(5).toString()) * Double.parseDouble(tuple.get(4).toString())).reduce(0.0, Double::sum);
                InsumoIndicadores indicador = new InsumoIndicadores("Grupo: " + list, " ", " ", 0.0, 0.0, Double.parseDouble(String.format("%.2f", costoTotal)));
                insumoIndicadoresObservableList.add(indicador);
                for (Tuple dato : datosList) {
                    insumoIndicadoresObservableList.add(new InsumoIndicadores(dato.get(1).toString(), dato.get(2).toString(), dato.get(3).toString(), new BigDecimal(String.format("%.4f", Double.parseDouble(dato.get(5).toString()))).doubleValue(), new BigDecimal(String.format("%.2f", Double.parseDouble(dato.get(4).toString()))).doubleValue(), new BigDecimal(String.format("%.2f", Double.parseDouble(dato.get(4).toString()) * Double.parseDouble(dato.get(5).toString()))).doubleValue()));
                }
            }
        }
        */

        return insumoIndicadoresObservableList;
    }

    //para el calculo de los recursos(Presupuesto)
    public ObservableList<InsumoIndicadores> getCalcrecursos(String query) {
        ObservableList<InsumoIndicadores> insumoRecursosObserve = FXCollections.observableArrayList();
        CalForReport cfr = new CalForReport();
        List<Tuple> datos = getDataTocreateModel(query);
        for (Tuple dato : datos) {
            if (dato.get(2).toString().trim().equals("1")) {
                Recursos rec = utilCalcSingelton.getSuministro(Integer.parseInt(dato.get(1).toString()));
                insumoRecursosObserve.add(new InsumoIndicadores(dato.get(3).toString().trim() + " " + dato.get(4).toString().trim(), dato.get(5).toString().trim() + " " + dato.get(6).toString().trim(), dato.get(7).toString().trim() + " " + dato.get(8).toString().trim(), dato.get(9).toString().trim() + " " + dato.get(10).toString().trim(), dato.get(11).toString().trim() + " " + dato.get(12).toString().trim(), rec.getCodigo().trim(), rec.getDescripcion().trim(), rec.getUm(), "I", new BigDecimal(String.format("%.4f", Double.parseDouble(dato.get(0).toString()))).doubleValue(), rec.getPreciomn()));

            } else if (dato.get(2).toString().trim().equals("S")) {
                Suministrossemielaborados rec = utilCalcSingelton.getSuministroSemielaboradoById(Integer.parseInt(dato.get(1).toString()));
                insumoRecursosObserve.add(new InsumoIndicadores(dato.get(3).toString().trim() + " " + dato.get(4).toString().trim(), dato.get(5).toString().trim() + " " + dato.get(6).toString().trim(), dato.get(7).toString().trim() + " " + dato.get(8).toString().trim(), dato.get(9).toString().trim() + " " + dato.get(10).toString().trim(), dato.get(11).toString().trim() + " " + dato.get(12).toString().trim(), rec.getCodigo().trim(), rec.getDescripcion().trim(), rec.getUm(), "I", new BigDecimal(String.format("%.4f", Double.parseDouble(dato.get(0).toString()))).doubleValue(), rec.getPreciomn()));

            }
            if (dato.get(2).toString().trim().equals("J")) {
                Juegoproducto rec = utilCalcSingelton.getJuegoProductoById(Integer.parseInt(dato.get(1).toString()));
                insumoRecursosObserve.add(new InsumoIndicadores(dato.get(3).toString().trim() + " " + dato.get(4).toString().trim(), dato.get(5).toString().trim() + " " + dato.get(6).toString().trim(), dato.get(7).toString().trim() + " " + dato.get(8).toString().trim(), dato.get(9).toString().trim() + " " + dato.get(10).toString().trim(), dato.get(11).toString().trim() + " " + dato.get(12).toString().trim(), rec.getCodigo().trim(), rec.getDescripcion().trim(), rec.getUm(), "I", new BigDecimal(String.format("%.4f", Double.parseDouble(dato.get(0).toString()))).doubleValue(), rec.getPreciomn()));
            }
        }
        return insumoRecursosObserve;
    }

    public void deleteIndicadores(int idI) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            int op1 = session.createQuery(" DELETE FROM NormasInd WHERE indcadorId =: idInd").setParameter("idInd", idI).executeUpdate();
            int op2 = session.createQuery(" DELETE FROM IndicadorGrup WHERE id =: id").setParameter("id", idI).executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void deleteNormas(int idI) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            int op1 = session.createQuery(" DELETE FROM NormasInd WHERE id =: idInd").setParameter("idInd", idI).executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }


}
