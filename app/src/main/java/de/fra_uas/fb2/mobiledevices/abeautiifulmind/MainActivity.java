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
    // declare components
    private RadioGroup radioGroup;
    private ImageView imageView;
    private Button generateGame;
    private String selectedStrategy;
    private TextView mySc;
    private TextView opponentSc;
    private TextView gameCnt;

    // when come back from play-game activity active call back method
    @Override
    protected void  onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // getting data from play-game activity
        String data1 = data.getStringExtra("myScore");
        String data2 = data.getStringExtra("opponentScore");
        boolean hasPlayed = data.getBooleanExtra("hasPlayed",false);
        if(hasPlayed){
            mySc.setText(String.valueOf(Integer.valueOf(data1)+Integer.valueOf(mySc.getText().toString())));
            opponentSc.setText(String.valueOf(Integer.valueOf(data2)+Integer.valueOf(opponentSc.getText().toString())));
            gameCnt.setText(String.valueOf(Integer.valueOf(gameCnt.getText().toString())+1));
        }
        // if the game was not played
        else{
            Toast.makeText(this, "The game could not be finished!", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get references of the components
        radioGroup = findViewById(R.id.radioGroup);
        imageView = findViewById(R.id.img);
        radioGroup = findViewById(R.id.radioGroup);
        generateGame = findViewById(R.id.generateGame);
        mySc = findViewById(R.id.myScore);
        opponentSc = findViewById(R.id.opponentScore);
        gameCnt = findViewById(R.id.gameCounts);
        mySc.setText("0");
        opponentSc.setText("0");
        // When any of the radio button is clicked
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup event, int i) {

                RadioButton selectedRadioButton = findViewById(i);
                selectedStrategy = selectedRadioButton.getText().toString();
                reset();
                // conditional rendering of image
                if (selectedStrategy.equals("Nash")) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        });
        // when user click on Generate-Game button
        generateGame.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
               // if user tries to play the game without choosing opponent strategy
                if(radioGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(MainActivity.this, "Select an opponent type first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // otherwise start new game activity
                Intent intent = new Intent(MainActivity.this,PlayGame.class);
                intent.putExtra("strategy",selectedStrategy);
                startActivityForResult(intent,1);
            }
        });
    }
    // reset values
    private void reset(){
        this.gameCnt.setText("0");
        this.opponentSc.setText("0");
        this.mySc.setText("0");
    }
    // prepare for a new game
   public void startOver(View view){
        // reset game count and score board
        reset();
   }

}