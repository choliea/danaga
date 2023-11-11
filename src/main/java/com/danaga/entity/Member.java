package com.danaga.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.danaga.dto.MemberInsertGuestDto;
import com.danaga.dto.MemberResponseDto;
import com.danaga.dto.MemberUpdateDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class Member {

	@Id
	@SequenceGenerator(name = "member_member_no_seq", sequenceName = "member_member_no_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_member_no_seq")
	private Long id;
	@Column(unique = true)
	private String userName;
	private String password;
	@Column(unique = true)
	private String email;
	private String name;
	@Column(unique = true)
	private String nickname;
	private String postCode;
	private String address;
	private String detailAddress;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	@Column(unique = true)
	private String phoneNo;
	@CreationTimestamp
	private LocalDateTime joinDate;
	@Builder.Default
	private String role = "Member";// Member, Guest, Admin
	@Builder.Default
	private String grade = "Rookie"; /* Rookie Bronze, Silver, Gold, Platinum, Diamond 결제 가격의 1%가 등급 포인트로 쌓임
										등급 점수   Rookie : 0 ~ 1000
										Bronze : 1001 ~ 5000
										Silver : 5001 ~ 10000
										Gold : 10001 ~ 20000
										Platinum : 20001 ~ 35000
										Diamond : 35001 ~  */
	@Builder.Default
	private Integer gradePoint = 0;

	public static Member toResponseEntity(MemberResponseDto memberResponseDto) {
		return Member.builder().id(memberResponseDto.getId()).userName(memberResponseDto.getUserName())
				.password(memberResponseDto.getPassword()).email(memberResponseDto.getEmail()).name(memberResponseDto.getName())
				.nickname(memberResponseDto.getNickname()).address(memberResponseDto.getAddress()).phoneNo(memberResponseDto.getPhoneNo())
				.joinDate(memberResponseDto.getJoinDate()).role(memberResponseDto.getRole()).grade(memberResponseDto.getGrade())
				.gradePoint(memberResponseDto.getGradePoint()).build();
	}

	public static Member toUpdateEntity(MemberUpdateDto memberUpdateDto) {
		return Member.builder().id(memberUpdateDto.getId()).password(memberUpdateDto.getPassword())
				.nickname(memberUpdateDto.getNickname()).address(memberUpdateDto.getAddress()).build();
	}

	public static Member toGuestEntity(MemberInsertGuestDto memberInsertGuestDto) {
		return Member.builder().id(memberInsertGuestDto.getId()).name(memberInsertGuestDto.getName())
				.phoneNo(memberInsertGuestDto.getPhoneNo()).userName(memberInsertGuestDto.getUserName())
				.email(memberInsertGuestDto.getEmail()) .role(memberInsertGuestDto.getRole()).build();
	}
	// 관계설정

	// Orders
	@ToString.Exclude
	@OneToMany(mappedBy = "member")
	@Builder.Default
	private List<Orders> orderList = new ArrayList<>();

	// Cart
	@ToString.Exclude
	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@Builder.Default
	private List<Cart> cartList = new ArrayList<>();

	// Interest
	@ToString.Exclude
	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@Builder.Default
	private List<Interest> interestList = new ArrayList<>();

	// RecnetView
	@ToString.Exclude
	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@Builder.Default
	private List<RecentView> recentViewList = new ArrayList<>();

	// Board
	@ToString.Exclude
	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
	@Builder.Default
	private List<Board> boardList = new ArrayList<>();

	// LikeConfig
	@ToString.Exclude
	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
	@Builder.Default
	private List<LikeConfig> lConfigs = new ArrayList<>();
//   // Comment
//   @OneToMany(mappedBy = "member")
//   @Builder.Default
//   private List<Comment> commentList = new ArrayList<>();

	// Review
//   @OneToMany(mappedBy = "member")
//   @Builder.Default
//   private List<Review> reviewList = new ArrayList<>();

// Review
//   @OneToMany(mappedBy = "member")
//   @Builder.Default
//   private List<Coupon> couponList = new ArrayList<>();

}