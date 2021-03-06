package clashingTSP;

import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.stream.IntStream;

import Entidades.Solucion;
import SQL.SqlServer;

public class Swap {
	private int[][] matrizF;
	private int[][] matrizD;
	private Random rnd;
	int[] valoresSwapped;
	ArrayList<Integer> memoria;
	int[] solucionSwapped;
	double costo;
	double distancia;

	public Swap(int[][] matrizF, int[][] matrizD) {
		this.matrizF = matrizF;
		this.matrizD = matrizD;
		rnd = new Random();
	}

	public int[][] getMatrizF() {
		return matrizF;
	}

	public int[][] getMatrizD() {
		return matrizD;
	}

	@Override
	public String toString() {
		return "Swap [matrizF=" + Arrays.toString(matrizF) + ", matrizD=" + Arrays.toString(matrizD) + "]";
	}

	public int[] generarSolcuionInicial(int[][] matriz) {
		int[] solucion = new int[matriz.length - 1];
		int i = 0;
		while (i < matriz.length - 1) {
			int indice = rnd.nextInt(matriz.length) + 1;
			if (indice != 1) {
				if (!IntStream.of(solucion).anyMatch(x -> x == indice)) {
					solucion[i] = indice;
					i++;
				}
			}
		}
		return solucion;
	}

	/**
	 * public int[] generarSolcuionInicial(int[][] matriz) { int[] solucion = new
	 * int[matriz.length]; int i = 0; while (i < matriz.length) { int indice =
	 * rnd.nextInt(matriz.length + 1); if (!IntStream.of(solucion).anyMatch(x -> x
	 * == indice)) { solucion[i] = indice; i++; } } return solucion; }
	 */

	public int[] generarSolucionInicialTSP(int[][] matriz) {
		int[] solucion = new int[matriz.length - 1];
		int i = 0;
		while (i < matriz.length - 1) {
			int indice = rnd.nextInt(matriz.length) + 1;
			if (indice != 1) {
				if (!IntStream.of(solucion).anyMatch(x -> x == indice)) {
					solucion[i] = indice;
					i++;
				}
			}
		}
		return solucion;
	}

	public int[] swapping(int[] solucionInicial, int cantidadSwappings) {
		valoresSwapped = new int[cantidadSwappings];
		int i = 0;
		memoria = new ArrayList<>();
		while (i < cantidadSwappings) {
			int num = rnd.nextInt(solucionInicial.length);
			if (!memoria.contains(num) && i != num) {
				if (valoresSwapped[i] == 0) {
					valoresSwapped[i] = solucionInicial[num];
					i++;
					memoria.add(num);
				}
			}
		}
		// valoresSwapped = new int[] {2,3};
		// solucionInicial = new int[] {3,2,1,4};
		// int z = 0;
		solucionSwapped = new int[solucionInicial.length];
		System.arraycopy(solucionInicial, 0, solucionSwapped, 0, solucionInicial.length);
		for (int x = 0; x < valoresSwapped.length; x++) {
			for (int y = 0; y < solucionInicial.length; y++) {
				if (valoresSwapped[x] == solucionInicial[y]) {
					solucionSwapped[y] = valoresSwapped[(cantidadSwappings - 1) - x];
					// z=y+1;
					break;
				}
			}
		}
		return solucionSwapped;
	}

	// Swaps a partir de valores ingresados de la solucion incial, que se modifica
	// su orden en la solucion inicial
	public int[] swapping(int[] solucionInicial, int[] valoresSwapped) {
		solucionSwapped = new int[solucionInicial.length];
		System.arraycopy(solucionInicial, 0, solucionSwapped, 0, solucionInicial.length);
		for (int x = 0; x < valoresSwapped.length; x++) {
			for (int y = 0; y < solucionInicial.length; y++) {
				if (valoresSwapped[x] == solucionInicial[y]) {
					solucionSwapped[y] = valoresSwapped[(valoresSwapped.length - 1) - x];
					// z=y+1;
					break;
				}
			}
		}
		return solucionSwapped;
	}

	public void toStringSolcuionInicial(int[] solucionInicial) {
		System.out.println("Solución inicial: " + Arrays.toString(solucionInicial));
		// System.out.println("A partir de matriz d:
		// "+Arrays.toString(generarSolcuionInicial(matrizD)));
	}

	public void toStringSolucion(int[] solucion, int numero) {
		System.out.println("solucion " + numero + ": " + Arrays.toString(solucion));
		// System.out.println("A partir de matriz d:
		// "+Arrays.toString(generarSolcuionInicial(matrizD)));
	}

	public void toStringSolucionSwapped(int[] solucionInicial, int cantidadSwappings) {
		System.out.println("Swapped solución incial: " + Arrays.toString(swapping(solucionInicial, cantidadSwappings)));
		/**
		 * for(int i : swapping(generarSolcuionInicial(matrizF))){ System.out.print(i+"
		 * "); }
		 */
	}

	public double evaluarCostoSolucion(int[] solucionInicial) {
		distancia = 0;
		// solucionInicial = new int[]{3,4,2};
		// solucionInicial = new
		// int[]{22,31,18,3,17,21,42,7,2,30,23,20,50,29,16,46,44,34,35,36,39,40,37,38,48,24,5,15,6,4,25,12,28,27,26,47,13,14,52,11,51,33,43,10,9,8,41,19,45,32,49};
		try {
			distancia += matrizD[0][solucionInicial[0] - 1];
			for (int i = 0; i < solucionInicial.length; i++) {
				if (i == (solucionInicial.length - 1)) {
					distancia += matrizD[solucionInicial[i] - 1][0];
				} else {
					distancia += matrizD[solucionInicial[i] - 1][solucionInicial[i + 1] - 1];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distancia;
	}

	/**
	 * public double evaluarCostoSolucion(int[] solucionInicial) { costo = 0; try {
	 * for (int i = 0; i < matrizF.length; i++) { for (int j = 0; j <
	 * matrizF[i].length; j++) { costo += matrizF[i][j] * matrizD[solucionInicial[i]
	 * - 1][solucionInicial[j] - 1]; } } } catch (Exception e) {
	 * e.printStackTrace(); } return costo; }
	 */

	public double evaluarDistanciaSolucionTSP(int[] solucionInicial) {
		distancia = 0;
		// solucionInicial = new int[]{3,4,2};
		// solucionInicial = new int[]{2, 3, 4};
		// solucionInicial = new
		// int[]{22,31,18,3,17,21,42,7,2,30,23,20,50,29,16,46,44,34,35,36,39,40,37,38,48,24,5,15,6,4,25,12,28,27,26,47,13,14,52,11,51,33,43,10,9,8,41,19,45,32,49};
		try {
			distancia += matrizD[0][solucionInicial[0] - 1];
			for (int i = 0; i < solucionInicial.length; i++) {
				if (i == (solucionInicial.length - 1)) {
					distancia += matrizD[solucionInicial[i] - 1][0];
				} else {
					distancia += matrizD[solucionInicial[i] - 1][solucionInicial[i + 1] - 1];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distancia;
	}

	public double evaluarDistanciaSolucionTSPLIB(int[] solucionInicial) {
		distancia = 0;
		try {
			// distancia += matrizD[solucionInicial[0] - 1][solucionInicial[0] - 1];//[Fila
			// cero primer elemento][segundo elemento de la solución inicial (columnas)]
			for (int i = 0; i < solucionInicial.length; i++) {
				if (i == (solucionInicial.length - 1)) {// si estamos parados el la última distancia a sumar
					distancia += matrizD[solucionInicial[i] - 1][solucionInicial[0] - 1];// [penúltimo elemento][primer
																							// elemento solución
																							// inicial]
				} else {
					distancia += matrizD[solucionInicial[i] - 1][solucionInicial[i + 1] - 1];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return distancia;
	}

	public int[] calcularListaOrdenadaSumatoriaMatriz(int[][] matriz, boolean ascendente) {
		int[] listaOrdenada = new int[matriz.length];
		for (int i = 0; i < matriz.length; i++) {
			int suma = 0;
			for (int j = 0; j < matriz[i].length; j++) {
				suma += matriz[i][j];
			}
			listaOrdenada[i] = suma;
		}
		if (ascendente) {
			int n = listaOrdenada.length;
			int aux = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 1; j < (n - i); j++) {
					if (listaOrdenada[j - 1] > listaOrdenada[j]) {
						aux = listaOrdenada[j - 1];
						listaOrdenada[j - 1] = listaOrdenada[j];
						listaOrdenada[j] = aux;
					}
				}
			}
		} else {
			int n = listaOrdenada.length;
			int aux = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 1; j < (n - i); j++) {
					if (listaOrdenada[j - 1] < listaOrdenada[j]) {
						aux = listaOrdenada[j - 1];
						listaOrdenada[j - 1] = listaOrdenada[j];
						listaOrdenada[j] = aux;
					}
				}
			}
		}
		return listaOrdenada;
	}

	public void toStringLista(int[] lista, String tipoLista) {
		System.out.println("Lista ordenada " + tipoLista + ": " + Arrays.toString(lista));
	}

	public int[] calcularListaOrdenadaMatriz(int[][] matriz, boolean ascendente) {
		int[] listaOrdenada = new int[matriz.length * matriz.length];
		int z = 0;
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				int val = matriz[i][j];
				if (val != 0) {
					listaOrdenada[z] = val;
					z++;
				}
			}
		}
		if (ascendente) {
			int n = listaOrdenada.length;
			int aux = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 1; j < (n - i); j++) {
					if (listaOrdenada[j - 1] > listaOrdenada[j]) {
						aux = listaOrdenada[j - 1];
						listaOrdenada[j - 1] = listaOrdenada[j];
						listaOrdenada[j] = aux;
					}
				}
			}
		} else {
			int n = listaOrdenada.length;
			int aux = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 1; j < (n - i); j++) {
					if (listaOrdenada[j - 1] < listaOrdenada[j]) {
						aux = listaOrdenada[j - 1];
						listaOrdenada[j - 1] = listaOrdenada[j];
						listaOrdenada[j] = aux;
					}
				}
			}
		}
		return listaOrdenada;
	}

	public double evaluarCostoSolucionListasOrdenadas(int[] listaOrdenadaAscendente, int[] listaOrdenadaDescendente) {
		double costo = 0;
		for (int i = 0; i < listaOrdenadaAscendente.length; i++) {
			costo += listaOrdenadaAscendente[i] * listaOrdenadaDescendente[i];
		}
		return costo;
	}

	public int[] eliminarCerosLista(int[] listaOrdenada) {
		ArrayList<Integer> listaTemp = new ArrayList<>();
		for (int i = 0; i < listaOrdenada.length; i++) {
			int val = listaOrdenada[i];
			if (val != 0) {
				listaTemp.add(val);
			}
		}
		return listaTemp.stream().mapToInt(i -> i).toArray();
	}

	public long calcularTamañoVecindad(int[][] matriz, int cantidadSwappings) {
		// return factorial(matriz.length) / ((factorial(cantidadSwappings) *
		// factorial(matriz.length - cantidadSwappings)));
		return (matriz.length * (matriz.length - 1)) / 2;
	}

	public static long factorial(int n) {
		long resultado = 1;
		for (int i = 1; i <= n; i++) {
			resultado *= i;
		}
		return resultado;
	}

	public void ejercicio1(int[] solucionInicial, int cantidadSwappings, int tamañoVecindad,
			double porcentajeVecindad) {
		long startTime = System.nanoTime();
		double costoSolucionInicial = evaluarCostoSolucion(solucionInicial);
		int[][] vecindad = new int[tamañoVecindad][solucionInicial.length];
		int indiceVecindad = 0;
		while (true) {
			int[] nuevoVecino = swapping(solucionInicial, cantidadSwappings);
			double costoNuevoVecino = evaluarCostoSolucion(nuevoVecino);
			if (costoNuevoVecino < costoSolucionInicial) {
				System.out.println("Costo nuevo mejor vecino: " + costoNuevoVecino);
				System.out.println("Mejor solución: " + Arrays.toString(nuevoVecino));
				break;
			}
			if (!contieneItem(vecindad, nuevoVecino)) {
				int[] nuevoGuardar = new int[nuevoVecino.length];
				System.arraycopy(nuevoVecino, 0, nuevoGuardar, 0, nuevoVecino.length);
				vecindad[indiceVecindad] = nuevoGuardar;
				indiceVecindad++;
			}
			double condicionParada = tamañoVecindad / (100 / porcentajeVecindad);
			if ((indiceVecindad) >= condicionParada) {
				System.out.println("Alcanzado el " + porcentajeVecindad + "% de la vecindad");
				break;
			}
		}
		toStringVecindad(vecindad);
		long endTime = System.nanoTime();
		long totalTime = (endTime - startTime) / 1000000;
		System.out.println("tiempo ejecución: " + totalTime + " milisegundos");
	}

	public void ejercicio1Continuo(int[] solucionInicial, int cantidadSwappings, int tamañoVecindad,
			double porcentajeVecindad) {
		double costoSolucionInicial = evaluarCostoSolucion(solucionInicial);
		ArrayList<int[]> vecindad = new ArrayList<int[]>();
		vecindad.add(solucionInicial);
		while (true) {
			int[] nuevoVecino = swapping(vecindad.get(vecindad.size() - 1), cantidadSwappings);
			double costoNuevoVecino = evaluarCostoSolucion(nuevoVecino);
			if (costoNuevoVecino < costoSolucionInicial) {
				System.out.println("Costo nuevo mejor vecino: " + costoNuevoVecino);
				costoSolucionInicial = costoNuevoVecino;
				// break;
			}
			if (!vecindad.contains(nuevoVecino)) {
				vecindad.add(nuevoVecino);
				if (vecindad.size() >= (tamañoVecindad / (100 / porcentajeVecindad))) {
					break;
				}
			}
		}
		// System.out.println("Mejor solución: "+Arrays.toString(solucion));
		// toStringVecindad(vecindad);
	}

	public void toStringVecindad(int[][] vecindad) {
		System.out.println("Vecindad:");
		for (int i = 0; i < vecindad.length; i++) {
			int[] ceros = new int[vecindad[i].length];
			if (!Arrays.equals(vecindad[i], ceros)) {
				toStringSolucion(vecindad[i], i);
			}
		}
	}

	public ArrayList<Double> ejercicio2(int[] solucionInicial, int cantidadSwappings, int tamañoVecindad,
			double porcentajeVecindad) {
		ArrayList<Double> costos = new ArrayList<Double>();
		int[] memoriaSolucionInicial = new int[solucionInicial.length];
		memoriaSolucionInicial = Arrays.copyOf(solucionInicial, solucionInicial.length);
		// System.arraycopy(solucionInicial, 0, memoriaSolucionInicial, 0,
		// solucionInicial.length);
		// int[] memoriaSolucionInicial = new int[solucionInicial.length];
		// System.arraycopy(solucionInicial, 0, memoriaSolucionInicial, 0,
		// solucionInicial.length);
		long startTime = System.nanoTime();
		double costoSolucionInicial = evaluarCostoSolucion(solucionInicial);
		int[][] vecindad = new int[tamañoVecindad][solucionInicial.length];
		int indiceVecindad = 0;
		while (true) {
			int[] nuevoVecino = swapping(solucionInicial, cantidadSwappings);
			/**
			 * double costoNuevoVecino = evaluarCostoSolucion(nuevoVecino);
			 * if(costoNuevoVecino<costoSolucionInicial) { System.out.println("Costo nuevo
			 * mejor vecino: "+costoNuevoVecino); System.out.println("Mejor solución:
			 * "+Arrays.toString(nuevoVecino)); break; }
			 */
			if (!contieneItem(vecindad, nuevoVecino)) {
				int[] nuevoGuardar = new int[nuevoVecino.length];
				System.arraycopy(nuevoVecino, 0, nuevoGuardar, 0, nuevoVecino.length);
				vecindad[indiceVecindad] = nuevoGuardar;
				indiceVecindad++;
			}
			double condicionParada = tamañoVecindad / (100 / porcentajeVecindad);
			if ((indiceVecindad) >= condicionParada) {
				System.out.println("Alcanzado el " + porcentajeVecindad + "% de la vecindad");
				break;
			}
		}
		int mejorVecino = -1;
		double costoMejorVecino = costoSolucionInicial;
		costos.add(costoMejorVecino);
		for (int i = 0; i < (indiceVecindad); i++) {
			double costoVecinoActual = evaluarCostoSolucion(vecindad[i]);
			if (costoVecinoActual < costoMejorVecino) {
				costoMejorVecino = costoVecinoActual;
				costos.add(costoMejorVecino);
				mejorVecino = i;
			}
		}
		System.out.println("Mejor solución " + porcentajeVecindad + "% de la vecindad: " + costoMejorVecino);
		System.out.println("Mejor vecino: "
				+ Arrays.toString((mejorVecino == -1) ? memoriaSolucionInicial : vecindad[mejorVecino]));
		// toStringVecindad(vecindad);
		long endTime = System.nanoTime();
		long totalTime = (endTime - startTime) / 1000000;
		System.out.println("tiempo ejecución: " + totalTime + " milisegundos");
		return costos;
	}

	public boolean contieneItem(int[][] listaSolcuiones, int[] solucion) {
		boolean contiene = false;
		for (int i = 0; i < listaSolcuiones.length; i++) {
			if (Arrays.equals(listaSolcuiones[i], solucion)) {
				contiene = true;
			}
		}
		return contiene;
	}

	public void guardarSolucion(int[] solucion, String instancia, String problema, double optimo) {
		ArrayList<Solucion> soluciones = new ArrayList<>();
		String fecha = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(Calendar.getInstance().getTime());
		for (int i = 0; i < solucion.length; i++) {
			Solucion sol = new Solucion();
			sol.setOrden(i);
			sol.setNodo(solucion[i]);
			sol.setInstancia(instancia);
			sol.setProblema(problema);
			sol.setOptimo(optimo);
			sol.setFechaEncontrado(fecha);
			soluciones.add(sol);
		}
		try {
			SqlServer.guardar(soluciones);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void guardarSolucionAPI(int[] solucion, String instancia, String problema, double optimo) {
		try {
			String fecha = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())+"T"+new SimpleDateFormat("HH_mm_ss").format(Calendar.getInstance().getTime());
			for (int i = 0; i < solucion.length; i++) {
				URL url = new URL("http://192.168.0.9:80/ClashAPI/api/soluciones/guardarNodo/"+(i+1)+"/"+solucion[i]+"/"+instancia+"/"+problema+"/"+optimo+"/"+fecha+"/");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				String output;
				while ((output = br.readLine()) != null) {
					if (output.equals("0")) {
						System.out.println("No guardado nodo "+i);
					}
				}

				conn.disconnect();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void guardarTourAPI(int[] solucion, String instancia, String problema, double optimo) {
		try {
			String fecha = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())+"T"+new SimpleDateFormat("HH_mm_ss").format(Calendar.getInstance().getTime());
			String tour = "[";
			for (int i = 0; i < solucion.length; i++) {
				if(i==solucion.length-1) {
					tour+=solucion[i];
				}else {
					tour+=solucion[i]+"_";
				}				
			}
			tour+="]";
			
			URL url = new URL("http://192.168.0.9:80/ClashAPI/api/tours/guardarTour/"+tour+"/"+instancia+"/"+problema+"/"+optimo+"/"+fecha+"/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				if (output.equals("0")) {
					System.out.println("Error al guardar el tour");
				}
			}

			conn.disconnect();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
