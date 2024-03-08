package com.legendarysoftwares.onetouchmail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private RequestQueue nQueue;
    private TextView mail_id;
    static TextView id,from,subject,date,fullmsg,read_btn;
    private Button getMail,getInbox;
    static String url,url2,url3,mailList,passId, user,domain,mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nQueue = Volley.newRequestQueue(this);

        mail_id = findViewById(R.id.email_viewTV);
        id = findViewById(R.id.id);
        from = findViewById(R.id.from);
        subject = findViewById(R.id.subject);
        date = findViewById(R.id.date);
        getMail = findViewById(R.id.get_email_btn);
        getInbox = findViewById(R.id.mail_list_btn);
        fullmsg = findViewById(R.id.fullmsg);
        read_btn = findViewById(R.id.read);

        getMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GanerateMailID();
            }
        });

        getInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInbox();
            }
        });
        read_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMsg();
            }
        });
    }

    public void GanerateMailID(){

        url="https://www.1secmail.com/api/v1/?action=genRandomMailbox&count=1";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                                mail = response.getString(0);
                                mail_id.setText("Email : " + mail + "\n\n");

                            } catch (JSONException e) {
                           e.printStackTrace();
                       }
                        Toast.makeText(MainActivity.this,mail,Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        nQueue.add(request);

    }

    public void getInbox(){

        String[] parts = mail.split("@");
        if (parts.length == 2) {
             user = parts[0].toString();
             domain = parts[1].toString();
        }
        url2=("https://www.1secmail.com/api/v1/?action=getMessages&login="+user+"&domain="+domain);
        //url2=("https://www.1secmail.com/api/v1/?action=getMessages&login=ehlcpw&domain=laafd.com");
        JsonArrayRequest inbox_request = new JsonArrayRequest(Request.Method.GET, url2, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response==null){
                            Toast.makeText(MainActivity.this,"Their Is No Mail...",Toast.LENGTH_SHORT).show();
                        }
                        else{
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonResponse=response.getJSONObject(i);

                                passId = jsonResponse.getString("id");
                                id.setText("");
                                id.append(passId+ "\n\n");
                                mailList = jsonResponse.getString("from");
                                from.setText("");
                                from.append(mailList+ "\n\n");
                                mailList = jsonResponse.getString("subject");
                                subject.setText("");
                                subject.append(mailList+ "\n\n");
                                mailList = jsonResponse.getString("date");
                                date.setText("");
                                date.append(mailList+ "\n\n");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        nQueue.add(inbox_request);
    }

    public void showMsg(){
        url3=("https://www.1secmail.com/api/v1/?action=readMessage&login="+user+"&domain="+domain+"&id="+passId);
        //url3=("https://www.1secmail.com/api/v1/?action=readMessage&login=ehlcpw&domain=laafd.com&id=524683636");
        JsonObjectRequest inbox_request = new JsonObjectRequest(Request.Method.GET, url3, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response==null) {
                            Toast.makeText(MainActivity.this, "Their is no mail..!", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                mailList = response.getString("textBody");
                                fullmsg.setText("");
                                fullmsg.append(mailList + "\n\n");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        nQueue.add(inbox_request);
    }

}


