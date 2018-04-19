package protocolsupportpocketstuff.util;

import protocolsupport.api.Connection;

public class PacketUtils {

	public static class RunWhenOnline extends Thread {

		private Connection connection;
		private Runnable run;
		private int maxtries;

		public RunWhenOnline(Connection connection, Runnable run, int maxtries) {
			this.connection = connection;
			this.run = run;
			this.maxtries = maxtries;
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
			run.run();
		}

	}

}
