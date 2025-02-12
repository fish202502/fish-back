package com.fish.shareplan.repository;

import com.fish.shareplan.domain.expense.dto.response.ExpenseItemDto;
import com.fish.shareplan.domain.expense.dto.response.ExpenseResponseDto;
import com.fish.shareplan.domain.expense.dto.response.ReceiptDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fish.shareplan.domain.expense.entity.QExpense.expense;
import static com.fish.shareplan.domain.expense.entity.QExpenseItem.expenseItem;
import static com.fish.shareplan.domain.expense.entity.QReceiptImage.receiptImage;

@RequiredArgsConstructor
public class ExpenseRepositoryImpl implements ExpenseRepositoryCustom {

    private final JPAQueryFactory factory;

    @Override
    public List<ExpenseResponseDto> findAllExpense(String roomId) {

        List<Tuple> tupleList = factory
                .select(expense.id,
                        expenseItem.id,
                        expenseItem.spender,
                        expenseItem.amount,
                        expenseItem.description,
                        expenseItem.spentAt,
                        receiptImage.id,
                        receiptImage.imageUrl)
                .from(expense)
                .innerJoin(expenseItem).on(expense.id.eq(expenseItem.expense.id))
                .leftJoin(receiptImage).on(expenseItem.id.eq(receiptImage.expenseItem.id))
                .where(expense.room.id.eq(roomId))
                .fetch();

        // 데이터 변환 로직
        Map<String, ExpenseResponseDto> expenseMap = new HashMap<>();
        Map<String, ExpenseItemDto> expenseItemMap = new HashMap<>();

//  [
//    {
//        "expenseId": "",
//        "expenseItemDtoList": [
//            {
//                "expenseItemId": "",
//                "spender": "",
//                "amount": 0.0,
//                "description": "",
//                "spendAt": "",
//                "receiptList": [
//                    {
//                        "receiptId": "",
//                        "url": ""
//                    },
//                    {
//                        "receiptId": "",
//                        "url": ""
//                    }
//                ]
//            }
//        ]
//    }
//    ]

        tupleList.forEach(tuple -> {
            String expenseId = tuple.get(expense.id);
            String itemId = tuple.get(expenseItem.id);
            String receiptId = tuple.get(receiptImage.id);

            //  ReceiptImageDto 만들기 (receiptList 부분)
            ReceiptDto receiptDto = null;
            String url = tuple.get(receiptImage.imageUrl);
            if (url != null) {
                receiptDto = new ReceiptDto(receiptId, url);
            }

            // ExpenseItemDto 만들기 (expenseItemDtoList 부분)
            ExpenseItemDto itemDto = expenseItemMap.get(itemId);
            if (itemDto == null) {
                itemDto = new ExpenseItemDto(
                        itemId,
                        tuple.get(expenseItem.spender),
                        tuple.get(expenseItem.amount),
                        tuple.get(expenseItem.description),
                        tuple.get(expenseItem.spentAt),
                        new ArrayList<>()
                );
                expenseItemMap.put(itemId, itemDto);
            }
            //expenseItemDtoList 에 ReceiptImageDto 추가
            // 영수증사진 있을때만 추가함
            if (url != null) {
                itemDto.getReceiptList().add(receiptDto);
            }

            // ExpenseDto 만들기
            ExpenseResponseDto expenseDto = expenseMap.get(expenseId);
            if (expenseDto == null) {
                expenseDto = new ExpenseResponseDto(expenseId, new ArrayList<>());
                expenseMap.put(expenseId, expenseDto);
            }
            if (!expenseDto.getExpenseItemList().contains(itemDto)) {
                expenseDto.getExpenseItemList().add(itemDto);
            }
        });
        List<ExpenseResponseDto> expenses = new ArrayList<>(expenseMap.values());
        return expenses;
    }
}
