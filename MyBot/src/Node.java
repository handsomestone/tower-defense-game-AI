public class Node
{
	public Node parent;
	//public int[][] status = new int[3][3];
	public Tile status ;
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
		int p=0;
		int sum=0;
		/*manhadon distance*/
		Xlength = Math.abs(endStatus.getCol()-status.getCol());
		Ylength = Math.abs(endStatus.getRow()-status.getRow());
		k = (Xlength+Ylength)*10;
	
		p = this.priority*6;
		
		sum = k+p;
		
		//sum = k;
		/*eular
		Xlength = Math.abs(endStatus.getCol()-status.getCol());
		Ylength = Math.abs(endStatus.getRow()-status.getRow());
		sum = (int)Math.sqrt((Xlength*Xlength+Ylength*Ylength));*/
		return sum;
	}
	
}

