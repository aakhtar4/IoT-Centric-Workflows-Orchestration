package connection;

import java.sql.*;

import coordination.Workflow_Coordinator_Main;

public class DBConnectionFactory {
	String url = null, driver = null, username = null, password = null;

	public Connection getConnection() {
		
		try {
			driver = "com.mysql.cj.jdbc.Driver";
			
			url = Workflow_Coordinator_Main.configReader.get_DB_URL();
			username = Workflow_Coordinator_Main.configReader.get_DB_Username();
			password = Workflow_Coordinator_Main.configReader.get_DB_Password();

			Class.forName(driver).newInstance();
			return DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			System.out.println("ConnectionFactory: " + e.toString() + 
					"\n URL: " + url + 
					"\n Driver: " + driver );
			throw new RuntimeException(e);
		}
	}

	public void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void rollback(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
