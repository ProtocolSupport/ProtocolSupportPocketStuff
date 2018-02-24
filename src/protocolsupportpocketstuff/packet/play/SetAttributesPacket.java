package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

import java.util.Arrays;
import java.util.List;

public class SetAttributesPacket extends PEPacket {
	private long entityId;
	private List<Attribute> attributes;

	public SetAttributesPacket(long entityId, List<Attribute> attributes) {
		this.entityId = entityId;
		this.attributes = attributes;
	}

	public SetAttributesPacket(long entityId, Attribute... attributes) {
		this.entityId = entityId;
		this.attributes = Arrays.asList(attributes);
	}


	@Override
	public int getPacketId() {
		return PEPacketIDs.SET_ATTRIBUTES;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		VarNumberSerializer.writeVarLong(serializer, entityId);
		encodeAttributes(connection, serializer, attributes);
	}

	public static void encodeAttributes(Connection connection, ByteBuf serializer, List<Attribute> attributes) {
		VarNumberSerializer.writeVarInt(serializer, attributes.size());
		for (Attribute attribute : attributes) {
			serializer.writeFloat(attribute.getMinimum());
			serializer.writeFloat(attribute.getMaximum());
			serializer.writeFloat(attribute.getValue());
			serializer.writeFloat(attribute.getDefaultValue()); //default value
			StringSerializer.writeString(serializer, connection.getVersion(), attribute.getName());
		}
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientData) { }

	public static class Attribute {
		private String name;
		private float minimum;
		private float maximum;
		private float value;
		private float defaultValue;

		public Attribute(String name, float minimum, float maximum, float value, float defaultValue) {
			this.name = name;
			this.minimum = minimum;
			this.maximum = maximum;
			this.value = value;
			this.defaultValue = defaultValue;
		}

		public String getName() {
			return name;
		}

		public float getMinimum() {
			return minimum;
		}

		public float getMaximum() {
			return maximum;
		}

		public float getValue() {
			return value;
		}

		public float getDefaultValue() {
			return defaultValue;
		}
	}
}