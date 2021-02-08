package system;

import java.io.IOException;
import java.util.Scanner;

import parking.Car;
import scenicSpot.Scenic;
import structure.Stack;
import structure.Queue;
import structure.SeqList;
import tool.Admin;
import tool.Graph;
import tool.Park;
import tool.User;

public class SAM
{	
	//运行SAM
	public static void main(String[] args) throws NumberFormatException, IOException
	{
		//加载所有资源
		Scanner input = new Scanner(System.in);//输入流
		SeqList<Scenic> Map = Graph.CreatGraph();//地图
		Stack<Car> Parking = Park.CreatPark();//停车场
		Queue<Car> Waiting = new Queue<Car>();
		SeqList<String> notice = Admin.loadNotice();//通知

		userSystem(input, Map, Parking, Waiting, notice);//开启系统
		
		//关闭 保存所有资源
		input.close();//输入流
	   	Graph.SaveGraph(Map);//地图
	   	Graph.SaveScenic(Map);//景点信息
	   	Admin.SaveNotice(notice);//通知
	   	Park.SavePark(Parking);//公园信息
	   	
	   	System.out.print("---");
	}
	
	//管理员操作系统
	public static void adminSystem(Scanner input, SeqList<Scenic> Map, Stack<Car> Parking, Queue<Car> Waiting, SeqList<String> notice) throws IOException
	{
		String operation;//用户操作
		
		adminMenu();//管理员操作目录
		
		do {	
			System.out.print(">>>");
			operation = input.nextLine();
			switch(operation) {
			case "#" : 	adminMenu();//显示操作菜单
						break;
			case "1" : 	Admin.Maintain(input, Map);//景点维护
						adminMenu();//管理员操作目录
						break;
			case "2" : 	User.Search(input, Map);//景点查找
			   			break;
			case "3" : 	User.Sort(input, Map);//景点排序
						break;
			case "4" : 	User.Roadmap(input, Map);//导游线路图
			   			break;
			case "5" : 	User.Distribution(Map);//景点分布图
						break;
			case "6" :	User.Loop(Map, Map.get(0));//导游路线图中的回路 默认北门
						break;
			case "7" : 	User.Shortest(input, Map);//两景点间的最短路径和最短距离
						break;		   
			case "8" : 	Park.Parking(input, Parking, Waiting);//停车场
						break;
			case "9" : 	Admin.Notice(input, notice);//通知管理
						adminMenu();//管理员操作目录
						break;
			case "0" : 	break;//返1回
			
			default:	input.reset();
						System.out.println("错误指令!");
			}
			
		}while(!operation.equals("0"));			
	}

	//管理员操作目录
	public static void adminMenu()
	{
		System.out.print("===============================\n"
					   + "     **欢迎使用景区信息管理系统**     \n"
					   + "          **请选择操作**          \n"
					   + "===============================\n"
					   + "#.-显示操作菜单\n"
					   + "1.+景点维护\n"
					   + "2.-景点查找\n"
					   + "3.-景点排序\n"
					   + "4.-导游线路图\n"
					   + "5.-景区景点分布图\n"
					   + "6.-导游路线图中的回路\n"
					   + "7.-两景点间的最短路径和最短距离\n"
					   + "8.-停车场\n"
					   + "9.+通知管理\n"
					   + "0.-返回\n");
	}
	
	//游客操作系统
	public static void userSystem(Scanner input, SeqList<Scenic> Map, Stack<Car> Parking, Queue<Car> Waiting, SeqList<String> notice) throws NumberFormatException, IOException
	{
		String operation;//用户操作
		
		userMenu(notice);//操作菜单
		
		do {
			System.out.print(">>>");
			operation = input.nextLine();
			switch(operation) {
			case "#" : 	userMenu(notice);//显示操作菜单
						break;
			case "1" : 	Admin.Login(input, Map, Parking, Waiting, notice);//管理员登陆
						userMenu(notice);//显示操作菜单
					   	break;
			case "2" : 	User.Search(input, Map);//景点查找
   						break;
			case "3" : 	User.Sort(input, Map);//景点查找
						break;
			case "4" : 	User.Roadmap(input, Map);//导游线路图
						break;
			case "5" : 	User.Distribution(Map);//景区景点分布图
   						break;
			case "6" : 	User.Loop(Map, Map.get(0));//导游路线图中的回路 默认北门
						break;
			case "7" :	User.Shortest(input, Map);//两景点间的最短路径和最短距离
						break;
			case "8" : 	Park.Parking(input, Parking, Waiting);//停车场
						break;		   
			case "0" : 	break;//退出系统
			
			default  : 	input.reset();
					   	System.out.println("错误指令!");
			}
			
		}while(!operation.equals("0"));
	}
	
	//用户操作目录
	public static void userMenu(SeqList<String> notice)
	{
		Admin.showNotice(notice);//显示通知
		
		System.out.print("===============================\n"
					   + "     **欢迎使用景区信息管理系统**     \n"
					   + "          **请选择操作**          \n"
					   + "===============================\n"
					   + "#.-显示操作菜单\n"
					   + "1.-管理员登陆\n"
					   + "2.-景点查找\n"
					   + "3.-景点排序\n"
					   + "4.-导游线路图\n"
					   + "5.-景区景点分布图\n"
					   + "6.-导游路线图中的回路\n"
					   + "7.-两景点间的最短路径和最短距离\n"
					   + "8.-停车场\n"
					   + "0.-退出系统\n");
	}
	
}
