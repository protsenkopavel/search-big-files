package com.project;

import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        String path;
        long fileSize;

        if (args.length == 2) {
            path = args[0];
            fileSize = Long.parseLong(args[1]) * 1024 * 1024;
        } else {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter the path to the directory: ");
            path = scanner.nextLine();

            System.out.print("Enter the file size in megabytes: ");
            fileSize = scanner.nextLong() * 1024 * 1024;

            scanner.close();
        }

        TreeMap<Long, String> files = new ForkJoinPool().invoke(new RecursiveFileSearcher(path, fileSize));

        if (files.isEmpty()) {
            System.out.println("No files found exceeding the specified size.");
        } else {
            files.forEach((size, filePath) -> {
                System.out.println(formatSize(size) + "\t" + filePath);
            });
        }
    }

    private static String formatSize(long size) {
        String[] units = {" bytes", " KB", " MB", " GB", " TB"};
        int unitIndex = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.1f%s", size / Math.pow(1024, unitIndex), units[unitIndex]);
    }
}