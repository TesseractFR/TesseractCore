package onl.tesseract.core.persistence.hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BDD {
    private static final Logger log = LoggerFactory.getLogger(BDD.class);

    private final String host;
    private final String user;
    private final String password;
    private final int port;
    private final String dbName;

    public BDD(String host, String user, String password, String dbName, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
        this.dbName = dbName;
    }

    public String getUrl(){
        log.info("jdbc:mysql://"+host+":"+port+"/"+dbName);
        return "jdbc:mysql://"+host+":"+port+"/"+dbName+"?autoReconnect=true";

    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
