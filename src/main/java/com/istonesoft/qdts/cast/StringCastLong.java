package com.istonesoft.qdts.cast;

public class StringCastLong implements StringCast {

	@Override
	public Object castToVal(String str, String parameterType) {
		return Long.parseLong(str);
	}

	@Override
	public Class castToClass() {
		return Long.class;
	}

}

