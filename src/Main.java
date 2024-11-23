/**
 * @autors: Arkadiusz Przychocki, Małgorzata Rywalska
 * Program do generowania permutacji macierzy i obliczania maksymalnych i minimalnych wartości dla sumy i iloczynu.
 * Użytkownik podaje liczbę wierszy i kolumn macierzy, a następnie wybiera sposób wprowadzenia wartości (ręcznie lub losowo).
 * Program generuje wszystkie możliwe permutacje kolumn i oblicza maksymalne i minimalne wartości dla sumy i iloczynu.
 * Wyniki są wyświetlane w konsoli.
 * Dodatkowo użytkownik może kontynuować obliczenia lub zakończyć program.
 * Do uruchomienia programu wymagane jest środowisko uruchomieniowe Java 22 lub nowsze.
 * Java 23 LTS -> https://www.oracle.com/java/technologies/downloads/
 */

import java.util.*;
import java.util.stream.IntStream;

public class Main {
    // Enum dla operacji
    enum Operation {
        SUM, PRODUCT
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Pętla do kontynuacji obliczeń
        boolean continueCalculating = true;

        while (continueCalculating) {
            // Użytkownik podaje liczbę wierszy i kolumn
            System.out.println("===================================");
            System.out.println("   GENERATOR PERMUTACJI MACIERZY   ");
            System.out.println("===================================");

            // Pobieranie liczby wierszy i kolumn od użytkownika
            int rows = getIntInput(scanner, "Podaj liczbę wierszy: ");
            int cols = getIntInput(scanner, "Podaj liczbę kolumn: ");

            // Uzyskanie macierzy od użytkownika
            int[][] matrix = getMatrixFromUser(rows, cols, scanner);

            // Wyświetlanie macierzy
            displayMatrix(matrix);

            // Generowanie wszystkich możliwych permutacji dla kolumn
            List<int[]> permutations = generatePermutations(cols); // Permutacje kolumn
            displayPermutations(permutations); // Wyświetlanie permutacji

            // Obliczanie wyników dla sumy i iloczynu
            Result sumResult = calculateAndDisplayResults(matrix, permutations, Operation.SUM);
            Result productResult = calculateAndDisplayResults(matrix, permutations, Operation.PRODUCT);

            // Wyświetlanie wyników
            displayCombinedResults(sumResult, productResult);

            // Zapytanie użytkownika, czy chce kontynuować
            System.out.print("Czy chcesz przeprowadzić kolejne obliczenia? (tak/nie): ");
            String response = scanner.next().trim().toLowerCase();
            continueCalculating = response.equals("tak");
        }

        // Podziękowanie użytkownikowi za korzystanie z programu
        System.out.println("Dziękujemy za korzystanie z programu!");
        scanner.close();
    }

    // Metoda do pobierania liczby całkowitej z walidacją
    public static int getIntInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt); // Wyświetlenie komunikatu
                return scanner.nextInt(); // Próba odczytania liczby całkowitej
            } catch (InputMismatchException e) {
                System.out.println("Proszę podać poprawną liczbę całkowitą."); // Obsługa błędów
                scanner.next();  // Odrzucamy nieprawidłowe dane
            }
        }
    }

    // Metoda do uzyskania macierzy od użytkownika
    public static int[][] getMatrixFromUser(int rows, int cols, Scanner scanner) {
        int[][] matrix = new int[rows][cols]; // Inicjalizacja macierzy
        System.out.print("Wybierz sposób wprowadzenia wartości (1 ręcznie, 2 losowo): ");
        int choice = scanner.nextInt(); // Odczyt wyboru użytkownika

        switch (choice) {
            case 1:
                // Wprowadzenie wartości przez użytkownika
                System.out.println("\nWprowadź wartości do macierzy:");
                int[][] finalMatrix = matrix;
                IntStream.range(0, rows).forEach(i ->
                        IntStream.range(0, cols).forEach(j -> {
                            System.out.print("Wartość [" + (i + 1) + "][" + (j + 1) + "]: "); // Indeksy zaczynają się od 1
                            finalMatrix[i][j] = scanner.nextInt(); // Odczyt wartości do macierzy
                        })
                );
                break;
            case 2:
                // Generowanie losowej macierzy
                matrix = generateRandomMatrix(rows, cols);
                System.out.println("\nWygenerowana macierz losowa:");
                break;
            default:
                // Obsługa błędnego wyboru
                System.out.println("Nieprawidłowy wybór. Używam domyślnej macierzy zerowej.");
                break;
        }
        return matrix; // Zwrócenie macierzy
    }

    // Generowanie losowej macierzy o podanych wymiarach
    public static int[][] generateRandomMatrix(int rows, int cols) {
        Random random = new Random(); // Inicjalizacja generatora liczb losowych
        // Tworzenie macierzy losowej
        return IntStream.range(0, rows)
                .mapToObj(_ -> IntStream.range(0, cols).map(_ -> random.nextInt(100)).toArray()) // Wypełnienie macierzy losowymi wartościami
                .toArray(int[][]::new); // Zwrócenie macierzy
    }

    // Wyświetlanie macierzy w czytelny sposób
    private static void displayMatrix(int[][] matrix) {
        System.out.println("\n===============================");
        System.out.println("       Macierz wejściowa        ");
        System.out.println("===============================");
        System.out.println("                Kolumna"); // Etykieta Kolumna
        // Wyświetlanie nagłówków kolumn
        System.out.print("             ");  // Dodatkowe spacje dla estetyki
        for (int col = 1; col <= matrix[0].length; col++) {
            System.out.printf("%-8d", col); // Wyświetlamy numery kolumn
        }
        System.out.println();
        System.out.println("------------------------------------------------------");
        // Wyświetlanie wierszy macierzy
        for (int row = 0; row < matrix.length; row++) {
            System.out.printf("  Wiersz %d | ", row + 1); // Wyświetlamy numery wierszy
            for (int value : matrix[row]) {
                System.out.printf("%-8d", value); // Wyświetlamy wartości w macierzy
            }
            System.out.println();
        }

        System.out.println("------------------------------------------------------");
    }

    // Generowanie permutacji liczb od 0 do n-1
    private static List<int[]> generatePermutations(int n) {
        List<int[]> permutations = new ArrayList<>(); // Lista do przechowywania permutacji
        permute(permutations, IntStream.range(0, n).toArray(), 0); // Wywołanie metody rekurencyjnej do generowania permutacji
        return permutations; // Zwrócenie listy permutacji
    }

    // Rekurencyjne generowanie permutacji
    private static void permute(List<int[]> permutations, int[] arr, int index) {
        if (index == arr.length) {
            permutations.add(arr.clone()); // Dodanie kopii aktualnej permutacji do listy
        } else {
            IntStream.range(index, arr.length).forEach(i -> {
                swap(arr, index, i); // Zamiana miejscami aktualnego elementu z innym
                permute(permutations, arr, index + 1); // Rekurencyjne wywołanie dla następnego indeksu
                swap(arr, index, i);  // Przywrócenie oryginalnego stanu
            });
        }
    }

    // Metoda do zamiany elementów w tablicy
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i]; // Tymczasowe przechowanie wartości
        arr[i] = arr[j]; // Zamiana miejscami
        arr[j] = temp; // Przywrócenie oryginalnego stanu
    }

    // Wyświetlanie permutacji
    private static void displayPermutations(List<int[]> permutations) {
        System.out.println("\n===============================");
        System.out.println("      Wygenerowane permutacje        ");
        System.out.println("===============================");
        for (int[] perm : permutations) {
            System.out.println(getPathString(perm)); // Wyświetlenie ścieżki dla każdej permutacji
        }
        System.out.println("===============================");
    }

    // Obliczanie wartości dla danej permutacji
    private static int calculateValue(int[][] matrix, int[] perm, Operation operation) {
        int result = (operation == Operation.SUM) ? 0 : 1; // Ustawienie wartości początkowej

        for (int i = 0; i < perm.length; i++) {
            if (operation == Operation.SUM) {
                result += matrix[i][perm[i]]; // Obliczenie sumy
            } else {
                result *= matrix[i][perm[i]]; // Obliczenie iloczynu
            }
        }
        return result; // Zwrócenie obliczonej wartości
    }

    // Obliczanie wyników dla sumy i iloczynu oraz wyświetlanie ich
    private static Result calculateAndDisplayResults(int[][] matrix, List<int[]> permutations, Operation operation) {
        List<Result> results = new ArrayList<>(); // Lista do przechowywania wyników

        for (int[] perm : permutations) {
            int value = calculateValue(matrix, perm, operation); // Obliczenie wartości
            results.add(new Result(value, perm)); // Dodanie wyniku i permutacji do listy
        }

        // Zdefiniowanie maksymalnych i minimalnych wartości
        Optional<Result> maxResult = results.stream().max(Comparator.comparingInt(r -> r.maxValue)); // Wyznaczenie maksymalnego wyniku
        Optional<Result> minResult = results.stream().min(Comparator.comparingInt(r -> r.minValue)); // Wyznaczenie minimalnego wyniku

        // Zwrócenie wyników, jeśli są dostępne
        return maxResult.map(result -> new Result(result.maxValue, result.maxPerm, minResult.get().minValue, minResult.get().minPerm)).orElse(null);
    }

    // Wyświetlanie wyników
    private static void displayCombinedResults(Result sumResult, Result productResult) {
        if (sumResult != null && productResult != null) { // Sprawdzenie, czy wyniki są dostępne
            System.out.println("\n===============================");
            System.out.println("        Wyniki obliczeń         ");
            System.out.println("===============================");
            System.out.println("Maksymalna suma: " + sumResult.maxValue + "  Ścieżka: " + getPathString(sumResult.maxPerm)); // Wyświetlenie maksymalnej sumy
            System.out.println("Minimalna suma: " + sumResult.minValue + "  Ścieżka: " + getPathString(sumResult.minPerm)); // Wyświetlenie minimalnej sumy
            System.out.println("Maksymalny iloczyn: " + productResult.maxValue + "  Ścieżka: " + getPathString(productResult.maxPerm)); // Wyświetlenie maksymalnego iloczynu
            System.out.println("Minimalny iloczyn: " + productResult.minValue + "  Ścieżka: " + getPathString(productResult.minPerm)); // Wyświetlenie minimalnego iloczynu
            System.out.println("===============================\n"); // Podsumowanie
        }
    }

    // Konwersja współrzędnych do łańcucha tekstowego
    private static String getPathString(int[] perm) {
        StringBuilder path = new StringBuilder(); // Tworzenie StringBuildera do budowy ścieżki
        for (int i = 0; i < perm.length; i++) {
            path.append("(").append(i + 1).append(", ").append(perm[i] + 1).append(")"); // Dodanie współrzędnych do ścieżki
            if (i < perm.length - 1) {
                path.append(" -> "); // Dodanie strzałki między współrzędnymi
            }
        }
        return path.toString(); // Zwrócenie ścieżki
    }
}

// Klasa Result do przechowywania wartości i odpowiadającej permutacji
class Result {
    int maxValue; // Przechowywana maksymalna wartość
    int minValue; // Przechowywana minimalna wartość
    int[] maxPerm; // Odpowiadająca maksymalna permutacja
    int[] minPerm; // Odpowiadająca minimalna permutacja

    // Konstruktor dla maksymalnych i minimalnych wartości
    Result(int maxValue, int[] maxPerm, int minValue, int[] minPerm) {
        this.maxValue = maxValue; // Ustawienie maksymalnej wartości
        this.maxPerm = maxPerm; // Ustawienie maksymalnej permutacji
        this.minValue = minValue; // Ustawienie minimalnej wartości
        this.minPerm = minPerm; // Ustawienie minimalnej permutacji
    }

    // Konstruktor dla wyniku pojedynczego
    Result(int value, int[] perm) {
        this.maxValue = value; // Ustawienie maksymalnej wartości
        this.minValue = value; // Ustawienie minimalnej wartości
        this.maxPerm = perm; // Ustawienie maksymalnej permutacji
        this.minPerm = perm; // Ustawienie minimalnej permutacji
    }
}
