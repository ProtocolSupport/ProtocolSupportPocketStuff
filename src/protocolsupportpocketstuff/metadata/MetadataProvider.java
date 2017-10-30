package protocolsupportpocketstuff.metadata;

import protocolsupport.api.unsafe.pemetadata.PEMetaProvider;
import protocolsupport.protocol.utils.types.NetworkEntity;
import protocolsupportpocketstuff.api.util.PocketUtils;

public class MetadataProvider extends PEMetaProvider {
	@Override
	public float getEntitySize(NetworkEntity networkEntity) {
		return PocketUtils.hasCustomScale(networkEntity.getId()) ? PocketUtils.getCustomScale(networkEntity.getId()) : 1;
	}
}