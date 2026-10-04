# DES / 3DES Encryption Tool

A Java desktop application that implements the **Data Encryption
Standard (DES)** from scratch and extends it to **Triple DES (3DES)**.
The project demonstrates block-cipher internals, object-oriented design,
padding, concurrent block processing, benchmarking, file handling, and a
graphical user interface built with Java Swing.

> **Educational project:** DES is obsolete, and 3DES is deprecated. This
> application is intended for learning and experimentation, not for
> protecting sensitive or production data.

## Features

-   **DES implemented from scratch**
    -   Initial and final permutations
    -   16-round Feistel network
    -   Expansion and permutation operations
    -   S-box substitution
    -   DES key schedule and round-key generation
-   **Triple DES (3DES)**
    -   EDE sequence: DES encryption with K1, DES decryption with K2,
        and DES encryption with K3
    -   Corresponding reverse sequence for decryption
-   **Padding**
    -   PKCS#5-style padding for data that does not fill a complete
        8-byte block
    -   Padding abstraction to keep the service layer extensible
-   **Parallel processing**
    -   Fixed thread pool using `ExecutorService`
    -   Per-block tasks implemented with `Callable` and lambda
        expressions
    -   `Future` results collected in input order
-   **Benchmarking**
    -   Sequential versus parallel encryption comparison
    -   Warm-up and repeated runs
    -   Average execution time and speedup calculation
    -   Output comparison to check correctness
-   **Swing GUI**
    -   Text encryption and decryption using DES or 3DES
    -   Hexadecimal key input and ciphertext display
    -   File selection and 3DES file encryption/decryption

## Technologies

-   Java
-   Java Swing
-   Java Concurrency API (`ExecutorService`, `Callable`, `Future`)
-   Object-oriented programming
-   File I/O and byte/hexadecimal conversion

## Project Structure

The exact package layout may vary as the project evolves. A
representative structure is:

``` text
src/
├── core/
│   ├── DES.java
│   ├── DESConstants.java
│   ├── Permutation.java
│   └── TripleDES.java
├── block/
│   ├── Block.java
│   └── BlockProcessor.java
├── key/
│   ├── DESKeySchedule.java     
├── padding/
│   ├── Padding.java
│   └── PKCS5Padding.java
├── crypto/
│   ├── FeistelFunction.java
│   └── SBox.java
├── service/
│   ├── DESService.java
│   └── TripleDESService.java
├── thread/
│   └── ParallelBlockProcessor.java
├── test/
│   ├── TripleDESTest.java
│   ├── TripleDESBenchmark.java
│   ├── DESBenchmarkTest.java
│   └── DESTest.java
├── file/
│   └── FileEncryptionService.java
├── gui/
│   └── EncryptionGUI.java
```

### Architecture

``` text
                         Swing GUI
                            |
                   Service Layer
                    /           \
              DESService    TripleDESService
                    \           /
                     Block Processing
                            |
                 ParallelBlockProcessor
                            |
                      DES / 3DES
                            |
                    64-bit data blocks
```

The GUI handles user interaction, the service classes coordinate padding
and encryption workflows, block processors handle block conversion and
processing, and the DES/3DES classes contain the cryptographic
operations.

## Getting Started

### Requirements

-   JDK 17 or a compatible Java version
-   An IDE such as IntelliJ IDEA, Eclipse, or VS Code, or a terminal
    with `javac` and `java`

### Run the application

1.  Clone the repository:

    ``` bash
    git clone <repository-url>
    cd <repository-folder>
    ```

2.  Open the project in your Java IDE, or compile the source files using
    your IDE's build configuration.

3.  Run the GUI entry point:

    ``` text
    gui.EncryptionGUI
    ```

If you use a terminal, compile the project according to your package
layout and run the fully qualified main class. Replace the repository
placeholders above with your actual repository URL and folder name.

## Using the GUI

### Text mode

1.  Select **Text** mode.
2.  Choose **DES** or **3DES**.
3.  Enter the key or keys as hexadecimal values.
4.  Enter the plaintext and click **Encrypt**.
5.  Copy the hexadecimal output.
6.  To decrypt, paste the hexadecimal ciphertext into the input area,
    provide the same key or keys, and click **Decrypt**.

### File mode

1.  Select **File** mode.
2.  Choose the file you want to process.
3.  Provide the 3DES keys.
4.  Choose the output location when prompted.
5.  Use **Encrypt** or **Decrypt** to process the file.

File processing currently reads the entire file into memory. Very large
files may require a streaming implementation.

## Key Concepts Demonstrated

### DES

DES operates on 64-bit blocks. Its key schedule derives 16 round keys,
and its Feistel structure applies 16 rounds of expansion, key mixing,
substitution, and permutation.

### 3DES

The project uses the EDE construction:

``` text
Plaintext
    |
DES Encrypt with K1
    |
DES Decrypt with K2
    |
DES Encrypt with K3
    |
Ciphertext
```

### Parallel block processing

The parallel processor submits independent block operations to a fixed
thread pool. Each task returns its processed block, and results are
copied back in the original block order. This preserves output ordering
even when tasks complete at different times.

Parallel execution is not guaranteed to be faster for every input size.
Task scheduling and coordination add overhead, so the benchmark compares
measured performance rather than assuming a speedup.

## Benchmarking

The benchmark compares sequential and parallel 3DES encryption over a
sample input, performs warm-up runs, repeats measurements, and checks
that both approaches produce identical output.

Speedup is calculated as:

``` text
Speedup = Sequential Average Time / Parallel Average Time
```

Actual results depend on the CPU, JVM, thread count, input size, and
system load.

## Validation

The DES implementation was tested against the standard known-answer
vector:

Input                 Value
  --------------------- --------------------
Plaintext             `0123456789ABCDEF`
Key                   `133457799BBCDFF1`
Expected ciphertext   `85E813540F0AB405`

This test checks the core DES block operation. Additional tests should
cover 3DES vectors, padding edge cases, empty inputs, invalid ciphertext
lengths, file round trips, and parallel-versus-sequential output
equality.

## Limitations and Security Notes

-   DES has an effective key size of only 56 bits and is not secure
    against modern attacks.
-   3DES is deprecated and should not be selected for new
    security-sensitive systems.
-   This project is designed to explain cryptographic algorithms and
    Java application architecture.
-   The current design uses manually entered hexadecimal keys and does
    not provide production-grade key management.
-   The file service reads the full file into memory rather than
    streaming it.
-   The described block-level parallel processing is suitable for
    independent block operations; modes such as CBC encryption have
    dependencies between blocks and cannot be parallelized in the same
    way.

For real applications, use a modern authenticated encryption scheme such
as **AES-GCM**, along with secure key management and appropriate nonce
handling.

## Future Improvements

-   Add streaming file encryption and decryption for large files.
-   Add automated unit tests for DES, 3DES, padding, and file
    operations.
-   Improve key validation and error messages in the GUI.
-   Add progress reporting and background execution so long operations
    do not freeze the Swing interface.
-   Add a modern authenticated encryption option such as AES-GCM.
-   Document benchmark results for different input sizes and thread
    counts.

## Learning Outcomes

Through this project, I practiced:

-   Implementing a cryptographic algorithm from its mathematical and
    procedural steps
-   Designing modular Java classes and service abstractions
-   Using Java concurrency tools for independent block processing
-   Measuring performance and verifying parallel output correctness
-   Working with binary data, hexadecimal representation, and file I/O
-   Building an event-driven desktop interface with Java Swing

