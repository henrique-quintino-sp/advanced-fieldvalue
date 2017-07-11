package forms;

import dao.IIQInstance;
import sailpoint.api.SailPointContext;
import sailpoint.object.*;
import sailpoint.tools.GeneralException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    private JTextField textField1;
    private JButton generateButton;
    private JButton loadButton;
    private JButton connectButton;

    private SailPointContext spContext;

    public FormMain() {
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSchema();
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getActionCommand());
                loadContext();
                loadApplications();

                loadIdentityAttributes();
            }
        });
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
            System.out.println(err.getMessage());
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

        }
        catch (GeneralException err)
        {
            System.out.println("Exception ok - " + err.getMessage());
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
            System.out.println("Error " + err.getMessage());
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
            System.err.println("Error in Connecting - " + err.getMessage());
        }
    }
}
