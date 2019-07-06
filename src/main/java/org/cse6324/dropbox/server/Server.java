package org.cse6324.dropbox.server;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Server
 */
@RestController
public class Server {
    @GetMapping("/{id}/directoryinfo/existing")
    public FileInfo[] getExistingFilesInfo(@PathVariable("id") String userID) {
        return DirectoryInfoManager.shared.readExisitngFileInfoFor(userID);
    }

    @GetMapping("/{id}/directoryinfo/deleted")
    public FileInfo[] getDeletedFilesInfo(@PathVariable("id") String userID) {
        return DirectoryInfoManager.shared.readDeletedFileInfoFor(userID);
    }

    @GetMapping("/{id}/file/{filepathBase64}")
    public Resource downloadFile(@PathVariable("id") String userID, @PathVariable String filepathBase64, HttpServletResponse response) throws IOException {
        String filepathString = new String(Base64.getDecoder().decode(filepathBase64));
        Path filepath = DirectoryInfoManager.shared.fullPathForUser(userID, filepathString);
        response.setHeader("Content-Disposition", "attachment; filename=" + filepath.getFileName());
        return new FileSystemResource(filepath);
    }

    @DeleteMapping("/{id}/file/{filepathBase64}")
    public ResponseEntity<?> deleteFile(@PathVariable("id") String userID, @PathVariable String filepathBase64) {
        String filepath = new String(Base64.getDecoder().decode(filepathBase64));
        if (DirectoryInfoManager.shared.deleteUserFile(userID, filepath)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/{id}/file/{filepathBase64}")
    public ResponseEntity<?> singleFileUpload(
        @PathVariable("id") String userID,
        @PathVariable("filepathBase64") String filepathBase64,
        @RequestParam("file") MultipartFile file,
        RedirectAttributes redirectAttributes
    ) {
        String filepath = new String(Base64.getDecoder().decode(filepathBase64));
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        if (DirectoryInfoManager.shared.saveUserFile(userID, file, filepath)) {
            redirectAttributes.addFlashAttribute("message",
            "You successfully uploaded '" + file.getOriginalFilename() + "'");
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }
}