package org.example;

import java.util.Objects;

public class SelectDeleteRowProps {
    private String title;
    private String value;

    public String getTitle(){
        return title;
    };
    public String getValue(){
        return value;
    };
    public boolean isPrimaryKey(){
        return Objects.equals(title, "id");
    }
    public int getTitleIndex(){
        switch (title) {
            case "id":
                return 0;
            case "name":
                return 1;
            case "number":
                return 2;
            case "email":
                return 3;
            default:
                return -1;
        }
    }

}
