package clashingTSP;

public class IndividuoAlgoritmoClash implements Comparable<IndividuoAlgoritmoClash>{
	private int[] solucion;
	private double costo;
	private double fitness;
	private int suerte;
	
	public IndividuoAlgoritmoClash(int[] solucion, double costo) {
		super();
		this.solucion = solucion;
		this.costo = costo;
		this.fitness = 0;
		this.suerte = 0;
	}
	
	public IndividuoAlgoritmoClash(int[] solucion, double costo, int suerte) {
		super();
		this.solucion = solucion;
		this.costo = costo;
		this.fitness = 0;
		this.suerte = suerte;
	}

	public int getSuerte() {
		return suerte;
	}

	public void setSuerte(int suerte) {
		this.suerte = suerte;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public int[] getSolucion() {
		return solucion;
	}

	public void setSolucion(int[] solucion) {
		this.solucion = solucion;
	}

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

	@Override
	public int compareTo(IndividuoAlgoritmoClash otroIndividuo) {
		if(this.getCosto()<otroIndividuo.getCosto())
	          return -1;
	    else if(otroIndividuo.getCosto()<this.getCosto())
	          return 1;
	    return 0;
	}	
}
