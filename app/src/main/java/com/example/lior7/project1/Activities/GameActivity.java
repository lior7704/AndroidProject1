package com.example.lior7.project1.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lior7.project1.Object_Classes.Card;
import com.example.lior7.project1.Adapters.ImageAdapter;
import com.example.lior7.project1.R;
import java.util.*;
import java.util.Arrays;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import tyrantgit.explosionfield.ExplosionField;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    //region Variables
    private int numOfCubes, maxTime, numOfMatches = 0, sizeOfMatrix, numClick = 0, firstClick = -1, secondClick= -1;
    private String name;
    private int borderColor;
    private GridView gridview;
    private TextView textViewTimer;
    private boolean timeIsUp = false;
    private CountDownTimer countDownTimer;
    private Runnable matchRunnable;
    private Handler handler;
    private Random rnd = new Random();
    ExplosionField explosionField;
    KonfettiView konfettiView;
    private int score = 0;
    private Stack<Card> matchStack;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];

    private float[] initialPos;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        explosionField = ExplosionField.attach2Window(this);
        Bundle data = getIntent().getExtras();
        if (data != null) {
            numOfCubes = data.getInt(DifficultyActivity.NUM_OF_CUBES);
            maxTime = data.getInt(DifficultyActivity.TIME);
            name = data.getString(MainActivity.NAME);
        }
        // Sign up to sensor services
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        matchStack = new Stack();

        bindUI();
        startTimer();
    }

    private void bindUI()
    {
        TextView textViewName = findViewById(R.id.textViewName);
        textViewName.setText(name);
        textViewTimer = findViewById(R.id.textViewTimer);
        gridview = findViewById(R.id.gridView);
        konfettiView = findViewById(R.id.konfettiView);

        // In easy level, choose the number of columns to be 2
        if (numOfCubes == DifficultyActivity.NUM_CARDS_EASY){
            gridview.setNumColumns(numOfCubes);
            gridview.requestLayout();
        }

        final Integer[] images = getImages();
        ImageAdapter imageAdapter = new ImageAdapter(this, images);
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // The image is defaultImage and Timer is still running
                if(!((Card)v).getState() && !timeIsUp) {
                    numClick++;
                    Card card = (Card) v;
                    // Show imageView picture
                    card.setImageResource(card.getImageId());
                    card.setState(true);

                    if(numClick % 2 != 0) {
                        firstClick = position;
                        // Mark card, each two cards marked with the same color
                        borderColor = rnd.nextInt();
                        setBorderColor(card, borderColor);
                    }
                    else {
                        secondClick = position;
                        setBorderColor(card, borderColor);
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
            // Confetti animation
            konfettiView.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(12, 5))
                    .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                    .stream(300, 5000L);

        }
        final String RES = result;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent resIntent = new Intent();
                resIntent.putExtra(DifficultyActivity.RESULT, RES);
                setResult(RESULT_OK, resIntent);
                finish();
            }
        }, 3000);
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
                    //SystemClock.sleep(1000);
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
        mSensorManager.unregisterListener(this);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLastAccelerometerSet = false;
        mLastMagnetometerSet = false;
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setBorderColor(Card imageView, int borderColor){
        imageView.setCropToPadding(true);
        imageView.setBackgroundColor(borderColor);
    }

    private void checkForCouple(){
        final Card card1 = (Card)gridview.getChildAt(firstClick);
        final Card card2 = (Card)gridview.getChildAt(secondClick);
        firstClick = secondClick = -1;
        handler = new Handler();
        matchRunnable = new Runnable() {
            @Override
            public void run() {
                // If images are not equal return to default pictures
                if (card1.getImageId() != card2.getImageId()) {
                    card1.setImageResource(Card.getDefaultImageId());
                    card1.setState(false);
                    card2.setImageResource(Card.getDefaultImageId());
                    card2.setState(false);
                }
                else{
                    numOfMatches++;
                    // Push cards into stack
                    matchStack.push(card1);
                    matchStack.push(card2);
                }

                if(numOfMatches == sizeOfMatrix / 2){ // User Won
                    gameEnd();
                }
                // Remove marking
                setBorderColor(card1, getResources().getColor(R.color.colorBackGroundWhite));
                setBorderColor(card2, getResources().getColor(R.color.colorBackGroundWhite));
            }
        };
        handler.postDelayed(matchRunnable, 1000);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            if(initialPos == null){
                initialPos = new float[3];
                initialPos[0] = Math.abs(mOrientation[0]);
                initialPos[1] = Math.abs(mOrientation[1]);
                initialPos[2] = Math.abs(mOrientation[1]);
            }
            float rotationLimit = 5;
            if(Math.abs(mOrientation[0]) - initialPos[0] > rotationLimit ||
                Math.abs(mOrientation[1]) - initialPos[1] > rotationLimit ||
                Math.abs(mOrientation[2]) - initialPos[2] > rotationLimit){
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if(v != null){
                    v.vibrate(500);
                }
                this.revertLastMatch();
            }
        }
    }

    private void revertLastMatch(){
        handler = new Handler();
        matchRunnable = new Runnable() {
            @Override
            public void run() {
                Card card1 = matchStack.pop();
                Card card2 = matchStack.pop();
                card1.setImageResource(Card.getDefaultImageId());
                card1.setState(false);
                card2.setImageResource(Card.getDefaultImageId());
                card2.setState(false);
                numOfMatches--;
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

    // References to images
    Integer[] cards = {
            R.drawable.card1, R.drawable.card2, R.drawable.card3,
            R.drawable.card4, R.drawable.card5, R.drawable.card6,
            R.drawable.card7, R.drawable.card8, R.drawable.card9,
            R.drawable.card10, R.drawable.card11, R.drawable.card12,
    };
}
