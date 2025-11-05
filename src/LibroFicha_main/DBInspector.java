package LibroFicha_main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import LibroFicha_config.DatabaseConnection;

public class DBInspector {
    public static void main(String[] args) {

        // Paso 1: registrar el driver MySQL
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return; // si no encuentra el driver, terminamos
        }

        // Paso 2: abrir la conexión y listar tablas
        try (Connection conn = DatabaseConnection.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "%", new String[] {"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Tabla: " + tableName);

                ResultSet columns = meta.getColumns(null, null, tableName, "%");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String type = columns.getString("TYPE_NAME");
                    String isNullable = columns.getString("IS_NULLABLE");
                    System.out.println("  Columna: " + columnName + " | Tipo: " + type + " | Nullable: " + isNullable);
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
