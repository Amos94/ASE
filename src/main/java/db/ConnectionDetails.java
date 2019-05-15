package db;

public enum ConnectionDetails {

//    URL("jdbc:h2:mem:"),
//    USER("ASE"),
//    PASSWORD("ASEPASSWORD");
	URL("jdbc:h2:tcp://localhost/~/test"),
	USER("sa"),
	PASSWORD("");

    private String conf;

    ConnectionDetails(String conf) {
        this.conf = conf;
    }

    public String getConf() {
        return conf;
    }
}
