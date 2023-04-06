package models;

public class ComunDataRepositorySingleton {
    public static ComunDataRepositorySingleton instancia = null;


    private ComunDataRepositorySingleton() {

    }

    public static ComunDataRepositorySingleton getInstance() {

        if (instancia == null) {
            instancia = new ComunDataRepositorySingleton();
        }

        return instancia;
    }


}
