package com.leyou.test.jvmtest;

import java.io.File;

public class DeleteFile {

    public static void main(String[] args) {

        File file = new File("D:\\360_manager");

        deleteFile(file);
    }

    private static void deleteFile(File file) {

        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()){
                    files[i].delete();
                } else {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            file.delete();
        }
    }
}
