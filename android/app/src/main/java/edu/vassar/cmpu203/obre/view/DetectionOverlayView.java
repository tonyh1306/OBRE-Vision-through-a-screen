package edu.vassar.cmpu203.obre.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import edu.vassar.cmpu203.obre.model.DetectedObject;

public class DetectionOverlayView extends View {
    private List<DetectedObject> detectedObjects = new ArrayList<>();
    private final Paint boxPaint = new Paint();
    private final Paint textPaint = new Paint();

    public DetectionOverlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);

        boxPaint.setColor(Color.RED);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(5f);

        textPaint.setColor(Color.RED);
        textPaint.setTextSize(40f);
        textPaint.setStyle(Paint.Style.FILL);
    }

    public void setDetectedObjects(List<DetectedObject> objects) {
        this.detectedObjects = objects;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (detectedObjects == null || detectedObjects.isEmpty()) return;

        // Hardcoded Model Size (YOLO default)
        float modelWidth = 640f;
        float modelHeight = 640f;

        // Calculate scale to fit screen
        float scaleX = (float) getWidth() / modelWidth;
        float scaleY = (float) getHeight() / modelHeight;

        for (DetectedObject obj : detectedObjects) {
            if (obj.getName() == null || obj.getName().startsWith("Error") || obj.getName().startsWith("No objects")) continue;

            List<Double> dims = obj.getDim();
            if (dims.size() < 4) continue;

            float x = dims.get(0).floatValue();
            float y = dims.get(1).floatValue();
            float w = dims.get(2).floatValue();
            float h = dims.get(3).floatValue();

            float left = x * scaleX;
            float top = y * scaleY;
            float right = (x + w) * scaleX;
            float bottom = (y + h) * scaleY;

            canvas.drawRect(left, top, right, bottom, boxPaint);
            canvas.drawText(obj.getName(), left, top - 10, textPaint);
        }
    }
}
