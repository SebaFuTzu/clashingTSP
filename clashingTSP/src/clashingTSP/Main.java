package clashingTSP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.jfree.ui.RefineryUtilities;

public class Main {
	public static final String SEPARATOR=";";
	public static final String QUOTE="\"";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//String Fayudantia1 = "F-ayudantia1.txt";
			//String Dayudantia1 = "D-ayudantia1.txt";
			int cantidadSwappings = 2;
			String Fayudantia1 = "matrizDistancias52.txt";
			String Dayudantia1 = "matrizDistancias52.txt";
			String path = System.getProperty("user.dir")+"\\datos\\";

			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			DateFormat dfLog = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			if(args[0].equalsIgnoreCase("-clash")) {
				//Clashing TSP
				//matrices distancias
				String distancias = "matrizDistancias9152.txt";
				Stream<String> matrizF = null;
				Stream<String> matrizD = Files.lines(Paths.get(path+distancias));				
				int[][] f = null;
				int[][] d = convertirString(matrizD);
				
				//solución a partir de archivo de texto
				Swap swap = new Swap(f, d);
				String solucionPath = "ar9152distancia837479.txt";
				Stream<String> solucionMatriz = Files.lines(Paths.get(path+solucionPath));
				int[] solucionInicial = convertirARepresentacion(solucionMatriz);
				swap.toStringSolcuionInicial(solucionInicial);
				
				//calculo de tiempo que tarda en calcular la distancia
				long startTime = System.nanoTime();// Contador de tiempo
				
				double distancia = swap.evaluarDistanciaSolucionTSPLIB(solucionInicial);
				
				long endTime = System.nanoTime();
				long totalTime = (endTime - startTime) / 1000000;
				System.out.println("tiempo ejecución: " + totalTime + " milisegundos");
				
				System.out.println("Distancia solución inicial: "+distancia);
				
			}else if(args[0].equalsIgnoreCase("-clash2")) {
				//Clashing TSP
				//matrices distancias
				String distancias = "matrizDistancias9152.txt";
				Stream<String> matrizF = null;
				Stream<String> matrizD = Files.lines(Paths.get(path+distancias));				
				int[][] f = null;
				int[][] d = convertirString(matrizD);
				
				//solución a partir de archivo de texto
				Swap swap = new Swap(f, d);
				/**String solucionPath = "ar9152distancia837479.txt";//"ar9152.tour";
				Stream<String> solucionMatriz = Files.lines(Paths.get(path+solucionPath));
				int[] solucionInicial = convertirARepresentacion(solucionMatriz);
				*/
				
				int[] solucionInicial = swap.generarSolucionInicialTSP(swap.getMatrizD());
				
				ArrayList<Double> costos = AlgoritmoModulador.Modulador2(solucionInicial, swap);
				/**swap.toStringSolcuionInicial(solucionInicial);*/				
				
				//final XYPlot demo = new XYPlot("Gráfico optimización Algoritmo Modulador", "distancia", costos, 6156, "distancia");
				String fileName = df.format(new java.util.Date());
				XYPlotSpeed plot = new XYPlotSpeed();
				plot.Plot("Gráfico optimización Modulador", "Costo sin memoria", costos, 837377, "Modulador_"+fileName+ String.format("%04d", 1), "");
				
				swap.guardarTourAPI(solucionInicial, "ar9152", "ArgentinaTSP", costos.get(costos.size()-1));
				
				System.out.println("Distancia solución inicial: "+costos.get(costos.size()-1));
				
			}else if(args[0].equalsIgnoreCase("-opti")) {
				//Código para presentación optimización
				//Sebastián Aliaga - Natalia Morales
				cantidadSwappings = Integer.parseInt(args[1]);

				Stream<String> matrizF = Files.lines(Paths.get(path+Fayudantia1));
				Stream<String> matrizD = Files.lines(Paths.get(path+Dayudantia1));
				//filasD.forEach(System.out::println);
				int[][] f = convertirString(matrizF);
				int[][] d = convertirString(matrizD);
				
				Swap swap = new Swap(f, d);
				int[] solucionInicial = swap.generarSolucionInicialTSP(swap.getMatrizD());
				swap.toStringSolcuionInicial(solucionInicial);
				
				double distancia = swap.evaluarDistanciaSolucionTSP(solucionInicial);
				System.out.println("Distancia solución inicial: "+distancia);
				
			}else if(args[0].equalsIgnoreCase("-path")) {  // si parte con -path asume que realizaras una prueba unitaria
				path = args[1];
				Fayudantia1 = args[2];
				Dayudantia1 = args[3];
				cantidadSwappings = Integer.parseInt(args[4]);

				Stream<String> matrizF = Files.lines(Paths.get(path+Fayudantia1));
				Stream<String> matrizD = Files.lines(Paths.get(path+Dayudantia1));
				//filasD.forEach(System.out::println);
				int[][] f = convertirString(matrizF);
				int[][] d = convertirString(matrizD);
				/*for(int[] i: m){
					for(int j: i){
						System.out.println(j);
					}
				}*/					

				Swap swap = new Swap(f, d);
				int[] solucionInicial = swap.generarSolcuionInicial(swap.getMatrizD());
				swap.toStringSolcuionInicial(solucionInicial);


				if(args[5].equals("ejercicio1")) {
					System.out.println("######## Ejercicio 1 ########");
					int tamañoVecindad = (int)swap.calcularTamañoVecindad(f, cantidadSwappings);
					swap.ejercicio1(solucionInicial, cantidadSwappings, tamañoVecindad, 50);
				}else if(args[5].equals("ejercicio2")) {
					System.out.println("######## Ejercicio 2 ########");
					int tamañoVecindad = (int)swap.calcularTamañoVecindad(f, cantidadSwappings);
					ArrayList<Double> costos = swap.ejercicio2(solucionInicial, cantidadSwappings, tamañoVecindad, 30);

					//plotting
					final XYPlot demo = new XYPlot("Gráfico optimización con operador swap "+args[5], "Costo", costos);

					//demo.pack();
					//RefineryUtilities.centerFrameOnScreen(demo);
					//demo.setVisible(true);
				}else if(args[5].equals("ejercicio3")) {
					System.out.println("######## Ejercicio 3 ########");
					int tamañoVecindad = (int)swap.calcularTamañoVecindad(f, cantidadSwappings);
					ArrayList<Double> costos = swap.ejercicio2(solucionInicial, cantidadSwappings, tamañoVecindad, 100);

					//plotting
					final XYPlot demo = new XYPlot("Gráfico optimización con operador swap "+args[5], "Costo", costos);
					//demo.pack();
					//RefineryUtilities.centerFrameOnScreen(demo);
					//demo.setVisible(true);
				}else if(args[5].equals("SA")) {
					System.out.println("######## Simulated Annealing ########");

					double temperaturaMinima = 0;
					double temperaturaMaxima = 350;
					double probabilidadAceptar = 0.99;
					int funcionEnfriamiento = SimulatedAnnealing.FUNCION_ENFRIAMIENTO_GEOMETRICO;
					double decrecimiento = SimulatedAnnealing.RAZON_DECRECIMIENTO_ARITMETICO;
					double ponderadorVecindad = 1;


					if ( args.length > 6)
						temperaturaMinima = Double.parseDouble(args[6]);

					if ( args.length > 7)
						temperaturaMaxima = Double.parseDouble(args[7]);

					if ( args.length > 8)
						probabilidadAceptar = Double.parseDouble(args[8]);

					if ( args.length > 9 )
						funcionEnfriamiento = Integer.parseInt(args[9]);

					if ( args.length > 10 ) 
						decrecimiento = Double.parseDouble(args[10]);
					else { 
						switch (funcionEnfriamiento) {
						case SimulatedAnnealing.FUNCION_ENFRIAMIENTO_ARITMETICO:
							decrecimiento = SimulatedAnnealing.RAZON_DECRECIMIENTO_ARITMETICO;
							break;
						case SimulatedAnnealing.FUNCION_ENFRIAMIENTO_GEOMETRICO:
							decrecimiento = SimulatedAnnealing.PORCENTAJE_RAZON_DECRECIMIENTO_GEOMETRICO;
							break;
						case SimulatedAnnealing.FUNCION_ENFRIAMIENTO_LOGARITMICO:
							decrecimiento = SimulatedAnnealing.CONSTANTE_DECRECIMIENTO_LOGARITMICO;
							break;
						}
					}

					if ( args.length > 11 ) 
						ponderadorVecindad = Double.parseDouble(args[11]);


					ArrayList<Costos> costos = SimulatedAnnealing.simulatedAnnealing(solucionInicial, temperaturaMinima, temperaturaMaxima, cantidadSwappings, 
							funcionEnfriamiento, probabilidadAceptar, swap, decrecimiento, ponderadorVecindad);

					//plotting
					final XYPlot demo = new XYPlot("Gráfico optimización Simulated Annealing", "Costo sin memoria", "Costo con memoria", costos);
					//demo.pack();
					//RefineryUtilities.centerFrameOnScreen(demo);
					//demo.setVisible(true);
				}else if(args[5].equals("TABU")) {
					System.out.println("######## Tabu search ########");
					int duracionTabuList = 22;
					int iteraciones = 20;
					int profundidadIntensificacion = 18;
					boolean intensificacion = true;
					boolean diversificacion = true;
					int iteracionesIntensificacion = 500;
					int cantidadDiversificacion = 1;

					if ( args.length > 6)
						duracionTabuList = Integer.parseInt(args[6]);

					if ( args.length > 7)
						iteraciones = Integer.parseInt(args[7]);

					if ( args.length > 8)
						profundidadIntensificacion = Integer.parseInt(args[8]);

					if ( args.length > 9)
						intensificacion = Boolean.parseBoolean(args[9]);

					if ( args.length > 7)
						diversificacion = Boolean.parseBoolean(args[10]);
					
					if ( args.length > 7)
						cantidadDiversificacion = Integer.parseInt(args[11]);

					ArrayList<Double> costos = TabuSearch.TabuSearch(solucionInicial, swap, duracionTabuList, iteraciones, profundidadIntensificacion, intensificacion, diversificacion, iteracionesIntensificacion, cantidadDiversificacion);

					//plotting
					final XYPlot demo = new XYPlot("Gráfico optimización Tabu Search", "Costo", costos, 6156, "costo"); //Yo
					//demo.pack();
					//RefineryUtilities.centerFrameOnScreen(demo);
					//demo.setVisible(true);
				}else if(args[5].equals("AG")) {
					System.out.println("######## Algoritmo Genético ########");
					int tamanoPoblacion = 128;
					int criterioParada = 0;
					int valorDetencion = 5000;
					double porcentajeCorteMenor = 0.25;
					double porcentajeCorteMayor = 0.75;
					boolean incluirMemoriaPrevia = false;
					int numeroDeRestarts = -1;
					double porcentajeAGuardarMejoresSolucionesEnMemoria = 0.1;
					double ponderadorCantidadDePadres = 0.25;
					int maximoIteracionesAdaptativo = 5000;

					if ( args.length > 6)
						tamanoPoblacion = Integer.parseInt(args[6]);

					if ( args.length > 7)
						criterioParada = Integer.parseInt(args[7]);

					if ( args.length > 8)
						valorDetencion = Integer.parseInt(args[8]);

					if ( args.length > 9)
						porcentajeCorteMenor = Double.parseDouble(args[9]);

					if ( args.length > 9)
						porcentajeCorteMayor = Double.parseDouble(args[10]);

					if ( args.length > 9)
						incluirMemoriaPrevia = Boolean.parseBoolean(args[11]);

					if ( args.length > 9)
						numeroDeRestarts = Integer.parseInt(args[12]);

					if ( args.length > 9)
						porcentajeAGuardarMejoresSolucionesEnMemoria = Double.parseDouble(args[13]);

					if ( args.length > 9)
						ponderadorCantidadDePadres = Double.parseDouble(args[14]);

					if ( args.length > 9)
						maximoIteracionesAdaptativo = Integer.parseInt(args[15]);


					ArrayList<Double> costos = AlgoritmoGenetico.algoritmoGenetico(solucionInicial, cantidadSwappings, tamanoPoblacion, criterioParada, valorDetencion, swap, porcentajeCorteMenor, porcentajeCorteMayor, incluirMemoriaPrevia, numeroDeRestarts, porcentajeAGuardarMejoresSolucionesEnMemoria, ponderadorCantidadDePadres, maximoIteracionesAdaptativo);

					//plotting
					final XYPlotVista demo = new XYPlotVista("Gráfico optimización Algoritmos Genéticos", "Costo", costos); //Yo
					demo.pack();
					RefineryUtilities.centerFrameOnScreen(demo);
					demo.setVisible(true);
				}else if(args[5].equals("AH")) {
					System.out.println("######## Algoritmo Híbrido ########");
					//Parámetros parte algoritmo genético
					int tamanoPoblacion = 20;
					int criterioParada = 0;
					int valorDetencion = 3;
					double porcentajeCorteMenor = 0.25;
					double porcentajeCorteMayor = 0.75;
					boolean incluirMemoriaPrevia = false;
					int numeroDeRestarts = -1;
					double porcentajeAGuardarMejoresSolucionesEnMemoria = 0.1;
					double ponderadorCantidadDePadres = 0.25;
					int maximoIteracionesAdaptativo = 5000;

					//Parámetros Tabu search
					int duracionTabuList = 10;
					int iteraciones = 20;
					int profundidadIntensificacion = 10;
					boolean intensificacion = false;
					boolean diversificacion = true;		
					int cantidadDiversificacion = 1;

					if ( args.length > 6)
						tamanoPoblacion = Integer.parseInt(args[6]);

					if ( args.length > 7)
						criterioParada = Integer.parseInt(args[7]);

					if ( args.length > 8)
						valorDetencion = Integer.parseInt(args[8]);

					if ( args.length > 9)
						porcentajeCorteMenor = Double.parseDouble(args[9]);

					if ( args.length > 9)
						porcentajeCorteMayor = Double.parseDouble(args[10]);

					if ( args.length > 9)
						incluirMemoriaPrevia = Boolean.parseBoolean(args[11]);

					if ( args.length > 9)
						numeroDeRestarts = Integer.parseInt(args[12]);

					if ( args.length > 9)
						porcentajeAGuardarMejoresSolucionesEnMemoria = Double.parseDouble(args[13]);

					if ( args.length > 9)
						ponderadorCantidadDePadres = Double.parseDouble(args[14]);

					if ( args.length > 9)
						maximoIteracionesAdaptativo = Integer.parseInt(args[15]);

					if ( args.length > 6)
						duracionTabuList = Integer.parseInt(args[16]);

					if ( args.length > 7)
						iteraciones = Integer.parseInt(args[17]);

					if ( args.length > 8)
						profundidadIntensificacion = Integer.parseInt(args[18]);

					if ( args.length > 9)
						intensificacion = Boolean.parseBoolean(args[19]);

					if ( args.length > 7)
						diversificacion = Boolean.parseBoolean(args[20]);
					
					if ( args.length > 7)
						cantidadDiversificacion = Integer.parseInt(args[21]);


					ArrayList<Double> costos = AlgoritmoHibrido.algoritmoHibrido(solucionInicial, cantidadSwappings, tamanoPoblacion, criterioParada, valorDetencion, swap, porcentajeCorteMenor, porcentajeCorteMayor, incluirMemoriaPrevia, numeroDeRestarts, porcentajeAGuardarMejoresSolucionesEnMemoria, ponderadorCantidadDePadres, maximoIteracionesAdaptativo, duracionTabuList, iteraciones, profundidadIntensificacion, intensificacion, diversificacion, cantidadDiversificacion);

					//plotting
					final XYPlotVista demo = new XYPlotVista("Gráfico optimización Algoritmos Genéticos", "Costo", costos); //Yo
					demo.pack();
					RefineryUtilities.centerFrameOnScreen(demo);
					demo.setVisible(true);
				}
			}else if(args[0].equalsIgnoreCase("-dataset")){ //si parto con -dataset carga el dataste para hacer varias pruebas programadas
				BufferedReader br = null;
				try {
					//dejo un espacio para cargar el dataset
					//br =new BufferedReader(new FileReader("dataset-SimulatedAnnealing.csv"));
					//String Fayudantia1 = path+"F"+datos;
					//String Dayudantia1 = path+"D"+datos;
					br =new BufferedReader(new FileReader(args[1]));
					//separo la linea por el separador
					String line = br.readLine();

					String [] fields = line.split(SEPARATOR);
					//remuevo basura del codigo
					fields = removeTrailingQuotes(fields);
					//line = br.readLine(); //activar si usamos cabecera ya q salta la primera linea
					//escribir archivo csv

					//creo la matriz afuera ya que si uso la misma matriz solo necesito actualizar solucion inicial y no todo
					Stream<String> matrizF;
					Stream<String> matrizD;
					path="";
					Fayudantia1="";
					Dayudantia1="";
					Swap swap;
					int[] solucionInicial;
					int[][] f= {{0}};
					int[][] d= {{0}};
					int cantidadExperimentos = 1;
					int mejorMaximoConocido = 6155;
					String fileName = df.format(new java.util.Date())  ; //+ ;
					FileWriter fileWriter = new FileWriter(args[2] + fileName  +  ".log" );
					//FileWriter fichero = null;
					//PrintWriter pw = null;
					//fichero = new FileWriter(args[2]  +  ".log");
					//pw = new PrintWriter(fichero);
					XYPlotSpeed plot = new XYPlotSpeed();
					int cantLines = 0;
					while (null!=line) {  //voy leyendo linea a linea
						//cargo los datos, si son diferentes actualizo todo
						cantLines++;
						if(path!=fields[1] || Fayudantia1!=fields[2] || Dayudantia1!=fields[3])
						{
							path = fields[1];
							Fayudantia1 = fields[2];
							Dayudantia1 = fields[3];
							cantidadSwappings = Integer.parseInt(fields[4]);

							matrizF = Files.lines(Paths.get(path+Fayudantia1));
							matrizD = Files.lines(Paths.get(path+Dayudantia1));

							f = convertirString(matrizF);
							d = convertirString(matrizD);		

						}
						else
						{	
							//Se quitan estas lineas porque cada experimento hace sus propios SWAP y genera las soluciones
							//ya tengo cargado los datos, falta crear nueva solucion inicial para proxima prueba
							//swap = new Swap(f, d);
							//solucionInicial = swap.generarSolcuionInicial(swap.getMatrizD());
							//swap.toStringSolcuionInicial(solucionInicial);
						}
						if(fields[5].equals("SA")) {
							//System.out.println("######## Simulated Annealing ########");

							double temperaturaMinima = 0;
							double temperaturaMaxima = 350;
							double probabilidadAceptar = 0.99;
							int funcionEnfriamiento = SimulatedAnnealing.FUNCION_ENFRIAMIENTO_GEOMETRICO;
							double decrecimiento = SimulatedAnnealing.RAZON_DECRECIMIENTO_ARITMETICO;
							double ponderadorVecindad = 1;

							//#¿NOMBRE?;./datos/;F64.txt;D64.txt;2;SA;1;1000;0.95;1;0.3;2;100;116
							temperaturaMinima = Double.parseDouble(fields[6]);

							temperaturaMaxima = Double.parseDouble(fields[7]);

							probabilidadAceptar = Double.parseDouble(fields[8]);

							funcionEnfriamiento = Integer.parseInt(fields[9]);

							decrecimiento = Double.parseDouble(fields[10]);

							ponderadorVecindad = Double.parseDouble(fields[11]);


							//plotting
							//final XYPlot demo = new XYPlot("Gráfico optimización Simulated Annealing", "Costo sin memoria", "Costo con memoria", costos);


							cantidadExperimentos = Integer.parseInt(fields[12]);
							mejorMaximoConocido = Integer.parseInt(fields[13]);
							//String fileName = df.format(new java.util.Date())  ; //+ ;

							//fileWriter = new FileWriter(args[2] + fileName  +  ".log" );
							System.out.println( dfLog.format(new java.util.Date()) + " Linea de experimento [" + cantLines + "]");

							for (int i = 0; i < cantidadExperimentos; i++) { 

								long startTime = System.nanoTime();// Contador de tiempo

								// tiempo de ejecución
								System.out.println(dfLog.format(new java.util.Date()) + " Experimento [" + i + "]");

								swap = new Swap(f, d);
								solucionInicial = swap.generarSolcuionInicial(swap.getMatrizD());

								swap.toStringSolcuionInicial(solucionInicial);

								ArrayList<Costos> costos = SimulatedAnnealing.simulatedAnnealing(solucionInicial, temperaturaMinima, temperaturaMaxima, cantidadSwappings, 
										funcionEnfriamiento, probabilidadAceptar, swap, decrecimiento, ponderadorVecindad);


								//plotting
								//final XYPlot demo = new XYPlot("Gráfico optimización Simulated Annealing", "Costo sin memoria", "Costo con memoria", costos, mejorMaximoConocido, fileName + "_" + String.format("%04d", i+1)); //Yo

								plot.Plot("Gráfico optimización Simulated Annealing", "Costo sin memoria", "Costo con memoria", costos, mejorMaximoConocido, fileName + "_" + String.format("%04d", i+1)); 

								//demo.pack();
								//System.out.println("Mejor solucion"+minimo(costos));
								//escribo solucion en el log
								//fileWriter.append(String.valueOf(minimo(costos)));
								//fileWriter.append(QUOTE);
								//RefineryUtilities.centerFrameOnScreen(demo);
								//demo.setVisible(true);}
								long endTime = System.nanoTime();
								long totalTime = (endTime - startTime) / 1000000;
								System.out.println("tiempo ejecución: " + totalTime + " milisegundos");
								fileWriter.write(fileName);
								fileWriter.write(SEPARATOR);
								fileWriter.write(String.valueOf(minimoCostos(costos)));
								fileWriter.write(SEPARATOR);
								fileWriter.write(String.valueOf(totalTime));
								fileWriter.write(System.lineSeparator());

							}
						}else if(fields[5].equals("TABU")) {
							//System.out.println("######## Tabu search ########");
							int duracionTabuList = Integer.parseInt(fields[6]);
							int iteraciones = Integer.parseInt(fields[7]);
							int profundidadIntensificacion = Integer.parseInt(fields[8]);
							boolean intensificacion = Boolean.parseBoolean(fields[9]);
							boolean diversificacion = Boolean.parseBoolean(fields[10]);
							int iteracionesIntensificacion = Integer.parseInt(fields[11]);
							int cantidadDiversificacion = Integer.parseInt(fields[12]);
							
							cantidadExperimentos = Integer.parseInt(fields[13]);
							mejorMaximoConocido = Integer.parseInt(fields[14]);
							String nombreInstancia = fields[15];
							//String fileName = df.format(new java.util.Date())  ; //+ ;

							System.out.println(dfLog.format(new java.util.Date()) + " Linea de experimento [" + cantLines + "]");

							for (int i = 0; i < cantidadExperimentos; i++) { 

								long startTime = System.nanoTime();// Contador de tiempo

								System.out.println(dfLog.format(new java.util.Date()) + " Experimento [" + i + "]");

								swap = new Swap(f, d);
								solucionInicial = swap.generarSolcuionInicial(swap.getMatrizD());

								swap.toStringSolcuionInicial(solucionInicial);

								ArrayList<Double> costos = TabuSearch.TabuSearch(solucionInicial, swap, duracionTabuList, iteraciones, profundidadIntensificacion, intensificacion, diversificacion, iteracionesIntensificacion, cantidadDiversificacion);

								fileName = df.format(new java.util.Date())  ; //+ ;
								//plotting
								//final XYPlot demo = new XYPlot("Gráfico optimización Tabu Search", "Costo", costos, mejorMaximoConocido, fileName + "_" + String.format("%04d", i+1)); //Yo
								plot.Plot(nombreInstancia, "Distancia", costos, mejorMaximoConocido, fileName + "_" + String.format("%04d", i+1), "Iteraciones");



								System.out.println("Mejor solucion: "+minimo(costos));

								long endTime = System.nanoTime();
								long totalTime = (endTime - startTime) / 1000000;
								System.out.println("tiempo ejecución: " + totalTime + " milisegundos");


								fileWriter.write(fileName);
								fileWriter.write(SEPARATOR);
								fileWriter.write(String.valueOf(minimo(costos)));
								fileWriter.write(SEPARATOR);
								fileWriter.write(String.valueOf(totalTime));								
								fileWriter.write(System.lineSeparator());

							}
						}else if(fields[5].equals("AG")) {
							//System.out.println("######## Tabu search ########");
							//#¿NOMBRE?;./datos/;F64.txt;D64.txt;2;AG;100;0;100;0.25;0.75;false;-1;0.1;0.25;5000;1;116;
							int tamanoPoblacion = 100;
							int criterioParada = 0;
							int valorDetencion = 100;
							double porcentajeCorteMenor = 0.25;
							double porcentajeCorteMayor = 0.75;
							boolean incluirMemoriaPrevia = false;
							int numeroDeRestarts = -1;
							double porcentajeAGuardarMejoresSolucionesEnMemoria = 0.1;

							double ponderadorCantidadDePadres = 0.25;
							int maximoIteracionesAdaptativo = 5000;

							tamanoPoblacion = Integer.parseInt(fields[6]);

							criterioParada = Integer.parseInt(fields[7]);

							valorDetencion = Integer.parseInt(fields[8]);

							porcentajeCorteMenor = Double.parseDouble(fields[9]);

							porcentajeCorteMayor = Double.parseDouble(fields[10]);

							incluirMemoriaPrevia = Boolean.parseBoolean(fields[11]);

							numeroDeRestarts = Integer.parseInt(fields[12]);

							porcentajeAGuardarMejoresSolucionesEnMemoria = Double.parseDouble(fields[13]);

							ponderadorCantidadDePadres = Double.parseDouble(fields[14]);

							maximoIteracionesAdaptativo = Integer.parseInt(fields[15]);


							cantidadExperimentos = Integer.parseInt(fields[16]);
							mejorMaximoConocido = Integer.parseInt(fields[17]);


							//ArrayList<Double> costos = AlgoritmoGenetico.algoritmoGenetico(solucionInicial, cantidadSwappings, tamanoPoblacion, criterioParada, valorDetencion, swap, porcentajeCorteMenor, porcentajeCorteMayor, incluirMemoriaPrevia, numeroDeRestarts, porcentajeAGuardarMejoresSolucionesEnMemoria);

							//plotting
							//final XYPlotVista demo = new XYPlotVista("Gráfico optimización Algoritmos Genéticos", "Costo", costos); //Yo
							System.out.println(dfLog.format(new java.util.Date()) + " Linea de experimento [" + cantLines + "]");

							for (int i = 0; i < cantidadExperimentos; i++) { 

								long startTime = System.nanoTime();// Contador de tiempo

								System.out.println(dfLog.format(new java.util.Date()) + " Experimento [" + i + "]");

								swap = new Swap(f, d);
								solucionInicial = swap.generarSolcuionInicial(swap.getMatrizD());

								swap.toStringSolcuionInicial(solucionInicial);

								ArrayList<Double> costos = AlgoritmoGenetico.algoritmoGenetico(solucionInicial, cantidadSwappings, tamanoPoblacion, criterioParada, valorDetencion, swap, porcentajeCorteMenor, porcentajeCorteMayor, incluirMemoriaPrevia, numeroDeRestarts, porcentajeAGuardarMejoresSolucionesEnMemoria, ponderadorCantidadDePadres, maximoIteracionesAdaptativo);

								//plotting
								final XYPlotVista demo = new XYPlotVista("Gráfico optimización Algoritmos Genéticos", "Costo", costos); //Yo

								fileName = df.format(new java.util.Date())  ; //+ ;
								//plotting
								//final XYPlot demo = new XYPlot("Gráfico optimización Tabu Search", "Costo", costos, mejorMaximoConocido, fileName + "_" + String.format("%04d", i+1)); //Yo
								plot.Plot("Gráfico optimización Algoritmo Genético", "Costo", costos, mejorMaximoConocido, fileName + "_" + String.format("%04d", i+1), "Generaciones");



								System.out.println("Mejor solucion: "+minimo(costos));

								long endTime = System.nanoTime();
								long totalTime = (endTime - startTime) / 1000000;
								System.out.println("tiempo ejecución: " + totalTime + " milisegundos");


								fileWriter.write(fileName);
								fileWriter.write(SEPARATOR);
								fileWriter.write(String.valueOf(minimo(costos)));
								fileWriter.write(SEPARATOR);
								fileWriter.write(String.valueOf(totalTime));								
								fileWriter.write(System.lineSeparator());

							}
						} else if(fields[5].equals("AH")) {
							System.out.println("######## Algoritmo Híbrido ########");
							//Parámetros parte algoritmo genético
							int tamanoPoblacion = 20;
							int criterioParada = 0;
							int valorDetencion = 3;
							double porcentajeCorteMenor = 0.25;
							double porcentajeCorteMayor = 0.75;
							boolean incluirMemoriaPrevia = false;
							int numeroDeRestarts = -1;
							double porcentajeAGuardarMejoresSolucionesEnMemoria = 0.1;
							double ponderadorCantidadDePadres = 0.25;
							int maximoIteracionesAdaptativo = 5000;

							//Parámetros Tabu search
							int duracionTabuList = 10;
							int iteraciones = 20;
							int profundidadIntensificacion = 10;
							boolean intensificacion = false;
							boolean diversificacion = true;					
							int cantidadDiversificacion = 1;

							tamanoPoblacion = Integer.parseInt(fields[6]);

							criterioParada = Integer.parseInt(fields[7]);

							valorDetencion = Integer.parseInt(fields[8]);

							porcentajeCorteMenor = Double.parseDouble(fields[9]);

							porcentajeCorteMayor = Double.parseDouble(fields[10]);

							incluirMemoriaPrevia = Boolean.parseBoolean(fields[11]);

							numeroDeRestarts = Integer.parseInt(fields[12]);

							porcentajeAGuardarMejoresSolucionesEnMemoria = Double.parseDouble(fields[13]);

							ponderadorCantidadDePadres = Double.parseDouble(fields[14]);

							maximoIteracionesAdaptativo = Integer.parseInt(fields[15]);

							duracionTabuList = Integer.parseInt(fields[16]);

							iteraciones = Integer.parseInt(fields[17]);

							profundidadIntensificacion = Integer.parseInt(fields[18]);

							intensificacion = Boolean.parseBoolean(fields[19]);

							diversificacion = Boolean.parseBoolean(fields[20]);
							
							cantidadDiversificacion = Integer.parseInt(fields[21]);

							cantidadExperimentos = Integer.parseInt(fields[22]);
							mejorMaximoConocido = Integer.parseInt(fields[23]);

							//ArrayList<Double> costos = AlgoritmoHibrido.algoritmoHibrido(solucionInicial, cantidadSwappings, tamanoPoblacion, criterioParada, valorDetencion, swap, porcentajeCorteMenor, porcentajeCorteMayor, incluirMemoriaPrevia, numeroDeRestarts, porcentajeAGuardarMejoresSolucionesEnMemoria, ponderadorCantidadDePadres, maximoIteracionesAdaptativo, duracionTabuList, iteraciones, profundidadIntensificacion, intensificacion, diversificacion);

							//plotting
							//final XYPlotVista demo = new XYPlotVista("Gráfico optimización Algoritmos Genéticos Hibrido", "Costo", costos); //Yo
							//demo.pack();
							//RefineryUtilities.centerFrameOnScreen(demo);
							//demo.setVisible(true);
							//plotting
							//final XYPlotVista demo = new XYPlotVista("Gráfico optimización Algoritmos Genéticos", "Costo", costos); //Yo
							System.out.println(dfLog.format(new java.util.Date()) + " Linea de experimento [" + cantLines + "]");

							for (int i = 0; i < cantidadExperimentos; i++) { 

								long startTime = System.nanoTime();// Contador de tiempo

								System.out.println(dfLog.format(new java.util.Date()) + " Experimento [" + i + "]");

								swap = new Swap(f, d);
								solucionInicial = swap.generarSolcuionInicial(swap.getMatrizD());

								swap.toStringSolcuionInicial(solucionInicial);

								ArrayList<Double> costos = AlgoritmoHibrido.algoritmoHibrido(solucionInicial, cantidadSwappings, tamanoPoblacion, criterioParada, valorDetencion, swap, porcentajeCorteMenor, porcentajeCorteMayor, incluirMemoriaPrevia, numeroDeRestarts, porcentajeAGuardarMejoresSolucionesEnMemoria, ponderadorCantidadDePadres, maximoIteracionesAdaptativo, duracionTabuList, iteraciones, profundidadIntensificacion, intensificacion, diversificacion, cantidadDiversificacion);

								//plotting
								final XYPlotVista demo = new XYPlotVista("Gráfico optimización Algoritmos Genéticos", "Costo", costos); //Yo

								fileName = df.format(new java.util.Date())  ; //+ ;
								//plotting
								//final XYPlot demo = new XYPlot("Gráfico optimización Tabu Search", "Costo", costos, mejorMaximoConocido, fileName + "_" + String.format("%04d", i+1)); //Yo
								plot.Plot("Gráfico optimización Algoritmo Híbrido", "Costo", costos, mejorMaximoConocido, fileName + "_" + String.format("%04d", i+1), "Generaciones");



								System.out.println("Mejor solucion: "+minimo(costos));

								long endTime = System.nanoTime();
								long totalTime = (endTime - startTime) / 1000000;
								System.out.println("tiempo ejecución: " + totalTime + " milisegundos");


								fileWriter.write(fileName);
								fileWriter.write(SEPARATOR);
								fileWriter.write(String.valueOf(minimo(costos)));
								fileWriter.write(SEPARATOR);
								fileWriter.write(String.valueOf(totalTime));								
								fileWriter.write(System.lineSeparator());

							}
						}else if(fields[5].equals("AC")) {
							//System.out.println("######## Tabu search ########");
							//#¿NOMBRE?;./datos/;F64.txt;D64.txt;2;AG;100;0;100;0.25;0.75;false;-1;0.1;0.25;5000;1;116;
							int tamanoPoblacion = 100;
							int criterioParada = 0;
							int valorDetencion = 100;
							double porcentajeCorteMenor = 0.25;
							double porcentajeCorteMayor = 0.75;
							boolean incluirMemoriaPrevia = false;
							int numeroDeRestarts = -1;
							double porcentajeAGuardarMejoresSolucionesEnMemoria = 0.1;

							double ponderadorCantidadDePadres = 0.25;
							int maximoIteracionesAdaptativo = 5000;
							boolean isClash = false;
							String nombreInstancia = "";

							tamanoPoblacion = Integer.parseInt(fields[6]);

							criterioParada = Integer.parseInt(fields[7]);

							valorDetencion = Integer.parseInt(fields[8]);

							porcentajeCorteMenor = Double.parseDouble(fields[9]);

							porcentajeCorteMayor = Double.parseDouble(fields[10]);

							incluirMemoriaPrevia = Boolean.parseBoolean(fields[11]);

							numeroDeRestarts = Integer.parseInt(fields[12]);

							porcentajeAGuardarMejoresSolucionesEnMemoria = Double.parseDouble(fields[13]);

							ponderadorCantidadDePadres = Double.parseDouble(fields[14]);

							maximoIteracionesAdaptativo = Integer.parseInt(fields[15]);
								
							isClash = Boolean.parseBoolean(fields[16]);

							cantidadExperimentos = Integer.parseInt(fields[17]);
							mejorMaximoConocido = Integer.parseInt(fields[18]);
							nombreInstancia = fields[19];

							//ArrayList<Double> costos = AlgoritmoGenetico.algoritmoGenetico(solucionInicial, cantidadSwappings, tamanoPoblacion, criterioParada, valorDetencion, swap, porcentajeCorteMenor, porcentajeCorteMayor, incluirMemoriaPrevia, numeroDeRestarts, porcentajeAGuardarMejoresSolucionesEnMemoria);

							//plotting
							//final XYPlotVista demo = new XYPlotVista("Gráfico optimización Algoritmos Genéticos", "Costo", costos); //Yo
							System.out.println(dfLog.format(new java.util.Date()) + " Linea de experimento [" + cantLines + "]");

							for (int i = 0; i < cantidadExperimentos; i++) { 

								long startTime = System.nanoTime();// Contador de tiempo

								System.out.println(dfLog.format(new java.util.Date()) + " Experimento [" + i + "]");

								swap = new Swap(f, d);
								solucionInicial = swap.generarSolcuionInicial(swap.getMatrizD());

								swap.toStringSolcuionInicial(solucionInicial);

								ArrayList<Double> costos = AlgoritmoClash.algoritmoClash(solucionInicial, cantidadSwappings, tamanoPoblacion, criterioParada, valorDetencion, swap, porcentajeCorteMenor, porcentajeCorteMayor, incluirMemoriaPrevia, numeroDeRestarts, porcentajeAGuardarMejoresSolucionesEnMemoria, ponderadorCantidadDePadres, maximoIteracionesAdaptativo, isClash);

								//plotting
								final XYPlotVista demo = new XYPlotVista("Gráfico optimización Algoritmo Clash", "Costo", costos); //Yo

								fileName = df.format(new java.util.Date())  ; //+ ;
								//plotting
								//final XYPlot demo = new XYPlot("Gráfico optimización Tabu Search", "Costo", costos, mejorMaximoConocido, fileName + "_" + String.format("%04d", i+1)); //Yo
								plot.Plot(nombreInstancia, "Costo", costos, mejorMaximoConocido, fileName + "_" + String.format("%04d", i+1), "Generaciones");



								System.out.println("Mejor solucion: "+minimo(costos));

								long endTime = System.nanoTime();
								long totalTime = (endTime - startTime) / 1000000;
								System.out.println("tiempo ejecución: " + totalTime + " milisegundos");


								fileWriter.write(fileName);
								fileWriter.write(SEPARATOR);
								fileWriter.write(String.valueOf(minimo(costos)));
								fileWriter.write(SEPARATOR);
								fileWriter.write(String.valueOf(totalTime));								
								fileWriter.write(System.lineSeparator());

							}
						}
						//Leo la siguiente linea
						line = br.readLine();
						if (line!= null) { 
							fields = line.split(SEPARATOR);
							//remuevo basura del codigo
							fields = removeTrailingQuotes(fields);
						}

					}
					fileWriter.flush();
					fileWriter.close();
					//pw.close();

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (null!=br) {
						br.close();
					}

				}
			}else if(args[0].equalsIgnoreCase("-help"))	{
				System.out.println("Ejemplo de sintaxis:");
				System.out.println("java -jar ayudantia1metaheuristicas.jar [carga desde cmd] [path] [txt matriz F] [txt matriz distancias] [cantidad de swappings] [nombre ejercicio]");
				System.out.println("java -jar ayudantia1metaheuristicas.jar -path C: F64.txt D64.txt 2 ejercicio1");
			}		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static int[][] convertirString(Stream<String> filas){
		String[] fila = filas.toArray(String[]::new);
		int[][] matriz = new int[fila.length][fila.length];
		for(int i=0;i<fila.length;i++){
			String[] f = fila[i].split(" ");
			for(int j=0;j<f.length;j++){
				matriz[i][j]=Integer.parseInt(f[j]);
			}
		}
		return matriz;
	}


	private static String[] removeTrailingQuotes(String[] fields) {
		String result[] = new String[fields.length];
		for (int i=0;i<result.length;i++){
			result[i] = fields[i].replaceAll("^"+QUOTE, "").replaceAll(QUOTE+"$", "");
		}
		return result;
	}

	static double minimo(ArrayList<Double> costos)
	{
		double min=costos.get(0);
		for(int i=1;i<costos.size();i++) {
			if(min>costos.get(i))
			{
				min=costos.get(i);
			}
		}
		return min;
	}

	static double minimoCostos(ArrayList<Costos> costos)
	{
		double min=costos.get(0).getCostoMejorSolucion();
		for(int i=1;i<costos.size();i++) {
			if(min>costos.get(i).getCostoMejorSolucion())
			{
				min=costos.get(i).getCostoMejorSolucion();
			}
		}
		return min;
	}

	public static int[] convertirARepresentacion(Stream<String> filas){
		String[] fila = filas.toArray(String[]::new);
		int[] representacion = new int[fila.length];
		for(int i=0;i<fila.length;i++){
			representacion[i]=Integer.parseInt(fila[i]);
		}
		return representacion;
	}
}
