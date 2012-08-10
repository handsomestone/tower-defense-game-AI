public class AstarDoer
{
	Tile startStatus;
	Tile endStatus;
	Location map[][];
	boolean isPath ;
	Node endPath ;
	List<Tile> path;
	NodeComparator cmp = new NodeComparator();  
	PriorityQueue<Node> open  ;
	PriorityQueue<Node>  close  ;
	public AstarDoer(Tile startStatus,Tile endStatus,Location map[][] )
	{
		this.startStatus = new Tile(startStatus.getRow(),startStatus.getCol());
		this.endStatus = new Tile(endStatus.getRow(),endStatus.getCol());
		this.map = map;
		this.open = new PriorityQueue<Node> (1000000,cmp); 
		this.close = new PriorityQueue<Node> (1000000,cmp);
		isPath = false;
	}
	
	private void initStart()
	{
		//将开始节点加入open列表
		Node startNode = new Node(startStatus);
		startNode.gvalue = 0;
		startNode.parent = null;
		startNode.hvalue = startNode.getH(endStatus);
		startNode.fvalue = startNode.gvalue+startNode.hvalue;
		startNode.priority = 0;
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
	private void makeZone(Node newNode,list<Node> newNodeChild)
	{
		//List<Node> zone = new ArrayList<Node>();
		int Xcol = newNode.getStatus().getCol();
		int Yrow = newNode.getStatus().getRow();
		if(map[Yrow][Xcol+1].getIlk().isPassable()&&map[Yrow][Xcol+1].getIsTower()==false)
		{
			Tile tile = map[Yrow][Xcol+1].getTile();
			Node rNode = new Node(tile);
			if(isInList(rNode,newNode.parent)==false)
			{
				rNode.parent = newNode;
				rNode.gvalue = newNode.gvalue+10;
				rNode.hvalue = rNode.getH( endStatus);
				rNode.fvalue = rNode.gvalue+rNode.hvalue;
				rNode.priority = 1;
				newNodeChild.add(rNode);
			}
		}
		if(map[Yrow-1][Xcol+1].getIlk().isPassable()&&map[Yrow-1][Xcol+1].getIsTower()==false)
		{
			if(map[Yrow][Xcol+1].getIlk().isPassable()&&map[Yrow][Xcol+1].getIsTower()==false)
			{
				if(map[Yrow-1][Xcol].getIlk().isPassable()&&map[Yrow-1][Xcol].getIsTower()==false)
				{
					Tile tile = map[Yrow-1][Xcol+1].getTile();
					Node ruNode = new Node(tile);
					if(isInList(ruNode,newNode.parent)==false)
					{
						ruNode.parent = newNode;
						ruNode.gvalue = newNode.gvalue+14;
						ruNode.hvalue = ruNode.getH(endStatus);
						ruNode.fvalue = ruNode.gvalue+ruNode.hvalue;
						ruNode.priority =2;
						newNodeChild.add(ruNode);
					}
				}
			}
		}
		if(map[Yrow-1][Xcol].getIlk().isPassable()&&map[Yrow-1][Xcol].getIsTower()==false)
		{
			Tile tile = map[Yrow-1][Xcol].getTile();
			Node uNode = new Node(tile);
			if(isInList(uNode,newNode.parent)==false)
			{
				uNode.parent = newNode;
				uNode.gvalue = newNode.gvalue+10;
				uNode.hvalue = uNode.getH(endStatus);
				uNode.fvalue = uNode.gvalue+uNode.hvalue;
				uNode.priority = 3;
				newNodeChild.add(uNode);
			}
		}
		if(map[Yrow-1][Xcol-1].getIlk().isPassable()&&map[Yrow-1][Xcol-1].getIsTower()==false)
		{
			if(map[Yrow-1][Xcol].getIlk().isPassable()&&map[Yrow-1][Xcol].getIsTower()==false)
			{
				if(map[Yrow][Xcol-1].getIlk().isPassable()&&map[Yrow][Xcol-1].getIsTower()==false)
				{
					Tile tile = map[Yrow-1][Xcol-1].getTile();
					Node luNode = new Node(tile);
					if(isInList(luNode,newNode.parent)==false)
					{
						luNode.parent = newNode;
						luNode.gvalue = newNode.gvalue+14;
						luNode.hvalue = luNode.getH(endStatus);
						luNode.fvalue = luNode.gvalue+luNode.hvalue;
						luNode.priority = 4;
						newNodeChild.add(luNode);
					}
				}
			}
		}
	    if(map[Yrow][Xcol-1].getIlk().isPassable()&&map[Yrow][Xcol-1].getIsTower()==false)
	    {
			Tile tile = map[Yrow][Xcol-1].getTile();
			Node lNode = new Node(tile);
			if(isInList(lNode,newNode.parent)==false)
			{
				lNode.parent = newNode;
				lNode.gvalue = newNode.gvalue+10;
				lNode.hvalue = lNode.getH(endStatus);
				lNode.fvalue = lNode.gvalue+lNode.hvalue;
				lNode.priority = 5;
				newNodeChild.add(lNode);
			}
		}
		if(map[Yrow+1][Xcol-1].getIlk().isPassable()&&map[Yrow+1][Xcol-1].getIsTower()==false)
		{			
			if(map[Yrow][Xcol-1].getIlk().isPassable()&&map[Yrow][Xcol-1].getIsTower()==false)
			{
				if(map[Yrow+1][Xcol].getIlk().isPassable()&&map[Yrow+1][Xcol].getIsTower()==false)
				{
					Tile tile = map[Yrow+1][Xcol-1].getTile();
					Node ldNode = new Node(tile);
					if(isInList(ldNode,newNode.parent)==false)
					{
						ldNode.parent = newNode;
						ldNode.gvalue = newNode.gvalue+14;
						ldNode.hvalue = ldNode.getH(endStatus);
						ldNode.fvalue = ldNode.gvalue + ldNode.hvalue;
						ldNode.priority = 6;
						newNodeChild.add(ldNode);
					}
				}
			}
		}
		
	   if(map[Yrow+1][Xcol].getIlk().isPassable()&&map[Yrow+1][Xcol].getIsTower()==false)
	   {
			Tile tile = map[Yrow+1][Xcol].getTile();
			Node dNode = new Node(tile);
			if(isInList(dNode,newNode.parent))
			{
				dNode.parent = newNode;
				dNode.gvalue = newNode.gvalue+10;
				dNode.hvalue = dNode.getH(endStatus);
				dNode.fvalue = dNode.gvalue+dNode.hvalue;
				dNode.priority = 7;
				newNodeChild.add(dNode);
			}
	   }
	   if(map[Yrow+1][Xcol+1].getIlk().isPassable()&&map[Yrow+1][Xcol+1].getIsTower()==false)
	   {
			if(map[Yrow][Xcol+1].getIlk().isPassable()&&map[Yrow][Xcol+1].getIsTower()==false)
			{
				if(map[Yrow+1][Xcol].getIlk().isPassable()&&map[Yrow+1][Xcol].getIsTower()==false)
				{
					Tile tile = map[Yrow+1][Xcol+1].getTile();
					Node rdNode = new Node(tile);
					if(isInList(rdNode,newNode.parent))
					{
						rdNode.parent = newNode;
						rdNode.gvalue = newNode.gvalue+14;
						rdNode.hvalue = rdNode.getH(endStatus);
						rdNode.fvalue = rdNode.gvalue+rdNode.hvalue;
						rdNode.priority = 8;
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
	public void getEndPath()
	{
		return this.endPath;
	}
	public List<Tile> getPath()
	{
		if(this.isPath == true)
		{
			this.path = new ArrayList<Tile>();
			Node newNode =endPath;
			while(newNode != null)
			{
				path.add(newNode);
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
	public void run()
	{
		initStart();
		while(open.isEmpty()==false)
		{
			Node newNode = open.poll();
			close.add(newNode);
			if(newNode.equal(endStatus))
			{
			/*
				int i=0;
				System.out.println("success finish this Task~~~~~");				
				while(newNode != null)
				{
					for(int j=0;j<9;j++)
					{
						System.out.print(newNode.status[j/3][j%3]);
						System.out.print("  ");
						if(j%3 == 2)
						{
							System.out.println();
						}
					}
					
					System.out.println("step "+i+"status fvalue"+newNode.fvalue);
					newNode = newNode.parent;
					i+=1;
				}
				break;*/
				setSuccess();
				setPath(newNode);
				break;
			}
			List<Node> newNodeChild = new ArrayList<Node>();
			makeZone(newNode,newNodeChild);
			for(Node iter:newNodeChild)
			{
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
				}
				else if(isOPenOrCloseIn(iter,close)==true)
				{
					/*in the close list ignore it*/
					continue;
				}
				else
				{
					open.add(iter);
				}
			}
		}
	}
	/* this is my A**/
	/*
	public void run()
	{
	
		initStart();
		while(open.isEmpty()==false)
		{
			Node newNode = open.poll();
			close.add(newNode);
			if(newNode.equal(endStatus))
			{
				setSuccess();
				setPath(newNode);
				break;
			}
			List<Node> newNodeChild = new ArrayList<Node>();
			makeZone(newNode,newNodeChild);
			for(Node iter:newNodeChild)
			{
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
				}
				else
				{
					open.add(iter);
				}
			}
		}
	}*/
	 class NodeComparator implements Comparator<Node> {  
        @Override  
        public int compare(Node x, Node y) { 
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
				if(x.parent.equal(y.parent.status))
				{
					return x.priority - y.priority;
				}
				return 0;
			}
            //return x.fvalue - y.fvalue;  
        }  
    }  
}
