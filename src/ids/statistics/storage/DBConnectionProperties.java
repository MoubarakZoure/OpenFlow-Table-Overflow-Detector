/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ids.statistics.storage;

/**
 *
 * @author Moubarak
 */
public class DBConnectionProperties {

    private String host = "localhost";
    private int port = 3306;
    private String schema = "ids_db";
    private String user = "root";
    private String password = "root";
    private int MaxPooledStatements = 5000;

    public DBConnectionProperties() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxPooledStatements() {
        return MaxPooledStatements;
    }

    public void setMaxPooledStatements(int MaxPooledStatements) {
        this.MaxPooledStatements = MaxPooledStatements;
    }

    @Override
    public String toString() {
        return "DBConnectionProperties{" + "host=" + host + ", port=" + port + ", schema=" + schema + ", user=" + user + ", password=" + password + ", MaxPooledStatements=" + MaxPooledStatements + '}';
    }

   

    
    

   
    

   

   

}
