package com.danaga.repository.product;

import java.util.List;

import com.danaga.dto.product.CategoryDto;
import com.danaga.dto.product.OptionDto;
import com.danaga.dto.product.QueryStringDataDto;

public class OptionSetSearchQuery {

	private String searchQuery;

	public void setOrderType(String orderType) {
		this.searchQuery = this.searchQuery.replace(":orderType", orderType);
	}

	public OptionSetSearchQuery() {
		this.searchQuery = "SELECT os " + "FROM OptionSet os " + " join fetch os.product p" + " WHERE os.stock >0 "
				+ " Order By :orderType ";
		setOrderType(OptionSetQueryData.BY_ORDER_COUNT);// default 설정
	}

	public OptionSetSearchQuery(QueryStringDataDto searchDto) {
		if (searchDto.getCategory() != null) {
			CategoryDto category = searchDto.getCategory();

			this.searchQuery = "SELECT os " + "FROM OptionSet os " + " join fetch os.product p "
					+ " join fetch p.categorySets cs  " + " join fetch cs.category c " + " WHERE os.stock >0 ";
			categoryFilter(category);
		} else {
			this.searchQuery = "SELECT os " + "FROM OptionSet os " + " join fetch os.product p" + " WHERE os.stock >0 ";
		}
		if (searchDto.getOptionset() != null) {
			List<OptionDto.OptionNameValueMapDto> optionset = searchDto.getOptionset();

			for (int i = 0; i < optionset.size(); i++) {
				String key = optionset.get(i).getOptionName();
				optionFilter(key, optionset.get(i).getOptionValue());
			}
		}
		if (searchDto.getNameKeyword() != null) {
			nameKeyword(searchDto.getNameKeyword());
		}
		if (searchDto.getMinPrice() != null && searchDto.getMaxPrice() != null) {
			priceRange(searchDto.getMinPrice(), searchDto.getMaxPrice());
		}
		if (searchDto.getMinPrice() != null && searchDto.getMaxPrice() == null) {
			onlyMinConstraint(searchDto.getMinPrice());
		}
		if (searchDto.getMaxPrice() != null && searchDto.getMinPrice() == null) {
			onlyMaxConstraint(searchDto.getMaxPrice());
		}

		this.searchQuery += " Order By :orderType ";
		if (searchDto.getOrderType() != null) {
			String orderType = searchDto.getOrderType();
			if (orderType.equals("판매순")) {
				setOrderType(OptionSetQueryData.BY_ORDER_COUNT);
			} else if (orderType.equals("조회순")) {
				setOrderType(OptionSetQueryData.BY_VIEW_COUNT);
			} else if (orderType.equals("최신순")) {
				setOrderType(OptionSetQueryData.BY_CREATE_TIME);
			} else if (orderType.equals("최저가순")) {
				setOrderType(OptionSetQueryData.BY_TOTAL_PRICE);
			} else {// default
				setOrderType(OptionSetQueryData.BY_ORDER_COUNT);
			}
		} else {
			setOrderType(OptionSetQueryData.BY_ORDER_COUNT);
		}
	}

	public void categoryFilter(CategoryDto category) {
		String category_filter = "";
		if (category.getName().equals("전체")) {
			category_filter = "AND c.parent.id = :categoryFilter ";
			category_filter = category_filter.replace(":categoryFilter", "" + category.getId() + "");
		} else {
			category_filter = "AND c.id = :categoryFilter ";
			category_filter = category_filter.replace(":categoryFilter", "" + category.getId() + "");
		}
		this.searchQuery += category_filter;
	}

	public void optionFilter(String optionName, List<String> optionValue) {
		if (optionValue != null) {
			String valueString = "o.value= :optionValue";
			if (optionValue.size() == 1) {
				valueString = valueString.replace(":optionValue", "'" + optionValue.get(0) + "' ");
			} else if (optionValue.size() > 1) {
				valueString = valueString.replace(":optionValue", "'" + optionValue.get(0) + "' ");
				for (int i = 1; i < optionValue.size(); i++) {
					valueString += " OR o.value=" + "'" + optionValue.get(i) + "' ";
				}
			}
			String option_filter = "AND EXISTS ( SELECT 1 FROM Options o WHERE o.optionSet = os AND o.name = :optionName AND ("
					+ valueString + ") ) ";
			option_filter = option_filter.replace(":optionName", "'" + optionName + "'");
			this.searchQuery += option_filter;
		}
	}

	public void priceRange(int minPrice, int maxPrice) {
		String price_range = "AND os.totalPrice between :minPrice and :maxPrice ";
		price_range = price_range.replace(":minPrice", String.valueOf(minPrice));
		price_range = price_range.replace(":maxPrice", String.valueOf(maxPrice));
		this.searchQuery += price_range;
	}

	private void onlyMinConstraint(int minPrice) {
		String minConstraint = "AND os.totalPrice >= :minPrice ";
		minConstraint = minConstraint.replace(":minPrice", String.valueOf(minPrice));
		this.searchQuery += minConstraint;
	}

	private void onlyMaxConstraint(int maxPrice) {
		String maxConstraint = "AND os.totalPrice <= :maxPrice ";
		maxConstraint = maxConstraint.replace(":maxPrice", String.valueOf(maxPrice));
		this.searchQuery += maxConstraint;
	}

	public void nameKeyword(String nameKeyword) {
		String name_keyword = "AND LOWER(os.product.name) like LOWER(:nameKeyword) ";
		name_keyword = name_keyword.replace(":nameKeyword", "'%" + nameKeyword + "%'");
		this.searchQuery += name_keyword;
	}

	public String build() {
		return this.searchQuery;
	}

}
