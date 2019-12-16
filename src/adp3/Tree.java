package adp3;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Tree<Key extends Comparable<Key>, Value> {
    private Node root; // root of BST

    public Tree(Key key, Value value) throws Exception {
        new Tree<Key, Value>(new Node(key, value, 1));
    }

    public Tree(Node root) throws Exception {
        this.root = root;
        if (!this.isBST()) {
            throw new Exception("not a bst");
        }
    }

    public static void main(String[] args) throws Exception {
        Tree<String, Integer> foo = new Tree<>("S", 5);
        foo.put("E", 5);
        foo.put("A", 10);
        foo.put("C", 11);
        foo.put("R", 8);
        foo.put("H", 3);
        foo.put("M", 7);
        foo.put("L", 5);
        foo.put("P", 4);
        foo.put("X", 0);
        System.out.println(foo);

        foo.changeKey("M", "Ö");
        System.out.println("ordered -> " + foo.isOrdered(foo.root, "C", "D"));
    }

    private boolean isBST() {
        return this.isBinaryTree(root) &&
                this.isOrdered(root, this.min(), this.max()) &&
                this.hasNoDuplicates(root);
    }

    private boolean hasNoDuplicates(Node root) {
        ArrayList<Key> keys = new ArrayList<>();
        return hasNoDuplicates(root, keys);
    }

    private boolean hasNoDuplicates(Node root, ArrayList<Key> keys) {
        if (null == root) {
            return true;
        }
        if (keys.contains(root.key)) {
            return false;
        }
        keys.add(root.key);
        return hasNoDuplicates(root.left, keys) && hasNoDuplicates(root.right, keys);
    }

    private boolean isBinaryTree(Node root) {
        if (root == null) {
            return false;
        }
        return isBinaryTree(root, 0) == root.N;
    }

    private int isBinaryTree(Node root, int n) {
        if (n > root.N) {
            return -1;
        }
        ++n;
        int leftN = 0;
        if (root.left != null) {
            leftN = isBinaryTree(root.left, n);
        }
        int rightN = 0;
        if (root.right != null) {
            rightN = isBinaryTree(root.right, n);
        }
        return n + leftN + rightN;
    }

    public void changeKey(Key old, Key newKey) {
        Node x = root;
        Node parent = x;
        while (x != null) {
            int cmp = old.compareTo(x.key);
            if (cmp == 0) {
                Node newNode = new Node(newKey, x.val, x.N);
                newNode.left = x.left;
                newNode.right = x.right;
                if (parent.left == x) {
                    parent.left = newNode;
                } else {
                    parent.right = newNode;
                }
                return;
            } else if (cmp < 0) {
                parent = x;
                x = x.left;
            } else {
                parent = x;
                x = x.right;
            }
        }
    }

    @Override
    public String toString() {
        return toString(root, 0);
    }

    private String toString(Node node, int level) {
        if (node == null) {
            return "NULL";
        }
        return level + " " + node.key + "-" + node.val + "[" + this.toString(node.left, level + 1) + " " + this.toString(node.right, level + 1) + "]";
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.N;
    }

    public Value get(Key key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) return x.val;
            else if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
        }
        return null;
    }

    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> queue = new Queue<>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key);
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }

    private Value get(Node x, Key key) { // Return value associated with key in the subtree rooted at x;
// return null if key not present in subtree rooted at x.
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.val;
    }

    public void put(Key key, Value val) { // Search for key. Update value if found; grow table if new.
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
// Change key’s value to val if key in subtree rooted at x.
// Otherwise, add new node to subtree associating key with val.
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    private void print(Node x) {
        if (x == null) return;
        print(x.left);
        StdOut.println(x.key);
        print(x.right);
    }

    public void deleteMin() {
        root = deleteMin(root);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node delete(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node t = x;
            x = min(t.right); // See page 407.
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public Key select(int k) {
        return select(root, k).key;
    }

    private Node select(Node x, int k) { // Return Node containing key of rank k.
        if (x == null) return null;
        int t = size(x.left);
        if (t > k) return select(x.left, k);
        else if (t < k) return select(x.right, k - t - 1);
        else return x;
    }

    public int rank(Key key) {
        return rank(key, root);
    }

    private int rank(Key key, Node x) { // Return number of keys less than x.key in the subtree rooted at x.
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return rank(key, x.left);
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
        else return size(x.left);
    }

    public Key min() {
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        return min(x.left);
    }

    public Key max() {
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        return max(x.right);
    }

    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) return null;
        return x.key;
    }

    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.left, key);
        Node t = floor(x.right, key);
        if (t != null) return t;
        else return x;
    }

    public boolean isOrdered(Node root, Key min, Key max) {
        if (root == null) {
            return true;
        }
        Key rootKey = root.key;

        if (rootKey.compareTo(min) >= 0 && rootKey.compareTo(max) <= 0) {
            return isOrdered(root.left, min, max) && isOrdered(root.right, min, max);
        }
        return false;
    }

    private class Node {
        private Key key; // key
        private Value val; // associated value
        private Node left, right; // links to subtrees
        private int N; // # nodes in subtree rooted here

        public Node(Key key, Value val, int N) {
            this.key = key;
            this.val = val;
            this.N = N;
        }
    }
}