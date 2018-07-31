package cakart.cakart.in.flashcard_app.register;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import cakart.cakart.in.flashcard_app.R;


public class LoginActivity extends AppCompatActivity  {

    private static final String TAG = "LogIn";
    ProgressDialog progressDialog;
    private EditText loginInputEmail, loginInputPassword;
    private Button btnlogin;
    private TextView txtLinkSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        loginInputEmail = (EditText) findViewById(R.id.email);
        loginInputPassword = (EditText) findViewById(R.id.password);

        btnlogin = (Button) findViewById(R.id.email_sign_in_button);

        txtLinkSignup = (TextView) findViewById(R.id.txtview_register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loging in please wait !!");

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginTask().execute(loginInputEmail.getText().toString(), loginInputPassword.getText().toString());
            }
        });
        txtLinkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), signup.class);
                startActivity(intent);
            }
        });
    }

    class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String reponse = getResp(params[0], params[1]);
            return reponse;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response == null) {
                Toast.makeText(getApplicationContext(), "No Internet Try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            progressDialog.dismiss();
            try {
                JSONObject j = new JSONObject(response);
                if (j.has("error")) {
                    Toast.makeText(getApplicationContext(), "Invalid Email and Password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Thank you ! Loged is sucessfully", Toast.LENGTH_SHORT).show();
                    JSONObject data = j;
                    String name = data.getString("name");
                    String email = data.getString("email");
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor e = pref.edit();
                    e.putString("name", name);
                    e.putString("email", email);
                    e.commit();
                    Intent intent = new Intent(LoginActivity.this, Homepage.class);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String getResp(String email, String password) {
            String url = "https://www.cakart.in/users/ca_found_app_sign_in.json";
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
                j2.put("email", email);
                j2.put("password", password);
                OutputStream os = c.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
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
                    case 401:
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (c != null) {
                    c.disconnect();
                }
            }
            return null;
        }

    }
}

