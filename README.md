# Rope

A simple implementation of the Rope data structure in Java.


## Overview

Rope is a tree-based data structure for efficiently storing and manipulating very long strings.
It allows for faster operations like insertion, deletion, and concatenation compared to standard string implementations, especially for large texts.

This library provides a simple and immutable Rope implementation.

*   **Efficient String Manipulation**: Performs fast concatenation, insertion, and deletion operations (typically O(log N)).
*   **Immutability**: All operations return a new Rope instance, ensuring that the original Rope remains unchanged.
*   **Balanced Tree**: The underlying tree is balanced by weight after operations to maintain performance.


## Usage

Here is a simple example of how to use the Rope library:

```java
// Create a Rope
Rope rope = new Rope("Hello, world!");

// Insert a string
// "Hello, beautiful world!"
Rope inserted = rope.insert(7, "beautiful ");

// Delete a section
// "Hello, world!"
Rope deleted = inserted.delete(7, 17);

// Concatenate with another Rope
Rope finalRope = deleted.concat(new Rope(" How are you?"));

// The final string is "Hello, world! How are you?"
System.out.println(finalRope.toString());
```


## Building

To build the library, run the following command:

```bash
./gradlew build
```


## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for details.
