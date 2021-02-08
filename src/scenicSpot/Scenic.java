package scenicSpot;

import structure.SeqList;

public class Scenic
{
	private String name;//景点名称
	private String info;//景点简介
	private int popularity;//景点欢迎度
	private int lounge;//有无休息区
	private int toilet;//有无公厕
	private SeqList<Path> adjacency = new SeqList<Path>();//与该景点相连的所有景点
	
	private int span = Integer.MAX_VALUE;//到达目的景点所需路程
	private String former = "";//确定最少费用路径后途径的上一个景点
	private SeqList<Path> sibLing = new SeqList<Path>();//所有子景点
	private boolean visited = false;//是否被访问过
	
	public Scenic()
	{
		super();
	}

	public Scenic(String name)
	{
		super();
		this.name = name;
	}
	
	public Scenic(String name, String info, int popularity, int lounge, int toilet)
	{
		super();
		this.name = name;
		this.info = info;
		this.popularity = popularity;
		this.lounge = lounge;
		this.toilet = toilet;
	}

	public Scenic(String name, String info, int popularity, int lounge, int toilet, SeqList<Path> adjacency)
	{
		super();
		this.name = name;
		this.info = info;
		this.popularity = popularity;
		this.lounge = lounge;
		this.toilet = toilet;
		this.adjacency = adjacency;
	}

	//添加路径
	public void add(Path path)
	{
		this.adjacency.add(path);
	}
	
	//移除到达某景点的路径
	public boolean remove(String scenic)
	{
		for(Path p : adjacency)
		{
			if(p.getScenic().equals(scenic))
			{
				return adjacency.remove(p);
			}
		}
		return false;
	}
	
	//移除路径
	public void remove(Path path)
	{
		this.adjacency.remove(path);
	}
	
	//比较两景点大小
	public int CompareTo(String standard, Scenic scenic)
	{
		switch(standard) {
		case "P" :  return Popularity(scenic);//景点欢迎度
		   			
		case "B" :  return Branch(scenic);//景点分叉路口数
							
		case "RT":  return RT(scenic);//景点有无休息区或公厕			
		
		default	 :	return 0;
		}
	}
	
	//按欢迎度比较
	public int Popularity(Scenic scenic)
	{
		if(this.popularity < scenic.popularity)
		{
			return -1;
		}
		else
		{
			if(this.popularity > scenic.popularity)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}
	
	//用路口分叉数比较
	public int Branch(Scenic scenic)
	{
		if(this.adjacency.size() < scenic.adjacency.size())
		{
			return -1;
		}
		else
		{
			if(this.adjacency.size() > scenic.adjacency.size())
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}
	
	//用休息区及公厕比较
	public int RT(Scenic scenic)
	{
		int weight1 = 2 * this.lounge + this.toilet;
		int weight2 = 2 * scenic.lounge + scenic.toilet;
		if(weight1 < weight2)
		{
			return -1;
		}
		else
		{
			if(weight1 > weight2)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}
	
	public String toString()
	{
		return 	name + "_" + info + "_" + popularity + "_" + lounge + "_" + toilet;
	}

//各属性  set get 方法
 	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getInfo()
	{
		return info;
	}

	public void setInfo(String info)
	{
		this.info = info;
	}

	public int getPopularity()
	{
		return popularity;
	}

	public void setPopularity(int popularity)
	{
		this.popularity = popularity;
	}

	public int getLounge()
	{
		return lounge;
	}

	public void setLounge(int lounge)
	{
		this.lounge = lounge;
	}

	public int getToilet()
	{
		return toilet;
	}

	public void setToilet(int toilet)
	{
		this.toilet = toilet;
	}

	public SeqList<Path> getAdjacency()
	{
		return adjacency;
	}

	public void setAdjacency(SeqList<Path> adjacency)
	{
		this.adjacency = adjacency;
	}

	public int getSpan()
	{
		return span;
	}

	public void setSpan(int span)
	{
		this.span = span;
	}

	public String getFormer()
	{
		return former;
	}

	public void setFormer(String former)
	{
		this.former = former;
	}

	public boolean isVisited()
	{
		return visited;
	}

	public void setVisited(boolean visited)
	{
		this.visited = visited;
	}
	
	public SeqList<Path> getSibLing()
	{
		return sibLing;
	}

	public void setSibLing(SeqList<Path> sibLing)
	{
		this.sibLing = sibLing;
	}

}
