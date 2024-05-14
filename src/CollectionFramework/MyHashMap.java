package CollectionFramework;

import static java.lang.Math.abs;

public class MyHashMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private Object[] table;
    private int size;

    public MyHashMap() {
        this.table = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int h = abs(key.hashCode());
        return h % table.length;
    }

    private Node<K, V> getIndex(int index, K key) {
        Node<K, V> node = (Node<K, V>) table[index];
        while (node != null) {
            if (node.key.equals(key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    public V get(K key) {
        int index = hash(key);
        Node<K, V> node = getIndex(index, key);
        if (node != null)
            return node.value;
        else
            return null;
    }

    public V put(K key, V value) {
        int index = hash(key);
        Node<K, V> node = getIndex(index, key);

        if (node != null) {
            node.value = value;
            return node.value;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = (Node<K, V>) table[index];
        table[index] = newNode;
        size++;

        if ((float) size / table.length > DEFAULT_LOAD_FACTOR) {
            resizeTable();
        }

        return null;
    }

    private void resizeTable() {
        Object[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        table = new Object[newCapacity];

        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = (Node<K, V>) oldTable[i];
            while (node != null) {
                int index = hash(node.key) % newCapacity;
                node.next = (Node<K, V>) table[index];
                table[index] = node;
                node = node.next;
            }
        }
    }

    public boolean containsKey(K key) {
        return getIndex(hash(key), key) != null;
    }

    public V remove(K key) {
        int index = hash(key);
        Node<K, V> prevNode = null;
        Node<K, V> node = (Node<K, V>) table[index];

        while (node != null) {
            if (node.key.equals(key)) {
                if (prevNode == null) {
                    table[index] = node.next;
                } else {
                    prevNode.next = node.next;
                }
                size--;
                return node.value;
            }
            prevNode = node;
            node = node.next;
            return node.value;
        }
        return null;
    }
}