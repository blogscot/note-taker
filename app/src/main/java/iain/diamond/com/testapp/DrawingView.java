package iain.diamond.com.testapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * DrawingView is a custom view class which allows users to create simple drawing notes.
 */

public class DrawingView extends View {

  private Path drawPath;
  private Paint drawPaint, canvasPaint;
  private Canvas drawCanvas;
  private Bitmap canvasBitmap;

  public DrawingView(Context context, AttributeSet attrs) {
    super(context, attrs);

    setFocusable(true);
    drawPath = new Path();
    drawPaint = new Paint();

    drawPaint.setColor(Color.BLACK);
    drawPaint.setAntiAlias(true);
    drawPaint.setStrokeWidth(10);
    drawPaint.setStyle(Paint.Style.STROKE);
    drawPaint.setStrokeJoin(Paint.Join.ROUND);
    drawPaint.setStrokeCap(Paint.Cap.ROUND);

    canvasPaint = new Paint(Paint.DITHER_FLAG);
  }

  public void startNew(){
    drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    invalidate();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    drawCanvas = new Canvas(canvasBitmap);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
    canvas.drawPath(drawPath, drawPaint);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    float touchX = event.getX();
    float touchY = event.getY();

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        drawPath.moveTo(touchX, touchY);
        break;
      case MotionEvent.ACTION_MOVE:
        drawPath.lineTo(touchX, touchY);
        break;
      case MotionEvent.ACTION_UP:
        drawCanvas.drawPath(drawPath, drawPaint);
        drawPath.reset();
        break;
      default:
        return false;
    }
    invalidate();
    return true;
  }
}
