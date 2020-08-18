public class HuffmanNode implements Comparable<HuffmanNode>{
    private Character inChar = null;
    private int frequency = 0;
    private HuffmanNode left;
    private HuffmanNode right;
    private HuffmanNode nextInOrder;
    private String coding = "";

    public HuffmanNode(){
        left = null;
        right = null;
        nextInOrder = null;
    }

    public Character getInChar() {
        return inChar;
    }

    public void setInChar(Character chr){
        inChar = chr;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int num) {
        frequency = num;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public void setLeft(HuffmanNode node) {
        left = node;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public void setRight(HuffmanNode node) {
        right = node;
    }

    public HuffmanNode getNextInOrder() {
        return nextInOrder;
    }

    public void setNextInOrder(HuffmanNode node) {
        nextInOrder = node;
    }

    public String getCoding(){
        return coding;
    }

    public void setCoding(String bit){
        coding += bit;
    }

    public void print(){
        if (getInChar() == null){
            left.print();
            right.print();
        }
        else{
            System.out.println(inChar + " : " + frequency + " : " + coding);
        }
    }

    @Override
    public int compareTo(HuffmanNode node) {
        if (this.frequency < node.frequency)
            return -1;
        else if (this.frequency > node.frequency)
            return 1;
        else
            return 0;
    }

    public String toString(){
        return inChar + " : " + frequency + " : " + coding;
    }
}
