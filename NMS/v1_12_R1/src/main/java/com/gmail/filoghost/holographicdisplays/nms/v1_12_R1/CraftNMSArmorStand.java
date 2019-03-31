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
package com.gmail.filoghost.holographicdisplays.nms.v1_12_R1;

import java.util.Collection;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class CraftNMSArmorStand extends CraftArmorStand {

	public CraftNMSArmorStand(CraftServer server, EntityNMSArmorStand entity) {
		super(server, entity);
	}
	
	// Disallow all the bukkit methods.
	
	@Override
	public void remove() {
		// Cannot be removed, this is the most important to override.
	}

	// Methods from Armor stand class
	
	@Override public void setArms(boolean arms) { }
	@Override public void setBasePlate(boolean basePlate) { }
	@Override public void setBodyPose(EulerAngle pose) { }
	@Override public void setBoots(ItemStack item) { }
	@Override public void setChestplate(ItemStack item) { }
	@Override public void setHeadPose(EulerAngle pose) { }
	@Override public void setHelmet(ItemStack item) { }
	@Override public void setItemInHand(ItemStack item) { }
	@Override public void setLeftArmPose(EulerAngle pose) { }
	@Override public void setLeftLegPose(EulerAngle pose) { }
	@Override public void setLeggings(ItemStack item) { }
	@Override public void setRightArmPose(EulerAngle pose) { }
	@Override public void setRightLegPose(EulerAngle pose) { }
	@Override public void setSmall(boolean small) { }
	@Override public void setVisible(boolean visible) { }
	@Override public void setMarker(boolean marker) { }

	// Methods from LivingEntity class
	@Override public boolean addPotionEffect(PotionEffect effect) { return false; }
	@Override public boolean addPotionEffect(PotionEffect effect, boolean param) { return false; }
	@Override public boolean addPotionEffects(Collection<PotionEffect> effects) { return false; }
	@Override public void setRemoveWhenFarAway(boolean remove) { }
	@Override public void setAI(boolean ai) { }
	@Override public void setCanPickupItems(boolean pickup) { }
	@Override public void setCollidable(boolean collidable) { }
	@Override public void setGliding(boolean gliding) {	}
	@Override public boolean setLeashHolder(Entity holder) { return false; }
	
	// Methods from Entity
	@Override public void setVelocity(Vector vel) { }
	@Override public boolean teleport(Location loc) { return false; }
	@Override public boolean teleport(Entity entity) { return false; }
	@Override public boolean teleport(Location loc, TeleportCause cause) { return false; }
	@Override public boolean teleport(Entity entity, TeleportCause cause) { return false; }
	@Override public void setFireTicks(int ticks) { }
	@Override public boolean setPassenger(Entity entity) { return false; }
	@Override public boolean eject() { return false; }
	@Override public boolean leaveVehicle() { return false; }
	@Override public void playEffect(EntityEffect effect) { }
	@Override public void setCustomName(String name) { }
	@Override public void setCustomNameVisible(boolean flag) { }
	@Override public void setGlowing(boolean flag) { }
	@Override public void setGravity(boolean gravity) { }
	@Override public void setInvulnerable(boolean flag) { }
	@Override public void setMomentum(Vector value) { }
	@Override public void setSilent(boolean flag) { }
	@Override public void setTicksLived(int value) { }
	
}
