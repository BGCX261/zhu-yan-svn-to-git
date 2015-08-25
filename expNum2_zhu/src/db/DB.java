package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import model.LyTable;
import model.User;

public class DB {
	Connection ct;
	PreparedStatement psmt;

	// �ڹ��캯���н��������ݿ�����ӣ������ڽ���DB�����ʱ������������ݿ�
	public DB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			ct = DriverManager.getConnection("jdbc:mysql://localhost:3306/JSP",
					"root", "890623");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����username��password��ѯ�û����鵽�ͷ��ظö���û�оͷ��ؿ�
	public User CheckUser(String username, String password) {
		try {
			psmt = ct
					.prepareStatement("select * from userTable where username=? and password =?");
			psmt.setString(1, username);
			psmt.setString(2, password);
			ResultSet rs = psmt.executeQuery();
			User user = new User();
			while (rs.next()) {
				user.setId(rs.getInt(1));
				user.setUsername(rs.getString(2));
				user.setPassword(rs.getString(3));
				return user;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ��ѯ������Ϣ������һ��ArrayList
	public ArrayList<LyTable> findLyInfo() {
		try {
			ArrayList<LyTable> al = new ArrayList<LyTable>();
			psmt = ct.prepareStatement("select * from lyTable");
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				LyTable ly = new LyTable();
				ly.setId(rs.getInt(1));
				ly.setUserId(rs.getInt(2));
				ly.setDate(rs.getDate(3));
				ly.setTitle(rs.getString(4));
				ly.setContent(rs.getString(5));
				al.add(ly);
			}
			return al;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ��ȡ��ӦuserId���û����û���
	public String getUserName(int id) {
		String username = null;
		try {
			psmt = ct
					.prepareStatement("select username from userTable where id=?");
			psmt.setInt(1, id);
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				username = rs.getString("username");
			}
			return username;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ����������Ϣ
	public boolean addInfo(LyTable ly) {
		try {
			psmt = ct
					.prepareStatement("insert into lyTable(UserId,Date,Title,Content) select ?,?,?,?");
			psmt.setInt(1, ly.getUserId());
			psmt.setDate(2, ly.getDate());
			psmt.setString(3, ly.getTitle());
			psmt.setString(4, ly.getContent());
			psmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// �����û���������
	public boolean insertUser(String username, String pwd) {
		try {
			psmt = ct.prepareStatement("insert into userTable(username,password) select ?,?");
			psmt.setString(1, username);
			psmt.setString(2, pwd);
			psmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
