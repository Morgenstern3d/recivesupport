package com.gitlab.hufeisen.supportBot.manage;

public class SQLManager {

	public static void onCreate() {
		
		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS supportchannels(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, guildid INTEGER, channelid INTEGER)");
		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS supportroles(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, guildid INTEGER, roleid INTEGER)");
		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS joinmessage(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, guildid INTEGER, value INTEGER)");
		
	}

}
