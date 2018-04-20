package protocolsupportpocketstuff.util;

import org.bukkit.Bukkit;

import protocolsupport.api.Connection;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;

public class PacketUtils {

	public static class RunWhenOnline extends Thread {

		private Connection connection;
		private Runnable run;
		private int maxtries;
		private boolean sync;
		private long logindelay;

		public RunWhenOnline(Connection connection, Runnable run, int maxtries) {
			this(connection, run, maxtries, false, 0L);
		}

		public RunWhenOnline(Connection connection, Runnable run, int maxtries, boolean sync, long logindelay) {
			this.connection = connection;
			this.run = run;
			this.maxtries = maxtries;
			this.sync = sync;
			this.logindelay = logindelay;
		}

		@Override
		public void run() {
			super.run();
			int tries = 0;
			while(connection.getPlayer() == null) {
				try {
					tries++;
					if (tries > maxtries) { return; }
					Thread.sleep(1000l);
				} catch (InterruptedException e) { }
			}
			if (sync) {
				Bukkit.getScheduler().runTaskLater(ProtocolSupportPocketStuff.getInstance(), run, tries > 0 ? logindelay : 0);
			} else {
				if (logindelay <= 0 || tries == 0) {
					run.run();
				} else {
					Bukkit.getScheduler().runTaskLaterAsynchronously(ProtocolSupportPocketStuff.getInstance(), run, logindelay);
				}
			}
		}

	}

}
