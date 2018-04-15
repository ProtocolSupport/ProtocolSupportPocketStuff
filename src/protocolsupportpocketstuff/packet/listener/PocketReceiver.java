package protocolsupportpocketstuff.packet.listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.api.Connection.PacketListener;
import protocolsupport.api.utils.Any;
import protocolsupportpocketstuff.packet.PEPacket;
import protocolsupportpocketstuff.util.serializer.PacketSerializer;

public class PocketReceiver extends PacketListener {

	private static final Map<Integer, Any<PEPacket, Set<Handler>>> packetHandlers = new Int2ObjectArrayMap<>();

	/**
	 * Registers all pocket packet handlers in a pocket packet listener.
	 * @param listener
	 */
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

	/**
	 * Adds a pocket packet handler
	 * @param packet
	 * @param handler
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void addHandler(PEPacket packet, Handler handler) {
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

	private Connection connection;

	public PocketReceiver(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void onRawPacketReceiving(RawPacketEvent event) {
		ByteBuf clientdata = event.getData().copy();
		int packetId = PacketSerializer.readPacketId(clientdata);
		Any<PEPacket, Set<Handler>> handlers = packetHandlers.get(packetId);
		if (handlers == null) { return; }
		PEPacket packet = handlers.getObj1();
		packet.decode(connection, clientdata);
		handlers.getObj2().forEach(handler -> {
			handler.handle(connection, packet);
		});
	}

}
