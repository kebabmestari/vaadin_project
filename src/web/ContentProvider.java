package web;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.word.WordList;

/**
 * Creates and provides different UI contents to the application
 * Created by samlinz on 3.11.2016.
 */
public class ContentProvider {
    private ContentProvider() {
    }

    public void getLoginPage() {
        LoginPage page = new LoginPage();
        page.init(this);
        UI.getCurrent().setContent(page);
    }

    public void getMainPage() {
        MainPage page = new MainPage(this);
        UI.getCurrent().setContent(page);
    }

    public void openSignUp() {
        SignUpForm signUpForm = new SignUpLogic();
        signUpForm.setSizeFull();
        signUpForm.setMargin(true);

        // Sign up window's styling.
        Window signupWindow = getWindow("Sign up");
        signupWindow.setContent(signUpForm);
        UI.getCurrent().addWindow(signupWindow);
    }

    public void initGame(WordList list) {
        Window gameWindow = getWindow("Playing list " + list.getName());
        gameWindow.setContent(new GameViewLayout(list, this, gameWindow));
        UI.getCurrent().addWindow(gameWindow);
    }

    /**
     * Get a modal window used by the system
     *
     * @return
     */
    public static Window getWindow(String caption) {
        Window result = new Window(caption);
        result.center();
        result.setWidth(300.0f, Sizeable.Unit.PIXELS);
        result.setHeight("200px");
        result.setResizable(false);
        result.setModal(true);
        result.setDraggable(false);
        return result;
    }

    public static ContentProvider getProvider() {
        return new ContentProvider();
    }


}
