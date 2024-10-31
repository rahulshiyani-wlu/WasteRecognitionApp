package com.example.project.waste_recognition_app;

import androidx.annotation.Keep;

@Keep
class CustomTools {
    @Keep
    static String capitailizeWord(String str) {
        if (str == null){
            return str;
        }

        StringBuffer s = new StringBuffer();


        char ch = ' ';
        for (int i = 0; i < str.length(); i++) {

            if (ch == ' ' && str.charAt(i) != ' ')
                s.append(Character.toUpperCase(str.charAt(i)));
            else
                s.append(str.charAt(i));
            ch = str.charAt(i);
        }

        return s.toString().trim();
    }
}
