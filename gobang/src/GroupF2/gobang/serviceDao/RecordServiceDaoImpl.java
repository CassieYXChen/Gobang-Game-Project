package GroupF2.gobang.serviceDao;

import GroupF2.gobang.pojo.Record;

/**
 * Combined implementation class that handles both the service and data access
 * operations for game records.
 */
public class RecordServiceDaoImpl extends BaseDao {

	/**
	 * Saves the game record information to the database. This method constructs
	 * an SQL query to insert the record details into the chess_record table.
	 * @param record The Record object containing information about the game.
	 * @return The number of database rows affected by the operation.
	 */
	public int saveRecord(Record record) {
		String sql = "insert into chess_record values(?,?,?,?,?)";
		return update(sql, null, record.getBlack(), record.getWhite(), record.getChessTime(), record.getResult());
	}

}
