package org.fourbit.stuffeverywhere;

import android.app.Application;
import android.os.StrictMode;

public class StuffEverywhereApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        /**
         * Enable {@link StrictMode}
         */
        if (getResources().getBoolean(R.bool.is_in_developer_mode)) {
               StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
               .detectNetwork()
               .penaltyDialog()
               .detectAll()
               .penaltyLog()
               .build());
        }
    }
}
