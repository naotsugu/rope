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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrettyPrinterTest {

    @Test
    void toDot() {
        Rope rope = new Rope()
            .concat(new Rope("aa"))
            .concat(new Rope("bbb"))
            .concat(new Rope("cccc"));

        assertEquals(
            """
            digraph G {
              n0 [label="root\\n5"];
              n1 [label="l\\n2"];
              n2 [label="l\\naa", shape=box];
              n1 -> n2;
              n3 [label="r\\nbbb", shape=box];
              n1 -> n3;
              n0 -> n1;
              n4 [label="r\\ncccc", shape=box];
              n0 -> n4;
            }
            """,
            PrettyPrinter.toDot(rope));
    }

}
