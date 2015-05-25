package com.databasetask.constant;

public class Constant {
	public static class Database{
		/** ���ݿ��� */
		public static final String DATABASE_NAME = "School.db";
		/** ���� */
		public static final String TABLE_NAME = "student";
		/** ���ݿ�汾 */
		public static final int VERSION = 1;
		/** ����Student�� */
		public  static final String CREATE_TABLE_SQL ="create table if not exists student(" +
				"_id integer primary key autoincrement," +
				"_name varchar(50) not null," +
				"_age integer not null)";
		/** ��ѯStudent����������� */
		public static final String DISPLAY_ALL_STUDENT = "select * from student";
	}

}
