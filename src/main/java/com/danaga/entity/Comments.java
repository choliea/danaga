package com.danaga.entity;

import java.util.ArrayList;
import java.util.List;

import com.danaga.dto.CommentDto;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Builder
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comments extends BaseEntity {

  @Id
  @SequenceGenerator(name = "comment_id_seq", sequenceName = "comment_id_seq", initialValue = 1, allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String writer;

  private String pw;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id", nullable = false)
  private Board board;

  @Column(nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id", nullable = true) // nullable 속성을 true로 변경
  private Comments parent;

  @Builder.Default
  @OneToMany(mappedBy = "parent", orphanRemoval = true,cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
  private List<Comments> children = new ArrayList<>();

  public void update(CommentDto commentRequestDto) {
    this.content = commentRequestDto.getContent();
  }

  // 부모 댓글 수정
  public void updateParent(Comments parent){
    this.parent = parent;
  }

  public static Comments toEntity(CommentDto comment, Board board, Comments parent) {
	    if (comment.getParentId() == null || comment.getParentId()==0L) {
	        return Comments.builder()
	                .id(comment.getId())
	                .writer(comment.getWriter())
	                .pw(comment.getPw())
	                .board(board)
	                .content(comment.getContent())
	                .build();
	    }else {

		    return Comments.builder()
		            .id(comment.getId())
		            .writer(comment.getWriter())
		            .pw(comment.getPw())
		            .board(board)
		            .content(comment.getContent())
		            .parent(parent)
		            .build();
		}
	}



 
}
