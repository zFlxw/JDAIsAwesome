package com.github.zflxw.sql;

import com.github.zflxw.JDAIsAwesome;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SQLManager {
    SQLite sqlite = JDAIsAwesome.getInstance().getSQLite();

    public void createTable() {
        // In dieser Methode werden wir später die Tabellen innerhalb der Datenbank erstellen, die wir für bestimmte Befehle oder andere System brauchen
        // Hiermit wird eine neue Tabelle mit dem namen Blacklist erstellt.
        sqlite.update("CREATE TABLE IF NOT EXISTS Blacklist (WORD VARCHAR(100) NOT NULL);");
    }

    // Mit dieser Methode können wir Wörter zu der Blacklist hinzufügen
    public void addBlacklistedWords(String[] words) {
        // Da wir die Wörter in einem Array übergeben, wird hier das gesamte Array durchgegangen
        for (String word : words) {
            // Hier wird abgefragt, ob dieses Wort noch nicht auf der Blacklist steht und nur wenn es noch nicht vorhanden ist, wird es hinzugefügt
            if (!getBlacklistedWords().contains(word))
                // Dieser SQL Befehl fügt das aktuelle Wort zur Datenbank hinzu
                sqlite.update("INSERT INTO Blacklist (WORD) VALUES ('"+word+"');");
        }
    }

    // Mit dieser Methode können wir Wörter von der Blacklist entfernen
    public void removeBlacklistedWords(String[] words) {
        // Das gleiche wie bei der add Methode. Das Array wird durchgegangen nur mit dem Unterschied, dass hier das Wort nur entfernt wird, wenn es auf der Liste steht
        for (String word : words) {
            if (getBlacklistedWords().contains(word))
                sqlite.update("DELETE FROM Blacklist WHERE WORD='"+word+"';");
        }
    }

    // Diese Methode gibt alle Wörter auf der Blacklist in einem HashSet zurück. (Ein HashSet ist eine Collection in der ein Wert nicht zweimal vorkommen kann, was bei uns der Fall ist)
    public Set<String> getBlacklistedWords() {
        // Hier wird ein neues HashSet erstellt
        Set<String> set = new HashSet<>();
        // Hier wird ein ResultSet erstellt, dass die Blacklist durchgeht
        ResultSet resultSet = JDAIsAwesome.getInstance().getSQLite().getQuery("SELECT * FROM Blacklist");
        try {
            // Solange ein weiterer Eintrag existiert, wird dieser zu dem Set hinzugefügt
            while (resultSet.next()) {
                set.add(resultSet.getString("WORD"));
            }
            // Das ResultSet wird wieder geschlossen
            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        // Das fertige ResultSet wird zurückgegeben
        return set;
    }
}
