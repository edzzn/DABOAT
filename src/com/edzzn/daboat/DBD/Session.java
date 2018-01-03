package com.edzzn.daboat.DBD;

import java.sql.*;

/**
 * Session
 *
 * Encargada de manejar la conección con la base de
 * datos. Es la única con acceso a la base de datos.
 *
 * Esta clase utiliza el patrón Singleton
 * */
public class Session {

    private String db_username;
    private String db_password;
    private String db_url;
    private String db_SysUsername;
    private String db_SysPassword;

    //Se crea un objeto Sesion
    private static final Session instance = new Session();

    // El constructor no puede ser instanciado
    private Session() {
        this.db_SysUsername = "system";
        this.db_SysPassword = "12345";
        this.db_url = "jdbc:oracle:thin:@localhost:1521:XE";
    }

    /**
     * Obtener una instancia de {@link Session}
     * */
    public static Session getInstance() {
        return instance;
    }

    /**
     * Actualiza las credenciales y se conecta con la base de datos.
     * Retorna un {@link Boolean} que permite comprobar si se realizo
     * la conexión.
     *
     * @param  username usuario sql
     * @param  password  contraseña de un usuario SQL
     * @return      true si se pudo conectar con la base de datos
     */
    public boolean connect(String username, String password) {
        this.db_username = username;
        this.db_password = password;

        Boolean connected = false;
        try {
            connected = validCredentials("jdbc:oracle:thin:@localhost:1521:XE", this.db_username, this.db_password);
        } catch (SQLException ignored) {

        }
        return connected;
    }

    /**
     * Método auxiliar que valida la conexión con
     * la base de datos.
     *
     * @param  url ubicación de la base de datos
     * @param  username usuario sql
     * @param  password  contraseña de un usuario SQL
     * @return      true si se pudo conectar con la base de datos
     */
    public static boolean validCredentials(String url, String username, String password) throws SQLException {
        Connection conn = DriverManager.getConnection(url, username, password);
        boolean reachable = false;// 10 sec
        try {
            reachable = conn.isValid(10);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reachable;
    }

    /**
     * Método ejecuta una consultaSQL y devuelve la
     * respuesta a la petición como un String.
     *
     * Para ejecutar la consulta es necesario de el usuario y contraseña de la base de datos.
     *
     * @param consultaSQL consulta a ser ejecutada
     * @param username  usuario sql que genera la consulta
     * @param password  contraseña del usuario que genera la consulta
     * @return String, respuesta de la consulta o la excepción
     */
    private String ejecutar(String consultaSQL, String username, String password) {
        try {
            // Carga la clase del Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            //Crea la conección con el objeto
            Connection connection = DriverManager.getConnection(db_url, username, password);

            //Crea el objeto Statement
            Statement stmt = connection.createStatement();

            //Ejectuta la query
            ResultSet rs = stmt.executeQuery(consultaSQL);

            // Obtener el numero de columnas
            int columnCount = rs.getMetaData().getColumnCount();
            StringBuilder response = new StringBuilder();

            // si existe un respuesta de oracle, se procesa y se crea el String de respuesta
            if (columnCount != 0) {
                while (rs.next()) {
                    if (rs.getObject(1) != null) {
                        for (int i = 1; i <= columnCount; i++) {
                            response.append(rs.getObject(i));
                            if (i != columnCount) {
                                response.append("\t");
                            }
                        }
                        response.append("\n");
                    }
                }
                response = new StringBuilder(response.substring(0, response.length() - 1));
            }

            //Cierra la conección con el objeto
            connection.close();
            return response.toString();

        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * Método permite basado en{@link #ejecutar(String, String, String)}, al
     * no enviar los parámetros de usuario y contraseña estos se recuperan de
     * Session
     *
     * @param consultaSQL consultas sql
     * @return respuesta de la consulta o la excepción
     * @see Session#ejecutar(String, String, String)
     */
    public String ejecutar(String consultaSQL) {
        return ejecutar(consultaSQL, this.db_username, this.db_password);
    }

    /**
     * Método permite basado en{@link #ejecutar(String, String, String)}, Este
     * método ejecuta la consultaSQL con las credenciales de System
     *
     * @param consultaSQL consultas sql
     * @return respuesta de la consulta o la excepción
     * @see Session#ejecutar(String, String, String)
     */
    public String ejecutarAsSysAdmin(String consultaSQL) {
        return ejecutar(consultaSQL, db_SysUsername, db_SysPassword);
    }

    /**
     * Método similar a {@link Session#ejecutarAsSysAdmin(String)} basado en  basado en{@link #ejecutar(String, String, String)}
     *
     * Este método ejecuta la un array de consultas
     *
     * @param consultasSQL Lista de consultas sql
     * @return respuesta de la consulta o la excepción
     * @see Session#ejecutar(String, String, String)
     */
    public String ejecutarAsSysAdmin(String[] consultasSQL) {
        StringBuilder response = new StringBuilder();
        for (String consultaSQL : consultasSQL) {
            response.append(ejecutarAsSysAdmin(consultaSQL));
        }
        return response.toString();
    }

    public String getDb_url() {
        return db_url;
    }

    public void setDb_url(String db_url) {
        this.db_url = db_url;
    }

    public String getDb_SysUsername() {
        return db_SysUsername;
    }

    public void setDb_SysUsername(String db_SysUsername) {
        this.db_SysUsername = db_SysUsername;
    }

    public String getDb_SysPassword() {
        return db_SysPassword;
    }

    public void setDb_SysPassword(String db_SysPassword) {
        this.db_SysPassword = db_SysPassword;
    }
}
