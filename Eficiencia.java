import java.util.HashSet;
import java.util.Random;

public class Eficiencia {

    public static void main(String[] args) {
        // Generar un arreglo grande para notar la diferencia de tiempos
        int n = 100000;
        int[] numeros = new int[n];
        Random rand = new Random();
        for(int i=0; i<n; i++){
            numeros[i] = rand.nextInt(50000);
        }
        
        // Objetivo que obligue al peor caso (no existe o está al final)
        int objetivo = -1; 

        // --- SOLUCIÓN GENÉRICA (FUERZA BRUTA) ---
        long inicioFuerzaBruta = System.nanoTime();
        boolean resultado1 = solucionGenerica(numeros, objetivo);
        long finFuerzaBruta = System.nanoTime();
        
        // --- SOLUCIÓN OPTIMIZADA (HASHSET) ---
        long inicioOptimizada = System.nanoTime();
        boolean resultado2 = solucionOptimizada(numeros, objetivo);
        long finOptimizada = System.nanoTime();

        System.out.println("--- Resultados ---");
        System.out.println("Sol. Genérica Tiempo: " + (finFuerzaBruta - inicioFuerzaBruta) / 1e6 + " ms");
        System.out.println("Sol. Optimizada Tiempo: " + (finOptimizada - inicioOptimizada) / 1e6 + " ms");
    }

    /**
     * Solución Genérica: Compara todos los pares posibles usando dos bucles.
     * Complejidad Temporal: O(n^2)
     * Complejidad Espacial: O(1) 
     */
    public static boolean solucionGenerica(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] + arr[j] == target) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Solución Optimizada: Usa una tabla Hash para recordar los números vistos.
     * Complejidad Temporal: O(n) 
     * Complejidad Espacial: O(n) 
     */
    public static boolean solucionOptimizada(int[] arr, int target) {
        HashSet<Integer> vistos = new HashSet<>();
        for (int num : arr) {
            int complemento = target - num;
            if (vistos.contains(complemento)) {
                return true;
            }
            vistos.add(num);
        }
        return false;
    }
}