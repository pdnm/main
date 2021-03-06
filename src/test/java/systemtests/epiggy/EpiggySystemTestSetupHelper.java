package systemtests.epiggy;

import java.nio.file.Path;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import org.testfx.api.FxToolkit;

import guitests.guihandles.MainWindowHandle;
import javafx.stage.Stage;
import seedu.address.EpiggyTestApp;
import seedu.address.model.ReadOnlyEPiggy;

/**
 * Contains helper methods that system tests require.
 */
//@@author yunjun199321
public class EpiggySystemTestSetupHelper {
    private EpiggyTestApp testApp;
    private MainWindowHandle mainWindowHandle;

    /**
     * Sets up a new {@code EpiggyTestApp} and returns it.
     */
    public EpiggyTestApp setupApplication(Supplier<ReadOnlyEPiggy> ePiggy, Path saveFileLocation) {
        try {
            FxToolkit.registerStage(Stage::new);
            FxToolkit.setupApplication(() -> testApp = new EpiggyTestApp(ePiggy, saveFileLocation));
        } catch (TimeoutException te) {
            throw new AssertionError("Application takes too long to set up.", te);
        }

        return testApp;
    }

    /**
     * Initializes TestFX.
     */
    public static void initialize() {
        try {
            FxToolkit.registerPrimaryStage();
            FxToolkit.hideStage();
        } catch (TimeoutException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Encapsulates the primary stage of {@code TestApp} in a {@code MainWindowHandle} and returns it.
     */
    public MainWindowHandle setupMainWindowHandle() {
        try {
            FxToolkit.setupStage((stage) -> {
                mainWindowHandle = new MainWindowHandle(stage);
                mainWindowHandle.focus();
            });
            FxToolkit.showStage();
        } catch (TimeoutException te) {
            throw new AssertionError("Stage takes too long to set up.", te);
        }

        return mainWindowHandle;
    }

    /**
     * Tears down existing stages.
     */
    public void tearDownStage() {
        try {
            FxToolkit.cleanupStages();
        } catch (TimeoutException te) {
            throw new AssertionError("Stage takes too long to tear down.", te);
        }
    }
}

