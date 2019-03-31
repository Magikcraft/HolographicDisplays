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

import com.gmail.filoghost.holographicdisplays.disk.Configuration;

public class BungeeServerInfo {

	private volatile boolean isOnline;
	private volatile int onlinePlayers;
	private volatile int maxPlayers;
	
	// The two lines of a motd
	private volatile String motd1; // Should never be null
	private volatile String motd2; // Should never be null
	
	private volatile long lastRequest;

	protected BungeeServerInfo() {
		isOnline = false;
		this.motd1 = "";
		this.motd2 = "";
		updateLastRequest();
	}
	
	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public String getMotd1() {
		return motd1;
	}
	
	public String getMotd2() {
		return motd2;
	}

	public void setMotd(String motd) {

		if (motd == null) {
			this.motd1 = "";
			this.motd2 = "";
			return;
		}
		
		if (motd.contains("\n")) {
			String[] split = motd.split("\n");
			this.motd1 = Configuration.pingerTrimMotd ? split[0].trim() : split[0];
			this.motd2 = Configuration.pingerTrimMotd ? split[1].trim() : split[1];
		} else {
			this.motd1 = Configuration.pingerTrimMotd ? motd.trim() : motd;
			this.motd2 = "";
		}
	}

	public long getLastRequest() {
		return lastRequest;
	}

	public void updateLastRequest() {
		this.lastRequest = System.currentTimeMillis();
	}

}
