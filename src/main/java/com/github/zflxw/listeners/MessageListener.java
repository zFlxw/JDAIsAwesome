package com.github.zflxw.listeners;

import com.github.zflxw.JDAIsAwesome;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

// Diese Klasse muss vom ListenerAdapter erben um auf die Events zugreifen zu können
public class MessageListener extends ListenerAdapter {
    // Diese Methode ist die bereits erwähne onMessageReceived Methode, die bei jeder gesendeten Nachricht ausgeführt wird
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Hier wird abgefragt, ob die Nachricht in einem TextChannel gesendet wurde und nicht in einer Direktnachricht
        if (event.isFromType(ChannelType.TEXT)) {
            // Hier werden als erstes mal sämtliche Variablen vom event rausgespeichert.
            Member member = event.getMember();
            User bot = event.getJDA().getUserById(JDAIsAwesome.getInstance().BOT_ID);
            MessageChannel channel = event.getChannel();
            Message message = event.getMessage();
            String rawMessage = message.getContentRaw();
            String messageString = event.getMessage().getContentDisplay();

            // v1.0.1 -
            checkMessage(bot, member, channel, message, rawMessage);

            // Hier wird abgefragt, ob die Nachricht mit dem voreingestellten Prefix startet. Dieser ist in einem public String in der Hauptklasse gespeichert und wird über die Instanz aufgerufen
            if (messageString.startsWith(JDAIsAwesome.getInstance().PREFIX)) {
                // Hier wird der Prefix abgeschnitten. Dazu habe ich hier fest die 1 eingetragen, kann aber auch mit JDAIsAwesome.getInstance().PREFIX.length(); dort eingetragen werden.
                // Außerdem wird alles was nach dem ersten Leerzeichen geschrieben wird abgeschnitten
                String command = messageString.substring(1).split(" ")[0];
                // Hier werden alle Argumente die nach dem Command kommen in einem Array gespeichert
                String[] args = messageString.substring(command.length() + 1).split(" ");

                // Hier wird abgefragt, ob der Command auch Argumente hat
                if (args.length > 0) {
                    // Hier werden direkt zwei Sachen gemacht. Erstens wird die Methode ausgeführt und dabei wird dann abgefragt, ob es diesen Command gibt.
                    // Wenn ja, dann wird dieser ausgeführt, wenn nicht, kommt die Nachricht di im if steht
                    if (!JDAIsAwesome.getInstance().getCommandManager().perform(command, member, channel, message)) {
                        // Diese Nachricht wird gesendet, wenn der Befehl invalide ist.
                        channel.sendMessage("*Dieser Befehl konnte nicht gefunden werden*").queue();
                    }
                }
            }
        }
    }

    // v1.0.1 - Ein weiteres Parameter brauchen wir hier nicht zu übergeben, da wir die Guilde über den Member bekommen können
    private void checkMessage(User bot, Member member, MessageChannel channel, Message message, String rawMessage) {
        long guildID = member.getGuild().getIdLong();
        // Frage ab, ob der Member nicht der Bot selbst ist und der Member kein Administrator ist (auch hier kann natürlich jede Permission genommen werden)
        if (member.getUser() != bot && !member.hasPermission(Permission.ADMINISTRATOR)) {
            // v1.0.1 - Hier müssen wir jetzt noch die guildID mit übergeben
            for (String word : JDAIsAwesome.getInstance().getSQLManager().getBlacklistedWords(guildID)) {
                // Frage ab, ob die Nachricht das aktuelle Wort beinhaltet
                if (rawMessage.toLowerCase().contains(word.toLowerCase())) {
                    // Lösche die Nachricht
                    message.delete().queue();
                    // Sende eine Nachricht in den Channel
                    channel.sendMessage("Bitte verwende nicht solche Wörter, " + member.getAsMention() + "!").queue();
                    // Breche die Schleife ab, da die Nachricht bereits gelöscht wurde
                    break;
                }
            }
        }
    }
}
