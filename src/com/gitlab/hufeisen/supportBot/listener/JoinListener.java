package com.gitlab.hufeisen.supportBot.listener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.gitlab.hufeisen.supportBot.manage.LiteSQL;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinListener extends ListenerAdapter {
	
	ArrayList<String> calledPlayers;
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		onJoin(event.getChannelJoined(), event.getEntity());
	}
	
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		onJoin(event.getChannelJoined(), event.getEntity());
		onLeave(event.getChannelLeft(), event.getEntity());
	}
	
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		onLeave(event.getChannelLeft(), event.getEntity());
	}
	
	public void onJoin(VoiceChannel joined, Member member) {
		calledPlayers = new ArrayList<String>();
		Guild guild = member.getGuild();
		ResultSet set = LiteSQL.onQuery("SELECT channelid FROM supportchannels WHERE guildid = " + guild.getIdLong() + " AND channelid = " + joined.getIdLong());
		try {
			if(set.next()) {
			
				for(Member memb : guild.getMembers()) {
					if(!memb.getUser().isBot()) {
						memb.getUser().openPrivateChannel().queue((ch) -> {
							
							for(int i=0; i<guild.getRoles().size(); i++) {
								if(memb.getRoles().contains(guild.getRoles().get(i))) {
								
									ResultSet set2 = LiteSQL.onQuery("SELECT roleid FROM supportroles WHERE guildid = " + guild.getIdLong() + " AND roleid = " + guild.getRoles().get(i).getIdLong());

									try {
										if(set.next()) {
											if(!calledPlayers.contains(memb.getEffectiveName())) {
											
												ch.sendMessage("Der Spieler `" + member.getEffectiveName() + "` wartet im Support Warteraum(`"+joined.getMembers().size()+"` insgesammt)").queue();
												
												calledPlayers.add(memb.getEffectiveName());
												
											}
										}
									} catch (SQLException e) {
										System.out.println("Rollen Daten konnten nicht überprüft werden. Error Code 102#2");
									}
									
								}
							}
						});
					}
				}
				member.getUser().openPrivateChannel().queue((ch) -> {
					
					ResultSet set2 = LiteSQL.onQuery("SELECT value FROM joinmessage WHERE guildid = " + guild.getIdLong() + " AND value = 1");
					
					try {
						if(set2.next()) {
							
							ch.sendMessage("Herzlich Willkommen im Support. Zurzeit ist kein Supporter verfügbar. Bitte warte einen Moment.").queue();
							
						}
					} catch (SQLException e) {
						System.out.println("Settings Daten konnten nicht überprüft werden. Error Code 103");
					}
					
				});
			}
		} catch (SQLException e) {
			System.out.println("Join Daten konnten nicht überprüft werden. Error Code 102#1");
		}

	}
	
	public void onLeave(VoiceChannel leaved, Member member) {
		
		calledPlayers = new ArrayList<String>();
		Guild guild = member.getGuild();
		ResultSet set = LiteSQL.onQuery("SELECT channelid FROM supportchannels WHERE guildid = " + guild.getIdLong() + " AND channelid = " + leaved.getIdLong());
		try {
			if(set.next()) {
			
				for(Member memb : guild.getMembers()) {
					if(!memb.getUser().isBot()) {
						memb.getUser().openPrivateChannel().queue((ch) -> {
							
							for(int i=0; i<guild.getRoles().size(); i++) {
								if(memb.getRoles().contains(guild.getRoles().get(i))) {
								
									ResultSet set2 = LiteSQL.onQuery("SELECT roleid FROM supportroles WHERE guildid = " + guild.getIdLong() + " AND roleid = " + guild.getRoles().get(i).getIdLong());

									try {
										if(set.next()) {
											if(!calledPlayers.contains(memb.getEffectiveName())) {
											
												ch.sendMessage("Der Spieler `" + member.getEffectiveName() + "` hat den Support Warteraum verlassen(`"+leaved.getMembers().size()+"` verbleibend)").queue();
												
												calledPlayers.add(memb.getEffectiveName());
												
											}
										}
									} catch (SQLException e) {
										System.out.println("Rollen Daten konnten nicht überprüft werden. Error Code 102#2");
									}
								}
							}
						});
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Join Daten konnten nicht überprüft werden. Error Code 102#1");
		}
		
	}
}
