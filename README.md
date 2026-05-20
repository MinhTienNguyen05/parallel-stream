# Parallel Seminar Demo

This project demonstrates sequential and parallel data processing in Java using `Stream` and `parallelStream()` on a Parquet dataset.

The program:

* Loads review data from a `.parquet` file
* Normalizes text
* Counts words
* Calculates SHA-256 hashes to detect spam review
* Measures execution time and throughput
* Compares Sequential vs Parallel processing

---

# Requirements

Before running the project, make sure the following are installed:

* Java 11 (recommended)
* Maven 3.9+
* macOS / Linux / Windows

---

# Recommended Versions

| Tool    | Version |
| ------- | ------- |
| Java    | 11      |
| Maven   | 3.9+    |
| Hadoop  | 3.3.4   |
| Parquet | 1.12.3  |

---

# Check Current Java and Maven Versions

Run:

```bash
java -version
mvn -version
```

Expected output:

```bash
Java version: 11
Apache Maven 3.9.x
```

---

# Common Issue: Maven Uses Wrong Java Version

Sometimes:

```bash
java -version
```

shows Java 11,

but:

```bash
mvn -version
```

shows Java 21 / 22 / 25.

This causes Hadoop compatibility errors such as:

```text
getSubject is not supported
```

or:

```text
invalid target release: 11
```

---

# Fix Java Version for Maven

## Step 1 — Check Installed JDKs

Run:

```bash
/usr/libexec/java_home -V
```

Example:

```bash
11.0.12 (x86_64)
25.0.2 (x86_64)
```

---

## Step 2 — Set Java 11

Run:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
export PATH=$JAVA_HOME/bin:$PATH
```

---

## Step 3 — Verify

Run:

```bash
mvn -version
```

You should see:

```text
Java version: 11
```

---

# Optional: Make Java 11 Permanent

Add the following lines to:

```bash
~/.zshrc
```

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
export PATH=$JAVA_HOME/bin:$PATH
```

Then reload:

```bash
source ~/.zshrc
```

---

# Install Java 11 (Homebrew)

If Java 11 is not installed:

```bash
brew install openjdk@11
```

---

# Project Structure

```text
parallel-demo/
│
├── pom.xml
├── review_philadelphia_500mb.parquet
└── src/
    └── main/
        └── java/
            └── ParallelSeminarDemo.java
```

---

# Run the Project

Compile and run:

```bash
mvn clean compile exec:java
```

---

# Expected Output

```text
Loading data from Parquet file
Number of reviews: 500000

Running: SEQUENTIAL
1. Execution Time: ...
2. Throughput: ...
3. Thread Metrics:
   - [main]: ...

Running: PARALLEL
1. Execution Time: ...
2. Throughput: ...
3. Thread Metrics:
   - [ForkJoinPool.commonPool-worker-1]: ...
```

---

# Notes

## Review Column

The Parquet schema uses the field:

```text
text
```

instead of:

```text
review
```

Therefore, the code reads reviews using:

```java
record.get("text")
```

---

## log4j Warning

You may see:

```text
log4j:WARN No appenders could be found
```

This warning is harmless and does not affect program execution.

---

# Technologies Used

* Java Stream API
* Java parallelStream()
* Apache Parquet
* Apache Hadoop
* Maven
* SHA-256 Hashing

---
