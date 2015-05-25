package com.example.bean;

public class Student {
	private int _id;
	private String name;
	private String grade;

	public Student() {

	}

	public Student(int _id, String name, String grade) {
		super();
		this._id = _id;
		this.name = name;
		this.grade = grade;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return "Student [_id=" + _id + ", name=" + name + ", grade=" + grade
				+ "]";
	}

}
