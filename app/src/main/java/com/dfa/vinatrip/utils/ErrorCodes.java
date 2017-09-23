package com.dfa.vinatrip.utils;

/**
 * Created by duytq on 9/19/2017.
 */

public class ErrorCodes {
    // Error System
    public static final int ERROR_SYSTEM_WRONG_ARGUMENTS = 9000;//               => [9000, 'Wrong Arguments'],
    public static final int ERROR_SYSTEM_FORBIDDEN = 9001;//                     => [9001, 'Access to this resource on the server is denied!'],
    public static final int ERROR_SYSTEM_NOT_FOUND = 9002;//                     => [9002, '​Resource not found'],
    public static final int ERROR_SYSTEM_INTERNAL_ERROR = 9003;//               => [9003, '​Internal Error'],
    public static final int ERROR_SYSTEM_NOT_IMPLEMENTED = 9004;//               => [9004, 'Not implemented'],
    public static final int ERROR_SYSTEM_UNAUTHORIZED = 9005;//                  => [9005, 'Unauthorized'],
    public static final int ERROR_SYSTEM_TOKEN_REQUIRED = 9006;//                => [9006, 'Please make sure your request has an Authorization header'],
    public static final int ERROR_SYSTEM_PERMISSION = 9007;//                    => [9007, 'You don\'t have permission!'],
    public static final int ERROR_SYSTEM_SUSPENDED = 9008;//                     => [9008, 'Hi, your account has been suspended. Please contact us'],
    public static final int ERROR_SYSTEM_USER_HAS_BLOCKED = 9010;//              => [9010, 'Hi, your account has been denied'],

    public static final int ERROR_OAUTH2_UNSUPPORTED_GRANT_TYPE = 8001;
    public static final int ERROR_OAUTH2_MISSING = 8002;
    public static final int ERROR_OAUTH2_TOKEN_NOT_BE_VERIFIED = 8003;
    public static final int ERROR_OAUTH2_TOKEN_INVALID = 8004;
    public static final int ERROR_OAUTH2_TOKEN_BEEN_REVOKED = 8005;
    public static final int ERROR_OAUTH2_TOKEN_DENIED_REQUEST = 8006;
    public static final int ERROR_OAUTH2_TOKEN_OWNER_DENIED_REQUEST = 8007;//      'The resource owner or authorization server denied the request.':8007,
    public static final int ERROR_OAUTH2_DECODING_JSON = 8008; //'Error while decoding to JSON':8008,
    public static final int ERROR_OAUTH2_INVALID_CLIENT = 8009;//        'invalid_client':8009,
    public static final int ERROR_OAUTH2_SERVER_ERROR = 8010;//        'server_error':8010,
    public static final int ERROR_OAUTH2_INVALID_SCOPE = 8011;//        'invalid_scope':8011,
    public static final int ERROR_OAUTH2_INVALID_REQUEST = 8012;//        'invalid_request':8012,
    public static final int ERROR_OAUTH2_INVALID_CREDENTIALS = 8013;//       'invalid_credentials':8013,
    public static final int ERROR_OAUTH2_INVALID_GRANT = 8014;//        'invalid_grant':8014

    // Validation code for “Name” field
    public static final int ERROR_NAME_REQUIRED = 2001;//                        => [2001, 'The name field is required.'],
    public static final int ERROR_NAME_MIN = 2002;//                             => [2002, 'The name length must be greater than 3 characters.'],
    public static final int ERROR_NAME_MAX = 2003;//                             => [2003, 'The name may not be greater than 50 characters.'],

    // Validation code for “First Name” field
    public static final int ERROR_FIRST_NAME_REQUIRED = 2001;//                  => [2001, 'The first name field is required.'],
    public static final int ERROR_FIRST_NAME_MIN = 2002;//                       => [2002, 'The first name length must be greater than 3 characters.'],
    public static final int ERROR_FIRST_NAME_MAX = 2003;//                       => [2003, 'The first name may not be greater than 50 characters.'],

    // Validation code for “Last Name” field
    public static final int ERROR_LAST_NAME_REQUIRED = 2001;//                   => [2001, 'The last name field is required.'],
    public static final int ERROR_LAST_NAME_MIN = 2002;//                        => [2002, 'The last name length must be greater than 3 characters.'],
    public static final int ERROR_LAST_NAME_MAX = 2003;//                        => [2003, 'The last name may not be greater than 50 characters.'],

    // Validation code for “Username” field
    public static final int ERROR_USERNAME_REQUIRED = 2010;//                    => [2010, 'The username field is required.'],
    public static final int ERROR_USERNAME_EXISTS = 2013;//                      => [2031, 'The username has already been taken.'],
    public static final int ERROR_USERNAME_UNIQUE = 2032;//                      => [2032, 'The supplied username is already registered to another account'],
    public static final int ERROR_USERNAME_MIN = 2011;//                         => [2011, 'The username length must be greater than 4 characters.'],
    public static final int ERROR_USERNAME_MAX = 2012;//                         => [2012, 'The username length must be at least than 20 characters'],

    // Validation code for “Email” field
    public static final int ERROR_EMAIL_REQUIRED = 2030;//                       => [2030, 'The email field is required.'],
    public static final int ERROR_EMAIL_EXISTS = 2031;//                         => [2031, 'The email has already been taken.'],
    public static final int ERROR_EMAIL_UNIQUE = 2032;//                         => [2032, 'The supplied email is already registered to another account'],
    public static final int ERROR_EMAIL_EMAIL = 2033;//                          => [2033, 'Invalid email'],
    public static final int ERROR_EMAIL_VERIFY_REQUIRED = 2034;//                => [2034, 'Your entered email is not verified yet.'],

    // Validation code for “Password” field
    public static final int ERROR_PASSWORD_REQUIRED = 2040;//                    => [2040, 'The password field is required.'],
    public static final int ERROR_PASSWORD_MIN = 2041;//                         => [2041, 'Password must be at least 8 characters.'],
    public static final int ERROR_PASSWORD_MAX = 2042;//                         => [2042, 'Password must least 255 characters.'],
    public static final int ERROR_PASSWORD_CONFIRMED = 2043;//                   => [2043, 'New password doesn\'t match.'],
    public static final int ERROR_PASSWORD_CONFIRMED_NOT_MATCH = 2044;//         => [2044, 'Confirmed password doesn\'t match.'],
    public static final int ERROR_PASSWORD_NEW_REQUIRED = 2045;//                => [2045, 'New password is required.'],
    public static final int ERROR_PASSWORD_WRONG = 2046;//                       => [2046, 'You\'ve entered wrong password.'],

    // Validation code for “Age” field
    public static final int ERROR_AGE_REQUIRED = 2050;//                         => [2050, 'The age field is required.'],
    public static final int ERROR_AGE_WRONG = 2051;//                            => [2051, 'The age must be a number.'],
    public static final int ERROR_AGE_MAX = 2052;//                              => [2052, 'The age may not be greater than :max.'],

    // Validation code for “Title” field
    public static final int ERROR_TITLE_REQUIRED = 2330;//                       => [2330, 'The title field is required.'],
    public static final int ERROR_TITLE_MIN = 2331;//                            => [2331, 'The title be at least 3 characters.'],
    public static final int ERROR_TITLE_MAX = 2332;//                            => [2332, 'The title must least 25 characters.'],

    // Validation code for “Description” field
    public static final int ERROR_DESCRIPTION_REQUIRED = 2340;//                 => [2340, 'The description field is required.'],
    public static final int ERROR_DESCRIPTION_MIN = 2341;//                      => [2341, 'The description must be at least 3 characters.'],
    public static final int ERROR_DESCRIPTION_MAX = 2342;//                      => [2342, 'The description  must least 2000 characters.'],

    // Validation code for “Gender” field
    public static final int ERROR_GENDER_REQUIRED = 2080;//                      => [2080, 'The gender field is required.'],

    // Verify
    public static final int ERROR_VERIFY_REQUIRED = 2070;//                      => [2070, 'The verification field is required.'],
    public static final int ERROR_VERIFY_FAILED = 2071;//                        => [2071, 'Activation is failed or expired. Please try again.'],

    // Upload file Start
    public static final int ERROR_UPLOAD_FILE = 2080;//                          => [2080, 'Error when upload file'],

    // Login Permission Start
    public static final int ERROR_LOGIN_PERMISSION = 2090;//                     => [2090, 'Please login to access this app.'],

    // Login
    public static final int ERROR_LOGIN_FAIL = 2100;//                           => [2100, 'Login Error Invalid email/phone/password combination. Please try again.'],

    // Reset password
    public static final int ERROR_RESET_PWD_SENT_PWD_FAILED_OR_EXPIRED = 2110;// => [2110, 'Attempt to reset password is failed or expire. Please try again.'],
    public static final int ERROR_RESET_PWD_SENT_PWD_PERMISSION = 2111;//        => [2111, 'You can\'t reset password this account.'],
    public static final int ERROR_RESET_FAIL = 2112;//                           => [2112, 'Reset fail.'],

    // Activation
    public static final int ERROR_ACTIVATE_VERIFY_COMPLETED = 2120;//            => [2120, 'Activation has completed.'],
    public static final int ERROR_USER_DELETED = 2121;//                         => [2121, 'The account registered with this email ID has been previously deleted. Please contact our admin for reactivating the account.'],
    public static final int ERROR_USER_WRONG_PASSWORD_OR_EMAIL = 2122;//         => [2122, 'Email Address and Password combination incorrect!'],
    public static final int ERROR_USER_EMAIL_CHANGED = 2123;//                   => [2123, 'Can\'t change email!'],
    public static final int ERROR_USER_DEACTIVATE_PERMISSION = 2124;//           => [2124, 'You don\'t have permission to deactivate this account.'],
    public static final int ERROR_USER_ACTIVATE_PERMISSION = 2125;//             => [2125, 'You don\'t have permission to activate this account.'],
    public static final int ERROR_USERS_ACTIVATE_VERIFY_FAILED = 2126;//         => [2126, 'Activation is failed or expired. Please try again!'],
    public static final int ERROR_USERS_PERMISSION = 2127;//                     => [2127, 'You don\'t have permission!'],
    public static final int ERROR_USER_UNACTIVE = 2128;//                        => [2128, 'User have not activated!'],
    public static final int ERROR_USER_NOT_FOUND = 2129;//                       => [2129, 'Not found any users!'],

    // Socialize Start
    public static final int ERROR_FB_CODE_TOKEN_REQUIRED = 2200;//               => [2200, 'The Facebook token is required'],
    public static final int ERROR_FB_CODE_TOKEN_ACCESS = 2201;//                 => [2201, 'Can not access token'],
    public static final int ERROR_FB_CODE_ACCOUNT_EXITS = 2202;//                => [2202, 'The Account has already existed, please connected.'],

    // Auth Facebook Login Start
    public static final int ERROR_FACEBOOK_CODE_TAKEN_ACCOUNT = 2210;//          => [2210, 'Facebook account has already been taken.'],
    public static final int ERROR_FACEBOOK_CODE_REQUIRED_REGISTER = 2211;//      => [2211, 'Facebook account not register.'],
    public static final int ERROR_FACEBOOK_CODE_AUTH_FAILED = 2212;//            => [2212, 'Facebook auth fail.'],
    public static final int ERROR_FACEBOOK_TYPE_OAUTHEXCEPTION = 2213;//         => [2213, 'Error validating access token: The session is invalid because the user logged out.'],

    // Validation code for “Language” field
    public static final int ERROR_LANGUAGE_ADDED = 2230;//                       => [2230, 'The language has already been added as your language.'],

    // Validation code for “Category” field
    public static final int ERROR_CATEGORY_ADDED = 2240;//                       => [2240, 'The category has already been added as your category.'],

    // Validation code for “Review” field
    public static final int ERROR_REVIEW_ADDED = 2261;//                         => [2261, 'Another review has already been added for this job.'],
    public static final int ERROR_RATING_REQUIRED = 2265;//                      => [2265, 'The rating field is required.'],
    public static final int ERROR_RATING_MIN = 2266;//                           => [2266, 'The rating length must be greater than 1 characters.'],
    public static final int ERROR_RATING_MAX = 2267;//                           => [2267, 'The rating may not be greater than 5 characters.'],
    public static final int ERROR_RATING_INTEGER = 2268;//                       => [2268, 'The rating must be an integer.'],

    // Other validation
    public static final int ERROR_PHONE_NUMBER_REQUIRED = 2300;//                => [2300, 'The Phone Number field is required.'],
    public static final int ERROR_COUNTRY_ID_INVALID = 2300;//                   => [2300, 'The Country field is invalid.'],
    public static final int ERROR_COUNTRY_ID_EXISTS = 2300;//                    => [2300, 'The Country field is invalid.'],

    // Validation code for “Message” field
    public static final int ERROR_MESSAGE_REQUIRED = 2350;//                     => [2350, 'The message field is required.'],

    // Validation code for “Category” field
    public static final int ERROR_CATEGORY_NAME_REQUIRED = 2001;//               => [2001, 'The name field is required.'],
    public static final int ERROR_CATEGORY_NAME_MIN = 2002;//                    => [2002, 'The name length must be greater than 3 characters.'],
    public static final int ERROR_CATEGORY_NAME_MAX = 2003;//                    => [2003, 'The name may not be greater than 120 characters.'],

    // Backend
    public static final int ERROR_BACKEND_PERMISSION = 2600;//                   => [2600, 'Please login by admin account.'],
    // Transaction

    //Country
    public static final int ERROR_COUNTRY_EXIST = 2701;//                        => [2701, 'The country have entered does not exist'],
    public static final int ERROR_PERMISSION_DELETE = 2702;//                    => [2702, 'You don\'t have permission to delete this item.'],

    //User
    public static final int ERROR_USER_EXIST = 2710;//                           => [2710, 'The user have entered does not exist'],

    //Phone number
    public static final int ERROR_PHONE_EXISTS = 2730;//                         => [2730, 'The phone number has already been taken.'],

    //Note
    public static final int ERROR_NOTE_EXISTS = 2740;//                          => [2740, 'The note identify have entered does not exist'],

    //Upload file
    public static final int ERROR_UPLOAD_FILE_ERROR_CODE = 2745;//               => [2745, 'Error when upload file'],

    //Block User
    public static final int ERROR_USER_DONT_BLOCK = 2760;//                      => [2760, 'You don\'t block this user'],
    public static final int ERROR_USER_ANY_BLOCK = 2761;//                       => [2761, 'You haven\'t blocked any user'],

    // Location
    public static final int ERROR_LOCATION_ID_REQUIRED = 2781;//                 => [2781, 'The Location field is required.'],
    public static final int ERROR_LOCATION_ID_EXISTS = 2782;//                   => [2782, 'The location identify have entered does not exist'],
    public static final int ERROR_LOCATION_REQUIRED = 2783;//                    => [2783, 'The Location field is required.'],
    public static final int ERROR_LOCATION_MIN = 2784;//                         => [2784, 'The Location length must be greater than 3 characters.'],
    public static final int ERROR_LOCATION_MAX = 2785;//                         => [2785, 'The Location may not be greater than 50 characters.'],
    public static final int ERROR_LOCATION_INVALID = 2786;//                     => [2786, 'The Location field is invalid.'],
    public static final int ERROR_LATITUDE_INVALID = 2787;//                     => [2787, 'The Latitude field is invalid.'],
    public static final int ERROR_LATITUDE_REQUIRED = 2787;//                    => [2787, 'The Latitude field is required.'],
    public static final int ERROR_LONGITUDE_INVALID = 2788;//                    => [2788, 'The Longitude field is invalid.'],
    public static final int ERROR_LONGITUDE_REQUIRED = 2788;//                   => [2788, 'The Latitude field is required.'],

    //Comment
    public static final int ERROR_COMMENT_PERMISSION = 2800;//                   => [2800, 'You don\'t have permission to delete this comment.'],

    //Trips
    public static final int ERROR_TRIP_NOT_FOUND = 2801;//                       => [2801, 'Not found any trips!'],
    public static final int ERROR_TRIP_SCHEDULE_NOT_FOUND = 2802;//              => [2802, 'Not found any trip schedules!'],
    public static final int ERROR_TRIP_UPDATE_PERMISSION = 2803;//               => [2803, 'You don\'t have permission to update this trip.'],
    public static final int ERROR_TRIP_SCHEDULE_UPDATE_PERMISSION = 2804;//      => [2804, 'You don\'t have permission to update this trip schedule.'],
}
