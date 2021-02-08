package parking;

public class Car
{
	private String number;//车牌号
	private String time;//入库时间
	
	public Car()
	{
		super();
	}

	public Car(String number)
	{
		super();
		this.number = number;
	}
	
	public Car(String number, String time)
	{
		super();
		this.number = number;
		this.time = time;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}
		
	public String toString()
	{
		return number + "_" + time;
	}
	
}
