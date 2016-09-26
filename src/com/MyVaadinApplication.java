package com;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

/**
 * Created by adminpc on 26/9/2016.
 */
public class MyVaadinApplication extends UI {
    @Override
    public void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        setContent(layout);
        layout.addComponent(new Label("Moi"));
    }
}
