package com.github.zflxw.commands;

import com.github.zflxw.JDAIsAwesome;
import com.github.zflxw.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

// Klasse mit dem ServerCommand implementiert
public class SayCommand implements ServerCommand {
    // Methode zum ausführen des Befehls
    @Override
    public void performCommand(Member member, MessageChannel channel, Message message, String[] args) {
        // Hier wird abgefragt, ob der Member der den Befehl gesendet hat, die Rechte dazu hat, Nachrichten zu verwalten. Hier könnt ihr natürlich wählen, was ihr möchtet
        if (member.hasPermission(Permission.MESSAGE_MANAGE)) {
            // Hier wird abgefragt, ob der Member nicht der Bot selber ist (um Fehler zu vermeiden). Dazu habe ich die BotID als String in der Main gespeichert.
            if (member.getUser() != member.getJDA().getUserById(JDAIsAwesome.getInstance().BOT_ID)) {
                // >say <message> - Dieser Kommentar dient nur zur verschaulichung, wie die Nachricht später aussehen soll.
                // Hier wird die Nachricht rausgespeichert, ohne das ">say " davor.
                String messageToSay = message.getContentDisplay().substring(">say ".length());
                // Hier sendet der Bot einfach nur die Nachricht, die der Member gesendet hat, nur ohne Befehl.
                channel.sendMessage(messageToSay).queue();
            }
        }
    }
}
