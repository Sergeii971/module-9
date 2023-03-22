package com.os.course.task4;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The type Connection pool.
 *
 * @author Verbovskiy Sergei
 * @version 1.0
 */
public class ConnectionPool {

    private static final String PROPERTIES_FILENAME = "config\\database.properties";
    private static final String DRIVER_NAME = "db.driver";
    private static final String LOGIN = "db.login";
    private static final String PASSWORD = "db.password";
    private static final String URL = "db.url";
    private static final int POOL_SIZE = 12;
    private static final ConnectionPool pool = new ConnectionPool();
    private final BlockingQueue<ProxyConnection> freeConnections;
    private final Queue<ProxyConnection> givenAwayConnections;

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ConnectionPool getInstance() {
        return pool;
    }

    ConnectionPool() {
        try {
            Properties properties = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream(PROPERTIES_FILENAME);
            properties.load(inputStream);
            Class.forName(properties.getProperty(DRIVER_NAME));
            freeConnections = new LinkedBlockingQueue<>(POOL_SIZE);
            givenAwayConnections = new ArrayDeque<>(POOL_SIZE);
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection connection = DriverManager.getConnection(properties.getProperty(URL),
                        properties.getProperty(LOGIN), properties.getProperty(PASSWORD));
                freeConnections.offer(new ProxyConnection(connection));
            }
        } catch (ClassNotFoundException | SQLException | IOException e) {
            System.out.println("Error while connection pool creating " + e);
            throw new RuntimeException("Error during connect to database", e);
        }
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public Connection getConnection() {
        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            givenAwayConnections.offer(connection);
        } catch (InterruptedException e) {
            System.out.println( "Error while getting connection" + e);
        }
        return connection;
    }

    /**
     * Release connection.
     *
     * @param connection the connection
     */
    public void releaseConnection(Connection connection) {
        if ((connection instanceof ProxyConnection) && (givenAwayConnections.remove(connection))) {
            freeConnections.offer((ProxyConnection) connection);
        } else {
            System.out.println("Invalid connection to realizing");
        }
    }

    /**
     * Destroy pool.
     */
    public void destroyPool() {
        try {
            for (int i = 0; i < POOL_SIZE; i++) {
                freeConnections.take().reallyClose();
            }
            deregisterDrivers();
        } catch (SQLException | InterruptedException e) {
            System.out.println("Error while close connection pool" + e);
        }
    }

    private void deregisterDrivers() throws SQLException {
        while(DriverManager.getDrivers().hasMoreElements()){
            DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
        }
    }
}