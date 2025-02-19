package com.fish.shareplan.domain.checklist.entity;

import com.fish.shareplan.domain.checklist.dto.reponse.CategoryResponseDto;
import com.fish.shareplan.domain.checklist.dto.request.CategoryRequestDto;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString (exclude = {"checkListItemList", "checkList"})
@Builder
@Entity
@Table(name = "tbl_checklist_category")
public class CheckListCategory {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36) DEFAULT UUID()")
    private final String id = UUID.randomUUID().toString();

    @Builder.Default
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CheckListItem> checkListItemList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id", nullable = false)
    private CheckList checkList;

    @Column(name = "content")
    private String content;

    public static CategoryResponseDto toDto(CheckListCategory category) {
        return CategoryResponseDto.builder()
                .content(category.getContent())
                .categoryId(category.getId())
                .build();
    }
    public void update(CategoryRequestDto dto){
        this.content = dto.getCategory();
    }
}
