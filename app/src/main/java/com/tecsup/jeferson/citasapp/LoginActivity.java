package com.tecsup.jeferson.citasapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edt_nombre, edt_contraseña;
    private Button btn_ingresar;
    //private RequestQueue requestQueue;
    //public static final String URL = "http://localhost/php/user_control.php";
    //private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_nombre = (EditText)findViewById(R.id.edt_nombre);
        edt_contraseña =(EditText)findViewById(R.id.edt_contraseña);
        btn_ingresar = ( Button)findViewById(R.id.btn_ingresar);

        btn_ingresar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Thread tr = new Thread(){
            @Override
            public void run() {
                final String resultado = enviarDatosGet(edt_nombre.getText().toString(),edt_contraseña.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = obtDatosJson(resultado);
                        if (r>0){
                            Intent intent = new Intent(getApplicationContext(),Welcome.class);
                            intent.putExtra("cod",edt_nombre.getText().toString());
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "Usuario or Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        tr.start();

    }

    public String enviarDatosGet(String user,String pass){
        URL url = null;
        String linea= "";
        int respuesta = 0;
        StringBuilder resul=null;

        try {
            url = new URL("http://192.168.56.1/WebService/valida.php?usu="+user+"&pass="+pass);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            respuesta=connection.getResponseCode();

            resul = new StringBuilder();

            if (respuesta == HttpURLConnection.HTTP_OK){
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while ((linea=reader.readLine())!=null){
                    resul.append(linea);
                }
            }
        } catch (Exception e) {

        }
        return resul.toString();
    }

    public int obtDatosJson(String response){
        int res=0;
        try {
            JSONArray json = new JSONArray(response);
            if (json.length() > 0) {
                res=1;
            }

        }catch (Exception e){

        }
        return res;
    }


}
//ESto va dentro del oncreate
/*requestQueue = Volley.newRequestQueue(this);

        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("succes")){
                                Toast.makeText(getApplicationContext(), "SUCCESS" + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Welcome.class));
                            }else{
                                Toast.makeText(getApplicationContext(), "Error "+jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> hashMap = new HashMap<String, String>();
                        hashMap.put("email",edt_nombre.getText().toString());
                        hashMap.put("password",edt_contraseña.getText().toString());
                        return hashMap;
                    }
                };
                requestQueue.add(request);
            }
        });*/