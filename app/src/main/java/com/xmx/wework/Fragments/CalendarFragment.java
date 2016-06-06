package com.xmx.wework.Fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.bigkoo.pickerview.OptionsPickerView;
import com.xmx.wework.Calendar.CalendarEntity;
import com.xmx.wework.Calendar.CalendarManager;
import com.xmx.wework.R;
import com.xmx.wework.Tools.BaseFragment;
import com.xmx.wework.Tools.Data.Callback.DelCallback;
import com.xmx.wework.Tools.Data.Callback.InsertCallback;
import com.xmx.wework.Tools.Data.Callback.UpdateCallback;
import com.xmx.wework.Tools.Datepicker.DateManager;
import com.xmx.wework.Tools.Datepicker.bizs.calendars.DPCManager;
import com.xmx.wework.Tools.Datepicker.bizs.decors.DPDecor;
import com.xmx.wework.Tools.Datepicker.cons.DPMode;
import com.xmx.wework.Tools.Datepicker.views.DatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends BaseFragment {
    DatePicker picker;
    OptionsPickerView pvOptions;
    String typeString;
    int typeInt;
    ArrayList<String> types;
    int year;
    int month;
    int day;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    protected void initView(View view) {
        picker = (DatePicker) view.findViewById(R.id.date_picker);

        final Calendar calendar = Calendar.getInstance();
        picker.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        picker.setMode(DPMode.SINGLE);

        picker.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint, String data) {
                int[] day = DateManager.getDate(data);

                List<CalendarEntity> calendarList = CalendarManager.getInstance()
                        .getSQLManager().selectByCondition("Time", false,
                                "Year=" + day[0], "Month=" + day[1], "Day=" + day[2]);

                if (calendarList.size() == 1) {
                    paint.setColor(Color.GREEN);
                    switch (calendarList.get(0).mStatus) {
                        case 1:
                            canvas.drawArc(new RectF(rect), 180, 180, true, paint);
                            break;
                        case 2:
                            canvas.drawArc(new RectF(rect), 0, 180, true, paint);
                            break;
                        case 3:
                            canvas.drawArc(new RectF(rect), 0, 360, true, paint);
                            break;
                    }
                }
            }
        });

        types = new ArrayList<>();
        types.add(getString(R.string.no));
        types.add(getString(R.string.morning));
        types.add(getString(R.string.afternoon));
        types.add(getString(R.string.all));
        pvOptions = new OptionsPickerView(getContext());
        pvOptions.setPicker(types);
        pvOptions.setCancelable(true);
        pvOptions.setTitle(getString(R.string.time));
        pvOptions.setCyclic(false);
    }

    @Override
    protected void setListener() {
        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                CalendarManager.getInstance();
                //TODO
                int[] d = DateManager.getDate(date);
                year = d[0];
                month = d[1];
                day = d[2];

                pvOptions.show();
            }
        });

        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                typeString = types.get(options1);
                typeInt = options1;
                //showToast("" + year + "-" + month + "-" + day + " " + typeString);

                List<CalendarEntity> calendarList = CalendarManager.getInstance()
                        .getSQLManager().selectByCondition("Time", false,
                                "Year=" + year, "Month=" + month, "Day=" + day);

                if (calendarList.size() == 1) {
                    CalendarEntity calendar = calendarList.get(0);
                    if (typeInt == 0) {
                        deleteCalendar(calendar.mCloudId);
                    } else {
                        updateCalendar(calendar.mCloudId, typeInt);
                    }
                } else if (calendarList.size() < 1) {
                    if (typeInt != 0) {
                        CalendarEntity calendar = new CalendarEntity();
                        calendar.mYear = year;
                        calendar.mMonth = month;
                        calendar.mDay = day;
                        calendar.mStatus = typeInt;
                        calendar.mTime = new Date();
                        insertCalendar(calendar);
                    }
                } else {
                    for (CalendarEntity calendar : calendarList) {
                        deleteCalendar(calendar.mCloudId);
                    }
                    if (typeInt != 0) {
                        CalendarEntity calendar = new CalendarEntity();
                        calendar.mYear = year;
                        calendar.mMonth = month;
                        calendar.mDay = day;
                        calendar.mStatus = typeInt;
                        calendar.mTime = new Date();
                        insertCalendar(calendar);
                    }
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        updatePaint();
    }

    void insertCalendar(CalendarEntity calendar) {
        CalendarManager.getInstance().insertData(calendar, new InsertCallback() {
            @Override
            public void success(String objectId) {
                showToast(R.string.add_success);
                updatePaint();
            }

            @Override
            public void notInit() {
                showToast(R.string.not_init);
            }

            @Override
            public void syncError(AVException e) {
                showToast(e.getMessage());
            }

            @Override
            public void notLoggedIn() {
                showToast(R.string.not_loggedin);
            }

            @Override
            public void errorNetwork() {
                showToast(R.string.network_error);
            }

            @Override
            public void errorUsername() {
                showToast(R.string.username_error);
            }

            @Override
            public void errorChecksum() {
                showToast(R.string.not_loggedin);
            }
        });
    }

    void deleteCalendar(String objectId) {
        CalendarManager.getInstance().deleteData(objectId,
                new DelCallback() {
                    @Override
                    public void success() {
                        showToast(R.string.delete_success);
                        updatePaint();
                    }

                    @Override
                    public void notInit() {
                        showToast(R.string.not_init);
                    }

                    @Override
                    public void syncError(AVException e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void notLoggedIn() {
                        showToast(R.string.not_loggedin);
                    }

                    @Override
                    public void errorNetwork() {
                        showToast(R.string.network_error);
                    }

                    @Override
                    public void errorUsername() {
                        showToast(R.string.username_error);
                    }

                    @Override
                    public void errorChecksum() {
                        showToast(R.string.not_loggedin);
                    }
                });
    }

    void updateCalendar(String objectId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("Status", status);
        CalendarManager.getInstance().updateData(objectId, map, new UpdateCallback() {
            @Override
            public void success() {
                showToast(R.string.update_success);
                updatePaint();
            }

            @Override
            public void notInit() {
                showToast(R.string.not_init);
            }

            @Override
            public void syncError(AVException e) {
                showToast(e.getMessage());
            }

            @Override
            public void notLoggedIn() {
                showToast(R.string.not_loggedin);
            }

            @Override
            public void errorNetwork() {
                showToast(R.string.network_error);
            }

            @Override
            public void errorUsername() {
                showToast(R.string.username_error);
            }

            @Override
            public void errorChecksum() {
                showToast(R.string.not_loggedin);
            }
        });
    }

    void updatePaint() {
        List<String> list = new ArrayList<>();
        List<CalendarEntity> calendarList = CalendarManager.getInstance().getSQLManager().selectAll();
        for (CalendarEntity c : calendarList) {
            int year = c.mYear;
            int month = c.mMonth;
            int day = c.mDay;
            list.add(""+year+"-"+month+"-"+day);
        }
        DPCManager.getInstance().setDecorBG(list);
    }
}
