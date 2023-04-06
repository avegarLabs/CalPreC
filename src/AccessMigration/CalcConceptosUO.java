package AccessMigration;

import AccessMigration.model.EmpresaToImportView;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CalcConceptosUO {

    public static CalcConceptosUO instancia = null;
    private double concepto1;
    private double concepto2;
    private double concepto3;
    private double concepto4;
    private double concepto5;
    private double concepto6;
    private double concepto7;
    private double concepto8;
    private double concepto9;
    private double concepto10;
    private double concepto11;
    private double concepto12;
    private double concepto13;
    private double concepto14;
    private double concepto15;
    private double concepto16;
    private double concepto17;
    private double concepto18;
    private double concepto19;
    private double concepto20;
    private double tempValue;
    private Empresaobraconcepto empresaobraconcepto;
    private List<Empresagastos> listGastos;
    private ArrayList<Empresaobraconcepto> listEmpresaObraConcepto;
    private Double[] valores;
    private int count;
    private int batchSize = 10;
    private double rest;
    private Double val;

    private CalcConceptosUO() {

    }

    public static CalcConceptosUO getInstance() {
        if (instancia == null) {
            instancia = new CalcConceptosUO();
        }

        return instancia;
    }

    public List<Empresagastos> getListGastos(Integer id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listGastos = new LinkedList<>();
            Empresaconstructora empresaconstructora = session.get(Empresaconstructora.class, id);
            listGastos = empresaconstructora.getEmpresagastosById();
            listGastos.size();

            tx.commit();
            session.close();
            return listGastos;
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


            count = 0;
            for (Empresaobraconcepto datos : empresaobraconceptoArrayList) {
                count++;
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


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            valores = new Double[3];
            List<Object[]> rvuo = session.createSQLQuery(" SELECT * FROM get_values_empresa_obra(:idOb, :idEmp)").setParameter("idOb", idob).setParameter("idEmp", idemp).getResultList();

            for (Object[] row : rvuo) {
                if (row[0] == null) {
                    valores[0] = 0.0;
                } else {
                    valores[0] = Double.parseDouble(row[0].toString());
                }
                if (row[1] == null) {
                    valores[1] = 0.0;
                } else {
                    valores[1] = Double.parseDouble(row[1].toString());
                }
                if (row[2] == null) {
                    valores[2] = 0.0;
                } else {
                    valores[2] = Double.parseDouble(row[2].toString());
                }

            }

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

    private Integer getTipoobraId(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Obra obra = (Obra) session.createQuery("FROM Obra WHERE id =: idO").setParameter("idO", id).getSingleResult();
            tx.commit();
            session.close();

            return obra.getTipoobraId();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return 0;
    }

    public void calcValoresdeGastos(List<EmpresaToImportView> empresaToImportViewList, int idOb) {

        for (EmpresaToImportView emp : empresaToImportViewList) {
            listEmpresaObraConcepto = new ArrayList<>();
            rest = 0.0;
            valores = new Double[3];
            valores = getListValoresGastos(idOb, emp.getId());
            concepto1 = valores[0];
            concepto2 = valores[1];
            concepto3 = valores[2];
            concepto4 = 0.0;
            concepto5 = 0.0;
            concepto6 = concepto1 + concepto2 + concepto3 + concepto4 + concepto5;
            concepto7 = 0.0;
            concepto8 = concepto7;
            concepto9 = concepto6 + concepto8;
            concepto10 = 0.0;
            concepto11 = 0.0;
            concepto12 = 0.0;
            concepto13 = 0.0;
            concepto14 = 0.0;
            concepto15 = 0.0;
            concepto16 = 0.0;
            concepto17 = concepto10 + concepto11 + concepto12 + concepto13 + concepto14 + concepto15 + concepto16;
            concepto18 = concepto9 + concepto17;
            rest = concepto9 - concepto1;
            concepto19 = 0.2 * rest;
            concepto20 = concepto18 + concepto19;

            listGastos = new LinkedList<>();
            listGastos = getListGastos(emp.getId());
            for (Empresagastos empG : listGastos) {
                empresaobraconcepto = new Empresaobraconcepto();
                empresaobraconcepto.setEmpresaconstructoraId(emp.getId());
                empresaobraconcepto.setObraId(idOb);
                if (empG.getConceptosgastoId() == 1) {
                    concepto1 = valores[0];
                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto1);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 2) {
                    concepto2 = valores[1];
                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto2);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 3) {
                    concepto3 = valores[2];
                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto3);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 4) {
                    if (empG.getPorciento() == 1.0) {
                        empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                        empresaobraconcepto.setValor(concepto4);
                        listEmpresaObraConcepto.add(empresaobraconcepto);
                    } else {
                        concepto4 = concepto1 + concepto2 + concepto3;
                        empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                        empresaobraconcepto.setValor(concepto4);
                        listEmpresaObraConcepto.add(empresaobraconcepto);
                    }

                } else if (empG.getConceptosgastoId() == 5) {
                    if (empG.getPorciento() == 1.0) {
                        empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                        empresaobraconcepto.setValor(concepto5);
                        listEmpresaObraConcepto.add(empresaobraconcepto);
                    } else {
                        rest = empG.getPorciento() / 100;
                        concepto5 = rest * concepto1;
                        empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                        empresaobraconcepto.setValor(concepto5);
                        listEmpresaObraConcepto.add(empresaobraconcepto);
                    }

                } else if (empG.getConceptosgastoId() == 6) {
                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto6);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 7) {
                    if (empG.getPorciento() == 1.0) {
                        empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                        empresaobraconcepto.setValor(concepto7);
                        listEmpresaObraConcepto.add(empresaobraconcepto);
                    } else {
                        rest = empG.getPorciento() / 100;
                        concepto7 = rest * concepto6;
                        empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                        empresaobraconcepto.setValor(concepto7);
                        listEmpresaObraConcepto.add(empresaobraconcepto);
                    }

                } else if (empG.getConceptosgastoId() == 8) {

                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto8);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 9) {

                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto9);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 10) {
                    if (empG.getCalcular() == 1) {
                        empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                        double valFt = calcConcept10(concepto9, getTipoobraId(idOb));
                        empresaobraconcepto.setValor(valFt);
                        listEmpresaObraConcepto.add(empresaobraconcepto);
                    } else {

                        empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                        empresaobraconcepto.setValor(concepto10);
                        listEmpresaObraConcepto.add(empresaobraconcepto);
                    }

                } else if (empG.getConceptosgastoId() == 11) {

                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto11);
                    listEmpresaObraConcepto.add(empresaobraconcepto);

                } else if (empG.getConceptosgastoId() == 12) {

                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto11);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 13) {

                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto13);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 14) {

                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto14);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 15) {

                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto15);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 16) {

                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto16);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 17) {

                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto17);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 18) {

                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto18);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 19) {

                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto19);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                } else if (empG.getConceptosgastoId() == 20) {

                    empresaobraconcepto.setConceptosgastoId(empG.getConceptosgastoId());
                    empresaobraconcepto.setValor(concepto20);
                    listEmpresaObraConcepto.add(empresaobraconcepto);
                }
            }

            insertValorestoGastos(listEmpresaObraConcepto);
        }

    }

    private double calcConcept10(double concepto9, Integer tipo) {
        val = 0.0;
        tempValue = 0.0;
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
