package ly.gerge.rancher2git;

import ly.gerge.rancher2git.repo.GitRepo;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    }

}