package com.capgemini.core.pw.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.capgemini.core.pw.beans.Customer;
import com.capgemini.core.pw.beans.Wallet;
import com.capgemini.core.pw.exception.InsufficientBalanceException;
import com.capgemini.core.pw.exception.InvalidInputException;
import com.capgemini.core.pw.DAO.WalletRepo;
import com.capgemini.core.pw.DAO.WalletRepoImpl;

public class WalletServiceImpl implements WalletService{
private WalletRepo repo;
	
	public WalletServiceImpl(Map<String, Customer> data)
	{
		repo= new WalletRepoImpl(data);
		
	}
	
	public WalletServiceImpl(WalletRepo repo) 
	{
		super();
		this.repo = repo;
	}

	public WalletServiceImpl()
	{
		repo= new WalletRepoImpl();
	}

public static boolean isMobileNoInvalid(String mobileNo) {
	
		if(String.valueOf(mobileNo).matches("[1-9][0-9]{9}")) 
		{
			return false;
		}		
		else 
			return true;
	}


public static boolean isNameInvalid(String name) {
	if(String.valueOf(name).matches("[A-Z][a-z]"))
	{return false;}
	else
		return true;
	
}
public static boolean isAmountInvalid(double amount) {
	while(amount>=0)
	{return false;}

		return true;
		
}

	public Customer showBalance(String mobileNo)
	{
		Customer customer=repo.findOne(mobileNo);
		if(customer!=null)
			return customer;
		else
			throw new InvalidInputException("Invalid mobile no ");
	}
	
	@Override
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) 
	{
		if(name==null ||mobileNo==null)
			throw new NullPointerException();
		Customer customer = new Customer(name, mobileNo, new Wallet(amount));
		
		if(repo.save(customer)==false)
			throw new InvalidInputException("");
			
			
		
		return customer;
	}
	
	@Override
	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount)
	{
		Customer customer = null;
		Customer customer1 = null;
		customer = withdrawAmount(sourceMobileNo,amount);
		
		if(repo.save(customer)==false)
			throw new InvalidInputException("");
		
		customer1 =depositAmount(targetMobileNo,amount);
		
		if(repo.save(customer)==false)
			throw new InvalidInputException("");
		
		
		return customer;
	}
	
	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount)
	{
		Customer customer= repo.findOne(mobileNo);
		if(customer!=null)
		customer.getWallet().setBalance(customer.getWallet().getBalance().add(amount));

		if(repo.save(customer)==false)
			throw new InvalidInputException("");
		
		return customer;
	}
	
	@Override
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) 
	{
		Customer customer= repo.findOne(mobileNo);
		if(customer!=null)
		customer.getWallet().setBalance(customer.getWallet().getBalance().subtract(amount));
		
		if(repo.save(customer)==false)
			throw new InvalidInputException("");
		
		return customer;
		
	}


}
