/**
 *
 */
module starter {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    //requires javafx.animation;
    opens starter to javafx.fxml;
    exports starter;
}