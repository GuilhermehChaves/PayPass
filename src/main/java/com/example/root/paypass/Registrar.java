package com.example.root.paypass;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Registrar extends AppCompatActivity {
    private static String URL = "API_URL";

    IntentFilter[] filters_register;
    String[][] techs_register;
    PendingIntent pendingIntent_register;
    private NfcAdapter adapter_register;

    private String card_id;
    private EditText pass, user, confPass;
    private Button btn_register;
    private TextView aproximar;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        pendingIntent_register = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter mifare = new IntentFilter((NfcAdapter.ACTION_TECH_DISCOVERED));
        filters_register = new IntentFilter[] { mifare };
        techs_register = new String[][] { new String[] { NfcA.class.getName() } };
        adapter_register = NfcAdapter.getDefaultAdapter(this);

        aproximar = (TextView) findViewById(R.id.aproximar);
        user = (EditText) findViewById(R.id.user_register);
        pass = (EditText) findViewById(R.id.senha_register);
        confPass = (EditText) findViewById(R.id.conf_senha);
        btn_register = (Button) findViewById(R.id.btn_register);

        aproximar.setText("Aproxime seu cartão...");

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pass.getText().toString().equals(confPass.getText().toString())) {
                    insert();
                } else {
                    alert("Senhas não correspondentes");
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] id = tag.getId();
        ByteBuffer wrapped = ByteBuffer.wrap(id);
        wrapped.order(ByteOrder.LITTLE_ENDIAN);
        int signedInt = wrapped.getInt();
        long number = signedInt & 0xffffffffl;

        card_id = Long.toString(number);
        aproximar.setText("");
        alert("Ok");
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter_register.enableForegroundDispatch(this, pendingIntent_register, filters_register, techs_register);
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter_register.disableForegroundDispatch(this);
    }

    protected void insert() {
        final String id_user, usuario, senha;
        id_user = card_id;
        usuario = user.getText().toString();
        senha = pass.getText().toString();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            if (response.equals("ok")) {
                                alert("Registrado com sucesso!!");
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                            } else {
                                alert("Falha ao registrar-se");
                            }
                        }
                    }
                    , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            alert("Error" + error.toString());
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id_user", id_user);
                            params.put("usuario", usuario);
                            params.put("senha", senha);
                            return params;
                        }
                    };

            MySingleton.getInstance(Registrar.this).addToRequestQueue(stringRequest);
        } catch (Exception ex) {
           alert("Erro Catch" + ex.toString())
        }
    }

    private void alert(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
