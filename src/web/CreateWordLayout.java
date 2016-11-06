package web;

/**
 * Created by samlinz on 3.11.2016.
 */

import com.vaadin.server.UserError;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import com.word.Word;
import com.word.WordFactory;
import com.word.WordProvider;
import com.word.lang.Language;
import com.word.lang.LanguageProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * !! DO NOT EDIT THIS FILE !!
 * <p>
 * This class is generated by Vaadin Designer and will be overwritten.
 * <p>
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
public class CreateWordLayout extends CreateWord {

    private Set<Word> explanations = new HashSet<>();
    private Language language;
    private Window window;

    public CreateWordLayout(Window window) {

        this.window = window;

        language = LanguageProvider.getLanguage(1);

        wordField.addTextChangeListener((e) -> {
            checkWordExists();
        });
        explanationButton.addClickListener((e) -> {
            String word = explanationField.getValue().toLowerCase();
            if (word != null && word.length() > 0) {
                addExplanation(word);
            }
        });
        saveButton.addClickListener((e) -> {
            String word = wordField.getValue();
            if (word != null && word.length() > 0) {
                saveWord();
            }
        });
        languageSelect.setNullSelectionAllowed(false);
        languageSelect.addItems(LanguageProvider.getAllLanguages()
                .stream().map((e) -> {
                    return e.getName();
                }).collect(Collectors.toList()));
    }

    /**
     * Check if inputted word exists already
     *
     * @return true if word exists
     */
    public boolean checkWordExists() {
        boolean result = false;
        String word = wordField.getValue().toLowerCase();
        if (word.length() > 0) {
            if (WordProvider.wordExists(word)) {
                Notification.show("Word exists already!", Notification.Type.WARNING_MESSAGE);
                result = true;
            }
        }
        return result;
    }

    /**
     * Save new word to database
     */
    public void saveWord() {
        if (checkWordExists()) {
            return;
        }
        if (languageSelect.getValue() == null) {
            Notification.show("Select language", Notification.Type.WARNING_MESSAGE);
            return;
        }
        String word = wordField.getValue().toLowerCase();
        Language lang = LanguageProvider.getLanguage((String) languageSelect.getValue());
        if (word.length() > 0) {
            Word newWord = WordFactory.getWord(
                    WordProvider.getNextId(),
                    word,
                    LanguageProvider.getLanguage((String) languageSelect.getValue()),
                    explanations.isEmpty() ? null : explanations
            );
            WordProvider.flush(newWord);
            Word testNewWord = WordProvider.getWord(newWord.getId());
            if (testNewWord != null && testNewWord.getWord().equals(newWord.getWord())) {
                Notification.show("Word " + word + " added");
                window.close();
            }
        }
    }

    public void addExplanation(String str) {
        str = str.toLowerCase();
        Word word = WordProvider.getWord(str);
        if (word == null) {
            explanationButton.setComponentError(new UserError("Word not found"));
            return;
        }
        explanations.add(word);
        explanationGrid.addRow(str);
    }
}
