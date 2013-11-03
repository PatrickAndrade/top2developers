package communicationActivity;


public class ArrayAdapterItem {

    private String mAnswer;

    public ArrayAdapterItem(String item) {
        mAnswer = item;
    }
    
    /**
     * returns the answer
     * @return mAnswer
     */
    public String getAnswer() {
        return mAnswer;
    }

    /**
     * set a new answer
     * @param answer
     */
    public void setAnswer(String answer) {
        mAnswer = answer;
    }

}