package com.github.zflxw.manager;

import com.github.zflxw.commands.BlacklistCommand;
import com.github.zflxw.commands.PrefixCommand;
import com.github.zflxw.commands.SayCommand;
import com.github.zflxw.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.concurrent.ConcurrentHashMap;

// Eine öffentliche Klasse
public class CommandManager {
    /* Dies ist eine ConcurrentHashMap. Auch hier gehe ich nicht genauer auf den Unterschied zu einer "normalen" HashMap ein. Der Haupgrund ist, dass hier
       der Key und die Value von "natur" aus miteinander synchronisiert sind.*/
    public ConcurrentHashMap<String, ServerCommand> commands;

    // Ein Konstrukter vom CommandManager. Hier werden die Befehle später eingetragen
    public CommandManager() {
        this.commands = new ConcurrentHashMap<>();

        // Hier fügst du neue Befehle hinzu. Dazu sage ich mehr, wenn wir zu einem Befehl kommen
        this.commands.put("say", new SayCommand());
        this.commands.put("blacklist", new BlacklistCommand());
        this.commands.put("prefix", new PrefixCommand());
    }

    // Diese Methode überprüft, ob es dein Befehl den ein Spieler eingibt auch wirklich gibt und führt diesen dann aus
    public boolean perform(String command, Member member, MessageChannel channel, Message message, String[] args) {
        ServerCommand serverCommand;
        if ((serverCommand = this.commands.get(command.toLowerCase())) != null) {
            serverCommand.performCommand(member, channel, message, args);
            return true;
        }

        return false;
    }
}
