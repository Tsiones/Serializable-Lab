package com.github.serializable.passwordvalidation;

/**
 * @author EnderCrypt
 * this is the exception that is thrown when a password doesent meet the requirments of a password validator
 */
@SuppressWarnings("serial")
public class PasswordRequirmentsNotMet extends Exception
{
	public PasswordRequirmentsNotMet()
	{
		super();
	}
	
	public PasswordRequirmentsNotMet(String string)
	{
		super(string);
	}

}
