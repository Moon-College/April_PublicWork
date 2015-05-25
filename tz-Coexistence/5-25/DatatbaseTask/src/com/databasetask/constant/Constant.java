package com.databasetask.constant;

public class Constant {
	public static class Database{
		/** 数据库名 */
		public static final String DATABASE_NAME = "School.db";
		/** 表名 */
		public static final String TABLE_NAME = "student";
		/** 数据库版本 */
		public static final int VERSION = 1;
		/** 创建Student表 */
		public  static final String CREATE_TABLE_SQL ="create table if not exists student(" +
				"_id integer primary key autoincrement," +
				"_name varchar(50) not null," +
				"_age integer not null)";
		/** 查询Student表的所有数据 */
		public static final String DISPLAY_ALL_STUDENT = "select * from student";
	}

}
