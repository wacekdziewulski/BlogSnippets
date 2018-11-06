package ovh.devolution;

public class A {
    private String text;
    private Integer number;

    public A(String text, Integer number) {
        this.text = text;
        this.number = number;
    }

    public A(A element) {
        this.text = element.text;
        this.number = element.number;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public Integer getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "A{" + "text='" + text + '\'' + ", number=" + number + '}';
    }
}
