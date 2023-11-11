package com.danaga.dao.product;

import com.danaga.dto.product.InterestDto;
import com.danaga.exception.product.AlreadyExistsException.ExistsInterestException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoInterestException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;

public interface InterestDao {
	
	Boolean isInterested(InterestDto dto) throws FoundNoMemberException, FoundNoOptionSetException;
	
	InterestDto save(InterestDto dto) throws FoundNoMemberException, FoundNoOptionSetException, ExistsInterestException;
	
	void delete(InterestDto dto) throws FoundNoMemberException, FoundNoOptionSetException, FoundNoInterestException;
	
	void deleteAll(Long memberId) throws FoundNoMemberException;
}
