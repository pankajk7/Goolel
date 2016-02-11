package com.goolel.utils;

import java.net.URI;

import android.R.bool;
import android.net.Uri;

public class Constants {
	
	public static String BASE_URL = "http://khadikhazana.com/goolel/webservice/";
	public static String SUFFIX_URL = "";
	public static int TIMEOUT = 20000;
//	public static boolean ISCAPTURE = false;
//	public static Uri FILE_URI ;
//	public static String VIDEO_TITLE ="";
	//Shared Preference
	public static String PREF_FILE_NAME = "main_file";
	public static String PREF_USER_ID = "user_id";
	public static String PREF_NOTIFICATION = "notification";
	public static String PREF_NOTIFICATION_MESSAGE = "notification_message";
	
	//GCM
	 // Google project id
    public static final String SENDER_ID = "23132219625"; 
	public static final String DISPLAY_MESSAGE_ACTION =
            "com.goolel.pushnotifications.DISPLAY_MESSAGE";
    public static final String EXTRA_MESSAGE = "message";

    //Parameter
//	public static final String FILE_UPLOAD_URL = "http://khadikhazana.com/uploads/video/post_api.php";
	public static final String FILE_UPLOAD_URL = "http://khadikhazana.com/goolel/webservice/upload_video";
	
	//Parameter API Call
	public static final String PARAMS_API_TITLE = "title";
	public static final String PARAMS_API_FROM_DATE = "from_date";
	public static final String PARAMS_API_TO_DATE = "to_date";
	
	//Parameter intent
	public static final String PARAMS_INTENT_VIDEO_LIST = "video_list_obj";
	
	// Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "file_upload";

	//Warning
	public static String ERROR_NETWORK_ERROR = "Network error occur.";
	
	//API
	public static final String API_GET_PROFILE = "profile/";
	public static final String API_LOGIN = "login";
	public static final String API_EDIT = "edit_profile";
	public static final String API_REGISTER = "add";
	public static final String API_FORGOT_PASSWORD = "forgot_pwd";
	public static final String API_CHANGE_PASSWORD = "change_pwd";
	public static final String API_GET_VIDEO_LIST = "video_list/";  //add user id at end
	public static final String API_GET_DELETE_VIDEO = "video_delete/";  //user_id
	public static final String API_GET_DELETE_VIDEO_PARAM1 = "/"; 
	public static final String API_GET_VIDEO_LIST_SEARCH = "video_list/";//video_id
	public static final String API_POST_VIDEO_LIST_SEARCH = "video_list/";//video_id
	public static final String API_VIDEO_TITLE_DUPLICATE = "video_title_duplicate";
	public static final String API_EMAIL_DUPLICATE = "email_duplicate";
}
