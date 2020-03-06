package org.firstinspires.ftc.teamcode.Util;

import android.media.MediaPlayer;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.R;

public class PlaySound {

    private MediaPlayer mp;


    public PlaySound(HardwareMap hardwareMap, int soundId){
        mp = MediaPlayer.create(hardwareMap.appContext, soundId);
        mp.setOnCompletionListener(MediaPlayer::release);
    }

    public void play(){
        try {
            mp.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void pause(){
        try {
            mp.pause();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            mp.stop();
            mp.release();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
