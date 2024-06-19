module cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.hivemq.client.mqtt;
    requires junit;


    opens cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka to javafx.fxml;
    exports cz.spsmb.sec.onlineplosinovaka.onlineplosinovaka;
}