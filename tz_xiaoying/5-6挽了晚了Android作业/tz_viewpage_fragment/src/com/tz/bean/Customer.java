package com.tz.bean;

import java.io.Serializable;

/**
 * 客户信息
 * @author Administrator
 *
 */
public class Customer implements Serializable {
	private String id;
	private String name;
	private String sex;
	private String phone;
	private String city;
	private String coment; //客户备注
	
	private int intent; //客户意向（0-,1-无意向,2-意向不明确,3-有意向）
	private int dialStatus; //电话拨打状态（0-未拨打,1-成功,2-未接通(无人接听),3-拒接,4-空号）
	                                  //       呼入状态（0-未接听,1-已接听）
	private String remark; //回访备注
	
	private int img; //显示图片

	public Customer() {
	} 

	public Customer(String id) {
		this.id = id;
	}

	public Customer(String id, String name, String phone) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getComent() {
		return coment;
	}

	public void setComent(String coment) {
		this.coment = coment;
	}

	public int getIntent() {
		return intent;
	}

	public void setIntent(int intent) {
		this.intent = intent;
	}

	public int getDialStatus() {
		return dialStatus;
	}

	public void setDialStatus(int dialStatus) {
		this.dialStatus = dialStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

}
