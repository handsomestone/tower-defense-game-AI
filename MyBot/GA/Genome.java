public class Genome
{
	List<Integer> vecBits;
	double fitness =0;

	public Genome(const int numBits,const int geneLength)
	{
		fitness = 0;
		vecBits = new ArrayList<Integer>();
		for(int i=0;i<numBits;i++)
		{
			int bit = Util.RandInt(geneLength);
			Integer bitInt = new Integer(bit);
			vecBit.add(bitInt);
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
	
}
