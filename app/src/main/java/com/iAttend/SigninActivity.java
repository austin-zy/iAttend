package com.iAttend;

/**
 * Created by erza on 15/11/15.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

public class SigninActivity  extends AsyncTask<String,Void,String>{
   // private TextView statusField,roleField;
    private Context context;
    private int byGetOrPost = 0;
    SessionManager session;

    //flag 0 means get and 1 means post.(By default it is get.)
    public SigninActivity(Context context, int flag) {
        this.context = context;
        String message=doInBackground();
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {


            try{
                String username = (String)arg0[0];
                String password = (String)arg0[1];
                String role = (String) arg0[2];
                String macAddress = (String) arg0[3];

               session = new SessionManager(context);

                if (role.equals("Student"))
                {
                    String link = "http://noirdev.xyz/login_student.php?username="+username+"&password="+password+"&macAddress="+macAddress;
                    URL url = new URL(link);
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(link);
                    HttpResponse response = client.execute(request);

                    BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";
                    String status="";

                    while ((line = in.readLine()) != null) {
                        String[] word=line.split("</br>");
                        status = word[0];
                        String name = word[1];
                        int id = Integer.parseInt(word[2]);
                        String email = word[3];
                        session.createLoginSession(id,name,email,role);

                        for(int i=0;i<word.length;i++){
                            System.out.println(word[i]);
                        }
                        System.out.println(status+""+role);
                        //sb.append(line);

                        break;
                    }
                    in.close();
                    return status+""+role;
                }
                else if(role.equals("Professor")){
                    String link = "http://noirdev.xyz/login_prof.php?username="+username+"&password="+password;
                    URL url = new URL(link);
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(link);
                    HttpResponse response = client.execute(request);

                    BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        System.out.println(line);
                        break;
                    }
                    in.close();
                    return sb.toString()+""+role;
                }
                else{
                    return "Error: Role";
                }

            }

            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }


    }

    @Override
    protected void onPostExecute(String result){
       // this.statusField.setText("Login Successful");
       // this.roleField.setText(result);
        //t.makeText(context,result,Toast.LENGTH_LONG).show();
        if (result.equals("successsuccessStudent")){
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
        }
        else if(result.equals("successsuccessProfessor")){
            Intent intent = new Intent(context, ProfActivity.class);
            context.startActivity(intent);
        }
        else{
            ((Activity)context).finish();
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            Toast.makeText((Activity)context,"Invalid Sign In",Toast.LENGTH_SHORT).show();
        }
    }
}
