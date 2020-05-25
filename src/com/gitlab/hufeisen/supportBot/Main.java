package com.gitlab.hufeisen.supportBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.security.auth.login.LoginException;

import org.apache.commons.io.IOUtils;

import com.gitlab.hufeisen.supportBot.listener.CommandListener;
import com.gitlab.hufeisen.supportBot.listener.JoinListener;
import com.gitlab.hufeisen.supportBot.manage.LiteSQL;
import com.gitlab.hufeisen.supportBot.manage.SQLManager;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Main {

	public static Main INSTANCE;
	
	public ShardManager shardMan;
	
	static String lizenz;
	
	private CommandManager cmdMan;
	
	public static void main(String[] args) {
		try {
			checkLizenz(new URL("http://hufeisen-games.bplaced.net/lizenz"));
		} catch (IOException e) {
			System.out.println("Bot konnte keine verbindung zum Development Server herstellen. Bitte stelle sicher das es eine Internet verbindung gibt. Sonst wende dich an den Entwickler: hufeisen-games@gmx.de");
		}
		try {
			if(lizenz.contains("Lizenz = true")) {
				new Main();
			} else {
				System.out.println("Deine Lizenz ist fehlerhaft. Bitte wende dich an den Entwickler: hufeisen-games@gmx.de");
				System.exit(0);
			}
		} catch (LoginException | IllegalArgumentException e) {
			System.out.println("Es gab einen unbekannten Fehler. Programm stopt mit ID 1");
		}

	}
	
	public Main() throws LoginException, IllegalArgumentException {
		INSTANCE = this;
		
		DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
		
		builder.setToken("NzE0Mzc5MTg2NDA3NDczMTY0.XstzhA.-ir_doWP_jtVb1Pk1Ue1Zfro7Z8");
		
		builder.setStatus(OnlineStatus.ONLINE);
		builder.setActivity(Activity.playing("by Hufeisen"));
		
		builder.addEventListeners(new CommandListener());
		builder.addEventListeners(new JoinListener());
		
		this.cmdMan = new CommandManager();
		
		LiteSQL.connect();
		SQLManager.onCreate();
		
		shardMan = builder.build();
		
		shutdown();
	
		System.out.println("Bot erfolgreich hochgefahren. Wenn du den Bot herrunter fahren möchtest benutzte 'exit' ");
	}
	
	public void shutdown() {
		
		new Thread(() -> {
			
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				while((line = reader.readLine()) != null) {
					
					if(line.equalsIgnoreCase("exit")) {
						if(shardMan != null) {
							
							shardMan.setStatus(OnlineStatus.OFFLINE);
							shardMan.shutdown();
							System.out.println("Bot wurde herruntergefahren.");
							LiteSQL.disconnect();
						}
						
						
						reader.close();
						break;
					}
					else if(line.equalsIgnoreCase("info")) {
						for(Guild guild : shardMan.getGuilds()) {
							System.out.println(guild.getName() + " " + guild.getIdLong());
						}
					}
					else {
						System.out.println("Benutze 'exit' zum herrunterfahren.");
					}
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
			
		}).start();
	}
	
	public static void checkLizenz(URL url) throws IOException{
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder(1280000);
		try {
			
			reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			int count;
			char[]data = new char[5000];
			while((count = reader.read(data)) != -1) {
				builder.append(data, 0, count);
			}
			
		}finally {
			
			IOUtils.closeQuietly(reader);
			
		}
		
		lizenz = builder.toString();
	}
	
	public CommandManager getCmdMan() {
		return cmdMan;
	}
	
}
