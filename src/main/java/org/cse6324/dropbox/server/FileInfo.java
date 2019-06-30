package org.cse6324.dropbox.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * FileInfo
 */
public class FileInfo {
    String filepath;
    String hash;
    Long lastModified;

    FileInfo(String filepath, String hash, Long lastModified) {
        this.filepath = filepath;
        this.hash = hash;
        this.lastModified = lastModified;
    }

    FileInfo(Path path) {
        filepath = path.toString();
        File f = path.toFile();
        lastModified = f.lastModified();
        hash = generateMD5(path);
    }

    private static String generateMD5(Path path){
        MessageDigest md;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path.toFile());
            md = MessageDigest.getInstance("MD5");
            FileChannel channel = inputStream.getChannel();
            ByteBuffer buff = ByteBuffer.allocate(2048);
            while(channel.read(buff) != -1)
            {
                buff.flip();
                md.update(buff);
                buff.clear();
            }
            byte[] hashValue = md.digest();
            return new String(hashValue);
        }
        catch (NoSuchAlgorithmException e)
        {
            return null;
        } 
        catch (IOException e) 
        {
            return null;
        }
        finally
        {
            try {
                if(inputStream!=null)inputStream.close();
            } catch (IOException e) {
    
            }
        } 
    }

    /**
     * @return the filepath
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * @return the lastModified
     */
    public Long getLastModified() {
        return lastModified;
    }

    static FileInfo example() {
        return new FileInfo("foo/bar", "0x6469796547", Long.valueOf(1));
    }
}