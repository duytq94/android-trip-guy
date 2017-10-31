package com.dfa.vinatrip.services.filter;

import android.widget.Toast;

import com.beesightsoft.caf.exceptions.ApiThrowable;
import com.beesightsoft.caf.exceptions.ErrorCodes;
import com.beesightsoft.caf.services.filter.InterceptFilter;
import com.beesightsoft.caf.services.log.LogService;
import com.beesightsoft.caf.services.network.NetworkProvider;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.dfa.vinatrip.utils.ErrorCodes.ERROR_OAUTH2_TOKEN_BEEN_REVOKED;
import static com.dfa.vinatrip.utils.ErrorCodes.ERROR_OAUTH2_TOKEN_INVALID;
import static com.dfa.vinatrip.utils.ErrorCodes.ERROR_OAUTH2_TOKEN_NOT_BE_VERIFIED;
import static com.dfa.vinatrip.utils.ErrorCodes.ERROR_OAUTH2_TOKEN_OWNER_DENIED_REQUEST;

/**
 * Created by duytq on 9/19/2017.
 */

public class ApiErrorFilter implements InterceptFilter {
    private NetworkProvider networkProvider;
    private LogService logService;

    public ApiErrorFilter(NetworkProvider networkProvider, LogService logService) {
        this.networkProvider = networkProvider;
        this.logService = logService;
    }

    @Override
    public <T> Observable.Transformer<T, T> execute() {
        return tObservable -> tObservable
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    ApiThrowable exceptionResult = null;
                            if (throwable instanceof ApiThrowable) {
                                ApiThrowable exception = ((ApiThrowable) throwable);
                                int code = exception.firstErrorCode();
                                switch (code) {
                                    case ErrorCodes.NETWORK_NOT_AVAILABLE_ERROR:
                                    case ErrorCodes.HTTP_IO_ERROR:
                                    case ErrorCodes.HTTP_TIMEOUT_ERROR:
                                        exceptionResult = ApiThrowable.from(
                                                ErrorCodes.NETWORK_NOT_AVAILABLE_ERROR,
                                                "Internet connection is offline.");
                                        break;
                                    case ERROR_OAUTH2_TOKEN_INVALID:
                                    case ERROR_OAUTH2_TOKEN_NOT_BE_VERIFIED:
                                    case ERROR_OAUTH2_TOKEN_BEEN_REVOKED:
                                    case ERROR_OAUTH2_TOKEN_OWNER_DENIED_REQUEST:
                                        // TODO
                                        break;
                                    default:
                                        exceptionResult = exception;
                                }
                            } else {
                                if (throwable instanceof SocketTimeoutException ||
                                        throwable instanceof SocketException
                                        || throwable instanceof UnknownHostException) {
                                    exceptionResult = ApiThrowable.from("Timeout connection");
                                } else {
                                    exceptionResult = ApiThrowable.from("Something Error");
                                    Toast.makeText(networkProvider.getContext(),
                                            "Oops! Something Error. Please Check Log.", Toast.LENGTH_SHORT).show();
                                }
                            }
                    return Observable.error(exceptionResult);
                });
    }
}
