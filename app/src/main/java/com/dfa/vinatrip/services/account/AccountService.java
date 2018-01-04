package com.dfa.vinatrip.services.account;

import com.beesightsoft.caf.services.authentication.AuthenticationManager;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.request.ChangePasswordRequest;
import com.dfa.vinatrip.models.request.ResetPasswordRequest;
import com.dfa.vinatrip.models.request.SignInSocialRequest;
import com.dfa.vinatrip.models.response.user.User;

import rx.Observable;

public interface AccountService extends AuthenticationManager<User, AuthRequest, SignInSocialRequest> {
    Observable<String> signUp(AuthRequest authRequest);

    Observable<User> editProfile(User user);

    Observable<String> resetPassword(ResetPasswordRequest resetPasswordRequest);

    Observable<String> changePassword(ChangePasswordRequest changePasswordRequest);
}
