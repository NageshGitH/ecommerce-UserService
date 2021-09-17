package com.userregistration.application.exception;

public class BankOperationsException extends RuntimeException
{
	  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	public BankOperationsException()
	  {
		  super();
	  }
	  public BankOperationsException(String message)
	  {
		  super(message);
	  }
	  
	  public BankOperationsException(String message, Throwable t)
	  {
		  super(message,t);
	  }
}
	  
