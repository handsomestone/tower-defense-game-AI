package Example;





import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;



public class example {
	public NodeComparator cmp = new NodeComparator();  
	public PriorityQueue<Node> open ;  
	public example()
	{
		open = new PriorityQueue<Node>(100000,cmp);
	}
	public static void main(String args[])
	{
		example t = new example();
		Node node = new Node();
		node.fvalue = 10;
		node.gvalue = 7;
		node.hvalue =3;
		Node node1 = new Node();
		node1.fvalue = 7;
		node1.gvalue = 8;
		node.hvalue =9;
		Node node2 = new Node();
		node2.fvalue =11;
		node2.gvalue = 5;
		node.hvalue =9;
		t.open.add(node);
		t.open.add(node1);
		t.open.add(node2);
		for(Node iter:t.open)
		{
			System.out.println(iter.fvalue);
			
		}
		Iterator<Node> ti = t.open.iterator();
		while(ti.hasNext())
		{
			Node n = ti.next();
			if(n.gvalue <4)
			{
				n.fvalue = 0;
				n.hvalue = 0;
				n.gvalue=1000;
			}
		}
		for(Node iter:t.open)
		{
			System.out.println("...."+iter.fvalue);
			
		}
	}
	class NodeComparator implements Comparator<Node> {  
        @Override  
        public int compare(Node x, Node y) { 
   	       
			return x.fvalue - y.fvalue; 
        }  
    }  
}
