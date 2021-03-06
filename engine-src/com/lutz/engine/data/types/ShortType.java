package com.lutz.engine.data.types;

import com.lutz.engine.data.DataType;

public class ShortType extends DataType {

	@Override
	public Class<?> getTypeClass() {

		return Short.class;
	}

	@Override
	public String getAbbreviation() {

		return "shrt";
	}

	@Override
	public Object readType(String toRead) {

		try {

			return Short.parseShort(toRead);

		} catch (Exception e) {

			return (short) 0;
		}
	}

	@Override
	public String writeType(Object toWrite) {

		return toWrite.toString();
	}
}
