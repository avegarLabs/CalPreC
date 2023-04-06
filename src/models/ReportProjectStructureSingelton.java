package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportProjectStructureSingelton {

    public static ReportProjectStructureSingelton instancia = null;
    private static Unidadobrarenglon unidadobrarenglon;
    public ArrayList<Obra> obraArrayList;
    public ArrayList<Zonas> zonasArrayList;
    public ArrayList<Objetos> objetosArrayList;
    public ArrayList<Nivel> nivelArrayList;
    public ArrayList<Especialidades> especialidadesArrayList;
    public ArrayList<Subespecialidades> subespecialidadesArrayList;
    public ArrayList<Empresaconstructora> empresaconstructoraArrayList;
    public ArrayList<Brigadaconstruccion> brigadaconstruccionArrayList;
    public ArrayList<Grupoconstruccion> grupoconstruccionArrayList;
    public ArrayList<Cuadrillaconstruccion> cuadrillaconstruccionArrayList;
    public List<Empresagastos> empresagastosList;
    public Renglonvariante renglonvariante;
    public Empresaconstructora empresaconstructora;
    public List<Renglonvariante> renglonvarianteList;
    private ObservableList<String> obrasObservableList;
    private ObservableList<String> zonasObservableList;
    private ObservableList<String> objetosObservableList;
    private ObservableList<String> nivelObservableList;
    private ObservableList<String> especialidadesObservableList;
    private ObservableList<String> subespecialidadesObservableList;
    private ObservableList<String> emprStringObservableList;
    private ObservableList<String> brigadaStringObservableList;
    private ObservableList<String> grupoStringObservableList;
    private ObservableList<String> cuadrillaStringObservableList;
    private Empresa empresa;
    private Obra obra;
    private List<Empresaobraconceptoscoeficientes> valCoeficienteList;

    private ReportProjectStructureSingelton() {

    }

    public static ReportProjectStructureSingelton getInstance() {
        if (instancia == null) {
            instancia = new ReportProjectStructureSingelton();
        }

        return instancia;
    }

    private static void deleteCertificacionRV(int idUO, int idRen) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            int opt1 = session.createQuery(" DELETE FROM Certificacionrecuo WHERE unidadobraId =: idU AND renglonId =: idR").setParameter("idU", idUO).setParameter("idR", idRen).executeUpdate();
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public static Unidadobrarenglon getUnidadobrarenglon(int idU, int idR) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadobrarenglon = new Unidadobrarenglon();
            Query query = session.createQuery("FROM Unidadobrarenglon WHERE unidadobraId =: idUn AND renglonvarianteId =: idRe").setParameter("idUn", idU).setParameter("idRe", idR);
            if (query.getResultList().isEmpty()) {
                System.out.println(" Eliminado " + "idU: " + idU + " idRe " + idR);
                deleteCertificacionRV(idU, idR);
                unidadobrarenglon = null;
            } else {
                unidadobrarenglon = (Unidadobrarenglon) query.getResultList().get(0);
            }

            tx.commit();
            session.close();
            return unidadobrarenglon;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return unidadobrarenglon;
    }

    public ObservableList<String> getObrasList() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            obrasObservableList = FXCollections.observableArrayList();
            obraArrayList = (ArrayList<Obra>) session.createQuery("FROM Obra WHERE tipo = 'UO'").list();
            obrasObservableList.addAll(obraArrayList.parallelStream().map(o -> o.toString().trim()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return obrasObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getObrasListRV() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            obrasObservableList = FXCollections.observableArrayList();
            obraArrayList = (ArrayList<Obra>) session.createQuery("FROM Obra WHERE tipo = 'RV'").list();
            obrasObservableList.addAll(obraArrayList.parallelStream().map(o -> o.toString()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return obrasObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getZonasList(int idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            zonasObservableList = FXCollections.observableArrayList();
            zonasObservableList.add("Todas");
            zonasArrayList = (ArrayList<Zonas>) session.createQuery("FROM Zonas WHERE obraId =: idObra").setParameter("idObra", idObra).getResultList();
            zonasObservableList.addAll(zonasArrayList.parallelStream().map(z -> z.toString().trim()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return zonasObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getEmpresaList(int idObra) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            emprStringObservableList = FXCollections.observableArrayList();
            empresaconstructoraArrayList = new ArrayList<>();
            List<Empresaobra> resultList = session.createQuery("FROM Empresaobra WHERE obraId =: idObra").setParameter("idObra", idObra).getResultList();
            for (Empresaobra row : resultList) {
                empresaconstructoraArrayList.add(row.getEmpresaconstructoraByEmpresaconstructoraId());
                emprStringObservableList.add(row.getEmpresaconstructoraByEmpresaconstructoraId().toString().trim());
            }

            tx.commit();
            session.close();
            return emprStringObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getObjetosList(int idZona) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            objetosObservableList = FXCollections.observableArrayList();
            objetosObservableList.add("Todos");
            objetosArrayList = (ArrayList<Objetos>) session.createQuery("FROM Objetos WHERE zonasId =: idZona").setParameter("idZona", idZona).getResultList();
            objetosObservableList.addAll(objetosArrayList.parallelStream().map(ob -> ob.toString().trim()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return objetosObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getNivelList(int idObjeto) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            nivelObservableList = FXCollections.observableArrayList();
            nivelObservableList.add("Todos");
            nivelArrayList = (ArrayList<Nivel>) session.createQuery("FROM Nivel WHERE objetosId =: idObjeto").setParameter("idObjeto", idObjeto).getResultList();
            nivelObservableList.addAll(nivelArrayList.parallelStream().map(n -> n.toString().trim()).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return nivelObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getEspecialidadesList() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            especialidadesObservableList = FXCollections.observableArrayList();
            especialidadesObservableList.add("Todas");
            especialidadesArrayList = (ArrayList<Especialidades>) session.createQuery("FROM Especialidades ").getResultList();
            especialidadesObservableList.addAll(especialidadesArrayList.parallelStream().map(es -> es.toString().trim()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return especialidadesObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getSubespecialidadesObservableList(int idEspec) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            subespecialidadesObservableList = FXCollections.observableArrayList();
            subespecialidadesObservableList.add("Todas");
            subespecialidadesArrayList = (ArrayList<Subespecialidades>) session.createQuery("FROM Subespecialidades WHERE especialidadesId =: idEsp").setParameter("idEsp", idEspec).getResultList();
            subespecialidadesObservableList.addAll(subespecialidadesArrayList.parallelStream().map(sub -> sub.toString()).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return subespecialidadesObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public Empresa getEmpresa() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            empresa = session.get(Empresa.class, 1);


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresa;
    }

    public Empresaconstructora getEmpresaconstructora(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            empresaconstructora = session.get(Empresaconstructora.class, id);


            tx.commit();
            session.close();
            return empresaconstructora;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresaconstructora;
    }

    public ObservableList<String> getBrigadaStringObservableList(int idEmpresa) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            brigadaStringObservableList = FXCollections.observableArrayList();
            brigadaStringObservableList.add("Todas");
            brigadaconstruccionArrayList = (ArrayList<Brigadaconstruccion>) session.createQuery("FROM Brigadaconstruccion WHERE empresaconstructoraId =: idEmp").setParameter("idEmp", idEmpresa).getResultList();
            brigadaStringObservableList.addAll(brigadaconstruccionArrayList.parallelStream().map(bg -> bg.toString().trim()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return brigadaStringObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getGrupoStringObservableList(int idBrigada) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            grupoStringObservableList = FXCollections.observableArrayList();
            grupoconstruccionArrayList = (ArrayList<Grupoconstruccion>) session.createQuery("FROM Grupoconstruccion WHERE brigadaconstruccionId =: idBrig").setParameter("idBrig", idBrigada).getResultList();
            grupoStringObservableList.add("Todos");
            grupoStringObservableList.addAll(grupoconstruccionArrayList.parallelStream().map(gpc -> gpc.toString().trim()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return grupoStringObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getCuadrillaStringObservableList(int idGrupo) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            cuadrillaconstruccionArrayList = (ArrayList<Cuadrillaconstruccion>) session.createQuery("FROM Cuadrillaconstruccion WHERE grupoconstruccionId =: idGrup").setParameter("idGrup", idGrupo).getResultList();
            cuadrillaStringObservableList = FXCollections.observableArrayList();
            cuadrillaStringObservableList.add("Todas");
            cuadrillaStringObservableList.addAll(cuadrillaconstruccionArrayList.parallelStream().map(cuadrillaconstruccion -> cuadrillaconstruccion.toString().trim()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return cuadrillaStringObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public Obra getObra(int idOb) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            obra = session.get(Obra.class, idOb);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return obra;
    }

    public List<Empresagastos> getEmpresagastosList() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            empresagastosList = new ArrayList<>();
            empresagastosList = session.createQuery("FROM Empresagastos ").getResultList();

            tx.commit();
            session.close();
            return empresagastosList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Empresaobraconceptoscoeficientes> getValCoeficienteList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            valCoeficienteList = new ArrayList<>();
            valCoeficienteList = session.createQuery("FROM Empresaobraconceptoscoeficientes ").getResultList();
            tx.commit();
            session.close();
            return valCoeficienteList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public Renglonvariante getRenglonvariante(int idReng) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            renglonvariante = session.get(Renglonvariante.class, idReng);
            tx.commit();
            session.close();
            return renglonvariante;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return renglonvariante;
    }

    public List<Renglonvariante> getAllRenglones() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            renglonvarianteList = new ArrayList<>();
            renglonvarianteList = session.createQuery(" from Renglonvariante ").getResultList();

            tx.commit();
            session.close();
            return renglonvarianteList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public int getIdObraByToString(String toString) {
        return obraArrayList.parallelStream().filter(iten -> iten.toString().trim().equals(toString.trim())).map(Obra::getId).findFirst().get();
    }

    public int getIdZonasByToString(String toString) {
        return zonasArrayList.parallelStream().filter(iten -> iten.toString().trim().equals(toString.trim())).map(Zonas::getId).findFirst().get();
    }

    public int getIdObjetosByToString(String toString) {
        return objetosArrayList.parallelStream().filter(iten -> iten.toString().trim().equals(toString.trim())).map(Objetos::getId).findFirst().get();
    }

    public int getIdNivelByToString(String toString) {
        return nivelArrayList.parallelStream().filter(iten -> iten.toString().trim().equals(toString.trim())).map(Nivel::getId).findFirst().get();
    }

    public int getIdEspecialidasByToString(String toString) {
        return especialidadesArrayList.parallelStream().filter(iten -> iten.toString().trim().equals(toString.trim())).map(Especialidades::getId).findFirst().get();
    }

    public int getIdSubespecialidadesByToString(String toString) {
        return subespecialidadesArrayList.parallelStream().filter(iten -> iten.toString().trim().equals(toString.trim())).map(Subespecialidades::getId).findFirst().get();
    }

    public int getIdEmpresaByToString(String toString) {
        return empresaconstructoraArrayList.parallelStream().filter(iten -> iten.toString().trim().equals(toString.trim())).map(Empresaconstructora::getId).findFirst().get();
    }

    public int getIdGrupoByToString(String toString) {
        return grupoconstruccionArrayList.parallelStream().filter(iten -> iten.toString().trim().equals(toString.trim())).map(Grupoconstruccion::getId).findFirst().get();
    }

    public int getIdBrigadaByToString(String toString) {
        return brigadaconstruccionArrayList.parallelStream().filter(iten -> iten.toString().trim().equals(toString.trim())).map(Brigadaconstruccion::getId).findFirst().get();
    }

    public int getIdCuadrillaByToString(String toString) {
        return cuadrillaconstruccionArrayList.parallelStream().filter(iten -> iten.toString().trim().equals(toString.trim())).map(Cuadrillaconstruccion::getId).findFirst().get();
    }
}
