package com.gitlab.hufeisen.supportBot.commands;

import com.gitlab.hufeisen.supportBot.commands.types.ServerCommand;
import com.gitlab.hufeisen.supportBot.manage.LiteSQL;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ToggleJoinMessage implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		Guild guild = m.getGuild();
		
		if(m.hasPermission(Permission.MANAGE_CHANNEL)) {
			
			String[] args = message.getContentDisplay().split(" ");
			
			if(args.length == 2) {
				if(args[1].equalsIgnoreCase("true")) {
				
					LiteSQL.onUpdate("INSERT INTO joinmessage(guildid, value) VALUES("+guild.getIdLong()+", "+ 1 +")");
					channel.sendMessage("Die Join Rückmeldung wurde erfolgreich aktiviert.").queue();
				}
				
				else if(args[1].equalsIgnoreCase("false")) {
					
					LiteSQL.onUpdate("DELETE FROM joinmessage WHERE value = " + 1);
					channel.sendMessage("Die Join Rückmeldung wurde erfolgreich deaktiviert.").queue();
					
				} else {
					
					channel.sendMessage("Diesen Wert gibt es nicht. Bitte benutze `true` oder `false`").queue();
					
				}

			}
			
		}
		
	}

}
