import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Genome
{
	public List<Integer> vecBits;
	double fitness =0;

	public Genome( int numBits, int geneLength)
	{
		fitness = 0;
		vecBits = new ArrayList<Integer>();
		int numFire =0;
		for(int i=0;i<numBits;i++)
		{
			int bit = Util.RandInt(geneLength);
			if(bit>=5)
			{
				numFire++;
			}
			if(numFire>20)
			{
				bit =0;
			}
			Integer bitInt = new Integer(bit);
			vecBits.add(bitInt);
		}
	}
	public Genome(List<Integer> lastDecisions,int geneLength ,int enemyTotalLife)
	{
		fitness =0;
		vecBits = new ArrayList<Integer>();
		int attackPower =0;
		for(int i=0;i<lastDecisions.size();i++)
		{
			int bit = lastDecisions.get(i).intValue();
			if(attackPower>enemyTotalLife*(2))
			{
				bit =0;
			}
			if(bit>=5)
			{
				int type = Util.decodeType(bit);
				int strength = Util.decodeStrength(bit);
				int power= Util.decodePower(type, strength);
				attackPower+=power;
			}
			else
			{
				if(attackPower<enemyTotalLife)
				{
					bit = Util.RandInt(geneLength);
				}
				
			}
			Integer bitInt = new Integer(bit);
			vecBits.add(bitInt);
			
		}
	}
	public Genome( int numBits, int geneLength,int enemyTotalLife)
	{
		fitness = 0;
		vecBits = new ArrayList<Integer>();
		int attackPower =0;
		for(int i=0;i<numBits;i++)
		{
			int bit = Util.RandInt(geneLength);
			if(attackPower>enemyTotalLife)
			{
				bit = 0;
			}
			if(bit>=5)
			{
				int type = Util.decodeType(bit);
				int strength = Util.decodeStrength(bit);
				int power= Util.decodePower(type, strength);
				attackPower+=power;
			}
			
			Integer bitInt = new Integer(bit);
			vecBits.add(bitInt);
		}
	}
	public Genome()
	{
		fitness =0;
		vecBits = new ArrayList<Integer>();
	}
	public Genome(int numBits)
	{
		fitness = 0;
		vecBits = new ArrayList<Integer>();
		for(int i=0;i<numBits;i++)
		{
			int bit = 0;
			if(i==41||i==42)
				bit =9;
			Integer bitInt = new Integer(bit);
			vecBits.add(bitInt);
		}
	}
	public void resetGenome(Map<Integer,Boolean> resetCannonList)
	{
		if(resetCannonList == null)
		{
			return;
		}
		for(int i=0;i<vecBits.size();i++)
		{
			Integer cannon = vecBits.get(i);
			if(cannon.intValue()>=5)
			{
				if(resetCannonList != null)
				{
					Integer ID = new Integer(i);
					Boolean isAttack = resetCannonList.get(ID);
					if(isAttack == null)
					{
						vecBits.set(i, new Integer(0));
						continue;
					}
					if(isAttack.booleanValue() == false)
					{
						vecBits.set(i, new Integer(0));
					}
				}
			}
		}
	}
	public List<Integer> getGenome()
	{
		return this.vecBits;
	}
	public double getFitness()
	{
		return this.fitness;
	}
	public void setFitness(double fitness)
	{
		this.fitness = fitness;
	}
	
}
