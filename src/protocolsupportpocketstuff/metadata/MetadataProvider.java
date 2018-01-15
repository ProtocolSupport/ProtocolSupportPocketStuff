package protocolsupportpocketstuff.metadata;

import org.bukkit.entity.EntityType;
import protocolsupport.api.unsafe.pemetadata.PEMetaProvider;
import protocolsupportpocketstuff.api.util.PocketUtils;

import java.util.UUID;

public class MetadataProvider extends PEMetaProvider {

	@Override
	public String getUseText(UUID uuid, int id, EntityType entitytype) {
		return "Interact";
	}

	@Override
	public float getSizeScale(UUID uuid, int id, EntityType entitytype) {
		return PocketUtils.hasCustomScale(id) ? PocketUtils.getCustomScale(id) : 1;
	}

}