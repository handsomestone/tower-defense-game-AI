package Example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import Example.Node;
public class Test {

	public static void main(String args[])
	{
		Map<Integer,List<Node>> t1 = new HashMap<Integer,List<Node>>();
		List<Node> ts = new ArrayList<Node>();
		//List<Node> ts1 = new ArrayList<Node>();
		//List<Node> ts2 = new ArrayList<Node>();
		Node tn1 = new Node(1,0);
		Node tn2 = new Node(2,0);
		Node tn3 = new Node(3,0);
		Node tn4 = new Node(4,1);
		Node tn5 = new Node(5,1);
		Node tn6 = new Node(6,1);
		ts.add(tn1);
		ts.add(tn2);
		ts.add(tn3);
		ts.add(tn4);
		ts.add(tn5);
		ts.add(tn6);
		Integer tin1 = new Integer(1);
		Integer tin2 = new Integer(2);
		List<Node> ts1 = t1.get(tin1);
		//System.out.println(5%0);
		System.out.println(5%1);
		System.out.println(5%2);
		System.out.println(5%3);
		System.out.println(5%5);
		System.out.println(5%5);
		System.out.println("-------------------");
		if(ts1 == null)
		{
			ts1 = new ArrayList<Node>();
		}
		else
		{
			ts1.clear();
		}
		List<Node> ts2 = t1.get(tin2);
		if(ts2 == null)
		{
			ts2 = new ArrayList<Node>();
		}
		else
		{
			ts2.clear();
		}
		for(int i=0;i<ts.size();i++)
		{
			Node tnd=ts.get(i);
			if(tnd.gvalue ==0)
			{
				ts1.add(tnd);
			}
			if(tnd.gvalue ==1)
			{
				ts2.add(tnd);
			}
		}
		t1.put(tin1, ts1);
		t1.put(tin2, ts2);
		List<Node> ts3 = t1.get(tin2);
		for(Node iter:ts3)
		{
			System.out.println(iter.fvalue);
		}
		Iterator<Node> it = ts3.iterator();
		while(it.hasNext())
		{
			Node newNode =it.next();
			boolean remove = false;
			if(newNode.fvalue ==6)
			{
				remove= true;
			}
			if(remove==true)
			{
				it.remove();
			}
		}
		List<Node> ts4 = t1.get(tin2);
		for(Node iter:ts4)
		{
			System.out.println(iter.fvalue);
		}
		for(Node iter:ts)
		{
			System.out.println(iter.fvalue);
		}
		List<Integer> m = new ArrayList<Integer>();
		Integer m1 = new Integer(1);
		Integer m2 = new Integer(2);
		Integer m3 = new Integer(3);
		Integer m4 = new Integer(4);
		m.add(m1);
		m.add(m2);
		m.add(m3);
		m.add(m4);
		for(Integer iter:m)
		{
			System.out.println(iter.intValue());
		}
		for(int i=0;i<m.size();i++)
		{
			if(m.get(i).intValue() == 3)
			{
				m.set(i, new Integer(10));
			}
		}
		for(Integer iter:m)
		{
			System.out.println(iter.intValue());
		}
	}
}
