package com.edzzn.daboat.LOGIC;

import com.edzzn.daboat.DBD.Parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase Abstracta ObjetoTabla
 *
 * De esta clase heredan todos los objetos que podran ser manejados
 * por la base de datos.
 */
public abstract class ObjetoTabla {

    private final static Parser parser = new Parser();

    public ObjetoTabla() {
    }

    /**
     * Este método permite pasar el objeto a {@link Parser#saveObject(ObjetoTabla)}
     * @see Parser#saveObject(ObjetoTabla)
     */
    public String saveObject() {
        return parser.saveObject(this);
    }

    /**
     * Este método permite pasar el objeto a {@link Parser#saveObject(ObjetoTabla)}
     * @see Parser#updateObject(ObjetoTabla)
     */
    public String updateObject() {
        return parser.updateObject(this);
    }

    /**
     * Este método permite pasar el objeto a {@link Parser#deleteObject(ObjetoTabla)}
     * @see Parser#deleteObject(ObjetoTabla)
     */
    public String deleteObject() {
        return parser.deleteObject(this);
    }

    /**
     * Este método permite pasar el objeto a {@link Parser#loadObject(ObjetoTabla)}
     * @see Parser#loadObject(ObjetoTabla)
     */
    public Object loadObject() {
        return parser.loadObject(this);
    }

    /**
     * Método abstracto que debe ser implementado por todos los
     * objetos que heredan esta clase.
     *
     * El objeto que se retorna es/debe ser el identificar
     * utilizado en la tabla de la base de datos.
     */
    public abstract Object getObjectID();

    /**
     * Retorna un ObjetoTabla, un objeto generico que puede ser utilizado
     * cuando se necesita una representación de dicho objeto.
     * <p>
     * Esta método solamente trabaja con clases que heredan de TableObject.
     * El método no es generico, se debe sobreescribir el metodo para agregar mayor
     * funcionalidad.
     *
     * @param tableName nombre de la tabla que permite determinar la clase del objeto
     * @return objeto del tipo deseado
     * @see ObjetoTabla
     */
    public static ObjetoTabla loadGenericObject(String tableName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String strDateTime = "0001-01-01 00:00:00";
        ObjetoTabla loadedObject;
        switch (tableName) {
            case "REGIONS":
                loadedObject = new Regions(0, "");
                break;
            case "COUNTRIES":
                loadedObject = new Countries("", "", 0);
                break;
            case "LOCATIONS":
                loadedObject = new Locations(0, "", "", "", "", "");
                break;
            case "JOB_HISTORY":
                loadedObject = new Job_History(0, LocalDateTime.parse(strDateTime, formatter), LocalDateTime.parse(strDateTime, formatter), "", 1);
                break;
            case "JOBS":
                loadedObject = new Jobs("", "", 0, 0);
                break;
            case "EMPLOYEES":
                loadedObject = new Employees(0, "", "", "", "", LocalDateTime.parse(strDateTime, formatter), "", Double.parseDouble("0"), Double.parseDouble("0"), 1, 1);
                break;
            case "DEPARTMENTS":
                loadedObject = new Departments(0, "", 0, 0);
                break;
            default:
                throw new IllegalArgumentException("Nombre de tabla invalido: " + ", " + tableName);
        }
        return loadedObject;
    }

    /**
     * Este método invoca un setter de un objeto cualqueria, dato el
     * objeto, el nombre del campo afectado por el setter y el
     * parámetro de entrada que se dará a este método.
     * <p>
     * Generalizaciones: Para este metodo se da por sentado que el setter
     * esta definido siguiendo estandares: set + NombreCampo, en donde el
     * la pimera letra del nombre del campo esta en mayusculas.
     *
     * @param fieldName nombre del campo del objeto, permite obtener el nombre del método
     * @param input     input que será enviado en la llamada al setter
     */
    public void genericSetter(String fieldName, Object input) {
        String methodName = buildSetMethodname(fieldName);
        java.lang.reflect.Method method = null;
        try {
            method = this.getClass().getMethod(methodName, getParameterClass(fieldName));

        } catch (SecurityException | NoSuchMethodException ignored) {
        }


        try {
            method.invoke(this, getParameterClass(fieldName).cast(input));
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ignored) { }

    }

    /**
     * Este método invoca un getter de un objeto cualqueria, dato el
     * objeto, el nombre del campo afectado por el getter y el
     * parámetro de entrada que se dará a este método.
     * <p>
     * Generalizaciones: Para este metodo se da por sentado que el setter
     * esta definido siguiendo estandares: get + NombreCampo, en donde el
     * la pimera letra del nombre del campo esta en mayusculas.
     *
     * @param fieldName nombre del campo del objeto, permite obtener el nombre del método
     * @return objeto que es parametro del campo enviado
     */
    public Object genericGetter(String fieldName) {
        String methodName = buildGetMethodname(fieldName);
        java.lang.reflect.Method method = null;
        Object returnObject = null;

        try {
            method = this.getClass().getMethod(methodName);
        } catch (SecurityException | NoSuchMethodException ignored) {
        }
        try {
            returnObject = method.getReturnType().cast(method.invoke(this));
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ignored) {
        }
        return returnObject;

    }

    /**
     * Este método toma el nombre del campo de un objeto
     * o columna de una tabla y retorna el Typo {@link Class}
     * que retorna el getter del campo ingresado.
     *
     * @param fieldName nombre del campo del objeto
     * @return      Clase que retorna el método con el nombre de campo ingresado
     * */
    public Class getParameterClass(String fieldName) {
        String methodName = buildSetMethodname(fieldName);
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                Class<?>[] parametros = method.getParameterTypes();
                return parametros[0];
            }
        }
        return "".getClass();
    }

    /**
     * Este método toma el nombre del campo de un objeto
     * o columna de una tabla y retorna el Typo {@link Class}
     * que retorna el getter del campo ingresado.
     *
     * @param fieldName nombre del campo del objeto
     * @return      Clase que retorna el método con el nombre de campo ingresado
     * */
    public Class getMethodReturnClass(String fieldName) {
        String methodName = buildGetMethodname(fieldName);
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                return method.getReturnType();
            }
        }
        return "".getClass();
    }

    /**
     * Método auxilizar, construye el nombre del
     * método setter del campo envido.
     *
     * @param fieldName nombre del campo del objeto
     * @return      método getter del fieldName
     * */
    private String buildSetMethodname(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * Método auxilizar, construye el nombre del
     * método getter del campo envido.
     *
     * @param fieldName nombre del campo del objeto
     * @return      método getter del fieldName
     * */
    private String buildGetMethodname(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

}

