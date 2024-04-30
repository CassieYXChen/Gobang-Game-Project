package GroupF2.gobang.serviceDao;

import GroupF2.gobang.base.JdbcConnection;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao {
	// Common insert, update, and delete operations
	public int update(String sql, Object... args) {// Number of placeholders in SQL should match the length of varargs!
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// 1. Get a database connection
			conn = JdbcConnection.getConnection();
			// 2. Precompile the SQL statement and return an instance of PreparedStatement
			ps = conn.prepareStatement(sql);
			// 3. Fill in the placeholders
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);// Be cautious of parameter declaration errors!
			}
			// 4. Execute the statement
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5. Close resources
			JdbcConnection.closeResource(null, ps);

		}
		return 0;
	}

	/**
	 *
	 * @Description: Generic query operation
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> T select(Class<T> clazz, String sql, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcConnection.getConnection();

			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			rs = ps.executeQuery();
			// Get the metadata of the result set: ResultSetMetaData
			ResultSetMetaData rsmd = rs.getMetaData();
			// Get the number of columns in the result set
			int columnCount = rsmd.getColumnCount();

			if (rs.next()) {
				T t = clazz.newInstance();
				// Process each column in a row of the result set
				for (int i = 0; i < columnCount; i++) {
					// Get column value
					Object columnValue = rs.getObject(i + 1);

					// Get the column name of each column
					String columnLabel = rsmd.getColumnLabel(i + 1);
					if (columnValue.getClass().toString().equals("class java.time.LocalDateTime")) {
						columnValue = Timestamp.valueOf((LocalDateTime) columnValue);
					}
					// Set the value of the columnName property in the t object using reflection
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columnValue);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcConnection.closeResource(null, ps, rs);

		}

		return null;
	}

	// Generic query operation used to return a collection of multiple records from a data table (version 2.0: considering transactions)
	public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcConnection.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			rs = ps.executeQuery();
			// Get the metadata of the result set: ResultSetMetaData
			ResultSetMetaData rsmd = rs.getMetaData();
			// Get the number of columns in the result set
			int columnCount = rsmd.getColumnCount();
			// Create a collection object
			ArrayList<T> list = new ArrayList<T>();
			while (rs.next()) {
				T t = clazz.newInstance();
				// Process each column in a row of the result set: assign values to the specified properties in the t object
				for (int i = 0; i < columnCount; i++) {
					// Get column value
					Object columnValue = rs.getObject(i + 1);

					// Get the column name of each column
					String columnLabel = rsmd.getColumnLabel(i + 1);

					// Set the value of the columnName property in the t object using reflection
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columnValue);
				}
				list.add(t);
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcConnection.closeResource(null, ps, rs);

		}

		return null;
	}

	/**
	 * Generic method for querying special values, returns a value of type Long
	 */
	public <E> E getValue(String sql, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcConnection.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);

			}

			rs = ps.executeQuery();
			if (rs.next()) {
				return (E) rs.getObject(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcConnection.closeResource(null, ps, rs);

		}
		return null;

	}
}
