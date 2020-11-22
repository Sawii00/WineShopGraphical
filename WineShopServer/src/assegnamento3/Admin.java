package assegnamento3;

public class Admin extends LoggableUser
{

	public Admin(String name, String surname, String email, String password) {
		super(name, surname, email, password);
	}
	
	@Override
	public String getUserType() {
		// TODO Auto-generated method stub
		return "admin";
	}
	
	
	
}
