package io.spring.initializr.app.utils;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import io.spring.initializr.app.exception.DirCreateException;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.stream.Collectors;

public class FileUtils {

    public static File createDir(String path) {
        File dir = new File(path);
        boolean rootVali = true;
        if (!dir.exists())
            rootVali = dir.mkdirs();
        if (!rootVali || !dir.isDirectory())
            throw new DirCreateException(path);
        return dir;
    }

    public static void cleanDir(File dir) {
        File[] subs = dir.listFiles();
        if (subs == null)
            return;
        for (File file : subs) {
            FileSystemUtils.deleteRecursively(file);
        }
    }

    public static File[] searchFiles(File root, String[] patterns) throws IOException {
        return Files.find(root.toPath(), Integer.MAX_VALUE, (path, basicFileAttributes) -> {
            if (!basicFileAttributes.isRegularFile())
                return false;
            for (String pattern : patterns) {
                if (FileSystems.getDefault().getPathMatcher(pattern).matches(path)) {
                    return true;
                }
            }
            return false;
        }).map(Path::toFile).collect(Collectors.toList()).toArray(new File[]{});
    }

    public static void copyRecursivelyWithRules(File src, File dest, FileFilter includeFilter, FileFilter excludeFilter) throws IOException {
        Assert.notNull(src, "Source File must not be null");
        Assert.notNull(dest, "Destination File must not be null");
        Assert.isTrue(src.isDirectory(), "Source file must be a directory");
        Assert.isTrue(dest.isDirectory(), "Destination file must be a directory");

        Path srcPath = src.toPath();
        Path destPath = dest.toPath();

        Files.walkFileTree(srcPath, new CopyWithRulesFileVisitor(srcPath, destPath, includeFilter,
                excludeFilter, new FilePairOperator() {
            @Override
            public FileVisitResult operate(Path dest, Path file) throws IOException {
                Files.copy(file, dest, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        }));
    }

    public static void templateRecursivelyWithRules(File src, File dest, FileFilter includeFilter, FileFilter excludeFilter, Map<String, Object> model) throws IOException {
        Assert.notNull(src, "Source File must not be null");
        Assert.notNull(dest, "Destination File must not be null");
        Assert.isTrue(src.isDirectory(), String.format("Source file {%s} must be a directory", src));
        Assert.isTrue(dest.isDirectory(), String.format("Destination file {%s} must be a directory", dest));

        Path srcPath = src.toPath();
        Path destPath = dest.toPath();

        Files.walkFileTree(srcPath, new CopyWithRulesFileVisitor(srcPath, destPath, includeFilter,
                excludeFilter, new FilePairOperator() {
            @Override
            public FileVisitResult operate(Path dest, Path file) throws IOException {
                Template template = Mustache.compiler().compile(new String(FileCopyUtils.copyToByteArray(file.toFile())));
                String body = template.execute(model);
                if (dest.toFile().exists()){
                    Files.delete(dest);
                }
                FileCopyUtils.copy(body.getBytes(), dest.toFile());
                return FileVisitResult.CONTINUE;
            }
        }));
    }

    static class CopyWithRulesFileVisitor extends SimpleFileVisitor<Path> {

        private Path srcPath;

        private Path destPath;

        private FileFilter includeFilter;

        private FileFilter excludeFilter;

        private FilePairOperator filePairOperator;

        public CopyWithRulesFileVisitor(Path srcPath, Path destPath, FileFilter includeFilter, FileFilter excludeFilter, FilePairOperator filePairOperator) {
            this.srcPath = srcPath;
            this.destPath = destPath;
            this.includeFilter = includeFilter;
            this.excludeFilter = excludeFilter;
            this.filePairOperator = filePairOperator;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (includeFilter != null && !includeFilter.accept(dir.toFile()))
                return FileVisitResult.CONTINUE;
            if (excludeFilter != null && excludeFilter.accept(dir.toFile()))
                return FileVisitResult.CONTINUE;
            Files.createDirectories(destPath.resolve(srcPath.relativize(dir)));
            return FileVisitResult.CONTINUE;
        }
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (includeFilter != null && !includeFilter.accept(file.toFile()))
                return FileVisitResult.CONTINUE;
            if (excludeFilter != null && excludeFilter.accept(file.toFile()))
                return FileVisitResult.CONTINUE;
            return this.filePairOperator.operate(destPath.resolve(srcPath.relativize(file)), file);
        }
    }

    interface FilePairOperator {
        public FileVisitResult operate(Path desc, Path file) throws IOException;
    }

    public static void templateFile(File template, File dest) {

    }

}
