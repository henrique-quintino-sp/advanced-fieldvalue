package forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dao.FieldValue;
import dao.IIQInstance;
import generators.GenerateApplicationXML;
import generators.GenerateFieldValuesXML;
import generators.GenerateSPDynamicFieldValueRuleXML;
import generators.GenerateTemplateXML;
import sailpoint.api.SailPointContext;
import sailpoint.object.*;
import sailpoint.tools.GeneralException;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by henrique.quintino on 7/10/2017.
 */
public class FormMain {
    private static final String FORBIDEN_IDENTITY_ATTRIBUTES = "mapping/forbidden-identity-attributes.txt";
    private static final String MAPPING_PATH = "mapping";
    private JPanel panel1;
    private JComboBox cbApplication;
    private JButton button1;
    private JList listSchema;
    private JList listIdentity;
    private JTable tableResult;
    private JButton xButton;
    private JTextField txtCustom;
    private JButton generateButton;
    private JButton loadButton;
    private JButton connectButton;
    private JCheckBox checkUnique;
    private JButton button2;
    private JButton mapButton;
    private JButton templatesButton;

    private SailPointContext spContext;
    private Application currentApplication;
    private String DESTINATION_FOLDER = "Field_Value_Files";
    private ArrayList<String> arrSchema;
    private ArrayList<String> arrIdentityAttribute;
    DefaultTableModel tableModel;

    public FormMain() {

        loadButton.addActionListener(e -> {
            loadSchema();
            loadDefaultTable();
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final JFrame fLoader = loadingScreen();

                Runnable r = () -> {
                    loadContext();
                    loadApplications();

                    loadIdentityAttributes();

                    loadDefaultTable();
                    fLoader.setVisible(false);
                    fLoader.dispose();
                };

                Thread t = new Thread(r);
                t.start();
            }
        });

        button1.addActionListener(e -> recordSelection());
        xButton.addActionListener(e -> deleteTableRow());
        generateButton.addActionListener(e -> generateFieldValueXml());

        mapButton.addActionListener(e -> performAutomapping());
        templatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (templatesButton.getText().equals("templates")) {
                    loadTemplates();
                    templatesButton.setText("attributes");
                } else {
                    loadIdentityAttributes();
                    templatesButton.setText("templates");
                }
            }
        });
    }

    private void loadTemplates() {
        arrIdentityAttribute = new ArrayList<>();
        URL url = ClassLoader.getSystemClassLoader().getResource(MAPPING_PATH + "/templates.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(url.getFile()));
            String line = reader.readLine();
            DefaultListModel listModel = new DefaultListModel();

            while (line != null && !line.isEmpty()) {
                listModel.addElement(line);
                arrIdentityAttribute.add(line);

                line = reader.readLine();
            }
            listIdentity.setModel(listModel);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void performAutomapping() {
        ArrayList<FieldValue> listResult = new ArrayList<>();

        listResult.addAll(filebasedMapping());

        listResult.addAll(sameNameMapping());

        recordTableItem(listResult);
        rebindSchema();
    }

    private void rebindSchema() {

        DefaultListModel listModel = (DefaultListModel) listSchema.getModel();
        listModel.clear();
        for (String s : arrSchema)
            listModel.addElement(s);
    }

    private void recordTableItem(ArrayList<FieldValue> listResult) {
        for (FieldValue fv : listResult) {
            String[] row = {fv.getAppAttribute(), fv.getTargetAttribute(), Boolean.toString(fv.isCheckUniqueness())};
            tableModel.addRow(row);
        }
    }

    private List<FieldValue> filebasedMapping() {
        ArrayList<FieldValue> listResult = new ArrayList<>();
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource(MAPPING_PATH);
            String filePath = url.getPath() + "/map-" + currentApplication.getType() + ".txt";

            File f = new File(filePath);
            if (f.exists()) {
                FileReader fileReader = new FileReader(filePath);
                BufferedReader reader = new BufferedReader(fileReader);
                String line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    if (line.startsWith("#")) {
                        line = reader.readLine();
                        continue;
                    }

                    String[] sides = line.split("=");
                    if (sides.length != 2)
                        throw new Exception("Invalid file format");

                    Optional<String> optionalSchema = arrSchema.stream()
                            .filter(p -> p.equals(sides[0]))
                            .findFirst();

                    String appName, targetAtt;
                    if (optionalSchema.isPresent()) {
                        appName = optionalSchema.get();

                        if (sides[1].contains("'") || sides[1].contains("+") || sides[1].contains("["))
                            targetAtt = sides[1];
                        else {
                            Optional<String> optionalIdentity = arrIdentityAttribute.stream()
                                    .filter(p -> p.equals(sides[1]))
                                    .findFirst();
                            if (optionalIdentity.isPresent())
                                targetAtt = optionalIdentity.get();
                            else {
                                line = reader.readLine();
                                continue;
                            }
                        }

                        arrSchema.remove(appName);

                        FieldValue tempFieldValue = new FieldValue(appName, targetAtt, false);
                        listResult.add(tempFieldValue);
                    }

                    line = reader.readLine();
                }
            }

        } catch (Exception err) {
            showErrorMsg(err.toString(), "File Based Mapping");
        }

        return listResult;
    }

    private List<FieldValue> sameNameMapping() {
        ArrayList<FieldValue> listResult = new ArrayList<>();

        ArrayList<String> listMatch = new ArrayList<String>();

        for (String item : arrSchema) {
            for (String s : arrIdentityAttribute.stream()
                    .filter(p -> p.equals(item))
                    .collect(Collectors.toList())) {
                listMatch.add(s);
                listResult.add(new FieldValue(item, s, false));
            }
        }
        arrSchema.removeAll(listMatch);

        return listResult;
    }

    /* Final Generate Files With everything */
    private void generateFieldValueXml() {

        try {
            List<FieldValue> listFieldValue = buildFieldValue();

            GenerateApplicationXML genApp = new GenerateApplicationXML(currentApplication.toXml(), currentApplication.getName(), listFieldValue);
            String appFile = genApp.writeXML(ClassLoader.getSystemResource(DESTINATION_FOLDER).getPath());

            GenerateFieldValuesXML genFV = new GenerateFieldValuesXML(currentApplication.getName(), listFieldValue);
            String fvFile = genFV.writeXML(ClassLoader.getSystemResource(DESTINATION_FOLDER).getPath());

            GenerateSPDynamicFieldValueRuleXML genSPD = new GenerateSPDynamicFieldValueRuleXML(currentApplication.getName());
            String spdFile = genSPD.writeXML(ClassLoader.getSystemResource(DESTINATION_FOLDER).getPath());

            GenerateTemplateXML genTemplate = new GenerateTemplateXML(currentApplication.getName(), listFieldValue);
            String templateFile = genTemplate.writeXML(ClassLoader.getSystemResource(DESTINATION_FOLDER).getPath());

            showInformationMsg("Files generated in the folder \n" + ClassLoader.getSystemResource(DESTINATION_FOLDER).getPath(), "Generation complete");
        } catch (Exception err) {
            showErrorMsg(err.toString(), "Error generating XML");
        }
    }

    private List<FieldValue> buildFieldValue() {
        ArrayList<FieldValue> arrFieldValue = new ArrayList<>();

        for (int count = 0; count < tableModel.getRowCount(); count++) {
            FieldValue f = new FieldValue();
            f.setAppAttribute(tableModel.getValueAt(count, 0).toString());
            f.setTargetAttribute(tableModel.getValueAt(count, 1).toString());
            f.setCheckUniqueness(Boolean.parseBoolean(tableModel.getValueAt(count, 2).toString()));
            f.setDisplayName(tableModel.getValueAt(count, 0).toString());

            arrFieldValue.add(f);
        }
        return arrFieldValue;
    }

    private void recordSelection() {
        if (validSelection()) {
            String schemaSelected = listSchema.getSelectedValue().toString();
            String identitySelected = txtCustom.getText();
            if (identitySelected == null || identitySelected.isEmpty())
                identitySelected = listIdentity.getSelectedValue().toString();
            Boolean unique = checkUnique.getSelectedObjects() != null;
            String[] row = {schemaSelected, identitySelected, unique.toString()};

            tableModel.addRow(row);

            clearSelection();
        }
    }

    private boolean validSelection() {
        ArrayList<String> arrErrors = new ArrayList<>();

        if (listSchema.getSelectedValue() == null)
            arrErrors.add(" * Invalid a schema item");
        if (listIdentity.getSelectedValue() == null && txtCustom.getText().isEmpty())
            arrErrors.add(" * Invalid Identity target or expression");

        if (arrErrors.size() > 0) {
            String errorMsg = "The following error(s) were found:\n\n";
            for (String s : arrErrors)
                errorMsg += s + "\n";

            showErrorMsg(errorMsg, "Invalid selection");
        }

        return arrErrors.size() == 0;
    }

    private void loadIdentityAttributes() {
        try {

            ObjectConfig objConfig = spContext.getObjectByName(ObjectConfig.class, "Identity");

            List<ObjectAttribute> atts = filterSpecialIdentityAttributes(objConfig.getObjectAttributes());

            DefaultListModel listModel = new DefaultListModel();
            arrIdentityAttribute = new ArrayList<>();
            listIdentity.setModel(listModel);
            for (ObjectAttribute att : atts) {
                listModel.addElement(att.getName());
                arrIdentityAttribute.add(att.getName());
            }
            Thread.sleep(100);
        } catch (GeneralException err) {
            showErrorMsg("Problem loading Identity attributes \n" + err.getMessage(), "Error loading Identity Attributes");
        } catch (Exception err) {
            showErrorMsg("Problem loading Identity attributes \n" + err.getMessage(), "Error loading Identity Attributes");
        }
    }

    private JFrame loadingScreen() {
        JFrame fLoad = new JFrame("Fetching the Rainbow");
        fLoad.setContentPane(new FormLoader().getPanel1());

        fLoad.setAlwaysOnTop(true);

        fLoad.pack();
        fLoad.setLocationRelativeTo(null);
        fLoad.setVisible(true);

        URL url = ClassLoader.getSystemResource("img/fav-icon2.png");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(url);
        fLoad.setIconImage(img);

        return fLoad;
    }

    private List<ObjectAttribute> filterSpecialIdentityAttributes(List<ObjectAttribute> atts) {
        ArrayList<ObjectAttribute> result = new ArrayList<>(atts);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ClassLoader.getSystemResource(FORBIDEN_IDENTITY_ATTRIBUTES).getPath()));
            String filterLine = reader.readLine();
            if (filterLine != null && !filterLine.isEmpty()) {
                if (!filterLine.startsWith("name="))
                    throw new Exception("Invalid file format");
                filterLine = filterLine.replaceAll("name=", "");
                String[] forbiddens = filterLine.split(",");

                for (String s : forbiddens) {
                    List<ObjectAttribute> listAttFilter = result.stream().filter(u -> u.getName().equals(s)).collect(Collectors.toList());
                    if (listAttFilter != null && listAttFilter.size() > 0)
                        result.removeAll(listAttFilter);
                }
            }
            reader.close();
        } catch (Exception err) {
            showErrorMsg("Filter Identity Attributes error: \n" + err.getMessage(), "Filter Identity Attributes");
        }

        return result;
    }

    private void loadSchema() {
        try {
            Application app = spContext.getObjectByName(Application.class, cbApplication.getSelectedItem().toString());
            Schema schema = app.getAccountSchema();

            DefaultListModel listModel = new DefaultListModel();
            listSchema.setModel(listModel);
            arrSchema = new ArrayList<>();
            for (String s : schema.getAttributeNames()) {
                listModel.addElement(s);
                arrSchema.add(s);
            }
            currentApplication = app;
        } catch (GeneralException err) {
            showErrorMsg("Problem loading Application Schema \n" + err.getMessage(), "Error loading Application Schema");

        }
    }

    private void loadApplications() {
        try {
            List<Application> apps = spContext.getObjects(Application.class);
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            cbApplication.setModel(model);

            for (Application app : apps)
                model.addElement(app.getName());

            //spContext.close();
        } catch (GeneralException err) {
            showErrorMsg("Problem loading Application \n" + err.getMessage(), "Error loading Application");
        }
    }

    private void loadContext() {
        try {
            IIQInstance instance = IIQInstance.getIIQInstance();
            instance.connect();
            instance.isConnected();
            spContext = instance.getContext();
        } catch (GeneralException err) {
            showErrorMsg("Problem connecting to IIQ \n" + err.getMessage(), "Error connecting to IIQ");
        }
    }

    private void clearSelection() {
        checkUnique.setSelected(false);
        txtCustom.setText("");
        listIdentity.clearSelection();
        listSchema.clearSelection();
    }

    private void showErrorMsg(String message, String title) {
        JOptionPane.showMessageDialog(panel1, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void showInformationMsg(String message, String title) {
        JOptionPane.showMessageDialog(panel1, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteTableRow() {
        int selectedRow = tableResult.getSelectedRow();
        if (selectedRow > -1) {
            tableModel.removeRow(selectedRow);
        }
    }

    private void loadDefaultTable() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Application");
        tableModel.addColumn("Identity");
        tableModel.addColumn("Unique");
        tableResult.setModel(tableModel);
        tableResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("FV Accelerator - Unicorn Power");
        frame.setContentPane(new FormMain().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        URL url = ClassLoader.getSystemResource("img/fav-icon2.png");

        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(url);
        frame.setIconImage(img);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 3, new Insets(5, 5, 5, 5), -1, -1));
        panel1.setMaximumSize(new Dimension(800, 600));
        panel1.setMinimumSize(new Dimension(800, 600));
        panel1.setPreferredSize(new Dimension(800, 600));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        generateButton = new JButton();
        generateButton.setText("Generate");
        panel2.add(generateButton, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xButton = new JButton();
        xButton.setText("X");
        panel2.add(xButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableResult = new JTable();
        scrollPane1.setViewportView(tableResult);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:350px:noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,top:4dlu:noGrow,center:d:grow"));
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("IIQ");
        CellConstraints cc = new CellConstraints();
        panel3.add(label1, cc.xy(1, 1));
        cbApplication = new JComboBox();
        cbApplication.setMinimumSize(new Dimension(36, 26));
        panel3.add(cbApplication, cc.xy(3, 3));
        final JLabel label2 = new JLabel();
        label2.setText("https://192.168.118.35:8080/identityiq");
        panel3.add(label2, cc.xy(3, 1));
        final JLabel label3 = new JLabel();
        label3.setText("App");
        panel3.add(label3, cc.xy(1, 3));
        loadButton = new JButton();
        loadButton.setText("Load");
        panel3.add(loadButton, cc.xy(5, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));
        connectButton = new JButton();
        connectButton.setText("Connect");
        panel3.add(connectButton, cc.xy(5, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 400), new Dimension(-1, 400), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Schema");
        panel4.add(label4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(42, 15), null, 0, false));
        txtCustom = new JTextField();
        panel4.add(txtCustom, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Identity");
        panel4.add(label5, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, 15), null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel4.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 3, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        listIdentity = new JList();
        listIdentity.setSelectionMode(0);
        scrollPane2.setViewportView(listIdentity);
        final JScrollPane scrollPane3 = new JScrollPane();
        panel4.add(scrollPane3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 4, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 350), new Dimension(-1, 350), null, 0, false));
        listSchema = new JList();
        listSchema.setLayoutOrientation(0);
        listSchema.setSelectionMode(0);
        scrollPane3.setViewportView(listSchema);
        templatesButton = new JButton();
        templatesButton.setText("templates");
        panel4.add(templatesButton, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(82, 15), null, 0, false));
        mapButton = new JButton();
        mapButton.setText("Map");
        panel4.add(mapButton, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button1 = new JButton();
        button1.setText("<>");
        panel4.add(button1, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkUnique = new JCheckBox();
        checkUnique.setLabel("Unique");
        checkUnique.setText("Unique");
        panel4.add(checkUnique, new com.intellij.uiDesigner.core.GridConstraints(2, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        label5.setLabelFor(scrollPane2);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
