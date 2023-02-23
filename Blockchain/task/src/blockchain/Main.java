package blockchain;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int numberOfHashZeros = Main.getIntFromCLI(
                "Enter how many zeros the hash must start with:");
        System.out.printf("Create hashes starting with %d zeros\n", numberOfHashZeros);
        Block.setNumberofPrefixedHashZeros(numberOfHashZeros);

        Main.createBlocks(5);
    }

    private static void createBlocks(int n) {
        Block block = new Block("0");
        System.out.println(block);
        for (int i = 1; i < n; i++) {
            System.out.println();
            block = new Block(block.getHash());
            System.out.println(block);
        }
    }

    private static int getIntFromCLI(String prompt) {
        System.out.println(prompt);
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextInt();
        }
    }
}
