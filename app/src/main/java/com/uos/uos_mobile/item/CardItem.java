package com.uos.uos_mobile.item;

/**
 * 카드정보를 담고 있는 클래스. <br><br>
 * 카드의 필수 정보인 카드번호, CVC, 만료날짜에 대한 데이터를 저장해놓을 수 있는 클래스입니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class CardItem {
    private String num;
    private String cvc;
    private String dueDate;

    /**
     * CardItem의 기본 생성자.
     */
    public CardItem() {

    }

    /**
     * CardItem의 명시적 생성자. <br><br>
     *
     * @param num 카드번호
     * @param cvc CVC
     * @param dueDate 만료날짜
     */
    public CardItem(String num, String cvc, String dueDate) {
        this.num = num;
        this.cvc = cvc;
        this.dueDate = dueDate;
    }

    /**
     * 객체에 등록된 모든 카드 정보를 초기화하는 함수.
     */
    public void clear() {
        num = "";
        cvc = "";
        dueDate = "";
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
