/*
 * Copyright 2025-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mammb.code.rope;

import com.mammb.code.rope.Node.Branch;

interface Balance {

    static Balance empty() { return node -> node; }

    /**
     * Apply balancing strategy.
     * @param node the node
     * @return balanced node
     */
    Node apply(Node node);

    static Balance weightBased() {

        return new Balance() {

            @Override
            public Node apply(Node node) {
                return switch (node) {
                    case Branch branch -> apply(branch);
                    default -> node;
                };
            }

            private Node apply(Branch node) {
                if (!isUnbalanced(node)) return node;

                double total = node.totalLength();
                double leftRatio = (double) node.left().totalLength() / total;

                // Left-heavy case (needs Right rotation)
                if (leftRatio > 0.618) {
                    // L-R case (double rotation)
                    if (node.left() instanceof Branch left &&
                        left.right().totalLength() > left.left().totalLength()) {
                        Node newLeft = rotateLeft(left);
                        Branch tempNode = new Branch(newLeft, node.right());
                        return rotateRight(tempNode);
                    }
                    // L-L case (Single Right rotation)
                    return rotateRight(node);
                }
                // Right-heavy case (needs Left rotation)
                else if (leftRatio < 0.382) {
                    // R-L case (Double rotation)
                    if (node.right() instanceof Branch right &&
                        right.left().totalLength() > right.right().totalLength()) {
                        Node newRight = rotateRight(node.right());
                        Branch tempNode = new Branch(node.left(), newRight);
                        return rotateLeft(tempNode);
                    }
                    // R-R case (Single Left rotation)
                    return rotateLeft(node);
                }

                return node;
            }

            boolean isUnbalanced(Node node) {
                if (node instanceof Branch branch) {
                    double total = branch.totalLength();
                    double leftRatio = (double) branch.left().totalLength() / total;

                    final double PHI_INV = 0.382; // 1/phi
                    return leftRatio < PHI_INV || leftRatio > (1.0 - PHI_INV);
                }
                return false;
            }
        };
    }

    /**
     * Rotate right.
     * <pre>
     *      P          Q
     *     / \        / \
     *    Q   C  ->  A   P
     *   / \            / \
     *  A   B          B   C
     * </pre>
     */
    private static Node rotateRight(Node node) {
        if (node instanceof Branch P && P.left() instanceof Branch Q) {
            Node B = Q.right();
            // new P node: Left=B, Right=P.right (shared)
            Node newP = new Branch(B, P.right());
            // new Q node (new root): Left=Q.left (shared), Right=newP
            Node newQ = new Branch(Q.left(), newP);
            return newQ;
        }
        return node;
    }

    /**
     * Rotate left.
     * <pre>
     *      P            Q
     *     / \          / \
     *    A   Q    ->  P   C
     *       / \      / \
     *      B   C    A   B
     * </pre>
     */
    private static Node rotateLeft(Branch node) {
        if (node instanceof Branch P && P.right() instanceof Branch Q) {
            Node B = Q.left();
            // new P node: Left=P.left (shared), Right=B
            Node newP = new Branch(P.left(), B);
            // new Q node (new root): Left=newP, Right=Q.right (shared)
            Node newQ = new Branch(newP, Q.right());
            return newQ;
        }
        return node;
    }

}
