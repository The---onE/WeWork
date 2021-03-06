package com.xmx.wework.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.xmx.wework.Cloud.Cloud;
import com.xmx.wework.Cloud.CloudAdapter;
import com.xmx.wework.Cloud.CloudEntityManager;
import com.xmx.wework.R;
import com.xmx.wework.Tools.BaseFragment;
import com.xmx.wework.Tools.Data.Callback.DelCallback;
import com.xmx.wework.Tools.Data.Callback.InsertCallback;
import com.xmx.wework.Tools.Data.Callback.SelectCallback;
import com.xmx.wework.Tools.Data.Callback.UpdateCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloudFragment extends BaseFragment {

    CloudAdapter cloudAdapter;
    ListView cloudList;
    Button add;
    EditText text;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_cloud, container, false);
    }

    @Override
    protected void initView(View view) {
        cloudList = (ListView) view.findViewById(R.id.list_cloud);
        cloudAdapter = new CloudAdapter(getContext(), new ArrayList<Cloud>());
        cloudList.setAdapter(cloudAdapter);

        text = (EditText) view.findViewById(R.id.edit_cloud);
        add = (Button) view.findViewById(R.id.btn_cloud);
    }

    @Override
    protected void setListener() {
        cloudList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Cloud cloud = (Cloud) cloudAdapter.getItem(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("要更新该记录吗？");
                builder.setTitle("提示");
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CloudEntityManager.getInstance().deleteFromCloud(cloud.mCloudId, new DelCallback() {
                            @Override
                            public void success() {
                                showToast(R.string.delete_success);
                                updateList();
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
                        CloudEntityManager.getInstance().updateToCloud(cloud.mCloudId, update,
                                new UpdateCallback() {
                                    @Override
                                    public void success() {
                                        showToast(R.string.update_success);
                                        updateList();
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
                Cloud entity = new Cloud();
                entity.mData = data;
                entity.mTime = new Date();
                CloudEntityManager.getInstance().insertToCloud(entity, new InsertCallback() {
                    @Override
                    public void success(String objectId) {
                        showToast(R.string.add_success);
                        updateList();
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
        updateList();
    }

    private void updateList() {
        CloudEntityManager.getInstance().selectByCondition(null,
                "Time", false,
                new SelectCallback<Cloud>() {
            @Override
            public void success(List<Cloud> clouds) {
                cloudAdapter.updateList(clouds);
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
