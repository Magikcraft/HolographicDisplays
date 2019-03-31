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
package com.gmail.filoghost.holographicdisplays.placeholder;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class PlaceholdersRegister {
	
	private static final Set<Placeholder> placeholders = Utils.newSet();
	
	// Register the default placeholders statically.
	static {
		
		register(new Placeholder(HolographicDisplays.getInstance(), "{online}", 1.0, new PlaceholderReplacer() {
			
			@Override
			public String update() {
				return String.valueOf(Bukkit.getOnlinePlayers().size());
			}
		}));
		
		register(new Placeholder(HolographicDisplays.getInstance(), "{max_players}", 10.0, new PlaceholderReplacer() {

			@Override
			public String update() {
				return String.valueOf(Bukkit.getMaxPlayers());
			}
		}));
		
		register(new Placeholder(HolographicDisplays.getInstance(), "{motd}", 60.0, new PlaceholderReplacer() {

			@Override
			public String update() {
				return Bukkit.getMotd();
			}
		}));
		
		register(new Placeholder(HolographicDisplays.getInstance(), "{time}", 0.9, new PlaceholderReplacer() {

			@Override
			public String update() {
				return Configuration.timeFormat.format(new Date());
			}
		}));
		
		register(new Placeholder(HolographicDisplays.getInstance(), "&u", 0.2, new CyclicPlaceholderReplacer(Utils.arrayToStrings(
				ChatColor.RED,
				ChatColor.GOLD,
				ChatColor.YELLOW,
				ChatColor.GREEN,
				ChatColor.AQUA,
				ChatColor.LIGHT_PURPLE
		))));
	}
	
	
	public static boolean register(Placeholder placeholder) {
		if (placeholders.contains(placeholder)) {
			return false;
		}
		
		placeholders.add(placeholder);
		return true;
	}
	
	public static Set<String> getTextPlaceholdersByPlugin(Plugin plugin) {
		Set<String> found = Utils.newSet();
		
		for (Placeholder placeholder : placeholders) {
			if (placeholder.getOwner().equals(plugin)) {
				found.add(placeholder.getTextPlaceholder());
			}
		}
		
		return found;
	}
	
	public static boolean unregister(Plugin plugin, String textPlaceholder) {
		
		Iterator<Placeholder> iter = placeholders.iterator();
		
		while (iter.hasNext()) {
			Placeholder placeholder = iter.next();
			
			if (placeholder.getOwner().equals(plugin) && placeholder.getTextPlaceholder().equals(textPlaceholder)) {
				iter.remove();
			
				for (DynamicLineData data : PlaceholdersManager.linesToUpdate) {
					if (data.getPlaceholders().contains(placeholder)) {
						data.getPlaceholders().remove(placeholder);
					}
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	protected static Set<Placeholder> getPlaceholders() {
		return placeholders;
	}

}
