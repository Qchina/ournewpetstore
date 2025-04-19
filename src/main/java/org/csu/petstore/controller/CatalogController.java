package org.csu.petstore.controller;

import org.csu.petstore.common.CommonResponse;
import org.csu.petstore.entity.Product;
import org.csu.petstore.service.CatalogService;
import org.csu.petstore.vo.CategoryVO;
import org.csu.petstore.vo.ItemVO;
import org.csu.petstore.vo.ProductVO;
import org.csu.petstore.vo.SearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.alibaba.fastjson.JSON;

@RestController
@Validated
@RequestMapping("/api/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

//    @GetMapping("index")
//    public String index(){
//        return "catalog/main";
//    }

    // 获取分类信息
    @GetMapping("/category")
    public CommonResponse<CategoryVO> viewCategory(@RequestParam String categoryId) {
        CategoryVO categoryVO = catalogService.getCategory(categoryId);
        if (categoryVO == null) {
            return CommonResponse.createForError("Category not found");
        }
        return CommonResponse.createForSuccess(categoryVO);
    }

    // 获取产品信息
    @GetMapping("/product")
    public CommonResponse<ProductVO> viewProduct(@RequestParam String productId) {
        ProductVO productVO = catalogService.getProduct(productId);
        if (productVO == null) {
            return CommonResponse.createForError("Product not found");
        }
        return CommonResponse.createForSuccess(productVO);
    }

    // 获取商品详情
    @GetMapping("/item")
    public CommonResponse<ItemVO> viewItem(@RequestParam String itemId) {
        ItemVO itemVO = catalogService.getItem(itemId);
        if (itemVO == null) {
            return CommonResponse.createForError("Item not found");
        }
        return CommonResponse.createForSuccess(itemVO);
    }

    // 搜索产品
    @GetMapping("/search")
    public CommonResponse<List<SearchVO>> searchProducts(@RequestParam String keyword) {
        List<SearchVO> searchVOList = catalogService.getSearch(keyword);
        if (searchVOList.isEmpty()) {
            return CommonResponse.createForError("No product found");
        }
        return CommonResponse.createForSuccess(searchVOList);
    }

    // 自动补全（关键词提示）
    @GetMapping("/autocomplete")
    public CommonResponse<List<Product>> searchAutoComplete(@RequestParam String keyword) {
        List<Product> productList = catalogService.searchProductList(keyword);
        return CommonResponse.createForSuccess(productList);
    }
}
