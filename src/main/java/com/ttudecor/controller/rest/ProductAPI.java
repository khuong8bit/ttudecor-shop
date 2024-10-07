package com.ttudecor.controller.rest;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ttudecor.dto.ProductDto;
import com.ttudecor.entity.Category;
import com.ttudecor.entity.Product;
import com.ttudecor.service.CategoryService;
import com.ttudecor.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductAPI {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	//all products
	@RequestMapping("all")
	public List<ProductDto> showAll(@PageableDefault(size = 9) Pageable pageable) {
		Page<ProductDto> page = productService.getProductDtoPaginated(pageable);
		return page.getContent();
	}
	
	//List product filter by category
	@RequestMapping("{id}")
	public ProductDto getProductById(@PathVariable("id") String id) {
		Product product = productService.findProductById(Integer.parseInt(id));
		return productService.copy(product);
	}
	
	//List product filter by category
	@RequestMapping("categoryId/{id}")
	public List<ProductDto> showByCategory(@PathVariable("id") String id, 
			@PageableDefault(size = 9) Pageable pageable) {
		Page<ProductDto> page = productService.getProductDtoByCategoryIdPaginated(Integer.parseInt(id), pageable);
		return page.getContent();
	}
	
	//Search product by name
	@RequestMapping("search")
	public List<ProductDto> search(@RequestParam("productName") String name,
			@PageableDefault(size = 9) Pageable pageable) {
		Page<ProductDto> page = productService.getProductDtoByNamePaginated(name, pageable);
		return page.getContent();
	}
	
	//Detail product page
	@GetMapping("/product/{url}")
	public String productDetail(Model model, @PathVariable("url") String url,
			@CookieValue(value = "related_product", defaultValue = "") String json,
			HttpServletResponse response) {
		
		url = url.substring(url.length() - 3);
		
		int id = Integer.parseInt(url);
		Product product = productService.findProductById(id);

		List<ProductDto> relatedProducts = productService.getRelatedProducts(response, json, product);
		Category category = product.getCategory();
		
		model.addAttribute("category", category);
		model.addAttribute("product", product);
		model.addAttribute("relatedProducts", relatedProducts);
		
		model.addAttribute("shopPage", true);
		return "shop/product-detail";
	}
	
	@GetMapping("/product")
	public String product() {
		return "redirect:/shop";
	}
}
