import java.util.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Użytkownik podaje liczbę wierszy i kolumn
        System.out.println("Podaj liczbę wierszy: ");
        int rows = scanner.nextInt();
        System.out.println("Podaj liczbę kolumn: ");
        int cols = scanner.nextInt();

        int[][] matrix = new int[rows][cols];

        // Wybór sposobu wprowadzenia wartości do macierzy
        System.out.println("Wybierz sposób wprowadzenia wartości (1 ręcznie, 2 losowo): ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                // Ręczne wprowadzenie wartości do macierzy
                System.out.println("Wprowadź wartości do macierzy:");
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        System.out.print("Wartość [" + i + "][" + j + "]: ");
                        matrix[i][j] = scanner.nextInt();
                    }
                }
                break;
            case 2:
                // Losowe generowanie wartości do macierzy
                matrix = generateRandomMatrix(rows, cols);
                break;
            default:
                System.out.println("Nieprawidłowy wybór.");
                return;
        }

        System.out.println("Macierz:");
        printMatrix(matrix);

        // Generowanie permutacji wierszy i kolumn
        List<int[]> rowPermutations = generatePermutations(rows);
        List<int[]> colPermutations = generatePermutations(cols);

        // Obliczanie maksymalnych i minimalnych sum oraz iloczynów
        int maxSum = Integer.MIN_VALUE;
        int minSum = Integer.MAX_VALUE;
        String maxSumPath = "";
        String minSumPath = "";
        int maxProduct = Integer.MIN_VALUE;
        int minProduct = Integer.MAX_VALUE;
        String maxProductPath = "";
        String minProductPath = "";

        for (int[] rowPerm : rowPermutations) {
            for (int[] colPerm : colPermutations) {
                int sum = calculateSum(matrix, rowPerm, colPerm);
                int product = calculateProduct(matrix, rowPerm, colPerm);

                // Update max/min sums and paths
                if (sum > maxSum) {
                    maxSum = sum;
                    maxSumPath = generatePath(rowPerm, colPerm);
                }
                if (sum < minSum) {
                    minSum = sum;
                    minSumPath = generatePath(rowPerm, colPerm);
                }

                // Update max/min products and paths
                if (product > maxProduct) {
                    maxProduct = product;
                    maxProductPath = generatePath(rowPerm, colPerm);
                }
                if (product < minProduct) {
                    minProduct = product;
                    minProductPath = generatePath(rowPerm, colPerm);
                }
            }
        }

        // Wyświetlanie wyników
        System.out.println("Maksymalna suma: " + maxSum + " (Ścieżka: " + maxSumPath + ")");
        System.out.println("Minimalna suma: " + minSum + " (Ścieżka: " + minSumPath + ")");
        System.out.println("Maksymalny iloczyn: " + maxProduct + " (Ścieżka: " + maxProductPath + ")");
        System.out.println("Minimalny iloczyn: " + minProduct + " (Ścieżka: " + minProductPath + ")");
    }

    // Generowanie losowej macierzy o podanych wymiarach
    public static int[][] generateRandomMatrix(int rows, int cols) {
        Random random = new Random();
        int[][] matrix = new int[rows][cols];
        IntStream.range(0, rows).forEach(i ->
                IntStream.range(0, cols).forEach(j -> matrix[i][j] = random.nextInt(100))
        );
        return matrix;
    }

    // Generowanie permutacji liczb od 0 do n-1
    private static List<int[]> generatePermutations(int n) {
        List<int[]> permutations = new ArrayList<>();
        permute(permutations, IntStream.range(0, n).toArray(), 0);
        return permutations;
    }

    // Rekurencyjne generowanie permutacji
    private static void permute(List<int[]> permutations, int[] arr, int index) {
        if (index == arr.length) {
            permutations.add(arr.clone());
        } else {
            for (int i = index; i < arr.length; i++) {
                swap(arr, index, i);
                permute(permutations, arr, index + 1);
                swap(arr, index, i);  // Przywróć oryginalny stan
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Wyświetlanie macierzy
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

    // Obliczanie sumy elementów macierzy zgodnie z permutacjami wierszy i kolumn
    private static int calculateSum(int[][] matrix, int[] rowPerm, int[] colPerm) {
        int sum = 0;
        for (int i = 0; i < rowPerm.length; i++) {
            sum += matrix[rowPerm[i]][colPerm[i]];
        }
        return sum;
    }

    // Obliczanie iloczynu elementów macierzy zgodnie z permutacjami wierszy i kolumn
    private static int calculateProduct(int[][] matrix, int[] rowPerm, int[] colPerm) {
        int product = 1;
        for (int i = 0; i < rowPerm.length; i++) {
            product *= matrix[rowPerm[i]][colPerm[i]];
        }
        return product;
    }

    // Generowanie ścieżki dla danego zestawu permutacji
    private static String generatePath(int[] rowPerm, int[] colPerm) {
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < rowPerm.length; i++) {
            path.append(String.format("(%d,%d) ", rowPerm[i], colPerm[i]));
        }
        return path.toString().trim();
    }
}
