package net.kcci.HomeIot;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class Fragment2Dashboard extends Fragment {
    MainActivity mainActivity;
    Switch switchSERVO;
    Switch switchPUMP;
    TextView textViewWater;
    TextView textViewcds;
    TextView textViewTemp;
    TextView textViewHumi;
    ImageView imageViewSERVO;
    ImageView imageViewPUMP;
    Button buttonCondition;
    Button buttonControl;
    boolean switchSERVOFlag = false;
    boolean switchPUMPFlag = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2dashboard, container, false);
        mainActivity = (MainActivity) getActivity();
        textViewWater = view.findViewById(R.id.textViewWater);
        textViewcds = view.findViewById(R.id.textViewcds);
        textViewTemp = view.findViewById(R.id.textViewTemp);
        textViewHumi = view.findViewById(R.id.textViewHumi);
        imageViewSERVO = view.findViewById(R.id.imageViewSERVO);
        imageViewPUMP = view.findViewById(R.id.imageViewPUMP);
        buttonCondition = view.findViewById(R.id.buttonCondition);
        buttonCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClientThread.socket != null) {
                    mainActivity.clientThread.sendData("[SQL]GETSENSOR\r\n");
                } else
                    Toast.makeText(getActivity(),"login 확인", Toast.LENGTH_SHORT).show();
            }
        });
        buttonControl = view.findViewById(R.id.buttonControl);
        buttonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClientThread.socket != null) {
                    mainActivity.clientThread.sendData(ClientThread.arduinoId + "GETSW");
                } else
                    Toast.makeText(getActivity(), "login 확인", Toast.LENGTH_SHORT).show();
            }
        });
        switchSERVO = view.findViewById(R.id.SERVO);
        switchSERVO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClientThread.socket != null) {
                    if (switchSERVO.isChecked()) {
                        mainActivity.clientThread.sendData(ClientThread.arduinoId + "SERVO@ON");
                        switchSERVO.setChecked(false);
                    } else {
                        mainActivity.clientThread.sendData(ClientThread.arduinoId + "SERVO@OFF");
                        switchSERVO.setChecked(true);
                    }
                } else
                    Toast.makeText(getActivity(), "login 확인", Toast.LENGTH_SHORT).show();
            }
        });

        switchPUMP = view.findViewById(R.id.PUMP);
        switchPUMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClientThread.socket != null) {
                    if (switchPUMP.isChecked()) {
                        mainActivity.clientThread.sendData(ClientThread.arduinoId + "PUMP@ON");
                        switchPUMP.setChecked(false);
                    } else {
                        mainActivity.clientThread.sendData(ClientThread.arduinoId + "PUMP@OFF");
                        switchPUMP.setChecked(true);
                    }
                }else
                    Toast.makeText(getActivity(), "login 확인", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    public void recvDataProcess(String recvData) {
        String[] splitLists = recvData.toString().split("\\[|]|@|\\n");
        if(splitLists[2].equals("SERVO")) {
            if (splitLists[3].equals("ON"))
                imageButtonUpdate("SERVOON");
            else if (splitLists[3].equals("OFF")) {
                imageButtonUpdate("SERVOOFF");
            }
        }
        else if(splitLists[2].equals("PUMP")) {
            if (splitLists[3].equals("ON"))
                imageButtonUpdate("PUMPON");
            else if (splitLists[3].equals("OFF")) {
                imageButtonUpdate("PUMPOFF");
            }
        }
         else if(splitLists[2].equals("SENSOR"))
            updateTextView(splitLists[3],splitLists[4],splitLists[5], splitLists[6]);
    }
    public void imageButtonUpdate(String cmd) {
        if (cmd.equals("SERVOON")) {
            imageViewSERVO.setImageResource(R.drawable.servo_on);
            imageViewSERVO.setBackgroundColor(Color.GREEN);
            switchSERVO.setChecked(true);
            switchSERVOFlag = true;
        } else if(cmd.equals("SERVOOFF")) {
            imageViewSERVO.setImageResource(R.drawable.servo_off);
            imageViewSERVO.setBackgroundColor(Color.LTGRAY);
            switchSERVO.setChecked(false);
            switchSERVOFlag = false;
        } else if(cmd.equals("PUMPON")) {
            imageViewPUMP.setImageResource(R.drawable.pump_on);
            imageViewPUMP.setBackgroundColor(Color.GREEN);
            switchPUMP.setChecked(true);
            switchPUMPFlag = true;
        } else if(cmd.equals("PUMPOFF")) {
            imageViewPUMP.setImageResource(R.drawable.pump_off);
            imageViewPUMP.setBackgroundColor(Color.LTGRAY);
            switchPUMP.setChecked(false);
            switchPUMPFlag = true;
        }
    }
    public void updateTextView(String wval, String temp, String humi, String cds) {
        textViewWater.setText(wval+"%"); //Water
        textViewcds.setText(cds+"%");    //Illumination
        textViewTemp.setText(temp+"℃");          //Temperature
        textViewHumi.setText(humi+"%");          //humidity
    }
}
