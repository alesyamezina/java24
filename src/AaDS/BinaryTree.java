package AaDS;

import java.util.LinkedList;
import java.util.Queue;
class Node {
    int value;
    Node left;
    Node right;

    public Node(int value) {
        this.value = value;
    }
}
public class BinaryTree {
    private Node root;

    public BinaryTree(int value) {
        this.root = new Node(value);
    }

    public void insert(int value) {
        insert(root, value);
    }

    private void insert(Node node, int value) {
        if (value < node.value) {
            if (node.left == null) {
                node.left = new Node(value);
            } else {
                insert(node.left, value);
            }
        } else {
            if (node.right == null) {
                node.right = new Node(value);
            } else {
                insert(node.right, value);
            }
        }
    }

    public void printBFS() {
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            System.out.print(node.value + " ");

            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
    }

    public void printDFS() {
        printDFS(root);
    }

    private void printDFS(Node node) {
        if (node == null) {
            return;
        }

        System.out.print(node.value + " ");
        printDFS(node.left);
        printDFS(node.right);
    }

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(3);
        tree.insert(7);
        tree.insert(12);
        tree.insert(4);
        tree.insert(20);

        System.out.println("BFS:");
        tree.printBFS();

        System.out.println("\nDFS:");
        tree.printDFS();
    }
}