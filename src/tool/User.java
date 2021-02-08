package tool;

import java.util.Scanner;

import scenicSpot.Path;
import scenicSpot.Scenic;
import structure.SeqList;

//游客的操作
public class User
{
	//输出景区景点分布图
	public static void Distribution(SeqList<Scenic> Map)
	{
		Graph.OutputGraph(Map);
	}
	
	//景点的查找
	public static void Search(Scanner input, SeqList<Scenic> Map)
	{
		SeqList<Scenic> result = new SeqList<Scenic>();
		
		System.out.println("请输入关键字：");
		System.out.print(">>>");
		String key = input.nextLine();
		
		for(Scenic s : Map)
		{
			if(KMP(s.toString(), key) != -1)
			{
				result.add(s);
			}
		}
		
		if(result.size() > 0)
		{
			System.out.println("景点名称" + "\t" + "简介" + "\t" + "欢迎度" + "\t" + "休息区" + "\t" + "卫生间");
			for(Scenic s : result)
			{
				System.out.println(s.getName() + "\t" + s.getInfo() + "\t" + s.getPopularity()
									+ "\t" + s.getLounge() + "\t" + s.getToilet());
			}
			
			String operation;
			do {
				System.out.println("是否需要将以上信息排序(Y\\N)：");
				System.out.print(">>>");
				operation = input.nextLine();
	
				switch(operation) {
				case "Y" :  Sort(input, result);
				   			break;
				case "N" :	break;		
	
				default:	input.reset();
							System.out.println("错误指令!");
				}
			}while(!operation.equals("N") && !operation.equals("Y"));
		}
		else
		{
			System.out.println("对不起，无相关信息。");
		}
	}
	
	//KMP算法
	public static int KMP(String ts, String ps)
	{
		char[] t = ts.toCharArray();
		char[] p = ps.toCharArray();
		  
		int i = 0; // 主串的位置 
		int j = 0; // 模式串的位置
		
		int[] next = getNext(ps);
		 
		while (i < t.length && j < p.length)
		{
	       if (j == -1 || t[i] == p[j])
	       { 
	    	   // 当j为-1时，要移动的是i，当然j也要归0
	    	   i++;
	    	   j++;
	       }
	       else
	       {
	    	   // i不需要回溯了
	    	   // i = i - j + 1;
	    	   j = next[j]; // j回到指定位置
	       }
		}
		
		if (j == p.length) 
		{
			return i - j;
		} 
		else
		{
			return -1;
		}
	}

	//KMP算法
	public static int[] getNext(String ps)
	{
		char[] p = ps.toCharArray();
		int[] next = new int[p.length];
		next[0] = -1;

		int j = 0;
		int k = -1;
		
		while (j < p.length - 1)
		{
			if (k == -1 || p[j] == p[k])
			{
				next[++j] = ++k;
			}
			else
			{
				k = next[k];
			}
		}
		return next;
	}
	
	//景点排序分流
	public static void Sort(Scanner input, SeqList<Scenic> Map)
	{
		SortMenu();
		
		String operation;//用户的操作
		
		//Copy一份Map 防止操作打乱Map的应有顺序
		SeqList<Scenic> Copy = new SeqList<Scenic>();
		for(Scenic s : Map)
		{
			Copy.add(s);
		}
		
		do {
			System.out.print(">>>");
			operation = input.nextLine();
			switch(operation) {
			case "1" :  QuickSort(Copy, 0, Copy.size()-1, "P");//景点欢迎度
						DisplaySort(Copy, "P");//展示排序结果
			   			break;
			case "2" :  QuickSort(Copy, 0, Copy.size()-1, "B");//景点分叉路口数
						DisplaySort(Copy, "B");//展示排序结果
   						break;		
			case "3" : 	QuickSort(Copy, 0, Copy.size()-1, "RT");//景点有无休息区或公厕
						DisplaySort(Copy, "RT");//展示排序结果
						break;
			case "0" :  break;//返回游客系统
			
			default:	input.reset();
						System.out.println("错误指令!");
			}
			
		}while(!operation.equals("0"));
	}
	
	//排序操作目录
	public static void SortMenu()
	{
		System.out.print("==============================\n"
			           + "       **请选择排序关键字**       \n"
			           + "==============================\n"
			           + "1.-景点欢迎度\n"
			           + "2.-景点分叉路口数\n"
			           + "3.-有无休息室或公厕\n"
			           + "0.-退出系统\n");
	}
	
	//快速排序
	public static void QuickSort(SeqList<Scenic> Map, int left, int right, String standard)
	{
	    if(left < right)
	    {					
	       int pivotpos = Partition(Map, left, right, standard);
	       QuickSort(Map, left, pivotpos-1, standard);	    
	        //在左子区间递归进行快速排序
	       QuickSort(Map, pivotpos+1, right, standard);
	        //在右子区间递归进行快速排序		
	    }
	}

	//快速排序
	public static int Partition(SeqList<Scenic> Map, int low, int high, String standard)
	{
	    int pivotPos = low;     //基准位置
	    
	    Scenic tem = Map.get(low);
	    Map.set(low, Map.get((low+high)/2));
	    Map.set((low+high)/2, tem);
	    
	    Scenic pivot = Map.get(low);	
	    for (int i = low+1; i <= high; i++ )
	    {
	    	if (Map.get(i).CompareTo(standard, pivot) > 0 && ++pivotPos != i )
	    	{
	  	    	tem = Map.get(i);
	  	    	Map.set(i, Map.get(pivotPos));
	  	    	Map.set(pivotPos, tem);
	    	}
	    }
	    
	    tem = Map.get(low);
	    Map.set(low, Map.get(pivotPos));
	    Map.set(pivotPos, tem);

	    return pivotPos;
	}
	
	//展示排序结果
	public static void DisplaySort(SeqList<Scenic> Map, String standrad)
	{
		if(!standrad.equals("B"))
		{
			System.out.println("景点名称" + "\t" + "简介" + "\t" + "欢迎度" + "\t" + "休息区" + "\t" + "卫生间");
			for(Scenic s : Map)
			{
				System.out.println(s.getName() + "\t" + s.getInfo() + "\t" + s.getPopularity()
								+ "\t" + s.getLounge() + "\t" + s.getToilet());
			}
		}
		else
		{
			System.out.println("景点名称" + "\t" + "简介" + "\t" + "欢迎度" + "\t" + "休息区" + "\t" + "卫生间" + "\t" + "分叉路口数");
			for(Scenic s : Map)
			{
				System.out.println(s.getName() + "\t" + s.getInfo() + "\t" + s.getPopularity()
								+ "\t" + s.getLounge() + "\t" + s.getToilet() + "\t" + s.getAdjacency().size());
			}
		}
	}
	
	//输出导游线路图
	public static void  Roadmap(Scanner input, SeqList<Scenic> Map)
	{
		System.out.println("起始景点：");
		Scenic original = Legal.isScenic(input, Map);
		
		System.out.println("目的景点：");
		Scenic destination = Legal.isScenic(input, Map);
		
		if(original.equals(destination))
		{
			Loop(Map, original);
		}
		else
		{
			Roadmap(Map, original, destination);
		}
		Graph.ClearAll(Map);
	}
	
	//输出导游路线图
	public static void Roadmap(SeqList<Scenic> Map, Scenic original, Scenic destination)
	{
		int span = 0;
		
		//完全复制Map
		SeqList<Scenic> Copy = new SeqList<Scenic>();
		for(Scenic s : Map)
		{
			Copy.add(s);
		}
		
		//将该景点从相连所有景点中的邻接景点目录中删除 并将自身景点从地图中删除
		for(Path p : destination.getAdjacency())
		{		
			Graph.SearchScenic(Copy, p.getScenic()).remove(destination.getName());
		}
		
		Copy.remove(destination);
		
		//获取最小生成树
		SeqList<Scenic> preMap = Graph.MiniTree(Copy, original);
		Scenic current = preMap.get(0);//当前景点
		String route = current.getName();//在路径中添加当前景点名
		
		
		for(int i = 1; i < preMap.size(); i++)
		{
			Scenic next = preMap.get(i);
			Path path = Graph.SearchPath(current.getSibLing(), next.getName());
			if(path != null)
			{
				route = route + "-" + next.getName();
				span += path.getDistance();
			}
			else
			{
				route = route + "-" + Dijkstra(current, next, preMap);
				span += next.getSpan();
				Graph.Clear(preMap);
			}
			
			current = next;
		}
		preMap.add(destination);
		for(Path p : destination.getAdjacency())
		{		
			Graph.SearchScenic(Copy, p.getScenic()).add(new Path(destination.getName(), p.getDistance(), p.getTime()));
		}

		Graph.Normalization(preMap, destination);
		
		route = route + "-" + Dijkstra(current, destination, preMap);
		span += destination.getSpan();
		Graph.Clear(preMap);
		
		System.out.println("由 " + original.getName() + " 至 " + destination.getName());
		
		System.out.println("导游路线图：");
		System.out.println(route);
		System.out.println("共 " + span + "公里。");
	}
	
	//两个景点间的最短路径和最短距离
	public static void Shortest(Scanner input, SeqList<Scenic> Map)
	{
		System.out.println("起始景点：");
		Scenic original = Legal.isScenic(input, Map);
		
		System.out.println("目的景点：");
		Scenic destination = Legal.isScenic(input, Map);
		
		ShortestMenu();
		
		String operation;
		String route = "";
		
		do {
			System.out.print(">>>");
			operation = input.nextLine();
			switch(operation) {
			case "1" :  System.out.println("由 " + original.getName() + " 至 " + destination.getName());
						route = Dijkstra(original, destination, Map);
						System.out.println("最短路径: " + destination.getSpan() + " 公里。");
						System.out.println(original.getName() + "-" + route);
						Graph.ClearAll(Map);
			   			break;
			case "2" :  System.out.println("由 " + original.getName() + " 至 " + destination.getName());
						route = Floyd(original, destination, Map);
						System.out.println(original.getName() + route);
   						break;		
			case "0" :  return;//返回游客系统
			
			default:	input.reset();
						System.out.println("错误指令!");
			}
			
		}while(!operation.equals("1") && !operation.equals("2"));
	}
	
	//最短路径目录
	public static void ShortestMenu()
	{
		System.out.print("==============================\n"
			           + "       **请选择排序关键字**       \n"
			           + "==============================\n"
			           + "1.-Dijkstra\n"
			           + "2.-Floyd\n"
			           + "0.-退出系统\n");
	}
	
	//两个景点间的最短路径和最短距离 迪杰斯特拉算法
	public static String Dijkstra(Scenic original, Scenic destination, SeqList<Scenic> Map)
	{
		int span = 0;//路程
		Scenic scenic;//当前访问的景点
		original.setVisited(true);//起始景点已被访问
		original.setSpan(0);
		
		//最短路径计算
		scenic = original;
		for(int i = 0; i < Map.size(); i++)
		{
			//当前景点的邻接景点路径目录
			SeqList<Path> adjacency = scenic.getAdjacency();
			
			//更新各个城市的最短路径
			for(Path path : adjacency)
			{
				//该条路径通向的景点
				Scenic nextScenic = Graph.SearchScenic(Map, path.getScenic());
				//如果这个景点没有被访问过
				if(!nextScenic.isVisited())
				{
					//获取这条路径的长度
					span = path.getDistance();
					//如果走当前路径的所走距离小于原先所走的路径长度
					if((span = scenic.getSpan() + span) < nextScenic.getSpan())
					{
						//则将景点的上家名称标记在当前景点的Form_city属性上
						nextScenic.setFormer(scenic.getName());
						nextScenic.setSpan(span);
					}

				}
			}
			
			//选出所有未访问景点中路径最小的景点
			int distance = Integer.MAX_VALUE;
			for(Scenic s : Map)
			{
				if((!s.isVisited()) && s.getSpan() < distance)
				{
					distance = s.getSpan();
					scenic = s;
				}
			}
			
			scenic.setVisited(true);
			
			//如果目的城市已经被访问则跳出循环
			if(destination.isVisited())
			{
				break;
			}
		}
		

		//打印路径
		String route = Route(Map, original, destination);
		
		return route;
	}
		
	//两个景点间的最短路径和最短距离 佛洛依德算法
	public static String Floyd(Scenic original, Scenic destination, SeqList<Scenic> Map)
	{  
        int s1 = getIndex(Map, original);
        int s2 = getIndex(Map, destination);

		int dist[][] = new int[Map.size()][Map.size()];
		int path[][] = new int[Map.size()][Map.size()];
		// 初始化
        for (int i = 0; i < Map.size(); i++)
        {
            for (int j = 0; j < Map.size(); j++)
            {
                dist[i][j] = getDistance(Map, i, j);  // "顶点i"到"顶点j"的路径长度为"i到j的权值"。 
                path[i][j] = j;                // "顶点i"到"顶点j"的最短路径是经过顶点j。
            }
        }

        // 计算最短路径
        for (int k = 0; k < Map.size(); k++) 
        {
            for (int i = 0; i < Map.size(); i++)
            {
                for (int j = 0; j < Map.size(); j++)
                {

                    // 如果经过下标为k顶点路径比原两点间路径更短，则更新dist[i][j]和path[i][j]
                    int tmp = (dist[i][k] == Integer.MAX_VALUE || dist[k][j] == Integer.MAX_VALUE) ? Integer.MAX_VALUE : (dist[i][k] + dist[k][j]);
                    if (dist[i][j] > tmp)
                    {
                        // "i到j最短路径"对应的值设，为更小的一个(即经过k)
                        dist[i][j] = tmp;
                        // "i到j最短路径"对应的路径，经过k
                        path[i][j] = path[i][k];
                    }
                }
            }
        }
        
        int distance = dist[s1][s2];
        if(distance == Integer.MAX_VALUE)
        {
        	return "两点间无通路";//两点间无通路
        }
        
        //还原路径
        int k = path[s1][s2];
        String result = "";
        while(k!=s2)
        {
        	result = result + "-" + Map.get(k).getName();
        	k = path[k][s2];
        }
        result = result + "-" + Map.get(s2).getName();

        System.out.println("最短路径: " + distance + " 公里。");
        return result;
	}
	
	//获取在地图中的位置
	public static int getIndex(SeqList<Scenic> Map, Scenic scenic)
	{
		for(int i = 0; i < Map.size(); i++)
		{
			if(Map.get(i).equals(scenic))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	//获取两景点间的距离
	public static int getDistance(SeqList<Scenic> Map, int i , int j)
	{
		for(Path path : Map.get(i).getAdjacency())
		{
			if(path.getScenic().equals(Map.get(j).getName()))
			{
				return path.getDistance();
			}
		}
		
		return Integer.MAX_VALUE;
	}
	
	//打印最短路径
	public static String Route(SeqList<Scenic> Map, Scenic original, Scenic destination)
	{
		String route = destination.getName();
		Scenic mid = destination;
		String former = mid.getFormer();
		
		while(!former.equals(original.getName()))
		{
			mid = Graph.SearchScenic(Map, former);
			if(mid == null)
				break;
			route = mid.getName() + "-" + route;
			former = mid.getFormer();	
			
		}
		
		return route;
	}
	
	//输出导游路线图中的回路
	public static void Loop(SeqList<Scenic> Map, Scenic original)
	{
		int span = 0;
		SeqList<Scenic> preMap = Graph.MiniTree(Map, original);
		Scenic current = preMap.get(0);
		preMap.add(current);
		String route = current.getName();
		for(int i = 1; i < preMap.size(); i++)
		{
			Scenic next = preMap.get(i);
			Path path = Graph.SearchPath(current.getSibLing(), next.getName());
			if(path != null)
			{
				route = route + "-" + next.getName();
				span += path.getDistance();
			}
			else
			{
				route = route + "-" + Dijkstra(current, next, Map);
				span += next.getSpan();
				Graph.Clear(Map);
			}
			
			current = next;
		}
		
		System.out.println("导游路线图：");
		System.out.println(route);
		System.out.println("共 " + span + "公里。");
	}
	
}
