package structure;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SeqList<AnyType> implements Iterable<AnyType> 
{
	private int CAPACITY = 10;//默认大小
	private AnyType[] elements;//存储的元素
	private int size;//顺序表大小
	
	public SeqList()
	{
		clear();
	}
	
	public SeqList(int newSize) 
	{
		CAPACITY = newSize;
		clear();
	}
	
	//清空
	@SuppressWarnings("unchecked")
	public void clear()
	{
		elements = (AnyType[]) new Object[CAPACITY];
		size = 0;
	}

	//获取顺序表大小
	public int size()
	{
		return size;
	}
	
	//获取顺序表中的所有元素
	public AnyType[] getElements()
	{
		return elements;
	}
	
	//判断是否为空
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	//判断是否有满
	private boolean isFull()
	{
		return size == CAPACITY;
	}
	
	//获取指定位置的元素
	public AnyType get(int index)
	{
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException();
		
		return elements[index];
	}
	
	//获得指定名称的元素
	public boolean contains(AnyType newVal)
	{
		for(int i = 0; i < size; i++)
		{
			if(elements[i].equals(newVal))
			{
				return true;
			}
		}
		
		return false;
	}
	
	//修改指定位置的元素
	public void set(int index , AnyType newVal)
	{
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException();
			
		elements[index] = newVal;
	}
	
	//修改顺序表容量大小
	@SuppressWarnings("unchecked")
	public void ensureCapacity(int newSize)
	{
		if(newSize >= size)
		{
			CAPACITY = newSize;
			AnyType[] newElements = (AnyType[]) new Object[CAPACITY];
			
			for(int i = 0; i < size; i++)
				newElements[i] = elements[i];
			
			elements = newElements;
		}
	}
	
	//释放顺序表中空元素所占的空间
	public void trimToSize()
	{
		ensureCapacity(size);
	}
	
	//插入元素
	public void add(int index, AnyType x)
	{
		if(index < 0 || index > size)
			throw new ArrayIndexOutOfBoundsException();
		
		if(isFull())
			ensureCapacity(size * 2 + 1);
		
		for(int i = size; i > index; i--)
			elements[i] = elements[i-1];
		
		elements[index] = x;

		size++;
	}
	
	//添加元素
	public void add(AnyType x)
	{
		add(size, x);
	}
	
	//删除指定位置的元素
	public void remove(int index)
	{
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException();
		
		for(int i = index ; i < size-1; i++)
			elements[i] = elements[i+1];
		
		elements[size-1] = null;
		size--;
	}
	
	//删除指定对象
	public boolean remove(AnyType x)
	{
		if(!isEmpty())
		{
			for(int i = 0; i < size; i++)
			{
				if(elements[i].equals(x))
				{
					remove(i);
					i--;
					return true;
				}
			}
		}
		return false;
	}
	
	//迭代器
	@Override
	public Iterator<AnyType> iterator()
	{
		return new Itr();
	}
	
	private class Itr implements Iterator<AnyType>
	{
		int next = 0;
		
        public Itr()
        {
        	super();
        }
        
		@Override
		public boolean hasNext()
		{
			return next != size;
		}

		@Override
		public AnyType next()
		{
			int i = next;
			if (i >= size)
                throw new NoSuchElementException();
			next = i + 1;
			return elements[i];
		}
		
	}
}


