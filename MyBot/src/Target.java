public class Target
{
	public int ID;
	public int life;
	public int timeToCome;
	public float arriveTime;
	public float leaveTime;
	public int hurtLife;
	//private Integer sE;
	public Target(int ID,int life,int timeToCome,float arriveTime,float leaveTime)
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
