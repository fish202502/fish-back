package com.fish.shareplan.enums;

public enum ChangeType {
    ALL,ROOM,WRITE,READ;

    @Override
    public String toString() {
        return name().toLowerCase(); // enum 이름을 소문자로 변환
    }

    public static boolean isValidType(String type) {
        for (ChangeType changeType : ChangeType.values()) {
            if (changeType.toString().equals(type.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
