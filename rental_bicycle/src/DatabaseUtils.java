package utils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class DatabaseUtils {

    private static final String PG_DUMP_PATH = "C:\\Program Files\\PostgreSQL\\16\\bin\\pg_dump.exe";
    private static final String PSQL_PATH = "C:\\Program Files\\PostgreSQL\\16\\bin\\psql.exe";
    private static final String DB_NAME = "Gaming_Leagues";
    private static final String DB_USER = "developer";
    private static final String DB_PASSWORD = "23100132";

    public static void realizarCopiasDeSeguridad(Component parent) {
        File backupDir = new File("backups/");
        if (!backupDir.exists()) backupDir.mkdir();

        String backupFile = "backups/respaldodb_" + System.currentTimeMillis() + ".sql";

        List<String> command = Arrays.asList(
                PG_DUMP_PATH,
                "-U", DB_USER,
                "-h", "localhost",
                "-d", DB_NAME,
                "-f", backupFile
        );

        ejecutarProceso(command, parent, "Copia de seguridad creada exitosamente: " + backupFile,
                "Error al crear la copia de seguridad.");
    }

    public static void restaurarBaseDeDatos(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo de respaldo");
        if (fileChooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        File backupFile = fileChooser.getSelectedFile();

        List<String> command = Arrays.asList(
                PSQL_PATH,
                "-U", DB_USER,
                "-h", "localhost",
                "-d", DB_NAME,
                "-f", backupFile.getAbsolutePath()
        );

        ejecutarProceso(command, parent, "Base de datos restaurada exitosamente.",
                "Error al restaurar la base de datos.");
    }

    private static void ejecutarProceso(List<String> command, Component parent, String successMessage, String errorMessage) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.environment().put("PGPASSWORD", DB_PASSWORD);
            builder.redirectErrorStream(true);

            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                JOptionPane.showMessageDialog(parent, successMessage, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, errorMessage + "\nCódigo de salida: " + exitCode + "\n" + output, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(parent, errorMessage + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
