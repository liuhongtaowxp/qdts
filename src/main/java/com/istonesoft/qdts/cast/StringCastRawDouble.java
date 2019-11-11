package com.istonesoft.qdts.cast;

public class StringCastRawDouble implements StringCast {

	@Override
	public Object castToVal(String str, String parameterType) {
		return Double.parseDouble(str);
	}

	@Override
	public Class castToClass() {
		return double.class;
	}

}

