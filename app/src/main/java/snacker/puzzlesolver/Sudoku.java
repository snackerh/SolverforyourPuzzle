package snacker.puzzlesolver;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Snacker on 2017-05-14.
 */

public class Sudoku extends View implements View.OnTouchListener {
    int mWidth = 9;
    int mHeight = 9;
    int mCellWidth, mCellHeight;
    int mCellMargin;
    int mEdgeThick;
    int mStatus;
    int mTextSize;
    int mXNow = -1, mYNow = -1;
    int[][] mBoard = new int[9][9];
    int[][] CheckBoard = new int[9][9];
    int[][] solveGrid = new int[9][9];
    int[][] onlySolution = new int[9][9];
    int solution = 0;
    Point mBoardPt;

    Paint mTextPaint, mInvalidTextPaint, mTileEdgePaint;

    final static int VALID = 0;
    final static int INVALID = 1;

    public Sudoku(){
        super(null);
    };

    public Sudoku(Context context){
        super(context);
        initializeBoard();
        mCellWidth = mCellHeight = this.getWidth() / (mWidth);
    }

    public Sudoku(Context context, AttributeSet attrs){
        super(context, attrs);
        initializeBoard();
        mCellWidth = mCellHeight = this.getWidth() / (mWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mCellWidth = mCellHeight = this.getWidth() / (mWidth);
        mCellMargin = (int)(mCellWidth * 0.05f);
        mEdgeThick = (int)(mCellWidth * 0.05f);
        mTextSize = mCellHeight / 2;
        mTextPaint = new Paint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mInvalidTextPaint = new Paint();
        mInvalidTextPaint.setTextAlign(Paint.Align.CENTER);
        mTileEdgePaint = new Paint();
        mTileEdgePaint.setStyle(Paint.Style.STROKE);

        mBoardPt = new Point(0,0);


        for(int x = 0; x< mWidth; x++){
            for (int y=0; y< mHeight; y++){
                int tx = mBoardPt.x + mCellWidth * x;
                int ty = mBoardPt.y + mCellWidth * y;
                Rect rt= new Rect(tx, ty, tx + mCellWidth, ty + mCellHeight);
                //rt.inset(mCellMargin,mCellMargin);

                mTextPaint.setTextSize(mTextSize);
                mTileEdgePaint.setStrokeWidth(mEdgeThick);
                float texty = mTextSize / 2;

                canvas.drawRect(rt, mTileEdgePaint);
                if(CheckBoard[x][y] <= 0){ mTextPaint.setColor(Color.BLACK); }
                else { mTextPaint.setColor(Color.RED); }
                if(mBoard[x][y] != 0){
                    canvas.drawText("" + mBoard[x][y], tx + mCellWidth/2, ty+ mCellHeight/2 + texty, mTextPaint);
                }
                else if(onlySolution[x][y] != 0 && mBoard[x][y] == 0){
                    mTextPaint.setColor(Color.BLUE);
                    canvas.drawText("" + onlySolution[x][y], tx + mCellWidth/2, ty+ mCellHeight/2 + texty, mTextPaint);
                }
            }
        }
        Log.d("LogTest","OnDraw Complete");
    }

    public void initializeBoard(){
        for (int x=0; x< mWidth; x++){
            for (int y=0; y< mHeight; y++){
                mBoard[x][y] = 0;
                CheckBoard[x][y] = 0;
                solveGrid[x][y] = 0;
            }
        }
        invalidate();
    }
    public void initializeSolve(){
        for (int x=0; x< mWidth; x++){
            for (int y=0; y< mHeight; y++){
                solveGrid[x][y] = 0;
                onlySolution[x][y] = 0;
            }
        }
        invalidate();
    }

    public boolean onTouch(View v, MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            initializeSolve();
            mXNow = getBoardX(event.getX());
            mYNow = getBoardY(event.getY());
            mBoard[mXNow][mYNow] = (mBoard[mXNow][mYNow] + 1) % 10;
            checkGrid(mXNow,mYNow);
            invalidate();
            return true;
        }
        else return false;
    }

    int getBoardX(float scrx){
        int x = (int)((scrx) / mCellWidth);
        if (x < 0) x = 0;
        if (x > 8) x= 8;
        return x;
    }
    int getBoardY(float scry){
        int y = (int)((scry) / mCellHeight);
        if (y < 0) y = 0;
        if (y > 8) y = 8;
        return y;
    }

    public void solvePuzzle() throws Exception{
        solution = 0;
        for(int x = 0; x < mWidth; x++){
            for(int y = 0; y < mWidth; y++){
                if (CheckBoard[x][y] >= 1){
                    Log.d("logtest","CheckBoard" + x +"," + y);
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Error");
                    alert.setMessage("Edit the puzzle");
                    AlertDialog alt = alert.create();
                    alt.show();
                    throw new Exception("Unsolvable");
                }
            }
        }
        Log.d("logtest","Begin solve");
        for(int x = 0; x < mWidth; x++) {
            for (int y = 0; y < mWidth; y++) {
                solveGrid[x][y] = mBoard[x][y];
            }
        }
        solveCell(0,0);
        invalidate();
    }

    public void solveCell(int x, int y) throws Exception{
        if (x == 9) {
            solution += 1;
            for(int k = 0; k < 9; k++){
                for(int l = 0; l < 9; l++){
                    onlySolution[k][l] = solveGrid[k][l];
                }
            }
            if (solution > 1){
                initializeSolve();
                invalidate();
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Error");
            alert.setMessage("Multiple Solution Found");
            AlertDialog alt = alert.create();
            alt.show();
            throw new Exception("Solution Found");}
            else return;
        }
        if(solveGrid[x][y] != 0){
            if(y < 8) solveCell(x, y+1);
            else solveCell(x+1,0);
        }
        else{
            for (int a=1; a< 10; a++) {
                if(checkCol(a,x) && checkRow(a,y) && checkBox(a,x,y) ) {
                    solveGrid[x][y] = a;
                    Log.d("logtest","solveGrid["+x+"]["+y+"] = "+solveGrid[x][y]);
                    if (y < 8) solveCell(x, y + 1);
                    else solveCell(x + 1, 0);
                    invalidate();
                }
            }
            solveGrid[x][y] = 0;
            Log.d("logtest","solve back");
            invalidate();
        }
    }

    public boolean checkCol(int x, int col){
        for(int r = 0; r < mWidth; r++){
            if(solveGrid[col][r] == x){
                return false;
            }
        }
        return true;
    }
    public boolean checkRow(int x, int row){
        for(int c = 0; c < mWidth; c++){
            if(solveGrid[c][row] == x){
                return false;
            }
        }
        return true;
    }
    public boolean checkBox(int x, int col, int row){
        for(int c = col - col%3; c < col - col %3 + 3; c++){
            for(int r = row - row%3; r < row - row %3 + 3; r++){
                if(solveGrid[c][r] == x) {
                return false;
                }
            }
        }
        return true;
    }

    public void checkGrid(int xNow, int yNow){
        int now = mBoard[xNow][yNow];
        if(now == 0){
            for (int x=0; x< mWidth; x++){
                if(x != xNow) {
                    if(mBoard[x][yNow] == 9){
                        CheckBoard[x][yNow] -= 1;
                        CheckBoard[xNow][yNow] -= 1;
                    }
                }
            }
            for (int y=0; y< mWidth; y++){
                if(y != yNow){
                    if(mBoard[xNow][y] == 9){
                        CheckBoard[xNow][y] -= 1;
                        CheckBoard[xNow][yNow] -= 1;
                    }
                }
            }
            for (int x= xNow - (xNow %3); x < xNow - (xNow %3) + 3; x++){
                for (int y= yNow - (yNow %3); y < yNow - (yNow %3)+ 3; y++){
                    if(mBoard[x][y] == 9){
                        CheckBoard[x][y] -= 1;
                        CheckBoard[xNow][yNow] -= 1;
                    }
                }
            }
        }
        else{
        for (int x=0; x< mWidth; x++){
            if(x != xNow) {
                if(mBoard[x][yNow] == now){
                    CheckBoard[x][yNow] += 1;
                    CheckBoard[xNow][yNow] += 1;
                }
                else{
                    if(mBoard[x][yNow] == now - 1 && now > 1){
                        CheckBoard[x][yNow] -= 1;
                        CheckBoard[xNow][yNow] -= 1;
                    }
                }
            }
        }
        for (int y=0; y< mWidth; y++){
            if(y != yNow) {
                if(mBoard[xNow][y] == now){
                    CheckBoard[xNow][y] += 1;
                    CheckBoard[xNow][yNow] += 1;
                }
                else{
                    if(mBoard[xNow][y] == now - 1 && now > 1){
                        CheckBoard[xNow][y] -= 1;
                        CheckBoard[xNow][yNow] -= 1;
                    }
                }
            }
        }
        for (int x= xNow - (xNow %3); x < xNow - (xNow %3) + 3; x++){
            for (int y= yNow - (yNow %3); y < yNow - (yNow %3)+ 3; y++){
                if(x != xNow || y != yNow) {
                    if(mBoard[x][y] == now){
                        CheckBoard[x][y] += 1;
                        CheckBoard[xNow][yNow] += 1;
                    }
                    else{
                        if(mBoard[x][y] == now - 1 && now > 1){
                            CheckBoard[x][y] -= 1;
                            CheckBoard[xNow][yNow] -= 1;
                        }
                    }
                }
            }
        }
    }
    }

    public String getLines(){
        String output = "";
        for(int i = 0; i < mWidth; i++) {
            for (int j = 0; j < mHeight; j++) {
                output += mBoard[i][j] + " ";
            }
        }
        return output;
    }

    public void setData(String str){
        String[] arr = str.split(" ");
        for(int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                mBoard[r][c] = Integer.parseInt(arr[9*r + c]);
                checkGrid(r, c);
            }
        }
        invalidate();

    }
}
