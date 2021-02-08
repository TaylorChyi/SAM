package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import parking.Car;
import scenicSpot.Scenic;
import structure.Stack;
import structure.SeqList;

//检验用户输入方法
public class Legal
{
	//判断用户输入是否为1或0
 	public static int isZero(Scanner input)
	{
 		System.out.print(">>>");
		String tem = input.nextLine();
		
		while(!tem.equals("0") && !tem.equals("1"))
		{
			System.out.println("输入错误，请重新输入！");
			System.out.print(">>>");
			tem = input.nextLine();
		}
		
		return Integer.parseInt(tem);
	}
	
	//判断用户输入是否为数字
	public static int isNumber(Scanner input)
	{
		System.out.print(">>>");
		String tem = input.nextLine();
		
		while(!isInteger(tem))
		{
			System.out.println("输入错误，请重新输入！");
			System.out.print(">>>");
			tem = input.nextLine();
		}
		
		return Integer.parseInt(tem);
	}
	
	//判断字符串是否为数字
	public static boolean isInteger(String string)
	{  
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
		return pattern.matcher(string).matches();  
	}
	
	//判断是否为管理员
	public static boolean isAdmin(String name,String password) throws IOException
	{
		File file = new File("admin_info.txt");
		
	    InputStreamReader input = new InputStreamReader(new FileInputStream(file), "UTF-8");
	    BufferedReader br = new BufferedReader(input);
	    
	    String lineTxt = null;
	    String admin;
	    String adminPassword;
	    
	    while ((lineTxt = br.readLine()) != null)
	    {
	    	StringTokenizer string = new StringTokenizer(lineTxt, "_");
	    	admin = string.nextToken();
	    	adminPassword = string.nextToken();
	    	
	    	if(admin.equals(name) && adminPassword.equals(password))
	    	{
	    		br.close();
	    		return true;
	    	}
	    }
	    
	    br.close();
	    return false;
	}
	
	//判断是否为景点
	public static Scenic isScenic(Scanner input, SeqList<Scenic> Map)
	{
		System.out.print(">>>");
		String tem = input.nextLine();
		Scenic scenic = Graph.SearchScenic(Map, tem);
		
		while(!tem.equals("#") && scenic == null)
		{
			System.out.println("公园中不含此景点，请重新输入！");
			System.out.print(">>>");
			tem = input.nextLine();
			scenic = Graph.SearchScenic(Map, tem);
		}
		if(tem.equals("#"))
		{
			scenic = new Scenic("#");
		}
		
		return scenic;
	}

	//判断是否为汽车
	public static Car isCar(Scanner input, Stack<Car> Parking)
	{
		System.out.print(">>>");
		String tem = input.nextLine();
		Car car = Park.SearchCar(Parking, tem);
		
		//终止条件 取消 或 找到相应汽车
		while(!tem.equals("#") && car == null)
		{
			System.out.println("车库中无此车，请重新输入！");
			System.out.print(">>>");
			tem = input.nextLine();
			car = Park.SearchCar(Parking, tem);
		}
		if(tem.equals("#"))
		{
			car = new Car("#");
		}
	
		return car;
	}

}
