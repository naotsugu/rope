package com.mammb.code.rope;

import com.mammb.code.rope.Node.Branch;
import com.mammb.code.rope.Node.Leaf;

public class Rope {

    private static final Rope EMPTY = new Rope("");
    private static final Balance balance = Balance.weightBased();

    private final Node root;

    private Rope(Node root) {
        this.root = (root == null) ? Node.EMPTY : root;
    }

    public Rope() {
        this.root = Node.EMPTY;
    }

    public Rope(String text) {
        this.root = (text == null || text.isEmpty()) ? Node.EMPTY : new Node.Leaf(text);
    }

    public Rope insert(int index, String str) {
        if (str.isEmpty()) return this;

        // split existing rope
        Rope[] parts = split(index);
        Rope leftPart = parts[0];
        Rope rightPart = parts[1];

        // concatenate (concatenate handles balancing)
        Rope insertRope = new Rope(str);
        return leftPart.concat(insertRope).concat(rightPart);
    }

    public Rope delete(int start, int end) {
        if (start < 0 || end > root.totalLength() || start > end) {
            throw new IndexOutOfBoundsException("Invalid range for deletion.");
        }
        if (start == end) return this;

        // split at start
        Rope[] parts1 = split(start);
        Rope leftPart = parts1[0];
        Rope rightPartAndDeleted = parts1[1];

        // split the second part at (end - start) to get the final right part
        Rope[] parts2 = rightPartAndDeleted.split(end - start);
        Rope retainedRightPart = parts2[1];

        // concatenate the two retained parts (concatenate handles balancing)
        return leftPart.concat(retainedRightPart);
    }

    public Rope concat(Rope that) {
        if (this.root.isEmpty()) return that;
        if (that.root.isEmpty()) return this;

        // concatenates this Rope with another Rope (O(1) to O(log N) with balancing)
        //                         newRoot
        //                          /   \
        //      A      D     ->    A      D
        //     / \    / \         / \    / \
        //    B   C  E   F       B   C  E   F
        Branch newRoot = new Branch(this.root, that.root);

        return new Rope(balance.apply(newRoot));
    }

    public Rope[] split(int index) {
        if (index < 0 || index > root.totalLength()) {
            throw new IndexOutOfBoundsException("index out of bounds for split.");
        }
        if (index == 0) {
            return new Rope[] { EMPTY, this };
        }
        if (index == root.totalLength()) {
            return new Rope[] { this, EMPTY };
        }

        // splits the rope at the given index, returning [leftRope, rightRope] (O(log N)).
        Node[] resultNodes = splitNode(root, index);
        // no explicit root balancing here; it's handled by subsequent concat/insert/delete.
        return new Rope[] { new Rope(resultNodes[0]), new Rope(resultNodes[1]) };
    }

    private Node[] splitNode(Node node, int index) {
        // recursive split function (O(log N))
        return switch (node) {
            case Leaf leaf     -> splitNode(leaf, index);
            case Branch branch -> splitNode(branch, index);
        };
    }

    private Node[] splitNode(Leaf leaf, int index) {
        // split the string and create new leaf nodes (CoW)
        //  ---------          ------      -------
        //  | abcde |    ->    | ab |      | cde |
        //  ---------          ------      -------
        //     leaf           leftPart    rightPart
        String leftText  = leaf.text().substring(0, index);
        String rightText = leaf.text().substring(index);

        Node leftPart  = leftText.isEmpty()  ? Node.EMPTY : new Leaf(leftText);
        Node rightPart = rightText.isEmpty() ? Node.EMPTY : new Leaf(rightText);
        return new Node[] { leftPart, rightPart };
    }

    private Node[] splitNode(Branch node, int index) {

        if (index < node.length()) {

            //      A        ->        A       /
            //    /   \              /   \    /
            //   B   node           B   node /         ->               rightPart
            //       /   \               /  /    \                       /  \
            //      D     E             D  /      E         leftPart    G    E
            //     / \   / \          /   /   \   / \         /             / \
            //     F  G  H  I        F   /     G  H  I       F              H  I

            // split point is in the left subtree
            Node[] leftSplit = splitNode(node.left(), index);

            Node leftPart = leftSplit[0];
            // new right part: split-right-of-left + original right (shared)
            Node rightPart = balance.apply(new Branch(leftSplit[1], node.right()));

            return new Node[] { leftPart, rightPart };

        } else {

            //      A        ->        A
            //    /   \              /   \
            //   B     C            B     C       /    ->      leftPart
            //       /   \              /   \    /               /  \
            //      D     E            D        /   E           D    H    rightPart
            //     / \   / \          / \   /  /     \         / \            \
            //     F  G  H  I        F   G  H /       I       F   G            I

            // split point is in the right subtree
            int rightIndex = index - node.length();
            Node[] rightSplit = splitNode(node.right(), rightIndex);

            // new left part: original left (shared) + split-left-of-right
            Node leftPart = balance.apply(new Branch(node.left(), rightSplit[0]));

            Node rightPart = rightSplit[1];

            return new Node[] { leftPart, rightPart };

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        inOrderTraversal(root, sb);
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        switch (node) {
            case Leaf leaf -> sb.append(leaf.text());
            case Branch branch -> {
                inOrderTraversal(branch.left(), sb);
                inOrderTraversal(branch.right(), sb);
            }
        }
    }

}
