public class CannonTower
{
	public boolean isAttack;
	
	private int towerType;
	private int towerStrength;

	private int range;
	private int rechargeTime;
	private int attackPower;
	
	private int towerCost;
	private Tile position;
    TargetComparator cmp = new TargetComparator();  
	PriorityQueue<Target> attackList ;  
	public CannonTower(int towerType,int towerStrength,Tile position)
	{
		this.towerType = towerType;
		this.towerStrength = towerStrength;
		this.position = position;
		this.isAttack = false;
		if(towerType == 0)
		{
			this.range = 3+towerStrength;
			this.attackPower = 10;
			this.rechargeTime = 10 - 2*towerStrength;
			
		}
		else if(towerType == 1)
		{
			this.range = 2;
			this.attackPower = 20*power(5,towerStrength);
			this.rechargeTime = 100;
		}
		else if(towerType == 2)
		{
			this.range = 2;
			this.attackPower =3*(1+towerStrength);
			this.rechargeTime = 20;
		}
		attackList = new PriorityQueue<Target>(1000000,cmp);
	}
	public void addTargetList(List<Target> targetL)
	{
		for(int i=0;i<targetL.size();i++)
		{
			attackList.add(targetL.get(i));
		}
		this.isAttack = true;
	}
	public List<Target> Attack()
	{
		List<Target> hasBeenAttack = new ArrayList<Target>();
		boolean isFirst = true;
		int lastAttackTime =0;
		int hurtLife =0;
		while(attackList.isEmpty == false)
		{
			Target task = attackList.poll();
			if(isFirst == true)
			{
				hurtLife = attackPower;
				lastAttackTime = task.arriveTime;
				while(lastAttackTime+rechargeTime<=task.leaveTime)
				{
					hurtLife+=attackPower;
					lastAttackTime = lastAttackTime+rechargeTime;
				}
				isFirst = false;			
			}
			else
			{
				while((lastAttackTime+rechargeTime)>=task.arriveTime&&(lastAttackTime+rechargeTime)<=task.leaveTime)
				{
					hurtLife +=attackPower;
					lastAttackTime = lastAttackTime+rechargeTime;
				}
			}
			if(hurtLife != 0)
			{
				task.subLife(hurtLife);
				hasBeenAttack.add(task);
				hurtLife = 0;
			}
			
			
		}
		return hasBeenAttack;
	}
	private int power(int num,int pow)
	{
		int total = 1;
		for(int i=0;i<pow;i++)
		{
			total *=num;
		}
		return total;
	}
	public int getTowerType()
	{
		return this.towerType;
	}
	public int getTowerStrength()
	{
		return this.towerStrength;
	}
	public int getTowerCost()
	{
	    towerCost = 0;
		if(towerType == 0)
		{
			towerCost = 10 + ((power(3,towerStrength)-1)/2)*3*10;
		}
		else if(towerType == 1)
		{
			towerCost = 15 + ((power(4,towerStrength)-1)/3)*4*15;
		}
		else
		{
			int n = towerStrength +1;
			towerCost = (n+(n*(n-1))/2)*20;
		}
		return towerCost;
	}
	
	public int getStrengthenCost(int baseStrength,int strength)
	{
		int strengthCost = 0;
		if(towerType == 0)
		{
			strengthCost = ((power(3,strength)-1)/2)*3*10-((power(3,baseStrength)-1)/2)*3*10;
		}
		else if(towerType == 1)
		{
			strengthCost = ((power(4,strength)-1)/3)*4*15 -((power(4,baseStrength)-1)/3)*4*15;
		}
		else if(towerType == 2)
		{
		    int n = baseStrength +1;
			int baseTowerCost = (n+(n*(n-1))/2)*20;
			int m =  strength +1;
			int improveCost = (m+(m*(m-1))/2)*20;
			strengthCost = improveCost - baseTowerCost;
		}
		return strengthCost;
	}
	public int getRange()
	{
		return this.range;
	}
	public int getAttackPower()
	{
		return this.attackPower;
	}
	public int getRechargeTime()
	{
		return this.rechargeTime;
	}
	public boolean checkIsInRange(Node pathNode)
	{
	    int rowDist = pathNode.status.getRow()-position.getRow();
		int colDist = pathNode.status.getCol()-position.getCol();
		int distance = rowDist*rowDist+colDist*colDist;
		if(distance<=(range*range))
		{
			return true;
		}
		return false;
	}
	public boolean checkIsInRange(List<Node> path)
	{
		for(int i=0;i<path.size();i++)
		{
			int rowDist = path.get(i).status.getRow()-position.getRow();
			int colDist = path.get(i).status.getCol()-position.getCol();
			int distance = rowDist*rowDist+colDist*colDist;
			if(distance<=(range*range))
			{
				return true;
			}
		}
		return false;
	}
	class TargetComparator implements Comparator<Target> {  
        @Override  
        public int compare(Target x, Target y) { 
   	       if(x.arriveTime>y.arriveTime)
   	       {
				return 1;
		   }
		   else if(x.arriveTime<y.arriveTime)
		   {
				return -1;
		   }
		   else 
		   {
				if(x.timeToCome>y.timeToCome)
				{
					return 1;
				}
				else if(x.timeToCome<y.timeToCome)
				{
					return -1;
				}
				else
				{
					if(x.ID>y.ID)
					{
						return 1;
					}
					else if(x.ID<y.ID)
					{
						return -1;
					}
					else
					{
						return 0;
					}
				}
		   }
			return x.arriveTime- y.arriveTime; 
        }  
    }  
}
