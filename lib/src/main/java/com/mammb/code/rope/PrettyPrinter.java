/*
 * Copyright 2025-2026 the original author or authors.
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

public interface PrettyPrinter {

    static String toDot(Rope rope) {
        return new RopeDotWalker().walk(rope.root());
    }

    class RopeDotWalker {

        private final StringBuilder sb = new StringBuilder();
        private int count = 0;

        private RopeDotWalker() {
            sb.append("digraph G {\n");
        }

        private String walk(Node node) {
            node(node, "root");
            return sb.append("}\n").toString();
        }

        private String node(Node node, String name) {
            return switch (node) {
                case Node.Leaf leaf -> leaf(leaf, name);
                case Node.Branch branch -> branch(branch, name);
            };
        }

        private String branch(Node.Branch branch, String name) {
            String id = "n" + (count++);
            sb.append("  ").append(id).append(" [label=\"").append(name).append("\\n").append(branch.weight()).append("\"];\n");
            String leftId = node(branch.left(), "l");
            sb.append("  ").append(id).append(" -> ").append(leftId).append(";\n");
            String rightId = node(branch.right(), "r");
            sb.append("  ").append(id).append(" -> ").append(rightId).append(";\n");
            return id;
        }

        private String leaf(Node.Leaf leaf, String name) {
            String id = "n" + (count++);
            sb.append("  ").append(id).append(" [label=\"").append(name).append("\\n").append(leaf.text()).append("\", shape=box];\n");
            return id;
        }
    }

}
