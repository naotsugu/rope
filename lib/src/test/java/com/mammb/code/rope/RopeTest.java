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

import static org.junit.jupiter.api.Assertions.*;

class RopeTest {

    @Test
    void concat() {
        Rope rope = new Rope()
            .concat(new Rope("The quick brown "))
            .concat(new Rope("fox jumps over "))
            .concat(new Rope("the lazy dog."));

        assertEquals(
            "The quick brown fox jumps over the lazy dog.",
            rope.toString());
    }

    @Test
    void split() {
        Rope[] ropes = new Rope()
            .concat(new Rope("The quick brown "))
            .concat(new Rope("fox jumps over "))
            .concat(new Rope("the lazy dog."))
            .split(16);

        assertEquals(
            "The quick brown ",
            ropes[0].toString());

        assertEquals(
            "fox jumps over the lazy dog.",
            ropes[1].toString());
    }

    @Test
    void insertAndDelete() {
        Rope rope = new Rope()
            .insert( 0, "The quick brown ")
            .insert(16, "fox jumps over ")
            .insert(31, "the lazy dog.")
            .insert(10, "very ");

        assertEquals(
            "The quick very brown fox jumps over the lazy dog.",
            rope.toString());

        Rope del = rope.delete(10, 15);
        assertEquals(
            "The quick brown fox jumps over the lazy dog.",
            del.toString());

        // CoW
        assertEquals(
            "The quick very brown fox jumps over the lazy dog.",
            rope.toString());
    }

}
