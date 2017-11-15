package com.android.betterway.myservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import com.android.betterway.R;
import com.android.betterway.data.Schedule;
import com.android.betterway.mainactivity.model.MainModel;
import com.android.betterway.mainactivity.view.MainActivity;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 后台服务
 */
public class MyService extends Service {
    public static final int SCHEDULE_ADD = 110;
    public static final int SCHEDULE_DELELTE = 111;
    public static final int DURATION_CHANGE = 112;
    public static final String SCHEDULE_ADD_KEY = "SAK";
    public static final String SCHEDULE_DELETE_KEY = "SDK";
    public static final String DURATION_CHANGE_KEY = "DCK";
    private static final String TAG = "MyService";
    private Disposable mDisposable;
    boolean weatherWarn;
    private int duration;
    private volatile List<Long> dates = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG, "onCreate()");
        initDates();
        initDuraion();
    }

    /**
     * 初始化提醒时间
     */
    private void initDuraion() {
        io.reactivex.Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                SharedPreferences sharedPreferences =
                        getSharedPreferences("com.android.betterway_preferences", Context.MODE_PRIVATE);
                weatherWarn = sharedPreferences.getBoolean("weather_warn", false);
                if (weatherWarn) {
                    String d = sharedPreferences.getString("warning_time", "15");
                    int dur = Integer.parseInt(d);
                    e.onNext(dur);
                } else {
                    e.onNext(0);
                }
            }
        }).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        duration = integer;
                    }
                });
    }


    void judgeStart() {
        LogUtil.i(TAG, "judgeStart()");
        if (weatherWarn) {
            io.reactivex.Observable.interval(duration, TimeUnit.MINUTES)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mDisposable = d;
                        }

                        @Override
                        public void onNext(Long aLong) {
                            if (TimeUtil.getTimeDuration(dates, duration)) {
                                setNotification();
                            }
                        }
                        @Override
                        public void onError(Throwable e) {

                        }
                        @Override
                        public void onComplete() {
                        }
                    });
        } else {
            if (mDisposable != null) {
                mDisposable.dispose();
            }
        }
    }
    private void setNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("提醒")
                .setContentText("路书行程即将开始")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_stat_warn)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }
    /**
     * 初始化日期
     */
    private void initDates() {
        io.reactivex.Observable.create(new ObservableOnSubscribe<List<Schedule>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Schedule>> e) throws Exception {
                MainModel mainModel = MainModel.getInstance();
                List<Schedule> schedules = mainModel.inquiryAllSchedule(getApplication());
                if (schedules.size() != 0) {
                    e.onNext(schedules);
                }
            }
        }).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Schedule>>() {
                    @Override
                    public void accept(List<Schedule> list) throws Exception {
                        for (Schedule schedule : list) {
                            dates.add(schedule.getStartTime());
                            LogUtil.i(TAG, dates.size() + "个");
                        }
                    }
                });
    }

    private void addDate(long date) {
        dates.add(date);
    }
    public void deleteDate(long date) {
        dates.remove(date);
    }
    public void changeDuration(int duration) {
        this.duration = duration;
        if (this.duration != 0) {
            weatherWarn = true;
        }
    }

    /**
     * 内部静态类
     */
    private static class MessagerHandler extends Handler {
        private static  HandlerInterface sHandlerInterface;
        public MessagerHandler(HandlerInterface handlerInterface) {
            super();
            sHandlerInterface = handlerInterface;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCHEDULE_ADD:
                    sHandlerInterface.addDateI(msg.getData().getLong(SCHEDULE_ADD_KEY));
                    LogUtil.i("MySerivice", "addDate()");
                    sHandlerInterface.judgeStartI();
                    break;
                case SCHEDULE_DELELTE:
                    sHandlerInterface.delateDateI(msg.getData().getLong(SCHEDULE_DELETE_KEY));
                    LogUtil.i("Myservice", "deleteDate()");
                    sHandlerInterface.judgeStartI();
                    break;
                case DURATION_CHANGE:
                    sHandlerInterface.durationChangeI(msg.getData().getInt(DURATION_CHANGE_KEY));
                    LogUtil.i("Myservice", "changeDuration()");
                    sHandlerInterface.judgeStartI();
                    break;
                default:
                    LogUtil.i("Myservice", "default");
                    break;
            }
        }
    }
    private final Messenger mMessenger = new Messenger(new MessagerHandler(new HandlerInterface() {
        @Override
        public void addDateI(long l) {
            addDate(l);
        }

        @Override
        public void delateDateI(long l) {
            deleteDate(l);
        }

        @Override
        public void durationChangeI(int i) {
            changeDuration(i);
        }

        @Override
        public void judgeStartI() {
            judgeStart();
        }
    }));
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
