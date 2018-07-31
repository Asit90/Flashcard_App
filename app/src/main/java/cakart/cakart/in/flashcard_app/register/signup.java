package cakart.cakart.in.flashcard_app.register;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import cakart.cakart.in.flashcard_app.R;


public class signup extends AppCompatActivity  {


    private static  final String TAG="signup";
    ProgressDialog progressDialog;
    private EditText signup_name,signup_email,signup_password,signup_repassword;
    private Button btnsignup;
    TextView txtviewlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Signing up !Please wait!");

        signup_name = (EditText) findViewById(R.id.sign_name);
        signup_email = (EditText) findViewById(R.id.sign_email);
        signup_password = (EditText) findViewById(R.id.sign_password);
        signup_repassword = (EditText) findViewById(R.id.sign_repassword);

        btnsignup = (Button) findViewById(R.id.btn_signup);
        txtviewlogin = (TextView) findViewById(R.id.textViewLoginLink);

        txtviewlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);

            }
        });


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!signup_password.getText().toString().equals(signup_repassword.getText().toString()) )
                {
                    Toast.makeText(signup.this, "Password and Re Enter Password are not matching", Toast.LENGTH_SHORT).show();
                }
                else {
                    submitForm();
                }
            }
        });


    }

    public void submitForm() {
        new RegisterTask().execute(
                signup_email.getText().toString(), signup_name.getText().toString(),
                signup_password.getText().toString());

    }


    class RegisterTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            String response = getResp(Uri.encode(params[0]), Uri.encode(params[1]), Uri.encode(params[2]));
            return response;
        }
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.show();
        }

        protected void onPostExecute(String response){
            if (response == null) {
                Toast.makeText(getApplicationContext(), "No Internet Try Again!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
            try{JSONObject j = new JSONObject(response);
                if (j.has("error")) {
                    JSONObject errors = j.getJSONObject("error");
                    JSONArray full_msg = errors.getJSONArray("full_messages");
                    String error_msg = full_msg.join(", ");
                    Toast.makeText(getApplicationContext(), "Error - " + error_msg, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Thank you! Registration succesful!", Toast.LENGTH_LONG).show();
                    JSONObject data = j.getJSONObject("user");
                    String name = data.getString("name");
                    String email = data.getString("email");
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor e = pref.edit();
                    e.putString("name", name);
                    e.putString("email", email);
                    e.commit();
                    Intent intent = new Intent(signup.this, Homepage.class);
                    startActivity(intent);
                    finish();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private String getResp(String email, String name, String password) {
        String url = "https://www.cakart.in/users/ca_found_app_sign_up.json?email=" + email + "&password=" + password + "&password_confirmation=" + password + "&name=" + name;
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("POST");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(60000);
            c.setReadTimeout(60000);
            c.setDoInput(true);
            c.setDoOutput(true);
            c.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            c.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            c.connect();

            JSONObject j2 = new JSONObject();
            j2.put("email",email);
            j2.put("name",name);
            j2.put("password",password);

            OutputStream os = c.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(j2.toString());
            writer.flush();
            writer.close();
            os.close();

            int status = c.getResponseCode();
            Log.d(TAG, "Response " + status);
            BufferedReader br;
            StringBuilder sb;
            String line;

            switch (status) {
                case 200:
                case 201:
                    br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    sb = new StringBuilder();
                    line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
                case 422:
                    br = new BufferedReader(new InputStreamReader(c.getErrorStream()));
                    sb = new StringBuilder();
                    line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
                case 500:
                    br = new BufferedReader(new InputStreamReader(c.getErrorStream()));
                    sb = new StringBuilder();
                    line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            } } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return null;
    }
}

