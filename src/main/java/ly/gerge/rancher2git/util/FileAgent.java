package ly.gerge.rancher2git.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/*** Created by Gergely Mentsik on 07.02.2017. */

public class FileAgent {

    public static long downloadFromWebResource(String webURL, String destinationPath, String authUser, String authPass) throws IOException {
        if(authUser != null && authPass != null){
            Authenticator.setDefault (new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {return new PasswordAuthentication(authUser,authPass.toCharArray());}
            });
        }

        URL website = new URL(webURL);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(destinationPath);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        return new File(destinationPath).length();
    }

    public static long downloadFromWebResource(String webURL, String destinationPath) throws IOException {
        URL website = new URL(webURL);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(destinationPath);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        return new File(destinationPath).length();
    }

    public static void unzip(String source, String destination) throws ZipException {
        new ZipFile(source).extractAll(destination);
    }
}
