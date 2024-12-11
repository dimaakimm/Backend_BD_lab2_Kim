package org.example;

import java.util.*;

public class EditRowProps {
    private String index;
    private String id;
    private String name;
    private String number;
    private String email;
    public String getId(){
        return id;
    };
    public String getName(){
        return name;
    };
    public String getIndex(){
        return index;
    };
    public String getNumber(){
        return number;
    }
    public String getEmail(){
        return email;
    }
    public Map<String, String> getNonNullFields() {
        // Создаем карту для ненулевых полей
        Map<String, String> nonNullFields = new LinkedHashMap<>();

        // Добавляем в карту только те поля, которые не равны null или пустой строке
        if (id != null && !id.isEmpty()) {
            nonNullFields.put("id", id);
        }
        if (name != null && !name.isEmpty()) {
            nonNullFields.put("name", name);
        }
        if (number != null && !number.isEmpty()) {
            nonNullFields.put("number", number);
        }
        if (email != null && !email.isEmpty()) {
            nonNullFields.put("email", email);
        }

        return nonNullFields;
    }
}
