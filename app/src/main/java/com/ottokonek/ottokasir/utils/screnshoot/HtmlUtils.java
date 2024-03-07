package com.ottokonek.ottokasir.utils.screnshoot;

import android.text.Html;
import android.text.Spanned;

public class HtmlUtils {

    /**
     * Get HTML Content
     *
     * @param content
     * @return
     */
    public static Spanned getHTMLContent(String content){
        return Html.fromHtml(content);
    }
}
