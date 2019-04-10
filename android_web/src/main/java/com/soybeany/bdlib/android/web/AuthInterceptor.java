package com.soybeany.bdlib.android.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.StdHintUtils;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogMsg;
import com.soybeany.bdlib.web.okhttp.HandledException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.Response;

import static com.soybeany.bdlib.android.util.BDContext.MAIN_HANDLER;

/**
 * 授权拦截器(建议使用{@link okhttp3.OkHttpClient.Builder#addInterceptor(Interceptor)}进行添加)
 * <br>Created by Soybeany on 2019/4/9.
 */
public class AuthInterceptor implements Interceptor {
    @NonNull
    private IDealer mDealer;
    @Nullable
    private AbstractDialog mDialog;

    public AuthInterceptor(@NonNull IDealer dealer, @Nullable AbstractDialog dialog) {
        mDealer = dealer;
        mDialog = dialog;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        // 响应有效则不作处理
        try {
            if (mDealer.isResponseValid(response)) {
                return response;
            }
        } catch (Exception e) {
            response.close();
            throw e;
        }
        // 响应无效则先关闭先前响应
        response.close();
        DialogMsg oldMsg = (null != mDialog ? mDialog.curMsg() : null);
        // 尝试重登录
        DialogMsg authMsg = mDealer.getAuthMsg(oldMsg);
        try {
            showDialog(authMsg);
            Response reLogin = mDealer.onAuth();
            if (!reLogin.isSuccessful()) {
                return reLogin;
            }
        } catch (IOException e) {
            throw mDealer.onAuthException(e);
        } finally {
            dismissDialog(oldMsg, authMsg);
        }
        // 再次请求并返回新响应
        DialogMsg reRequestMsg = mDealer.getReRequestMsg(oldMsg);
        try {
            showDialog(reRequestMsg);
            return mDealer.onReRequest(chain.call());
        } catch (IOException e) {
            throw mDealer.onReRequestException(e);
        } finally {
            dismissDialog(oldMsg, reRequestMsg);
        }
    }

    private void showDialog(DialogMsg msg) {
        if (null != mDialog) {
            MAIN_HANDLER.post(() -> mDialog.show(msg));
        }
    }

    private void dismissDialog(DialogMsg oldMsg, DialogMsg curMsg) {
        if (null != mDialog && oldMsg != curMsg) {
            MAIN_HANDLER.post(() -> mDialog.dismiss(curMsg));
        }
    }

    public interface IDealer {
        boolean isResponseValid(Response response);

        default DialogMsg getAuthMsg(DialogMsg oldMsg) {
            return new DialogMsg(StdHintUtils.STD_RE_LOGIN_HINT).cancelable(oldMsg.isCancelable()).multiHint((hint, count, cancelable) -> hint);
        }

        Response onAuth() throws IOException;

        default IOException onAuthException(IOException e) {
            return e instanceof HandledException ? e : new IOException("重登录异常:" + e.getMessage());
        }

        default DialogMsg getReRequestMsg(DialogMsg oldMsg) {
            return oldMsg;
        }

        default Response onReRequest(Call call) throws IOException {
            return call.execute();
        }

        default IOException onReRequestException(IOException e) {
            return e instanceof HandledException ? e : new IOException("重请求异常:" + e.getMessage());
        }
    }
}
