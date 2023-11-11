package com.danaga.kakao;

import lombok.Data;

@Data
public class KakaoProfile {

	public Long id;
	public String connected_at;
	public KakaoAccount kakao_account;
	
	// 정보
	@Data
	public class KakaoAccount {

		public Boolean name_needs_agreement;
		public String name;
		public Boolean has_email;
		public Boolean email_needs_agreement;
		public Boolean is_email_valid;
		public Boolean is_email_verified;
		public String email;
		public Boolean has_phone_number;
		public Boolean phone_number_needs_agreement;
		public String phone_number;
		public Boolean has_birthyear;
		public Boolean birthyear_needs_agreement;
		public String birthyear;
		public Boolean has_birthday;
		public Boolean birthday_needs_agreement;
		public String birthday;
		public String birthday_type;

	}
}