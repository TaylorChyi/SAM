package scenicSpot;

public class Path
{
	private String scenic;//与景点相连的景点
	private int distance;//从原景点到达该景点的路程
	private int time;//从原景点到达该景点的时间
	
	public Path()
	{
		super();
	}

	public Path(String scenic, int distance, int time)
	{
		super();
		this.scenic = scenic;
		this.distance = distance;
		this.time = time;
	}

	public String getScenic()
	{
		return scenic;
	}
	
	public void setScenic(String scenic)
	{
		this.scenic = scenic;
	}
	
	public int getDistance()
	{
		return distance;
	}
	
	public void setDistance(int distance)
	{
		this.distance = distance;
	}
	
	public int getTime()
	{
		return time;
	}
	
	public void setTime(int time)
	{
		this.time = time;
	}
	
	public String toString()
	{
		return this.scenic + "_" + distance + "_" + time;
	}
	
	public String toString(String scenic)
	{
		return scenic + "_" + this.scenic + "_" + distance + "_" + time;
	}
}
