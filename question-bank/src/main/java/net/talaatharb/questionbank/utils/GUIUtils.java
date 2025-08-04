package net.talaatharb.questionbank.utils;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GUIUtils {

    public static final void setAnchorAllDirections(Parent root, Double offset) {
        AnchorPane.setBottomAnchor(root, offset);
        AnchorPane.setLeftAnchor(root, offset);
        AnchorPane.setRightAnchor(root, offset);
        AnchorPane.setTopAnchor(root, offset);
    }
    
    public static final void setAnchorZero(Parent root) {
        setAnchorAllDirections(root, 0.0);
    }
}
