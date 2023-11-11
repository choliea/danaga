package com.danaga.dao.product;

import org.springframework.stereotype.Repository;

import com.danaga.dto.product.InterestDto;
import com.danaga.entity.Interest;
import com.danaga.entity.Member;
import com.danaga.entity.OptionSet;
import com.danaga.exception.product.AlreadyExistsException.ExistsInterestException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoInterestException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;
import com.danaga.repository.MemberRepository;
import com.danaga.repository.product.InterestRepository;
import com.danaga.repository.product.OptionSetRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InterestDaoImpl implements InterestDao{

	private final InterestRepository repository;
	private final MemberRepository memberRepository;
	private final OptionSetRepository optionSetRepository;
	
	@Override
	public Boolean isInterested(InterestDto dto) throws FoundNoMemberException, FoundNoOptionSetException {
		Member findMember = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new FoundNoMemberException());
		OptionSet findOptionSet = optionSetRepository.findById(dto.getOptionSetId()).orElseThrow(() -> new FoundNoOptionSetException());
		return repository.existsByMemberIdAndOptionSetId(findMember.getId(), findOptionSet.getId());
	}

	@Override
	public InterestDto save(InterestDto dto) throws FoundNoMemberException, FoundNoOptionSetException, ExistsInterestException {
		memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new FoundNoMemberException());
		optionSetRepository.findById(dto.getOptionSetId()).orElseThrow(() -> new FoundNoOptionSetException());
		if(isInterested(dto)) {
			throw new ExistsInterestException();
		}
		Interest savedEntity = repository.save(dto.toEntity(dto));
		return new InterestDto(savedEntity);
	}

	@Override
	public void delete(InterestDto dto) throws FoundNoMemberException, FoundNoOptionSetException, FoundNoInterestException {
		if(isInterested(dto)) {
		repository.deleteByOptionSetIdAndMemberId(dto.getOptionSetId(),dto.getMemberId());
		}else {
			throw new FoundNoInterestException();
		}
	}

	@Override
	public void deleteAll(Long memberId) throws FoundNoMemberException {
		Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new FoundNoMemberException());
		repository.deleteByMemberId(findMember.getId());		
	}

}
