package org.cse6324.dropbox.server;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DirectoryInfoManager
 */
public class DirectoryInfoManager {
    File directory;

    DirectoryInfoManager(String userDataDirectoryPath) {
        directory = new File(userDataDirectoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    FileInfo[] getNewInfo(String userFolder) {
        List<FileInfo> fileInfos = new ArrayList<>();
        try {
            fileInfos = Files.walk(
                Paths.get(directory.getAbsolutePath(), userFolder)
            )
            .filter(Files::isRegularFile)
            .parallel()
            .map(FileInfo::new)
            .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
        }
        return fileInfos.toArray(new FileInfo[fileInfos.size()]);
    }

    FileInfo[] getSavedInfo(String userFolder) {
        return null;
    }

    static DirectoryInfoManager shared = new DirectoryInfoManager("userdata/");
}