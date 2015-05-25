package com.databasetask.entity;

public class Student {
	private int _id;
	private int _age;
	private String _name;

	public Student() {
		
	}
	
	/**
	 * 数据库添加学生时
	 * ID有自增长 就不能添加
	 * @param _age
	 * @param _name
	 */
	public Student(int _age, String _name) {
		this._age = _age;
		this._name = _name;
	}
	
	/**
	 * 数据更新时
	 * 需要ID作为条件
	 * @param _id
	 * @param _age
	 * @param _name
	 */
	public Student(int _id,int _age, String _name) {
		this._id = _id;
		this._age = _age;
		this._name = _name;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int get_age() {
		return _age;
	}

	public void set_age(int _age) {
		this._age = _age;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	@Override
	public String toString() {
		return "id="+_id+"\tname="+_name+"\tage="+_age;
	}
}
