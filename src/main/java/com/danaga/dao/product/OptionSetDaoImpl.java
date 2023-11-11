package com.danaga.dao.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.danaga.dto.product.OptionSetCreateDto;
import com.danaga.dto.product.OptionSetUpdateDto;
import com.danaga.dto.product.ProductListOutputDto;
import com.danaga.dto.product.QueryStringDataDto;
import com.danaga.entity.Member;
import com.danaga.entity.OptionSet;
import com.danaga.entity.Options;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionsException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoProductException;
import com.danaga.repository.MemberRepository;
import com.danaga.repository.product.OptionSetRepository;
import com.danaga.repository.product.OptionSetSearchQuery;
import com.danaga.repository.product.OptionsRepository;
import com.danaga.repository.product.ProductRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OptionSetDaoImpl implements OptionSetDao{

	private final OptionSetRepository repository;
	private final OptionsRepository optionsRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	
	@PersistenceContext
	private EntityManager em;

	
	@Override
	public List<ProductListOutputDto> findByFilter(QueryStringDataDto dataDto,Integer firstResult){
		String jpql = new OptionSetSearchQuery(dataDto).build();
		TypedQuery<OptionSet> query = em.createQuery(jpql,OptionSet.class);
		query.setFirstResult(firstResult);
		query.setMaxResults(20);
		List<ProductListOutputDto> finalResult = query.getResultList().stream()
		        .map(t -> {
		            ProductListOutputDto productDto = new ProductListOutputDto(t);
		            return productDto;
		        })
		        .collect(Collectors.toList());
		return finalResult;
	}
	@Override
	public List<ProductListOutputDto> findForMemberByFilter(QueryStringDataDto dataDto, String username,Integer firstResult ) throws FoundNoMemberException{
		Member member = null;
			if(username.contains("@")) {
				member =memberRepository.findByEmail(username).orElseThrow(FoundNoMemberException::new);
			}else { 
			member = memberRepository.findByUserName(username).orElseThrow(FoundNoMemberException::new);
			}
		    String mainJpql = new OptionSetSearchQuery(dataDto).build();
		    TypedQuery<OptionSet> query = em.createQuery(mainJpql, OptionSet.class);
		    query.setFirstResult(firstResult);
		    query.setMaxResults(20);
		    TypedQuery<Long> heartQuery = em.createQuery("SELECT i.optionSet.id FROM Interest i WHERE i.member = :member", Long.class);
		    heartQuery.setParameter("member", member);
		    List<Long> heartOptionSetIds = heartQuery.getResultList();
		    
		    List<ProductListOutputDto> finalResult = query.getResultList().stream()
		        .map(t -> {
		            ProductListOutputDto productDto = new ProductListOutputDto(t);
		            if (!heartOptionSetIds.isEmpty()) {
		                productDto.setIsInterested(heartOptionSetIds.contains(t.getId()));
		            }
		            return productDto;
		        })
		        .collect(Collectors.toList());
		return finalResult;
	}
	@Override
	public OptionSet findById(Long id) throws FoundNoOptionSetException {
		return repository.findById(id).orElseThrow(() -> new FoundNoOptionSetException());
	}
	@Override
	public List<OptionSet> findAllByProductId(Long productId) {
		return repository.findByProductId(productId);
	}
	@Override
	public OptionSet create(OptionSetCreateDto dto) {
		return repository.save(dto.toEntity());
	}
	@Override
	public void deleteById(Long id) throws FoundNoOptionSetException {
		repository.findById(id).orElseThrow(() -> new FoundNoOptionSetException());
		repository.deleteById(id);
	}
	@Override
	public void deleteAllByProductId(Long productId) throws FoundNoProductException {
		productRepository.findById(productId).orElseThrow(() -> new FoundNoProductException());
		repository.deleteAllByProductId(productId);
		
	}
	@Override
	public List<OptionSet> findAllByRecentView_MemberId(Long memberId) throws FoundNoMemberException {
		memberRepository.findById(memberId).orElseThrow(() -> new FoundNoMemberException());
		return repository.findAllByRecentViews_MemberId(memberId);
	}
	@Override
	public List<OptionSet> findAllByInterest_MemberId(Long memberId) throws FoundNoMemberException {
		memberRepository.findById(memberId).orElseThrow(() -> new FoundNoMemberException());
		return repository.findAllByInterests_MemberId(memberId);
	}
	@Override
	public OptionSet findByOptionId(Long optionId) throws FoundNoOptionsException {
		optionsRepository.findById(optionId).orElseThrow(() -> new FoundNoOptionsException());
		return repository.findByOptions_Id(optionId);
	}
	@Override
	public OptionSet updateStock(OptionSetUpdateDto dto) throws FoundNoOptionSetException {
		//존재하는 optionset인지 검증
		OptionSet origin = repository.findById(dto.getId()).orElseThrow(() -> new FoundNoOptionSetException());
		origin.setStock(dto.getStock());
		return repository.save(origin);
	}
	@Override
	public OptionSet updateOrderCount(Long optionSetId, Integer orderCount) throws FoundNoOptionSetException {
		OptionSet origin = repository.findById(optionSetId).orElseThrow(() -> new FoundNoOptionSetException());
		origin.setOrderCount(orderCount);
		return repository.save(origin);
	}
	@Override
	public OptionSet updateViewCount(Long optionSetId) throws FoundNoOptionSetException {
		OptionSet origin = repository.findById(optionSetId).orElseThrow(() -> new FoundNoOptionSetException());
		origin.setViewCount(origin.getViewCount()+1);
		return repository.save(origin);
	}
	@Override
	public OptionSet calculateTotalPrice(OptionSetUpdateDto dto) throws FoundNoOptionSetException {
		OptionSet origin = repository.findById(dto.getId()).orElseThrow(() -> new FoundNoOptionSetException());
		int productPrice = origin.getTotalPrice();
		int totalPrice = productPrice;
		List<Options> options = dto.getOptions().stream().map(t -> t.toEntity()).collect(Collectors.toList());
		for (int i = 0; i < options.size(); i++) {
			totalPrice+=options.get(i).getExtraPrice();
		}
		origin.setTotalPrice(totalPrice);
		return repository.save(origin);
	}

}

