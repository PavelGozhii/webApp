package dao;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private final static String url = "jdbc:postgresql://localhost:5432/ProductViewer";
    private final static String user = "postgres";
    private final static String password = "root";
    private static final Logger logger = Logger.getLogger(DBConnector.class);

    public static Connection connect() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            logger.debug("Connection is successful");
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        } finally {
            return connection;
        }
    }


}
