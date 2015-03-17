package com.lutz.engine.data.types;

import com.lutz.engine.data.DataType;

public class ByteType extends DataType {

	@Override
	public Class<?> getTypeClass() {

		return Byte.class;
	}

	@Override
	public String getAbbreviation() {

		return "byte";
	}

	@Override
	public Object readType(String toRead) {

		try {

			return Byte.parseByte(toRead);

		} catch (Exception e) {

			return 0;
		}
	}

	@Override
	public String writeType(Object toWrite) {

		return toWrite.toString();
	}
}
