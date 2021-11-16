package me.funayd.gildedupgrade.nbtapi.utils;

import org.bukkit.Bukkit;

import java.util.logging.Logger;

/**
 * This class acts as the "Brain" of the NBTApi. It contains the main logger for
 * other classes,registers bStats and checks rather Maven shading was done
 * correctly.
 * 
 * @author tr7zw
 *
 */
@SuppressWarnings("javadoc")
public enum MinecraftVersion {
	UNKNOWN(Integer.MAX_VALUE), // Use the newest known mappings
	MC1_7_R4(174), MC1_8_R3(183), MC1_9_R1(191), MC1_9_R2(192), MC1_10_R1(1101), MC1_11_R1(1111), MC1_12_R1(1121),
	MC1_13_R1(1131), MC1_13_R2(1132), MC1_14_R1(1141), MC1_15_R1(1151), MC1_16_R1(1161), MC1_16_R3(1163), MC1_17_R1(1171);

	private static MinecraftVersion version;
	private static Boolean hasGsonSupport;
//	private static boolean updateCheckDisabled = false;
	/**
	 * Logger used by the api
	 */
	private static final Logger logger = Logger.getLogger("NBTAPI");

	private final int versionId;

	MinecraftVersion(int versionId) {
		this.versionId = versionId;
	}

	/**
	 * @return A simple comparable Integer, representing the version.
	 */
	public int getVersionId() {
		return versionId;
	}
	
	/**
	 * Returns true if the current versions is at least the given Version
	 * 
	 * @param version The minimum version
	 * @return
	 */
	public static boolean isAtLeastVersion(MinecraftVersion version) {
		return getVersion().getVersionId() >= version.getVersionId();
	}
	
	/**
	 * Returns true if the current versions newer (not equal) than the given version
	 * 
	 * @param version The minimum version
	 * @return
	 */
	public static boolean isNewerThan(MinecraftVersion version) {
		return getVersion().getVersionId() > version.getVersionId();
	}

	/**
	 * Getter for this servers MinecraftVersion. Also init's bStats and checks the
	 * shading.
	 * 
	 * @return The enum for the MinecraftVersion this server is running
	 */
	public static MinecraftVersion getVersion() {
		if (version != null) {
			return version;
		}
		final String ver = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		logger.info("[GU-info] Found Spigot: " + ver + "! Trying to find NMS support");
		try {
			version = MinecraftVersion.valueOf(ver.replace("v", "MC"));
		} catch (IllegalArgumentException ex) {
			version = MinecraftVersion.UNKNOWN;
		}
		if (version != UNKNOWN) {
			logger.info("[GU-info] NMS support '" + version.name() + "' loaded!");
		} else {
			logger.warning("[GU-info] Wasn't able to find NMS Support! Some functions may not work!");
		}
		return version;
	}
	public static boolean hasGsonSupport() {
		if (hasGsonSupport != null) {
			return hasGsonSupport;
		}
		try {
			logger.info("[GU-info] Found Gson: " + Class.forName("com.google.gson.Gson"));
			hasGsonSupport = true;
		} catch (Exception ex) {
			logger.info("[GU-info] Gson not found! This will not allow the usage of some methods!");
			hasGsonSupport = false;
		}
		return hasGsonSupport;
	}

	/**
	 * @return Logger used by the NBT-API
	 */
	public static Logger getLogger() {
		return logger;
	}

}
