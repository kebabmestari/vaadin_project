package web;

import com.game.*;
import com.user.User;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.word.Word;
import com.word.WordList;
import com.word.WordListProvider;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * Thread object for tracking time left
 * to answer and call timeout
 */
class TimeTracker extends Thread {

    private boolean running;
    private final int time;
    private long startTime;
    private GameViewLayout cb;
    private Logger LOG;
    private long lastTick;

    public void stopRunning() {
        running = false;
    }

    public TimeTracker(GameViewLayout callback, int timeleft) {
        this.time = timeleft * 1000;
        resetTimer();
        cb = callback;
    }

    public void resetTimer() {
        this.running = true;
        this.startTime = this.lastTick = System.currentTimeMillis();
        this.LOG = Logger.getLogger(TimeTracker.class.getName());
        LOG.info("Initialized TimeTracker with maxtime " + this.time + " and starttime " + this.startTime);
    }

    public void closeTimer() {
        running = false;
    }

    @Override
    public void run() {
        AtomicLong timer = new AtomicLong(0);
        try {
            while (running) {
                timer.set(System.currentTimeMillis() - startTime);
                if (timer.get() >= time) {
                    System.out.println("TIMEOUT");
                    UI.getCurrent().access(()-> {
                        cb.timeOut();
                        UI.getCurrent().push();
                    });
                }
                if(System.currentTimeMillis() - this.lastTick > 1000) {
                    this.lastTick = System.currentTimeMillis();
                    UI.getCurrent().access(()-> {
                        cb.setTimeBar((float) timer.get() / (float) time);
                        UI.getCurrent().push();
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("KILLING THREAD ");
    }
}

/**
 * Created by samlinz on 4.11.2016.
 */
public class GameViewLayout extends GameView {
    private RoundInstance round;
    private Word currentWord;
    private boolean gameOn;
    private ContentProvider provider;
    private TimeTracker timer;
    private Window thiswindow;
    private int number;

    public static int TIME = 10;

    public GameViewLayout(WordList list, ContentProvider provider, Window thiswindow) {
        super();

        this.provider = provider;
        this.thiswindow = thiswindow;

        this.number = 0;

        this.timeleft.setValue(1.f);

        round = WordListProvider.getInstance(list);

        gameOn = true;
        getNext();

        submitButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        submitButton.addClickListener((e) -> {
            if (gameOn) {
                String inputWord = ((String) answerInput.getValue()).toLowerCase();
                if (inputWord.length() > 0) {
                    if (round.guessWord(inputWord, currentWord)) {
                        Notification.show("Correct answer", Notification.Type.TRAY_NOTIFICATION);
                        round.addScore();
                    } else {
                        Notification.show("Wrong answer", Notification.Type.TRAY_NOTIFICATION);
                    }
                    getNext();
                }
            }
        });
    }

    /**
     * Time out when time runs out
     */
    public void timeOut() {
        if (gameOn) {
            Notification.show("Time out", Notification.Type.TRAY_NOTIFICATION);
            getNext();
        } else {
            timer.closeTimer();
        }
    }

    public void setTimeBar(float val) {
        this.timeleft.setValue(val);
    }

    /**
     * Get next word
     */
    private void getNext() {

        if (timer != null) {
            timer.resetTimer();
        } else {
            timer = new TimeTracker(this, TIME);
            timer.start();
        }

        if (!gameOn) {
            if(timer != null) {
                timer.closeTimer();
            }
            return;
        }

        // fetch next word
        try {
            currentWord = round.getNextWord();
            wordLabel.setValue(currentWord.getWord().toUpperCase());
            this.number++;
            numberLabel.setValue(this.number + " / " + round.getMasterList().getMaxScore());
        } catch (RoundOver roundOver) {
            Notification.show(
                    "Round over", "score " + round.getScore() + "/" + round.getMasterList().getMaxScore(),
                    Notification.Type.ERROR_MESSAGE
            );
            createResult();
            provider.getMainPage();
        }
    }

    /**
     * Create result object from the round
     * and flush it to db
     */
    private void createResult() {
        gameOn = false;
        if (round == null) {
            return;
        }
        if (timer != null) {
            timer.interrupt();
        }
        User user = ((MyUI) UI.getCurrent()).getUser().getUser();
        Result newResult = ResultFactory.createNewResult(
                user,
                round.getMasterList(),
                round.getScore()
        );
        LOG.info("Created result " + newResult.getUser().getName() + "-" + newResult.getList().getName() + "-" + newResult.getDate().toString());
        ResultProvider.flush(newResult);
        user.addResult(newResult);
        thiswindow.close();
    }

    private static Logger LOG = Logger.getLogger(GameViewLayout.class.getName());
}
