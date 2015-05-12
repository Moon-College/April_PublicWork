package com.tz.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 通话记录
 * @author Administrator
 *
 */
public class CdrMoc implements Serializable {
	private int calltype = 0; //呼叫类型（0-呼入,1-呼出）
	private Date calldate; //呼叫时间
	private int duration = 0; //通话时长
	private Customer customer; //呼叫的客户

	public CdrMoc() { 
	}
	
	public CdrMoc(int calltype) {
		this.calltype = calltype;
	}
	
	public CdrMoc(int calltype, Date calldate, int duration) {
		super();
		this.calltype = calltype;
		this.calldate = calldate;
		this.duration = duration;
	}

	public int getCalltype() {
		return calltype;
	}
	public void setCalltype(int calltype) {
		this.calltype = calltype;
	}
	
	public Date getCalldate() {
		return calldate;
	}
	
	public void setCalldate(Date calldate) {
		this.calldate = calldate;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
