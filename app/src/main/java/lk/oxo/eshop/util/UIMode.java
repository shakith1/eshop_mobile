package lk.oxo.eshop.util;

import android.content.Context;
import android.content.res.Configuration;

public class UIMode {
    private static int uiModeFlags;

    public static int getUiModeFlags(Context context) {
        uiModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return uiModeFlags;
    }

}
