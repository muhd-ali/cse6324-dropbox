package org.cse6324.dropbox.server;

import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Server
 */
@RestController
public class Server {
    @GetMapping("/{id}/directoryinfo/existing")
    public FileInfo[] getExistingFilesInfo(@PathVariable("id") String userID) {
        return DirectoryInfoManager.shared.getNewInfo(userID);
    }

    @GetMapping("/{id}/directoryinfo/deleted")
    public FileInfo[] getDeletedFilesInfo(@PathVariable("id") String userID) {
        return DirectoryInfoManager.shared.getNewInfo(userID);
    }

    @GetMapping("/{id}/file/{filepathBase64}")
    public FileSystemResource downloadFile(@PathVariable("id") String userID, @PathVariable String filepathBase64) {
        filepathBase64 = "RTpcV29ya1xDb3Vyc2VzXENTRSA2MzI0IEFkdi4gU29mdHdyLiBFbmdnXFByb2plY3RcZHJvcGJveFx1c2VyZGF0YVx1c2VyMDAwMFxmb2xkZXIxXGV4aXN0aW5nRmlsZS50eHQ=";
        // TODO: Remove this artificial encoded
        String filepath = new String(Base64.getDecoder().decode(filepathBase64));
        return new FileSystemResource(filepath);
    }
}