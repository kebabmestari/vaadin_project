package web;

import com.database.Query;
import com.database.QueryRunner;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import com.word.*;
import com.word.lang.Language;
import com.word.lang.LanguageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by samlinz on 5.11.2016.
 */
public class CreateListLayout extends CreateList {

    private List<String> words = new ArrayList<>();
    private Language selected;

    public CreateListLayout() {
        super();
        createWordButton.addClickListener((e) -> {
            Window wordWindow = ContentProvider.getWindow("Create new word");
            wordWindow.setContent((CreateWord) new CreateWordLayout(wordWindow));
            wordWindow.setSizeFull();
            UI.getCurrent().addWindow(wordWindow);
        });
        addButton.addClickListener((e) -> {
            addWord();
        });
        saveButton.addClickListener((e) -> {
            saveList();
        });
        resetButton.addClickListener((e) -> {
            resetList();
        });
        removeButton.addClickListener((e) -> {
            for (Object o : wordGrid.getSelectedRows()) {
                String wordName = (String) o;
                words.stream().forEach((w) -> {
                    if (w.equals(wordName)) {
                        words.remove(w);
                    }
                });
            }
            updateGrid();
            wordGrid.getSelectionModel().reset();
        });
        languageSelect.setNullSelectionAllowed(false);
        languageSelect.addItems(LanguageProvider.getAllLanguages()
                .stream().map((e) -> {
                    return e.getName();
                }).collect(Collectors.toList()));
        languageSelect.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (words.size() > 0) {
                    resetList();
                }
                selected = (Language) LanguageProvider.getLanguage((String) languageSelect.getValue());
            }
        });
        updateGrid();
    }

    /**
     * Update grid element to show all words
     */
    public void updateGrid() {
        wordGrid.getContainerDataSource().removeAllItems();
        for (String s : words) {
            wordGrid.addRow(s);
        }
    }

    public void resetList() {
        words.clear();
        updateGrid();
    }

    public void addWord() {
        if (languageSelect.getValue() == null) {
            Notification.show("Select list language first", Notification.Type.WARNING_MESSAGE);
            return;
        }
        Window wordWindow = ContentProvider.getWindow("Add word to list");
        HorizontalLayout l = new HorizontalLayout();
        VerticalLayout v = new VerticalLayout();
        TextField addWordField = new TextField();
        Button addWord = new Button("Add word");
        l.setSpacing(true);
        Label suggestionLabel = new Label("...");
        v.addComponents(addWordField, suggestionLabel);
        l.addComponents(v, addWord);
        l.setSizeFull();
        l.setMargin(true);
        wordWindow.setContent(l);
        UI.getCurrent().addWindow(wordWindow);

        addWordField.addTextChangeListener((e) -> {
            String word = (String) e.getText();
            QueryRunner qr = QueryRunner.getRunner(Query.SEARCH_WORDS);
            System.out.println(word + "%");
            qr.setParam(word + "%");
            qr.setParam(selected.getId());
            qr.run();
            final List<String> word1 = qr.getResults(String.class, "word");
            final List<String> lang1 = qr.getResults(String.class, "lang");
            if (word1 == null || word1.isEmpty() || word.length() == 0) {
                suggestionLabel.setValue("...");
            } else {
                suggestionLabel.setValue(word1.get(0) + " - " + lang1.get(0));
            }
/*            if(WordProvider.wordExists(word)) {
                Notification.show("Word " + word + " exists in database!");
            }*/
        });
        addWord.addClickListener((e) -> {
            String word = (String) addWordField.getValue();
            if (WordProvider.wordExists(word)) {
                words.add(word);
                updateGrid();
                wordWindow.close();
            } else {
                Notification.show("Word does not exist");
            }
        });
    }

    /**
     * Create actual WordList object and flush it to db
     */
    public void saveList() {
        String name = (String) listnameField.getValue();
        if (languageSelect.getValue() == "") {
            Notification.show("Select list language", Notification.Type.WARNING_MESSAGE);
            return;
        }
        // must have name
        if (name == null) {
            return;
        }
        // no empty lists
        if (words.size() == 0) {
            return;
        }
        if (WordListProvider.listExists(name)) {
            Notification.show("List called " + name + " exists already!");
            return;
        }
        List<Word> wordObj = words
                .stream()
                .map((w) -> {
                    return WordProvider.getWord(w);
                })
                .collect(Collectors.toList());
        LOG.info("Creating new word list");
        WordList newList = WordListFactory.getWordList(
                WordListProvider.getNextId(),
                name,
                ((MyUI) UI.getCurrent()).getUser().getUser(),
                10,
                wordObj
        );
        WordListProvider.flushToDatabase(newList);
        Notification.show("Word list created");
        resetList();
        LOG.info("List " + name + " created and flushed to db");
    }

    private static Logger LOG = Logger.getLogger(CreateListLayout.class.getName());
}
