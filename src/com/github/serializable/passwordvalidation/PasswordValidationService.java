package com.github.serializable.passwordvalidation;

/**
 * @author EnderCrypt
 * This is the main password validation service, it requires a <tt>passwordValidator</tt> class to function
 * any class can implement the <tt>PasswordValidator</tt> interface and be sent in
 * the password validator is what is used to test if a password is valid, and can throw a
 * PasswordRequirmentsNotMet exception if the password breaks any rule
 */
public class PasswordValidationService
{
	private PasswordValidator passwordValidator;

	public PasswordValidationService(PasswordValidator passwordValidator)
	{
		this.passwordValidator = passwordValidator;
	}

	/**
	 * this is the method that checks if a password string is valid and follows the rules of the <tt>passwordValidator</tt>
	 * @param a string representation of the password that is to be checked
	 * @throws PasswordRequirmentsNotMet
	 */
	public synchronized void validate(String password) throws PasswordRequirmentsNotMet
	{
		passwordValidator.reset();
		for (int i = 0; i < password.length(); i++)
		{
			passwordValidator.register(password.charAt(i));
		}
		passwordValidator.validate();
	}
	
	
}
