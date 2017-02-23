package com.company;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Created by mfreche on 23/02/17.
 */
public class FileWriter {

    public void ecrireFichierResultat(String fileName, Map<Cache,List<Video>> resultat)
            throws IOException {
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(String.valueOf(resultat.size()));
            for ( Map.Entry<Cache,List<Video>> entry : resultat.entrySet() ) {
                writer.newLine();
                writer.write(String.valueOf(entry.getKey().getId()));
                for ( Video v : entry.getValue() ) {
                    writer.write(" "+v.getId());
                }
            }
        }

    }
}
