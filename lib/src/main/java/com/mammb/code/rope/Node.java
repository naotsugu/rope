package com.mammb.code.rope;

/**
 * Each node further up the tree holds the sum of the lengths of all the leaves in its left subtree.
 * <pre>
 *               Branch
 *                (5)
 *              /     \
 *     Branch (3)      (1) Branch
 *          /   \     /
 *        (3)   (2)  (1)
 *       Leaf  Leaf  Leaf
 *      |abc|  |de|  |f|
 * </pre>
 */
sealed interface Node {

    Node EMPTY = new Leaf("");

    // total length (weight) of the string in the left subtree (or length of the string if it's a leaf).
    int length();

    // recursively calculate the total length of the string represented by this node.
    int totalLength();

    default boolean isEmpty() {
        return totalLength() == 0;
    }

    record Leaf(String text) implements Node {

        @Override
        public int length() {
            return text.length();
        }

        @Override
        public int totalLength() {
            return length();
        }
    }

    record Branch(Node left, Node right, int length) implements Node {

        Branch(Node left, Node right) {
            this(left, right, left.totalLength());
        }

        @Override
        public int totalLength() {
            return left.totalLength() + right.totalLength();
        }

    }

}
