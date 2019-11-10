package com.istonesoft.qdts.resource;
/**
 * controller层的返回值，必须使用
 * @author issuser
 *
 */
public class QdResult {
	//返回结果
	private Object result;
	//错误信息
	private String msg;
	//1成功   0失败
	private String flag;
	
	public QdResult() {
		super();
	}
	public QdResult(Object result, String msg, String flag) {
		super();
		this.result = result;
		this.msg = msg;
		this.flag = flag;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "QdResult [result=" + result + ", msg=" + msg + ", flag=" + flag + "]";
	}
	
	
}
