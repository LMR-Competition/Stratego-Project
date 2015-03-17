package com.lutz.engine.data.types.special;

import com.lutz.engine.core.LutzEngine;
import com.lutz.engine.data.DataType;
import com.lutz.engine.ui.graphics.resolutions.ScreenResolution;

public class ResolutionType extends DataType {

	@Override
	public Class<?> getTypeClass() {

		return ScreenResolution.class;
	}

	@Override
	public String getAbbreviation() {

		return "scr_res";
	}

	@Override
	public Object readType(String toRead) {

		if (toRead.contains("x")) {

			String[] strParts = toRead.split("x", 2);

			String possibleProblem = "null";

			int width = 0, height = 0;

			try {

				possibleProblem = strParts[0];
				width = Integer.parseInt(strParts[0]);

				possibleProblem = strParts[1];
				height = Integer.parseInt(strParts[1]);

				return ScreenResolution.getScreenResolutionForDimensions(width,
						height);

			} catch (Exception e) {

				LutzEngine.getEngineLogger().warn(
						"Malformed screen resolution dimension '"
								+ possibleProblem + "'!");
			}
		}

		return ScreenResolution.getDefaultResolution();
	}

	@Override
	public String writeType(Object toWrite) {

		ScreenResolution r = (ScreenResolution) toWrite;

		return r.getWidth() + "x" + r.getHeight();
	}

}
