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
package com.gmail.filoghost.holographicdisplays.task;

import java.util.Set;
import java.util.logging.Level;

import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.exception.HologramNotFoundException;
import com.gmail.filoghost.holographicdisplays.exception.InvalidFormatException;
import com.gmail.filoghost.holographicdisplays.exception.WorldNotFoundException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;

public class StartupLoadHologramsTask implements Runnable {

	@Override
	public void run() {
		Set<String> savedHologramsNames = HologramDatabase.getHolograms();
		if (savedHologramsNames != null) {
			for (String hologramName : savedHologramsNames) {
				try {
					NamedHologram namedHologram = HologramDatabase.loadHologram(hologramName);
					NamedHologramManager.addHologram(namedHologram);
					namedHologram.refreshAll();
				} catch (HologramNotFoundException e) {
					ConsoleLogger.log(Level.WARNING, "Hologram '" + hologramName + "' not found, skipping it.");
				} catch (InvalidFormatException e) {
					ConsoleLogger.log(Level.WARNING, "Hologram '" + hologramName + "' has an invalid location format.");
				} catch (WorldNotFoundException e) {
					ConsoleLogger.log(Level.WARNING, "Hologram '" + hologramName + "' was in the world '" + e.getMessage() + "' but it wasn't loaded.");
				} catch (Exception e) {
					ConsoleLogger.log(Level.WARNING, "Unhandled exception while loading the hologram '" + hologramName + "'. Please contact the developer.", e);
				}
			}
		}
	}

}
