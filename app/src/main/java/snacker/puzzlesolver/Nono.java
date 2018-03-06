package snacker.puzzlesolver;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snacker on 2017-05-19.
 */

public class Nono extends View implements View.OnTouchListener{
    int mWidth = 10;
    int mHeight = 10;
    int mCellWidth, mCellHeight;
    int mCellMargin;
    int mEdgeThick;
    int mStatus = 0;
    int mTextSize;
    int mXNow = -1, mYNow = -1;
    int solution = 0;
    int loop = 0;
    int solveGrid[][];
    int unsolve;
    int rowM = 0;
    List<ArrayList<Integer>> col = new ArrayList<ArrayList<Integer>>();
    List<ArrayList<Integer>> row = new ArrayList<ArrayList<Integer>>();
    List<ArrayList<int[]>> colPossible = new ArrayList<ArrayList<int[]>>();
    List<ArrayList<int[]>> rowPossible = new ArrayList<ArrayList<int[]>>();

    Point mBoardPt;
    Paint mTextPaint, mTilePaint, mTileEdgePaint;

    EditText et;

    final static int VALID = 0;
    final static int INVALID = 1;

    public Nono(){
        super(null);
    };

    public Nono(Context context){
        super(context);
        //initializeBoard(10,10);
        mCellWidth = mCellHeight = this.getWidth() / (mWidth);
    }

    public Nono(Context context, AttributeSet attrs){
        super(context, attrs);
        for(int x = 0; x < mWidth; x++){
            ArrayList<Integer> list = new ArrayList<Integer>();
            list.add(0);
            col.add(list);
        }
        for(int x = 0; x < mHeight; x++){
            ArrayList<Integer> list = new ArrayList<Integer>();
            list.add(0);
            row.add(list);
        }
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
        if(mStatus >= 1){
        rowM = 1;
            DisplayMetrics dm = getResources().getDisplayMetrics();
        int num = 0;
            for(num = 0; num <mWidth; num++) {
                if(col.get(num).size() > rowM) rowM = col.get(num).size();
            }
            for(num = 0; num <mHeight; num++) {
                if(row.get(num).size() > rowM) rowM = row.get(num).size();
            }

            int mBigger = mWidth>mHeight?mWidth:mHeight;
        mCellWidth = mCellHeight = this.getWidth() / (mBigger + rowM);
        mCellMargin = (int)(mCellWidth * 0.05f);
        mEdgeThick = (int)(mCellWidth * 0.05f);
        mTextSize = mCellHeight / 2;
        mTextPaint = new Paint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTilePaint = new Paint();
        mTilePaint.setStyle(Paint.Style.FILL);
        mTileEdgePaint = new Paint();
        mTileEdgePaint.setStyle(Paint.Style.STROKE);



        mBoardPt = new Point(mCellWidth * (rowM),mCellWidth* (rowM));

        float texty = mTextSize / 2;
        for(int x = 0; x< mWidth; x++){
            for (int y=0; y< mHeight; y++){
                int tx = mBoardPt.x + mCellWidth * x;
                int ty = mBoardPt.y + mCellWidth * y;
                Rect rt= new Rect(tx, ty, tx + mCellWidth, ty + mCellHeight);
                //rt.inset(mCellMargin,mCellMargin);

                mTextPaint.setTextSize(mTextSize);
                mTileEdgePaint.setStrokeWidth(mEdgeThick);


                canvas.drawRect(rt, mTileEdgePaint);
            }
        }

        for(int x = 0; x < mWidth; x++) {
            for(int y = col.get(x).size() - 1; y >= 0; y--){
                int tx = mBoardPt.x + mCellWidth * x;
                int ty = mBoardPt.y - mCellWidth * (col.get(x).size() - y);
                canvas.drawText("" + col.get(x).get(y), tx + mCellWidth/2, ty+ mCellHeight/2 + texty, mTextPaint);
            }
        }
        for(int x = 0; x < mHeight; x++) {
            for(int y = row.get(x).size() - 1; y >= 0; y--){
                int tx = mBoardPt.x - mCellWidth * (row.get(x).size() - y);
                int ty = mBoardPt.y + mCellWidth * x;
                canvas.drawText("" + row.get(x).get(y), tx + mCellWidth/2, ty+ mCellHeight/2 + texty, mTextPaint);
            }
        }

        if(mStatus >= 2){
            for(int x = 0; x < mWidth; x++){
                for(int y = 0; y < mHeight; y++){
                    int tx = mBoardPt.x + mCellWidth * x;
                    int ty = mBoardPt.y + mCellWidth * y;
                    Rect rt= new Rect(tx, ty, tx + mCellWidth, ty + mCellHeight);
                    if(solveGrid[y][x] == 1) {
                        canvas.drawRect(rt,mTilePaint);
                    }
                }
            }
        }
        Log.d("LogTest","OnDraw Complete");
        }
        else{;}
    }

    public void initializeBoard(int w, int h){
        mWidth = w;
        mHeight = h;

        mStatus = 1;
        col.clear();
        colPossible.clear();
        rowPossible.clear();
        for(int x = 0; x < w; x++){
            ArrayList<Integer> list = new ArrayList<Integer>();
            ArrayList<int[]> list2 = new ArrayList<int[]>();
            int[] arr = {0,h-1};
            list.add(0);
            list2.add(arr);
            col.add(list);
            colPossible.add(list2);
        }
        row.clear();
        for(int x = 0; x < h; x++){
            ArrayList<Integer> list = new ArrayList<Integer>();
            ArrayList<int[]> list2 = new ArrayList<int[]>();
            int[] arr = {0,w-1};
            list.add(0);
            list2.add( arr);
            row.add(list);
            rowPossible.add(list2);
        }
        solveGrid = new int[h][w];
        for(int x = 0; x < w; x++){
            for(int y = 0; y < h; y++){
                solveGrid[y][x] = 0;
            }
        }
        invalidate();
    }
    public boolean onTouch(View v, MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            mXNow = getCol(event.getX());
            mYNow = getRow(event.getY());
            int pt = 0;
            if(mYNow == -1 && mXNow != -1){
                col.get(mXNow).clear();
                colPossible.get(mXNow).clear();
                String linetext = et.getText().toString();
                String[] split = linetext.split(" ");
                int diff = mHeight;
                int len = 0;
                while(len < split.length) {
                    col.get(mXNow).add(Integer.parseInt(split[len]));
                    diff -= Integer.parseInt(split[len]);
                    len++;
                    diff--;
                }
                diff++;
                for(int i = 0; i < split.length; i++){
                    int[] arr = {pt , pt + Integer.parseInt(split[i]) + diff - 1};
                   colPossible.get(mXNow).add(arr);
                    pt+= Integer.parseInt(split[i]) + 1;
                }
            }
            else if(mYNow != -1 && mXNow == -1){
                row.get(mYNow).clear();
                rowPossible.get(mYNow).clear();
                String linetext = et.getText().toString();
                String[] split = linetext.split(" ");
                int diff = mWidth;
                int len = 0;
                while(len < split.length) {
                    row.get(mYNow).add(Integer.parseInt(split[len]));
                    diff -= Integer.parseInt(split[len]);
                    len++;
                    diff--;
                }
                diff++;
                for(int i = 0; i < split.length; i++){
                    int[] arr = {pt , pt + Integer.parseInt(split[i]) + diff - 1};
                    rowPossible.get(mYNow).add(arr);
                    pt+= Integer.parseInt(split[i]) + 1;
                }
            }
            invalidate();
            return true;
        }
        return false;
    }

    public void setEditText(EditText e){
        et = e;
    }

    int getRow(float scrx){
        int val;
        float x = ((scrx - mCellWidth *rowM) / mCellHeight);
        if (x < 0) val = -1;
        else if (x > mHeight) val= -1;
        else val = (int)x;
        return val;
    }
    int getCol(float scry){
        int val;
        float y = ((scry - mCellWidth * (rowM)) / mCellWidth);
        if (y < 0) val = -1;
        else if (y > mWidth) val = -1;
        else val = (int) y;
        return val;
    }

    public void solvePuzzle(){
        solution = 0;
        unsolve = mHeight * mWidth;
        solveGrid = new int[mHeight][mWidth];
        int[] colBlocksum = new int[mWidth];
        int[] rowBlocksum = new int[mHeight];

        for(int x = 0; x < mHeight; x++){
            for(int y = 0; y < mWidth; y++){
                solveGrid[x][y] = 0;
            }
        }
        for (int x = 0; x < mHeight; x++) {
            for (int y = 0; y < row.get(x).size(); y++) {
                rowBlocksum[x] = rowBlocksum[x] + row.get(x).get(y) + 1;
            }
            rowBlocksum[x]--;
        }
        for (int x = 0; x < mWidth; x++) {
            for (int y = 0; y < col.get(x).size(); y++) {
                colBlocksum[x] = colBlocksum[x] + col.get(x).get(y) + 1;
            }
            colBlocksum[x]--;
        }

        if(checkCol() && checkRow()) {
            mStatus = 2;
            while (unsolve > 0) {
                for (int x = 0; x < mWidth; x++) {
                    simpleColBoxes(x);
                }
                for (int y = 0; y < mHeight; y++) {
                    simpleRowBoxes(y);
                }
                for (int k = 0; k < mWidth; k++) {
                    updatePossibleCol(k);
                }
                for (int k = 0; k < mHeight; k++) {
                    updatePossibleRow(k);
                }
                for (int k = 0; k < mWidth; k++) {
                    simpleSpaceCol(k);
                }
                for (int k = 0; k < mHeight; k++) {
                    simpleSpaceRow(k);
                }
                for (int k = 0; k < mWidth; k++) {
                    updatePossibleCol(k);
                }
                for (int k = 0; k < mHeight; k++) {
                    updatePossibleRow(k);
                }
                loop++;
                if(loop > 10){
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Error");
                    alert.setMessage("This puzzle has multiple solutions");
                    AlertDialog alt = alert.create();
                    alt.show();
                    break;}
                invalidate();
            }

            }
        else{
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Error");
            alert.setMessage("Some lines are invalid. Edit the puzzle");
            AlertDialog alt = alert.create();
            alt.show();
            //throw new Exception("Some lines are invalid");
        }
    }

    public boolean checkCol(){
        for(int x = 0; x < mWidth; x++){
            int blocksum = 0;
            for(int y = 0; y < col.get(x).size(); y++){
                blocksum = blocksum + col.get(x).get(y) + 1;
            }
            blocksum--;
            if(blocksum > mHeight){
                return false;
            }
            else{
                return true;
            }
        }
        return true;
    }

    public boolean checkRow() {
        for (int x = 0; x < mHeight; x++) {
            int blocksum = 0;
            for (int y = 0; y < row.get(x).size(); y++) {
                blocksum = blocksum + row.get(x).get(y) + 1;
            }
            blocksum--;
            if (blocksum > mWidth) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public void simpleColBoxes(int r){
        for(int y = 0; y < col.get(r).size(); y++) {
            for (int min = colPossible.get(r).get(y)[1] - col.get(r).get(y) + 1; min <= colPossible.get(r).get(y)[0] + col.get(r).get(y) -1; min++) {
                if(solveGrid[min][r] == 0) {
                    solveGrid[min][r] = 1;
                    unsolve--;
                    loop = -1;
                }
            }
        }
    }

    public void simpleRowBoxes(int r){
        for(int y = 0; y < row.get(r).size(); y++) {
            for (int min = rowPossible.get(r).get(y)[1] - row.get(r).get(y) + 1; min <= rowPossible.get(r).get(y)[0] + row.get(r).get(y) -1; min++) {
                if(solveGrid[r][min] == 0) {
                    solveGrid[r][min] = 1;
                    unsolve--;
                    loop = -1;
                }
            }
        }
    }

    public void updatePossibleCol(int r){
        int len = 0;
        int start = -1;
        int end = -1;
        int pos = 0;
        int n = 0;
        int onlyn = 0;
        if (solveGrid[0][r] == 1){ start = 0; len++;}
        if (solveGrid[0][r] == -1){
            for(n = 0 ; n < col.get(r).size(); n++){
                if (colPossible.get(r).get(n)[0] == 0){
                    int[] arr = {1, colPossible.get(r).get(n)[1]};
                    colPossible.get(r).set(n, arr);
                }
            }
        }
        for(int y = 1; y < mHeight; y++){
            if(solveGrid[y][r] == 1){
                if(solveGrid[y][r] > solveGrid[y-1][r]){
                    start = y;
                }
                if(y == mHeight -1){
                    end = y;
                    int[] arr = {y + 1 - col.get(r).get(col.get(r).size() - 1), y};
                    colPossible.get(r).set(col.get(r).size() - 1, arr);
                }
                len++;
            }
            else if(solveGrid[y][r] == -1){
                if(solveGrid[y -1][r] == 1){
                    end = y - 1;
                    for(n = 0 ; n < col.get(r).size(); n++){
                        if(len > col.get(r).get(n)){ continue; }
                        else if (colPossible.get(r).get(n)[0] <= start && colPossible.get(r).get(n)[1] >= end ){
                            pos++;
                            onlyn = n;
                        }
                        else{ continue; }
                    }
                    if(pos == 1){
                        int s = end + 1 - col.get(r).get(onlyn);
                        int e = start + col.get(r).get(onlyn) - 1;
                        float m = (float)(s + e) / 2;
                        if(s < 0) {s = 0;}
                        if(e >= mWidth) {e = mHeight -1;}
                        for(int p = s; p <= e;p++){
                            if(p < m && solveGrid[p][r] == -1){
                                s = p + 1;
                            }
                            else if(p >= m && solveGrid[p][r] == -1) {
                                e = p - 1;
                            }
                        }
                        int[] arr = {s, e};
                        colPossible.get(r).set(onlyn, arr);
                        pos = 0;
                    }
                    len = 0;
                }
                else {
                    for (n = 0; n < col.get(r).size(); n++) {
                        float med = (float) (colPossible.get(r).get(n)[0] + colPossible.get(r).get(n)[1]) / 2;
                        if (y < med && y >= colPossible.get(r).get(n)[0]) {
                            if (y - colPossible.get(r).get(n)[0] < col.get(r).get(n)) {
                                int[] arr = {y + 1, colPossible.get(r).get(n)[1]};
                                colPossible.get(r).set(n, arr);
                            }
                        } else if (y > med && y <= colPossible.get(r).get(n)[1]) {
                            if (colPossible.get(r).get(n)[1] - y < col.get(r).get(n)) {
                                int[] arr = {colPossible.get(r).get(n)[0], y - 1};
                                colPossible.get(r).set(n, arr);
                            }
                        }
                    }
                }
            }
            else if(solveGrid[y][r] == solveGrid[y-1][r] - 1){
                end = y - 1;
                for(n = 0 ; n < col.get(r).size(); n++){
                    if(len > col.get(r).get(n)){ continue; }
                    else if (colPossible.get(r).get(n)[0] <= start && colPossible.get(r).get(n)[1] >= end ){
                        pos++;
                        onlyn = n;
                    }
                    else{ continue; }
                }
                if(pos == 1){
                    int s = end + 1 - col.get(r).get(onlyn);
                    int e = start + col.get(r).get(onlyn) - 1;
                    float m = (float)(s + e) / 2;
                    if(s < 0) {s = 0;}
                    if(e >= mWidth) {e = mHeight -1;}
                    for(int p = s; p <= e;p++){
                        if(p < m && solveGrid[p][r] == -1){
                            s = p + 1;
                        }
                        else if(p >= m && solveGrid[p][r] == -1) {
                            e = p - 1;
                        }
                    }
                    int[] arr = {s, e};
                    colPossible.get(r).set(onlyn, arr);
                    pos = 0;
                }
                len = 0;
            }
        }
    }
    public void updatePossibleRow(int r){
        int len = 0;
        int start = -1;
        int end = -1;
        int pos = 0;
        int n = 0;
        int onlyn = 0;
        if (solveGrid[r][0] == 1){ start = 0; len++;}
        if (solveGrid[r][0] == -1){
            for(n = 0 ; n < row.get(r).size(); n++){
                if (rowPossible.get(r).get(n)[0] == 0){
                    int[] arr = {1, rowPossible.get(r).get(n)[1]};
                    rowPossible.get(r).set(n, arr);
                }
            }
        }
        for(int y = 1; y < mWidth; y++){
            if(solveGrid[r][y] == 1){
                if(solveGrid[r][y] > solveGrid[r][y - 1]){
                    start = y;
                }
                len++;
                if(y == mWidth -1){
                    int[] arr = {y + 1 - row.get(r).get(row.get(r).size() - 1), y};
                    rowPossible.get(r).set(row.get(r).size() - 1, arr);
                }
            }
            else if(solveGrid[r][y] == -1){
                if(solveGrid[r][y-1] == 1) {
                    end = y - 1;
                    for(n = 0 ; n < row.get(r).size(); n++){
                        if(len > row.get(r).get(n)){ continue; }
                        else if (rowPossible.get(r).get(n)[0] <= start && rowPossible.get(r).get(n)[1] >= end ){
                            pos++;
                            onlyn = n;
                        }
                        else{ continue; }
                    }
                    if(pos == 1){
                        int s = end + 1 - row.get(r).get(onlyn);
                        int e = start + row.get(r).get(onlyn) - 1;
                        float m = (float)(s + e) / 2;
                        if(s < 0) {s = 0;}
                        if(e >= mWidth) {e = mWidth -1;}
                        for(int p = s; p <= e;p++){
                            if(p < m && solveGrid[r][p] == -1){
                                s = p + 1;
                            }
                            else if(p >= m && solveGrid[r][p] == -1) {
                                e = p - 1;
                            }
                        }
                        int[] arr = {s, e};
                        rowPossible.get(r).set(onlyn, arr);
                        pos = 0;
                    }
                    len = 0;
                }
                else{
                    for(n = 0; n < row.get(r).size(); n++){
                        float med = (float)(rowPossible.get(r).get(n)[0] + rowPossible.get(r).get(n)[1]) / 2;
                        if(y < med && y >= rowPossible.get(r).get(n)[0]){
                            if(y - rowPossible.get(r).get(n)[0] < row.get(r).get(n)) {
                                int[] arr = {y + 1, rowPossible.get(r).get(n)[1]};
                                rowPossible.get(r).set(n, arr);
                            }
                        }
                        else if(y > med && y <= rowPossible.get(r).get(n)[1]){
                            if(rowPossible.get(r).get(n)[1] - y < row.get(r).get(n)) {
                                int[] arr = {rowPossible.get(r).get(n)[0], y - 1};
                                rowPossible.get(r).set(n, arr);
                            }
                        }
                    }
                }
            }
            else if(solveGrid[r][y] == solveGrid[r][y - 1] - 1){
                end = y - 1;
                for(n = 0 ; n < row.get(r).size(); n++){
                    if(len > row.get(r).get(n)){ continue; }
                    else if (rowPossible.get(r).get(n)[0] <= start && rowPossible.get(r).get(n)[1] >= end ){
                        pos++;
                        onlyn = n;
                    }
                    else{ continue; }
                }
                if(pos == 1){
                    int s = end + 1 - row.get(r).get(onlyn);
                    int e = start + row.get(r).get(onlyn) - 1;
                    float m = (float)(s + e) / 2;
                    if(s < 0) {s = 0;}
                    if(e >= mWidth) {e = mWidth -1;}
                    for(int p = s; p <= e;p++){
                        if(p < m && solveGrid[r][p] == -1){
                            s = p + 1;
                        }
                        else if(p >= m && solveGrid[r][p] == -1) {
                            e = p - 1;
                        }
                    }
                    int[] arr = {s, e};
                    rowPossible.get(r).set(onlyn, arr);
                    pos = 0;
                }
                len = 0;
            }
        }
    }


    public void simpleSpaceCol(int r){
        int startone, endone, starttwo;
        if(col.get(r).size() == 1){
            for(int k = 0; k < colPossible.get(r).get(0)[0]; k++){
                if(solveGrid[k][r] == 0) {
                    solveGrid[k][r] = -1;
                    unsolve--;
                    loop = -1;
                }
            }
            for(int k = colPossible.get(r).get(0)[1] + 1; k < mHeight ; k++){
                if(solveGrid[k][r] == 0) {
                    solveGrid[k][r] = -1;
                    unsolve--;
                    loop = -1;
                }
            }
            if(col.get(r).get(0) == 0){
                for(int k = 0; k < mHeight; k++){
                    if(solveGrid[k][r] == 0) {
                        solveGrid[k][r] = -1;
                        unsolve--;
                        loop = -1;
                    }
                }
            }
        }
        for(int y = 0; y < col.get(r).size() - 1; y++){
            startone = colPossible.get(r).get(y)[0];
            endone = colPossible.get(r).get(y)[1];
            starttwo = colPossible.get(r).get(y + 1)[0];
            if(endone -startone + 1 == col.get(r).get(y)){
                if(startone != 0) {
                    if (solveGrid[startone - 1][r] == 0) {
                        solveGrid[startone - 1][r] = -1;
                        unsolve--;
                        loop = -1;
                    }
                }
                if(endone != mHeight -1){
                    if (solveGrid[endone + 1][r] == 0) {
                        solveGrid[endone + 1][r] = -1;
                        unsolve--;
                        loop = -1;
                    }
                }
            }

            for(int k = endone + 1; k < starttwo; k++){
                if(solveGrid[k][r] == 0) {
                    solveGrid[k][r] = -1;
                    unsolve--;
                    loop = -1;
                }
            }
        }
        startone = colPossible.get(r).get(col.get(r).size() - 1)[0];
        endone = colPossible.get(r).get(col.get(r).size() - 1)[1];
        if(endone -startone + 1 == col.get(r).get(col.get(r).size() - 1)){
            if(startone != 0) {
                if (solveGrid[startone - 1][r] == 0) {
                    solveGrid[startone - 1][r] = -1;
                    unsolve--;
                    loop = -1;
                }
            }
            if(endone != mHeight -1){
                if (solveGrid[endone + 1][r] == 0) {
                    solveGrid[endone + 1][r] = -1;
                    unsolve--;
                    loop = -1;
                }
            }
        }
        for(int p = colPossible.get(r).get(colPossible.get(r).size() - 1)[1] + 1;p < mHeight;p++){
            if(solveGrid[p][r] == 0) {
                solveGrid[p][r] = -1;
                unsolve--;
                loop = -1;
            }
        }
    }

    public void simpleSpaceRow(int r){
        int startone, endone, starttwo;
        if(row.get(r).size() == 1){
            for(int k = 0; k < rowPossible.get(r).get(0)[0]; k++){
                if(solveGrid[r][k] == 0) {
                    solveGrid[r][k] = -1;
                    unsolve--;
                    loop = -1;
                }
            }
            for(int k = rowPossible.get(r).get(0)[1] + 1; k < mWidth ; k++){
                if(solveGrid[r][k] == 0) {
                    solveGrid[r][k] = -1;
                    unsolve--;
                    loop = -1;
                }
            }
            if(row.get(r).get(0) == 0){
                for(int k = 0; k < mWidth; k++){
                    if(solveGrid[r][k] == 0) {
                        solveGrid[r][k] = -1;
                        unsolve--;
                        loop = -1;
                    }
                }
            }
        }
        for(int y = 0; y < row.get(r).size() - 1; y++){
            startone = rowPossible.get(r).get(y)[0];
            endone = rowPossible.get(r).get(y)[1];
            starttwo = rowPossible.get(r).get(y + 1)[0];
            if(endone -startone + 1 == row.get(r).get(y)){
                if(startone != 0) {
                    if (solveGrid[r][startone - 1] == 0) {
                        solveGrid[r][startone - 1] = -1;
                        unsolve--;
                        loop = -1;
                    }
                }
                if(endone != mWidth -1){
                    if (solveGrid[r][endone + 1] == 0) {
                        solveGrid[r][endone + 1] = -1;
                        unsolve--;
                        loop = -1;
                    }
                }
            }
            for(int k = endone + 1; k < starttwo; k++){
                if(solveGrid[r][k] == 0) {
                    solveGrid[r][k] = -1;
                    unsolve--;
                    loop = -1;
                }
            }
        }
        startone = rowPossible.get(r).get(row.get(r).size() - 1)[0];
        endone = rowPossible.get(r).get(row.get(r).size() - 1)[1];
        if(endone -startone + 1 == row.get(r).get(row.get(r).size() - 1)){
            if(startone != 0) {
                if (solveGrid[r][startone - 1] == 0) {
                    solveGrid[r][startone - 1] = -1;
                    unsolve--;
                    loop = -1;
                }
            }
            if(endone != mWidth -1){
                if (solveGrid[r][endone + 1] == 0) {
                    solveGrid[r][endone + 1] = -1;
                    unsolve--;
                    loop = -1;
                }
            }
        }
        for(int p = rowPossible.get(r).get(rowPossible.get(r).size() - 1)[1] + 1;p < mWidth;p++){
            if(solveGrid[r][p] == 0) {
                solveGrid[r][p] = -1;
                unsolve--;
                loop = -1;
            }
        }
    }

    // for external use
    public String getLines(){
        String output = "";
        for(int i = 0; i < mWidth; i++){
            for(int j = 0; j < col.get(i).size(); j++){
                output += col.get(i).get(j) + " ";
            }
            output += "/";
        }

        for(int i = 0; i < mHeight; i++){
            for(int j = 0; j < row.get(i).size(); j++){
                output += row.get(i).get(j) + " ";
            }
            output += "/";
        }

        return output;
    }

    public void setData(String str, int mXNow, int mYNow){
        {
            int pt = 0;
            if(mYNow == -1 && mXNow != -1){
                col.get(mXNow).clear();
                colPossible.get(mXNow).clear();
                String[] split = str.split(" ");
                int diff = mHeight;
                int len = 0;
                while(len < split.length) {
                    col.get(mXNow).add(Integer.parseInt(split[len]));
                    diff -= Integer.parseInt(split[len]);
                    len++;
                    diff--;
                }
                diff++;
                for(int i = 0; i < split.length; i++){
                    int[] arr = {pt , pt + Integer.parseInt(split[i]) + diff - 1};
                    colPossible.get(mXNow).add(arr);
                    pt+= Integer.parseInt(split[i]) + 1;
                }
            }
            else if(mYNow != -1 && mXNow == -1){
                row.get(mYNow).clear();
                rowPossible.get(mYNow).clear();
                String[] split = str.split(" ");
                int diff = mWidth;
                int len = 0;
                while(len < split.length) {
                    row.get(mYNow).add(Integer.parseInt(split[len]));
                    diff -= Integer.parseInt(split[len]);
                    len++;
                    diff--;
                }
                diff++;
                for(int i = 0; i < split.length; i++){
                    int[] arr = {pt , pt + Integer.parseInt(split[i]) + diff - 1};
                    rowPossible.get(mYNow).add(arr);
                    pt+= Integer.parseInt(split[i]) + 1;
                }
            }
            invalidate();
        }
    }
}
