package com.github.serializable.passwordvalidation;

/**
 * @author EnderCrypt
 * this is the <tt>PasswordValidator</tt> interface that allow you to create your own password validators
 * for passwordValidationService 
 */
public interface PasswordValidator
{

	/**
	 * this is called just before testing a new password, usually when you wanna reset all variables to 0
	 */
	public void reset();
	/**
	 * this method will get activated once for every character in a password, and your allowed to throw the
	 * PasswordRequirmentsNotMet exception if you see a violation of the rules you want
	 * @param a character from the password (in sequence)
	 * @throws PasswordRequirmentsNotMet
	 */
	public void register(char c) throws PasswordRequirmentsNotMet;
	/**
	 * this is the final method which is called once the whole password has gonna through, allowing you to do
	 * a final check on all values and throw the <tt>PasswordRequirmentsNotMet</tt> exception if any rules are violated
	 * @throws PasswordRequirmentsNotMet
	 */
	public void validate() throws PasswordRequirmentsNotMet;

}
