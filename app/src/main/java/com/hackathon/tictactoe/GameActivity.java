package com.hackathon.tictactoe;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by macbook on 24.04.2016.
 */
public class GameActivity extends Activity
{
    int [] array = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    GridView gridView;
    TextView textView;

    boolean isGameOver = false;

    int turn_counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        textView = (TextView) findViewById(R.id.textView_turn);

        gridView = (GridView) findViewById(R.id.gridView);

        ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext());

        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(!(array[position] == 0))
                    return;

                if(isGameOver)
                    return;

                textView.setTextColor(Color.RED);
                textView.setText("Computer Turn");


                array[position] = 1;
                ImageView tmp = (ImageView) gridView.getItemAtPosition(position);
                tmp.setImageResource(R.drawable.x_icon);

                int result = checkGameOver();

                //you won
                if(result == 1)
                {
                    textView.setTextColor(Color.parseColor("#B5007B"));
                    textView.setText("Game Over, You WON!");
                    isGameOver = true;
                }
                else if(result == 2) //draw
                {
                    textView.setTextColor(Color.parseColor("#B5007B"));
                    textView.setText("Game Over, Draw!");
                    isGameOver = true;
                }

                //if you won, it finished
                if(isGameOver)
                    return;


                Thread thread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Random random = new Random();
                        int sleep_time = 100 + random.nextInt(1000);
                        try
                        {
                            Thread.sleep(sleep_time);
                        }
                        catch(InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                computerMove();

                                int result = checkGameOver();
                                //computer won
                                if(result == 1)
                                {
                                    textView.setTextColor(Color.parseColor("#B5007B"));
                                    textView.setText("Game Over, Computer WON!");
                                    isGameOver = true;
                                }
                                else if(result == 2) //draw
                                {
                                    textView.setTextColor(Color.parseColor("#B5007B"));
                                    textView.setText("Game Over, Draw!");
                                    isGameOver = true;
                                }
                            }
                        });
                    }
                });

                thread.start();


            }
        });


        Button restartButton = (Button) findViewById(R.id.button_restart);
        restartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                array = new int[9];
                isGameOver = false;
                turn_counter = 0;

                textView.setTextColor(Color.BLUE);
                textView.setText("Your Turn");

                for(int i=0;i<9;i++)
                {
                    ImageView tmp = (ImageView) gridView.getItemAtPosition(i);
                    tmp.setImageResource(R.drawable.blank_square);
                }

            }
        });



    }


    //AI of the game
    public void computerMove()
    {
        turn_counter++;

        boolean flag = false;

        if(turn_counter >= 3)
        {

            //check that can computer win
            for(int i=0;i<9;i++)
            {
                if(array[i] == 0)
                {
                    array[i] = 2;
                    if(checkAI())
                    {
                        ImageView tmp = (ImageView) gridView.getItemAtPosition(i);
                        tmp.setImageResource(R.drawable.o_icon);
                        flag = true;
                        break;
                    }
                    else
                        array[i] = 0;
                }
            }
        }


        if(!flag)
        {
            //check that can person win
            for(int i=0;i<9;i++)
            {
                if(array[i] == 0)
                {
                    array[i] = 1;
                    if(checkAI())
                    {
                        array[i] = 2;
                        ImageView tmp = (ImageView) gridView.getItemAtPosition(i);
                        tmp.setImageResource(R.drawable.o_icon);
                        flag = true;
                        break;
                    }
                    else
                        array[i] = 0;
                }
            }
        }


        if(!flag)
        {
            Random random = new Random();

            int computer_index = random.nextInt(9);

            while(!(array[computer_index] == 0))
                computer_index = random.nextInt(9);

            array[computer_index] = 2;
            ImageView tmp = (ImageView) gridView.getItemAtPosition(computer_index);
            tmp.setImageResource(R.drawable.o_icon);
        }

        textView.setTextColor(Color.BLUE);
        textView.setText("Your Turn");

    }



    public boolean checkAI()
    {
        //check horizontal
        if(areSame(0, 1, 2))
            return true;

        if(areSame(3, 4, 5))
            return true;

        if(areSame(6, 7, 8))
            return true;



        //check vertical
        if(areSame(0, 3, 6))
            return true;

        if(areSame(1, 4, 7))
            return true;

        if(areSame(2, 5, 8))
            return true;



        //check diagonal from right to left
        if(areSame(2, 4, 6))
            return true;



        //check diagonal from left to right
        if(areSame(0, 4, 8))
            return true;


        return false;
    }



    public boolean areSame(int index1, int index2, int index3)
    {
        int x = array[index1];
        int y = array[index2];
        int z = array[index3];

        if(x == y && y == z && x != 0)
            return true;
        else
            return false;
    }


    public void drawHorizontal(int position)
    {
        ImageView imageView = (ImageView) gridView.getItemAtPosition(position);
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        bitmap = Bitmap.createBitmap(bitmap).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawLine(0, bitmap.getHeight() / 2, bitmap.getWidth(), bitmap.getHeight() / 2, paint);
        imageView.setImageBitmap(bitmap);
    }

    public void drawVertical(int position)
    {
        ImageView imageView = (ImageView) gridView.getItemAtPosition(position);
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        bitmap = Bitmap.createBitmap(bitmap).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawLine(bitmap.getWidth() / 2, 0, bitmap.getWidth() / 2, bitmap.getHeight(), paint);
        imageView.setImageBitmap(bitmap);
    }

    public void drawLR(int position)
    {
        ImageView imageView = (ImageView) gridView.getItemAtPosition(position);
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        bitmap = Bitmap.createBitmap(bitmap).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawLine(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
        imageView.setImageBitmap(bitmap);
    }

    public void drawRL(int position)
    {
        ImageView imageView = (ImageView) gridView.getItemAtPosition(position);
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        bitmap = Bitmap.createBitmap(bitmap).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawLine(bitmap.getWidth(), 0, 0, bitmap.getHeight(), paint);
        imageView.setImageBitmap(bitmap);
    }


    public int checkGameOver()
    {
        //check horizontal
        if(areSame(0, 1, 2))
        {
            drawHorizontal(0);
            drawHorizontal(1);
            drawHorizontal(2);
            return 1;
        }

        if(areSame(3, 4, 5))
        {
            drawHorizontal(3);
            drawHorizontal(4);
            drawHorizontal(5);
            return 1;
        }

        if(areSame(6, 7, 8))
        {
            drawHorizontal(6);
            drawHorizontal(7);
            drawHorizontal(8);
            return 1;
        }



        //check vertical
        if(areSame(0, 3, 6))
        {
            drawVertical(0);
            drawVertical(3);
            drawVertical(6);
            return 1;
        }

        if(areSame(1, 4, 7))
        {
            drawVertical(1);
            drawVertical(4);
            drawVertical(7);
            return 1;
        }

        if(areSame(2, 5, 8))
        {
            drawVertical(2);
            drawVertical(5);
            drawVertical(8);
            return 1;
        }



        //check diagonal from right to left
        if(areSame(2, 4, 6))
        {
            drawRL(2);
            drawRL(4);
            drawRL(6);
            return 1;
        }



        //check diagonal from left to right
        if(areSame(0, 4, 8))
        {
            drawLR(0);
            drawLR(4);
            drawLR(8);
            return 1;
        }

        //draw check
        for(int i=0;i<9;i++)
            if(array[i] == 0)
                return 0;

        return 2;
    }

}
