package com.example.onlinemusicapp.Model;

import com.squareup.moshi.Json;

import java.util.List;

public class MV {
    @Json(name = "items")
    private List<Item> items = null;
    @Json(name = "total")
    private int total;
    @Json(name = "hasMore")
    private boolean hasMore;

    public MV() {
    }

    /**
     *
     * @param total
     * @param hasMore
     * @param items
     */
    public MV(List<Item> items, int total, boolean hasMore) {
        super();
        this.items = items;
        this.total = total;
        this.hasMore = hasMore;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

}
