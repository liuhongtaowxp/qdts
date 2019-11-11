package com.istonesoft.qdts.cast;

public class StringCastInt implements StringCast {

	@Override
	public Object castToVal(String str, String parameterType) {
		return Integer.parseInt(str);
	}

	@Override
	public Class castToClass() {
		return int.class;
	}

}
