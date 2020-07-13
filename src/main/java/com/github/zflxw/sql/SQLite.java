package com.github.zflxw.sql;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLite {
    // Deklarierung der Connection und Statement Variable
    private Connection conn;
    private Statement statement;

    // In dieser Methode verbindet sich der Bot mit der Datenbank
    public void connect() {
        // Zu anfang wir die Connection auf null gesetzt
        conn = null;

        // Das try catch dient zum Abfangen von Fehlern
        try {
            // Hier wird eine Referenz zu der Datei der Datenbank erstellt
            File file = new File("database.db");
            // Hier wird überprüft, ob die Datei bereits existiert. Wenn nicht, wird diese erstellt.
            if (!file.exists()) {
                file.createNewFile();
            }

            // Dieser String beinhaltet die URL mit der sich der Bot zur Datenbank verbindet
            String url = "jdbc:sqlite:" + file.getPath();
            // Hier wird die Connection auf die entsprechende URL gebunden und verbunden.
            conn = DriverManager.getConnection(url);

            // Hier wird dem Statement einfach ein neuer Wert gegeben, bzw. ein neues Statement erstellt
            statement = conn.createStatement();

            // Hier noch eine Debug Nachricht in der Konsole, dass der Bot gestartet ist
            System.out.println("Verbindung zur Datenbank hergestellt!");
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }

    // In dieser Methode wird die Verbindung zwischen Bot und Datenbank wieder getrennt
    public void disconnect() {
        try {
            // Hier wird abgefragt, ob der Bot überhaupt mit der Datenbank verbunden ist. Nur wenn der Bot verbunden ist, wird die Verbindung getrennt
            if (conn != null) {
                conn.close();
                System.out.println("Verbindung zur Datenbank getrennt!");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    // Mit dieser Methode können wir Änderungen an der Datenbank vornehmen
    public void update(String sql) {
        try {
            statement.execute(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    // Mit dieser Methode können wir Informationen aus der Datenbnk auslesen
    public ResultSet getQuery(String sql) {
        try {
            return statement.executeQuery(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
