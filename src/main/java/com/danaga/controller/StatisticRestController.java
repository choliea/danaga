package com.danaga.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.danaga.dao.product.ProductDao;
import com.danaga.dto.admin.AdminOptionDto;
import com.danaga.dto.admin.AdminOptionSetDto;
import com.danaga.dto.admin.AdminOrderUpdate;
import com.danaga.dto.admin.AdminProductInsertDto;
import com.danaga.dto.admin.AdminProductOnlyDto;
import com.danaga.dto.product.CategoryDto;
import com.danaga.dto.product.ProductDto;
import com.danaga.dto.product.ProductSaveDto;
import com.danaga.entity.Category;
import com.danaga.entity.CategorySet;
import com.danaga.entity.OptionSet;
import com.danaga.entity.Options;
import com.danaga.entity.Product;
import com.danaga.entity.Statistic;
import com.danaga.repository.BoardRepository;
import com.danaga.repository.CategorySetRepository;
import com.danaga.repository.MemberRepository;
import com.danaga.repository.product.CategoryRepository;
import com.danaga.repository.product.OptionSetRepository;
import com.danaga.repository.product.ProductRepository;
import com.danaga.service.MemberService;
import com.danaga.service.StatisticService;
import com.danaga.service.product.OptionSetService;

@RestController
@RequestMapping("/admin_data")
public class StatisticRestController {
	@Autowired
	private StatisticService statisticService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private OptionSetService optionSetService;
	@Autowired
	private OptionSetRepository optionSetRepository;
	@Autowired
	private CategorySetRepository categorySetRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("/daily_update")
	public ResponseEntity<?> dailyUpdate() {
		statisticService.updateLatest7Days();
		return new ResponseEntity<>("Statistic updated successfully", HttpStatus.OK);
	}

	@GetMapping("/option_year")
	public List<Statistic> optionYear() {
		List<Statistic> list = statisticService.yearlyStatistic("2023");
		return list;
	}

	@GetMapping("/option_days")
	public List<Statistic> optionDays() {
		List<Statistic> list = statisticService.latest7DaysStatistic();
		return list;
	}

	// 기존방식
	@PostMapping("/product")
	public ResponseEntity<?> createProduct(@RequestBody AdminProductInsertDto adminProductInsertDto) {
		try {
			String img = convertPath(adminProductInsertDto.getAdminProductDto().getImg());
			String prevImage = convertPath(adminProductInsertDto.getAdminProductDto().getPrevImage());
			String descImage = convertPath(adminProductInsertDto.getAdminProductDto().getDescImage());
			adminProductInsertDto.getAdminProductDto().setImg(img);
			adminProductInsertDto.getAdminProductDto().setPrevImage(prevImage);
			adminProductInsertDto.getAdminProductDto().setDescImage(descImage);
			statisticService.createProduct(adminProductInsertDto);
			return new ResponseEntity<>("Product created successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error creating product", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/uploadImg")
	public ResponseEntity<?> uploadImg(@RequestParam("img") MultipartFile img,
			@RequestParam("prevImage") MultipartFile prevImage, @RequestParam("descImage") MultipartFile descImage) {
		try {
			String imgFileName = saveImage(img);
			String prevImageFileName = saveImage(prevImage);
			String descImageFileName = saveImage(descImage);
			return new ResponseEntity<>("Images uploaded successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error uploading images", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 신규방식 - 카테고리
	@PostMapping("/createCategory")
	public ResponseEntity<?> createCategory(@RequestBody CategoryDto dto) {
		try {
			categoryRepository.save(dto.toEntity());
			return new ResponseEntity<>("Category created successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error creating category", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 신규방식 - product
	@PostMapping("/createProduct")
	public ResponseEntity<?> createProductOnly(@RequestBody AdminProductOnlyDto adminProductOnlyDto) {
		try {
			adminProductOnlyDto.getProductSaveDto()
					.setImg(convertPath(adminProductOnlyDto.getProductSaveDto().getImg()));
			adminProductOnlyDto.getProductSaveDto()
					.setPrevImage(convertPath(adminProductOnlyDto.getProductSaveDto().getPrevImage()));
			adminProductOnlyDto.getProductSaveDto()
					.setDescImage(convertPath(adminProductOnlyDto.getProductSaveDto().getDescImage()));
			statisticService.createProductOnly(adminProductOnlyDto);
			return new ResponseEntity<>("Product created successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error creating Product", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 신규방식 - optionSet
	@PostMapping("/createOptionSet")
	public ResponseEntity<?> createOptionSet(@RequestBody AdminOptionSetDto dto) {
		try {
			statisticService.createOptionSet(dto);
			return new ResponseEntity<>("OptionSet created successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error creating OptionSet", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/product/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		try {
			OptionSet os = optionSetRepository.findById(id).get();
			Product product = os.getProduct();
			List<CategorySet> cs = product.getCategorySets();
			// delete Options
			List<Options> optionList = os.getOptions();
			for (Options options : optionList) {
				optionSetService.deleteOption(options.getId());
			}
			// delete Option Set
			optionSetService.deleteOptionSet(id);
			// delete Category Set
			for (CategorySet category : cs) {
				categorySetRepository.delete(category);
				// delete Category
				categoryRepository.delete(category.getCategory());
			}
			// delete Product
			productRepository.delete(product);
			return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error deleting product", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/optionSet/{id}")
	public ResponseEntity<?> deleteOptionSet(@PathVariable Long id) {
		try {
			OptionSet os = optionSetRepository.findById(id).get();
			// delete Options
			List<Options> optionList = os.getOptions();
			for (Options options : optionList) {
				optionSetService.deleteOption(options.getId());
			}
			// delete Option Set
			optionSetService.deleteOptionSet(id);
			return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error deleting product", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/order/{id}")
	public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody AdminOrderUpdate adminOrderUpdate) {
		try {
			statisticService.updateOrderStatement(adminOrderUpdate.getOrderId(), adminOrderUpdate.getStatement());
			return new ResponseEntity<>("Order statement changed successfully", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error changing order statement", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/member/{id}")
	public ResponseEntity<?> deleteMember(@PathVariable Long id) {
		try {
			memberService.deleteMember(memberRepository.findById(id).get().getUserName());
			return new ResponseEntity<>("Member deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error deleting member", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/board/{id}")
	public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
		try {
			boardRepository.deleteById(id);
			return new ResponseEntity<>("Board deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error deleting member", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String saveImage(MultipartFile file) throws IOException {
		String fileName = file.getOriginalFilename();
		String filePath = System.getProperty("user.dir") + "/src/main/resources/static/images/uploaded/"
				+ File.separator + fileName;

		File dest = new File(filePath);
		file.transferTo(dest);
		return fileName;
	}

	public static String convertPath(String originalPath) {
		String fileName = originalPath.substring(originalPath.lastIndexOf("\\") + 1);
		String newPath = "/images/uploaded/" + fileName;
		return newPath;
	}

}
