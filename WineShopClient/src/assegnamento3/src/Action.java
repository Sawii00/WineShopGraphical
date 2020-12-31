package assegnamento3.src;

/**
 * Functional Interface that represents an action to be performed upon closure
 * of a ConfirmationBox.
 * 
 * @see ConfirmationBox
 **/
@FunctionalInterface
public interface Action
{
	/**
	 * Action to be performed.
	 **/
	void act();
}
