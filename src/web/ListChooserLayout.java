package web;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.word.WordList;
import com.word.WordListProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Layout representing page
 * for selecting word lists to start the game
 */
public class ListChooserLayout extends VerticalLayout {

    private Grid listGrid;
    private Collection<WordList> wordList = new ArrayList<>();
    private BeanItemContainer<WordList> beanContainer = new BeanItemContainer<>(WordList.class, wordList);
    private Button chooseButton;
    private Button refreshButton;
    private WordList chosenList;

    private List<WordList> lists;

    private ContentProvider provider;

    public ListChooserLayout(ContentProvider provider) {
        super();

        this.provider = provider;

        chooseButton = new Button("Choose list");
        refreshButton = new Button("Refresh");

        setMargin(true);
        setSpacing(true);

        HorizontalLayout buttonRow = new HorizontalLayout();
        buttonRow.addComponents(chooseButton, refreshButton);
        buttonRow.setMargin(true);
        buttonRow.setSpacing(true);

        listGrid = new Grid();
        listGrid.addColumn("name", String.class);
        listGrid.addColumn("language", String.class);
        listGrid.addColumn("creator", String.class);

        // listen for list selection
        listGrid.addSelectionListener((e) -> {
            try {
                chosenList = lists.get((Integer) e.getSelected().iterator().next() - 1);
                LOG.info("Chose list " + chosenList.getName());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        // start game
        chooseButton.addClickListener((e) -> {
            if (chosenList != null) {
                provider.initGame(chosenList);
            }
        });

        refreshButton.addClickListener((e) -> {
            this.lists.clear();
            listGrid.getContainerDataSource().removeAllItems();
            updateLists();
        });

        updateLists();

        addComponents(buttonRow, listGrid);
    }

    /**
     * Update lists in grid
     */
    public void updateLists() {
        lists = null;
        lists = WordListProvider.getAllLists();
        for (WordList l : lists) {
            listGrid.addRow(l.getName(), l.getWords().get(0).getLang().getName(), l.getCreator().getName());
        }
    }

    private static Logger LOG = Logger.getLogger(ListChooserLayout.class.getName());

}
