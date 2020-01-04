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
package com.gmail.filoghost.holographicdisplays.bridge.protocollib.current;

import java.util.List;
import java.util.Optional;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.gmail.filoghost.holographicdisplays.util.NMSVersion;

public class MetadataHelper {
	
	private Serializer itemSerializer;
	private Serializer intSerializer;
	private Serializer byteSerializer;
	private Serializer stringSerializer;
	private Serializer booleanSerializer;
	private Serializer chatComponentSerializer;

	private int itemSlotIndex;
	private int entityStatusIndex;
	private int airLevelWatcherIndex;
	private int customNameIndex;
	private int customNameVisibleIndex;
	private int noGravityIndex;
	private int armorStandStatusIndex;
	private int slimeSizeIndex;
	
	
	public MetadataHelper() {
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_14_R1)) {
			itemSlotIndex = 7;
		} else if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_10_R1)) {
			itemSlotIndex = 6;
		} else if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_9_R1)) {
			itemSlotIndex = 5;
		} else {
			itemSlotIndex = 10;
		}
		
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_15_R1)) {
			armorStandStatusIndex = 14;
		} else {
			armorStandStatusIndex = 11;
		}
		
		entityStatusIndex = 0;
		airLevelWatcherIndex = 1;
		customNameIndex = 2;
		customNameVisibleIndex = 3;
		noGravityIndex = 5;
		slimeSizeIndex = 15;
		
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_9_R1)) {
			itemSerializer = Registry.get(MinecraftReflection.getItemStackClass());
			intSerializer = Registry.get(Integer.class);
			byteSerializer = Registry.get(Byte.class);
			stringSerializer = Registry.get(String.class);
			booleanSerializer = Registry.get(Boolean.class);
		}
		
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
			chatComponentSerializer = Registry.get(MinecraftReflection.getIChatBaseComponentClass(), true);
		}
	}
	

	public WrappedWatchableObject getCustomNameWacthableObject(WrappedDataWatcher metadata) {
		return metadata.getWatchableObject(customNameIndex);
	}
	
	
	public WrappedWatchableObject getCustomNameWatchableObject(List<WrappedWatchableObject> dataWatcherValues) {
		for (int i = 0; i < dataWatcherValues.size(); i++) {
			WrappedWatchableObject watchableObject = dataWatcherValues.get(i);
			
			if (watchableObject.getIndex() == customNameIndex) {
				return watchableObject;
			}
		}
		
		return null;
	}
	
	
	public String getSerializedCustomName(WrappedWatchableObject customNameWatchableObject) {
		Object customNameWatchableObjectValue = customNameWatchableObject.getValue();
		
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
			if (!(customNameWatchableObjectValue instanceof Optional)) {
				return null;
			}
			
			Optional<?> customNameOptional = (Optional<?>) customNameWatchableObjectValue;
			if (!customNameOptional.isPresent()) {
				return null;
			}
			
			WrappedChatComponent componentWrapper = WrappedChatComponent.fromHandle(customNameOptional.get());
			return componentWrapper.getJson();
			
		} else {
			if (!(customNameWatchableObjectValue instanceof String)) {
				return null;
			}
			
			return (String) customNameWatchableObjectValue;
		}
	}
	
	
	public void setSerializedCustomName(WrappedWatchableObject customNameWatchableObject, String serializedCustomName) {
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
			customNameWatchableObject.setValue(Optional.of(WrappedChatComponent.fromJson(serializedCustomName).getHandle()));
		} else {
			customNameWatchableObject.setValue(serializedCustomName);
		}
	}
	

	public void setEntityStatus(WrappedDataWatcher dataWatcher, byte statusBitmask) {
		requireMinimumVersion(NMSVersion.v1_9_R1);
		dataWatcher.setObject(new WrappedDataWatcherObject(entityStatusIndex, byteSerializer), statusBitmask);
	}

	
	public void setCustomName(WrappedDataWatcher dataWatcher, String customName) {
		requireMinimumVersion(NMSVersion.v1_9_R1);
		
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
			dataWatcher.setObject(new WrappedDataWatcherObject(customNameIndex, chatComponentSerializer), Optional.of(WrappedChatComponent.fromText(customName).getHandle()));
		} else {
			dataWatcher.setObject(new WrappedDataWatcherObject(customNameIndex, stringSerializer), customName);
		}
	}

	
	public void setCustomNameVisible(WrappedDataWatcher dataWatcher, boolean customNameVisible) {
		requireMinimumVersion(NMSVersion.v1_9_R1);
		dataWatcher.setObject(new WrappedDataWatcherObject(customNameVisibleIndex, booleanSerializer), customNameVisible);
	}

	
	public void setNoGravity(WrappedDataWatcher dataWatcher, boolean noGravity) {
		requireMinimumVersion(NMSVersion.v1_9_R1);
		dataWatcher.setObject(new WrappedDataWatcherObject(noGravityIndex, booleanSerializer), noGravity);
	}

	
	public void setArmorStandStatus(WrappedDataWatcher dataWatcher, byte statusBitmask) {
		requireMinimumVersion(NMSVersion.v1_9_R1);
		dataWatcher.setObject(new WrappedDataWatcherObject(armorStandStatusIndex, byteSerializer), statusBitmask);
	}

	
	public void setItemMetadata(WrappedDataWatcher dataWatcher, Object nmsItemStack) {
		if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_9_R1)) {
			if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_11_R1)) {
				dataWatcher.setObject(new WrappedDataWatcherObject(itemSlotIndex, itemSerializer), nmsItemStack);
			} else {
				dataWatcher.setObject(new WrappedDataWatcherObject(itemSlotIndex, itemSerializer), com.google.common.base.Optional.of(nmsItemStack));
			}
			dataWatcher.setObject(new WrappedDataWatcherObject(airLevelWatcherIndex, intSerializer), 300);
			dataWatcher.setObject(new WrappedDataWatcherObject(entityStatusIndex, byteSerializer), (byte) 0);
		} else {
			dataWatcher.setObject(itemSlotIndex, nmsItemStack);
			dataWatcher.setObject(airLevelWatcherIndex, 300);
			dataWatcher.setObject(entityStatusIndex, (byte) 0);
		}
	}
	
	
	public void setSlimeSize(WrappedDataWatcher dataWatcher, int size) {
		requireMinimumVersion(NMSVersion.v1_15_R1);
		dataWatcher.setObject(new WrappedDataWatcherObject(slimeSizeIndex, intSerializer), size);
	}
	
	
	private static void requireMinimumVersion(NMSVersion minimumVersion) {
		if (!NMSVersion.isGreaterEqualThan(minimumVersion)) {
			throw new UnsupportedOperationException("Method only available from NMS version " + minimumVersion);
		}
	}
	
}
