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

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void ofNodes() {

        Node a = new Node.Leaf("a");
        Node b = new Node.Leaf("b");
        Node c = new Node.Leaf("c");
        Node d = new Node.Leaf("d");
        Node e = new Node.Leaf("e");

        //          1
        //       /     \
        //     2         3
        //    /  \     /
        //   4    5    6
        //  / \  / \  / \
        //  a b  c  d e
        var node1 = (Node.Branch) Node.of(List.of(a, b, c, d, e));

        var node2 = left(node1);
        var node3 = right(node1);

        var node4 = left(node2);
        var node5 = right(node2);

        var node6 = left(node3);

        assertEquals(a, node4.left());
        assertEquals(b, node4.right());

        assertEquals(c, node5.left());
        assertEquals(d, node5.right());

        assertEquals(e, node6.left());
        assertEquals(Node.EMPTY, node6.right());

        assertEquals(Node.EMPTY, node3.right());
    }

    private static Node.Branch left(Node node) {
        Node.Branch branch = (Node.Branch) node;
        return (Node.Branch) branch.left();
    }

    private static Node.Branch right(Node node) {
        Node.Branch branch = (Node.Branch) node;
        return (Node.Branch) branch.right();
    }
}
