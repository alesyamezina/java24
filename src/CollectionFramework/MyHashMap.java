package CollectionFramework;

import static java.lang.Math.abs;

public class MyHashMap<K, V> {

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private Node[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public MyHashMap(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be >= 1");
        }
        this.table = new Node[capacity];
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    private int indexHash(K key) {
        return abs(key.hashCode()) % table.length;
    }

    public V get(K key) {
        Node<K, V> node = table[indexHash(key)];

        while (node != null) {
            if (node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    public void put(K key, V value) {
        if ((float) size / table.length > DEFAULT_LOAD_FACTOR) {
            resizeTable();
        }

        int indexNewNode = indexHash(key);
        Node<K, V> newNode = new Node<>(key, value);

        if (table[indexNewNode] == null) {
            table[indexNewNode] = newNode;
            size++;
        } else {
            Node<K, V> prevNode = null;
            Node<K, V> node = table[indexHash(key)];

            while (node != null){
                if (node.key.equals(key)) {
                    node.value = value;
                    break;
                }
                prevNode = node;
                node = node.next;
            }
            if (prevNode != null) {
                prevNode.next = newNode;
            }
            size++;
        }
    }

    public void remove(K key) {
        Node<K, V> prevNode = null;
        Node<K, V> node = table[indexHash(key)];

        while (node != null) {
            if (node.key.equals(key)) {
                if (prevNode == null) {
                    table[indexHash(key)] = node.next;
                } else {
                    prevNode.next = node.next;
                }
                size--;
            }
            prevNode = node;
            node = node.next;

        }

    }

    public boolean containsKey(K key) {
        Node<K, V> node = table[indexHash(key)];

        while (node != null) {
            if (node.key.equals(key)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    private void resizeTable() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        table = new Node[newCapacity];

        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                int index = indexHash(node.key) % newCapacity;
                node.next = table[index];
                table[index] = node;
                node = node.next;
            }
        }
    }
}