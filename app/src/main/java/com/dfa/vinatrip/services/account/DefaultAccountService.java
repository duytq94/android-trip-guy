package com.dfa.vinatrip.services.account;

import com.beesightsoft.caf.services.authentication.AbstractAccountManager;
import com.beesightsoft.caf.services.authentication.AuthenticationManagerConfiguration;
import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.request.SignInSocialRequest;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.services.filter.ApiErrorFilter;

import rx.Observable;


/**
 * Created by duytq on 9/17/2017.
 */

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
        return null;
    }

    @Override
    public Observable<User> signUp(AuthRequest authRequest) {
        return networkProvider
                .transformResponse(restAccountService.signUp(authRequest))
                .compose(apiErrorFilter.execute());
    }
}
