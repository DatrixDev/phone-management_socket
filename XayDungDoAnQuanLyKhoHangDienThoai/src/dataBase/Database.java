package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;

public final class Database {
	private Connection conn = null;
	private static Database instance = null;

	private static final String URL = "jdbc:mysql://localhost:3306/quanlykhohangdienthoai?useSSL=false&serverTimezone=UTC";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	private Database() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // Load driver MySQL
			this.conn = DriverManager.getConnection(URL, USER, PASSWORD);
			if (this.conn == null) {
				System.out.println("Lỗi kết nối tới MySQL");
			} else {
//				System.out.println("Kết nối MySQL thành công");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}

	public static Connection getConnected() {
		instance = getInstance();
		try {
			if (instance.getConn() == null || instance.getConn().isClosed()) {
				instance = new Database();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance.getConn();
	}

	public Connection getConn() {
		return conn;
	}

}
