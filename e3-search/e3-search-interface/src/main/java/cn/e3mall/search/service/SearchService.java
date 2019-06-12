package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchResult;

/**
 * 商品搜索Service
 *
 * @author XYQ
 */
public interface SearchService {
    /**
     * 搜索商品
     *
     * @param keyword 关键字
     * @param page    查询第几页
     * @param rows    每页显示记录数
     * @return 查询结果pojo
     * @throws Exception 抛出的异常
     */
    SearchResult search(String keyword, int page, int rows) throws Exception;
}
