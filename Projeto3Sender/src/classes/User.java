package classes;

public class User{

	private String username;
	private String password;
	private boolean admin;
	private boolean activated;
	public User(String username, String password) {
		this.username=username;
		this.password=password;
		this.admin=false;
		this.activated=true;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public boolean isActivated() {
		return activated;
	}
	public void setActivated(boolean activated) {
		this.activated = activated;
	}

}
