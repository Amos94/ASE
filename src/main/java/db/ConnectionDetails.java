package db;

public enum ConnectionDetails {

    URL("jdbc:h2:mem:"),
    USER("ASE"),
    PASSWORD("ASEPASSWORD");

    private String conf;

    ConnectionDetails(String conf) {
        this.conf = conf;
    }

    public String getConf() {
        return conf;
    }
}
