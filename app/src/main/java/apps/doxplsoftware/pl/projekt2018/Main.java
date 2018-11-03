package apps.doxplsoftware.pl.projekt2018;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Random;

public class Main extends AppCompatActivity implements View.OnTouchListener {

    public static final int FPS = 30;
    GameView gameView;
    Bitmap playerBmp, enemyBmp, coinBmp;
    int x;
    int y;
    Block []enemy;
    int elements = 9;
    int score = 0;
    int screenWidth;
    int screenHeight;
    int bitmapSide;
    boolean gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        gameView = new GameView(this);
        gameView.setOnTouchListener(this);
        playerBmp = BitmapFactory.decodeResource(getResources(), R.drawable.part);
        enemyBmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
        coinBmp = BitmapFactory.decodeResource(getResources(), R.drawable.coin);
        this.bitmapSide = playerBmp.getWidth();
        gameOver = false;
        x = 0;
        y = 0;
        this.enemy = new Block[elements + 1];
        placeOpponents();
        setContentView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        try
        {
            Thread.sleep(30);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        switch(motionEvent.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                this.x = (int) motionEvent.getX();
                this.y = (int) motionEvent.getY();
        }
        return true;
    }

    public void update()
    {
        //Enemy move
        for(int i=0; i <= this.elements; i++) {
            this.enemy[i].setX(this.enemy[i].getX() + 10 * this.enemy[i].getSignX());
            this.enemy[i].setY(this.enemy[i].getY() + 1 * this.enemy[i].getSignY());
            if (this.enemy[i].getX() >= screenWidth) {
                this.enemy[i].setSignX(-1);
            }
            if (this.enemy[i].getX() <= 0) {
               this.enemy[i].setSignX(1);
            }
            if (this.enemy[i].getY() >= screenHeight) {
                this.enemy[i].setSignY(-1);
            }
            if (this.enemy[i].getY() <= 0) {
                this.enemy[i].setSignY(1);
            }
        }
    }

    public void placeOpponents()
    {
        Random randomGenerator = new Random();
        Random randomSign = new Random();
        for(int i=0; i < this.elements; i++)
        {
            this.enemy[i] = new Block(randomGenerator.nextInt(screenWidth - 150 - bitmapSide) + 150,
                randomGenerator.nextInt(screenHeight - 150 - bitmapSide) + 150, true);
            boolean sign = randomSign.nextBoolean();
            this.enemy[i].setSignX(sign ? 1 : -1);
            this.enemy[i].setSignY(sign ? 1 : -1);
        }
        placeCoin();
    }

    public int checkCollision()
    {
        for(int i=0; i <= this.elements; i++)
        {
            if ((this.x > this.enemy[i].getX() && this.x < this.enemy[i].getX() + bitmapSide) && (this.y > this.enemy[i].getY() && this.y < this.enemy[i].getY() + bitmapSide)) {
                return (this.enemy[i].getBlockType()) ? 1 : 2;
            }
            if ((this.x + bitmapSide > this.enemy[i].getX() && this.x + bitmapSide < this.enemy[i].getX() + bitmapSide) && (this.y > this.enemy[i].getY() && this.y < this.enemy[i].getY() + bitmapSide)) {
                return (this.enemy[i].getBlockType()) ? 1 : 2;
            }
            if ((this.x > this.enemy[i].getX() && this.x < this.enemy[i].getX() + bitmapSide) && (this.y + bitmapSide > this.enemy[i].getY() && this.y + bitmapSide < this.enemy[i].getY() + bitmapSide)) {
                return (this.enemy[i].getBlockType()) ? 1 : 2;
            }
            if ((this.x + bitmapSide > this.enemy[i].getX() && this.x + bitmapSide < this.enemy[i].getX() + bitmapSide) && (this.y + bitmapSide > this.enemy[i].getY()&& this.y + bitmapSide < this.enemy[i].getY() + bitmapSide)) {
                return (this.enemy[i].getBlockType()) ? 1 : 2;
            }
        }
        return 0;
    }

    public void placeCoin()
    {
        Random randomGenerator = new Random();
        this.enemy[elements] = new Block(randomGenerator.nextInt(screenWidth - 150 - bitmapSide) + 150,
                randomGenerator.nextInt(screenHeight - 150 - bitmapSide) + 150, false);
        this.enemy[elements].setSignX(randomGenerator.nextBoolean() ? 1 : -1);
        this.enemy[elements].setSignY(randomGenerator.nextBoolean() ? 1 : -1);
    }

    public class GameView extends SurfaceView implements Runnable
    {
        Thread thread = null;
        SurfaceHolder surfaceHolder;
        boolean isRunning = false;

        public GameView(Context context)
        {
            super(context);
            surfaceHolder = getHolder();
        }

        public void run()
        {
            while(isRunning)
            {
                if(!surfaceHolder.getSurface().isValid())
                    continue;

                Canvas canvas = surfaceHolder.lockCanvas();
                update();
                onDraw(canvas);
                //onDraw(canvas, null, true);
                surfaceHolder.unlockCanvasAndPost(canvas);

                /*if(gameOver) {
                    onDraw(canvas, getResources().getString(R.string.gameOver), false);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    break;
                } */
                switch(checkCollision())
                {
                    case 1:
                        playSound(10);
                        //canvas = surfaceHolder.lockCanvas();
                        //onDraw(canvas, getResources().getString(R.string.gameOver), false);
                        //surfaceHolder.unlockCanvasAndPost(canvas);
                        break;
                    case 2:
                        playSound(11);
                        score += 100;
                        placeCoin();
                        break;
                    default:
                        break;
                }
            }
        }

        public void playSound(int sound)
        {
            MediaPlayer mediaPlayer = null;
            Context context = getApplicationContext();
            switch(sound)
            {
                case Sources.GAMEOVER_SOUND:
                    mediaPlayer = MediaPlayer.create(context, R.raw.gameover);
                    break;
                case Sources.COIN:
                    mediaPlayer = MediaPlayer.create(context, R.raw.coin);
                    break;
                default:
                    break;
            }
            mediaPlayer.start();
        }


        public void onDraw(Canvas canvas)
        {
            canvas.drawARGB(255, 49, 78, 107);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            canvas.drawBitmap(playerBmp, x, y, null);
            for(int i=0; i < elements; i++)
                canvas.drawBitmap(enemyBmp, enemy[i].getX(), enemy[i].getY(), null);
            canvas.drawBitmap(coinBmp, enemy[elements].getX(), enemy[elements].getY(), null);
        }

        public void onDraw(Canvas canvas, String text, boolean updateScore)
        {
            canvas.drawARGB(255, 49, 78, 107);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            if(updateScore)
            {
                canvas.drawText(getResources().getString(R.string.score) + ": " + score, 50, 50, paint);
                return;
            }
            canvas.drawText(text, 50, 50, paint);
        }


        public void pause()
        {
            isRunning = false;
            while(true)
            {
                try {
                    thread.join();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                break;
            }
            thread = null;
        }

        public void resume()
        {
            isRunning = true;
            thread = new Thread(this);
            thread.start();
        }
    }
}
