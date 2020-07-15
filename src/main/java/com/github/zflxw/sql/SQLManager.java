package com.github.zflxw.sql;

import com.github.zflxw.JDAIsAwesome;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SQLManager {
    SQLite sqlite = JDAIsAwesome.getInstance().getSQLite();

    public void createTable() {
        /* v1.0.0 - In dieser Methode werden wir später die Tabellen innerhalb der Datenbank erstellen, die wir für bestimmte Befehle oder andere System brauchen
         * Hiermit wird eine neue Tabelle mit dem namen Blacklist erstellt.
         *
         * v1.0.1 - Hinzufügen der Guild Abhängigkeit / Keine globale Blacklist
         */
        sqlite.update("CREATE TABLE IF NOT EXISTS Blacklist (WORD VARCHAR(100) NOT NULL, GUILD_ID VARCHAR(100) NOT NULL);");
        sqlite.update("CREATE TABLE IF NOT EXISTS Prefixes (PREFIX VARCHAR(10), GUILD_ID VARCHAR(100));");
    }

    /* v1.0.0 - Hinzufügen der Methode addBlacklistWords
     * v1.0.1 - Hinzufügen des "GuildID" Parameters
     */
    public void addBlacklistedWords(String[] words, long guildID) {
        // Da wir die Wörter in einem Array übergeben, wird hier das gesamte Array durchgegangen
        for (String word : words) {
            /* v1.0.0 - Hier wird abgefragt, ob dieses Wort noch nicht auf der Blacklist steht und nur wenn es noch nicht vorhanden ist, wird es hinzugefügt
             * v1.0.1 - Angeben des "GuildID" Parameters
             */
            if (!getBlacklistedWords(guildID).contains(word))
                // Dieser SQL Befehl fügt das aktuelle Wort zur Datenbank hinzu. Bei der GuildID werden keine Hochkommata benötigt, da dieser Wert eine Zahl ist
                sqlite.update("INSERT INTO Blacklist (WORD, GUILD_ID) VALUES ('"+word+"',+"+guildID+");");
        }
    }

    /* v1.0.0 - Hinzufügen der Methode removeBlacklistedWords
     * v1.0.1 - Hinzufügen des "GuildID" Parameters
     */
    public void removeBlacklistedWords(String[] words, long guildID) {
        // Das gleiche wie bei der add Methode. Das Array wird durchgegangen nur mit dem Unterschied, dass hier das Wort nur entfernt wird, wenn es auf der Liste steht
        for (String word : words) {
            // Angeben des Paramters der GuildID
            if (getBlacklistedWords(guildID).contains(word))
                sqlite.update("DELETE FROM Blacklist WHERE WORD='"+word+"';");
        }
    }

    // Als Parameter brauchen wir hier die ID der Guilde
    public boolean isPrefixSet(long guildID) {
        // Erstelle ein neues ResultSet. Hier werden alle Results gespeichert, die der ID der Guilde entsprechen, die angegeben wurde
        ResultSet resultSet = sqlite.getQuery("SELECT * FROM Prefixes WHERE GUILD_ID="+guildID+";");
        try {
            // Hier wird abgefragt, ob es einen Eintrag gibt
            if (resultSet.next()) {
                // Falls es einen Prefix gibt, wird true zurückgegeben
                return true;
            }
            // Das ResultSet wird geschlossen
            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        // Falls kein Prefix gefunden wurde, wird false zurückgegeben
        return false;
    }

    // Als Parameter brauchen wir die ID der Guilde
    public String getPrefix(long guildID) {
        // Erstelle ein neues ResultSet. Hier werden alle Results gespeichert, die der ID der Guilde entsprechen, die angegeben wurde
        ResultSet resultSet = sqlite.getQuery("SELECT * FROM Prefixes WHERE GUILD_ID="+guildID+";");
        try {
            // Hier wird abgefragt, ob es einen Eintrag gibt
            if (resultSet.next()) {
                // Hier wird der Eintrag zurückgegeben, sonfern es einen gibt
                return resultSet.getString("PREFIX");
            }
            // Das ResultSet wird geschlossen
            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        // Falls kein Prefix gefunden wurde, wird der Standard Prefix des Bots zurückgegeben.
        return JDAIsAwesome.getInstance().DEFAULT_PREFIX;
    }

    // Als Parameter werden der Prefix und die ID der Guilde übergeben
    public void setPrefix(String prefix, long guildID) {
        // Frage ab, ob bereits ein custom Prefix gesetzt ist
        if (isPrefixSet(guildID))
            // Wenn einer gesetzt ist, wird dieser aktualisiert
            sqlite.update("UPDATE Prefixes SET PREFIX='"+prefix+"' WHERE GUILD_ID="+guildID+";");
        else
            // Wenn keiner gesetzt ist, wird ein neuer eingetragem
            sqlite.update("INSERT INTO Prefixes (PREFIX, GUILD_ID) VALUES ('"+prefix+"',"+guildID+");");
    }

    /* v1.0.0 - Diese Methode gibt alle Wörter auf der Blacklist in einem HashSet zurück. (Ein HashSet ist eine Collection in der ein Wert nicht zweimal vorkommen kann, was bei uns der Fall ist)
     * v1.0.1 - Hinzufügen des "GuildID" Parameters
     */
    public Set<String> getBlacklistedWords(long guildID) {
        // Hier wird ein neues HashSet erstellt
        Set<String> set = new HashSet<>();
        // Hier wird ein ResultSet erstellt, dass die Blacklist durchgeht
        ResultSet resultSet = JDAIsAwesome.getInstance().getSQLite().getQuery("SELECT * FROM Blacklist WHERE GUILD_ID="+guildID+";");
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
