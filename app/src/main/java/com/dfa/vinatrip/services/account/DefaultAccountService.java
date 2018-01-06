package com.dfa.vinatrip.services.account;

import com.beesightsoft.caf.services.authentication.AbstractAccountManager;
import com.beesightsoft.caf.services.authentication.AuthenticationManagerConfiguration;
import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.request.ChangePasswordRequest;
import com.dfa.vinatrip.models.request.ResetPasswordRequest;
import com.dfa.vinatrip.models.request.SignInSocialRequest;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.services.filter.ApiErrorFilter;

import rx.Observable;

public class DefaultAccountService
        extends AbstractAccountManager<User, AuthRequest, SignInSocialRequest>
        implements AccountService {

    private NetworkProvider networkProvider;
    private RestAccountService restAccountService;
    private ApiErrorFilter apiErrorFilter;

    public DefaultAccountService(AuthenticationManagerConfiguration configuration, NetworkProvider networkProvider,
                                 RestAccountService restAccountService, ApiErrorFilter apiErrorFilter) {
        super(configuration);
        this.networkProvider = networkProvider;
        this.restAccountService = restAccountService;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    protected Observable<User> onLogin(AuthRequest loginRequest) {
        return networkProvider
                .transformResponse(restAccountService.signIn(loginRequest))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<User> loginSocial(SignInSocialRequest loginRequest) {
        return null;
    }

    @Override
    protected Observable<User> onLoginSocial(SignInSocialRequest loginRequest) {
        return null;
    }

    @Override
    protected Observable<String> onLogout() {
        return networkProvider
                .transformResponse(restAccountService.signOut(getCurrentUser().getAccessToken()))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<String> signUp(AuthRequest authRequest) {
        return networkProvider
                .transformResponse(restAccountService.signUp(authRequest))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<User> editProfile(User user) {
        return networkProvider
                .transformResponse(restAccountService.editProfile(getCurrentUser().getAccessToken(), user))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<String> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        return networkProvider
                .transformResponse(restAccountService.resetPassword(resetPasswordRequest))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<String> changePassword(ChangePasswordRequest changePasswordRequest) {
        return networkProvider
                .transformResponse(restAccountService.changePassword(getCurrentUser().getAccessToken(), changePasswordRequest))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<User> getUserInfo(long userId) {
        return networkProvider
                .transformResponse(restAccountService.getUserInfo(userId))
                .compose(apiErrorFilter.execute());
    }
}
