package com.yash.factorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button NewGame;
    EditText number;
    RelativeLayout rl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NewGame=findViewById(R.id.btn_newGame);
        number=findViewById(R.id.ET_NUMBER);
        NewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int factorNumber = Integer.parseInt(String.valueOf(number.getText()));
                    Intent intent = new Intent(MainActivity.this, gamePage.class);
                    intent.putExtra("factorNumber", factorNumber);
                    startActivity(intent);
                    finish();
                    number.setHint("ENTER NUMBER");
                    }
                catch (Exception e) {
                    number.setError("ENTER NUMBER"); // use either Toast or setError..!! setError() looks more aesthetic to me lol..!
                    // Toast.makeText(MainActivity.this, "ERROR..!!! ENTER A NUMBER.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
