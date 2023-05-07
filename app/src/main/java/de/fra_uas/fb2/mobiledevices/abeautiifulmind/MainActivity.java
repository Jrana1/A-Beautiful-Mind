package de.fra_uas.fb2.mobiledevices.abeautiifulmind;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private ImageView imageView;
    private Button generateGame;
    private  String selectedStrategy;
    private TextView mySc;
    private TextView opponentSc;
    private TextView gameCnt;
    @Override
    protected void  onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
       // Intent intent = getIntent();
        String data1 = data.getStringExtra("myScore");
        String data2 = data.getStringExtra("opponentScore");
        boolean hasPlayed = data.getBooleanExtra("hasPlayed",false);
        if(hasPlayed){
            mySc.setText(data1);
            opponentSc.setText(data2);
            gameCnt.setText(String.valueOf(Integer.valueOf(gameCnt.getText().toString())+1));
        }
        else{
            Toast.makeText(this, "The game could not be finished!", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.radioGroup);
        imageView = findViewById(R.id.img);
        radioGroup = findViewById(R.id.radioGroup);
        generateGame = findViewById(R.id.generateGame);
        mySc = findViewById(R.id.myScore);
        opponentSc = findViewById(R.id.opponentScore);
        gameCnt = findViewById(R.id.gameCounts);
        mySc.setText("0");
        opponentSc.setText("0");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup event, int i) {

                RadioButton selectedRadioButton = findViewById(i);
                selectedStrategy = selectedRadioButton.getText().toString();
                reset();
                if (selectedStrategy.equals("Nash")) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        });
        generateGame.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
                if(radioGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(MainActivity.this, "Select an opponent type first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this,PlayGame.class);
                intent.putExtra("strategy",selectedStrategy);
               // startActivity(intent);
                startActivityForResult(intent,1);
            }
        });
    }
    private void reset(){

        this.gameCnt.setText("0");
        this.opponentSc.setText("0");
        this.mySc.setText("0");
    }
   public void startOver(View view){
        // reset game count and score board
        reset();


   }

}