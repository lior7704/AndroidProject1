package com.example.lior7.project1;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;
import java.util.Arrays;

import tyrantgit.explosionfield.ExplosionField;

public class GameActivity extends AppCompatActivity {
    private int numOfCubes, maxTime, numOfMatches = 0, sizeOfMatrix, numClick = 0, firstClick = -1, secondClick= -1;
    private String name;
    private int borderColor;
    private GridView gridview;
    private TextView textViewName, textViewTimer;
    private ImageAdapter imageAdapter;
    private boolean timeIsUp = false;
    private CountDownTimer countDownTimer;
    private Runnable matchRunnable;
    private Handler handler;
    private Random rnd = new Random();
    ExplosionField explosionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        explosionField = ExplosionField.attach2Window(this);
        Bundle data = getIntent().getExtras();
        numOfCubes = data.getInt(DifficultyActivity.NUM_OF_CUBES);
        maxTime = data.getInt(DifficultyActivity.TIME);
        name = data.getString(MainActivity.NAME);

        bindUI();
        startTimer();
    }

    private void bindUI()
    {
        textViewName = findViewById(R.id.textViewName);
        textViewName.setText(name);
        textViewTimer = findViewById(R.id.textViewTimer);
        gridview = findViewById(R.id.gridView);

        // In easy level, choose the number of columns to be 2
        if (numOfCubes == DifficultyActivity.NUM_CARDS_EASY){
            gridview.setNumColumns(numOfCubes);
            gridview.requestLayout();
        }

        final Integer[] images = getImages();
        imageAdapter = new ImageAdapter(this, images);
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // The image is defaultImage and Timer is still running
                if(!((Card)v).getState() && !timeIsUp) {
                    numClick++;
                    Card img_view = (Card) v;
                    // Show imageView picture
                    img_view.setImageResource(img_view.getImageId());
                    img_view.setState(true);

                    if(numClick % 2 != 0) {
                        firstClick = position;
                        // Mark card, each two cards marked with the same color
                        borderColor = rnd.nextInt();
                        setBorderColor(img_view, borderColor);
                    }
                    else {
                        secondClick = position;
                        setBorderColor(img_view, borderColor);
                    }
                    if(firstClick != -1 && secondClick != -1){
                        checkForCouple();
                    }
                }
            }
        });

    }

    private void gameEnd(){
        String result = "";
        if(numOfMatches == sizeOfMatrix / 2) {
            result = getString(R.string.game_win_message);
            // TODO: add animation function here

        }
        Intent resIntent = new Intent();
        resIntent.putExtra(DifficultyActivity.RESULT, result);
        setResult(RESULT_OK, resIntent);
        finish();
    }

    private void startTimer()
    {
        countDownTimer = new CountDownTimer(maxTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutesLeft = (int)(millisUntilFinished/ 1000) / 60;
                int secondsLeft = (int)millisUntilFinished/ 1000;
                textViewTimer.setText(String.format("%d:%02d", minutesLeft , secondsLeft));
            }

            @Override
            public void onFinish() {
                timeIsUp = true;
                textViewTimer.setText(String.format("%d:%02d", 0 , 0));
                // Explosion animations
                for(int i=0; i<gridview.getChildCount(); i++) {
                    View child = gridview.getChildAt(i);
                    explosionField.explode(child);
                    SystemClock.sleep(1000);
                }
                // Show game end message
                Toast.makeText(getApplicationContext(), R.string.game_end_message, Toast.LENGTH_LONG).show();
                gameEnd();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent resIntent = new Intent();
        setResult(RESULT_CANCELED, resIntent);
        if(countDownTimer != null)
            countDownTimer.cancel();
        if(handler != null && matchRunnable != null)
            handler.removeCallbacks(matchRunnable);
        finish();
    }

    private void setBorderColor(Card imageView, int borderColor){
        imageView.setCropToPadding(true);
        imageView.setBackgroundColor(borderColor);
    }

    private void checkForCouple(){
        final Card imgView1 = (Card)gridview.getChildAt(firstClick);
        final Card imgView2 = (Card)gridview.getChildAt(secondClick);
        firstClick = secondClick = -1;
        handler = new Handler();
        matchRunnable = new Runnable() {
            @Override
            public void run() {
                // If images are not equal return to default pictures
                if (imgView1.getImageId() != imgView2.getImageId()) {
                    imgView1.setImageResource(Card.DEFAULT_IMAGE_ID);
                    imgView1.setState(false);
                    imgView2.setImageResource(Card.DEFAULT_IMAGE_ID);
                    imgView2.setState(false);
                } else
                    numOfMatches++;
                if(numOfMatches == sizeOfMatrix / 2){ // User Won
                    gameEnd();
                }
                // Remove marking
                setBorderColor(imgView1, getResources().getColor(R.color.colorBackGroundWhite));
                setBorderColor(imgView2, getResources().getColor(R.color.colorBackGroundWhite));
            }
        };
        handler.postDelayed(matchRunnable, 1000);
    }

    private Integer[] getImages()
    {
        // Decide size of matrix according to numOfCubes(from DifficultyActivity)
        if(numOfCubes == DifficultyActivity.NUM_CARDS_HARD)
            sizeOfMatrix = numOfCubes * DifficultyActivity.NUM_CARDS_MEDIUM;
        else
            sizeOfMatrix = numOfCubes * numOfCubes;

        // Shuffle all animals pictures
        List shuffledList = Arrays.asList(cards);
        Collections.shuffle(shuffledList);
        Integer[] arrayFrom = (Integer[])shuffledList.toArray();

        int half_of_images = sizeOfMatrix/ DifficultyActivity.NUM_CARDS_EASY;
        Integer[] images = new Integer[sizeOfMatrix];
        // Copy half_of_images from cards array to images array
        System.arraycopy(arrayFrom, 0, images, 0, half_of_images);
        // Copy the same images, to get pairs of animals
        System.arraycopy(arrayFrom, 0, images, half_of_images, half_of_images);

        // Shuffle images array
        List list = Arrays.asList( images );
        Collections.shuffle(list);
        return (Integer[]) list.toArray();
    }

    // References to animal images
    Integer[] cards = {
            R.drawable.card1, R.drawable.card2, R.drawable.card3,
            R.drawable.card4, R.drawable.card5, R.drawable.card6,
            R.drawable.card7, R.drawable.card8, R.drawable.card9,
            R.drawable.card10, R.drawable.card11, R.drawable.card12,
    };
}
