package ly.gerge.rancher2git;

import com.mashape.unirest.http.exceptions.UnirestException;
import ly.gerge.rancher2git.rancher.RancherInstance;
import ly.gerge.rancher2git.rancher.RancherStack;
import ly.gerge.rancher2git.repo.GitRepo;
import ly.gerge.rancher2git.util.FileAgent;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import javax.naming.AuthenticationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;

/*** Created by Gergely Mentsik on 05.02.2017. */
public class Rancher2git {
    public static void main(String[] args) throws Exception {

        System.out.print("Starting up ... ");

        String rancherApiURL = args[0] + "/stacks";
        String rancherUSER =  args[1];
        String rancherPASS =  args[2];
        String repoURL = args[3];
        String repoUSER =  args[4];
        String repoPASS =  args[5];

        try {
            RancherInstance rancherInstance = new RancherInstance(rancherApiURL,rancherUSER,rancherPASS);

            System.out.print("cleaning.\n");
            FileUtils.deleteDirectory(new File("tmp"));
            FileUtils.deleteDirectory(new File("repo"));

            if(! new File("tmp").mkdirs()){
                System.err.println("Could not create temporary directory. Exiting.");
                return;
            }

            System.out.print("Fetching rancher stacks ... ");
            ArrayList<RancherStack> rancherStacks = rancherInstance.fetchRancherStacks();
            System.out.print(" downloading them ... ");
            rancherInstance.downloadStacks(rancherStacks,"tmp");
            System.out.print("done. \n");

            Authenticator.setDefault (null); //Fixed the Basic Authentication Error for GitRepo.

            System.out.print("Cloning git repository ... ");
            try(GitRepo gitRepo = new GitRepo("repo",repoURL,repoUSER,repoPASS)){
                gitRepo.close();
                FileUtils.copyDirectory(new File("repo/.git"), new File("tmp/git"));
                FileUtils.cleanDirectory(new File("repo"));
                FileUtils.moveDirectory(new File("tmp/git"),new File("repo/.git"));
            } catch (GitAPIException | FileNotFoundException f){
                System.err.println("Could not clone repo, please check your repository URL, Username and Password! URL: " + repoURL );
                f.printStackTrace();
                System.err.println("User: " + repoUSER + " Password: " + repoPASS);
                return;
            }
            System.out.print("done. \n");


            System.out.print("Copying configs into the repository ... ");
            for (RancherStack stack : rancherStacks) {
                if(new File("repo" + File.separator + stack.getName()).mkdirs())
                    FileAgent.unzip("tmp"+File.separator+stack.getName() + ".zip", "repo" + File.separator +stack.getName());
            }
            System.out.print("done. \n");

            System.out.print("Pushing the repo ... ");
            try(GitRepo gitRepo = new GitRepo("repo",repoURL,repoUSER,repoPASS)){
                gitRepo.push();
            } catch (GitAPIException g){
                System.err.println("\n Could not push to repo, please check if " + repoUSER + " is allowed to push to the branch master.");
            }
            System.out.print("done. \n");

            System.out.println("Successfully finished.");

        } catch (ZipException e) {
            System.err.println("Error while handling the Zip file: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error while handling a file: " + e.getMessage());
        } catch (UnirestException e) {
            System.err.println("API communication Error: " + e.getMessage());
        } catch (AuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage());
        }
    }
}
