package org.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.model.Xsb;

public class DBConn {
	Connection conn;
	PreparedStatement pstmt;
	public DBConn(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/XSCJ","root","890623");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Ìí¼ÓÑ§Éú
	public boolean save(Xsb xs){
		try {
			pstmt=conn.prepareStatement("insert into XSB values(?,?,?,?,?,?)");
			pstmt.setString(1, xs.getXh());
			pstmt.setString(2, xs.getXm());
			pstmt.setByte(3, xs.getXb());
			pstmt.setDate(4, xs.getCssj());
			pstmt.setString(5, xs.getZy());			
			pstmt.setString(6, xs.getBz());
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
