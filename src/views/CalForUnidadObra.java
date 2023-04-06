package views;

import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Esta clase calcula los conceptos del presupusto a nivel de unidad de obra
 */
public class CalForUnidadObra extends Thread {
    public UnidadesObraObraController unidadesObraObraController;
    public Empresaconstructora empresaconstructoraModel;
    public Obra obra;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private List<Empresagastos> listGastos;
    private Double[] valores;

    public CalForUnidadObra(UnidadesObraObraController ObraController, Empresaconstructora empresa, Obra ob) {
        unidadesObraObraController = ObraController;
        empresaconstructoraModel = empresa;
        obra = ob;

    }

    public List<Conceptosgasto> getListGastos(int idResol) {
        listGastos = new LinkedList<>();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Conceptosgasto> list = new ArrayList<>();
            list = session.createQuery("FROM Conceptosgasto WHERE pertence =: id").setParameter("id", idResol).getResultList();
            tx.commit();
            session.close();
            return list;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public void insertValorestoGastos(ArrayList<Empresaobraconcepto> empresaobraconceptoArrayList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            int count = 0;
            for (Empresaobraconcepto datos : empresaobraconceptoArrayList) {
                count++;
                int batchSize = 10;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.saveOrUpdate(datos);
            }

            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Double[] getListValoresGastos(Integer idob, Integer idemp) {
        valores = new Double[3];

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Unidadobra> uoList = new ArrayList<>();
            uoList = session.createQuery(" FROM Unidadobra WHERE obraId = :idOb AND  empresaconstructoraId =: idEmp ").setParameter("idOb", idob).setParameter("idEmp", idemp).getResultList();
            valores[0] = uoList.parallelStream().map(Unidadobra::getCostoMaterial).reduce(0.0, Double::sum);
            valores[1] = uoList.parallelStream().map(Unidadobra::getCostomano).reduce(0.0, Double::sum);
            valores[2] = uoList.parallelStream().map(Unidadobra::getCostoequipo).reduce(0.0, Double::sum);

            tx.commit();
            session.close();
            return valores;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return new Double[3];
    }

    @Override
    public void run() {
        calcValoresdeGastos(unidadesObraObraController, empresaconstructoraModel, obra);
    }


    public void calcValoresdeGastos(UnidadesObraObraController controller, Empresaconstructora empresa, Obra obraSend) {
        ArrayList<Empresaobraconcepto> listEmpresaObraConcepto = new ArrayList<>();
        obra = obraSend;
        valores = new Double[3];
        valores = getListValoresGastos(obra.getId(), empresa.getId());
        unidadesObraObraController = controller;
        if (obra.getSalarioId() == 1) {
            double rest = 0.0;
            double concepto1 = valores[0];
            double concepto2 = valores[1];
            double concepto3 = valores[2];
            double concepto4 = 0.0;
            double concepto5 = 0.0;
            double concepto6 = concepto1 + concepto2 + concepto3 + concepto4 + concepto5;
            double concepto7 = 0.0;
            double concepto8 = concepto7;
            double concepto9 = concepto6 + concepto8;
            double concepto10 = 0.0;
            double concepto11 = 0.0;
            double concepto12 = 0.0;
            double concepto13 = 0.0;
            double concepto14 = 0.0;
            double concepto15 = 0.0;
            double concepto16 = 0.0;
            double concepto17 = concepto10 + concepto11 + concepto12 + concepto13 + concepto14 + concepto15 + concepto16;
            double concepto18 = concepto9 + concepto17;
            rest = concepto9 - concepto1;
            double concepto19 = 0.2 * rest;
            double concepto20 = concepto18 + concepto19;
            listGastos = new LinkedList<>();
            listGastos = empresa.getEmpresagastosById();
            for (Empresagastos empG : listGastos) {
                if (empG.getConceptosgastoId() == 1) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto1, 0));
                } else if (empG.getConceptosgastoId() == 2) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto2, 0));
                } else if (empG.getConceptosgastoId() == 3) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto3, 0));
                } else if (empG.getConceptosgastoId() == 4) {
                    if (empG.getPorciento() == 1.0) {
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto4, 0));
                    } else {
                        concepto4 = concepto1 + concepto2 + concepto3;
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto4, 0));
                    }

                } else if (empG.getConceptosgastoId() == 5) {
                    if (empG.getPorciento() == 1.0) {
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto5, 0));
                    } else {
                        rest = empG.getPorciento() / 100;
                        concepto5 = rest * concepto1;
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto5, 0));
                    }
                } else if (empG.getConceptosgastoId() == 6) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto6, 0));
                } else if (empG.getConceptosgastoId() == 7) {
                    if (empG.getPorciento() == 1.0) {
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto7, 0));
                    } else {
                        rest = empG.getPorciento() / 100;
                        concepto7 = rest * concepto6;
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto7, 0));
                    }
                } else if (empG.getConceptosgastoId() == 8) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto8, 0));
                } else if (empG.getConceptosgastoId() == 9) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto9, 0));
                } else if (empG.getConceptosgastoId() == 10) {
                    if (empG.getCalcular() == 1) {
                        double valFt = calcConcept10(concepto9, obra.getTipoobraId());
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), valFt, 0));
                    } else {
                        listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto10, 0));
                    }
                } else if (empG.getConceptosgastoId() == 11) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto11, 0));
                } else if (empG.getConceptosgastoId() == 12) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto12, 0));
                } else if (empG.getConceptosgastoId() == 13) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto13, 0));
                } else if (empG.getConceptosgastoId() == 14) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto14, 0));
                } else if (empG.getConceptosgastoId() == 15) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto15, 0));
                } else if (empG.getConceptosgastoId() == 16) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto16, 0));
                } else if (empG.getConceptosgastoId() == 17) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto17, 0));
                } else if (empG.getConceptosgastoId() == 18) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto18, 0));
                } else if (empG.getConceptosgastoId() == 19) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto19, 0));
                } else if (empG.getConceptosgastoId() == 20) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), empG.getConceptosgastoId(), concepto20, 0));
                }
            }
            insertValorestoGastos(listEmpresaObraConcepto);
        } else if (obra.getSalarioId() == 2 || obra.getSalarioId() == 3) {
            double rest = 0.0;
            double conceptoA1 = valores[0];
            double conceptoA2 = valores[1];
            double conceptoA3 = valores[2];
            double conceptoA4 = 0.0;
            double conceptoA5 = 0.0;
            double conceptoA6 = 0.0;
            double conceptoA7 = 0.0;
            double conceptoA8 = 0.0;
            double conceptoA9 = 0.0;
            double conceptoA10 = 0.0;
            double conceptoA11 = 0.0;
            double conceptoA12 = 0.0;
            double conceptoA13 = 0.0;
            double conceptoA14 = 0.0;
            double conceptoA15 = 0.0;
            double conceptoA16 = 0.0;
            double conceptoA17 = 0.0;
            for (Conceptosgasto cg : getListGastos(obra.getSalarioId())) {
                if (cg.getDescripcion().trim().equals("Materiales")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA1, 0));
                } else if (cg.getDescripcion().trim().equals("Mano de Obra")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA2, 0));
                } else if (cg.getDescripcion().trim().equals("Uso de Equipos")) {
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA3, 0));
                } else if (cg.getDescripcion().trim().equals("Otros Gastos Directos del Proceso Productivo")) {
                    conceptoA4 = getValorEmpresaObraConcepto(empresa.getId(), cg.getId());
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA4, 0));
                } else if (cg.getDescripcion().trim().equals("Costos Directos de Producción")) {
                    conceptoA5 = conceptoA1 + conceptoA2 + conceptoA3 + conceptoA4;
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA5, 0));
                } else if (cg.getDescripcion().trim().equals("Gastos Asociados a la Producción de la Obra")) {
                    conceptoA6 = getValorEmpresaObraConcepto(empresa.getId(), cg.getId());
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA6, 0));
                } else if (cg.getDescripcion().trim().equals("Total de Costos y Gastos de Producción de la Obra")) {
                    conceptoA7 = conceptoA5 + conceptoA6;
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA7, 0));
                } else if (cg.getDescripcion().trim().equals("Gastos Generales y de Administración")) {
                    conceptoA8 = getValorEmpresaObraConcepto(empresa.getId(), cg.getId());
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA8, 0));
                } else if (cg.getDescripcion().trim().equals("Gastos Indirectos")) {
                    conceptoA9 = conceptoA6 + conceptoA8;
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA9, 0));
                } else if (cg.getDescripcion().trim().equals("Otros Conceptos de Gastos")) {
                    conceptoA10 = getValorEmpresaObraConcepto(empresa.getId(), cg.getId());
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA10, 0));
                } else if (cg.getDescripcion().trim().equals("Gastos Financieros")) {
                    conceptoA11 = getValorEmpresaObraConcepto(empresa.getId(), cg.getId());
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA11, 0));
                } else if (cg.getDescripcion().trim().equals("Gastos Tributarios")) {
                    conceptoA12 = getValorEmpresaObraConcepto(empresa.getId(), cg.getId());
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA12, 0));
                } else if (cg.getDescripcion().trim().equals("Total de Gastos de la Obra")) {
                    conceptoA13 = conceptoA8 + conceptoA10 + conceptoA11 + conceptoA12;
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA12, 0));
                } else if (cg.getDescripcion().trim().equals("Total de Costos y Gastos")) {
                    conceptoA14 = conceptoA5 + conceptoA9 + conceptoA10 + conceptoA11 + conceptoA12;
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA13, 0));
                } else if (cg.getDescripcion().trim().equals("Impuestos sobre ventas autorizados por MFP")) {
                    conceptoA15 = getValorEmpresaObraConcepto(empresa.getId(), cg.getId());
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA15, 0));
                } else if (cg.getDescripcion().trim().equals("Utilidad")) {
                    rest = conceptoA14 - conceptoA1 - conceptoA13;
                    conceptoA16 = 0.15 * rest;
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA16, 0));
                } else if (cg.getDescripcion().trim().equals("Precio del Servicio de Construcción y Montaje")) {
                    conceptoA17 = conceptoA13 + conceptoA14 + conceptoA15;
                    listEmpresaObraConcepto.add(utilCalcSingelton.createEOC(empresa.getId(), obra.getId(), cg.getId(), conceptoA17, 0));
                }
            }
            insertValorestoGastos(listEmpresaObraConcepto);
        }
    }

    private double getValorEmpresaObraConcepto(int idEmpresa, int idCon) {
        return obra.getEmpresaobraconceptosById().parallelStream().filter(item -> item.getEmpresaconstructoraId() == idEmpresa && item.getConceptosgastoId() == idCon).map(Empresaobraconcepto::getValor).findFirst().orElse(0.0);
    }

    private double calcConcept10(double concepto9, Integer tipo) {
        Double val = 0.0;
        double tempValue = 0.0;
        if (tipo == 1) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 2) {
            val = 0.04 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 3) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 4) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 5) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 6) {
            val = 0.02 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 7) {
            val = 0.02 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 8) {
            val = 0.04 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 9) {
            val = 0.02 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 10) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 13) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 14) {
            val = 0.02 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 15) {
            val = 0.03 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 16) {
            val = 0.06 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        } else if (tipo == 17) {
            val = 0.01 * concepto9;
            tempValue = Math.round(val * 100d) / 100d;
        }

        return tempValue;
    }
}
