package com.company;

public class Main {

    public static void main(String[] args) {

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        ParseInputFile parseInputFile = new ParseInputFile();
        parseInputFile.parseFile(classLoader.getResource("kittens.in").toString().replace("file:/", ""));
    }
}
