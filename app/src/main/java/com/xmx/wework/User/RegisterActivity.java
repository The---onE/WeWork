package com.xmx.wework.User;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xmx.wework.R;
import com.xmx.wework.Tools.ActivityBase.BaseTempActivity;
import com.xmx.wework.User.Callback.RegisterCallback;

public class RegisterActivity extends BaseTempActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void setListener() {
        final Button register = getViewById(R.id.register_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nn = getViewById(R.id.register_nickname);
                final String nickname = nn.getText().toString();
                EditText un = getViewById(R.id.register_username);
                final String username = un.getText().toString();
                EditText pw = getViewById(R.id.register_password);
                final String password = pw.getText().toString();
                EditText pw2 = getViewById(R.id.register_password2);
                String password2 = pw2.getText().toString();

                if (nickname.equals("")) {
                    showToast(R.string.nickname_hint);
                    return;
                }
                if (username.equals("")) {
                    showToast(R.string.username_empty);
                    return;
                }
                if (password.equals("")) {
                    showToast(R.string.password_empty);
                    return;
                }
                if (!password.equals(password2)) {
                    showToast(R.string.password_different);
                    return;
                }

                register.setEnabled(false);
                UserManager.getInstance().register(username, password, nickname, new RegisterCallback() {
                    @Override
                    public void success() {
                        showToast(R.string.register_success);
                        finish();
                    }

                    @Override
                    public void usernameExist() {
                        showToast(R.string.username_exist);
                        register.setEnabled(true);
                    }

                    @Override
                    public void nicknameExist() {
                        showToast(R.string.nickname_exist);
                        register.setEnabled(true);
                    }

                    @Override
                    public void errorNetwork() {
                        showToast(R.string.network_error);
                        register.setEnabled(true);
                    }
                });
            }
        });

        getViewById(R.id.register_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
