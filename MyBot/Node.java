public class Node
{
	public Node parent;
	//public int[][] status = new int[3][3];
	Tile status ;
	public int fvalue;
	public int gvalue;
	public int hvalue;
	public int priority;
	public Node(Tile status)
	{
		this.status = new Tile(status.getRow(),status.getCol());
	}
	public Tile getStatus()
	{
		return this.status;
	}
	public void setG(int gvalue)
	{
		this.gvalue = gvalue;
	}

	public boolean equal(Tile endStatus)
	{
		if(status.getRow()==endStatus.getRow())
		{
			if(status.getCol() == endStatus.getCol())
				return true;
		}
		return false;
	}
	public int getH(Tile endStatus)
	{
		int Xlength = 0;
		int Ylength = 0;
		int k=0;
		/*manhadon distance*/
		Xlength = Math.abs(endStatus.getCol()-status.getCol());
		Ylength = Math.abs(endStatus.getRow()-status.getRow());
		k = Xlength+Ylength;
		return k;
	}
	
}

