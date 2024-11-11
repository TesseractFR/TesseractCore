package onl.tesseract.core.persistence.hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BDDConnection {
    private static final Logger log = LoggerFactory.getLogger(BDDConnection.class);
    private final BDD database;
    private Connection connection;


    public BDDConnection(BDD database) {
        this.database = database;
        this.connect();
    }


    private void connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(database.getUrl(),database.getUser(), database.getPassword());
            log.info("[BDD] : connection done");
        } catch (SQLException | ClassNotFoundException throwables) {
            log.error("Could not connect to database", throwables);
        }
    }

    public void close() throws SQLException {
        if(this.connection != null){
            if(!this.connection.isClosed()){
                this.connection.close();
            }
        }
    }

    public Connection getConnection() throws SQLException {
        if(connection != null){
            if(!connection.isClosed()){
                return connection;
            }
        }
        connect();
        return connection;
    }
}
