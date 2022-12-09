package org.example.utils;

import org.example.CardQuestion;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry
 */
public class Parser {
    public static void main(String[] args) {
        File homeDirectory = FileSystemView.getFileSystemView().getHomeDirectory();
        final File file = new File(homeDirectory.getPath() + File.separator + "lvl1.txt");
        List<CardQuestion> list = parse(file);
    }
    private static List<CardQuestion> parse(File file) {
        return new ArrayList<>();
    }
}
