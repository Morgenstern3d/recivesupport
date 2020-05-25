package com.gitlab.hufeisen.supportBot;

import java.util.concurrent.ConcurrentHashMap;

import com.gitlab.hufeisen.supportBot.commands.AddChannelCommand;
import com.gitlab.hufeisen.supportBot.commands.AddRoleCommand;
import com.gitlab.hufeisen.supportBot.commands.RemoveChannelCommand;
import com.gitlab.hufeisen.supportBot.commands.RemoveRoleCommand;
import com.gitlab.hufeisen.supportBot.commands.ToggleJoinMessage;
import com.gitlab.hufeisen.supportBot.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;



public class CommandManager {
	public ConcurrentHashMap<String, ServerCommand> commands;
	
	public CommandManager() {
		this.commands = new ConcurrentHashMap<>();
		
		this.commands.put("addchannel", new AddChannelCommand());
		this.commands.put("removechannel", new RemoveChannelCommand());
		this.commands.put("addrole", new AddRoleCommand());
		this.commands.put("removerole", new RemoveRoleCommand());
		this.commands.put("togglejoinmessage", new ToggleJoinMessage());
		
	}
	
	public boolean perform(String command, Member m, TextChannel channel, Message message) {
		
		ServerCommand cmd;
		if((cmd = this.commands.get(command.toLowerCase())) != null) {
			cmd.performCommand(m, channel, message);
			return true;
		}
		
		return false;
	}
}

