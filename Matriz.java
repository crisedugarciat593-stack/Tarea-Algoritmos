import java.util.Random;

public class Matriz {

    public static void main(String[] args) {
        int filas = 1000;
        int columnas = 1000;
        int[][] matriz = new int[filas][columnas];
        Random rand = new Random();

        // 1. Generar matriz de 1000x1000 con positivos y negativos
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = rand.nextInt(2001) - 1000; // Entre -1000 y 1000
            }
        }

        // Búsqueda de x y -x
        int x = 500;
        boolean encontradoX = busquedaSecuencial(matriz, x);
        boolean encontradoMenosX = busquedaSecuencial(matriz, -x);
        System.out.println("Encontrado x (" + x + "): " + encontradoX);
        System.out.println("Encontrado -x (" + (-x) + "): " + encontradoMenosX);

        // Aplanar matriz a 1D para los métodos de ordenamiento
        int[] arregloPlano = aplanarMatriz(matriz);

        // Clonamos el arreglo para probar Radix Sort
        int[] arrRadix = new int[arregloPlano.length];
        System.arraycopy(arregloPlano, 0, arrRadix, 0, arregloPlano.length);
        
        System.out.println("Iniciando Radix Sort...");
        long startTime = System.currentTimeMillis();
        radixSort(arrRadix);
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo Radix Sort: " + (endTime - startTime) + " ms");
    }

    // --- Métodos Auxiliares ---
    public static boolean busquedaSecuencial(int[][] matriz, int objetivo) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] == objetivo) return true;
            }
        }
        return false;
    }

    public static int[] aplanarMatriz(int[][] matriz) {
        int[] plano = new int[matriz.length * matriz[0].length];
        int idx = 0;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                plano[idx++] = matriz[i][j];
            }
        }
        return plano;
    }

    public static int obtenerMinimo(int[] arr) {
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }

    public static int obtenerMaximo(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    // --- Algoritmos de Ordenamiento Básicos ---

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; ++i) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] L = new int[n1];
        int[] R = new int[n2];
        for (int i = 0; i < n1; ++i) L[i] = arr[left + i];
        for (int j = 0; j < n2; ++j) R[j] = arr[mid + 1 + j];
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) arr[k++] = L[i++];
            else arr[k++] = R[j++];
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    public static void shellSort(int[] arr) {
        int n = arr.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i += 1) {
                int temp = arr[i];
                int j;
                for (j = i; j >= gap && arr[j - gap] > temp; j -= gap)
                    arr[j] = arr[j - gap];
                arr[j] = temp;
            }
        }
    }

    // --- Algoritmos Especiales (Adaptados para Negativos) ---

    public static void countingSort(int[] arr) {
        int min = obtenerMinimo(arr);
        int max = obtenerMaximo(arr);
        int range = max - min + 1;
        int[] count = new int[range];
        int[] output = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            count[arr[i] - min]++;
        }
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }
        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[arr[i] - min] - 1] = arr[i];
            count[arr[i] - min]--;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] = output[i];
        }
    }

    public static void radixSort(int[] arr) {
        int min = obtenerMinimo(arr);
        
        // Paso 1: Desplazar todos los números para que sean positivos
        for (int i = 0; i < arr.length; i++) {
            arr[i] -= min;
        }

        int max = obtenerMaximo(arr);
        
        // Paso 2: Aplicar Radix Sort tradicional sobre positivos
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countSortForRadix(arr, exp);
        }

        // Paso 3: Revertir el desplazamiento para recuperar valores originales
        for (int i = 0; i < arr.length; i++) {
            arr[i] += min;
        }
    }

    private static void countSortForRadix(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];

        for (int i = 0; i < n; i++) {
            count[(arr[i] / exp) % 10]++;
        }
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }
        for (int i = 0; i < n; i++) {
            arr[i] = output[i];
        }
    }
}