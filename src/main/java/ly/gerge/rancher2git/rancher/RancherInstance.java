package ly.gerge.rancher2git.rancher;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import ly.gerge.rancher2git.util.FileAgent;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.naming.AuthenticationException;
import java.io.*;
import java.util.ArrayList;

/*** Created by Gergely Mentsik on 05.02.2017 */

public class RancherInstance {
    private String apiURL;
    private String apiUser;
    private String apiPw;
    private ArrayList<RancherStack> stacks = new ArrayList<>();

    public RancherInstance(String apiURL, String apiUser, String apiPw) {
        this.apiURL = apiURL;
        this.apiUser = apiUser;
        this.apiPw = apiPw;
    }

    public RancherInstance(String apiURL) {
        this.apiURL = apiURL;
    }

    /** This will download the zipped rancher and docker compose files via the Rancher API  +**/
    private boolean downloadComposeZip(String stackId, String dstFile) throws IOException {
        if(apiUser == null && apiPw == null){
            return FileAgent.downloadFromWebResource(apiURL+"/"+stackId + "/composeconfig",dstFile) > 0;
        }else{
            return FileAgent.downloadFromWebResource(apiURL+"/"+stackId + "/composeconfig",dstFile,apiUser,apiPw) > 0;
        }
    }

    /** Download stacks in Batches **/
    public void downloadStacks(ArrayList<RancherStack> stacks, String baseDir){
        stacks.forEach(x -> {
            try {
                downloadComposeZip(x.getId(), baseDir + File.separator + x.getName() + ".zip");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /** fetch stacks from server **/
    public ArrayList<RancherStack> fetchRancherStacks() throws UnirestException, AuthenticationException {
        HttpResponse<JsonNode> jsonResponse = null;

        if(apiPw != null && apiUser != null)
            jsonResponse = Unirest.get(apiURL).header("accept", "application/json").basicAuth(apiUser,apiPw).asJson();

        if(apiUser == null && apiPw == null)
            jsonResponse = Unirest.get(apiURL).header("accept", "application/json").asJson();

        if (jsonResponse != null) {
            if(jsonResponse.getStatus() == 401)
                throw new AuthenticationException("Rancher Server returned 401 (Unauthorized).");

            JSONArray jsonStacks = jsonResponse.getBody().getObject().getJSONArray("data");

            for(int i = 0; i<jsonStacks.length();i++){
                JSONObject rStack = (JSONObject) jsonStacks.get(i);
                RancherStack newStack = new RancherStack();
                newStack.setId(rStack.getString("id"));
                newStack.setName(rStack.getString("name"));
                this.stacks.add(newStack);
            }
        }else{
            System.err.println("Could not fetch stacks from server.");
            return null;
        }
        return this.stacks;
    }
}
