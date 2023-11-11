package com.danaga.dao.product;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.danaga.entity.RecentView;
import com.danaga.exception.product.AlreadyExistsException.ExistsRecentViewException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoRecentViewException;
import com.danaga.repository.MemberRepository;
import com.danaga.repository.product.OptionSetRepository;
import com.danaga.repository.product.RecentViewRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class RecentViewDaoImpl implements RecentViewDao{

	private final RecentViewRepository repository;
	private final MemberRepository memberRepository;
	private final OptionSetRepository optionSetRepository;
	
	@PersistenceContext
	private EntityManager em;
	
	//나의 최근본 상품 하나 선택 삭제
	@Override
	public void delete(RecentView entity) throws FoundNoRecentViewException {
		RecentView find = repository.findByMemberIdAndOptionSetId(entity.getMember().getId(), entity.getOptionSet().getId());
		if(find==null) {
			throw new FoundNoRecentViewException();
		}
		repository.deleteByMemberIdAndOptionSetId(find.getMember().getId(),find.getOptionSet().getId());
	}
	//나의 최근본 상품 전체 삭제
	@Override
	public void deleteAll(Long memberId) throws FoundNoRecentViewException {
		List<RecentView> find = repository.findByMemberId(memberId);
		if(find==null) {
			throw new FoundNoRecentViewException();
		}
		repository.deleteByMemberId(memberId);
	}

	//recentView 추가
	@Override
	public RecentView save(RecentView entity) throws FoundNoMemberException, FoundNoOptionSetException, ExistsRecentViewException {
		memberRepository.findById(entity.getMember().getId()).orElseThrow(() -> new FoundNoMemberException());
		optionSetRepository.findById(entity.getOptionSet().getId()).orElseThrow(()-> new FoundNoOptionSetException());
		RecentView find=repository.findByMemberIdAndOptionSetId(entity.getMember().getId(), entity.getOptionSet().getId());
		if(find!=null) {
			throw new ExistsRecentViewException();
		}
		return repository.save(entity);
	}
	
	//30일 지난 최근 본 상품 목록 삭제
	@Override
	public void removeOldRecents() {
		String jpql = "DELETE FROM RecentView rv WHERE rv.createTime < :cutoffDate";
		em.createQuery(jpql)
		  .setParameter("cutoffDate", LocalDateTime.now().minusDays(30))
		  .executeUpdate();
	}
	
}
