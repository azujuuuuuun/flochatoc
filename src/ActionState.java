public class ActionState {
    String action;
    String outgoing[];
    String incoming[];
    boolean isOrdered;

    public ActionState(String action, String outgoing[], String incoming[]) {
        this.action = action;
        this.outgoing = outgoing;
        this.incoming = incoming;
        this.isOrdered = false;
    }
}