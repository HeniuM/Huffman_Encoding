package org.example;

import java.util.*;

class Node implements Comparable<Node> {
    char character;
    int frequency;
    Node left, right;

    public Node(char character, int frequency, Node left, Node right) {
        this.character = character;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.frequency, other.frequency);
    }
}

public class Main {
    public static void main(String[] args) {
        // Accepting text input from the user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the text to encode: ");
        String text = scanner.nextLine();

        // Removing spaces and non-alphabetic characters
        text = text.replaceAll("\\s+", "");
        text = text.replaceAll("[^a-zA-Z]+", "");

        // Creating a frequency table of characters
        Map<Character, Integer> frequencies = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
        }

        // Building the Huffman tree
        PriorityQueue<Node> minHeap = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            minHeap.offer(new Node(entry.getKey(), entry.getValue(), null, null));
        }

        while (minHeap.size() > 1) {
            Node leftChild = minHeap.poll();
            Node rightChild = minHeap.poll();
            Node parent = new Node('-', leftChild.frequency + rightChild.frequency, leftChild, rightChild); // '-' for internal nodes
            minHeap.offer(parent);
        }

        Node root = minHeap.poll();

        // Generating Huffman codes
        Map<Character, String> codes = new HashMap<>();
        generateCodes(root, "", codes);

        // Displaying the results (characters with their frequencies and Huffman codes)
        System.out.println("Character\tFrequency\tHuffman code");
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            char character = entry.getKey();
            int frequency = entry.getValue();
            String code = codes.get(character);
            System.out.println(character + "\t\t\t" + frequency + "\t\t\t" + (code != null ? code : "-")); // '-' if code is empty
        }

        // Drawing the Huffman tree
        System.out.println("\nHuffman Tree:");
        drawHuffmanTree(root, "");
    }

    private static void generateCodes(Node node, String code, Map<Character, String> codes) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            codes.put(node.character, code);
        }

        generateCodes(node.left, code + "0", codes);
        generateCodes(node.right, code + "1", codes);
    }

    private static void drawHuffmanTree(Node node, String indent) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            System.out.println(indent + node.character);
        } else {
            System.out.println(indent + "-");
            System.out.println(indent + "|");
            System.out.println(indent + "+-left:");
            drawHuffmanTree(node.left, indent + "|    ");
            System.out.println(indent + "+-right:");
            drawHuffmanTree(node.right, indent + "     ");
        }
    }
}
