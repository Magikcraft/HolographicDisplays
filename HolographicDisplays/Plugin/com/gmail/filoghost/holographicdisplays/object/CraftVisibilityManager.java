package com.gmail.filoghost.holographicdisplays.object;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.ProtocolLibHook;
import com.gmail.filoghost.holographicdisplays.util.Validator;

public class CraftVisibilityManager implements VisibilityManager {

	private final CraftHologram hologram;
	private Map<String, Boolean> playersVisibilityMap;
	private boolean visibleByDefault;

	private static final int VISIBILITY_DISTANCE_SQUARED = 64 * 64;
	
	public CraftVisibilityManager(CraftHologram hologram) {
		Validator.notNull(hologram, "hologram");
		this.hologram = hologram;
		this.visibleByDefault = true;
	}
	
	@Override
	public boolean isVisibleByDefault() {		
		return visibleByDefault;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setVisibleByDefault(boolean visibleByDefault) {
		if (this.visibleByDefault != visibleByDefault) {
			
			boolean oldVisibleByDefault = this.visibleByDefault;
			this.visibleByDefault = visibleByDefault;
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				
				if (playersVisibilityMap != null && playersVisibilityMap.containsKey(player.getName().toLowerCase())) {
					// Has a specific value set
					continue;
				}
					
				if (oldVisibleByDefault) {
					// If previously was visible, now is NOT visible by default, because the value has changed
					sendDestroyPacketIfNear(player, hologram);
				} else {
					// Opposite case
					sendCreatePacketIfNear(player, hologram);
				}
			}
		}
	}
	
	@Override
	public void showTo(Player player) {
		Validator.notNull(player, "player");
		
		boolean wasVisible = isVisibleTo(player);
		
		if (playersVisibilityMap == null) {
			// Lazy initialization
			playersVisibilityMap = new ConcurrentHashMap<String, Boolean>();
		}
		
		playersVisibilityMap.put(player.getName().toLowerCase(), true);
		
		if (wasVisible == false) {
			sendCreatePacketIfNear(player, hologram);
		}
	}
	
	
	@Override
	public void hideTo(Player player) {
		Validator.notNull(player, "player");
		
		boolean wasVisible = isVisibleTo(player);
		
		if (playersVisibilityMap == null) {
			// Lazy initialization
			playersVisibilityMap = new ConcurrentHashMap<String, Boolean>();
		}
		
		playersVisibilityMap.put(player.getName().toLowerCase(), false);
		
		if (wasVisible == true) {
			sendDestroyPacketIfNear(player, hologram);
		}
	}
	
	@Override
	public boolean isVisibleTo(Player player) {
		Validator.notNull(player, "player");
		
		if (playersVisibilityMap != null) {

			Boolean value = playersVisibilityMap.get(player.getName().toLowerCase());
			if (value != null) {
				return value;
			}
		}

		return visibleByDefault;
	}

	@Override
	public void resetVisibility(Player player) {
		Validator.notNull(player, "player");
		
		if (playersVisibilityMap == null) {
			return;
		}
		
		boolean wasVisible = isVisibleTo(player);
		
		playersVisibilityMap.remove(player.getName().toLowerCase());
		
		if (visibleByDefault && !wasVisible) {
			sendCreatePacketIfNear(player, hologram);
			
		} else if (!visibleByDefault && wasVisible) {
			sendDestroyPacketIfNear(player, hologram);
		}
	}
	
	@Override
	public void resetVisibilityAll() {
		if (playersVisibilityMap != null) {
			playersVisibilityMap.clear();
			playersVisibilityMap = null;
		}
	}
	
	private static void sendCreatePacketIfNear(Player player, CraftHologram hologram) {
		if (HolographicDisplays.useProtocolLib() && isNear(player, hologram)) {
			ProtocolLibHook.sendCreateEntitiesPacket(player, hologram);
		}
	}
	
	private static void sendDestroyPacketIfNear(Player player, CraftHologram hologram) {
		if (HolographicDisplays.useProtocolLib() && isNear(player, hologram)) {
			ProtocolLibHook.sendDestroyEntitiesPacket(player, hologram);
		}
	}
	
	private static boolean isNear(Player player, CraftHologram hologram) {
		return player.getWorld().equals(hologram.getWorld()) && player.getLocation().distanceSquared(hologram.getLocation()) < VISIBILITY_DISTANCE_SQUARED;
	}

	@Override
	public String toString() {
		return "CraftVisibilityManager [playersMap=" + playersVisibilityMap + ", visibleByDefault=" + visibleByDefault + "]";
	}
	
}