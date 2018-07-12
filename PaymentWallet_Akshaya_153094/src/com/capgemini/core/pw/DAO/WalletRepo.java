package com.capgemini.core.pw.DAO;
import com.capgemini.core.pw.beans.Customer;
public interface WalletRepo {public boolean save(Customer customer);

public Customer findOne(String mobileNo);

}
