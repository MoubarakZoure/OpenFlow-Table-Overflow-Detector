/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ids.statistics.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;
import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 *
 * @author Moubarak
 */
public class MySqlPoolableObjectFactory extends BasePoolableObjectFactory {

    private Properties properties;
    private static final Logger LOG = Logger.getLogger(MySqlPoolableObjectFactory.class.getName());
    private DBConnectionProperties dbConnectionProperties = new DBConnectionProperties();

    public MySqlPoolableObjectFactory(DBConnectionProperties dbConnectionProperties) {
        this.dbConnectionProperties=dbConnectionProperties;
    }
    

    @Override
    public Object makeObject() throws Exception {
        Connection connection = null;

        String url = "jdbc:mysql://" + dbConnectionProperties.getHost() + ":" + dbConnectionProperties.getPort() + "/"
                + dbConnectionProperties.getSchema() + "?autoReconnectForPools=true";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, getProperties());

            if (connection != null) {
                connection.setAutoCommit(true);

               // LOG.log(Level.INFO, "Connexion \u00e0 la base de donn\u00e9es {0}cr\u00e9e", url);

                return connection;
            } else {
               // LOG.log(Level.INFO, "Connexion \u00e0 la base de donn\u00e9es {0} non cr\u00e9e", url);
                return null;
            }

        } catch (SQLException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", dbConnectionProperties.getUser());
            properties.setProperty("password", dbConnectionProperties.getPassword());
            properties.setProperty("MaxPooledStatements", dbConnectionProperties.getMaxPooledStatements() + "");
        }
        return properties;
    }

    public DBConnectionProperties getDbConnectionProperties() {
        return dbConnectionProperties;
    }

    public void setDbConnectionProperties(DBConnectionProperties dbConnectionProperties) {
        this.dbConnectionProperties = dbConnectionProperties;
    }

    public static Logger getLOG() {
        return LOG;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
