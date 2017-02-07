package ly.gerge.rancher2git.rancher;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import ly.gerge.rancher2git.util.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

/**
 * Created by Gergely Mentsik on 05.02.2017
 */

public class RancherInstance {
    private String apiURL;
    private String apiUser;
    private String apiPw;


    public RancherInstance(String apiURL, String apiUser, String apiPw) {
        this.apiURL = apiURL;
        this.apiUser = apiUser;
        this.apiPw = apiPw;
    }

    public RancherInstance(String apiURL) {
        this.apiURL = apiURL;
    }

    private boolean downloadComposeZip(String stackId, String dstFile) throws IOException {
        if(apiUser == null && apiPw == null){
            return FileUtils.downloadFromWebResource(apiURL+"/"+stackId,dstFile) > 0;
        }else{
            return FileUtils.downloadFromWebResource(apiURL+"/"+stackId,dstFile,apiUser,apiPw) > 0;
        }
    }

    /** This will download the rancher and docker Compose files in zip via the Rancher API **/
    public ArrayList<RancherStack> downloadZippedStackConfigs(String dstDir) {
        ArrayList<RancherStack> rancherStacks = new ArrayList<>();
        HttpResponse<JsonNode> jsonResponse;

        try{

            if(apiPw != null && apiUser != null){
                System.out.println("Rancher: Authenticating with username and password");
                jsonResponse = Unirest.get(apiURL).header("accept", "application/json").basicAuth(apiUser,apiPw).asJson();

            }else{
                System.out.println("Rancher: No authentication required.");
                jsonResponse = Unirest.get(apiURL).header("accept", "application/json").asJson();
            }

            if (jsonResponse.getStatus() == 401){
                System.err.println("Rancher: Unauthorized. Aborting.");
                return null;
            }

            JSONArray stacks = jsonResponse.getBody().getObject().getJSONArray("data");

            for(int i = 0; i<stacks.length();i++){
                JSONObject rStack = (JSONObject) stacks.get(i);
                RancherStack stack = new RancherStack();
                stack.setId(rStack.getString("id"));
                stack.setName(rStack.getString("name"));
                downloadComposeZip(apiURL + "/" + stack.getId() + "/composeconfig",dstDir + File.separator +stack.getName()+".zip");
                rancherStacks.add(stack);
            }
            return rancherStacks;

        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void checkIn(){

    }

}
