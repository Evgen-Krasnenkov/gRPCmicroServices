package com.shopping.dao;

import org.h2.tools.RunScript;
import org.h2.tools.Server;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class H2DataBaseConnection {
    private static final Logger logger = Logger.getLogger(H2DataBaseConnection.class.getName());
    static {
        try {
            initializeDatabase(getConnectionToDB());
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "No such file", e);
        } catch (SQLException e){
            logger.log(Level.SEVERE, "SQL error", e);
        }
    }
    static Server server;
    public static Connection getConnectionToDB() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:mem:shoppingDb","","");
        } catch (Exception exception){
            logger.log(Level.SEVERE, "No connection available!", exception);
        }
        logger.info("Connection created successfully");
        return connection;
    }

    public static void startDatabase() throws SQLException{
        server = Server.createTcpServer().start();
    }
    public static void stopDataBase(){
        server.stop();
    }

    public static void initializeDatabase(Connection conn) throws FileNotFoundException, SQLException {
        InputStream sqlScriptResourceAsStream = H2DataBaseConnection.class.getClassLoader().getResourceAsStream("initialize.sql");
        RunScript.execute(conn, new InputStreamReader(sqlScriptResourceAsStream));
    }
}
