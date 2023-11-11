package com.danaga.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.ColumnDefault;

import com.danaga.dto.BoardDto;
import com.danaga.dto.LikeConfigDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "Board")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@Data
public class Board extends BaseEntity {
	@Id
	@SequenceGenerator(name = "board_id_seq", sequenceName = "board_id_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable=false)
	private String title;
	@Column(length = 2000,nullable=false)
	private String content;
	private String img1;
	private String img2;
	private String img3;
	private String img4;
	private String img5;
	@ColumnDefault(value = "0")
	@Column(name = "is_like", nullable = false)
	private Integer isLike;
	@ColumnDefault(value = "0")
	@Column(name = "dis_like", nullable = false)
	private Integer disLike;
	@ColumnDefault(value = "0")
	@Column(name = "read_count", nullable = false)
	private Integer readCount;
	@ColumnDefault(value = "1")
	@Column(name = "is_admin", nullable = false)
	private Integer isAdmin;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	@Builder.Default
	private Member member = new Member();

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "board_group_id")
	@ToString.Exclude
	@Builder.Default
	private BoardGroup boardGroup = new BoardGroup();

	@OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@Builder.Default
	@ToString.Exclude
	private List<Comments> comments = new ArrayList<>();

	@OneToMany(mappedBy = "board")
	@Builder.Default
	@ToString.Exclude
	private List<LikeConfig> lConfigs = new ArrayList<>();

	/*
	 * 해당 entity는 BoardGroupConfig의 상태(pk)에따라 게시판의 성향이 달라진다. like는 memberId를 체크하여
	 * 해당게시물에 like를 누른 회원은 두번이상 좋아요가 박히지 않으며 두번클릭시 취소가 된다.
	 * 
	 * pk 분류 1. 자유게시판 2. 1:1문의 3. FAQ 4. 공지
	 * 
	 * like and dislike properties join table with thumbs thumbs_id (thumbs pk)
	 * member_id(member pk)(fk) product_id(product pk)(fk) thumbs_status
	 * 
	 */

	public static Board toEntity(BoardDto dto, Member memberT, BoardGroup boardGroupT) {

		return Board.builder().id(dto.getId()).title(dto.getTitle()).content(dto.getContent()).img1(dto.getImg1()).img2(dto.getImg2())
				.img3(dto.getImg3()).img4(dto.getImg4()).img5(dto.getImg5()).isLike(dto.getIsLike()).isAdmin(dto.getIsAdmin())
				.disLike(dto.getDisLike()).readCount(dto.getReadCount()).member(memberT).boardGroup(boardGroupT)
				.build();
	}

	public void patch(BoardDto dto) {
		
		if (dto.getTitle() != null) {
			this.title = dto.getTitle();
		}
		if (dto.getContent() != null) {
			this.content = dto.getContent();
		}
		if (dto.getImg1() != null) {
			this.img1 = dto.getImg1();
		}
		if (dto.getImg2() != null) {
			this.img2 = dto.getImg2();
		}
		if (dto.getImg3() != null) {
			this.img3 = dto.getImg3();
		}
		if (dto.getImg4() != null) {
			this.img4 = dto.getImg4();
		}
		if (dto.getImg5() != null) {
			this.img5 = dto.getImg5();
		}
	}

	public void updateConfig(LikeConfig config) {
		this.lConfigs.add(config);
	}

	public void readCountUp(Board board) {
		board.readCount++;
	}

}
