package ly.gerge.rancher2git.zip;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Created by gerge on 05.02.2017.
 */
public class ZipAgent {

    public static void unzip(String source, String destination) throws ZipException {
        new ZipFile(source).extractAll(destination);
    }
}
