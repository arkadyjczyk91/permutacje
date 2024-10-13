import java.util.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Wyświetlenie nagłówka
        System.out.println("===================================");
        System.out.println("   GENERATOR PERMUTACJI MACIERZY   ");
        System.out.println("===================================");

        // Użytkownik podaje liczbę wierszy i kolumn dla macierzy
        System.out.print("Podaj liczbę wierszy: ");
        int rows = scanner.nextInt();
        System.out.print("Podaj liczbę kolumn: ");
        int cols = scanner.nextInt();

        // Tworzenie finalnej zmiennej matrix, której wartości zależą od wyboru użytkownika
        final int[][] matrix;

        // Użytkownik wybiera sposób wprowadzenia danych do macierzy (ręcznie lub losowo)
        System.out.print("Wybierz sposób wprowadzenia wartości (1 ręcznie, 2 losowo): ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // Alokacja pamięci dla macierzy, jeśli użytkownik chce wprowadzać wartości ręcznie
                matrix = new int[rows][cols];
                System.out.println("\nWprowadź wartości do macierzy:");

                // Pobieranie wartości do macierzy od użytkownika
                IntStream.range(0, rows).forEach(i ->
                        IntStream.range(0, cols).forEach(j -> {
                            System.out.print("Wartość [" + i + "][" + j + "]: ");
                            matrix[i][j] = scanner.nextInt();
                        })
                );
                break;
            case 2:
                // Losowe generowanie macierzy o podanych wymiarach
                matrix = generateRandomMatrix(rows, cols);
                System.out.println("\nWygenerowana macierz losowa:");
                break;
            default:
                // Zakończenie programu, jeśli wybór jest nieprawidłowy
                System.out.println("Nieprawidłowy wybór.");
                return;
        }

        // Wyświetlanie macierzy wejściowej (zależnie od tego, czy była ręcznie wprowadzana, czy losowo generowana)
        System.out.println("\n===============================");
        System.out.println("       Macierz wejściowa        ");
        System.out.println("===============================");
        printMatrix(matrix); // Funkcja pomocnicza do czytelnego wyświetlania macierzy

        // Generowanie wszystkich permutacji indeksów kolumn (do ułożenia elementów w macierzy)
        List<int[]> permutations = generatePermutations(matrix.length);

        // Wyświetlanie wygenerowanych permutacji
        System.out.println("\n===============================");
        System.out.println("        Wygenerowane permutacje ");
        System.out.println("===============================");
        permutations.forEach(perm -> System.out.println(Arrays.toString(perm)));

        // Obliczanie maksymalnej i minimalnej sumy oraz iloczynu elementów macierzy zgodnie z permutacjami
        int maxSum = permutations.stream()
                .mapToInt(perm -> calculateSum(matrix, perm)) // Obliczanie sum dla każdej permutacji
                .max()
                .orElse(Integer.MIN_VALUE);

        int minSum = permutations.stream()
                .mapToInt(perm -> calculateSum(matrix, perm)) // Obliczanie minimalnych sum
                .min()
                .orElse(Integer.MAX_VALUE);

        int maxProduct = permutations.stream()
                .mapToInt(perm -> calculateProduct(matrix, perm)) // Obliczanie maksymalnych iloczynów
                .max()
                .orElse(Integer.MIN_VALUE);

        int minProduct = permutations.stream()
                .mapToInt(perm -> calculateProduct(matrix, perm)) // Obliczanie minimalnych iloczynów
                .min()
                .orElse(Integer.MAX_VALUE);

        // Wyświetlanie wyników maksymalnych i minimalnych sum oraz iloczynów
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

    /**
     * Generowanie losowej macierzy o podanych wymiarach.
     * Elementy są losowane w zakresie od 0 do 99.
     *
     * @param rows liczba wierszy
     * @param cols liczba kolumn
     * @return wygenerowana macierz 2D
     */
    public static int[][] generateRandomMatrix(int rows, int cols) {
        Random random = new Random();  // Tworzymy obiekt generatora losowego
        return IntStream.range(0, rows).mapToObj(i ->
                IntStream.range(0, cols).map(j -> random.nextInt(100)).toArray()
        ).toArray(int[][]::new);  // Tworzymy i zwracamy macierz 2D
    }

    /**
     * Generowanie wszystkich permutacji liczb od 0 do n-1.
     * Każda permutacja reprezentuje różne kolejności kolumn, które będą używane
     * do obliczeń sum i iloczynów elementów.
     *
     * @param n liczba elementów (kolumn)
     * @return lista wszystkich permutacji
     */
    private static List<int[]> generatePermutations(int n) {
        List<int[]> permutations = new ArrayList<>();
        permute(permutations, IntStream.range(0, n).toArray(), 0);
        return permutations; // Zwraca listę permutacji
    }

    /**
     * Rekurencyjne generowanie permutacji liczb w tablicy arr.
     *
     * @param permutations lista, do której będą dodawane permutacje
     * @param arr          tablica elementów, z których generowane są permutacje
     * @param index        aktualny indeks, od którego permutacje są obliczane
     */
    private static void permute(List<int[]> permutations, int[] arr, int index) {
        if (index == arr.length) {
            permutations.add(arr.clone()); // Jeśli dotarliśmy do końca tablicy, dodaj permutację
        } else {
            IntStream.range(index, arr.length).forEach(i -> {
                swap(arr, index, i);       // Zamień aktualny element z kolejnym
                permute(permutations, arr, index + 1); // Wywołanie rekurencyjne dla kolejnego indeksu
                swap(arr, index, i);       // Przywróć oryginalny stan (backtracking)
            });
        }
    }

    /**
     * Zamiana dwóch elementów w tablicy arr.
     *
     * @param arr tablica, w której dokonujemy zamiany
     * @param i   indeks pierwszego elementu
     * @param j   indeks drugiego elementu
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Obliczanie sumy elementów macierzy dla danej permutacji kolumn.
     *
     * @param matrix macierz 2D
     * @param perm   permutacja kolumn (indeksy kolumn)
     * @return suma elementów zgodnie z permutacją
     */
    private static int calculateSum(int[][] matrix, int[] perm) {
        return IntStream.range(0, perm.length)
                .map(i -> matrix[i][perm[i]]) // Wybierz element z wiersza i-tego zgodnie z permutacją
                .sum();
    }

    /**
     * Obliczanie iloczynu elementów macierzy dla danej permutacji kolumn.
     *
     * @param matrix macierz 2D
     * @param perm   permutacja kolumn (indeksy kolumn)
     * @return iloczyn elementów zgodnie z permutacją
     */
    private static int calculateProduct(int[][] matrix, int[] perm) {
        return IntStream.range(0, perm.length)
                .map(i -> matrix[i][perm[i]]) // Wybierz element z wiersza i-tego zgodnie z permutacją
                .reduce(1, (a, b) -> a * b); // Mnożenie elementów
    }

    /**
     * Wyświetlanie macierzy w czytelny sposób. Każdy wiersz jest wyświetlany w osobnej linii.
     *
     * @param matrix macierz do wyświetlenia
     */
    private static void printMatrix(int[][] matrix) {
        Arrays.stream(matrix).forEach(row -> {
            Arrays.stream(row).forEach(value -> System.out.print(value + "\t")); // Wyświetl każdy element z wiersza
            System.out.println(); // Przejdź do nowej linii po każdym wierszu
        });
    }
}
