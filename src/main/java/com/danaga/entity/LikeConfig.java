package com.danaga.entity;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.danaga.dto.LikeConfigDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "LikeConfig") 
@Table(name = "like_config", uniqueConstraints = {
    @UniqueConstraint(name = "like_config_uq", columnNames = {"board_id", "member_id"})
})
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LikeConfig {
    @Id
    @SequenceGenerator(name = "like_config_id_seq", sequenceName = "like_config_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_config_id_seq") 
    private Long id;

    @ColumnDefault(value = "0")
    private Integer isLike; //1.좋아요누른 상태 0.좋아요 없는상태
    @ColumnDefault(value = "0")
    private Integer disLike; //0.싫어요 없는상태 1.좋아요 누른 상태
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "board_id")
    @ToString.Exclude
    private Board board;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    @ToString.Exclude
    private Member member;

    
    
    public static LikeConfig createConfig(Board board, Member member) {
    	return LikeConfig.builder()
    			.id(0L).isLike(0).disLike(0).board(board).member(member)
    			.build();
    }
    
    public void patch(LikeConfigDto dto) {
    	this.isLike=dto.getIsLike();
    	this.disLike=dto.getDisLike();
    }

}