package com.WooGLEFX.Engine;

import java.util.ArrayList;
import java.util.List;

import com.WooGLEFX.Structures.EditorAttribute;
import com.WooGLEFX.Structures.EditorObject;

public class ClipboardHandler {

    public static List<EditorObject> importFromClipboardString(String clipboard) {

        //WOGEditor:circle<id=wheel;x=2;y=1024.9834;radius=120;material=machine;tag=mostlydeadly>circle<id=wheel;x=2;y=1024.9834;radius=120;material=machine;tag=mostlydeadly>

        List<EditorObject> objects = new ArrayList<>();
        EditorObject object = null;

        String currentWord = "";
        String attributeName = "";
        boolean settingAttribute = false;

        for (int i = 0; i < clipboard.length(); i++){
            char part = clipboard.charAt(i);

            if (settingAttribute) {
                if (part == '=') {
                    attributeName = currentWord;
                    currentWord = "";
                } else if (part == ';') {
                    object.setAttribute(attributeName, currentWord);
                    currentWord = "";
                } else if (part == '>') {
                    settingAttribute = false;
                    object.setAttribute(attributeName, currentWord);
                    currentWord = "";
                    objects.add(object);
                    object = null;
                } else {
                    currentWord += part;
                }
            } else {
                if (part == ':') {
                    if (!currentWord.equals("WOGEditor")) {
                        return null;
                    }
                    currentWord = "";
                } else if (part == '<') {
                    object = EditorObject.create(currentWord, new EditorAttribute[0], null);
                    object.setRealName(currentWord);
                    currentWord = "";
                    settingAttribute = true;
                } else {
                    currentWord += part;
                }
            }
        }

        return objects;

    }

    public static String exportToClipBoardString(List<EditorObject> objects) {
        StringBuilder clipboard = new StringBuilder("WOGEditor:");

        for (EditorObject object : objects) {
            clipboard.append(object.getRealName());
            clipboard.append("<");

            for (int i = 0; i < object.getAttributes().length; i++){
                EditorAttribute attribute = object.getAttributes()[i];
                if (attribute.getValue() != null && !attribute.getValue().equals(attribute.getDefaultValue()) && !attribute.getValue().equals("")){
                    clipboard.append(attribute.getName()).append("=").append(attribute.getValue());
                    clipboard.append(";");
                }
            }

            clipboard.deleteCharAt(clipboard.length() - 1);
            clipboard.append(">");
        }

        return clipboard.toString();
    }
}
