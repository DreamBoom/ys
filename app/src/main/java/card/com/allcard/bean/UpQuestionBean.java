package card.com.allcard.bean;

public class UpQuestionBean {

    /**
     * id : 密保问题id
     * answer : 密保答案
     */

    private String id;
    private String question;
    private String answer;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
