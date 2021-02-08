package structure;

import java.util.Iterator;
import java.util.NoSuchElementException;
import structure.SeqList;

public class Stack<AnyType> implements Iterable<AnyType> 
{
	private int CAPACITY = 5;//容量大小
	private Node<AnyType> top;//栈顶
	private Node<AnyType> bottom;//栈底
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
	public Stack()
	{
		super();
		clear();
	}
	
	//自定义栈大小
	public Stack(int CAPACITY)
	{
		super();
		clear();
		this.CAPACITY = CAPACITY;
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
	
	//判断是否为满
	public boolean isFull()
	{
		return size == CAPACITY; 
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
		if(!isFull())
		{
			Node<AnyType> newNode = new Node<AnyType>(data, top, top.low);
			top.low.high = newNode;
			top.low = newNode;
			size++;
		}
		
		return size;
	}
	
	//删除栈顶元素
	public int remove()
	{	
		if(!isEmpty())		
		{
			top.low = top.low.low;
			top.low.high = top;
			size--;
		}
		
		return size;
	}
	
	//删除符合特征的元素
	public SeqList<AnyType> remove(AnyType number)
	{
		if(!isEmpty())
		{
			SeqList<AnyType> result = new SeqList<AnyType>();
			Node<AnyType> current = top;
		
			for(int i = 0; i < size; i++)
			{
				current = current.low;
				result.add(current.data);
				
				if(current.data.equals(number))
				{
					current.high.low = current.low;
					current.low.high = current.high;
					
					size--;
					
					return result;
				}
			}
		}
		
		return new SeqList<AnyType>();
	}
	
	//迭代器
	@Override
	public Iterator<AnyType> iterator()
	{
		return new Itr();
	}
	
	private class Itr implements Iterator<AnyType>
	{
		Node<AnyType> next = bottom;
		
        public Itr()
        {
        	super();
        }
        
		@Override
		public boolean hasNext()
		{
			return !next.high.equals(top);
		}

		@Override
		public AnyType next()
		{
			if (next.high.equals(top))
                throw new NoSuchElementException();
			
			next = next.high;
			
			return next.data;
		}
		
	}
}


