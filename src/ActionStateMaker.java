import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import java.io.File;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ActionStateMaker {
    public static ActionState[] analyze(String inputFilePath) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();;
            Document document = documentBuilder.parse(new File(inputFilePath));
            Element rootElement = document.getDocumentElement();
            Element stateMachine = (Element)rootElement.getElementsByTagName("UML:StateMachine.top").item(0);
            NodeList actionStateNodeList = stateMachine.getElementsByTagName("UML:ActionState");
            int numberOfAction = actionStateNodeList.getLength();
            ActionState actionStates[] = new ActionState[numberOfAction];
            for (int i = 0; i < numberOfAction; i++) {
                Element actionStateElement = (Element)actionStateNodeList.item(i);
                String action = actionStateElement.getAttribute("name");
                NodeList outgoingNodeList = actionStateElement.getElementsByTagName("UML:StateVertex.outgoing");
                int numberOfOutgoing = outgoingNodeList.getLength();
                String outgoing[] = new String[numberOfOutgoing];
                for (int j = 0; j < numberOfOutgoing; j++) {
                    Element outgoingElement =  (Element)outgoingNodeList.item(j);
                    Element transition = (Element)outgoingElement.getElementsByTagName("UML:Transition").item(0);
                    outgoing[j] = transition.getAttribute("xmi.idref");
                }
                NodeList incomingNodeList = actionStateElement.getElementsByTagName("UML:StateVertex.incoming");
                int numberOfIncoming = incomingNodeList.getLength();
                String incoming[] = new String[numberOfIncoming];
                for (int j = 0; j < numberOfIncoming; j++) {
                    Element incomingElement =  (Element)incomingNodeList.item(j);
                    Element transition = (Element)incomingElement.getElementsByTagName("UML:Transition").item(0);
                    incoming[j] = transition.getAttribute("xmi.idref");
                }
                actionStates[i] = new ActionState(action, outgoing, incoming);
            }
            return actionStates;
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }
}
