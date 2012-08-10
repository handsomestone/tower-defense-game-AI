import java.util.List;

public class Enemy{
	private int enemyID;
	
	private int life;
	private float speed;
	private Tile startPoint;
	private int timeToCome;
	private int timeToMove;
	
	private List<Tile> possiblePath;
	private int pathLength;
	private Tile endPoint;

	private List<Tile> newPath;
	private int newPathLength;
	private Tile newEndPoint;
	
	public Enemy(int timeToCome,int life,int timeToMove,Tile startPoint)
	{
		this.startPoint = startPoint;
		this.life = life;
		this.timeToCome = timeToCome;
		this.timeToMove = timeToMove;
	}
	public void setEnemyID(int ID)
	{
		this.enemyID = ID;
	}
	public void setSpeed(int length)
	{
		float floatlength=length;
		float timemove=timeToMove;
		this.speed = floatlength/timemove;
	/*	System.out.println("this.speed "+this.speed);
		System.out.println("this.length "+length);
		System.out.println("this.timeToMove "+timeToMove);*/
	}
	public void setPath(List<Tile> possiblePath)
	{
		this.possiblePath = possiblePath;
	}
	public void setEndPoint(Tile endPoint)
	{
		this.endPoint = endPoint;
	}
	public void setPathLength(int pathlength)
	{
		this.pathLength = pathLength;
	}
	public void setNewPath(List<Tile> newPath)
	{
		this.newPath = newPath;
	}
	public void setNewPathLength(int newPathLength)
	{
		this.newPathLength = newPathLength;
	}
	public void setNewEndPoint(Tile newEndPoint)
	{
		this.newEndPoint = newEndPoint;
	}
	public int getEnemyID()
	{
		return this.enemyID;
	}
	public int getLife()
	{
		return this.life;
	}
	public void subLife(int hurtLife)
	{
		this.life = this.life-hurtLife;
	}
	public float getSpeed()
	{
		return this.speed;
	}
	public Tile getStartPoint()
	{
		return this.startPoint;
	}
	public int getTimeToCome()
	{
		return this.timeToCome;
	}
	public int getTimeToMove()
	{
		return this.timeToMove;
	}
	public List<Tile> getPath()
	{
		return this.possiblePath;
	}
	public int getPathLength()
	{
		return this.pathLength;
	}
	public Tile getEndPoint()
	{
		return this.endPoint;
	}
	
}

