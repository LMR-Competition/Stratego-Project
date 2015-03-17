package com.lutz.engine.data;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.lutz.advlogging.Logger.Verbosity;
import com.lutz.engine.core.LutzEngine;
import com.lutz.engine.util.ExtendedMap;

public class PersistentDataManager {

	private static ExtendedMap<Class<?>, String, Object> data = new ExtendedMap<Class<?>, String, Object>();

	public static void putData(String key, Object value) {

		data.put(value.getClass(), key, value);
	}

	public static Object getData(String key) {

		return data.get(key);
	}

	public static int getDataAsInt(String key) {

		return getDataAsInt(key, 0);
	}

	public static int getDataAsInt(String key, int def) {

		Object o = getData(key);

		if (o != null) {

			if (o instanceof Integer) {

				return (Integer) o;
			}
		}

		return def;
	}

	public static boolean getDataAsBoolean(String key) {

		return getDataAsBoolean(key, false);
	}

	public static boolean getDataAsBoolean(String key, boolean def) {

		Object o = getData(key);

		if (o != null) {

			if (o instanceof Boolean) {

				return (Boolean) o;
			}
		}

		return def;
	}

	public static boolean hasData(String key) {

		return data.containsKey(key);
	}

	public static void writeData() {

		try {

			new File("engine/").mkdirs();

			File dataFile = new File("engine/data.dat");

			PrintStream ps = new PrintStream(dataFile);

			for (Class<?> type : data.typeSet()) {

				DataType dataType = DataTypes.getDataType(type);

				if (dataType != null) {

					for (String key : data.keySet(type)) {

						ps.println(dataType.getAbbreviation().toUpperCase()
								+ ":"
								+ key
								+ "="
								+ dataType.writeType(data.get(type, key))
										.replace("\n", "$(nl);")
										.replace("\r", "$(cr);"));
					}

				} else {

					LutzEngine
							.getEngineLogger()
							.warn("The data type '"
									+ type.getName()
									+ "' does not have a DataType registered for it.  No data entries of this type will be saved!",
									Verbosity.MINIMAL);
				}
			}

			ps.close();

		} catch (Exception e) {

			LutzEngine
					.getEngineLogger()
					.warn("There was an error while writing the persistent data!  Some data may now be missing or corrupt!",
							Verbosity.MINIMAL);

			LutzEngine.getEngineLogger().logException(e);
		}
	}

	public static void readData() {

		File dataFile = new File("engine/data.dat");

		if (dataFile.exists()) {

			try {

				List<String> lines = new ArrayList<String>();

				Scanner sc = new Scanner(dataFile);

				while (sc.hasNextLine()) {

					lines.add(sc.nextLine());
				}

				sc.close();

				for (int i = 0; i < lines.size(); i++) {

					String line = lines.get(i);

					if (line.contains("=")) {

						String[] parts = line.split("=", 2);

						if (parts[0].contains(":")) {

							String value = parts[1];

							String[] declParts = parts[0].split(":", 2);

							DataType type = DataTypes.getDataType(declParts[0]);

							if (type != null) {

								Object parsedValue = type.readType(value
										.replace("$(nl);", "\n").replace(
												"$(cr);", "\r"));

								putData(declParts[1], parsedValue);

							} else {

								LutzEngine
										.getEngineLogger()
										.warn("The data type abbreviation '"
												+ declParts[0].toUpperCase()
												+ "' does not have a DataType registered for it.",
												Verbosity.MINIMAL);
							}

						} else {

							LutzEngine
									.getEngineLogger()
									.warn("Line "
											+ (i + 1)
											+ " of the persistent data file could not be read due to formatting errors!",
											Verbosity.MINIMAL);
						}

					} else {

						LutzEngine
								.getEngineLogger()
								.warn("Line "
										+ (i + 1)
										+ " of the persistent data file could not be read due to formatting errors!",
										Verbosity.MINIMAL);
					}
				}

			} catch (Exception e) {

				LutzEngine
						.getEngineLogger()
						.warn("An error occurred while reading persistent data!  There may be some missing data!",
								Verbosity.MINIMAL);

				LutzEngine.getEngineLogger().logException(e);
			}
		}
	}
}
