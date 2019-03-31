/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.holographicdisplays.bridge.bungeecord;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.NMSVersion;

public class BungeeChannel implements PluginMessageListener {

	private static BungeeChannel instance;
	
	public static BungeeChannel getInstance() {
		if (instance == null) {
			instance = new BungeeChannel(HolographicDisplays.getInstance());
		}
		return instance;
	}
	
	private BungeeChannel(Plugin plugin) {
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
        
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
        	// TODO implement when RedisBungee will be updated
        } else {
        	Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "RedisBungee");
        	Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "RedisBungee", this);
        }
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		String targetChannel = Configuration.useRedisBungee ? "RedisBungee" : "BungeeCord";
		
		if (channel.equals(targetChannel)) {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
			
			try {
				String subChannel = in.readUTF();
				 
				if (subChannel.equals("PlayerCount")) {
					String server = in.readUTF();
					 
					if (in.available() > 0) {
						int online = in.readInt();
						
						BungeeServerInfo serverInfo = BungeeServerTracker.getOrCreateServerInfo(server);
						serverInfo.setOnlinePlayers(online);
					}
				}
			 
			} catch (EOFException e) {
				// Do nothing.
			} catch (IOException e) {
				// This should never happen.
				e.printStackTrace();
			}
		}
	}
	
	
	public void askPlayerCount(String server) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);

		try {
			out.writeUTF("PlayerCount");
			out.writeUTF(server);
		} catch (IOException e) {
			// It should not happen.
			ConsoleLogger.log(Level.WARNING, "I/O Exception while asking for player count on server '" + server + "'.", e);
		}

		// OR, if you don't need to send it to a specific player
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		if (players.size() > 0) {
			players.iterator().next().sendPluginMessage(HolographicDisplays.getInstance(), Configuration.useRedisBungee ? "RedisBungee" : "BungeeCord", b.toByteArray());
		}
	}
}
