package com.uuuuuuuuuuuuuuu.search.repository.response;

import java.util.List;


public class ScrollResponse<T> {
    private List<T> list;
    private String scrollId;

    public ScrollResponse(List<T> list, String scrollId) {
        this.list = list;
        this.scrollId = scrollId;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }
}
