package ly.gerge.rancher2git.repo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Gergely Mentsik on 05.02.2017
 */

public class GitRepo implements AutoCloseable{
    private File localPath;
    private String remote;
    private Git git;
    private CredentialsProvider cp;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public GitRepo(String localPath, String remote, String username, String password) throws IOException, GitAPIException {
        this.localPath = new File(localPath);
        this.remote = remote;
        cp = new UsernamePasswordCredentialsProvider(username, password);
        initRepo();
    }

    public GitRepo(String localPath, String remote) throws IOException, GitAPIException {
        this.localPath = new File(localPath);
        this.remote = remote;
        initPublicRepo();
    }

    private void initPublicRepo() throws IOException, GitAPIException {
        if(localPath.exists()){
            git = Git.open(localPath);
        }else{
            git = Git.cloneRepository().setURI(remote).setDirectory(new File(localPath.getPath())).call();
        }
    }

    private void initRepo() throws IOException, GitAPIException {
        if(localPath.exists()){
            git = Git.open(localPath);
        }else{
            git = Git.cloneRepository().setURI(remote).setDirectory(new File(localPath.getPath())).setCredentialsProvider(cp).call();
        }

    }

    public void push() throws IOException, GitAPIException {
        git.add().addFilepattern(".").call();
        git.add().setUpdate(true).addFilepattern(".").call();
        Date date = new Date();
        git.commit().setMessage("Snapshot from: " + dateFormat.format(date)).call();
        git.push().setCredentialsProvider(cp).call();
    }

    @Override
    public void close() throws Exception {
        git.close();
    }
}
