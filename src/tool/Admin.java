package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import parking.Car;
import scenicSpot.Path;
import scenicSpot.Scenic;
import structure.Queue;
import structure.SeqList;
import structure.Stack;
import system.SAM;

//管理员特有的操作
public class Admin
{
	//登陆
	public static void Login(Scanner input, SeqList<Scenic> Map, Stack<Car> Parking, Queue<Car> Waiting, SeqList<String> notice) throws IOException
	{
		System.out.println("管理员账号：");
		System.out.print(">>>");
		String user = input.nextLine();
		
		System.out.println("密码：");
		System.out.print(">>>");
		String password = input.nextLine();

		if(Legal.isAdmin(user, password))
		{
			SAM.adminSystem(input, Map, Parking, Waiting, notice);			
		}
		else
		{
			System.out.println("登陆失败！");
		}		
	}
	
//维护操作
	//景点维护系统
	public static void Maintain(Scanner input, SeqList<Scenic> Map) throws IOException
	{
		MaintainMenu();//维护系统操作目录
		
		String operation;//用户的操作
		
		do {
			System.out.print(">>>");
			operation = input.nextLine();
			switch(operation) {
			case "1" : 	newScenic(input, Map);//新增景点
			   			break;
			case "2" :  removeScenic(input, Map);//删除景点
			   			break;
			case "3" : 	newPath(input, Map);//新增路段
						break;
			case "4" : 	removePath(input, Map);//删除路段
			   			break; 
			case "5" : 	Graph.showGraph(Map);//显示地图
   						break; 
			case "6" : 	Graph.OutputGraph(Map);;//显示地图
						break; 			
			case "0" : 	break;//返回管理员系统
						
			default:	input.reset();
						System.out.println("错误指令!");
			}
			
		}while(!operation.equals("0"));
				
	}
	
	//操作菜单
	public static void MaintainMenu()
	{
		System.out.print("==============================\n"
					   + "    **管理员景点维护系统**    \n"
				       + "        **请选择操作**        \n"
				       + "==============================\n"
				       + "1.-新增景点\n"
				       + "2.-删除景点\n"
				       + "3.-新增路段\n"
				       + "4.-删除路段\n"
				       + "5.-显示地图\n" 
				       + "6.-显示路段关系\n" 
				       + "0.-退出系统\n");
	}
	
	//新增景点
	public static void newScenic(Scanner input, SeqList<Scenic> Map)
	{
		//管理员输入景点名称
		System.out.println("景点名称：");
		System.out.print(">>>");
		String name = input.nextLine();
		if(Graph.SearchScenic(Map, name) != null)
		{
			System.out.println("该景点已位于公园内！");
			return;
		}
		
		//管理员输入景点信息
		System.out.println("景点信息：");
		System.out.print(">>>");
		String info = input.nextLine();
		
		//管理员输入景点欢迎度
		System.out.println("景点欢迎度：");
		int popularity = Legal.isNumber(input);
		
		//管理员输入景点有无休息区
		System.out.println("有无休息区(1为有 0为无)：");
		int lounge = Legal.isZero(input);
		
		//管理员输入景点有无公厕
		System.out.println("有无公厕(1为有 0为无)：");
		int toilet = Legal.isZero(input);
		
		//管理员依次输入景点相连的所有景点
		SeqList<Path> adjacency = new SeqList<Path>();
		System.out.println("依次输出该景点直接可达的所有景点：");
		
		//管理员输入相连景点名称
		System.out.println("景点名称('#'结束)：");
		String operation = Legal.isScenic(input, Map).getName();
		int distance;
		int time;
		
		while(!operation.equals("#"))
		{
			//管理员输入相连景点路程
			System.out.println("路程：");
			System.out.print(">>>");
			distance = Integer.parseInt(input.nextLine());
			
			//管理员输入相连景点耗时
			System.out.println("耗时：");
			System.out.print(">>>");
			time = Integer.parseInt(input.nextLine());
			
			////将构造的路径添加到连接路径目录中
			adjacency.add(new Path(operation, distance, time));
			Graph.SearchScenic(Map, operation).add(new Path(name, distance, time));
			
			System.out.println("景点名称('#'结束)：");
			operation = Legal.isScenic(input, Map).getName();
			
		}
		
		//如果新的景点中有景点与其相连 则将新构造的景点添加至Map
		if(adjacency.size() != 0)
		{
			Scenic scenic = new Scenic(name, info, popularity, lounge, toilet, adjacency);
			Map.add(scenic);
			Graph.Normalization(Map, scenic);
			System.out.println("添加成功！");
		}
		else
		{
			System.out.println("不可添加'孤岛'景点");
		}
	}

	//删除景点
	public static void removeScenic(Scanner input, SeqList<Scenic> Map)
	{
		//管理员输入要删除的景点名称
		System.out.println("删除景点名称：");
		Scenic scenic = Legal.isScenic(input, Map);
		
		//防止删除导致的孤岛
		for(Scenic s : Map)
		{
			if(s.getAdjacency().size() == 1)
			{
				if(s.getAdjacency().get(0).getScenic().equals(scenic.getName()))
				{
					System.out.println("删除失败！");
					return;
				}
			}
		}
		
		//将该景点从相连所有景点中的邻接景点目录中删除 并将自身景点从地图中删除
		for(Path p : scenic.getAdjacency())
		{			
			Graph.SearchScenic(Map, p.getScenic()).remove(scenic.getName());
		}
		
		//将自身景点从地图中删除
		Map.remove(scenic);
		System.out.println("删除成功！");
	}

	//新增路段
	public static void newPath(Scanner input, SeqList<Scenic> Map)
	{
		System.out.println("新增路段连接第一个景点名称：");
		Scenic scenic1 = Legal.isScenic(input, Map);
		
		System.out.println("新增路段连接第二个景点名称：");
		Scenic scenic2 = Legal.isScenic(input, Map);
		
		System.out.println("路程：");
		int distance = Legal.isNumber(input);
		
		System.out.println("耗时：");
		int time = Legal.isNumber(input);
		if(Graph.SearchPath(scenic2.getAdjacency(), scenic1.getName()) == null)
		{
			scenic2.add(new Path(scenic1.getName(), distance, time));
			scenic1.add(new Path(scenic2.getName(), distance, time));
		}
		else
		{
			Graph.SearchPath(scenic1.getAdjacency(), scenic2.getName()).setDistance(distance);
			Graph.SearchPath(scenic1.getAdjacency(), scenic2.getName()).setTime(time);
			
			Graph.SearchPath(scenic2.getAdjacency(), scenic1.getName()).setDistance(distance);
			Graph.SearchPath(scenic2.getAdjacency(), scenic1.getName()).setTime(time);
		}
		Graph.Normalization(Map, scenic1);
		Graph.Normalization(Map, scenic2);
		System.out.println("添加成功！");
	}

	//删除路段
	public static void removePath(Scanner input, SeqList<Scenic> Map)
	{
		System.out.println("删除路段连接第一个景点名称：");
		Scenic scenic1 = Legal.isScenic(input, Map);
		
		System.out.println("删除路段连接第二个景点名称：");
		Scenic scenic2 = Legal.isScenic(input, Map);
		
		if(scenic1.getAdjacency().size() > 1 && scenic2.getAdjacency().size() > 1)
		{
			scenic1.remove(scenic2.getName());
			scenic2.remove(scenic1.getName());
			System.out.println("删除成功！");
		}
		else
		{
			System.out.println("该景点不可删除！");
		}
	}

//通知操作
	//发布通知系统
	public static void Notice(Scanner input, SeqList<String> notice) throws IOException
	{
		NoticeMenu();//通知操作目录
		
		String operation;//用户的操作
		
		do {
			System.out.print(">>>");
			operation = input.nextLine();
			switch(operation) {
			case "1" : 	addNotice(input, notice);//新增通知
			   			break;
			case "2" : 	removeNotice(input, notice);//删除通知
						break;
			case "3" : 	showNotice(notice);//显示通知
			   			break; 
			case "0" : 	break;//返回管理员系统
						
			default:	input.reset();
						System.out.println("错误指令!");
			}
			
		}while(!operation.equals("0"));
	}
	
	//通知系统菜单
	public static void NoticeMenu()
	{
		System.out.print("==============================\n"
				   	   + "       **通知管理系统**       \n"
			           + "        **请选择操作**        \n"
			           + "==============================\n"
			           + "1.-新增通知\n"
			           + "2.-删除通知\n"
			           + "3.-显示通知\n"
			           + "0.-返回\n");
	}
	
	//加载通知信息
	public static SeqList<String> loadNotice() throws IOException
	{
		//读取景点信息文件
		InputStreamReader input = new InputStreamReader(new FileInputStream("notice_info.txt"), "GBK");
		BufferedReader br = new BufferedReader(input);
		
		 SeqList<String> notice = new SeqList<String>();
		 
		//初始化所有景点
		String lineTxt = null;
		
		while ((lineTxt = br.readLine()) != null)
		{
		   	notice.add(lineTxt);
		}
		
		br.close();
		
		return notice;
	}
	
	//新增通知
	public static void addNotice(Scanner input, SeqList<String> notice)
	{
		System.out.println("新通知：");
		System.out.print(">>>");
		String news = input.nextLine();
		notice.add(news);
		System.out.println("添加成功！");
	}
	
	//删除通知
	public static void removeNotice(Scanner input, SeqList<String> notice)
	{
		if(notice.size() != 0)
		{
			showNotice(notice);
			System.out.println("删除第几条通知：");
			int index = Legal.isNumber(input) - 1;
			if(index < notice.size() && index >= 0)
			{
				notice.remove(index);
				System.out.println("删除成功！");
			}
			else
			{
				System.out.println("删除失败！");
			}
		}
	}
	
	//显示通知
	public static void showNotice(SeqList<String> notice)
	{
		if(notice.size() != 0)
		{
			System.out.println("*通知：");
			for(int i = 0; i < notice.size(); i++)
			{
				System.out.println("   " + (i + 1) + "." + notice.get(i));
			}
			System.out.println();
		}
	}
	
	//保存通知
	public static void SaveNotice(SeqList<String> notice) throws IOException
	{
		//向景点信息文件中写入
		OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream("notice_info.txt"),"GBK");
		BufferedWriter bw = new BufferedWriter(writerStream);
		
		for(String str : notice)
		{
			bw.write(str.toString() + System.getProperty("line.separator"));//将当前 通知写入文件
		}
		
	    bw.close();
	}

}
