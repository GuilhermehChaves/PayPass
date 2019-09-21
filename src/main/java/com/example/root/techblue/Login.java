package com.example.root.techblue;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    IntentFilter[] filters;
    String[][] techs;
    PendingIntent pendingIntent;
    private NfcAdapter adapter;

    Button register;
    private String idCard;
    private String pass;

    private static String URL = "http://mypentest.tk/xyz/ghi/abc/login.php";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);







        pendingIntent = PendingIntent.getActivity(this,0,
        new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);


        IntentFilter mifare = new IntentFilter((NfcAdapter.ACTION_TECH_DISCOVERED));
            filters = new IntentFilter[]{mifare};
                techs = new String[][]{new String[] {NfcA.class.getName()}};
                    adapter = NfcAdapter.getDefaultAdapter(this);



            register = (Button)findViewById(R.id.register_btn);
            register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
           final Intent intent = new Intent(getApplicationContext(), Registrar.class);
           startActivity(intent);

           }//Fim onClick
       });//Fim setOnclick




    }//Fim onCreate





    @Override
    protected void onNewIntent(Intent intent) {


        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
         byte[] id = tag.getId();
            ByteBuffer wrapped = ByteBuffer.wrap(id);
                wrapped.order(ByteOrder.LITTLE_ENDIAN);
                 int signedInt = wrapped.getInt();
                    long number = signedInt & 0xffffffffl;


                      idCard = Long.toString(number);
                        dialog();





    }

    @Override
    protected void onResume() {
        super.onResume();
            adapter.enableForegroundDispatch(this,pendingIntent,filters,techs);
    }

    @Override
    protected void onPause() {
        super.onPause();
            adapter.disableForegroundDispatch(this);
    }




    private void dialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Login.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_dialog, null);
                final EditText senha_login = (EditText) mView.findViewById(R.id.senha_login);
                    final Button login_btn = (Button)mView.findViewById(R.id.login_btn);

        mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
                dialog.show();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = senha_login.getText().toString();
                    login();
                        dialog.cancel();
            }
        });



    }





    private void login(){




        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonMainNode = jsonResponse.optJSONArray("dados");

                            if(response.equals("nada")){
                               alert("Nenhum valor encontrado");
                            }else{

                                for(int i = 0; i < jsonMainNode.length(); i++){

                                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                    String id = jsonChildNode.optString("id");
                                    String senha = jsonChildNode.optString("senha");
                                    String nome = jsonChildNode.optString("nome");




                                    if(idCard.equals(id) && pass.equals(senha)){

                                        Intent intent = new Intent(Login.this, Main.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("id",id);
                                        bundle.putString("nome", nome);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        break;


                                    } else{
                                        alert("Cartão ou senha inválidos");

                                    }


                                }
                            }

                        } catch (JSONException e) {
                            alert("Cartão ou senha inválidos");
                            e.printStackTrace();
                        }catch (Exception ex){
                            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alert("Falha ao conectar-se com o servidor");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_card", idCard );
                return params;
            }
        };

        MySingleton.getInstance(Login.this).addToRequestQueue(stringRequest);
    }





private void alert(String msg){
    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
}



}
