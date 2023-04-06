package models;

public class PgsqlConnection {

    public String type;
    public String url;
    public String database;
    public String port;

    public PgsqlConnection(String type, String url, String database, String port) {
        this.type = type;
        this.url = url;
        this.database = database;
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}
