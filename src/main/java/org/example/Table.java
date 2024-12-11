package org.example;

import java.util.List;
import java.util.Map;

public class Table {
    private List<String> titles;
    private List<Map<String, String>> data;

    public Table(List<String> titles, List<Map<String, String>> data) {
        this.titles = titles;
        this.data = data;
    }

    public List<String> getTitles() {
        return titles;
    }

    public List<Map<String, String>> getData() {
        return data;
    }
}
