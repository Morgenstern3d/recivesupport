package com.gitlab.hufeisen.supportBot.commands;

import com.gitlab.hufeisen.supportBot.commands.types.ServerCommand;
import com.gitlab.hufeisen.supportBot.manage.LiteSQL;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class RemoveChannelCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		if(m.hasPermission(Permission.MANAGE_CHANNEL)) {
			
			String[] args = message.getContentDisplay().split(" ");
			
			if(args.length == 2) {
						
				LiteSQL.onUpdate("DELETE FROM supportchannels WHERE channelid = " + args[1]);
				channel.sendMessage("Der Channel mit der ID `" + args[1] + "` wurde aus der Datenbank gelöscht").queue();
				
			}
			
		}
		
	}

}
