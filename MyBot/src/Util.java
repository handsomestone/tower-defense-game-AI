import java.util.Random;

public class Util
{
	private static Random rand = new Random();
	public static int RandInt(int x)
	{
		int k=x+20;
		int m = rand.nextInt(k);
		if(m>14)
		{
			m=0;
		}
		return m;
	}
	public static double RandFloat()
	{
		
		return rand.nextDouble();
	}
	public static boolean RandBool()
	{
		return rand.nextBoolean();
	}
	public static int decodeType(int key)
	{
		if(key>=5&&key<=9)
		{
			return 0;
		}
		else if(key>=10&&key<=14)
		{
			return 1;
		}
		return 2;
	}
	public static int decodeStrength(int key)
	{
		if(key>=5&&key<=9)
		{
			return key-5;
		}
		else if(key>=10&&key<=14)
		{
			return key-10;
		}
		return key -15;
	}
	public static int decodePower(int towerType,int towerStrength)
	{
		int attackPower =0;
		if(towerType == 0)
		{
			
			attackPower = 10;
			
			
		}
		else if(towerType == 1)
		{
			
			attackPower = 20*power(5,towerStrength);
			
		}
		else if(towerType == 2)
		{
			
			attackPower =3*(1+towerStrength);
			
		}
		return attackPower;
	}
	public static int power(int num,int pow)
	{
		int total = 1;
		for(int i=0;i<pow;i++)
		{
			total *=num;
		}
		return total;
	}
	public static int getTowerCost(int towerType,int towerStrength)
	{
	    int towerCost = 0;
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
	public static int getStrengthenCost(int towerType,int baseStrength,int strength)
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
}
