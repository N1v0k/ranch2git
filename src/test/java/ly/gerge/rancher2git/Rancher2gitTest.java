package ly.gerge.rancher2git;

import ly.gerge.rancher2git.repo.GitRepo;

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

/*** Created by Gergely Mentsik on 17.02.2017. */

public class Rancher2gitTest {
    private String testDir= "tests";

    public Rancher2gitTest() {
    }

    //Clone https://github.com/twbs/bootstrap.git
    @Test
    public void test_CloneRepo() throws Exception {
        String testRepo = "https://github.com/twbs/bootstrap.git";
        String tmpDir = testDir + "/test-repo";

        GitRepo gitRepo = new GitRepo(tmpDir,testRepo);
        gitRepo.close();
        assertTrue(Files.isDirectory(new File(tmpDir+"/.git").toPath()));
    }

}