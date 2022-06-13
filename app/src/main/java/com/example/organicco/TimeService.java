package com.example.organicco;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeService extends Service {
    public timeBinder timeBinder=new timeBinder() ;
    public TimeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
             return timeBinder;
    }
    public String getUpdateTime(){

        SimpleDateFormat dateFormat =new SimpleDateFormat("hh:mm:ss  dd/mm/yyyy", Locale.US);
        return dateFormat.format(new Date());

    }
    public class timeBinder extends Binder {
        TimeService getBoundService(){
            return TimeService.this;
        }
    }
}