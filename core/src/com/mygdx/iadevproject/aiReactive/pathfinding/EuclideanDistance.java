package com.mygdx.iadevproject.aiReactive.pathfinding;

public class EuclideanDistance implements Distance {

	private static Distance uniqueInstance = new EuclideanDistance();
	
	// Constructor privado.
	private EuclideanDistance() {}
	
	// Método que devuelve la instancia única.
	public static Distance getUniqueInstance() {
		return uniqueInstance;
	}

	@Override
	public float[][] getMatrixOfDistances(int width, int height, int xSource, int ySource) {
		// Creamos la matriz que será devuelta.
		float[][] result = new float[width][height];
		// La posición del objeto origen se inicializa a 0.
		result[xSource][ySource] = 0.0f;
		// Recorremos la matriz para calcular el valor del resto de posiciones.
		for (int x = 0; x<width; x++) {
			for (int y = 0; y<height; y++) {
				result[x][y] = (float) Math.sqrt((xSource - x)*(xSource - x) + (ySource - y)*(ySource - y));
			}
		}	
		// Devolvemos el resultado.
		return result;
	}
	
	public static void main(String[] args) {
		Distance d = EuclideanDistance.getUniqueInstance();
		float[][] matriz = d.getMatrixOfDistances(20, 14, 0, 0);
		for (int y = 0; y<14; y++) {
			for (int x = 0; x<20; x++) {
				// Quitamos decimales para que se vea bonito por pantalla. En la matriz sí está el número completo con todos los decimales posibles.
				float numero = (float)((int)(matriz[x][y]*10))/10;
				if (matriz[x][y] < 10) {
					System.out.print(numero + "      ");
				} else {
					System.out.print(numero + "     ");
				}
				
			}
			System.out.println();
		}
	}
}
