package assegnamento3.src;

/**
 * The enum {@code StatusCode} encodes the possible return values of a server
 * request.
 **/
public enum StatusCode
{
	/**
	 * Represents a successful transaction.
	 **/
	SUCCESS,
	/**
	 * Invalid arguments were received.
	 **/
	INVALID_ARGUMENTS,
	/**
	 * The user was not authorized to perform the request.
	 **/
	NOT_AUTHORIZED,
	/**
	 * Represents a generic error.
	 **/
	ERROR
}