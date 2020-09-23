package crawler;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.stream.IntStream;


public class WebCrawler extends JFrame {

    static DefaultTableModel model;
    static JProgressBar progressBar;
    static JLabel textPages;

    public WebCrawler() {

        Color background = new Color(59, 59, 59);
        Color green = new Color(100, 200, 100);
        Color darkGreen = new Color(30, 120, 20);

        setTitle("Web Crawler");
        setForeground(darkGreen);
        setBackground(green);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);


        JLabel title = new JLabel();
        title.setName("TitleLabel");

        /** First line **/
        JLabel textURL = new JLabel("Start URL: ");
        textURL.setForeground(darkGreen);

        JTextField textFieldURL = new JTextField();
        textFieldURL.setName("UrlTextField");
        textFieldURL.setBorder(new LineBorder(green));
        textFieldURL.setForeground(green);
        textFieldURL.setForeground(darkGreen);

        JToggleButton jButtonRun = new JToggleButton("Run");
        jButtonRun.setName("RunButton");
        jButtonRun.setBackground(green);

        JPanel textFieldAndButtonStartURL = new JPanel();
        textFieldAndButtonStartURL.setLayout(new BoxLayout(textFieldAndButtonStartURL, BoxLayout.X_AXIS));
        textFieldAndButtonStartURL.add(textURL);
        textFieldAndButtonStartURL.add(textFieldURL);
        textFieldAndButtonStartURL.add(jButtonRun);


        /** Second line **/
        JLabel textWorkers = new JLabel("Workers: ");
        textWorkers.setForeground(darkGreen);

        JTextField textFieldWorkers = new JTextField();
        textFieldWorkers.setName("DepthTextField");
        textFieldWorkers.setForeground(darkGreen);

        JPanel labelAndTextFieldWorkers = new JPanel();
        labelAndTextFieldWorkers.setLayout(new BoxLayout(labelAndTextFieldWorkers, BoxLayout.X_AXIS));
        labelAndTextFieldWorkers.add(textWorkers);
        labelAndTextFieldWorkers.add(textFieldWorkers);


        /** Third line **/
        JLabel textMaxDepth = new JLabel("Maximum depth: ");
        textMaxDepth.setForeground(darkGreen);

        JTextField textFieldMaxDepth = new JTextField();
        textFieldWorkers.setName("DepthTextField");
        textFieldWorkers.setForeground(darkGreen);

        JCheckBox checkBoxDepth = new JCheckBox("Enabled");
        checkBoxDepth.setName("DepthCheckBox");
        checkBoxDepth.setForeground(green);

        JPanel labelAndTextFieldMaxDepth = new JPanel();
        labelAndTextFieldMaxDepth.setLayout(new BoxLayout(labelAndTextFieldMaxDepth, BoxLayout.X_AXIS));
        labelAndTextFieldMaxDepth.add(textMaxDepth);
        labelAndTextFieldMaxDepth.add(textFieldMaxDepth);
        labelAndTextFieldMaxDepth.add(checkBoxDepth);


        /** Fourth line - time limit **/
        JLabel textTimeLimit = new JLabel("Time limit: ");
        textTimeLimit.setForeground(darkGreen);

        JTextField textFieldTimeLimit = new JTextField();
        textFieldTimeLimit.setForeground(darkGreen);

        JLabel seconds = new JLabel(" seconds ");
        seconds.setForeground(darkGreen);

        JCheckBox checkBoxTimeLimit = new JCheckBox("Enabled");
        checkBoxTimeLimit.setForeground(green);

        JPanel labelAndTextFieldTimeLimit = new JPanel();
        labelAndTextFieldTimeLimit.setLayout(new BoxLayout(labelAndTextFieldTimeLimit, BoxLayout.X_AXIS));
        labelAndTextFieldTimeLimit.add(textTimeLimit);
        labelAndTextFieldTimeLimit.add(textFieldTimeLimit);
        labelAndTextFieldTimeLimit.add(seconds);
        labelAndTextFieldTimeLimit.add(checkBoxTimeLimit);


        /** Fifth line - elapsed time **/
        JLabel textElapsedTime = new JLabel("Elapsed time: ");
        textElapsedTime.setForeground(darkGreen);

        JLabel textTime = new JLabel("0:00");
        textTime.setForeground(darkGreen);

        JPanel labelElapsedTime = new JPanel();
        labelElapsedTime.setLayout(new BoxLayout(labelElapsedTime, BoxLayout.X_AXIS));
        labelElapsedTime.add(textElapsedTime);
        labelElapsedTime.add(textTime);


        /** Sixth line - parsed pages **/
        JLabel textParsedPages = new JLabel("Parsed pages: ");
        textParsedPages.setForeground(darkGreen);
        //textParsedPages.setName("ParsedLabel");

        textPages = new JLabel();
        textPages.setForeground(darkGreen);
        textPages.setName("ParsedLabel");

        JPanel labelParsedPages = new JPanel();
        labelParsedPages.setLayout(new BoxLayout(labelParsedPages, BoxLayout.X_AXIS));
        labelParsedPages.add(textParsedPages);
        labelParsedPages.add(textPages);


        /** Seventh line - EXPORT **/

        JLabel textExport = new JLabel("Export: ");
        textExport.setForeground(darkGreen);

        JTextField jTextFieldExport = new JTextField();
        jTextFieldExport.setName("ExportUrlTextField");
        jTextFieldExport.setForeground(darkGreen);

        JButton jButtonSave = new JButton("Save");
        jButtonSave.setName("ExportButton");
        jButtonSave.setBackground(green);

        JPanel exportPanel = new JPanel();
        exportPanel.setLayout(new BoxLayout(exportPanel, BoxLayout.X_AXIS));
        exportPanel.add(textExport);
        exportPanel.add(jTextFieldExport);
        exportPanel.add(jButtonSave);

        jButtonSave.addActionListener(actionEvent -> {
            String path = jTextFieldExport.getText();
            File file = new File(path);
            try  (PrintWriter printWriter = new PrintWriter(file)) {

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        printWriter.println(model.getValueAt(i, j));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        jButtonRun.addActionListener(actionEvent -> {
            String url = textFieldURL.getText();
            int noOfWorkers = textFieldWorkers.getText().isEmpty() ?
                    1 : Integer.parseInt(textFieldWorkers.getText());
            //int maximumDepth = textFieldMaxDepth.g

            String titleString = MainTitle.getMainTitle(url);

            model.setRowCount(0);
            model.addRow(new Object[]{url, titleString});

            title.setText(titleString);

            SecundaryLinksAndTitles secundaryLinksAndTitles = new SecundaryLinksAndTitles(url);
            secundaryLinksAndTitles.execute();

        });

        JLabel textTitle = new JLabel("Title: ");
        textTitle.setForeground(darkGreen);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(textTitle);
        titlePanel.add(title);

        model = new DefaultTableModel(0, 2);
        model.setColumnIdentifiers(new String[]{"URL", "Title"});

        JTable jTable = new JTable(model);
        jTable.setName("TitlesTable");
        jTable.setFillsViewportHeight(true);
        jTable.setEnabled(false);
        jTable.setForeground(darkGreen);
        JScrollPane jScrollPane = new JScrollPane(jTable);
        jScrollPane.setBackground(green);


        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBackground(Color.white);
        progressBar.setForeground(green);
        progressBar.setBorder(new LineBorder(darkGreen));

        JPanel jPanel = new JPanel();
        GroupLayout layout = new GroupLayout(jPanel);
        jPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(textFieldAndButtonStartURL)
                        .addComponent(labelAndTextFieldWorkers)
                        .addComponent(labelAndTextFieldMaxDepth)
                        .addComponent(labelAndTextFieldTimeLimit)
                        .addComponent(labelElapsedTime)
                        .addComponent(labelParsedPages)
                        .addComponent(exportPanel)
                        .addComponent(titlePanel)
                        .addComponent(jScrollPane)
                        .addComponent(progressBar))
        );


        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldAndButtonStartURL, 30, 30, 30))
                .addComponent(labelAndTextFieldWorkers, 30, 30, 30)
                .addComponent(labelAndTextFieldMaxDepth, 30, 30, 30)
                .addComponent(labelAndTextFieldTimeLimit, 30, 30, 30)
                .addComponent(labelElapsedTime, 30, 30, 30)
                .addComponent(labelParsedPages, 30, 30, 30)
                .addComponent(exportPanel, 30, 30, 30)
                .addComponent(titlePanel, 30, 30, 30)
                .addComponent(jScrollPane, 30, 100, 650)
                .addComponent(progressBar, 30, 30, 30)

        );

        add(jPanel);

        setVisible(true);
    }
}