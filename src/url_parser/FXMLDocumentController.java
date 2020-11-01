/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package url_parser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * FXML Controller class
 *
 * @author Jamal
 */
public class FXMLDocumentController implements Initializable {
    @FXML
    private TextField tf_url;
    @FXML
    private JFXButton btn_start;
    @FXML
    private JFXButton btn_reset;
    @FXML
    private Label errorMessage;
    @FXML
    private JFXListView<Node> lv_data;
    @FXML
    private JFXProgressBar progress;
    
    final WebView browser = new WebView();
    final WebEngine webengine = browser.getEngine();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        errorMessage.setText("");
        progress.setProgress(0);
    }    

    @FXML
    private void start(ActionEvent event) {
        if(!tf_url.getText().trim().isEmpty()){
            try{
                webengine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                            Document doc = webengine.getDocument();
                            
                            if(doc!=null){
                                NodeList nl = doc.getElementsByTagName("a");
                                System.out.println(nl.getLength());
                                double progressVal = (double)nl.getLength()/100.0;
                                double tmp = 0;
                                progress.setProgress(tmp);
                                for(int i=0; i<nl.getLength(); i++){
                                    System.out.println("==================");
                                    Element node = (Element) nl.item(i);
                                    addElement(node);
                                    tmp +=progressVal;
                                    progress.setProgress(tmp);
                                }
                                progress.setProgress(0);
                            }else{
                                errorMessage.setText("NULL DOC");
                            }
                        }
                    }
                });
        
                webengine.load(tf_url.getText().trim());
            }catch(Exception ex){
                errorMessage.setText("ERROR");
            }
        }
    }

    @FXML
    private void reset(ActionEvent event) {
        tf_url.clear();
        lv_data.getItems().clear();
        progress.setProgress(0);
    }
    
    public synchronized void addElement(Element element) {
        NamedNodeMap attributes = element.getAttributes();
        System.out.println("Text> "+element.getTextContent());
        
        Attr href = (Attr) attributes.getNamedItem("href");
        String textVal = element.getTextContent();
        String linkVal = href.getNodeValue();
        
        if(!textVal.isEmpty()){
            Label text = new Label("Text: "+textVal);
            text.setStyle(" -fx-font-weight:bold;");
            lv_data.getItems().add(text);
        }
        if(!linkVal.isEmpty()){
            Label link = new Label("Link: "+linkVal);
            link.setStyle("-fx-font-weight:bold;");
            lv_data.getItems().add(link);
        }
        
        lv_data.getItems().add(new Label("---"));
    }
}
