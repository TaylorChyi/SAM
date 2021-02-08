package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;

import parking.Car;
import structure.Queue;
import structure.SeqList;
import structure.Stack;

public class Park
{
	//停车场管理系统
	public static void Parking(Scanner input, Stack<Car> Parking, Queue<Car> Waiting)
	{
		String operation;//用户操作
		parkMenu();//停车场操作目录
		
		do {
			System.out.print(">>>");
			operation = input.nextLine();
			switch(operation) {
			case "1" : 	Show(Parking);//展示停车场信息
   						break;
			case "2" : 	Load(input, Parking, Waiting);//汽车入库
			   			break;
			case "3" :  Unload(input, Parking, Waiting);//汽车出库
			   			break;		
			case "0" : 	break;//返回管理员系统
						
			default:	input.reset();
						System.out.println("错误指令!");
			}
			
		}while(!operation.equals("0"));
	}
	
	//停车场操作目录
	public static void parkMenu()
	{
		System.out.print("=============================\n"
				   	   + "    **欢迎使用停车场管理系统**     \n"
				       + "        **请选择操作**          \n"
				       + "=============================\n"
				       + "1.-停车场信息\n"
				       + "2.-汽车入库\n"
				       + "3.-汽车出库\n"
				       + "0.-退出系统\n");
	}

	//加载停车场信息
	public static Stack<Car> CreatPark() throws IOException
	{
		//读取车库信息文件
		InputStreamReader input = new InputStreamReader(new FileInputStream("parking_info.txt"), "GBK");
		BufferedReader br = new BufferedReader(input);
		
		Stack<Car> Parking = new Stack<Car>();
		//初始化车库信息
		Car car;
		String lineTxt;
		while ((lineTxt = br.readLine()) != null)
		{
			StringTokenizer string = new StringTokenizer(lineTxt, "_");
			String number = string.nextToken();
			String time = string.nextToken();
			car = new Car(number, time);
		   	Parking.add(car);
		}
		
		br.close();
		
		return Parking;
	}
	
	//保存停车场信息
	public static void SavePark(Stack<Car> Parking) throws IOException
	{
		//向景点信息文件中写入
		OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream("parking_info.txt"),"GBK");
		BufferedWriter bw = new BufferedWriter(writerStream);
		
		for(Car c : Parking)
		{
			bw.write(c.toString() + System.getProperty("line.separator"));//将当前 通知写入文件
		}
		
	    bw.close();
	}
	
	//汽车入库
	public static void Load(Scanner input, Stack<Car> Parking, Queue<Car> Waiting)
	{
		//输入车牌号
		System.out.println("请输入车牌号：");
		System.out.print(">>>");
		String number = input.nextLine();
		
		if(!number.equals("#"))
		{
			if(SearchCar(Parking, number) == null)
			{
				//获取当前时间
				Date Now = new Date( );
				SimpleDateFormat ft = new SimpleDateFormat ("HH:mm");
				String time = ft.format(Now);
				
				//汽车入库
				Car car = new Car(number, time);
				
				//如果车库已满进入等待队列，反之进入车库
				if(Parking.isFull())
				{
					if(SearchCar(Waiting, number) == null)
					{
						System.out.println("车库已满，已为您排队请耐心等待！");
						Waiting.add(car);
					}
					else
					{
						System.out.println("该车辆已在排队队列中，请勿重复操作！");
					}
				}
				else
				{
					int index = Parking.add(car);
					System.out.println("该车已进入停车场：位于 " + index + " 号车道。");
				}
			}
			else
			{
				System.out.println("该车辆已经入库！");
			}
		}
	}
	
	//汽车出库
	public static void Unload(Scanner input, Stack<Car> Parking, Queue<Car> Waiting)
	{
		if(Parking.isEmpty())
		{
			System.out.println("车库已空！");
		}
		else
		{
			//输入车牌号
			System.out.println("请输入车牌号：");
			Car car = Legal.isCar(input, Parking);
			if(!car.getNumber().equals("#"))
			{
				//获取当前时间
				Date Now = new Date( );
				SimpleDateFormat ft = new SimpleDateFormat ("HH:mm");
				String time = ft.format(Now);
				//汽车出库
				SeqList<Car> transList = Remove(Parking, car.getNumber());
				Transform(transList);
				CheckOut(time, car.getTime());
				
				//如果等待队列非空 将队头车辆入库
				if(!Waiting.isEmpty())
				{
					Car newCar = Waiting.remove();
					newCar.setTime(time);//改为真正入库时间
					int index = Parking.add(newCar);
					System.out.println("+位于等待队列车辆 " + newCar.getNumber() + " 已进入停车场：位于 " + index + " 号车道。");
				}
			}

		}
	}
	
	//展示停车场信息
	public static void Show(Stack<Car> Parking)
	{
		int i = 0;
		for(Car c : Parking)
		{
			i++;
			System.out.println("车辆 " + c.getNumber() +" 在 " + i + " 号车位。");
		}
	}
	
	//为出库转移车辆
	public static void Transform(SeqList<Car> transList)
	{
		for(int i = 0; i < transList.size()-1; i++)
		{
			Car car = transList.get(i);
			System.out.println("-车辆 " + car.getNumber() + " 暂时出库。");
		}
		System.out.println("*车辆 " + transList.get(transList.size()-1).getNumber() + " 离开停车场。");
	}
	
	//结账
	public static void CheckOut(String now, String former)
	{
		StringTokenizer nowString = new StringTokenizer(now, ":");
		StringTokenizer formerString = new StringTokenizer(former, ":");
		
		int hour = Integer.parseInt(nowString.nextToken()) - Integer.parseInt(formerString.nextToken());
		int minute = Integer.parseInt(nowString.nextToken()) - Integer.parseInt(formerString.nextToken());
		
		int time = 60 * hour + minute;
		
		double money = (double)Math.round(time / 9.0 * 100) / 100;
		
		System.out.println("入库时间：" + former + " 出库时间：" + now);
		System.out.println("停留时间：" + time + " 分钟");
		System.out.println("停车费：" + money + " 元");
	}
	
	//搜索车辆
	public static Car SearchCar(Stack<Car> Parking, String number)
	{
		for(Car c : Parking)
		{
			if(c.getNumber().equals(number))
			{
				return c;
			}
		}
		
		return null;
	}
	
	//搜索车辆
	public static Car SearchCar(Queue<Car> Parking, String number)
	{
		for(Car c : Parking)
		{
			if(c.getNumber().equals(number))
			{
				return c;
			}
		}
		
		return null;
	}
	
	//删除车辆
	public static SeqList<Car> Remove(Stack<Car> Parking, String number)
	{
		Car car = SearchCar(Parking, number);
		return Parking.remove(car);
	}
}
