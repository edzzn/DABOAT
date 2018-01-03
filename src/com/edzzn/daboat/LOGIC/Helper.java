package com.edzzn.daboat.LOGIC;

import com.edzzn.daboat.DBD.Parser;
import com.edzzn.daboat.DBD.Session;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Clase HELPER
 *
 * HELPER proporciona funcionalidad para
 * realizar diversas tareas de consultas
 *
 */
public class Helper {

    private final static Parser parser = new Parser();

    /**
     * Este método retorona un array de objetos construidos a partir de
     * el nombre de la tabla.  Retorna todos los objetos de la tabla.
     *
     * @param  tableName repuesta de una consulta sql
     * @return ArrayList<ObjetoTabla> array de objetos
     * @see #objetosArray(String, String)
     */
    public static ArrayList<ObjetoTabla> returnAllObject(String tableName) {
        String sqlQuery = "SELECT * FROM " + tableName;
        String result = parser.conn.ejecutar(sqlQuery);

        return objetosArray(result, tableName);
    }

    /**
     * Este método retorona un array de objetos construidos a partir de
     * una petición a la base de datos y el nombre de la tabla
     *
     * @param  tableName repuesta de una consulta sql
     * @param consulta nombre de la tabla
     * @return ArrayList<ObjetoTabla> array de objetos
     * @see #objetosArray(String, String)
     */
    public static ArrayList<ObjetoTabla> returnAllObject(String tableName, String consulta) {
        return objetosArray(parser.conn.ejecutar(consulta), tableName);
    }

    /**
     * Este método retorona un array de objetos construidos a partir de
     * la respuesta de una petición a la base de datos y el nombre de la tabla
     *
     * @param  respuestaConsulta repuesta de una consulta sql
     * @param tableName nombre de la tabla
     * @return ArrayList<ObjetoTabla> array de objetos
     * @see     ObjetoTabla
     * @see #buildObject(String, String[], String[])
     */
    private static ArrayList<ObjetoTabla> objetosArray(String respuestaConsulta, String tableName) {
        ArrayList<ObjetoTabla> array = new ArrayList<>();

        // Se declaran las columnas aqui para evitar que se declaren en BuildObject, recude en gran medida
        // las llamadas a la base de datos por ende mejorando el rendimiento
        String[] columnas = Helper.getColumns(tableName);

        String[] objetos = respuestaConsulta.split("\n");
        for (String objeto : objetos) {
            String[] argvs = objeto.split("\t");
            for (int j = 0; j < argvs.length; j++) {
                if (argvs[j].equalsIgnoreCase("null")) {
                    argvs[j] = "0";
                }
            }
            array.add((ObjetoTabla) buildObject(tableName, argvs, columnas));
        }
        return array;
    }

    /**
     * El método parametriza la entrada de objeto, y utilizando
     * el nombre de la tabla obtiene la clase e instancia
     * un objeto con los valores enviados.
     *
     * @param  tableName nombre de la tabla
     * @param arguments valores para construir el objeto
     * @param columnas listado de columnas de la tabla
     * @return Object instanciado
     */
    public static Object buildObject(String tableName, String[] arguments, String[] columnas){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");

    try {
        Class clazz = Class.forName(buildClassName(tableName));
        Object[] input = new Object[columnas.length];
        Class[] inputParametersTypes = getConstuctorParameters(clazz, columnas.length);
        Constructor constructor = clazz.getConstructor(inputParametersTypes);

        for(int i=0; i<columnas.length; i++){
            if (inputParametersTypes[i].equals(String.class)) {
                input[i] = arguments[i];
            } else if (inputParametersTypes[i].equals(Integer.class)) {
                input[i] = Integer.parseInt(arguments[i]);

            } else if (inputParametersTypes[i].equals(Double.class)) {
                input[i] = Double.parseDouble(arguments[i]);
            } else if (inputParametersTypes[i].equals(LocalDateTime.class)) {
                input[i] =  LocalDateTime.parse(arguments[i], formatter);
            } else {
                throw new IllegalArgumentException("Unknown Class: ");
            }
        }
        return clazz.cast(constructor.newInstance(input));

    } catch (Exception e){}
    return null;
}

    /**
     * Método auxiliar que a partir del nombre de la tabla
     * construye el nombre de la clase a la que pertenece.
     *
     * @param  tabledName nombre de la tabla
     * @return String, nombre de la clase
     */
    private static String buildClassName(String tabledName){
        int index = tabledName.indexOf("_");
        if(index>=0){
            return "com.edzzn.daboat.LOGIC." + tabledName.substring(0,1).toUpperCase() + tabledName.substring(1,index+1).toLowerCase() +  tabledName.substring(index+1,index+2).toUpperCase() + tabledName.substring(index+2).toLowerCase();

        } else {
            return "com.edzzn.daboat.LOGIC." + tabledName.substring(0,1).toUpperCase() + tabledName.substring(1).toLowerCase();
        }
    }

    /**
     * Retorna un listado de los tipos de parámetros que
     * requiere el constructor de cierta clase
     *
     * @param clazz clase del objeto dueño del constructor
     * @param numParameters número de parámetros del constructor
     * @return Class[], Tipos de parámetros
     */
    private static Class[] getConstuctorParameters(Class clazz, int numParameters) throws Exception {
        // Puede encontar errores si se tiene dos constructores con el mismo número de parámetros
        Constructor[] constructors = clazz.getConstructors();

        for (Constructor constructor: constructors){
            Class[] parameterClasses = constructor.getParameterTypes();
            if(parameterClasses.length == numParameters){
                return parameterClasses;
            }

        }
        throw new Exception("No se encontró el constructor para la clase " + clazz.getName());

    }

    /**
     * Retorna todas las tablas a las que tiene acceso
     * el usuario conectado a las {@link Session}
     *
     * @return String[], lista de los privilegios
     */
    public static String[] getTables() {
        return parser.conn.ejecutar("SELECT TABLE_NAME FROM USER_TABLES").split("\n");
    }


    /**
     * Método auxiliar que permite obtener el listado
     * de columnas de una tabla específica.
     *
     * @param tableName nombre de la tabla
     * @return String[], listado de columnas
     */
    public static String[] getColumns(String tableName) {
        String consulta = "SELECT column_name FROM user_tab_cols WHERE table_name='" + tableName.toUpperCase() + "'";
        return parser.conn.ejecutar(consulta).split("\n");
    }

    /**
     * Retorna una lista de los usuarios de la base de
     * datos.
     *
     * @return String[], lista de usuarios de la DB
     */
    public static String[] getUsers() {
        String consulta = "SELECT username FROM dba_users";
        return parser.conn.ejecutarAsSysAdmin(consulta).split("\n");
    }

    /**
     * Retorna todos los privilegios concedidos a un usuario
     * sql específico
     *
     * @param username nombre de usuario sql
     * @return String[], lista de los privilegios
     */
    public static String[] getUsersPrivileges(String username) {
        String consulta = "SELECT privilege FROM DBA_SYS_PRIVS WHERE grantee='" + username + "'";
        return parser.conn.ejecutarAsSysAdmin(consulta).split("\n");
    }

    /**
     * Retorna todos los privilegios disponibles para
     * un usuario de la base de datos     *
     *
     * @return String[], lista de los privilegios
     */
    public static String[] getPrivileges() {
        String consulta = "SELECT name FROM system_privilege_map";
        return parser.conn.ejecutarAsSysAdmin(consulta).split("\n");
    }

    /**
     * Este método permite pasar una lista de consultas a {@link Session#ejecutarAsSysAdmin(String)}
     *
     * @param sqlQueries lista de consultas sql
     * @return String, el resultado de la consulta o la consulta enviada
     * @see Session#ejecutarAsSysAdmin(String)
     */
    public static String ejecutarMultiples(String[] sqlQueries) {
        return parser.conn.ejecutarAsSysAdmin(sqlQueries);
    }

}
