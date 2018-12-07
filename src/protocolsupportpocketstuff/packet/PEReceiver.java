package protocolsupportpocketstuff.packet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.api.Connection.PacketListener;
import protocolsupport.api.utils.Any;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupportpocketstuff.api.util.PocketPacketHandler;
import protocolsupportpocketstuff.api.util.PocketPacketListener;
import protocolsupportpocketstuff.util.packet.serializer.PacketSerializer;

public class PEReceiver {

	protected static final Map<Integer, Any<PEPacket, Set<Handler>>> packetHandlers = new HashMap<>();

    //=====================================================\\
    //						Registration				   \\
    //=====================================================\\

	@Deprecated
	@SuppressWarnings("unchecked")
	public static void registerPacketListeners(PocketPacketListener listener) {
		Class<? extends PocketPacketListener> clazz = listener.getClass();
		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(PocketPacketHandler.class)) {
				try {
					Class<? extends PEPacket> packetClazz = (Class<? extends PEPacket>) method.getParameterTypes()[1];
					PEPacket packet = packetClazz.newInstance();
					addHandler(packet, new Handler(listener, method));
				} catch (InstantiationException | IllegalAccessException | ClassCastException e) {
					e.printStackTrace();
				}
			}
		}
	}

    //=====================================================\\
    //						Handlers					   \\
    //=====================================================\\

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void addHandler(PEPacket packet, Handler handler) {
		int packetId = packet.getPacketId();
		Any<PEPacket, Set<Handler>> any = packetHandlers.get(packetId);
		if (any == null) {
			Set<Handler> handlers = new HashSet<Handler>();
			handlers.add(handler);
			packetHandlers.put(packetId, new Any(packet, handlers));
		} else {
			Set<Handler> handlers = any.getObj2();
			handlers.add(handler);
		}
	}

	public static class Handler {

		private PocketPacketListener listener;
		private Method method;

		public Handler(PocketPacketListener listener, Method method) {
			this.listener = listener;
			this.method = method;
		}

		public void handle(Connection connection, PEPacket packet) {
			try {
				method.invoke(listener, connection, packet);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}

    //=====================================================\\
    //						Receiver					   \\
    //=====================================================\\

	protected static void decodehandle(ConnectionImpl connection, ByteBuf clientdata) {
		int packetId = PacketSerializer.readPacketId(clientdata);
		Any<PEPacket, Set<Handler>> handlers = packetHandlers.get(packetId);
		if (handlers == null) { return; }
		PEPacket packet = handlers.getObj1();
		packet.decode(connection, clientdata);
		handlers.getObj2().forEach(handler -> {
			handler.handle(connection, packet);
		});
	}

	public static class PEReceiverListener extends PacketListener {

		private ConnectionImpl connection;

		public PEReceiverListener(ConnectionImpl connection) {
			this.connection = connection;
		}

		@Override
		public void onRawPacketReceiving(RawPacketEvent event) {
			if (connection.getVersion() == null) {
				return; //Keep going until version type is known.
			} else if (connection.getVersion() == ProtocolVersion.MINECRAFT_PE) {
				decodehandle(connection, event.getData().copy()); //If version is PE, handle the packet.
			} else {
				connection.removePacketListener(this); //If version turns out not to be PE stop listening.
			}
		}

	}

}
