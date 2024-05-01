package GroupF2.gobang.serviceDao;
import GroupF2.gobang.pojo.Sinfo;

import java.util.List;

/**
 * Combined class for handling service and data access operations related to Sinfo.
 */
public class SinfoServiceDaoImpl extends BaseDao {

	/**
	 * Saves Sinfo details into the database.
	 * @param sinfo the Sinfo object containing account and address information
	 * @return the number of rows affected by the insert operation
	 */
	public int saveSinfo(Sinfo sinfo) {
		String sql = "insert into sinfo values(null, ?, ?, ?)";
		return update(sql, sinfo.getAccount(), sinfo.getAddress(), 0);
	}

	/**
	 * Queries the IP address associated with a given account.
	 * @param account the account identifier
	 * @return Sinfo object containing the account and address details
	 */
	public Sinfo queryIPByAccount(String account) {
		String sql = "select address, status from sinfo where account = ?";
		return select(Sinfo.class, sql, account);
	}

	/**
	 * Deletes an Sinfo record based on the account identifier.
	 * @param account the account to delete
	 * @return the number of rows affected
	 */
	public int deleteSinfoByAccount(String account) {
		String sql = "delete from sinfo where account = ?";
		return update(sql, account);
	}

	/**
	 * Queries all accounts with a status other than zero.
	 * @return a list of Sinfo objects containing account details
	 */
	public List<Sinfo> queryAllAccount() {
		String sql = "select account, status from sinfo where status != 0";
		return getForList(Sinfo.class, sql);
	}

	/**
	 * Updates the status associated with an account.
	 * @param account the account identifier
	 * @param status the new status
	 * @return the number of rows affected
	 */
	public int updateStatusByAccount(String account, int status) {
		String sql = "update sinfo set status = ? where account = ?";
		return update(sql, status, account);
	}

	/**
	 * Updates the IP address associated with an account.
	 * @param account the account identifier
	 * @param ip the new IP address
	 * @return the number of rows affected
	 */
	public int updateIPByAccount(String account, String ip) {
		String sql = "update sinfo set address = ? where account = ?";
		return update(sql, ip, account);
	}

	/**
	 * Queries a limited list of accounts with non-zero status.
	 * @param index the start index for the query
	 * @param size the number of records to return
	 * @return a list of Sinfo objects
	 */
	public List<Sinfo> queryAllByLimit(int index, int size) {
		String sql = "select account, status from sinfo where status != 0 limit ?, ?";
		return getForList(Sinfo.class, sql, index, size);
	}

	/**
	 * Queries the total count of accounts with non-zero status.
	 * @return the total count of qualifying accounts
	 */
	public Long queryAllCount() {
		String sql = "select count(*) from sinfo where status != 0";
		return getValue(sql);
	}
}
