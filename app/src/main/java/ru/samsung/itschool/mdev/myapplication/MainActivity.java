package ru.samsung.itschool.mdev.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btn1, btn2;
    private TextView tv;
    private Handler h;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        tv = findViewById(R.id.textView);
        // Looper - запускает цикл обработки сообщений
        // и запускает его в главном потоке UI - getMainLooper()

        h = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {

                // Прием текстового сообщения
                String m1 = (String) msg.obj;
                tv.setText(m1);

                tv.setText("Шаг итерации i="+msg.what);
            }
        };

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for(int i=0; i<=10; i++) {
                                doSlow();
                                h.sendEmptyMessage(i);

                                // Отправка текстового сообщения в UI поток
                                String message = "Hello string!";
                                Message msg = Message.obtain();
                                msg.obj = message; // кладем строку в объект msg
                                msg.setTarget(h);
                                msg.sendToTarget();

                                // нельзя менять UI из другого потока
                                // tv.setText("Шаг итерации i="+i); ТАК ДЕЛАТЬ НЕЛЬЗЯ!!!

                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();




            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RRR","Нажали на вторую кнопку!");
            }
        });
    }

    public void doSlow() throws InterruptedException {
        Thread.sleep(1000);
    }
}