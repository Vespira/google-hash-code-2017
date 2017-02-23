package com.company;

public class Main {

    public static void main(String[] args) {

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        ParseInputFile parseInputFile = new ParseInputFile();
        String path = classLoader.getResource("kittens.in").toString();
        if (path.contains("file:/")) {
            path = path.replace("file:/", "");
        }
        parseInputFile.parseFile(path);
    }
}
