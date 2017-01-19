package com.tip.capstone.mlearning.ui.simulation;

import android.content.ClipData;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.flexbox.FlexboxLayout;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.SimulationData;
import com.tip.capstone.mlearning.databinding.ActivityReviseSimulationBinding;

public class ReviseSimulationActivity extends AppCompatActivity
        implements View.OnDragListener, View.OnTouchListener {

    private ActivityReviseSimulationBinding binding;

    private Drawable enterShape;
    private Drawable normalShape;

    public static final int[] SIMULATION_DRAWABLES = {
            R.drawable.circle_red,
            R.drawable.circle_blue,
            R.drawable.circle_yellow};

    public static final int[] PANEL_2 = {R.drawable.circle_red};
    public static final int[] PANEL_3 = {R.drawable.circle_blue};
    public static final int[] PANEL_4 = {R.drawable.circle_yellow};

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_revise_simulation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Simulation"); // TODO: 01/12/2016 set title on manifest instead

        enterShape = ContextCompat.getDrawable(this, R.drawable.shape_droptarget);
        normalShape = ContextCompat.getDrawable(this, R.drawable.shape);

        binding.layoutTopLeft.setOnDragListener(this);
        binding.layoutTopRight.setOnDragListener(this);
        binding.layoutBottomLeft.setOnDragListener(this);
        binding.layoutBottomRight.setOnDragListener(this);

        for (int i = 0; i < SIMULATION_DRAWABLES.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(SIMULATION_DRAWABLES[i]);
            imageView.setImageDrawable(ContextCompat.getDrawable(this,
                    SimulationData.SIMULATION_DRAWABLES[i]));
            imageView.setOnTouchListener(this);
            binding.layoutTopLeft.addView(imageView);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        ImageView imageView = (ImageView) dragEvent.getLocalState();
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                if (isEnterable(view, imageView)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        view.setBackground(enterShape);
                    } else {
                        //noinspection deprecation
                        view.setBackgroundDrawable(enterShape);
                    }
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(normalShape);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(normalShape);
                }
                break;
            case DragEvent.ACTION_DROP:
                if (isEnterable(view, imageView)) {
                    // Dropped, reassign View to ViewGroup
                    View view1 = (View) dragEvent.getLocalState();
                    ViewGroup owner = (ViewGroup) view1.getParent();
                    owner.removeView(view1);
                    FlexboxLayout container = (FlexboxLayout) view;
                    container.addView(view1);
                    view1.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(normalShape);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(normalShape);
                }
                break;
        }
        return true;
    }

    private boolean isEnterable(View view, ImageView imageView) {
        switch (view.getId()) {
            case R.id.layout_top_left:
                return true;
            case R.id.layout_top_right:
                return asd(PANEL_2, imageView.getDrawable());
            case R.id.layout_bottom_left:
                return asd(PANEL_3, imageView.getDrawable());
            case R.id.layout_bottom_right:
                return asd(PANEL_4, imageView.getDrawable());
            default:
                return false;
        }
    }

    private boolean asd(int[] panels, Drawable drawable) {
        for (int i = 0; i < panels.length; i++) {
            if (drawable.getConstantState() == ContextCompat.getDrawable(this, panels[i]).getConstantState()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData clipData = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(clipData, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }
}
