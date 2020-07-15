package com.github.zflxw.commands;

import com.github.zflxw.JDAIsAwesome;
import com.github.zflxw.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class PrefixCommand implements ServerCommand {
    @Override
    public void performCommand(Member member, MessageChannel channel, Message message, String[] args) {
        // Permissions abfragen
        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            // Abfragen, ob es nicht der Bot selbst war, der die Nachricht gesendet hat
            if (member.getUser() != member.getJDA().getSelfUser()) {
                // Speichere den aktuellen Prefix
                String currentPrefix = JDAIsAwesome.getInstance().getSQLManager().getPrefix(member.getGuild().getIdLong());

                // Frage ab, ob die Nachricht nur ein Argument hat
                 if (args.length == 1) {
                    // Speichere den womöglich neuen Prefix
                    String newPrefix = args[0];
                    // Frage ab, ob der angebene Prefix nicht schon der aktuelle ist
                    if (!newPrefix.equalsIgnoreCase(currentPrefix)) {
                        // Setze den neuen Prefix
                        JDAIsAwesome.getInstance().getSQLManager().setPrefix(newPrefix, member.getGuild().getIdLong());
                        // Sende noch eine Nachricht, because Nachrichten sind tolL!
                        channel.sendMessage("Der neue Prefix ist nun ``"+newPrefix+"``!").queue();
                    } else {
                        channel.sendMessage("Der Prefix ``"+newPrefix+"`` ist bereits der aktuelle Prefix!").queue();
                    }
                } else {
                    channel.sendMessage("Bitte verwende ``"+currentPrefix+"prefix <prefix>``, um dem Prefix zu ändern, " + member.getAsMention() + "!").queue();
                }
            }
        }
    }
}
