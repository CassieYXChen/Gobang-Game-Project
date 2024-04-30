package GroupF2.gobang.serviceDao;

import GroupF2.gobang.pojo.Address;

/**
 * Combined class that handles service logic and data access for Addresses.
 * @author CUHK CSCI3100 2023-24 Group F2
 */
public class AddressServiceDaoImpl extends BaseDao {

    public Address queryAccountByIP(String address) {
        String sql = "select id, account, address, remember from address_user where address = ?";
        return select(Address.class, sql, address);
    }

    public int saveAddress(Address address) {
        String sql = "insert into address_user values(null, ?, ?, ?)";
        return update(sql, address.getAccount(), address.getAddress(), 0);
    }

    public int updateRemember(int remember, String account) {
        String sql = "update address_user set remember = ? where account = ?";
        return update(sql, remember, account);
    }

    public int updateAccount(String ip, String account) {
        String sql = "update address_user set account = ? where address = ?";
        return update(sql, account, ip);
    }
}
