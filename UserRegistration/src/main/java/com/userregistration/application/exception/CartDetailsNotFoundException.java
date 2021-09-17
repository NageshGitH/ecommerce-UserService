package com.userregistration.application.exception;

public class CartDetailsNotFoundException 	extends RuntimeException
	{
		private static final long serialVersionUID = 1L;

		public CartDetailsNotFoundException(String message) {
			super(message);

		}

		public CartDetailsNotFoundException(String message, Throwable t) {
			super(message, t);

		}
}