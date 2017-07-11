package forms;

import dao.IIQInstance;
import sailpoint.api.SailPointContext;
import sailpoint.object.*;
import sailpoint.tools.GeneralException;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrique.quintino on 7/10/2017.
 */
public class FormMain {
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

    private SailPointContext spContext;
    private String currentApplication;
    private String DESTINATION_FOLDER= "Field Value Files";
    DefaultTableModel tableModel;

    public FormMain() {

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSchema();
                loadDefaultTable();
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                loadContext();
                loadApplications();

                loadIdentityAttributes();

                loadDefaultTable();
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordSelection();
            }
        });
        xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTableRow();
            }
        });
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateFieldValueXml();
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("iiq.properties"));

                    String line = reader.readLine();
                    while(line != null)
                    {
                        if(line.startsWith("dataSource.url"))
                        {
                            System.out.println(line);
                            break;
                        }
                        line = reader.readLine();
                    }
                    reader.close();
                }catch (Exception err){
                    System.out.println(err.getMessage());

                }
            }
        });
    }

    private void generateFieldValueXml() {
        //GenerateFieldValuesXML genXml = new GenerateFieldValuesXML(currentApplication,null,null);
        //genXml.writeXML();
    }

    private void recordSelection() {
        if(validSelection()) {
            String schemaSelected = listSchema.getSelectedValue().toString();
            String identitySelected = txtCustom.getText();
            if(identitySelected == null || identitySelected.isEmpty())
                identitySelected = listIdentity.getSelectedValue().toString();
            Boolean unique = checkUnique.getSelectedObjects() != null;
            String[] row = {schemaSelected, identitySelected, unique.toString()};

            tableModel.addRow(row);

            clearSelection();
        }
    }

    private boolean validSelection() {
        ArrayList<String> arrErrors = new ArrayList<>();

        if(listSchema.getSelectedValue() == null)
            arrErrors.add(" * Invalid a schema item");
        if(listIdentity.getSelectedValue() == null && txtCustom.getText().isEmpty())
            arrErrors.add(" * Invalid Identity target or expression");

        if(arrErrors.size() > 0)
        {
            String errorMsg="The following error(s) were found:\n\n";
            for(String s : arrErrors )
                errorMsg += s + "\n";

            showErrorMsg(errorMsg,"Invalid selection");
        }

        return arrErrors.size() == 0;
    }



    private void loadIdentityAttributes() {
        try {
            ObjectConfig objConfig = spContext.getObjectByName(ObjectConfig.class, "Identity");

            List<ObjectAttribute> atts = objConfig.getObjectAttributes();

            DefaultListModel listModel = new DefaultListModel();
            listIdentity.setModel(listModel);
            for(ObjectAttribute att : atts)
                listModel.addElement(att.getName());

        }catch (GeneralException err)
        {
            showErrorMsg("Problem loading Identity attributes \n" + err.getMessage(),"Error loading Identity Attributes");
        }
    }

    private void loadSchema() {
        try
        {
            Application app = spContext.getObjectByName(Application.class, cbApplication.getSelectedItem().toString());
            Schema schema = app.getAccountSchema();

            DefaultListModel listModel = new DefaultListModel();
            listSchema.setModel(listModel);
            for(String s : schema.getAttributeNames())
                listModel.addElement(s);
            currentApplication = cbApplication.getSelectedItem().toString();
        }
        catch (GeneralException err)
        {
            showErrorMsg("Problem loading Application Schema \n" + err.getMessage(),"Error loading Application Schema");

        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("FV Accelerator - Unicorn Power");
        frame.setContentPane(new FormMain().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        java.net.URL url = ClassLoader.getSystemResource("img/fav-icon2.png");

        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(url);
        frame.setIconImage(img);
    }

    private void loadApplications() {
        try {
            List<Application> apps = spContext.getObjects(Application.class);
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            cbApplication.setModel(model);

            for(Application app : apps)
                model.addElement(app.getName());

            //spContext.close();
        }catch (GeneralException err)
        {
            showErrorMsg("Problem loading Application \n" + err.getMessage(),"Error loading Application");
        }
    }

    private void loadContext() {
        try {
            IIQInstance instance = IIQInstance.getIIQInstance();
            instance.connect();
            instance.isConnected();

            spContext = instance.getContext();
        }
        catch (GeneralException err)
        {
            showErrorMsg("Problem connecting to IIQ \n" + err.getMessage(),"Error connecting to IIQ");
        }
    }

    private void clearSelection() {
        checkUnique.setSelected(false);
        txtCustom.setText("");
        listIdentity.clearSelection();
        listSchema.clearSelection();
    }
    private void showErrorMsg(String message,String title) {
        JOptionPane.showMessageDialog(panel1, message,title,JOptionPane.ERROR_MESSAGE);
    }
    private void deleteTableRow() {
        int selectedRow = tableResult.getSelectedRow();
        if(selectedRow > -1)
        {
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
}
