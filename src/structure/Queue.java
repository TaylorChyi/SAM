package structure;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<AnyType> implements Iterable<AnyType> 
{
	private Node<AnyType> top;//队列顶
	private Node<AnyType> bottom;//队列底
	private int size;//栈中元素个数
	
	private static class Node<AnyType>
	{
		private AnyType data;
		private Node<AnyType> high;
		private Node<AnyType> low;
		
		private Node()
		{
			super();
		}
		
		private Node(AnyType data, Node<AnyType> high, Node<AnyType> low)
		{
			this.data = data;
			this.high = high;
			this.low = low;
		}
	}
	
	//默认大小栈
	public Queue()
	{
		super();
		clear();
	}
	
	//清空
	public void clear()
	{
		top = new Node<AnyType>();
		bottom = new Node<AnyType>(null, top, null);
		top.low = bottom;
		size = 0;
	}

	//获取栈中元素个数
	public int size()
	{
		return size;
	}
	
	//获取栈顶
	public Node<AnyType> getTop()
	{
		return top;
	}
	
	//获取栈底
	public Node<AnyType> getBottom()
	{
		return bottom;
	}
	
	//判断是否为空
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	//获取指定位置的元素
	public AnyType get(int index)
	{
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException();
		
		AnyType result = null;
		Node<AnyType> tem = top;
		for(int i = 0; i < index; i++)
		{
			tem = tem.low;
			result = tem.data;
		}
		
		return result;
	}
		
	//添加新元素
	public int add(AnyType data)
	{
		Node<AnyType> newNode = new Node<AnyType>(data, bottom.high, bottom);
		bottom.high.low = newNode;
		bottom.high = newNode;
		size++;
		
		return size;
	}
	
	//删除栈顶元素
	public AnyType remove()
	{	
		if(!isEmpty())		
		{
			AnyType tem = top.low.data;
			top.low = top.low.low;
			top.low.high = top;
			size--;
			
			return tem;
		}
		
		return null;
	}
	
	//迭代器
	@Override
	public Iterator<AnyType> iterator()
	{
		return new Itr();
	}
	
	private class Itr implements Iterator<AnyType>
	{
		Node<AnyType> next = top;
		
        public Itr()
        {
        	super();
        }
        
		@Override
		public boolean hasNext()
		{
			return !next.low.equals(bottom);
		}

		@Override
		public AnyType next()
		{
			if (next.low.equals(bottom))
                throw new NoSuchElementException();
			
			next = next.low;
			
			return next.data;
		}
		
	}
}


