package org.free;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

public class UI {
    public static StringMTree stringMTree = new StringMTree();

    public static void mainloop() throws IOException {
        // Setup terminal and screen layers
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        // Create panel to hold components
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));
        panel.addComponent(new Label("Add String/URL"));
        final TextBox strAdd = new TextBox().addTo(panel);
        final Label lblOutputAdd = new Label("");
        new Button("Add", new Runnable() {
            @Override
            public void run() {
                String strAddText = strAdd.getText();
                stringMTree.add(strAddText);
                lblOutputAdd.setBackgroundColor(TextColor.ANSI.MAGENTA)
                        .setForegroundColor(TextColor.ANSI.WHITE).setText(strAddText);
            }
        }).addTo(panel);
        panel.addComponent(lblOutputAdd);

        panel.addComponent(new EmptySpace(new TerminalSize(0, 0))); // Empty space underneath labels
        panel.addComponent(new EmptySpace(new TerminalSize(0, 0))); // Empty space underneath labels

        panel.addComponent(new Label("Search String/URL"));
        final TextBox strSearch = new TextBox().addTo(panel);
        final Label lblOutputSearch = new Label("");

        new Button("Search", new Runnable() {
            @Override
            public void run() {
                String strSearchText = strSearch.getText();
                Optional<String> searchRes = stringMTree.query(strSearchText);
                String dipStr = searchRes.orElse("Not found");
                lblOutputSearch.setBackgroundColor(TextColor.ANSI.MAGENTA)
                        .setForegroundColor(TextColor.ANSI.WHITE).setText(dipStr);
            }
        }).addTo(panel);

        panel.addComponent(lblOutputSearch);

        panel.addComponent(new Label("Tolerance"));
        final Label lblOutputTolerance = new Label(Integer.toString(stringMTree.getTolerance()))
            .setBackgroundColor(TextColor.ANSI.MAGENTA).setForegroundColor(TextColor.ANSI.WHITE);
        final TextBox tolerance = new TextBox().setValidationPattern(Pattern.compile("[0-9]*")).addTo(panel);

        new Button("Update", new Runnable() {
            @Override
            public void run() {
                int toleranceValue = Integer.parseInt(tolerance.getText());
                lblOutputTolerance.setBackgroundColor(TextColor.ANSI.MAGENTA)
                    .setForegroundColor(TextColor.ANSI.WHITE).setText(Integer.toString(toleranceValue));
                stringMTree.setTolerance(toleranceValue);
            }
        }).addTo(panel);

        panel.addComponent(lblOutputTolerance);

        panel.addComponent(new Label("File path"));
        final Label lblFileStatus= new Label("")
                .setBackgroundColor(TextColor.ANSI.MAGENTA).setForegroundColor(TextColor.ANSI.WHITE);
        final TextBox filePath = new TextBox().addTo(panel);

        new Button("Read", new Runnable() {
            @Override
            public void run() {
                String path = filePath.getText();
                boolean success = FileIO.read(path, stringMTree);
                String dipStr = success ? "File read!" : "File not found";
                lblFileStatus.setBackgroundColor(TextColor.ANSI.MAGENTA).setForegroundColor(TextColor.ANSI.WHITE)
                        .setText(dipStr);

            }
        }).addTo(panel);

        panel.addComponent(lblFileStatus);

        // Create window to hold the panel
        BasicWindow window = new BasicWindow();
        window.setComponent(panel);

        // Create gui and start gui
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(),
                new EmptySpace(TextColor.ANSI.GREEN));
        gui.addWindowAndWait(window);
    }
}
