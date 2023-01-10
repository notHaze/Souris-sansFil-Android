package com.remotedesktopcontrol.application;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.remotedesktopcontrol.R;
import com.remotedesktopcontrol.message.MouseClick;
import com.remotedesktopcontrol.message.TypeBouton;

import java.util.concurrent.atomic.AtomicReference;

public class ButtonState extends androidx.appcompat.widget.AppCompatButton {

    private TypeBouton bouton;
    private boolean state;
    private int height ;
    private int width ;
    private int posX ;
    private int posY ;
    private TextView pad;


    public ButtonState(@NonNull Context c) {
        super(c);
    }

    public ButtonState(@NonNull Context c, AttributeSet attrs) {
        super(c,attrs);
    }

    public ButtonState(@NonNull Context c, AttributeSet attrs, int defStyleAttr) {
        super(c,attrs,defStyleAttr);
    }

    public void setButton(TypeBouton bouton, TextView pad) {
        this.bouton = bouton;
        this.state=false;
        this.pad = pad;
        setTouch();
    }

    private void setTouch() {

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AtomicReference<Boolean> valueReturn = new AtomicReference<>(true);
                Thread networkSend = new Thread(() -> {
                    int action = event.getActionMasked();

                    MouseClick click;
                    int x;
                    int y;
                    System.out.println("Count : "+event.getPointerCount());
                    switch (action) {

                        case MotionEvent.ACTION_DOWN:
                            click = new MouseClick(getBouton(), 1);
                            Accueil.getClient().addQueue(click);
                            state=true;
                            System.out.println(getBouton() + " : ACTION_DOWN");
                            System.out.println(event.getPointerId(event.getActionIndex()));
                            break;
                        case MotionEvent.ACTION_UP:
                            click = new MouseClick(getBouton(), 0);
                            Accueil.getClient().addQueue(click);
                            state=false;
                            System.out.println(getBouton() + " : ACTION_UP");
                            v.performClick();
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            int index = event.getActionIndex();
                            x = (int) event.getX(index);
                            y = (int) event.getY(index);
                            System.out.println("x : "+x+" ,posX : "+posX+" ,posY : "+posY+" width : "+width);
                            if (isHover(x,y)) {
                                if(!isPressed()) {
                                    click = new MouseClick(getBouton(), 1);
                                    Accueil.getClient().addQueue(click);
                                    state=true;
                                }

                            } else {
                                valueReturn.set(false);
                            }
                            System.out.println(getBouton() + " : ACTION_POINTER_DOWN");
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            boolean onePresent = false;
                            if(isPressed()) {

                                for (int i = 0; i < event.getPointerCount(); i++) {
                                    x = (int) event.getX(i);
                                    y = (int) event.getY(i);
                                    if (isHover(x,y)) {
                                        onePresent = true;
                                        break;
                                    }
                                }
                            }
                            if (!onePresent) {
                                click = new MouseClick(getBouton(), 0);
                                Accueil.getClient().addQueue(click);
                                state=false;
                            } else {
                                valueReturn.set(false);
                            }
                            System.out.println(getBouton() + " : ACTION_POINTER_UP");
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        default:
                            System.out.println("DEFAULT");
                    }
                });
                networkSend.start();
                return valueReturn.get();
            }
        });

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver vto = getViewTreeObserver();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height = getHeight();
                width = getWidth();
                posX = (int) getX();
                posY = (int) getY(); //height is ready
            }
        });
    }

    public Boolean isHover(int x, int y) {
        if ((x < posX + width && x > posX) && (y < width + posY && y > posY)) {
            return true;
        }
        return false;
    }


    public TypeBouton getBouton() {
        return bouton;
    }

    public boolean isPresseds() {
        return state;
    }

    public void setPressed(boolean state) {
        this.state = state;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }


}
