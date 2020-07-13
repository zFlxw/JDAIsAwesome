package com.github.zflxw.commands;

import com.github.zflxw.JDAIsAwesome;
import com.github.zflxw.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Set;

public class BlacklistCommand implements ServerCommand {
    @Override
    public void performCommand(Member member, MessageChannel channel, Message message) {
        // Frage ab, ob der Member Administratorrechte hat.
        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            // Frage ab, ob der Member nicht der Bot selbst ist
            if (member.getUser() != member.getJDA().getUserById(JDAIsAwesome.getInstance().BOT_ID)) {
                // >blacklist <add/remove> <word> - Den Befehl zur übersicht mit aufgeschrieben
                String[] args = message.getContentRaw().substring(">blacklist ".length()).split(" ");
                // Frage ab, ob es zwei Argumente oder mehr gibt
                if (args.length >= 2) {
                    // Frage ab, ob das erste Argument "add" ist
                    if (args[0].equalsIgnoreCase("add")) {
                        // Erstelle ein neues Array mit der Länge von args.length - 1
                        String[] words = new String[args.length-1];
                        // Kopiere die Argumente am der zweiten Position zu den Wörtern
                        System.arraycopy(args, 1, words, 0, args.length - 1);
                        // Führe die addBlacklistWords Methode aus
                        JDAIsAwesome.getInstance().getSQLManager().addBlacklistedWords(words);
                        // Sende eine abschließende Nachricht
                        channel.sendMessage("Die Wörter wurden zur Blacklist hinzugefügt, sofern sie nicht schon vorhanden waren").queue();
                    } // Frage ab, ob das erste Argument "remove" ist
                    else if (args[0].equalsIgnoreCase("remove")) {
                        // Hier passiert genau das gleiche wie bei add, außer dass dieses mal diese remove methode aufgerufen wird
                        String[] words = new String[args.length-1];
                        System.arraycopy(args, 1, words, 0, args.length - 1);
                        JDAIsAwesome.getInstance().getSQLManager().removeBlacklistedWords(words);
                        channel.sendMessage("Die Wörter wurden von der Blacklist entfernt, sofern sie vorhanden waren").queue();
                    } else {
                        channel.sendMessage("Bitte gebe ``>blacklist <add/remove> <word(s)>`` ein");
                    }
                } else if (args.length == 1) {
                    // Frage ab, ob das erste Argument "list" ist
                    if (args[0].equalsIgnoreCase("list")) {
                        // erstelle ein Set für die Wörter und kopiere alle Wörter auf der Blacklist darauf
                        Set<String> words = JDAIsAwesome.getInstance().getSQLManager().getBlacklistedWords();
                        channel.sendMessage("Blacklisted Words: ").queue();
                        // gebe alle wörter im Chat aus
                        for (String word : words) {
                            channel.sendMessage(word).queue();
                        }
                    } else {
                        channel.sendMessage("Bitte gebe ``>blacklist list`` ein");
                    }
                } else {
                    channel.sendMessage("Bitte gebe ``>blacklist <add/remove/list> (word(s))`` ein");
                }
            }
        }
    }
}
