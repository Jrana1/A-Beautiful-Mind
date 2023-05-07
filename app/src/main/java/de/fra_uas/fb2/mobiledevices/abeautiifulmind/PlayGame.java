package de.fra_uas.fb2.mobiledevices.abeautiifulmind;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class PlayGame extends AppCompatActivity {
    private Button dismiss;
    private TextView strategyName;
    private TextView aa;
    private TextView bb;
    private TextView ab;
    private TextView ba;
    private Button actionA;
    private Button actionB;
    private int mySc;
    private int oppSc;
    private String stName;
    private Point[][]joinActions=new Point[2][2];
    private TextView resultTxt1;
    private TextView resultTxt2;
    private TextView resultTxt3;
    private TextView resultTxt4;
    private TextView[][] textViews=new TextView[2][2];
    private boolean hasPlayed=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        dismiss = findViewById(R.id.dismiss);
        Bundle extras = getIntent().getExtras();
        strategyName=findViewById(R.id.strategyName);
        aa=findViewById(R.id.aa);
        bb=findViewById(R.id.bb);
        ba=findViewById(R.id.ba);
        ab=findViewById(R.id.ab);
        actionA=findViewById(R.id.actionA);
        actionB=findViewById(R.id.actionB);
        resultTxt1=findViewById(R.id.resultText1);
        resultTxt2=findViewById(R.id.resultText2);
        resultTxt3=findViewById(R.id.resultText3);
        resultTxt4=findViewById(R.id.resultText4);
        textViews[0][0]=aa;
        textViews[0][1]=ab;
        textViews[1][0]=ba;
        textViews[1][1]=bb;
        Random rn = new Random();
        int min=-5;
        int max=5;
        int i=0;
        int [][]tmp=new int[4][2];
        for(int m=0;m<2;m++){
            for(int n=0;n<2;n++){
                int x=rn.nextInt((max-min)+1) + min;
                int y=rn.nextInt((max-min)+1) + min;
                tmp[i][0]=x;
                tmp[i][1]=y;
                i++;
                String str = String.format("%d / %d",x,y);
                textViews[m][n].setText(str);
            }
        }
        int  k=0;
        for(int m=0;m<2;m++){
            for(int n=0;n<2;n++){
                joinActions[m][n]=new Point(tmp[k][0],tmp[k][1]);
                k++;
            }
        }
        strategyName.setText(extras.getString("strategy"));
        YoYo.with(Techniques.FlipInX)
                .duration(700)
                .repeat(3)
                .playOn((strategyName));
        stName=extras.getString("strategy");

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.putExtra("myScore",String.valueOf(mySc));
                intent.putExtra("opponentScore",String.valueOf(oppSc));
                if(actionA.isEnabled()){
                    hasPlayed=false;
                }
                intent.putExtra("hasPlayed",hasPlayed);
                setResult(1,intent);
                finish();
            }
        });
    }
    public void handleAction(View view){

        Button btn = (Button) view;
        actionA.setEnabled(false);
        actionB.setEnabled(false);

        if(this.stName.equals("Random")){
            randomStrategy(btn.getText().toString());

        }
       else if(this.stName.equals("Greedy")){
           greedyStrategy(btn.getText().toString());

        }
        else if(this.stName.equals("Cautious")){
            cautiousStrategy(btn.getText().toString());

        }
       else if(this.stName.equals("Nash")){
           nashStrategy(btn.getText().toString());

        }
    }
    private  void nashStrategy(String myChoice){

        int row = getRow(myChoice);
        Point[][] map=new Point[2][2];
        for(int i=0;i<2;i++){
            for(int j=0;j<2;j++){

                map[i][j]=new Point(0,0);
            }
        }
        updateMapCols(map,0);
        updateMapCols(map,1);
        updateMapRows(map,0);
        updateMapRows(map,1);
        int nrEqulibria =0;
        List<Point>pair=new ArrayList<>();
        for(int i=0;i<2;i++){
            for(int j=0;j<2;j++){

                if(map[i][j].x==1 && map[i][j].y==1){
                    nrEqulibria++;
                    pair.add(new Point(i,j));

                }
            }
        }
        if(nrEqulibria>1){
            Toast.makeText(this, "There are multiple Nash equilibrium in this game\nOpponent can not choose an action\nGame can not be finished", Toast.LENGTH_SHORT).show();
            hasPlayed=false;
            return;
        }
        if(nrEqulibria==0){
            Toast.makeText(this, "There is no Nash equilibrium in this game\nOpponent can not choose an action\nGame can not be finished", Toast.LENGTH_SHORT).show();
            hasPlayed=false;
            return;
        }
        if(nrEqulibria==1){

            setMyScOppScAndRefreshResult(row,pair.get(0).y);
            String myAc="";
            String oppAc="";
            if(pair.get(0).x==0){
                myAc="My Action A";
            }
            if(pair.get(0).x==1){
                myAc="My Action B";
            }

            if(pair.get(0).y==0){
                oppAc="Opponent Action: A";
            }
            if(pair.get(0).y==1){
                oppAc="Opponent Action: B";
            }
            String msg = String.format("There is an unique equilibrium in this game.\n%s\n%s",myAc,oppAc);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }

    }
    private void updateMapCols(Point[][]map,int fixedRow){
        int maxPayOff=Integer.MIN_VALUE;
        int maxIdx=-1;
        for(int i=0;i<2;i++){

            if(joinActions[fixedRow][i].y>maxPayOff){
                maxPayOff=joinActions[fixedRow][i].y;
                maxIdx=i;
            }
        }
        if(maxIdx!=-1){
            map[fixedRow][maxIdx].y=1;
        }
    }
    private void updateMapRows(Point[][]map,int fixedCol){
        int maxPayOff=Integer.MIN_VALUE;
        int maxIdx=-1;
        for(int i=0;i<2;i++){

            if(joinActions[i][fixedCol].x>maxPayOff){
                maxPayOff=joinActions[i][fixedCol].x;
                maxIdx=i;
            }
        }
        if(maxIdx!=-1){
            map[maxIdx][fixedCol].x=1;
        }
    }
    private void cautiousStrategy(String myChoice){
        int row = getRow(myChoice);
        int col=-1;
        int firstMin=Integer.MAX_VALUE;
        int firstIdx=-1;
        int secondMin=Integer.MAX_VALUE;
        int secondIdx=-1;
        for(int i=0;i<2;i++){
            if(joinActions[i][0].y<firstMin){
                firstMin=joinActions[i][0].y;
                firstIdx=i;
            }
        }
        for(int i=0;i<2;i++){
            if(joinActions[i][1].y<secondMin){
                secondMin=joinActions[i][1].y;
            }
            System.out.println(joinActions[i][1].y);
        }
        System.out.println(firstMin+" "+firstIdx+" "+secondMin+" "+secondIdx);
        if(firstMin>secondMin){
            col=0;
        }
        else{
            col=1;
        }
        setMyScOppScAndRefreshResult(row,col);
    }
    private void greedyStrategy(String myChoice){
        int row;
        row = getRow(myChoice);
        int payOff = Integer.MIN_VALUE;
        int col=-1;
        for(int i=0;i<2;i++){

            if(joinActions[row][i].y>payOff){
                payOff=joinActions[row][i].y;
                col=i;
            }
        }
        setMyScOppScAndRefreshResult(row,col);
    }
    private int getRow(String myChoice){
        int row;
        if(myChoice.charAt(myChoice.length()-1)=='a'){
            return row=0;
        }
        else{
            return  row=1;
        }
    }
    private void randomStrategy(String myChoice){
        int row,col;
        row=getRow(myChoice);
        Random rd = new Random();
        col = rd.nextInt(2);
        setMyScOppScAndRefreshResult(row,col);
    }
    private void setMyScOppScAndRefreshResult(int row,int col){
        this.mySc=joinActions[row][col].x;
        this.oppSc=joinActions[row][col].y;
        refreshResult(row,col,this.mySc,this.oppSc);
    }
    private void refreshResult(int myChoice, int oppChoice, int mySc, int oppSc){
        if(myChoice==0){
            resultTxt1.setText("You chose: Action A");
        }
        else{
            resultTxt1.setText("You chose: Action B");
        }
        if(oppChoice==0){
            resultTxt2.setText("Opponent chose: Action A");
        }
        else{
            resultTxt2.setText("Opponent chose: Action B");
        }
        resultTxt3.setText(String.format("You obtain: %d points",mySc));
        resultTxt4.setText(String.format("Opponent obtain: %d points",oppSc));
        textViews[myChoice][oppChoice].setBackgroundColor(Color.parseColor("#F08080"));
    }
}
// use as a pair
class Point{
    public int x,y;
    public Point(int x,int y){
        this.x=x;
        this.y=y;
    }
    
}