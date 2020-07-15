package com.github.zflxw.commands.types;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

// Eine öffentliche Schnittstelle mit dem Namen ServerCommand
public interface ServerCommand {
    // Diese Methode führt sozusagen später den Befehl aus. Als Parameter werden dazu der Member, der MessageChannel und die Message genommen
    public void performCommand(Member member, MessageChannel channel, Message message, String[] args);
}
