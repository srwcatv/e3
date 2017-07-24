package com.catv.common.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 搜索
 */
@Getter
@Setter
public class SearchItem implements Serializable {
    private String id;
    private String title;
    private String sell_point;
    private long price;
    private String image;
    private String category_name;
}


