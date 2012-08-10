public class Util
{
	private static Random rand = new Random();
	public static int RandInt(int x)
	{
		return rand.nextInt(x);
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
	
}
