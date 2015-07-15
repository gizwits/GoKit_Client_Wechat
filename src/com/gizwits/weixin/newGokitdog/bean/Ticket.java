package com.gizwits.weixin.newGokitdog.bean;

public class Ticket
 {
     public String tvalue;
     public long ttime;
   
	public String getValue() {
		return tvalue;
	}
	public void setValue(String val) {
		tvalue = val;
	}
	public long getTime() {
		return ttime;
	}
	public void setTime(long time) {
		ttime = time;
	}
 }