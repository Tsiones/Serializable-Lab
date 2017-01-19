package com.github.serializable.exceptions;

@SuppressWarnings("serial")
public class PriceOutOfBoundsException extends RuntimeException
{

	public PriceOutOfBoundsException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PriceOutOfBoundsException(String message)
	{
		super(message);
	}

	public PriceOutOfBoundsException(Throwable cause)
	{
		super(cause);
	}

}
