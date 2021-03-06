package com.xmx.wework.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.xmx.wework.R;
import com.xmx.wework.Sync.Sync;
import com.xmx.wework.Sync.SyncAdapter;
import com.xmx.wework.Sync.SyncEntityManager;
import com.xmx.wework.Sync.SyncManager;
import com.xmx.wework.Tools.BaseFragment;
import com.xmx.wework.Tools.Data.Callback.DelCallback;
import com.xmx.wework.Tools.Data.Callback.InsertCallback;
import com.xmx.wework.Tools.Data.Callback.SelectCallback;
import com.xmx.wework.Tools.Data.Callback.UpdateCallback;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xmx on 2016/6/1.
 */
public class SyncFragment extends BaseFragment {

    SyncAdapter syncAdapter;
    ListView syncList;
    Button add;
    EditText text;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_sync, container, false);
    }

    @Override
    protected void initView(View view) {
        syncList = (ListView) view.findViewById(R.id.list_sync);
        SyncManager.getInstance().updateData();
        syncAdapter = new SyncAdapter(getContext());
        syncList.setAdapter(syncAdapter);

        text = (EditText) view.findViewById(R.id.edit_sync);
        add = (Button) view.findViewById(R.id.btn_sync);
    }

    @Override
    protected void setListener() {
        syncList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Sync sync = (Sync) syncAdapter.getItem(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("要更新该记录吗？");
                builder.setTitle("提示");
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SyncEntityManager.getInstance().deleteData(sync.mCloudId, new DelCallback() {
                            @Override
                            public void success() {
                                showToast(R.string.delete_success);
                                SyncManager.getInstance().updateData();
                                syncAdapter.updateList();
                            }

                            @Override
                            public void notInit() {
                                showToast(R.string.failure);
                            }

                            @Override
                            public void syncError(AVException e) {
                                showToast(R.string.sync_failure);
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
                });
                builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Map<String, Object> update = new HashMap<>();
                        update.put("Time", new Date());
                        SyncEntityManager.getInstance().updateData(sync.mCloudId, update,
                                new UpdateCallback() {
                                    @Override
                                    public void success() {
                                        showToast(R.string.update_success);
                                        SyncManager.getInstance().updateData();
                                        syncAdapter.updateList();
                                    }

                                    @Override
                                    public void notInit() {
                                        showToast(R.string.failure);
                                    }

                                    @Override
                                    public void syncError(AVException e) {
                                        showToast(R.string.sync_failure);
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
                });
                builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return false;
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = text.getText().toString();
                final Sync entity = new Sync();
                entity.mData = data;
                entity.mTime = new Date();
                SyncEntityManager.getInstance().insertData(entity, new InsertCallback() {
                    @Override
                    public void success(String objectId) {
                        showToast(R.string.add_success);
                        entity.mCloudId = objectId;
                        SyncManager.getInstance().updateData();
                        syncAdapter.updateList();
                    }

                    @Override
                    public void notInit() {
                        showToast(R.string.failure);
                    }

                    @Override
                    public void syncError(AVException e) {
                        showToast(R.string.sync_failure);
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
                text.setText("");
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        SyncEntityManager.getInstance().syncFromCloud(null,
                new SelectCallback<Sync>() {
                    @Override
                    public void success(List<Sync> syncs) {
                        SyncManager.getInstance().updateData();
                        syncAdapter.updateList();
                        showToast(R.string.sync_success);
                    }

                    @Override
                    public void notInit() {
                        showToast(R.string.failure);
                    }

                    @Override
                    public void syncError(AVException e) {
                        showToast(R.string.sync_failure);
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
}
