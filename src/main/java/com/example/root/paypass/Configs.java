package com.example.root.paypass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configs extends AppCompatActivity {
    private static String URL_DELETE = "API_URL";

    List<String> opcoes;
    ArrayAdapter<String> adaptador;
    ListView lista;

    private String id_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configs);

        Bundle bundle = getIntent().getExtras();
        
        id_card = bundle.getString("id");
        lista = (ListView) findViewById(R.id.lista);
        opcoes = new ArrayList<String>();

        opcoes.add("Sobre");
        opcoes.add("Sair");
        opcoes.add("Excluir conta");

        adaptador = new ArrayAdapter<String>(Configs.this, android.R.layout.simple_list_item_1, opcoes);
        lista.setAdapter(adaptador);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        delete();
                        break;
                }
            }
        });
    }

    private void delete() {
        final String id;
        id = id_card;

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            if (response.equals("Excluido com sucesso!!")) {
                                alert(response);
                                final Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                            } else {
                                alert("Erro ao excluir conta.");
                            }
                        }
                    }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alert("Error" + error.toString());
                                error.printStackTrace();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                // params.put(Key, value);
                                params.put("id", id);
                                return params;
                            }
                        };

            MySingleton.getInstance(Configs.this).addToRequestQueue(stringRequest);

        } catch (Exception ex) {
            alert("Erro Catch" + ex.toString())
        }
    }

    private void alert(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
