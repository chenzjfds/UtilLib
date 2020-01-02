package com.webank.ai.util;

import android.content.Context;
import android.media.AudioManager;

/**
 * <pre>
 *     author : v_wizardchen
 *     e-mail : v_wizardchen@webank.com
 *     time   : 2018/08/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class VolumeUtil {
    public static void setVolume(Context mContext, int volume) {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
        /*audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, AudioManager.FLAG_PLAY_SOUND);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_PLAY_SOUND);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, AudioManager.FLAG_PLAY_SOUND);*/
    }

    public static int getMaxVolume(Context context) {
        AudioManager AudioManagermAm = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return AudioManagermAm.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//当前音量intmCurrentVolume=mAm.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static int getVolume(Context context) {
        AudioManager AudioManagermAm = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return AudioManagermAm.getStreamVolume(AudioManager.STREAM_MUSIC);//当前音量intmCurrentVolume=mAm.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static void lowVolume(Context mContext, int value) {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume -= value, AudioManager.FLAG_PLAY_SOUND);
    }

    public static void increaseVolume(Context mContext, int value) {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume += value, AudioManager.FLAG_PLAY_SOUND);
        /*audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, AudioManager.FLAG_PLAY_SOUND);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_PLAY_SOUND);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, AudioManager.FLAG_PLAY_SOUND);*/
    }
}
