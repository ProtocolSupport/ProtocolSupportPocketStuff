package protocolsupportpocketstuff.metadata;

import org.bukkit.entity.EntityType;
import protocolsupport.api.unsafe.pemetadata.PEMetaProvider;
import protocolsupportpocketstuff.api.entity.PocketMetadata;

import java.util.UUID;

public class MetadataProvider extends PEMetaProvider {

	public static final String DEFAULTINTERACT = "Interact";

	@Override
	public String getUseText(UUID uuid, int id, EntityType entitytype) {
		return PocketMetadata.hasCustomInteractText(id) ? PocketMetadata.getInteractText(id) : DEFAULTINTERACT;
	}

	@Override
	public float getSizeScale(UUID uuid, int id, EntityType entitytype) {
		return PocketMetadata.hasCustomScale(id) ? PocketMetadata.getCustomScale(id) : 1;
	}

}