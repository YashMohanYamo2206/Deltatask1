package com.yash.factorapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

public class gamePage extends AppCompatActivity {
    int factorNumber;
    private static final long START_TIME_IN_MILLIS = 5000;
    CountDownTimer countDownTimer;
    private boolean streakBroken = false,TimerRunning = false;
    private long TimeLeftInMillis = START_TIME_IN_MILLIS;
    int rightScore = 0,highestStreak=1,streak=0, generateFactorsClickCount = 0;




    ConstraintLayout cl;
    Button generateFactors, num1, num2, num3,quit;
    TextView tv_countDownTimer,tv_maxScore,tv_highest;
    Random number = new Random();
    Vibrator v;





    Vector<Integer> v_factors = new Vector<>();
    Vector<Integer> v_notFactors = new Vector<>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);



        Intent intent=getIntent();
        factorNumber = intent.getIntExtra("factorNumber",4);




        tv_highest=findViewById(R.id.tv_highest);
        tv_maxScore= findViewById(R.id.tv_rightScore);
        tv_countDownTimer= findViewById(R.id.tv_countDownTimer);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        cl=findViewById(R.id.constrainLayout);
        generateFactors = findViewById(R.id.btn_generateFactors);
        num1 = findViewById(R.id.btn_num1);
        num2 = findViewById(R.id.btn_num2);
        num3 = findViewById(R.id.btn_num3);
        quit = findViewById(R.id.btn_quit);




        SharedPreferences HighestStreak = this.getSharedPreferences("HighestScore",MODE_PRIVATE);
        highestStreak=HighestStreak.getInt("highestStreak",0);



        tv_highest.setText("Highest streak :- " + highestStreak);




        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TimerRunning){
                Intent intent = new Intent(gamePage.this, MainActivity.class);
                startActivity(intent);
                finish();}
                else{
                    pauseTimer();
                    resetTimer();
                    Intent intent = new Intent(gamePage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });



        updateCountDownText();
    }

    @SuppressLint("SetTextI18n")



    public void clickGenerateFactors(View v) {
        try {


                int et_randNum = factorNumber;

                for (int i = 2; i <= et_randNum; i++) {
                    if (et_randNum % i == 0) {
                        v_factors.add(i);
                    } else {
                        v_notFactors.add(i);
                    }
                }

                v_factors.trimToSize();
                v_notFactors.trimToSize();

                int num_rand1 = number.nextInt(v_notFactors.size());
                int num_rand2 = number.nextInt(v_notFactors.size());
                int num_rand3 = number.nextInt(v_factors.size());

                ArrayList<Integer> rand = new ArrayList<Integer>();

                rand.add(v_factors.get(num_rand3));
                rand.add(v_notFactors.get(num_rand1));
                v_notFactors.remove(v_notFactors.get((num_rand1)));
                rand.add(v_notFactors.get(num_rand2));

                Collections.shuffle(rand);
                Collections.shuffle(rand);

                num1.setVisibility(View.VISIBLE);
                num1.setText(String.valueOf(rand.get(0)));
                num2.setVisibility(View.VISIBLE);
                num2.setText(String.valueOf(rand.get(1)));
                num3.setVisibility(View.VISIBLE);
                num3.setText(String.valueOf(rand.get(2)));

                /*generateFactorsClickCount++;
                if (generateFactorsClickCount > 1) {
                    resetTimer();
                }*/
                resetTimer();
                startTimer();

                num1.setEnabled(true);
                num2.setEnabled(true);
                num3.setEnabled(true);
                num1.setTextColor(Color.BLACK);
                num2.setTextColor(Color.BLACK);
                num3.setTextColor(Color.BLACK);


                cl.setBackgroundColor(Color.WHITE);
                generateFactors.setEnabled(false);

                v_factors.clear();
                v_notFactors.clear();

        }
        catch (Exception e) {
            Toast.makeText(this, "Error !! Enter a Number.", Toast.LENGTH_SHORT).show();
        }
    }



    public void clickNumOne(View view) {
        if( factorNumber % Integer.parseInt(String.valueOf(num1.getText())) ==0){
            Toast.makeText(this, "right answer!!!", Toast.LENGTH_SHORT).show();
            cl.setBackgroundColor(Color.GREEN);
            rightScore++;
            tv_maxScore.setText("Right :- " + rightScore);
            num1.setEnabled(false);
            num2.setEnabled(false);
            num3.setEnabled(false);
            generateFactors.setText("CONTINUE");
            pauseTimer();
            if(TimerRunning){
                countDownTimer.cancel();
                TimerRunning=false;
            }
            generateFactors.setEnabled(true);




            if(!streakBroken){
                streak+=1;
                if(streak>highestStreak){
                    highestStreak=streak;
                    tv_highest.setText("Highest streak :- " + highestStreak);
                    SharedPreferences HighestStreak = getSharedPreferences("HighestScore",MODE_PRIVATE);
                    SharedPreferences.Editor editor = HighestStreak.edit();
                    editor.putInt("highestStreak",highestStreak);
                    editor.apply();
                }
            }
            else{
                streakBroken=false;
            }
        }




        else{
            streakBroken=true;
            streak=1;
            pauseTimer();
            num1.setEnabled(false);
            Toast.makeText(this, "wrong answer!!! Here's your right answer..!!", Toast.LENGTH_SHORT).show();
            cl.setBackgroundColor(Color.RED);
            v.vibrate(800);
            if(factorNumber % Integer.parseInt(String.valueOf(num2.getText())) !=0){
                num2.setEnabled(false);
                num3.setTextColor(Color.GREEN);
                num3.setEnabled(false);
            }
            else{
                num2.setTextColor(Color.GREEN);
                num2.setEnabled(false);
                num3.setEnabled(false);
            }
            generateFactors.setText("CONTINUE");
            generateFactors.setEnabled(true);

        }
    }



    public void clickNumTwo(View view) {
        if(factorNumber % Integer.parseInt(String.valueOf(num2.getText())) ==0){
            Toast.makeText(this, "right answer!!!", Toast.LENGTH_SHORT).show();
            cl.setBackgroundColor(Color.GREEN);
            rightScore++;
            tv_maxScore.setText("Right :- " + rightScore);
            num1.setEnabled(false);
            num2.setEnabled(false);
            num3.setEnabled(false);
            generateFactors.setText("CONTINUE");
            pauseTimer();
            generateFactors.setEnabled(true);




            if(!streakBroken){
                streak+=1;
                if(streak>highestStreak){
                    highestStreak=streak;
                    tv_highest.setText("Highest streak :- " + highestStreak);
                    SharedPreferences HighestStreak = getSharedPreferences("HighestScore",MODE_PRIVATE);
                    SharedPreferences.Editor editor = HighestStreak.edit();
                    editor.putInt("highestStreak",highestStreak);
                    editor.commit();
                }
            }
            else{
                streakBroken=false;
            }

        }




        else{
            streakBroken=true;
            streak=1;
            pauseTimer();
            num2.setEnabled(false);
            Toast.makeText(this, "wrong answer!!! Here's your right answer..!!", Toast.LENGTH_SHORT).show();
            cl.setBackgroundColor(Color.RED);
            v.vibrate(800);
            if(factorNumber % Integer.parseInt(String.valueOf(num1.getText())) !=0){
                num1.setEnabled(false);
                num3.setTextColor(Color.GREEN);
                num3.setEnabled(false);
            }
            else{
                num1.setTextColor(Color.GREEN);
                num1.setEnabled(false);
                num3.setEnabled(false);
            }
            generateFactors.setText("CONTINUE");
            generateFactors.setEnabled(true);

        }
    }



    public void clickNumThree(View view) {
        if(factorNumber % Integer.parseInt(String.valueOf(num3.getText())) ==0){
            Toast.makeText(gamePage.this, "right answer!!!", Toast.LENGTH_SHORT).show();
            cl.setBackgroundColor(Color.GREEN);
            rightScore++;
            tv_maxScore.setText("Right :- " + rightScore);
            num1.setEnabled(false);
            num2.setEnabled(false);
            num3.setEnabled(false);
            pauseTimer();
            generateFactors.setEnabled(true);
            generateFactors.setText("CONTINUE");





            if(!streakBroken){
                streak+=1;
                if(streak>highestStreak){
                    highestStreak=streak;
                    tv_highest.setText("Highest streak :- " + highestStreak);
                    SharedPreferences HighestStreak = getSharedPreferences("HighestScore",MODE_PRIVATE);
                    SharedPreferences.Editor editor = HighestStreak.edit();
                    editor.putInt("highestStreak",highestStreak);
                    editor.commit();
                }
            }
            else{
                streakBroken=false;
            }

        }





        else{
            streakBroken=true;
            streak=1;
            pauseTimer();
            num3.setEnabled(false);
            Toast.makeText(gamePage.this, "wrong answer!!! Here's your right answer..!!", Toast.LENGTH_SHORT).show();
            cl.setBackgroundColor(Color.RED);
            v.vibrate(800);
            if(factorNumber % Integer.parseInt(String.valueOf(num2.getText())) !=0){
                num2.setEnabled(false);
                num1.setTextColor(Color.GREEN);
                num1.setEnabled(false);
            }
            else{
                num2.setTextColor(Color.GREEN);
                num2.setEnabled(false);
                num1.setEnabled(false);
            }
            generateFactors.setText("CONTINUE");
            generateFactors.setEnabled(true);

        }
    }







    private void startTimer() {
        countDownTimer = new CountDownTimer(TimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                TimerRunning=true;
            }

            @Override
            public void onFinish() {
                TimerRunning = false;
                resetTimer();




                if(factorNumber % Integer.parseInt(String.valueOf(num1.getText())) !=0 && factorNumber % Integer.parseInt(String.valueOf(num2.getText()) ) !=0){
                   if( num1.isEnabled())
                   {
                    num1.setEnabled(false);
                    num2.setEnabled(false);
                    num3.setTextColor(Color.GREEN);
                       Toast.makeText(gamePage.this, "No answer..!! Here's your right answer", Toast.LENGTH_SHORT).show();
                       num3.setEnabled(false);
                   // v.vibrate(800);
                    cl.setBackgroundColor(Color.RED);
                   }
                }




                if(factorNumber % Integer.parseInt(String.valueOf(num2.getText())) !=0 && factorNumber % Integer.parseInt(String.valueOf(num3.getText())) !=0){
                    if( num1.isEnabled()){
                    num3.setEnabled(false);
                    num2.setEnabled(false);
                    num1.setTextColor(Color.GREEN);
                    num1.setEnabled(false);
                        Toast.makeText(gamePage.this, "No answer..!! Here's your right answer", Toast.LENGTH_SHORT).show();

                        cl.setBackgroundColor(Color.RED);
                  //  v.vibrate(800);
                        }

                }




                if(factorNumber % Integer.parseInt(String.valueOf(num3.getText())) !=0 && factorNumber % Integer.parseInt(String.valueOf(num1.getText())) !=0){
                    if( num1.isEnabled())
                    {num1.setEnabled(false);
                    num3.setEnabled(false);
                    num2.setTextColor(Color.GREEN);
                    num2.setEnabled(false);
                        Toast.makeText(gamePage.this, "No answer..!! Here's your right answer", Toast.LENGTH_SHORT).show();

                        cl.setBackgroundColor(Color.RED);
                  //  v.vibrate(800);
                    }
                }




                streakBroken=true;
                streak=0;
                generateFactors.setText("CONTINUE");
                generateFactors.setEnabled(true);

                //cl.setBackgroundColor(Color.RED);
                v.vibrate(800);

            }
        }.start();

    }





    private void updateCountDownText() {
        int seconds = (int) (TimeLeftInMillis / 1000) ;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d",  seconds%60);

        tv_countDownTimer.setText(timeLeftFormatted);
    }






    private void resetTimer() {
        TimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        TimerRunning = false;
    }






    private void pauseTimer() {
        countDownTimer.cancel();
        TimerRunning = false;
    }





    @Override
    public void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
        try {

            ColorDrawable color = (ColorDrawable) cl.getBackground();
            int colorid = color.getColor();

            ColorStateList num1_color = num1.getTextColors();
            int num1_color_id = num1_color.getDefaultColor();

            ColorStateList num2_color = num2.getTextColors();
            int num2_color_id = num2_color.getDefaultColor();

            ColorStateList num3_color = num3.getTextColors();
            int num3_color_id = num3_color.getDefaultColor();



            b.putInt("num1visiblity",num1.getVisibility());
            b.putLong("timeleftinmillis", TimeLeftInMillis);
            b.putString("num1", String.valueOf(num1.getText()));
            b.putBoolean("num1enablity", num1.isEnabled());
            b.putInt("num1_color_id", num1_color_id);



            b.putInt("num2visiblity",num2.getVisibility());
            b.putString("num2", String.valueOf(num2.getText()));
            b.putBoolean("num2enablity", num2.isEnabled());
            b.putInt("num2_color_id", num2_color_id);




            b.putInt("num3visiblity",num3.getVisibility());
            b.putString("num3", String.valueOf(num3.getText()));
            b.putBoolean("num3enablity", num3.isEnabled());
            b.putInt("num3_color_id", num3_color_id);




            b.putBoolean("timerrunning", TimerRunning);
            b.putInt("backgroundclr", colorid);
            b.putString("generatefactortext", generateFactors.getText().toString());
            b.putBoolean("generateenabled", generateFactors.isEnabled());

        }
        catch (Exception e){

        }



}





    @Override
    protected void onRestoreInstanceState(@NonNull Bundle b) {
        super.onRestoreInstanceState(b);
            generateFactors.setText(b.getString("generatefactortext","generate factor"));
            generateFactors.setEnabled(b.getBoolean("generateenabled", true));



            num1.setVisibility(b.getInt("num1visiblity",0x00000004));
            num1.setText(b.getString("num1",null));
            num1.setEnabled(b.getBoolean("num1enablity",false));
            num1.setTextColor(b.getInt("num1_color_id"));




            num2.setVisibility(b.getInt("num2visiblity",0x00000004));
            num2.setText(b.getString("num2",null));
            num2.setEnabled(b.getBoolean("num2enablity",false));
            num2.setTextColor(b.getInt("num2_color_id"));




            num3.setVisibility(b.getInt("num3visiblity",0x00000004));
            num3.setText(b.getString("num3",null));
            num3.setEnabled(b.getBoolean("num3enablity",false));
            num3.setTextColor(b.getInt("num3_color_id"));




            cl.setBackgroundColor(b.getInt("backgroundclr"));
            TimeLeftInMillis=b.getLong("timeleftinmillis",5000);
            TimerRunning=b.getBoolean("timerrunning",false);
            updateCountDownText();

            if(TimerRunning){
                startTimer();
            }


    }


}


