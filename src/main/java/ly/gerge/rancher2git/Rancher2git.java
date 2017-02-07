package ly.gerge.rancher2git;

import com.mashape.unirest.http.exceptions.UnirestException;
import ly.gerge.rancher2git.rancher.RancherInstance;
import ly.gerge.rancher2git.rancher.RancherStack;
import ly.gerge.rancher2git.repo.GitRepo;
import ly.gerge.rancher2git.zip.ZipAgent;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;

import javax.naming.AuthenticationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/*** Created by Gergely Mentsik on 05.02.2017. */
public class Rancher2git {
    public static void main(String[] args) {

        System.out.println("Starting up...");
        String rancherApiURL = args[0] + "/stacks";
        String rancherUSER =  args[1];
        String rancherPASS =  args[2];
        String repoURL = args[3];
        String repoUSER =  args[4];
        String repoPASS =  args[5];

        try {
            RancherInstance rancherInstance = new RancherInstance(rancherApiURL,rancherUSER,rancherPASS);

            FileUtils.deleteDirectory(new File("tmp"));
            FileUtils.deleteDirectory(new File("repo"));
            new File("tmp").mkdirs();

            ArrayList<RancherStack> rancherStacks = rancherInstance.fetchRancherStacks();
            rancherInstance.downloadStacks(rancherStacks,"tmp");

            try(GitRepo gitRepo = new GitRepo("repo",repoURL,repoUSER,repoPASS)){
                FileUtils.copyDirectory(new File("repo/.git"), new File("tmp/git"));
            } catch (GitAPIException g){
                if (g.getClass().equals(TransportException.class) ){
                    System.err.println("Could not clone repo, please check your repository URL, Username and Password!");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try{
                FileUtils.cleanDirectory(new File("repo"));
                FileUtils.moveDirectory(new File("tmp/git"),new File("repo/.git"));
            } catch(FileNotFoundException f) {
                System.err.println("Could not clone the repo: " + repoURL );
                return;
            }


            for (RancherStack stack : rancherStacks) {
                new File("repo" + File.separator + stack.getName()).mkdirs();
                ZipAgent.unzip("tmp"+File.separator+stack.getName() + ".zip", "repo" + File.separator +stack.getName());
            }

            try(GitRepo gitRepo = new GitRepo("repo",repoURL,repoUSER,repoPASS)){
                gitRepo.push();
            } catch (GitAPIException g){
                System.err.println("Could not push to repo, please check if " + repoUSER + " is allowed to push to the branch master.");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }
}
