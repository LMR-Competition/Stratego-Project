package com.lutz.engine.data;

import java.util.HashMap;
import java.util.Map;

public class DataTypes {

	private static Map<Class<?>, DataType> dataTypes = new HashMap<Class<?>, DataType>();
	
	public static void registerDataType(DataType type){
		
		dataTypes.put(type.getTypeClass(), type);
	}
	
	public static DataType[] getDataTypes(){
		
		return dataTypes.values().toArray(new DataType[]{});
	}

	public static DataType getDataType(Class<?> c) {

		if (dataTypes.containsKey(c)) {

			return dataTypes.get(c);
		}

		return null;
	}

	public static DataType getDataType(String abbrev) {

		for (DataType type : dataTypes.values()) {

			if (type.getAbbreviation().equalsIgnoreCase(abbrev)) {

				return type;
			}
		}

		return null;
	}
}
