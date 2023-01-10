package com.remotedesktopcontrol.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.MotionEventCompat;

import com.remotedesktopcontrol.R;
import com.remotedesktopcontrol.message.MouseClick;
import com.remotedesktopcontrol.message.MouseCoordinate;
import com.remotedesktopcontrol.message.TypeBouton;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Remote extends Activity {

    private MouseCoordinate coord1;
    private MouseCoordinate coord2;
    private MouseCoordinate coordPrim2;
    private MouseCoordinate coordSec2;
    private int sensScroll = 4;
    private int pointerPrincipal;
    private int pointeurSecondaire;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        final ButtonState leftclick = (ButtonState) findViewById(R.id.buttonLeftClick);
        final ButtonState rightclick = (ButtonState) findViewById(R.id.buttonRightClick);
        final TextView pad = (TextView) findViewById(R.id.textViewPad);
        leftclick.setButton(TypeBouton.BOUTON_1, pad);
        rightclick.setButton(TypeBouton.BOUTON_3, pad);


        pointerPrincipal= -100;
        pointeurSecondaire= -100;
        pad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Thread track = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            int index;
                            int action = event.getActionMasked();
                            switch (action) {
                                case MotionEvent.ACTION_POINTER_DOWN:
                                    int x = (int)event.getX(event.getActionIndex());
                                    int y = (int)event.getY(event.getActionIndex());
                                    if (leftclick.isHover(x,y)) {

                                    } else {
                                        System.out.println("MotionEvent.ACTION_POINTER_DOWN");
                                        pointerPrincipal = event.getPointerId(0);
                                        pointeurSecondaire = event.getPointerId(1);
                                        coord1 = new MouseCoordinate((int) event.getX(pointerPrincipal), (int) event.getY(pointerPrincipal));
                                        coord2 = new MouseCoordinate((int) event.getX(pointeurSecondaire), (int) event.getY(pointeurSecondaire));

                                        System.out.println("INDEX : "+pointerPrincipal);
                                        System.out.println("INDEX : "+pointeurSecondaire);
                                    }

                                    break;
                                case MotionEvent.ACTION_POINTER_UP:
                                    System.out.println("MotionEvent.ACTION_POINTER_UP");
                                    pointerPrincipal = -100;
                                    pointeurSecondaire = -100;


                                    break;
                                case MotionEvent.ACTION_DOWN:

                                    pointerPrincipal = -100;
                                    pointeurSecondaire = -100;
                                    System.out.println("MotionEvent.ACTION_DOWN");
                                    index = event.getActionIndex();
                                    coord2 = new MouseCoordinate((int) event.getX(index), (int) event.getY(index));

                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    //System.out.println("MotionEvent.ACTION_MOVE");
                                    if (event.getPointerCount()==2) {
                                        int indexPrims = event.findPointerIndex(pointerPrincipal);
                                        int indexSecs = event.findPointerIndex(pointeurSecondaire);
                                        coordPrim2 = new MouseCoordinate((int) event.getX(indexPrims), (int) event.getY(indexPrims));
                                        coordSec2 = new MouseCoordinate((int) event.getX(indexSecs), (int) event.getY(indexSecs));
                                        coord1 = coord1.minus(coordPrim2);
                                        coord2 = coord2.minus(coordSec2);
                                        if (coord1.getY() * coord2.getY() > 0) {
                                            int moy = ((coord1.getY() + coord2.getY()) / (2 * sensScroll));
                                            Accueil.getClient().addQueue(new MouseClick(TypeBouton.SCROLL, moy));
                                        }
                                        coord1 = coordPrim2;
                                        coord2 = coordSec2;
                                    } else {

                                        index = event.getActionIndex();
                                        System.out.println(event.getPointerCount());
                                        coord1 = coord2;
                                        coord2 = new MouseCoordinate((int) event.getX(), (int) event.getY());
                                        //System.out.println("INDEX : "+index1);
                                        Accueil.getClient().addQueue(coord2.minus(coord1));
                                    }
                                    break;
                                case MotionEvent.ACTION_CANCEL:
                                    System.out.println("MotionEvent.ACTION_CANCEL");
                                    pointerPrincipal = -100;
                                    pointeurSecondaire = -100;
                                    break;
                                case MotionEvent.ACTION_UP:

                                    break;
                                default:
                                    System.out.println("DEFAULT : " + action);
                                    break;

                            }
                        }
                    }
                });
            track.start();
            return true;
            }
        });
        Resources res = getResources();

        runOnUiThread(new Runnable() {
            public void run() {
                Toast errorToast = Toast.makeText(Remote.this, res.getString(R.string.Remote_connection)+" "+Accueil.getClient().toString(), Toast.LENGTH_SHORT);
                errorToast.show();
            }
        });
    }


    public boolean shouldMoving(ButtonState leftclick, ButtonState rightclick, MotionEvent event) {
        if ((leftclick.isPresseds() && event.getPointerCount()==2) || (leftclick.isPresseds() && rightclick.isPresseds() && event.getPointerCount()==3)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Thread closeConnection = new Thread (new Runnable(){
            @Override
            public void run() {
                Accueil.getClient().close();
            }
        });
        closeConnection.start();
    }



}
