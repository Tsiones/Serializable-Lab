package com.github.serializable.exceptions;

@SuppressWarnings("serial")
public class InvalidIDException extends RuntimeException
{

	public InvalidIDException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvalidIDException(String message)
	{
		super(message);
	}

	public InvalidIDException(Throwable cause)
	{
		super(cause);
	}

}
