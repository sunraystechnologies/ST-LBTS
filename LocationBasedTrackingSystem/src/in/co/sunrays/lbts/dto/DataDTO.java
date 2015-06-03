package in.co.sunrays.lbts.dto;

public class DataDTO {
	public static final String TABLE_NAME = "dailyRoutineData";
	public static final String TABLE_NAME1 = "favouritePlace";

	public static final String TASK_ID = "ID";
	public static final String TASK_NAME = "taskName";
	public static final String TASK_DATE = "taskDate";
	public static final String TASK_LOCATION = "taskLocation";
	public static final String TASK_RADIUS = "taskRadius";
	public static final String TASK_NEARBY = "taskNearby";
	public static final String TASK_TIME = "taskTime";
	public static final String Latitude = "latitude";
	public static final String Longitude = "longitude";

	private int ID;
	private String taskName;
	private String taskDate;
	private String taskLocation;
	private int taskRadius;
	private String taskNearby;
	private String taskTime;
	private double latitude;
	private double longitude;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

	public String getTaskLocation() {
		return taskLocation;
	}

	public void setTaskLocation(String taskLocation) {
		this.taskLocation = taskLocation;
	}

	public int getTaskRadius() {
		return taskRadius;
	}

	public void setTaskRadius(int taskRadius) {
		this.taskRadius = taskRadius;
	}

	public String getTaskNearby() {
		return taskNearby;
	}

	public void setTaskNearby(String taskNearby) {
		this.taskNearby = taskNearby;
	}

	public String getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(String taskTime) {
		this.taskTime = taskTime;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
