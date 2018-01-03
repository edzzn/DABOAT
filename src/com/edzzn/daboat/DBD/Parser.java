package com.edzzn.daboat.DBD;

import com.edzzn.daboat.LOGIC.Helper;
import com.edzzn.daboat.LOGIC.ObjetoTabla;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Parser
 * La clase Parser es la encargada en comunicarse
 * con {@link Session}. La clase Parser se encarga
 * principalmente en el manejo de los objetos {@link ObjetoTabla}
 * y su comunicación con la Base de datos
 * */
public class Parser {

    public Parser() {
    }

    // Conección con la clase Session, permite el acceso a la base de datos
    public final Session conn = Session.getInstance();

    /**
     * Este método toma un objeto de tipo ObjetoTabla y lo descompone
     * creando la consulta SQL que será utilizada para insertar
     * el objeto en la base de datos.
     *
     * @param  objetoTabla  objeto que será guardado en lase de datos
     * @return      retorna la consulta realizada o la excepción lanzada
     * @see         ObjetoTabla
     */
    public String saveObject(ObjetoTabla objetoTabla) {
        String tableName = objetoTabla.getClass().getSimpleName();
        String sqlQuery = "INSERT INTO " + tableName + "(";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        StringBuilder sqlColumnas = new StringBuilder();
        String[] columnas = Helper.getColumns(tableName);

        StringBuilder values = new StringBuilder();

        for (int i = 0; i < columnas.length; i++) {
            sqlColumnas.append(columnas[i]);
            // Existe una distinción si el objeto es una fecha (LocalDateTime)
            if (objetoTabla.getMethodReturnClass(columnas[i].toLowerCase()).equals(LocalDateTime.class)) {
                values.append("to_date('").append(LocalDateTime.class.cast(objetoTabla.genericGetter(columnas[i].toLowerCase())).format(formatter)).append("','YYYY-MM-DD')");
            } else {
                values.append("'").append(objetoTabla.genericGetter(columnas[i].toLowerCase())).append("'");
            }
            if (i != columnas.length - 1) {
                sqlColumnas.append(", ");
                values.append(", ");
            }
        }
        sqlQuery += sqlColumnas + ") VALUES (" + values + ")";

        String ejecutarResponse = conn.ejecutar(sqlQuery);

        // Se obtiene el resultado de la petición, Si este esta vacio, entonces no existió ningun error
        if (ejecutarResponse.equalsIgnoreCase("")) {
            return sqlQuery;
        } else {
            return ejecutarResponse;
        }
    }

    /**
     * Este método toma un objeto de tipo ObjetoTabla y lo descompone
     * y actualiza su registro en la base de datos.
     * El objectID debe ser un identificar único del objeto segun su
     * representación en la base de datos.
     *
     * @param  objetoTabla  objeto que reemplazara al que se encuentra en la DB
     * @param  objetoID identificar del objeto a actualizar
     * @return      retorna la consulta realizada o la excepción lanzada
     * @see         ObjetoTabla
     */
    public String updateObject(ObjetoTabla objetoTabla, Object objetoID) {
        // update by id of the object.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String tableName = objetoTabla.getClass().getSimpleName().toUpperCase();

        String sqlQuery = "UPDATE " + tableName + "\nSET ";
        StringBuilder values = new StringBuilder();
        String condition = "\n" + whereObjectID(objetoTabla, objetoID);
        String[] columnas = Helper.getColumns(tableName);


        for (int i = 1; i < columnas.length; i++) {
            values.append(columnas[i]).append(" = ");
            // Existe una distinción si el objeto es una fecha (LocalDateTime)
            if (objetoTabla.getMethodReturnClass(columnas[i].toLowerCase()).equals(LocalDateTime.class)) {
                values.append("to_date('").append(LocalDateTime.class.cast(objetoTabla.genericGetter(columnas[i].toLowerCase())).format(formatter)).append("','YYYY-MM-DD')");
            } else {
                values.append("'").append(objetoTabla.genericGetter(columnas[i].toLowerCase())).append("'");
            }
            if (i != columnas.length - 1) {
                values.append(", ");
            }
        }
        sqlQuery += values + condition;

        String ejecutarResponse = conn.ejecutar(sqlQuery);

        // Se obtiene el resultado de la petición, Si este esta vacio, entonces no existió ningun error
        if (ejecutarResponse.equalsIgnoreCase("")) {
            return sqlQuery;
        } else {
            return ejecutarResponse;
        }

    }

    /**
     * Método similar a {@link Parser#updateObject(ObjetoTabla, Object)}. La diferencia
     * principal es que este método no neceista del objectID ya que este
     * será recuperado del propio objeto.
     *
     * @param  objetoTabla objeto que reemplazara al que se encuentra en la DB
     * @return      retorna la consulta realizada o la excepción lanzada
     * @see         Parser#updateObject(ObjetoTabla, Object)
     */
    public String updateObject(ObjetoTabla objetoTabla) {
        return updateObject(objetoTabla, objetoTabla.getObjectID());
    }

    /**
     * Este método permite eliminar la representación de un
     * objeto {@link ObjetoTabla}.
     *
     * @param  objetoTabla  objeto a ser eliminado de la base de datos
     * @return      retorna la consulta realizada o la excepción lanzada
     * @see         ObjetoTabla
     */
    public String deleteObject(ObjetoTabla objetoTabla) {
        // Delete object by ID, Any object of can delete objects from its own class.
        String tableName = objetoTabla.getClass().getSimpleName().toUpperCase();

        String sqlQuery = "DELETE FROM  " + tableName + "\n";
        String condition = "\n" + whereObjectID(objetoTabla);
        sqlQuery += condition;
        String ejecutarResponse = conn.ejecutar(sqlQuery);

        // Se obtiene el resultado de la petición, Si este esta vacio, entonces no existió ningun error
        if (ejecutarResponse.equalsIgnoreCase("")) {
            return sqlQuery;
        } else {
            return ejecutarResponse;
        }
    }

    /**
     * Este método permite cargar un el objeto de la base da datos, dado a si
     * mismo como parámetro.
     *
     * @param  objetoTabla  objeto para obtener los parámetros de carga
     * @return      retorna la consulta realizada o la excepción lanzada
     * @see         ObjetoTabla
     */
    public Object loadObject(ObjetoTabla objetoTabla) {
        String tableName = objetoTabla.getClass().getSimpleName().toUpperCase();
        String sqlQuery = "SELECT * FROM " + tableName;

        // Se declaran las columnas aqui para evitar que se declaren en BuildObject, recude en gran medida
        // las llamadas a la base de datos por ende mejorando el rendimiento
        String[] columnas = Helper.getColumns(tableName);
        String condition = "\n" + whereObjectID(objetoTabla);
        sqlQuery += condition;

        String result = conn.ejecutar(sqlQuery);
        String[] argvs = result.split("\n")[0].split("\t");
        for (int i = 0; i < argvs.length; i++) {
            if (argvs[i].equalsIgnoreCase("null")) {
                argvs[i] = "0";
            }
        }

        return Helper.buildObject(tableName, argvs, columnas);
    }

    /**
     * Método basado en {@link Parser#whereObjectID(ObjetoTabla, Object)}.
     * El parámetro objectID es obtenido a partir del objetoTabla.
     *
     * @param  objetoTabla  an absolute URL giving the base location of the image
     * @return      retorna la consulta realizada o la excepción lanzada
     * @see         Parser#whereObjectID(ObjetoTabla, Object)
     * @see         ObjetoTabla#getObjectID()
     */
    private String whereObjectID(ObjetoTabla objetoTabla) {
        return whereObjectID(objetoTabla, objetoTabla.getObjectID());
    }

    /**
     * Construye la condición WHERE objectId = 'value'. a partir de un
     * objeto tipo {@link ObjetoTabla} y su objectID.
     *
     * @param  objetoTabla objeto del cual se basa la busqueda.
     * @param  objectID  parametro o multiples parámetros
     * @return      retorna la consulta realizada o la excepción lanzada
     */
    private String whereObjectID(ObjetoTabla objetoTabla, Object objectID) {
        String tableName = objetoTabla.getClass().getSimpleName().toUpperCase();
        StringBuilder condition = new StringBuilder();
        String[] columnas = Helper.getColumns(tableName);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
        DateTimeFormatter formatterSQL = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Los objetos ingresados pueden ser String[] o String
        // Si el objeto es un String{] se los recorre
        if (objectID.getClass().equals(String[].class)) {
            String[] ids = (String[]) objectID;
            for (int i = 0; i < ids.length; i++) {
                if (objetoTabla.getMethodReturnClass(columnas[i].toLowerCase()).equals(LocalDateTime.class)) {
                    LocalDateTime dateTime = LocalDateTime.parse(ids[i], formatter);
                    condition.append(columnas[i]).append(" = ").append("to_date('").append(dateTime.format(formatterSQL)).append("','YYYY-MM-DD')");
                } else {
                    condition.append(columnas[i]).append(" = '").append(ids[i]).append("'");
                }

                if (i != ids.length - 1) {
                    condition.append("\nAND ");
                }
            }
        } else {
            // Si el objectID es String, entonces solo escribimos la sentencia.
            String id = objectID.toString();
            if (objetoTabla.getMethodReturnClass(columnas[0].toLowerCase()).equals(LocalDateTime.class)) {
                // Si la id primaria es una fecha.
                LocalDateTime dateTime = LocalDateTime.parse(id, formatter);
                condition.append(columnas[0]).append(" = ").append("to_date('").append(dateTime.format(formatterSQL)).append("','YYYY-MM-DD')");
            } else {
                condition.append(columnas[0]).append(" = '").append(id).append("'");
            }
        }
        return "WHERE " + condition;
    }
}