import java.util.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Użytkownik podaje liczbę wierszy i kolumn
        System.out.println("===================================");
        System.out.println("   GENERATOR PERMUTACJI MACIERZY   ");
        System.out.println("===================================");
        System.out.print("Podaj liczbę wierszy: ");
        int rows = scanner.nextInt();
        System.out.print("Podaj liczbę kolumn: ");
        int cols = scanner.nextInt();

        // Tworzenie finalnej zmiennej dla matrix
        final int[][] matrix;

        // Wybór sposobu wprowadzenia wartości do macierzy
        System.out.print("Wybierz sposób wprowadzenia wartości (1 ręcznie, 2 losowo): ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                matrix = new int[rows][cols];  // alokacja pamięci dla macierzy
                System.out.println("\nWprowadź wartości do macierzy:");
                IntStream.range(0, rows).forEach(i ->
                        IntStream.range(0, cols).forEach(j -> {
                            System.out.print("Wartość [" + i + "][" + j + "]: ");
                            matrix[i][j] = scanner.nextInt();
                        })
                );
                break;
            case 2:
                matrix = generateRandomMatrix(rows, cols);
                System.out.println("\nWygenerowana macierz losowa:");
                break;
            default:
                System.out.println("Nieprawidłowy wybór.");
                return;
        }

        // Wyświetlanie macierzy
        System.out.println("\n===============================");
        System.out.println("       Macierz wejściowa        ");
        System.out.println("===============================");
        printMatrix(matrix);

        // Generowanie permutacji kolumn i obliczanie wyników
        List<int[]> permutations = generatePermutations(matrix.length);

        System.out.println("\n===============================");
        System.out.println("        Wygenerowane permutacje ");
        System.out.println("===============================");
        permutations.forEach(perm -> System.out.println(Arrays.toString(perm)));

        // Obliczanie maksymalnej i minimalnej sumy oraz iloczynu
        int maxSum = permutations.stream()
                .mapToInt(perm -> calculateSum(matrix, perm))
                .max()
                .orElse(Integer.MIN_VALUE);

        int minSum = permutations.stream()
                .mapToInt(perm -> calculateSum(matrix, perm))
                .min()
                .orElse(Integer.MAX_VALUE);

        int maxProduct = permutations.stream()
                .mapToInt(perm -> calculateProduct(matrix, perm))
                .max()
                .orElse(Integer.MIN_VALUE);

        int minProduct = permutations.stream()
                .mapToInt(perm -> calculateProduct(matrix, perm))
                .min()
                .orElse(Integer.MAX_VALUE);

        // Wyświetlanie wyników
        System.out.println("\n===============================");
        System.out.println("        Wyniki obliczeń         ");
        System.out.println("===============================");
        System.out.println("Maksymalna suma: " + maxSum);
        System.out.println("Minimalna suma: " + minSum);
        System.out.println("-------------------------------");
        System.out.println("Maksymalny iloczyn: " + maxProduct);
        System.out.println("Minimalny iloczyn: " + minProduct);
        System.out.println("===============================\n");
    }

    // Generowanie losowej macierzy o podanych wymiarach
    public static int[][] generateRandomMatrix(int rows, int cols) {
        Random random = new Random();
        return IntStream.range(0, rows).mapToObj(i ->
                IntStream.range(0, cols).map(j -> random.nextInt(100)).toArray()
        ).toArray(int[][]::new);
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
            IntStream.range(index, arr.length).forEach(i -> {
                swap(arr, index, i);
                permute(permutations, arr, index + 1);
                swap(arr, index, i);  // Przywróć oryginalny stan
            });
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

    // Wyświetlanie macierzy w czytelny sposób
    private static void printMatrix(int[][] matrix) {
        Arrays.stream(matrix).forEach(row -> {
            Arrays.stream(row).forEach(value -> System.out.print(value + "\t"));
            System.out.println();
        });
    }
}