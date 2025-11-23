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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Gatherers;

/**
 * Each node further up the tree holds the sum of the lengths of all the leaves in its left subtree.
 * <pre>
 *               Branch                 navigate index: 4
 *                (5)                           (5)     (4 < 5)  go to the left child
 *              /     \                       /         4
 *     Branch (3)      (1) Branch           (3)         (4 >= 3) go to the right child and 4 - 3 -> index: 1
 *          /   \     /   \                    \        1
 *        (3)   (2)  (1)   (5)                 (2)
 *       Leaf  Leaf  Leaf  Leaf             | d | e |
 *      |abc|  |de|  |f|  |ghijk|           | 0 | 1 |  index: 1 -> [e]
 * </pre>
 */
sealed interface Node {

    Node EMPTY = new Leaf("");

    // total length of the string in the left subtree (or length of the string if it's a leaf).
    int weight();

    // recursively calculate the total length of the string represented by this node.
    int totalLength();

    default boolean isEmpty() {
        return totalLength() == 0;
    }

    static Node of(CharSequence text) {
        if (text == null || text.isEmpty()) return EMPTY;

        int leafSize = 512;
        if (text.length() <= leafSize) {
            return new Leaf(text.toString());
        }

        List<Node> list = new ArrayList<>();
        for (int i = 0; i < text.length(); i += leafSize) {
            var sub = text.subSequence(i, Math.min(i + leafSize, text.length()));
            list.add(new Leaf(sub.toString()));
        }
        if (list.size() % 2 != 0) list.add(EMPTY);

        return of(list);
    }

    static Node of(Collection<Node> nodes) {
        return fold(nodes).stream().findFirst().orElse(EMPTY);
    }

    private static Collection<Node> fold(Collection<Node> nodes) {

        if (nodes == null || nodes.isEmpty()) return Collections.emptyList();
        if (nodes.size() == 1) return nodes;
        if (nodes.size() % 2 != 0) {
            nodes = new ArrayList<>(nodes);
            nodes.add(EMPTY);
        }

        var folded = nodes.stream()
            .gather(Gatherers.windowFixed(2))
            .map(pair -> new Branch(pair.get(0), pair.get(1)))
            .map(Node.class::cast)
            .toList();

        return fold(folded);
    }

    record Leaf(String text) implements Node {

        @Override
        public int weight() {
            return text.length();
        }

        @Override
        public int totalLength() {
            return weight();
        }
    }

    record Branch(Node left, Node right, int weight) implements Node {

        Branch(Node left, Node right) {
            this(left, right, left.totalLength());
        }

        @Override
        public int totalLength() {
            return left.totalLength() + right.totalLength();
        }

    }

}
