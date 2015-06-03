package in.co.sunrays.lbts.dto;

public class UserDTO {

	public static final String TABLE_NAME = "user";

	public static final String ID = "ID";
	public static final String FIRST_NAME = "FISRT_NAME";
	public static final String LAST_NAME = "LAST_NAME";
	public static final String LOGIN = "LOGIN";
	public static final String PASSWORD = "PASSWORD";
	public static final String ADDRESS = "ADDRESS";
	public static final String MOBILE_NO = "MOBILE_NO";
	public static final String LATITUDE = "LAST_LATTITUDE";
	public static final String LONGITUDE = "LAST_LONGITUDE";

	private int id;
	private String firstName;
	private String lastName;
	private String login;
	private String password;
	private String address;
	private String mobileNo;
	private double lattitude;
	private double longitude;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public double getLattitude() {
		return lattitude;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}