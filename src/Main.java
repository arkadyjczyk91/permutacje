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
                int[][] scannerMatrix = matrix;
                System.out.println("Wprowadź wartości do macierzy:");
                IntStream.range(0, rows).forEach(i ->
                        IntStream.range(0, cols).forEach(j -> {
                            System.out.print("Wartość [" + i + "][" + j + "]: ");
                            scannerMatrix[i][j] = scanner.nextInt();
                        })
                );
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
        int[][] finalMatrix = matrix;
        IntStream.range(0, rows).forEach(i -> {
            IntStream.range(0, cols).forEach(j -> System.out.print(finalMatrix[i][j] + "\t"));
            System.out.println();
        });

        // Generowanie permutacji i obliczanie wyników
        List<int[]> permutations = generatePermutations(matrix.length);
        int[][] matrixFinal = matrix;
        int maxSum = permutations.stream()
                .mapToInt(perm -> calculateSum(matrixFinal, perm))
                .max()
                .orElse(0);
        int minSum = permutations.stream()
                .mapToInt(perm -> calculateSum(matrixFinal, perm))
                .min()
                .orElse(0);

        int maxProduct = permutations.stream()
                .mapToInt(perm -> calculateProduct(matrixFinal, perm))
                .max()
                .orElse(0);
        int minProduct = permutations.stream()
                .mapToInt(perm -> calculateProduct(matrixFinal, perm))
                .min()
                .orElse(0);

        // Wyświetlanie wyników
        System.out.println("Maksymalna suma: " + maxSum);
        System.out.println("Minimalna suma: " + minSum);
        System.out.println("Maksymalny iloczyn: " + maxProduct);
        System.out.println("Minimalny iloczyn: " + minProduct);
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

    // Obliczanie sumy elementów macierzy zgodnie z permutacją
    private static int calculateSum(int[][] matrix, int[] perm) {
        return IntStream.range(0, perm.length)
                .map(i -> matrix[i][perm[i]])
                .sum();
    }

    // Obliczanie iloczynu elementów macierzy zgodnie z permutacją
    private static int calculateProduct(int[][] matrix, int[] perm) {
        return IntStream.range(0, perm.length)
                .map(i -> matrix[i][perm[i]])
                .reduce(1, (a, b) -> a * b);
    }
}
