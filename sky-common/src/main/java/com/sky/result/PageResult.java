package com.sky.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询结果（所有的分页查询，都可以用他接收前端的“data”；）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult implements Serializable {
    /**
     * 在前端传递参数的过程中，`total`和`records`这两个参数通常表示的是数据的总数。
     *
     * - `total`：这个参数通常表示**数据的总数，即从服务器端获取的所有数据的数量**。
     * 它通常用于在前端显示一个总的数据量，或者**用于分页功能中，以确定当前页码之前有多少个数据页。**
     * - `records`：这个参数通常表示实际返回的数据条数。
     * **在分页功能中，`records`**代表当前这一页要展示的数据集合,数据多个,为数组**
     */

    private long total; //总记录数
//    分页功能中，`records`**代表当前这一页要展示的数据集合,数据多个,为数组（前端是数组，后端用集合接收）
    private List records; //当前页数据集合（查到的当前页的所有实体对象，封装到这个集合中去）

}
