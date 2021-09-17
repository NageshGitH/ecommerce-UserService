package com.userregistration.application.exception;

public class OutOfStockException extends RuntimeException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public OutOfStockException()
  {
	  super();
  }
  public OutOfStockException(String message)
  {
	  super(message);
  }
  
  public OutOfStockException(String message, Throwable t)
  {
	  super(message,t);
  }
  
  
}
