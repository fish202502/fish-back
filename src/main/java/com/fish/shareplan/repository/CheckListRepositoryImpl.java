package com.fish.shareplan.repository;

import com.fish.shareplan.domain.checklist.entity.CheckList;
import com.fish.shareplan.domain.checklist.entity.QCheckList;
import com.fish.shareplan.domain.checklist.entity.QCheckListItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CheckListRepositoryImpl implements CheckListRepositoryCustom {

    private final JPAQueryFactory factory;

    @Override
    public List<CheckList> findAllCheckList(String roomId) {
        return factory.select(QCheckList.checkList)
                .from(QCheckList.checkList)
                .innerJoin(QCheckList.checkList, QCheckListItem.checkListItem.checklist)
                .where(QCheckList.checkList.room.id.eq(roomId))
                .fetch();
    }
}
