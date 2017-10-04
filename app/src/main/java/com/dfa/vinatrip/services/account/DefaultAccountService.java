package com.dfa.vinatrip.services.account;

import com.beesightsoft.caf.services.authentication.AbstractAccountManager;
import com.beesightsoft.caf.services.authentication.AuthenticationManagerConfiguration;
import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.request.SignInSocialRequest;
import com.dfa.vinatrip.models.response.User;

import rx.Observable;


/**
 * Created by duytq on 9/17/2017.
 */

public class DefaultAccountService extends
        AbstractAccountManager<User, AuthRequest, SignInSocialRequest>
        implements AccountService {
    
    private NetworkProvider networkProvider;
    private RestAccountService restAccountService;
    
    public DefaultAccountService(AuthenticationManagerConfiguration configuration, NetworkProvider networkProvider,
                                 RestAccountService restAccountService) {
        super(configuration);
        this.networkProvider = networkProvider;
        this.restAccountService = restAccountService;
    }
    
    @Override
    protected Observable<User> onLogin(AuthRequest loginRequest) {
        return networkProvider.transformResponse(restAccountService.signIn(loginRequest));
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
}
