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
            .split(16);;

        assertEquals(
            "The quick brown ",
            ropes[0].toString());

        assertEquals(
            "fox jumps over the lazy dog.",
            ropes[1].toString());
    }

    @Test
    void insert() {
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

    }

}
