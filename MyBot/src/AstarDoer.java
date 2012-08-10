import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AstarDoer
{
	private boolean write_Astar=true;
	Tile startStatus;
	Tile endStatus;
	Location mymap[][];
	boolean isPath ;
	boolean isGA =false;
	Node endPath ;
	List<Tile> path;

	Map<Integer,Integer> tileToCannon;

	NodeComparator cmp = new NodeComparator();  
	PriorityQueue<Node> open ;  
	PriorityQueue<Node>  close  ;
	public AstarDoer(Tile startStatus,Tile endStatus,Location mymap[][] )
	{
		this.startStatus = new Tile(startStatus.getRow(),startStatus.getCol());
		this.endStatus = new Tile(endStatus.getRow(),endStatus.getCol());
		this.mymap = mymap;
		this.open = new PriorityQueue<Node> (1000000,cmp); 
		this.close = new PriorityQueue<Node> (1000000,cmp);
		isPath = false;
	}
	public AstarDoer(Tile startStatus,Tile endStatus,Location mymap[][],Map<Integer,Integer> tileToCannon)
	{
		this.startStatus = new Tile(startStatus.getRow(),startStatus.getCol());
		this.endStatus = new Tile(endStatus.getRow(),endStatus.getCol());
		this.mymap = mymap;
		this.open = new PriorityQueue<Node> (1000000,cmp); 
		this.close = new PriorityQueue<Node> (1000000,cmp);

		this.tileToCannon = tileToCannon;
		
		isPath = false;
		isGA=true;
	}
	private void initStart()
	{
		//将开始节点加入open列表
		Node startNode = new Node(startStatus);
		startNode.gvalue = 0;
		startNode.parent = null;
		startNode.priority = 0;
		startNode.hvalue = startNode.getH(endStatus);
		startNode.fvalue = startNode.gvalue+startNode.hvalue;
		
		open.add(startNode);
	}
	private boolean isInList(Node lNode,Node newNodeParent)
	{
		//判断该节点是否已经在之前出现过，之前出现的g值 肯定小于现在的g值，因为都是通过
		//同一节点经过上下左右变换得到
		while(newNodeParent != null)
		{
			if(lNode.equal(newNodeParent.status))
			{
				return true;
			}
			newNodeParent = newNodeParent.parent;
		}
		return false;
	}
	private void makeZoneGA(Node newNode,List<Node> newNodeChild)
	{
		//List<Node> zone = new ArrayList<Node>();
		int Xcol = newNode.getStatus().getCol();
		int Yrow = newNode.getStatus().getRow();
		for(int i=-1;i<=1;i++)
		{
			int Y = Yrow+i;
			for(int j=-1;j<=1;j++)
			{
				int X = Xcol+j;
				if(i==0&&j==0)
				{
					continue;
				}
				if(mymap[Y][X].getIlk().isDefend())
				{
					Tile tile = mymap[Y][X].getTile();
					Node gNode = new Node(tile);
					gNode.parent = newNode;
					if(Math.abs(i)==1&&Math.abs(j)==1)
					{
						gNode.gvalue = newNode.gvalue+14;
					}
					else
					{
						gNode.gvalue = newNode.gvalue+10;
					}
					
					if(j==1)
					{
						if(i==0)
						{
							gNode.priority = 1;
						}
						else if(i==-1)
						{
							gNode.priority = 2;
						}
						else
						{
							gNode.priority = 8;
						}
					}
					else if(j==0)
					{
						if(i==-1)
						{
							gNode.priority = 3;
						}
						else
						{
							gNode.priority = 7;
						}
					}
					else if(j==-1)
					{
						if(i==0)
						{
							gNode.priority = 5;
						}
						else if(i==-1)
						{
							gNode.priority = 4;
						}
						else
						{
							gNode.priority = 6;
						}
					}
					gNode.hvalue = gNode.getH(endStatus);
					gNode.fvalue = gNode.gvalue+gNode.hvalue;
					newNodeChild.add(gNode);
				}
			}
		}
		
		
		if(mymap[Yrow][Xcol+1].getIlk().isPassable())
		{
			int key = 0;
			key = Yrow<<6|(Xcol+1);
			Integer key_tile = new Integer(key);
			Integer cannon = tileToCannon.get(key_tile);
			if(cannon == null)
			{
				cannon = new Integer(0);
			}
			if(cannon.intValue()<5)
			{
				Tile tile = mymap[Yrow][Xcol+1].getTile();
				Node rNode = new Node(tile);
				if(isInList(rNode,newNode.parent)==false)
				{
					rNode.parent = newNode;
					rNode.gvalue = newNode.gvalue+10;
					rNode.priority = 1;
					
					rNode.hvalue = rNode.getH( endStatus);
					rNode.fvalue = rNode.gvalue+rNode.hvalue;
					
					newNodeChild.add(rNode);
				}
			}
		}
		if(mymap[Yrow-1][Xcol+1].getIlk().isPassable())
		{
			int key = 0;
			key = (Yrow-1)<<6|(Xcol+1);
			Integer tile = new Integer(key);
			Integer cannon = tileToCannon.get(tile);
			if(cannon == null)
			{
				cannon = new Integer(0);
			}
			if(cannon.intValue()<5)
			{
				if(mymap[Yrow][Xcol+1].getIlk().isPassable())
				{
					int key1 = 0;
					key1 = Yrow<<6|(Xcol+1);
					Integer tile1 = new Integer(key1);
					Integer cannon1 = tileToCannon.get(tile1);
					if(cannon1 == null)
					{
						cannon1 = new Integer(0);
					}
					if(cannon1.intValue()<5)
					{
						if(mymap[Yrow-1][Xcol].getIlk().isPassable())
						{
								int key2 = 0;
								key2 = (Yrow-1)<<6|(Xcol);
								Integer tile2 = new Integer(key2);
								Integer cannon2= tileToCannon.get(tile2);
								if(cannon2 == null)
								{
									cannon2 = new Integer(0);
								}
								if(cannon2.intValue()<5)
								{
									Tile t_tile = mymap[Yrow-1][Xcol+1].getTile();
									Node ruNode = new Node(t_tile);
									if(isInList(ruNode,newNode.parent)==false)
									{
										ruNode.parent = newNode;
										ruNode.gvalue = newNode.gvalue+14;
										ruNode.priority =2;
										
										ruNode.hvalue = ruNode.getH(endStatus);
										ruNode.fvalue = ruNode.gvalue+ruNode.hvalue;
										
										newNodeChild.add(ruNode);
									}
								}
						}
					}
				}
			}
		}
		if(mymap[Yrow-1][Xcol].getIlk().isPassable())
		{
				int key = 0;
				key = (Yrow-1)<<6|(Xcol);
				Integer tile = new Integer(key);
				Integer cannon = tileToCannon.get(tile);
				if(cannon == null)
				{
					cannon = new Integer(0);
				}
				if(cannon.intValue()<5)
				{
					Tile t_tile = mymap[Yrow-1][Xcol].getTile();
					Node uNode = new Node(t_tile);
					if(isInList(uNode,newNode.parent)==false)
					{
						uNode.parent = newNode;
						uNode.gvalue = newNode.gvalue+10;
						uNode.priority = 3;
						
						uNode.hvalue = uNode.getH(endStatus);
						uNode.fvalue = uNode.gvalue+uNode.hvalue;
						
						newNodeChild.add(uNode);
					}
				}
		}
		if(mymap[Yrow-1][Xcol-1].getIlk().isPassable())
		{
			int key = 0;
			key = (Yrow-1)<<6|(Xcol-1);
			Integer tile = new Integer(key);
			Integer cannon = tileToCannon.get(tile);
			if(cannon == null)
			{
				cannon = new Integer(0);
			}
			if(cannon.intValue()<5)
			{
				if(mymap[Yrow-1][Xcol].getIlk().isPassable())
				{
					int key1 = 0;
					key1 = (Yrow-1)<<6|(Xcol);
					Integer tile1 = new Integer(key1);
					Integer cannon1 = tileToCannon.get(tile1);
					if(cannon1 == null)
					{
						cannon1 = new Integer(0);
					}
					if(cannon1.intValue()<5)
					{
						if(mymap[Yrow][Xcol-1].getIlk().isPassable())
						{
								int key2 = 0;
								key2 = (Yrow)<<6|(Xcol-1);
								Integer tile2 = new Integer(key2);
								Integer cannon2 = tileToCannon.get(tile2);
								if(cannon2 == null)
								{
									cannon2= new Integer(0);
								}
								if(cannon2.intValue()<5)
								{
									Tile t_tile = mymap[Yrow-1][Xcol-1].getTile();
									Node luNode = new Node(t_tile);
									if(isInList(luNode,newNode.parent)==false)
									{
										luNode.parent = newNode;
										luNode.gvalue = newNode.gvalue+14;
										luNode.priority = 4;
										
										luNode.hvalue = luNode.getH(endStatus);
										luNode.fvalue = luNode.gvalue+luNode.hvalue;
										
										newNodeChild.add(luNode);
									}
								}
						}
					}
				}
			}
		}
	    if(mymap[Yrow][Xcol-1].getIlk().isPassable())
	    {
	    	int key = 0;
			key = (Yrow)<<6|(Xcol-1);
			Integer tile = new Integer(key);
			Integer cannon = tileToCannon.get(tile);
			if(cannon == null)
			{
				cannon = new Integer(0);
			}
			if(cannon.intValue()<5)
			{
				Tile t_tile = mymap[Yrow][Xcol-1].getTile();
				Node lNode = new Node(t_tile);
				if(isInList(lNode,newNode.parent)==false)
				{
					lNode.parent = newNode;
					lNode.gvalue = newNode.gvalue+10;
					lNode.priority = 5;
					
					lNode.hvalue = lNode.getH(endStatus);
					lNode.fvalue = lNode.gvalue+lNode.hvalue;
					
					newNodeChild.add(lNode);
				}
			}
		}
		if(mymap[Yrow+1][Xcol-1].getIlk().isPassable())
		{	
			int key = 0;
			key = (Yrow+1)<<6|(Xcol-1);
			Integer tile = new Integer(key);
			Integer cannon = tileToCannon.get(tile);
			if(cannon == null)
			{
				cannon = new Integer(0);
			}
			if(cannon.intValue()<5)
			{
				if(mymap[Yrow][Xcol-1].getIlk().isPassable())
				{
					int key1= 0;
					key1= (Yrow)<<6|(Xcol-1);
					Integer tile1= new Integer(key1);
					Integer cannon1= tileToCannon.get(tile1);
					if(cannon1 == null)
					{
						cannon1 = new Integer(0);
					}
					if(cannon1.intValue()<5)
					{
						if(mymap[Yrow+1][Xcol].getIlk().isPassable())
						{
								int key2 = 0;
								key2 = (Yrow+1)<<6|(Xcol);
								Integer tile2 = new Integer(key2);
								Integer cannon2 = tileToCannon.get(tile2);
								if(cannon2 == null)
								{
									cannon2 = new Integer(0);
								}
								if(cannon2.intValue()<5)
								{
									Tile t_tile = mymap[Yrow+1][Xcol-1].getTile();
									Node ldNode = new Node(t_tile);
									if(isInList(ldNode,newNode.parent)==false)
									{
										ldNode.parent = newNode;
										ldNode.gvalue = newNode.gvalue+14;
										ldNode.priority = 6;
										
										ldNode.hvalue = ldNode.getH(endStatus);
										ldNode.fvalue = ldNode.gvalue + ldNode.hvalue;
										
										newNodeChild.add(ldNode);
									}
								}
						}
					}
				}
			}
		}
		
	   if(mymap[Yrow+1][Xcol].getIlk().isPassable())
	   {

	   		int key = 0;
			key = (Yrow+1)<<6|(Xcol);
			Integer tile = new Integer(key);
			Integer cannon = tileToCannon.get(tile);
			if(cannon == null)
			{
				cannon = new Integer(0);
			}
			if(cannon.intValue()<5)
			{
				Tile t_tile = mymap[Yrow+1][Xcol].getTile();
				Node dNode = new Node(t_tile);
				if(isInList(dNode,newNode.parent)==false)
				{
					dNode.parent = newNode;
					dNode.gvalue = newNode.gvalue+10;
					dNode.priority = 7;
					
					dNode.hvalue = dNode.getH(endStatus);
					dNode.fvalue = dNode.gvalue+dNode.hvalue;
					
					newNodeChild.add(dNode);
				}
			}
	   }
	   if(mymap[Yrow+1][Xcol+1].getIlk().isPassable())
	   {
	   		int key = 0;
			key = (Yrow+1)<<6|(Xcol+1);
			Integer tile = new Integer(key);
			Integer cannon = tileToCannon.get(tile);
			if(cannon == null)
			{
				cannon = new Integer(0);
			}
			if(cannon.intValue()<5)
			{
				if(mymap[Yrow][Xcol+1].getIlk().isPassable())
				{
						int key1 = 0;
						key1 = (Yrow)<<6|(Xcol+1);
						Integer tile1 = new Integer(key1);
						Integer cannon1 = tileToCannon.get(tile1);
						if(cannon1 == null)
						{
							cannon1 = new Integer(0);
						}
						if(cannon1.intValue()<5)
						{
							if(mymap[Yrow+1][Xcol].getIlk().isPassable())
							{

								int key2 = 0;
								key2 = (Yrow+1)<<6|(Xcol);
								Integer tile2 = new Integer(key2);
								Integer cannon2 = tileToCannon.get(tile2);
								if(cannon2 == null)
								{
									cannon2 = new Integer(0);
								}
								if(cannon2.intValue()<5)
								{
									Tile t_tile = mymap[Yrow+1][Xcol+1].getTile();
									Node rdNode = new Node(t_tile);
									if(isInList(rdNode,newNode.parent)==false)
									{
										rdNode.parent = newNode;
										rdNode.gvalue = newNode.gvalue+14;
										rdNode.priority = 8;
										
										rdNode.hvalue = rdNode.getH(endStatus);
										rdNode.fvalue = rdNode.gvalue+rdNode.hvalue;
										
										newNodeChild.add(rdNode);
									}
								}
							}
						}
				}
			}
	   }
	}
	private void makeZone(Node newNode,List<Node> newNodeChild)
	{
		//List<Node> zone = new ArrayList<Node>();
		int Xcol = newNode.getStatus().getCol();
		int Yrow = newNode.getStatus().getRow();
		for(int i=-1;i<=1;i++)
		{
			int Y = Yrow+i;
			for(int j=-1;j<=1;j++)
			{
				int X = Xcol+j;
				if(i==0&&j==0)
				{
					continue;
				}
				if(mymap[Y][X].getIlk().isDefend())
				{
					Tile tile = mymap[Y][X].getTile();
					Node gNode = new Node(tile);
					gNode.parent = newNode;
					if(Math.abs(i)==1&&Math.abs(j)==1)
					{
						gNode.gvalue = newNode.gvalue+14;
					}
					else
					{
						gNode.gvalue = newNode.gvalue+10;
					}
					
					if(j==1)
					{
						if(i==0)
						{
							gNode.priority = 1;
						}
						else if(i==-1)
						{
							gNode.priority = 2;
						}
						else
						{
							gNode.priority = 8;
						}
					}
					else if(j==0)
					{
						if(i==-1)
						{
							gNode.priority = 3;
						}
						else
						{
							gNode.priority = 7;
						}
					}
					else if(j==-1)
					{
						if(i==0)
						{
							gNode.priority = 5;
						}
						else if(i==-1)
						{
							gNode.priority = 4;
						}
						else
						{
							gNode.priority = 6;
						}
					}
					gNode.hvalue = gNode.getH(endStatus);
					gNode.fvalue = gNode.gvalue+gNode.hvalue;
					newNodeChild.add(gNode);
				}
			}
		}
		if(mymap[Yrow][Xcol+1].getIlk().isPassable()&&mymap[Yrow][Xcol+1].getIsTower()==false)
		{
			Tile tile = mymap[Yrow][Xcol+1].getTile();
			Node rNode = new Node(tile);
			if(isInList(rNode,newNode.parent)==false)
			{
				rNode.parent = newNode;
				rNode.gvalue = newNode.gvalue+10;
				rNode.priority = 1;
				
				rNode.hvalue = rNode.getH( endStatus);
				rNode.fvalue = rNode.gvalue+rNode.hvalue;
				
				newNodeChild.add(rNode);
			}
		}
		if(mymap[Yrow-1][Xcol+1].getIlk().isPassable()&&mymap[Yrow-1][Xcol+1].getIsTower()==false)
		{
			if(mymap[Yrow][Xcol+1].getIlk().isPassable()&&mymap[Yrow][Xcol+1].getIsTower()==false)
			{
				if(mymap[Yrow-1][Xcol].getIlk().isPassable()&&mymap[Yrow-1][Xcol].getIsTower()==false)
				{
					Tile tile = mymap[Yrow-1][Xcol+1].getTile();
					Node ruNode = new Node(tile);
					if(isInList(ruNode,newNode.parent)==false)
					{
						ruNode.parent = newNode;
						ruNode.gvalue = newNode.gvalue+14;
						ruNode.priority =2;
						
						ruNode.hvalue = ruNode.getH(endStatus);
						ruNode.fvalue = ruNode.gvalue+ruNode.hvalue;
						
						newNodeChild.add(ruNode);
					}
				}
			}
		}
		if(mymap[Yrow-1][Xcol].getIlk().isPassable()&&mymap[Yrow-1][Xcol].getIsTower()==false)
		{
			Tile tile = mymap[Yrow-1][Xcol].getTile();
			Node uNode = new Node(tile);
			if(isInList(uNode,newNode.parent)==false)
			{
				uNode.parent = newNode;
				uNode.gvalue = newNode.gvalue+10;
				uNode.priority = 3;
				
				uNode.hvalue = uNode.getH(endStatus);
				uNode.fvalue = uNode.gvalue+uNode.hvalue;
				
				newNodeChild.add(uNode);
			}
		}
		if(mymap[Yrow-1][Xcol-1].getIlk().isPassable()&&mymap[Yrow-1][Xcol-1].getIsTower()==false)
		{
			if(mymap[Yrow-1][Xcol].getIlk().isPassable()&&mymap[Yrow-1][Xcol].getIsTower()==false)
			{
				if(mymap[Yrow][Xcol-1].getIlk().isPassable()&&mymap[Yrow][Xcol-1].getIsTower()==false)
				{
					Tile tile = mymap[Yrow-1][Xcol-1].getTile();
					Node luNode = new Node(tile);
					if(isInList(luNode,newNode.parent)==false)
					{
						luNode.parent = newNode;
						luNode.gvalue = newNode.gvalue+14;
						luNode.priority = 4;
						
						luNode.hvalue = luNode.getH(endStatus);
						luNode.fvalue = luNode.gvalue+luNode.hvalue;
						
						newNodeChild.add(luNode);
					}
				}
			}
		}
	    if(mymap[Yrow][Xcol-1].getIlk().isPassable()&&mymap[Yrow][Xcol-1].getIsTower()==false)
	    {
			Tile tile = mymap[Yrow][Xcol-1].getTile();
			Node lNode = new Node(tile);
			if(isInList(lNode,newNode.parent)==false)
			{
				lNode.parent = newNode;
				lNode.gvalue = newNode.gvalue+10;
				lNode.priority = 5;
				
				lNode.hvalue = lNode.getH(endStatus);
				lNode.fvalue = lNode.gvalue+lNode.hvalue;
				
				newNodeChild.add(lNode);
			}
		}
		if(mymap[Yrow+1][Xcol-1].getIlk().isPassable()&&mymap[Yrow+1][Xcol-1].getIsTower()==false)
		{			
			if(mymap[Yrow][Xcol-1].getIlk().isPassable()&&mymap[Yrow][Xcol-1].getIsTower()==false)
			{
				if(mymap[Yrow+1][Xcol].getIlk().isPassable()&&mymap[Yrow+1][Xcol].getIsTower()==false)
				{
					Tile tile = mymap[Yrow+1][Xcol-1].getTile();
					Node ldNode = new Node(tile);
					if(isInList(ldNode,newNode.parent)==false)
					{
						ldNode.parent = newNode;
						ldNode.gvalue = newNode.gvalue+14;
						ldNode.priority = 6;
						
						ldNode.hvalue = ldNode.getH(endStatus);
						ldNode.fvalue = ldNode.gvalue + ldNode.hvalue;
						
						newNodeChild.add(ldNode);
					}
				}
			}
		}
		
	   if(mymap[Yrow+1][Xcol].getIlk().isPassable()&&mymap[Yrow+1][Xcol].getIsTower()==false)
	   {
			Tile tile = mymap[Yrow+1][Xcol].getTile();
			Node dNode = new Node(tile);
			if(isInList(dNode,newNode.parent)==false)
			{
				dNode.parent = newNode;
				dNode.gvalue = newNode.gvalue+10;
				dNode.priority = 7;
				
				dNode.hvalue = dNode.getH(endStatus);
				dNode.fvalue = dNode.gvalue+dNode.hvalue;
				
				newNodeChild.add(dNode);
			}
	   }
	   if(mymap[Yrow+1][Xcol+1].getIlk().isPassable()&&mymap[Yrow+1][Xcol+1].getIsTower()==false)
	   {
			if(mymap[Yrow][Xcol+1].getIlk().isPassable()&&mymap[Yrow][Xcol+1].getIsTower()==false)
			{
				if(mymap[Yrow+1][Xcol].getIlk().isPassable()&&mymap[Yrow+1][Xcol].getIsTower()==false)
				{
					Tile tile = mymap[Yrow+1][Xcol+1].getTile();
					Node rdNode = new Node(tile);
					if(isInList(rdNode,newNode.parent)==false)
					{
						rdNode.parent = newNode;
						rdNode.gvalue = newNode.gvalue+14;
						rdNode.priority = 8;
						
						rdNode.hvalue = rdNode.getH(endStatus);
						rdNode.fvalue = rdNode.gvalue+rdNode.hvalue;
						
						newNodeChild.add(rdNode);
					}
				}
			}
	   }
	}
	private void setSuccess()
	{
		this.isPath = true;
	}
	public boolean hasPath()
	{
		return this.isPath;
	}
	private void setPath(Node endNode)
	{
		this.endPath = endNode;
	}
	public Node getEndPath()
	{
		return this.endPath;
	}
	public int getLength()
	{
		return this.endPath.gvalue;
	}
	public List<Tile> getPath()
	{
		if(this.isPath == true)
		{
			this.path = new ArrayList<Tile>();
			Node newNode =endPath;
			while(newNode != null)
			{
				path.add(newNode.status);
				newNode = newNode.parent;
				
			}
			return path;
		}
		return null;
	}
	
	private boolean isOPenOrCloseIn(Node newNode,PriorityQueue<Node> openList)
	{
		Iterator<Node> iter = openList.iterator();
		while(iter.hasNext())
		{
			Node iterNode = iter.next();
			if(iterNode.equal(newNode.status))
			{
				return true;
			}
		}
		return false;
	}
	/*A* pathfinder online data*/
	private boolean checkInline(Tile startStatus,Tile endStatus)
	{
		if(startStatus.getRow()!=endStatus.getRow()&&startStatus.getCol()!=endStatus.getCol())
		{
			return false;
		}
		return true;
	}
	private int checkObstacleInline(Tile startStatus,Tile endStatus)
	{
		/*行*/
		if(startStatus.getRow() == endStatus.getRow())
		{
			int index = 0;
			int boundary = 0;
			if(startStatus.getCol()<endStatus.getCol())
			{
				index = startStatus.getCol();
				boundary = endStatus.getCol();
			}
			else
			{
				index = endStatus.getCol();
				boundary = startStatus.getCol();
			}
			index = index+1;
			for(;index<boundary;index++)
			{
				if(mymap[startStatus.getRow()][index].getIlk().isPassable()==false||mymap[startStatus.getRow()][index].getIsTower()==true)
				{
					return 1;
				}
			}
		}
		else if(startStatus.getCol() == endStatus.getCol())
		{/*列*/
			int index = 0;
			int boundary = 0;
			if(startStatus.getRow()<endStatus.getRow())
			{
				index = startStatus.getRow();
				boundary = endStatus.getRow();
			}
			else
			{
				index = endStatus.getRow();
				boundary = endStatus.getRow();
			}
			index = index+1;
			for(;index<boundary;index++)
			{
				if(mymap[index][startStatus.getCol()].getIlk().isPassable()==false||mymap[index][startStatus.getCol()].getIsTower()==true)
				{
					return 2;
				}
			}
		}
		return 0;
	}
	public void run()
	{
		//System.out.println("AStar in run~~~~~");
		boolean first = true;
		open.clear();
		close.clear();
		initStart();
		while(open.isEmpty()==false)
		{
			//System.out.println("AStar in open.isEmpty()==false~~~~~");
			Node newNode = open.poll();
			close.add(newNode);
			//System.out.println("Astar in NewNode "+newNode.status.getRow()+"."+newNode.status.getCol());
			if(newNode.equal(endStatus)&&newNode !=null)
			{/*
				System.out.println("!!!!AStar in endStatus");
				Node test = newNode;
				while(test!=null)
				{
					System.out.print(test.status.getRow());
					System.out.print('-');
					System.out.print(test.status.getCol());					
					test=test.parent;
					if(test!=null)
					System.out.print("-->");
				}
				System.out.println();*/
				setSuccess();
				setPath(newNode);
				break;
			}
			if(first==true&&open.isEmpty()==false)
			{
				first = false;
				if(checkInline(startStatus,endStatus) == false)
				{
				/*	System.out.println("not in the same line");
					Node seNode = open.poll();
					System.out.println("se"+seNode.priority);
					System.out.println("New"+newNode.priority);
					if(seNode.priority<newNode.priority)
					{
						newNode = seNode;
						close.add(seNode);
					}
					else
					{
						open.add(seNode);
					}*/
				}
				else 
				{
					int preObstacle = checkObstacleInline(startStatus,endStatus);
					if(preObstacle!=0)
					{
						if(preObstacle == 1)
						{
						
							List<Node> rowNode = new ArrayList<Node>();
							Node seNode = open.poll();
							while(seNode.status.getRow() == startStatus.getRow())
							{
								rowNode.add(seNode);
								seNode = open.poll();
								if(seNode == null)
									break;
							}
							if(seNode != null)
							newNode = seNode;
							if(rowNode.isEmpty()==false)
							{
								close.addAll(rowNode);
							}
							if(seNode!=null)
							close.add(seNode);
						}
						else if(preObstacle == 2)
						{
							
							List<Node> colNode = new ArrayList<Node>();
							Node seNode = open.poll();
							while(seNode.status.getCol() == startStatus.getCol())
							{
								colNode.add(seNode);
								seNode = open.poll();
								if(seNode == null)
									break;
							}
							if(seNode != null)
								newNode = seNode;
							if(colNode.isEmpty()==false)
							{
								close.addAll(colNode);
							}
							if(seNode != null)
							close.add(seNode);
						}
					}
				}
			}
		
			//System.out.println("newNode "+newNode.status.getRow()+newNode.status.getCol());
			
			List<Node> newNodeChild = new ArrayList<Node>();
			if(isGA == false)
			{
				makeZone(newNode,newNodeChild);
			}
			else
			{
				makeZoneGA(newNode,newNodeChild);
			}
			//System.out.println("newNodeChild "+newNodeChild.size());
			for(Node iter:newNodeChild)
			{
				boolean remove = false;
				//System.out.println("iter newNodeChild "+iter.status.getRow()+iter.status.getCol());
				if(isOPenOrCloseIn(iter,open) == true)
				{
					Iterator<Node> iterNew = open.iterator();
					while(iterNew.hasNext())
					{
						Node iterNode = iterNew.next();
						
						
						if(iterNode.equal(iter.status))
						{
							if(iterNode.priority != newNode.priority)
							{
								
							}
							
							if(iter.gvalue < iterNode.gvalue)
							{
								iterNode.parent = iter.parent;
								iterNode.gvalue = iter.gvalue;
								iterNode.hvalue = iter.hvalue;
								iterNode.fvalue = iter.fvalue;
								
								iterNode.priority = iter.priority;
							//	System.out.println("changed !!!!!!!!!!!!!!!!!");
							//	System.out.println("newNode~~~~~~~~~~~~");
							//	System.out.println("status "+iter.status.getRow()+"|"+iter.status.getCol());
								//
								remove = true;
								//open.remove(iterNode);
								//open.add(iter);
							}
							
						}
						if(remove ==true)
						{
							iterNew.remove();
							
							//open.add(iter);
						}
						
					}
					//System.out.println("AStar open in");
				}
				else if(isOPenOrCloseIn(iter,close)==true)
				{
					//System.out.println("AStar close in");
				/*	Iterator<Node> iterNew = close.iterator();
					while(iterNew.hasNext())
					{
						Node iterNode = iterNew.next();
						if(iterNode.equal(iter.status))
						{
							if(iter.gvalue < iterNode.gvalue)
							{
								iterNode.parent = iter.parent;
								iterNode.gvalue = iter.gvalue;
								iterNode.fvalue = iter.fvalue;
								iterNode.priority = iter.priority;
								close.remove(iterNode);
								open.add(iter);
							}
						}
					}*/
					continue;
				}
				else
				{
					//System.out.println("AStar open add");
					open.add(iter);
				}
				if(remove == true)
				{
					open.add(iter);
				}
			}
		}
		//System.out.println("Astar Run END~~~~");
	}
	/* this is my A**/
	 private void debugAstar(String format ,Object... args) {
	    	if (!write_Astar)
	    		return;
	    	FileOutputStream file;
			try {
				file = new FileOutputStream("debugAstar.txt",true);
			} catch (FileNotFoundException e) {
				write_Astar = false;
				return;
			}
	    	PrintStream p = new PrintStream(file);
	    	
	    	p.printf(format, args);
	    	try {
				file.close();
			} catch (IOException e) {
			}
	    }/*
	public void run()
	{
		System.out.println("AStar in");
		initStart();
		while(open.isEmpty()==false)
		{
			Node newNode = open.poll();
			close.add(newNode);
			System.out.println("newNode "+newNode.status.getRow()+newNode.status.getCol());
			if(newNode.equal(endStatus))
			{
				System.out.println("AStar endStatus");
				Node fNode = newNode;
				while(fNode != null)
				{
					debugAstar("%d",fNode.status.getRow(),fNode.status.getCol());
					debugAstar("%s","/n");
					fNode = fNode.parent;
				}
				debugAstar("%s","end for this search~~~~~~~~~~~~");
				setSuccess();
				setPath(newNode);
				break;
			}
			List<Node> newNodeChild = new ArrayList<Node>();
			makeZone(newNode,newNodeChild);
			System.out.println("NewNodeChild size "+newNodeChild.size());
			
			for(Node iter:newNodeChild)
			{
				System.out.println("!!!!for Node iter"+iter.status.getRow()+iter.status.getCol());
				if(isOPenOrCloseIn(iter,open) == true)
				{
					Iterator<Node> iterNew = open.iterator();
					while(iterNew.hasNext())
					{
						Node iterNode = iterNew.next();
						if(iterNode.equal(iter.status))
						{
							if(iter.gvalue < iterNode.gvalue)
							{
								iterNode.parent = iter.parent;
								iterNode.gvalue = iter.gvalue;
								iterNode.fvalue = iter.fvalue;
								iterNode.priority = iter.priority;
							}
						}
					}
				//	System.out.println("AStar open in");
				}
				else if(isOPenOrCloseIn(iter,close)==true)
				{
					Iterator<Node> iterNew = close.iterator();
					while(iterNew.hasNext())
					{
						Node iterNode = iterNew.next();
						if(iterNode.equal(iter.status))
						{
							if(iter.gvalue < iterNode.gvalue)
							{
								iterNode.parent = iter.parent;
								iterNode.gvalue = iter.gvalue;
								iterNode.fvalue = iter.fvalue;
								iterNode.priority = iter.priority;
								close.remove(iterNode);
								open.add(iter);
							}
						}
					}
					//System.out.println("AStar close in");
				}
				else
				{
					open.add(iter);
					//System.out.println("AStar add in");
				}
			}
		}
	}*/
	 class NodeComparator implements Comparator<Node> {  
        @Override  
        public int compare(Node x, Node y) { 
   	     /*   System.out.println("X Status"+x.status.getRow()+' '+x.status.getCol());
			System.out.println(x.gvalue+" hvalue "+x.hvalue);
			System.out.println(" pri "+x.priority);
			System.out.println("Y Status"+y.status.getRow()+' '+y.status.getCol());
			System.out.println(y.gvalue+" hvalue "+y.hvalue);
			System.out.println(" pri "+y.priority);*/
        	if(x.fvalue>y.fvalue)
        	{
        		
				return 1;
			}
			else if(x.fvalue<y.fvalue)
			{
				return -1;
			}
			else if(x.fvalue == y.fvalue)
			{
				if(x.parent!=null&&y.parent!=null)
				{	
					if(x.parent.equal(y.parent.status))
					{
					/*	System.out.println("X Status"+x.status.getRow()+' '+x.status.getCol());
						System.out.println(x.gvalue+" hvalue "+x.hvalue);
						System.out.println(" pri "+x.priority);
						System.out.println("Y Status"+y.status.getRow()+' '+y.status.getCol());
						System.out.println(y.gvalue+" hvalue "+y.hvalue);
						System.out.println(" pri "+y.priority);*/
						return x.priority - y.priority;
					}
				}
				return 0;
			}
            //return x.fvalue - y.fvalue;  
			return x.fvalue - y.fvalue; 
        }  
    }  
}
