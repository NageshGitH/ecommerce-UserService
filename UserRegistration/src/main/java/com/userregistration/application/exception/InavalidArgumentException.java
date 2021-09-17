package com.userregistration.application.exception;

public class InavalidArgumentException extends RuntimeException
{


	public InavalidArgumentException()
	  {
		  super();
	  }
	  public InavalidArgumentException(String message)
	  {
		  super(message);
	  }
	  
	  public InavalidArgumentException(String message, Throwable t)
	  {
		  super(message,t);
	  }
}