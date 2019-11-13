package com.istonesoft.qdts.cast;

public class StringCastRawLong implements StringCast {

	@Override
	public Object castToVal(String str, String parameterType) {
		return Long.parseLong(str);
	}

	@Override
	public Class<?> castToClass() {
		return long.class;
	}

}

