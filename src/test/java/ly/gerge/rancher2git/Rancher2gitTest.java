package ly.gerge.rancher2git;

import ly.gerge.rancher2git.repo.GitRepo;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

/*** Created by Gergely Mentsik on 17.02.2017. */

class Rancher2gitTest {
    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    //Clone https://github.com/twbs/bootstrap.git
    @Test
    void test_CloneRepo() throws Exception {
        String testRepo = "https://github.com/twbs/bootstrap.git";
        String tmpDir = "test-repo";

        GitRepo gitRepo = new GitRepo(tmpDir,testRepo);
        gitRepo.close();
        assertTrue(Files.isDirectory(new File(tmpDir+"/.git").toPath()));

        FileUtils.cleanDirectory(new File(tmpDir));
    }

}