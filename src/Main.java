import de.jplag.JPlag;
import de.jplag.JPlagResult;
import de.jplag.cpp.CPPLanguage;
import de.jplag.exceptions.ExitException;
import de.jplag.java.JavaLanguage;
import de.jplag.options.JPlagOptions;
import de.jplag.reporting.reportobject.ReportObjectFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Set;

public class Main {

    public static int countFiles(String directoryPath) {
        int count = 0;
        File directory = new File(directoryPath);
        File[] folders = directory.listFiles(File::isDirectory);

        if (folders == null) return 0;

        for (File folder : folders) {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".java") || name.endsWith(".cpp"));
            if (files != null) {
                count += files.length;
            }
        }
        return count;
    }

    public static void copyFilesByType(String sourceDir, String targetJava, String targetCpp) {
        File src = new File(sourceDir);

        int beforeCount = countFiles(sourceDir);

        File[] folders = src.listFiles(File::isDirectory);
        if (folders == null) {
            System.out.println("No folders found");
            return;
        }

        for (File folder : folders) {
            String folderName = folder.getName();
            if (!folderName.matches("\\d{8}")) continue;

            File[] files = folder.listFiles();
            if (files == null) continue;

            for (File file : files) {
                String fileName = file.getName();
                if (fileName.endsWith(".java")) {
                    File destDir = new File(targetJava, folderName);
                    destDir.mkdirs();
                    try {
                        Files.copy(file.toPath(), new File(destDir, fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.err.println("无法复制 Java 文件：" + file.getAbsolutePath());
                        e.printStackTrace();
                    }
                } else if (fileName.endsWith(".cpp")) {
                    File destDir = new File(targetCpp, folderName);
                    destDir.mkdirs();
                    try {
                        Files.copy(file.toPath(), new File(destDir, fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.err.println("无法复制 C++ 文件：" + file.getAbsolutePath());
                        e.printStackTrace();
                    }
                }
            }
        }

        int afterJavaCount = countFiles(targetJava);
        int afterCppCount = countFiles(targetCpp);

        System.out.println("处理前文件总数: " + beforeCount);
        System.out.println("处理后 Java 文件总数: " + afterJavaCount);
        System.out.println("处理后 C++ 文件总数: " + afterCppCount);
    }


    public static void check_java(String directoryPath, String outputPath, JavaLanguage javaLanguage) {
        Set<File> submissionDirectories = Set.of(new File(directoryPath));
        JPlagOptions options = new JPlagOptions(javaLanguage, submissionDirectories, Set.of());
        run(options, outputPath);
    }


    public static void check_cpp(String directoryPath, String outputPath, CPPLanguage cppLanguage) {
        Set<File> submissionDirectories = Set.of(new File(directoryPath));
        JPlagOptions options = new JPlagOptions(cppLanguage, submissionDirectories, Set.of());
        run(options, outputPath);
    }


    public static void run(JPlagOptions options, String outputPath) {
        try {
            JPlagResult result = JPlag.run(options);
            File outputDir = new File(outputPath);
            ReportObjectFactory reportObjectFactory = new ReportObjectFactory(outputDir);
            reportObjectFactory.createAndSaveReport(result);
            System.out.println("Done!");
            System.out.println("Report saved to " + outputDir.getAbsolutePath());
        } catch (FileNotFoundException | ExitException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        // Lab name
        String lab_name = "lab3";
        String lab_a = lab_name + "a";
        String lab_b = lab_name + "b";

        String a_sourceDir = "Lab/" + lab_name + "/" + lab_a;
        String a_targetJavaDir = a_sourceDir + "-java";
        String a_targetCppDir = a_sourceDir + "-cpp";

        String b_sourceDir = "Lab/" + lab_name + "/" + lab_b;
        String b_targetJavaDir = b_sourceDir + "-java";
        String b_targetCppDir = b_sourceDir + "-cpp";

        // java and cpp files classifier
        copyFilesByType(a_sourceDir, a_targetJavaDir, a_targetCppDir);
        copyFilesByType(b_sourceDir, b_targetJavaDir, b_targetCppDir);
        System.out.println("文件分类完成！");

        // similarity detection
        JavaLanguage javaLanguage = new JavaLanguage();
        javaLanguage.getOptions();
        CPPLanguage cppLanguage = new CPPLanguage();
        cppLanguage.getOptions();

        String cpp_result = "_cpp_result.zip";
        String java_result = "_java_result.zip";
        String a_outputDir = "output/" + lab_name + "/" + lab_a;
        String b_outputDir = "output/" + lab_name + "/" + lab_b;

        //cpp
        check_cpp(a_targetCppDir, a_outputDir + cpp_result, cppLanguage);
        check_cpp(b_targetCppDir, b_outputDir + cpp_result, cppLanguage);

        //java
        check_java(a_targetJavaDir, a_outputDir + java_result, javaLanguage);
        check_java(b_targetJavaDir, b_outputDir + java_result, javaLanguage);
    }
}
