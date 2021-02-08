package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.StringTokenizer;

import scenicSpot.Path;
import scenicSpot.Scenic;
import structure.SeqList;

public class Graph
{
//读写文件操作
	
	//初始化地图信息
	public static SeqList<Scenic> CreatGraph() throws NumberFormatException, IOException
	{
		//读取景点信息文件
		InputStreamReader input = new InputStreamReader(new FileInputStream("scenic_info.txt"), "GBK");
		BufferedReader br = new BufferedReader(input);
		
	    SeqList<Scenic> Map = new SeqList<Scenic>();
	    
		String name;//景点名称
		String info;//景点简介
		int popularity;//景点欢迎度
		int lounge;//有无休息区
		int toilet;//有无公厕
		
		//初始化所有景点
		String lineTxt = null;
	    while ((lineTxt = br.readLine()) != null)
	    {
	    	StringTokenizer string = new StringTokenizer(lineTxt, "_");
	    	name = string.nextToken();
	    	info = string.nextToken();
	    	popularity = Integer.parseInt(string.nextToken());
	    	lounge = Integer.parseInt(string.nextToken());
	    	toilet = Integer.parseInt(string.nextToken());
	    	
	    	Map.add(new Scenic(name, info, popularity, lounge, toilet));
	    }
	    br.close();
	    
	    //读取路径信息文件
	    input = new InputStreamReader(new FileInputStream("path_info.txt"), "GBK");
		br = new BufferedReader(input);
		
	    String name1;//景点名
	    String name2;//上面这一景点所连接的一个景点名
	    int distance;//两景点间的距离
	    int time; //两景点的路程耗时
	    Scenic[] result;//找到的两个景点对象
	    
	    //初始化景点的邻接表
	    while ((lineTxt = br.readLine()) != null)
	    {
	    	StringTokenizer string = new StringTokenizer(lineTxt, "_");
	    	name1 = string.nextToken();
	    	name2 = string.nextToken();
	    	result = Search(Map, name1, name2);
	    	distance = Integer.parseInt(string.nextToken());
	    	time = Integer.parseInt(string.nextToken());
	    	
	    	result[0].getAdjacency().add(new Path(name2, distance, time));
	    	result[1].getAdjacency().add(new Path(name1, distance, time));
	    }
	    
	    br.close();
	    return Map;
	}

	//广度优先保存地图信息
	public static void SaveGraph(SeqList<Scenic> Map) throws IOException
	{
		OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream("path_info.txt"),"GBK");
		BufferedWriter bw = new BufferedWriter(writerStream);
		
		for(Scenic scenic : Map)
	    {
	    	//当前景点的所有相邻景点
	        for(Path path : scenic.getAdjacency())
	        {
	        	Scenic newScenic = SearchScenic(Map, path.getScenic());//为这个景点名的景点对象
	        	if(!newScenic.isVisited())
	 	 	   	{   
	        		bw.write(path.toString(scenic.getName()) + System.getProperty("line.separator"));
	 	 	   	}
	        }
	        scenic.setVisited(true);
	    }
	    bw.close();
	}
	
	//广度优先保存景点信息
	public static void SaveScenic(SeqList<Scenic> Map) throws IOException
	{
		//向景点信息文件中写入
		OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream("scenic_info.txt"),"GBK");
		BufferedWriter bw = new BufferedWriter(writerStream);
		
	    for(Scenic scenic : Map)
	    {
	    	bw.write(scenic.toString() + System.getProperty("line.separator"));//将当前景点写入文件
	    }
	    
	    bw.close();
	}

//输出操作
	//将地图输出成邻接矩阵
	public static void OutputGraph(SeqList<Scenic> Map)
	{
		//表头
		for(Scenic s : Map)
		{
			System.out.print("\t" + s.getName());
		}
		System.out.println();
		
		//内容
		for(Scenic s : Map)//遍历Map中的每一个景点
		{
			Iterator<Path> iterator = s.getAdjacency().iterator();//当前景点的所有相邻景点
			Path current;
			//防止景点被修改后成为孤岛 而产生空指针异常
			if(iterator.hasNext())
			{
				current = iterator.next();
			}
			else
			{
				current = new Path("", 0, 0);
			}
			String scenic = current.getScenic();
			System.out.print(s.getName());
			
			for(Scenic m : Map)
			{
				if(m.getName().equals(s.getName()))//遇见自身
				{
					System.out.print("\t0");
				}
				else
				{
					if(m.getName().equals(scenic))//可达情况
					{
						System.out.print("\t" + current.getDistance());
						if(iterator.hasNext())
						{
							current = iterator.next();
							scenic = current.getScenic();
						}
						else
						{
							current = new Path("", 0, 0);
						}
					}
					else//不可达情况
					{
						System.out.print("\t32767");
					}
				}
			}
			System.out.println();
		}
	}
	
	//邻接表形式展示Map
	public static void showGraph(SeqList<Scenic> Map)
	{
		for(Scenic s : Map)
		{
			System.out.print(s.getName() + "\t");
			for(Path p : s.getAdjacency())
			{
				System.out.print(p.getScenic() + "_" + p.getDistance() + "\t");
			}
			System.out.println();
		}
	}
	
	//展示地图详细信息
	public static void showInfo(SeqList<Scenic> Map)
	{
		for(Scenic s : Map)
		{
			System.out.println(s.toString());
		}
	}
	
//查找操作
	//用于查找某路径是否属于这一邻接路径目录
	public static Path SearchPath(SeqList<Path> adjacency, String name)
	{
		for(Path p : adjacency)
		{
			if(p.getScenic().equals(name))
				return p;
		}
		
		return null;	
	}
	
	//用于查找是该景点名的景点对象 查找一个
	public static Scenic SearchScenic(SeqList<Scenic> Map, String name)
	{
		for(Scenic s : Map)
		{
			if(s.getName().equals(name))
				return s;
		}
		
		return null;	
	}
	
	//用于查找是该景点名的景点对象 同时找两个
	public static Scenic[] Search(SeqList<Scenic> Map, String scenic1, String scenic2)
	{
		Scenic[] result = new Scenic[2];
		for(Scenic s : Map)
		{
			if(s.getName().equals(scenic1))
				result[0] = s;
			if(s.getName().equals(scenic2))
				result[1] = s;
		}
		return result;			
	}

	//判断顺序表中是否有该景点
	public static boolean containsScenic(SeqList<Scenic> Map, String scenic)
	{
		for(Scenic s : Map)
		{
			if(s.getName().equals(scenic))
				return true;
		}
		
		return false;
	}
	
	//判断顺序表中是否有该路径
	public static boolean containsPath(SeqList<Path> adjacency, String scenic)
	{
		for(Path p : adjacency)
		{
			if(p.getScenic().equals(scenic))
				return true;
		}
		
		return false;
	}
	
//调整操作
	//将调整的图进行广度规范化
	public static SeqList<Scenic> Normalization(SeqList<Scenic> Map , Scenic target)
	{
		SeqList<Scenic> norMap = new SeqList<Scenic>();//用于存放排好序的邻接表
		SeqList<String> formerMap = new SeqList<String>();//用于存放那些比新加景点较先的景点(先后是指：广度遍历访问的先后顺序)

		int point = 0;//用于指向当前norMap中最小为空位置
		Scenic scenic = Map.get(0);//头景点
	    scenic.setVisited(true);//标记为访问过
	    norMap.add(scenic);//将头景点添加到新邻接表中
	    formerMap.add(scenic.getName());//将头景点添加到较先邻接表中
	    
	    boolean flag = true;//用于标记按照广度遍历是否访问过新增加景点
	    
	    //纵向规整
	    while(point < Map.size())
	    {
	    	scenic = norMap.get(point);//模拟广度遍历 从"队头"取一景点
	    	point++;
	    		    	
	        Iterator<Path> iterator = scenic.getAdjacency().iterator();
	        while(iterator.hasNext())
	        {
	        	//遍历该景点与之相连的所有景点
	        	Scenic newScenic = SearchScenic(Map, iterator.next().getScenic());
	        	if(!newScenic.isVisited())
	 	 	   	{   
	        		newScenic.setVisited(true);//设置景点已被访问
	        		norMap.add(newScenic);//将景点加入新邻接表
	        		
	        		if(flag)//如果还未访问到新加入景点
	        		{
	        			if(!newScenic.equals(target))//当前景点和新加入景点不一样
	        			{
	        				formerMap.add(newScenic.getName());//加入到较先邻接表
	        			}
	        			else
	        			{
	        				flag = false;//反之 设置未false
	        			}
	        		}
	 	 	   	}
	        } 
	    }
	    
	    //将新邻接表中各景点标记重置
	    Graph.Clear(norMap);
	    
	    //横向规整
	    SeqList<Path> Adjacency = target.getAdjacency();//新加入景点的邻接路径目录
	    SeqList<Path> newAdjacency = new SeqList<Path>();//用于存储新规整新加入景点的排序后的邻接路径目录
	    
	    for(Scenic s : norMap)
	    {
	    	Path path;
	    	//顺序遍历地图 每有一个景点在新加入静待你的邻接目录中 便将该景点路径放于新邻接路径目录中
	    	if((path = SearchPath(Adjacency, s.getName())) != null)
	    	{
	    		newAdjacency.add(path);
	    	}
	    }
	    
	    SearchScenic(norMap, target.getName()).setAdjacency(newAdjacency);
	    
	    for(Path p : Adjacency)
	    {
	    	//获得邻接路径目录中的一个景点的邻接路径目录
	    	SeqList<Path> adjacency = SearchScenic(norMap, p.getScenic()).getAdjacency();
	    	
	    	int i = adjacency.size()-1;//用于标记新加入景点的在邻接路径目录中的位置
	    	Path tem = adjacency.get(i);//新加入景点的路径
	    	Path compare = adjacency.get(i-1);//用于表示新加入景点的在邻接路径目录中的"前一个景点"
	    	
	    	//停止条件："前一个景点"在较先邻接表中 或 遍历到数组头
	    	while(!formerMap.contains(compare.getScenic()))
	    	{
	    		adjacency.set(i, compare);
	    		i--;
	    		if(i == 0)
	    			break;
	    		compare = adjacency.get(i-1);	    		
	    	}
	    	
	    	//将新加入景点放入正确位置
	    	adjacency.set(i, tem);
	    }
	    
	    return norMap;
	    
	}
	
	//最小生成树
	public static SeqList<Scenic> MiniTree(SeqList<Scenic> Map, Scenic head)
	{
		//构建用于储存最小生成树的顺序表
		SeqList<Scenic> mini = new SeqList<Scenic>();
		
		//Scenic head = Map.get(0);//获取头节点
		mini.add(head);//将头节点放入最小生成树中
		Scenic scenic = head;
		
		for(int i = 0; i < Map.size()-1; i++)
		{
			Path minPath = new Path();//最短路径
			int minDistance = Integer.MAX_VALUE;//最短路径长度
			for(Scenic s : mini)//遍历放在最小生成树中的所有景点
			{
				//遍历该景点相连的所有路径
				for(Path p : s.getAdjacency())
				{
					//如果最小生成树中不含有该景点 且 该路径小于当前最小路径
					if((!containsScenic(mini, p.getScenic())) && p.getDistance() < minDistance)
					{
						minPath = p;
						minDistance = p.getDistance();
						scenic = s;
					}
				}
			}
			
			//最终得到的最短路径通向的景点
			Scenic newScenic = SearchScenic(Map, minPath.getScenic());
			
			//如果存在该景点 则将其加入到上一景点的子路径中
			if(minPath.getScenic() != null)
			{
				scenic.getSibLing().add(minPath);
				mini.add(newScenic);
			}
		}
		
		//创建一先序遍历顺序表
		SeqList<Scenic> preMap = new SeqList<Scenic>();
		//生成先序遍历最小生成树
		PreOrder(Map, preMap, head);
		
		return preMap;//返回先序遍历最小生成树
	}	
	
	//先序遍历
	public static void PreOrder(SeqList<Scenic> Map, SeqList<Scenic> preMap, Scenic scenic)
	{
		if(scenic != null)
		{
			preMap.add(scenic);
		
			for(Path path : scenic.getSibLing())
			{
				Scenic newScenic = SearchScenic(Map, path.getScenic());
				PreOrder(Map, preMap, newScenic);
			}
		}
	}
	
	//重置Map中的visited Former Span
	public static void Clear(SeqList<Scenic> Map)
	{
		for(Scenic s : Map)
		{
			s.setFormer("");
			s.setVisited(false);
			s.setSpan(Integer.MAX_VALUE);
		}
	}
	
	//重置Map中的所有标记
	public static void ClearAll(SeqList<Scenic> Map)
	{
		for(Scenic s : Map)
		{
			s.setFormer("");
			s.setVisited(false);
			s.setSpan(Integer.MAX_VALUE);
			s.setSibLing(new SeqList<Path>());
		}
	}
	
}
