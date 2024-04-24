package com.project;

import java.io.File;
import java.util.TreeMap;
import java.util.concurrent.RecursiveTask;

public class RecursiveFileSearcher extends RecursiveTask<TreeMap<Long, String>> {
    private final String path;
    private final long fileSize;

    public RecursiveFileSearcher(String path, long fileSize) {
        this.path = path;
        this.fileSize = fileSize;
    }

    @Override
    protected TreeMap<Long, String> compute() {
        TreeMap<Long, String> files = new TreeMap<>();
        File directory = new File(path);
        File[] fileList = directory.listFiles();

        if (fileList != null) {
            for (File file : fileList) {
                if (file.isDirectory()) {
                    RecursiveFileSearcher task = new RecursiveFileSearcher(file.getAbsolutePath(), fileSize);
                    task.fork();
                    files.putAll(task.join());
                } else {
                    if (file.isFile() && file.length() > fileSize) {
                        files.put(file.length(), file.getAbsolutePath());
                    }
                }
            }
        }
        return files;
    }
}