package GroupF2.gobang.base;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcConnection {
	private static ThreadLocal<Connection> conns = new ThreadLocal<>();

	/**
	 * Get connection
	 * @return null if connection fails, otherwise connection is successful
	 */
	public static Connection getConnection() {
		Connection conn = conns.get();
		if (conn == null) {
			try {
				// 1. Read the four basic pieces of information from the configuration file
				InputStream is = JdbcConnection.class.getClassLoader().getResourceAsStream("db.properties");
				Properties pros = new Properties();
				pros.load(is);

				String user = pros.getProperty("username");
				String password = pros.getProperty("password");
				String url = pros.getProperty("url");
				String driverClass = pros.getProperty("driver");

				// 2. Load driver
				Class.forName(driverClass);

				// 3. Get connection
				conn = DriverManager.getConnection(url, user, password);
				// Save to ThreadLocal object for later jdbc operations
				conns.set(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	/**
	 * Commit transaction
	 */
	public static void commit(Connection conn) {
		if (conn != null) {
			try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Rollback transaction
	 */
	public static void rollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Set transaction to manual commit
	 */
	public static void disableAutocommit(Connection conn) {
		if (conn != null) {
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Close database connection
	 */
	public static void close(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
				conns.remove();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * closeResource
	 */
	public static void closeResource(Connection conn,Statement ps){
		try {
			if(ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn != null) {
				conn.close();
				conns.remove();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * closeResource
	 */
	public static void closeResource(Connection conn,Statement ps,ResultSet rs){
		try {
			if(ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn != null) {
				conn.close();
				conns.remove();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
