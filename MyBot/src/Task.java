
public class Task {
	public boolean isDestroy=false;
	public boolean isBuild=false;
	public boolean isStrength=false;
	public int type;
	
	public int strength;
	public int baseStrength;
	
	public Tile position;
	public Task(int type,int strength,Tile position)
	{
		this.type = type;
		this.strength = strength;
		this.position =position;
	}
}
