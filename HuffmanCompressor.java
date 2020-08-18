import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HuffmanCompressor {
    private HuffmanNode root;
    private String inputText;
    private ArrayList<HuffmanNode> nodes;
    private HashMap<Character, String> encodeKey = new HashMap<>();

    public HuffmanCompressor(){
        root = null;
        inputText = "";
        nodes = null;
    }

    /**
     * Method that reads a file into a string variable
     */
    public String readIn(String fileName) {
        String text = "";
        try{
            text = new String(Files.readAllBytes(Paths.get(fileName)));
            System.out.println(text);
        }
        catch (IOException E){
            E.printStackTrace();
        }
        inputText = text;
        return text;
    }

    /**
     * Creates a Hash map of characters along with their frequencies and creates array list of these pairs
     */
    public void initializeTable(){
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < inputText.length(); i++){
            if (map.containsKey(inputText.charAt(i))){
                int temp = map.get(inputText.charAt(i));
                map.put(inputText.charAt(i), map.get(inputText.charAt(i))+1);
            }
            else {
                map.put(inputText.charAt(i), 1);
            }
        }

        ArrayList<HuffmanNode> array = new ArrayList<>();
        for (Character key : map.keySet()) {
            HuffmanNode newNode = new HuffmanNode();
            newNode.setFrequency(map.get(key));
            newNode.setInChar(key);
            array.add(newNode);
        }
        nodes = array;
        Collections.sort(nodes);

    //        HuffmanNode top = new HuffmanNode();
    //        HuffmanNode pointer = top;
    //        while (map.isEmpty() != true){
    //            int intMax = 0;
    //            Character charMax = null;
    //            for (Character key : map.keySet()){
    //                if (map.get(key) > intMax){
    //                    intMax = map.get(key);
    //                    charMax = key;
    //                }
    //            }
    //            HuffmanNode temp = new HuffmanNode();
    //            temp.setFrequency(intMax);
    //            temp.setInChar(charMax);
    //            pointer = temp;
    //            pointer = pointer.getNextInOrder();
    //            map.remove(charMax);
    //        }
    }

    /**
     * Shifts two nodes under a common parent node which contains the sum of their frequencies
     */
    public HuffmanNode combinePointers(HuffmanNode node1, HuffmanNode node2){
        HuffmanNode parent = new HuffmanNode();
        parent.setFrequency(node1.getFrequency() + node2.getFrequency());
        if (node1.getFrequency() <= node2.getFrequency()){
            parent.setLeft(node1);
            parent.setRight(node2);
        }
        else{
            parent.setLeft(node2);
            parent.setRight(node1);
        }
        return parent;
    }

    /**
     * Creates the Huffman binary tree
     */
    public HuffmanNode constructTree(){
        while(nodes.size() > 1){
            HuffmanNode parent = combinePointers(nodes.get(0), nodes.get(1));
            nodes.add(parent);
            nodes.remove(0);
            nodes.remove(0);
            Collections.sort(nodes);
        }
        root = nodes.get(0);
        return nodes.get(0);
    }

    /**
     * finds huffman encoding for each character starting at the root
     */
    public void encoding(HuffmanNode root) {
        if(root.getLeft() != null && root.getRight() != null){
            encoding(root.getLeft(), "0");
            encoding(root.getRight(), "1");
        }
    }

    /**
     * finds huffman encoding of all characters under a given node
     */
    public void encoding(HuffmanNode node, String input){
        node.setCoding(input);
        if (node.getLeft() != null && node.getRight() != null) {
            encoding(node.getLeft(), node.getCoding() + "0");
            encoding(node.getRight(), node.getCoding() + "1");
        }
    }

    /**
     * Finds the encoding of a particular character
     */
    public String findEncoding(HuffmanNode node, Character ch){
        if(node.getLeft() != null && node.getRight() != null){
            return findEncoding(node.getLeft(), ch) + findEncoding(node.getRight(), ch);
        }
        if (ch.equals(node.getInChar())){
            return node.getCoding();
        }
        else {
            return "";
        }
    }

    /**
     * Finds the height of the huffman binary tree
     */
    public int huffmanHeight(HuffmanNode root){
        if (root.getLeft() != null && root.getRight() != null){
            return 1 + Math.max(huffmanHeight(root.getLeft()), huffmanHeight(root.getRight()));
        }
        else {
            return 0;
        }
    }

    /**
     * Finds the balance of the Huffman tree
     */
    public int huffmanBalance(HuffmanNode root){
        return huffmanHeight(root.getLeft()) - huffmanHeight(root.getRight());
    }

    /**
     * Calculates the space saved by the Huffman compression algorithm
     */
    public int spaceSaved(HuffmanNode node){
        if(node.getLeft() != null && node.getRight() != null){
            return spaceSaved(node.getLeft()) + spaceSaved(node.getRight());
        }
        return root.getFrequency() * (8 - root.getCoding().length());
    }

    /**
     * Finds the number of leaf nodes in the tree
     */
    public int numLeaves(HuffmanNode node){
        if(node == null){
            return 0;
        }
        if(node.getLeft() == null && node.getRight() == null){
            return 1;
        }
        else {
            return numLeaves(node.getLeft()) + numLeaves(node.getRight());
        }
    }

    /**
     * Converts File to Encoded File
     */
    public void encodeFile(String inFile, String outFile, HuffmanNode node) throws IOException {
        String str = readIn(inFile);
        String output = "";
        for (int i = 0; i < str.length(); i++){
            output += findEncoding(node, str.charAt(i));
        }
        Writer writer = new FileWriter(outFile);
        writer.write(output);
        writer.close();
    }

    /**
     * Static method applying the Huffman encoding algorithm
     */
    public static String huffmanCoder(String inputFileName, String outputFileName) throws IOException {
        HuffmanCompressor compressor = new HuffmanCompressor();
        if (inputFileName == null){
            return "Input File Error";
        }
        if (outputFileName == null){
            return "No Output File";
        }
        compressor.readIn(inputFileName);
        compressor.initializeTable();
        compressor.constructTree();
        compressor.encoding(compressor.root);
        System.out.println("Height of tree: " + compressor.huffmanHeight(compressor.root));
        System.out.println("Number of leaves: " + compressor.numLeaves(compressor.root));
        System.out.println("Balance of tree: " + compressor.huffmanBalance(compressor.root));
        System.out.println("Space saved: " + compressor.spaceSaved(compressor.root));
        compressor.root.print();
        compressor.encodeFile(inputFileName, outputFileName, compressor.root);
        return "OK";
    }

    public static void main(String[] args) throws IOException {
//        HuffmanCompressor tester = new HuffmanCompressor();
//        tester.readIn("RandomSample.txt");
//        tester.initializeTable();
//        tester.constructTree();
//        tester.encoding(tester.root);
//        tester.root.print();
//        tester.encodeFile("Frankenstein.txt", "outputFile.txt", tester.root);
        HuffmanCompressor.huffmanCoder("Frankenstein.txt", "staticOutput.txt");
    }
}
