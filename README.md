# 🔍 Code Similarity Detection (Java & C++)

This project is designed for the CS208 course at Southern University of Science and Technology (SUSTech). It provides an automated tool to check code similarity between Java and C++ lab submissions using the [JPlag](https://github.com/jplag/JPlag) framework.

## 📌 Features

- 📁 Automatically classifies .java and .cpp files from student folders.
- 🔍 Performs code similarity detection using JPlag for Java and C++.
- 📄 Generates ZIP reports for easy inspection and grading.

## 📁 Directory Structure
```
.
├── Lab/
│   └── lab3/
│       ├── lab3a/
│       │   └── 20240001/   <- Contains Java/C++ files
│       └── lab3b/
│           └── 20240002/
├── output/
│   └── lab3/
│       ├── lab3a_java_result.zip
│       ├── lab3a_cpp_result.zip
│       └── ...
├── src/
│   ├── Main.java
│   ├── JPlag(.jar dependencies)
│   └── ...
└── README.md
```

## 🚀 How to Use

### 1. Prepare the Input
Organize the lab submissions as follows:
```
Lab/lab3/lab3a/20240001/*.cpp or *.java
Lab/lab3/lab3b/20240002/*.cpp or *.java
```
Each student's folder must be named with an **8-digit** ID (e.g., 20240001).

### 2. Run the program 
This will:

- Automatically separate Java and C++ files into new directories (e.g., lab3a-java/, lab3a-cpp/)
- Perform similarity analysis using JPlag
- Save reports into the output/ directory

### 3. Visualize the output
Run this commands in terminal, then upload the `.zip` report files.
```
cd src
java -jar jplag-6.0.0-jar-with-dependencies.jar
```


## ⚙️ Configuration
To analyze a different lab (e.g., lab4), simply change this line in `Main.java`:
```
String lab_name = "lab3";
```

## 📦 Dependencies
Java 21+

## ❗ Notes
- Only `.java` and `.cpp` files are processed.
- Only first-level subdirectories with 8-digit names will be included.
- Invalid folder names or missing files will be skipped with warnings.
