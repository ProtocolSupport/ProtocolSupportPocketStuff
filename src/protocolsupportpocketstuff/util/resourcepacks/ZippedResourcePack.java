package protocolsupportpocketstuff.util.resourcepacks;

import org.apache.commons.io.IOUtils;
import protocolsupport.libs.com.google.gson.JsonArray;
import protocolsupport.libs.com.google.gson.JsonElement;
import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupportpocketstuff.util.StuffUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZippedResourcePack implements ResourcePack {
	File file;
	private String name;
	private String uuid;
	private String version;
	private byte[] hash;

	public ZippedResourcePack(File file) {
		this.file = file;

		JsonObject manifest = null;

		// Read manifest from ZIP
		try {
			ZipFile zipFile;
			zipFile = new ZipFile(file);

			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			while(entries.hasMoreElements()){
				ZipEntry entry = entries.nextElement();
				if (entry.getName().equals("pack_manifest.json"))
					throw new InvalidResourcePackException("Unsupported old pack format");

				if (!entry.getName().equals("manifest.json"))
					continue;

				InputStream stream = zipFile.getInputStream(entry);
				StringWriter writer = new StringWriter();
				IOUtils.copy(stream, writer, "UTF-8");
				String manifestText = writer.toString();
				manifest = StuffUtils.JSON_PARSER.parse(manifestText).getAsJsonObject();
				stream.close();
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (manifest == null)
			throw new InvalidResourcePackException("Unsupported pack format");

		JsonObject header = manifest.getAsJsonObject("header");
		this.name = header.get("name").getAsString();
		this.uuid = header.get("uuid").getAsString();

		JsonArray versionArray = header.get("version").getAsJsonArray(); // This is a JSON array
		// [3, 2, 0]
		// We need to convert it to 3.2.0
		StringBuilder version = new StringBuilder();
		int idx = 0;
		for (JsonElement element : versionArray) {
			version.append(element.getAsString());
			idx++;
			if (versionArray.size() > idx) {
				version.append(".");
			}
		}
		this.version = version.toString();

		// Get SHA-256 of the file
		try {
			byte[] buffer = new byte[8192];
			int count;
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			while ((count = bis.read(buffer)) > 0) {
				digest.update(buffer, 0, count);
			}
			this.hash = digest.digest();
		} catch (Exception e) {
			throw new InvalidResourcePackException("Couldn't get the SHA256 from archive");
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPackId() {
		return uuid;
	}

	@Override
	public int getPackSize() {
		return (int) file.length();
	}

	@Override
	public String getPackVersion() {
		return version;
	}

	@Override
	public byte[] getSha256() {
		return hash;
	}

	@Override
	public byte[] getPackChunk(int chunkIdx) {
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			int arraySize = 1048576;
			int offset = 1048576 * chunkIdx;

			int distanceToTheEnd = (int) raf.length() - offset;

			if (arraySize > distanceToTheEnd) {
				arraySize = distanceToTheEnd;
			}

			byte[] array = new byte[arraySize];

			raf.seek(offset);
			raf.read(array, 0, arraySize);

			return array;
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Something went very wrong");
	}
}
