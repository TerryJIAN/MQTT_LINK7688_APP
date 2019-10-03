package com.yln.mqttdemo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yln.mqttdemo.mqtt.MqttListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MqttListener{

    private Switch switchA;
    private Switch switchB;
    private Switch switchC;
    private Switch switchD;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private Button buttonA;
    private TextView mMessageRV1;
    private TextView mMessageRV2;
    private TextView mMessageRV3;

    private CardView CardViewA;
    private CardView CardViewB;

    private Gson gson=new Gson();
    private List<MqttMessage> messageList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MqttService.addMqttListener(this);
        Intent intent=new Intent(this, MqttService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }else {
            startService(intent);
        }



        switchA = findViewById(R.id.switch1);
        switchB = findViewById(R.id.switch2);
        switchC = findViewById(R.id.switch3);
        switchD = findViewById(R.id.switch4);

        buttonA = findViewById(R.id.button);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);

        mMessageRV1=findViewById(R.id.textView1);
        mMessageRV2=findViewById(R.id.textView2);
        mMessageRV3=findViewById(R.id.textView3);

        CardViewA= findViewById(R.id.cardView6);
        CardViewB= findViewById(R.id.cardView7);


        switchA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                if (isChecked)
                {

                    MqttMessage mqttMessage=new MqttMessage();
                    mqttMessage.setMessage("on,");
                    mqttMessage.setUsername(Constants.MQTT_USERNAME);
                    MqttService.getMqttConfig().pubMsg(Constants.MQTT_pubTOPIC,gson.toJson(mqttMessage),0);


                }
                else
                {
                    MqttMessage mqttMessage=new MqttMessage();
                    mqttMessage.setMessage("off,");
                    mqttMessage.setUsername(Constants.MQTT_USERNAME);
                    MqttService.getMqttConfig().pubMsg(Constants.MQTT_pubTOPIC,gson.toJson(mqttMessage),0);


                }

            }
        });

        switchB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                if (isChecked)
                { //右邊
                    /*開起switchB*/
                    MqttMessage mqttMessage=new MqttMessage();
                    mqttMessage.setMessage("A model,");
                    mqttMessage.setUsername(Constants.MQTT_USERNAME);
                    MqttService.getMqttConfig().pubMsg(Constants.MQTT_pubTOPIC,gson.toJson(mqttMessage),0);
                    CardViewA.setVisibility(View.VISIBLE);
                    switchC.setChecked(true);
                }
                else
                { //左邊

                    MqttMessage mqttMessage=new MqttMessage();
                    mqttMessage.setMessage("Fan off,");
                    mqttMessage.setUsername(Constants.MQTT_USERNAME);
                    MqttService.getMqttConfig().pubMsg(Constants.MQTT_pubTOPIC,gson.toJson(mqttMessage),0);

                    CardViewA.setVisibility(View.INVISIBLE);
                    CardViewB.setVisibility(View.INVISIBLE);




                }

            }
        });
        switchC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                if (isChecked)
                {

                    MqttMessage mqttMessage=new MqttMessage();
                    mqttMessage.setMessage("A model,");
                    mqttMessage.setUsername(Constants.MQTT_USERNAME);
                    MqttService.getMqttConfig().pubMsg(Constants.MQTT_pubTOPIC,gson.toJson(mqttMessage),0);

                    switchD.setChecked(false);

                }
                else
                {

                    switchD.setChecked(true);

                }

            }
        });

        switchD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                if (isChecked)
                {

                    CardViewB.setVisibility(View.VISIBLE);
                    switchC.setChecked(false);
                }
                else
                {
                    switchC.setChecked(true);

                    CardViewB.setVisibility(View.INVISIBLE);

                }

            }
        });

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText1.getText().toString().matches("")||
                        editText2.getText().toString().matches("")||
                        editText3.getText().toString().matches("")||
                        editText4.getText().toString().matches("")||
                        editText5.getText().toString().matches("")) {

                    MqttAppState.getInstance().showToast("欄位未填好!!");
                }else{
                    MqttMessage mqttMessage=new MqttMessage();
                    mqttMessage.setMessage("B model,"+editText1.getText().toString()+','+editText2.getText().toString()+','+editText3.getText().toString()+','+editText4.getText().toString()+','+editText5.getText().toString());
                    mqttMessage.setUsername(Constants.MQTT_USERNAME);
                    MqttService.getMqttConfig().pubMsg(Constants.MQTT_pubTOPIC,gson.toJson(mqttMessage),0);
                }

            }
        });



/*
        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        layoutManager.setReverseLayout(true);//倒序显示
        layoutManager.setStackFromEnd(true);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MqttService.removeMqttListener(this);
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onFail() {

    }

    @Override
    public void onLost() {

    }

    @Override
    public void onReceive(String topic, String message) {

        if(topic.equals(Constants.MQTT_SubTOPIC)){
            MqttMessage mqttMessage=gson.fromJson(message,MqttMessage.class);

            if(messageList.size()>0)
                messageList.add(0,mqttMessage);
            else
                messageList.add(mqttMessage);

            //mMessageRV1.setText(mqttMessage.getMessage());
            String[] split_line = mqttMessage.getMessage().split(",");
            mMessageRV1.setText(split_line[0]);
            mMessageRV2.setText(split_line[1]);
            mMessageRV3.setText(split_line[2]);
        }
    }

    @Override
    public void onSendSucc() {
        MqttAppState.getInstance().showToast("消息发送成功");

    }
}
