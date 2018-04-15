package protocolsupportpocketstuff.api.entity;

import org.bukkit.entity.Entity;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe.EntityMetadata.PeMetaBase;
import protocolsupport.protocol.utils.datawatcher.DataWatcherObject;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectFloatLe;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectString;
import protocolsupport.utils.CollectionsUtils;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.metadata.MetadataProvider;
import protocolsupportpocketstuff.packet.play.EntityDataPacket;

import java.util.concurrent.ConcurrentHashMap;

public class PocketMetadata {

	private static final ConcurrentHashMap<Integer, Float> entityScales = new ConcurrentHashMap<>(0);
	private static final ConcurrentHashMap<Integer, String> entityInteracts = new ConcurrentHashMap<>(0);

    //=====================================================\\
    //						Setting						   \\
    //=====================================================\\

	/**
	 * Sets the scale of an entity
	 * @param entity
	 * @param scale
	 */
	public static void setScale(Entity entity, float scale) {
		setScale(entity.getEntityId(), scale);
	}

	/**
	 * Sets the scale of an entity using its ID
	 * @param entityId
	 * @param scale
	 */
	public static void setScale(int entityId, float scale) {
		if (scale == 1) {
			entityScales.remove(entityId);
		} else {
			entityScales.put(entityId, scale);
		}
		// Update to all players
		CollectionsUtils.ArrayMap<DataWatcherObject<?>> metadata = new CollectionsUtils.ArrayMap<>(PeMetaBase.SCALE);
		metadata.put(PeMetaBase.SCALE, new DataWatcherObjectFloatLe(scale));
		EntityDataPacket packet = new EntityDataPacket(entityId, metadata);
		for (Connection connection : PocketCon.getPocketConnections()) {
			PocketCon.sendPocketPacket(connection, packet);
		}
	}

	/**
	 * Sets the interact text of an entity.
	 * You can see the text when you look at an entity on pocketedition.
	 * @param entity
	 * @param interactText
	 */
	public static void setInteractText(Entity entity, String interactText) {
		setInteractText(entity.getEntityId(), interactText);
	}
	
	/**
	 * Sets the interact text of an entity using its id.
	 * You can see the text when you look at an entity on pocketedition.
	 * @param entityId
	 * @param interactText
	 */
	public static void setInteractText(int entityId, String interactText) {
		if (interactText.equals(MetadataProvider.DEFAULTINTERACT)) {
			entityInteracts.remove(entityId);
		} else {
			entityInteracts.put(entityId, interactText);
		}
		// Update to all players
		CollectionsUtils.ArrayMap<DataWatcherObject<?>> metadata = new CollectionsUtils.ArrayMap<>(PeMetaBase.BUTTON_TEXT);
		metadata.put(PeMetaBase.BUTTON_TEXT, new DataWatcherObjectString(interactText));
		EntityDataPacket packet = new EntityDataPacket(entityId, metadata);
		for (Connection connection : PocketCon.getPocketConnections()) {
			PocketCon.sendPocketPacket(connection, packet);
		}
	}
    //=====================================================\\
    //						Checking					   \\
    //=====================================================\\

	/**
	 * Returns if an entity has an custom scale set
	 * @param entity
	 * @return if the entity has an custom scale set
	 */
	public static boolean hasCustomScale(Entity entity) {
		return hasCustomScale(entity.getEntityId());
	}

	/**
	 * Returns if an entityId has an custom scale set
	 * @param entityId
	 * @return if the entityId has an custom scale set
	 */
	public static boolean hasCustomScale(int entityId) {
		return entityScales.containsKey(entityId);
	}

	/**
	 * Returns if an entity has custom interact text set.
	 * This means the interact text is not "Interact".
	 * @param entity
	 * @return if the entity has custom interact text.
	 */
	public static boolean hasCustomInteractText(Entity entity) {
		return hasCustomInteractText(entity.getEntityId());
	}

	/**
	 * Returns if an entityId has custom interact text set.
	 * This means the interact text is not "Interact".
	 * @param entityId
	 * @return if the entityId has custom interact text.
	 */
	public static boolean hasCustomInteractText(int entityId) {
		return entityInteracts.containsKey(entityId);
	}

    //=====================================================\\
    //						Getting						   \\
    //=====================================================\\

	/**
	 * Gets the entity's custom scale.
	 * @param entity
	 * @return the entity custom scale.
	 */
	public static float getCustomScale(Entity entity) {
		return getCustomScale(entity.getEntityId());
	}

	/**
	 * Gets the entityId custom scale.
	 * @param entityId
	 * @return the entityId custom scale.
	 */
	public static float getCustomScale(int entityId) {
		return entityScales.get(entityId);
	}

	/**
	 * Gets the entity's custom interact text.
	 * @param entity
	 * @return the custom interact text.
	 */
	public static String getInteractText(Entity entity) {
		return entityInteracts.get(entity.getEntityId());
	}

	/**
	 * Gets the entityId custom interact text.
	 * @param entityId
	 * @return the custom interact text.
	 */
	public static String getInteractText(int entityId) {
		return entityInteracts.get(entityId);
	}

}