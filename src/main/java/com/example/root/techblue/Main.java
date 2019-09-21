package com.example.root.techblue;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Main extends AppCompatActivity {

    private  TextView nome_main;
    private Button buy;

    private AlertDialog.Builder builder;
    private  String id_card;



    private static String URL_VALOR = "http://mypentest.tk/xyz/ghi/abc/valor.php";
    private static String URL_UPDATE = "http://mypentest.tk/xyz/ghi/abc/update.php";

    private TextView valor;


    ImageButton edit;
    ImageView perf, config;
    double valor_double;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Bundle bundle = getIntent().getExtras();
            String nome;
                nome = bundle.getString("nome");
                    id_card = bundle.getString("id");



        nome_main = (TextView)findViewById(R.id.nome_main);
            nome_main.setText(nome);
                edit = (ImageButton)findViewById(R.id.edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            editarAvatar();
            }
        });




        buy = (Button)findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        valor = (TextView)findViewById(R.id.valor);
            perf = (ImageView)findViewById(R.id.perfil);
                config = (ImageView)findViewById(R.id.configs);



        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(),Configs.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",id_card);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        valor();

    }








    private void valor(){



        final String id;
        id = id_card;


        try {
//String Request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_VALOR,
//ResponseListener
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {

                            if(!response.equals("nada")) {

                                try {

                                    valor_double = valor_double(response);
                                    DecimalFormat df = new DecimalFormat("0.00");
                                    valor.setText("R$ " + df.format(valor_double) );



                                }catch (Exception ex){

                                    alert(ex.toString());

                                }




                            }
                            else if (response.equals("nada")){
                                   alert("Nenhum valor encontrado");
                            }


                        }
                    }
//ErrorListener
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    alert("Error" + error.toString());
                    error.printStackTrace();
                }
            })
                    //Abrir chaves aqui
            {
// digitar get -> map

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
//params.put(Key, value);
                    params.put("id_card", id);
                    return params;
                }
            };


            MySingleton.getInstance(Main.this).addToRequestQueue(stringRequest);


        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Erro Catch" + ex.toString(), Toast.LENGTH_LONG).show();
        }




    }



private void alert(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

}

private double valor_double(String valor){

        double valor_double = Double.parseDouble(valor);
        return valor_double;

}





private void dialog(){


    AlertDialog.Builder mBuilder = new AlertDialog.Builder(Main.this);
    View mView = getLayoutInflater().inflate(R.layout.compra, null);
    final EditText cartao = (EditText) mView.findViewById(R.id.credit);
    final Button btn_pay = (Button)mView.findViewById(R.id.pay);


    btn_pay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            update_valor();

        }
    });


    mBuilder.setView(mView);
    AlertDialog dialog = mBuilder.create();
    dialog.show();
}



private void editarAvatar(){

    AlertDialog.Builder mBuilder = new AlertDialog.Builder(Main.this);
    View mView = getLayoutInflater().inflate(R.layout.avatar, null);

    ImageView avatar_um, avatar_dois, avatar_tres,avatar_quatro, avatar_cinco, avatar_seis;

    avatar_um = (ImageView)mView.findViewById(R.id.avatarum);
    avatar_dois = (ImageView)mView.findViewById(R.id.avatartres);
    avatar_tres = (ImageView)mView.findViewById(R.id.avatarquatro);

    avatar_quatro = (ImageView)mView.findViewById(R.id.avatarcinco);
    avatar_cinco = (ImageView)mView.findViewById(R.id.avatarseis);
    avatar_seis = (ImageView)mView.findViewById(R.id.avatarsete);


    mBuilder.setView(mView);
    final AlertDialog dialog = mBuilder.create();
    dialog.show();



    avatar_um.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Drawable drawable= getResources().getDrawable(R.drawable.avatarseis);
            perf.setImageDrawable(drawable);
            dialog.cancel();
        }
    });


    avatar_dois.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Drawable drawable= getResources().getDrawable(R.drawable.avatarcinco);
            perf.setImageDrawable(drawable);
            dialog.cancel();
        }
    });


    avatar_tres.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Drawable drawable= getResources().getDrawable(R.drawable.avatarsete);
            perf.setImageDrawable(drawable);
            dialog.cancel();
        }

    });





    avatar_quatro.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Drawable drawable= getResources().getDrawable(R.drawable.avatartres);
            perf.setImageDrawable(drawable);
            dialog.cancel();
        }
    });

    avatar_cinco.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Drawable drawable= getResources().getDrawable(R.drawable.avatarzero);
            perf.setImageDrawable(drawable);
            dialog.cancel();
        }
    });

    avatar_seis.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Drawable drawable= getResources().getDrawable(R.drawable.avatarum);
            perf.setImageDrawable(drawable);
            dialog.cancel();
        }
    });




}





    private void update_valor(){

        final String id;
        final String valor_string;

        valor_double= valor_double + 5;

        id = id_card;
        valor_string = Double.toString(valor_double);

        try {
//String Request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE,
//ResponseListener
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {

                            if(response.equals("ok")) {

                                try {

                                alert("Pagamento realizado com sucesso!!!");
                                valor();


                                }catch (Exception ex){

                                    alert(ex.toString());

                                }




                            }
                            else if (response.equals("nada")){
                                alert("Nenhum valor encontrado");
                            }


                        }
                    }
//ErrorListener
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    alert("Error" + error.toString());
                    error.printStackTrace();
                }
            })
                    //Abrir chaves aqui
            {
// digitar get -> map

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
//params.put(Key, value);
                    params.put("id_card", id);
                    params.put("valor", valor_string);
                    return params;
                }
            };


            MySingleton.getInstance(Main.this).addToRequestQueue(stringRequest);


        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Erro Catch" + ex.toString(), Toast.LENGTH_LONG).show();
        }






    }








}
