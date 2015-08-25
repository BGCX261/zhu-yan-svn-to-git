package com.model;



import java.sql.DriverManager;

import com.db.Xsb;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class DBConn {
	Connection conn;
	PreparedStatement pstmt;
	public DBConn() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn=(Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1/xscj", "root", "805614");
		} catch(Exception e){
			System.out.println("hello");
			e.printStackTrace();
		}
	}
	public boolean save(Xsb xs) {
		try {
			pstmt = (PreparedStatement) conn.prepareStatement("insert into xsb values(?,?,?,?,?,?)");
			pstmt.setString(1, xs.getXh());
			pstmt.setString(2, xs.getXm());
			pstmt.setString(3, xs.getXb());
			pstmt.setString(5, xs.getZy());
			pstmt.setDate(4, xs.getCssj());
			pstmt.setString(6, xs.getBz());
			pstmt.executeUpdate();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
