public class Target
{
	private int ID;
	private int life;
	private int timeToCome;
	private int arriveTime;
	private int leaveTime;
	private int hurtLife;
	//private Integer sE;
	public Target(int ID,int life,timeToCome,arriveTime,leaveTime)
	{
		this.ID = ID;
		this.life = life;
		this.timeToCome = timeToCome;
		this.arriveTime = arriveTime;
		this.leaveTime = leaveTime;
		this.hurtLife =0;
	}

	public int getID()
	{
		return this.ID;
	}
	public int getHurtLife()
	{
		return this.hurtLife;
	}
	public void subLife(int attackPower)
	{
	 // this.life = this.life-attackPower;
	 	this.hurtLife = this.hurtLife+attackPower;
	}
}
