package com.borymskyi.entity.enums;

/**
 * Хранит доступное состояние пользователей. В зависимости от состояния пользователя, мы будем
 * ожидать определенные действия от пользователя.
 *
 * @author Dmitrii Borymskyi
 * @version 1.0
 */
public enum UserState {
    BASIC_STATE,
    WAIT_FOR_EMAIL_STATE //приложение будет считать валидным только ввод мейла.
}
