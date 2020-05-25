package com.gitlab.hufeisen.supportBot.commands;

import com.gitlab.hufeisen.supportBot.commands.types.ServerCommand;
import com.gitlab.hufeisen.supportBot.manage.LiteSQL;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class AddRoleCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		Guild guild = m.getGuild();
		
		if(m.hasPermission(Permission.MANAGE_CHANNEL)) {
			
			String[] args = message.getContentDisplay().split(" ");
			
			if(args.length == 2) {
						
				LiteSQL.onUpdate("INSERT INTO supportroles(guildid, roleid) VALUES("+guild.getIdLong()+","+args[1]+")");
				channel.sendMessage("Die Rolle mit der ID `" + args[1] + "` wurde erfolgreich in die Datenbank eingetragen.").queue();
				
			}
			
		}
		
	}

}
