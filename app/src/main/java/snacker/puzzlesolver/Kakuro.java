package snacker.puzzlesolver;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snacker on 2017-05-19.
 */

public class Kakuro extends View implements View.OnTouchListener{
    int mWidth = 10;
    int mHeight = 8;
    int mCellWidth, mCellHeight;
    int mCellMargin;
    int mEdgeThick;
    int mStatus;
    int mTextSize, mSmallTextSize;
    int mXNow = -1, mYNow = -1;
    int[][] mBoard = new int[10][10];
    int[][] HorBoard = new int[10][10];
    int[][] VerBoard = new int[10][10];
    int[][] CheckBoard = new int[10][10];
    int[][] solveGrid = new int[10][10];
    int[][] onlySolution = new int[10][10];
    int solution = 0;
    Point mBoardPt;

    Paint mTextPaint, mInvalidTextPaint, mTileEdgePaint, mBoardPaint;

    EditText et;
    RadioButton rb1, rb2, rb3;

    final static int VALID = 0;
    final static int INVALID = 1;

    public Kakuro(){
        super(null);
    };

    public Kakuro(Context context){
        super(context);
        initializeBoard(mHeight, mWidth);
        mCellWidth = mCellHeight = this.getWidth() / (mWidth);
    }

    public Kakuro(Context context, AttributeSet attrs){
        super(context, attrs);
        initializeBoard(mHeight,mWidth);
        mCellWidth = mCellHeight = this.getWidth() / (mWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = (width / mWidth) * (mHeight);
        setMeasuredDimension(width, height);
    }

    public void setEditText(EditText e){
        et = e;
    }

    public void setHorRadioButton(RadioButton r){
        rb1 = r;
    }

    public void setVerRadioButton(RadioButton r){
        rb2 = r;
    }

    public void setValRadioButton(RadioButton r){
        rb3 = r;
    }

    @Override
    protected void onDraw(Canvas canvas){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mCellWidth = mCellHeight = this.getWidth() / (mWidth);
        mCellMargin = (int)(mCellWidth * 0.05f);
        mEdgeThick = (int)(mCellWidth * 0.05f);
        mTextSize = mCellHeight / 2;
        mSmallTextSize = mCellHeight / 3;
        mTextPaint = new Paint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mInvalidTextPaint = new Paint();
        mInvalidTextPaint.setTextAlign(Paint.Align.CENTER);
        mTileEdgePaint = new Paint();
        mTileEdgePaint.setStyle(Paint.Style.STROKE);

        mBoardPt = new Point(0,0);

        mBoardPaint = new Paint();
        mBoardPaint.setStyle(Paint.Style.FILL);
        mBoardPaint.setColor(Color.LTGRAY);
        Rect board = new Rect(0,0,this.getWidth(),getHeight());
        canvas.drawRect(board,mBoardPaint);

        for(int x = 0; x< mWidth; x++){
            for (int y=0; y< mHeight; y++){
                int tx = mBoardPt.x + mCellWidth * x;
                int ty = mBoardPt.y + mCellWidth * y;
                Rect rt= new Rect(tx, ty, tx + mCellWidth, ty + mCellHeight);

                //rt.inset(mCellMargin,mCellMargin);

                mBoardPaint.setColor(Color.WHITE);
                mTileEdgePaint.setStrokeWidth(mEdgeThick);
                float texty = mTextSize / 2;

                canvas.drawRect(rt, mTileEdgePaint);
                if(CheckBoard[y][x] <= 0){ mTextPaint.setColor(Color.BLACK); }
                else { mTextPaint.setColor(Color.RED); }

                if(HorBoard[y][x] != -1 || VerBoard[y][x] != -1){
                    texty = mSmallTextSize / 2;
                    canvas.drawRect(rt,mBoardPaint);
                    canvas.drawLine(tx,ty,tx + mCellWidth , ty + mCellHeight,mTileEdgePaint);
                    mTextPaint.setTextSize(mSmallTextSize);
                    if(HorBoard[y][x] != -1){
                    canvas.drawText("" + HorBoard[y][x], tx + 3* mCellWidth /4, ty + mCellHeight / 4 + texty, mTextPaint);
                    }
                    if(VerBoard[y][x] != -1){
                    canvas.drawText("" + VerBoard[y][x], tx +  mCellWidth / 4, ty + 3 * mCellHeight / 4 + texty, mTextPaint);
                    }
                }

                if(mBoard[y][x] != -1){
                    canvas.drawRect(rt,mBoardPaint);
                }

                if(onlySolution[y][x] != -1 && mBoard[y][x] == 0){
                    mTextPaint.setTextSize(mTextSize);
                    mTextPaint.setColor(Color.BLUE);
                    canvas.drawText("" + onlySolution[y][x], tx + mCellWidth/2, ty+ mCellHeight/2 + texty, mTextPaint);
                }
            }
        }
        //Log.d("LogTest","OnDraw Complete");
    }

    public void initializeBoard(int h, int w){
        mWidth = w;
        mHeight = h;

        mBoard = new int[mHeight][mWidth];
        HorBoard = new int[mHeight][mWidth];
        VerBoard = new int[mHeight][mWidth];
        CheckBoard = new int[mHeight][mWidth];
        solveGrid = new int[mHeight][mWidth];
        onlySolution = new int[mHeight][mWidth];

        for (int x=0; x< h; x++){
            for (int y=0; y< w; y++){
                mBoard[x][y] = -1;
                HorBoard[x][y] = -1;
                VerBoard[x][y] = -1;
                CheckBoard[x][y] = 0;
                solveGrid[x][y] = 0;
            }
        }
        invalidate();
    }
    public void initializeSolve(int h, int w){
        for (int x=0; x< h; x++){
            for (int y=0; y< w; y++){
                solveGrid[x][y] = -1;
                onlySolution[x][y] = -1;
            }
        }
        invalidate();
    }

    public boolean onTouch(View v, MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            initializeSolve(mHeight, mWidth);
            mXNow = getBoardX(event.getX());
            mYNow = getBoardY(event.getY());
            String linetext = et.getText().toString();
            if(linetext.isEmpty()){ linetext = "-1";}
            int val =  Integer.parseInt(linetext);
            if(val > 45 || val < -1){
                Toast.makeText(getContext(),"Unavailable number",Toast.LENGTH_SHORT).show();
                return true;
            }
            if(val == -1) {
                mBoard[mYNow][mXNow] = -1;
                HorBoard[mYNow][mXNow] = -1;
                VerBoard[mYNow][mXNow] = -1;
            }
            else if(rb1.isChecked()){
                HorBoard[mYNow][mXNow] = val;
                mBoard[mYNow][mXNow] = -1;
            }
            else if(rb2.isChecked()){
                VerBoard[mYNow][mXNow] = val;
                mBoard[mYNow][mXNow] = -1;
            }
            else if(rb3.isChecked()){
                HorBoard[mYNow][mXNow] = -1;
                VerBoard[mYNow][mXNow] = -1;
                mBoard[mYNow][mXNow] = 0;
            }
            //checkGrid(mXNow,mYNow);
            invalidate();
            return true;
        }
        else return false;
    }

    int getBoardX(float scrx){
        int x = (int)((scrx) / mCellHeight);
        if (x < 0) x = 0;
        if (x > mWidth - 1) x= mWidth - 1;
        return x;
    }
    int getBoardY(float scry){
        int y = (int)((scry) / mCellWidth);
        if (y < 0) y = 0;
        if (y > mHeight - 1) y = mHeight - 1;
        return y;
    }

    public void solvePuzzle() throws Exception{
        solution = 0;
        for(int x = 0; x < mHeight; x++) {
            for (int y = 0; y < mWidth; y++) {
                solveGrid[x][y] = mBoard[x][y];
            }
        }
        solveCell(0,0,0);
        invalidate();
    }

    public void solveCell(int x, int y, int t) throws Exception{
        if (x == mHeight && t == 0) {
            solution += 1;
            for(int k = 0; k < mHeight; k++){
                for(int l = 0; l < mWidth; l++){
                    onlySolution[k][l] = solveGrid[k][l];
                }
            }
            if (solution > 1){
                initializeSolve(mHeight, mWidth);
                invalidate();
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Error");
                alert.setMessage("Multiple Solution Found");
                AlertDialog alt = alert.create();
                alt.show();
                throw new Exception("Solution Found");}
            else return;
        }
        if(solveGrid[x][y] == -1){
            if(t != 0) return;
            if(HorBoard[x][y] > 0) {t = HorBoard[x][y];}
            if(y < mWidth - 1) solveCell(x, y+1,t);
            else solveCell(x+1,0,t);
        }
        else{
            for (int a=1; a< 10; a++) {
                if(checkCol(a,x,y) && checkRow(a,x,y)) {
                    solveGrid[x][y] = a;
                    Log.d("logtest","solveGrid["+x+"]["+y+"] = "+solveGrid[x][y]);
                    if (y < mWidth - 1) solveCell(x, y + 1, t - a);
                    else {solveCell(x + 1, 0, t-a);}
                    invalidate();
                }
            }
            solveGrid[x][y] = 0;
            Log.d("logtest","solve back");
            invalidate();
        }
    }

    public boolean checkCol(int x, int row, int col){
        int startIndex = 0;
        int endIndex = 0;
        int isEmpty = 0;
        for(int m = row; m >= 0; m--){
            if(VerBoard[m][col] > 0) {
                startIndex = m;
                break;
            }
        }
        for(int m = row; m < mHeight; m++){
            if(mBoard[m][col] == -1) {
                endIndex = m -1;
                break;
            }
            else if(m == mHeight -1){
                endIndex = m;
                break;
            }
        }
        int sum = 0;
        for(int r = startIndex + 1; r <= endIndex; r++){
            if(r != row){
                if(solveGrid[r][col] == 0) { isEmpty = 1; }
                sum += solveGrid[r][col];
            }
            if(solveGrid[r][col] == x){
                return false;
            }
            else if(sum + x > VerBoard[startIndex][col]){
                return false;
            }
        }
        if(isEmpty == 0 && sum + x < VerBoard[startIndex][col]){
            return false;
        }
        return true;
    }

    public boolean checkRow(int x, int row, int col){
        int startIndex = 0;
        int endIndex = 0;
        for(int m = col; m >= 0; m--){
            if(HorBoard[row][m] > 0) {
                startIndex = m;
                break;
            }
        }
        for(int m = col; m < mWidth; m++){
            if(mBoard[row][m] == -1) {
                endIndex = m - 1;
                break;
            }
            else if( m == mWidth -1){
                endIndex = m;
                break;
            }
        }

        int sum = 0;
        for(int r = startIndex + 1; r <= endIndex; r++){
            if(r!= col){sum += solveGrid[row][r];}
            if(solveGrid[row][r] == x){
                return false;
            }
            else if(sum + x > HorBoard[row][startIndex]){
                return false;
            }
        }
        return true;
    }

    public String getLines(){
        String output = "";
        int val;
        for(int i = 0; i < mHeight; i++) {
            for (int j = 0; j < mWidth; j++) {
                 val = 0;
                if(HorBoard[i][j] == -1 && VerBoard[i][j] == -1){
                    if(mBoard[i][j] == -1){
                        output += "-3 ";
                    }
                    else {output += "-2 ";}
                }
                else {
                    output += HorBoard[i][j] + " " + VerBoard[i][j] + " ";
                }
            }
        }
        return output;
    }

    public void setData(String str){
        String[] arr = str.split(" ");
        int r = 0;
        int c = 0;
        for(int l = 0; l < arr.length; l++) {
            switch (Integer.parseInt(arr[l])){
                case -3:
                    mBoard[r][c] = -1;
                    HorBoard[r][c] = -1;
                    VerBoard[r][c] = -1;
                    break;
                case -2:
                    HorBoard[r][c] = -1;
                    VerBoard[r][c] = -1;
                    mBoard[r][c] = 0;
                    break;
                default:
                    HorBoard[r][c] = Integer.parseInt(arr[l]);
                    l++;
                    VerBoard[r][c] = Integer.parseInt(arr[l]);
                    mBoard[r][c] = -1;
            }
            if(c < mWidth - 1){
                c++;
            }
            else {
                r++;
                c = 0;
            }
        }
        invalidate();

    }
}
