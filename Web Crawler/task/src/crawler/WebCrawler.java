package crawler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebCrawler extends JFrame {

    enum LinkType {
        Absolute,
        Relative,
        WithoutProtocol
    }

    DefaultTableModel model;

    public WebCrawler() {

        setTitle("WebCrawler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);


        JLabel textURL = new JLabel("URL: ");

        JTextField jTextField = new JTextField();
        jTextField.setName("UrlTextField");

        JButton jButton = new JButton("Parse");
        jButton.setName("RunButton");


        JTextArea jTextArea = new JTextArea();

        JLabel textTitle = new JLabel("Title: ");

        JLabel title = new JLabel();
        title.setName("TitleLabel");

        jButton.addActionListener(actionEvent -> {
            String url = jTextField.getText();
            String titleString = getMainTitle(url);
            title.setText(titleString);

            model.setRowCount(0);
            model.addRow(new Object[]{url, titleString});
            getLinkAndTitle(url);

        });



        JLabel textExport = new JLabel("Export: ");

        JTextField jTextFieldExport = new JTextField();
        jTextFieldExport.setName("ExportUrlTextField");


        JButton jButtonSave = new JButton("Parse");
        jButtonSave.setName("ExportButton");

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


        JPanel textFieldAndButton = new JPanel();
        textFieldAndButton.setLayout(new BoxLayout(textFieldAndButton, BoxLayout.X_AXIS));
        textFieldAndButton.add(textURL);
        textFieldAndButton.add(jTextField);
        textFieldAndButton.add(jButton);


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
        JScrollPane jScrollPane = new JScrollPane(jTable);


        JPanel exportPanel = new JPanel();
        exportPanel.setLayout(new BoxLayout(exportPanel, BoxLayout.X_AXIS));
        exportPanel.add(textExport);
        exportPanel.add(jTextFieldExport);
        exportPanel.add(jButtonSave);


        JPanel jPanel = new JPanel();
        GroupLayout layout = new GroupLayout(jPanel);
        jPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(textFieldAndButton, 100, 500, 950)
                .addComponent(titlePanel, 100, 500, 950)
                .addComponent(jScrollPane, 100, 500, 950)
                .addComponent(jTextArea, 0, 0, 0)
                .addComponent(exportPanel, 100, 500, 950))
        );


        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(textFieldAndButton, 30, 30, 30))
                .addComponent(titlePanel, 30, 30, 30)
                .addComponent(jScrollPane, 100, 300, 650)
                .addComponent(jTextArea, 0, 0, 0)
                .addComponent(exportPanel, 30, 30, 30)
        );

        add(jPanel);
    }


    public String getMainTitle(String url) {

        Pattern javaPattern = Pattern.compile("(<title>)(.*)(</title>)");

        try (InputStream inputStream = new BufferedInputStream(new URL(url).openStream())) {
            String siteText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            Matcher matcher = javaPattern.matcher(siteText);

            if (matcher.find()) {
                return matcher.group(2);
            }

        } catch (Exception ignored) { }

        return "";

    }

    public void getLinkAndTitle(String url) {

        String link;
        String title;

        try {
            URLConnection connection = new URL(url).openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String siteText;

            while ((siteText = reader.readLine()) != null) {

                Matcher matcherLink = Pattern.compile("(?<=href=[\"']).*(?=[\"']>)").matcher(siteText);

                if (matcherLink.find()) {

                    String initialLink = matcherLink.group();

                    LinkType linkType = getLinkType(initialLink);

                    switch (linkType) {
                        case Absolute:
                            // https://www.wikipedia.org/index.html
                            link = initialLink;
                            break;

                        case WithoutProtocol:
                            // //en.wikipedia.org/ or en.wikipedia.org/
                            if (initialLink.startsWith("//")) {
                                if (url.startsWith("https")) {
                                    link = "https:" + initialLink;
                                } else {
                                    link = "http:" + initialLink;
                                }
                            } else {
                                if (url.startsWith("https")) {
                                    link = "https://" +  initialLink;
                                } else {
                                    link = "http://" +  initialLink;
                                }
                            }
                            break;

                        case Relative:
                            // page.html
                            if (!url.endsWith("/")) {
                                link = url.replaceAll("(?<=[^/])/[^ /]+", "") + "/" + initialLink;
                            } else {
                                link = url.replaceAll("(?<=[^/])/[^ /]+", "") + initialLink;
                            }
                            break;

                        default:
                            link = "";
                            break;
                    }

                    title = getMainTitle(link);

                    if (!title.equals("")) {
                        model.addRow(new Object[]{link, title});
                    }

                }
            }

        } catch (IOException ignored) { }
    }


    public LinkType getLinkType(String initialLink) {

        if (initialLink.startsWith("http")) {
            return LinkType.Absolute;

        } else if (!initialLink.contains("/")) {
            return LinkType.Relative;

        } else {
            return LinkType.WithoutProtocol;
        }
    }
}