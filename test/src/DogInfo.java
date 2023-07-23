import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;


import se.datadosen.component.RiverLayout;

public class DogInfo {
	// DB ���� ������
	Connection conn;		// DB ���� Connection ��ü��������
	
	// �ֻ��� ������
	JFrame frame;
	String frameTitle = "������ ���� (pet) �����ͺ��̽� Ŭ���̾�Ʈ";

	// �ؽ�Ʈ �ڽ���
    JTextField name;		// name �ʵ� ���÷��̸� ���� �ڽ�
    JTextField owner;		// owner �ʵ� ���÷��̸� ���� �ڽ�
    JTextField species;		// species �ʵ� ���÷��̸� ���� �ڽ�
    JTextField birth;		// birth �ʵ� ���÷��̸� ���� �ڽ�

    // ������ ���� �ڽ�
    JTextField search;		// ������ ����  �ʵ�

    // ���� ��ư��
    JRadioButton male = new JRadioButton("Male");			// ������ ����ϱ� ���� �ϵ�� ��ư
    JRadioButton female = new JRadioButton("Female");		// ������ ����ϱ� ���� �ϵ�� ��ư
    
    // ��ư��
    JButton bSearch;		// ���� ������ ���� ��ư
    JButton bSave;			// ���� ������ ���� ��ư
    JButton bDelete;		// ���� ������ ���� ��ư
    JButton bNew;			// �ű� ������ ���� ��ư
    
    // ����Ʈ
    JList names = new JList();			// ������ �̸��� ������ �ִ� ����Ʈ
    
    public static void main(String[] args) {
       DogInfo client = new DogInfo();
       client.setUpGUI();
       client.dbConnectionInit();
    }

    private void setUpGUI() {
    	// build GUI
	   	frame = new JFrame(frameTitle);

	   	// ������ �̸� ��ü�� �����ִ� ��Ʈ�� (���� ��� �г�)
	   	JPanel leftTopPanel = new JPanel(new RiverLayout());
	   	
        JScrollPane cScroller = new JScrollPane(names);
        cScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        cScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        names.setVisibleRowCount(7);
        names.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        names.setFixedCellWidth(100);
        leftTopPanel.add("br center", new JLabel("������ �̸���"));
        leftTopPanel.add("p center", cScroller);
	   	
        // �Է� â��� �� (������ ��� �г�)
	   	JPanel rightTopPanel = new JPanel(new RiverLayout());
	   	name = new JTextField(20);
	   	owner = new JTextField(20);
	   	species = new JTextField(20);
	   	birth = new JTextField(20);
	    ButtonGroup gender = new ButtonGroup();				// ���� ��ư �׷�
	    gender.add(male);
	    gender.add(female);
	    
	   	rightTopPanel.add("br center", new JLabel("�� �� �� �� ��"));
	   	rightTopPanel.add("p left", new JLabel("��   ��"));
	   	rightTopPanel.add("tab", name);
	   	rightTopPanel.add("br", new JLabel("��   ��"));
	   	rightTopPanel.add("tab", owner);
	   	rightTopPanel.add("br", new JLabel("��"));
	   	rightTopPanel.add("tab", species);
	   	rightTopPanel.add("br", new JLabel("��   ��"));
	   	rightTopPanel.add("tab", male);
	   	rightTopPanel.add("tab", female);
	   	rightTopPanel.add("br", new JLabel("��   ��"));
	   	rightTopPanel.add("tab", birth);

        // ���� �ϴ� �г�
	   	JPanel leftBottomPanel = new JPanel(new RiverLayout());
	   	JPanel tmpPanel = new JPanel(new RiverLayout());
	   	search = new JTextField(20);
        bSearch = new JButton("�˻�");
        tmpPanel.add(search);
        tmpPanel.add(bSearch);
        leftBottomPanel.add("center", tmpPanel);
        
        // ������ �ϴ� �г�
	   	JPanel rightBottomPanel = new JPanel(new RiverLayout());
	   	tmpPanel = new JPanel(new RiverLayout());
        bSave = new JButton("����");
        bDelete = new JButton("����");
        bNew = new JButton("�ű�");
        tmpPanel.add(bSave);
        tmpPanel.add("tab", bDelete);
        tmpPanel.add("tab", bNew);
        rightBottomPanel.add("center", tmpPanel);
        rightBottomPanel.add("br", Box.createRigidArea(new Dimension(0,20)));

	   	// GUI ��ġ
        JPanel topPanel = new JPanel(new GridLayout(1,2));
        topPanel.add(leftTopPanel);
        topPanel.add(rightTopPanel);
        JPanel bottomPanel = new JPanel(new GridLayout(1,2));
        bottomPanel.add(leftBottomPanel);
        bottomPanel.add(rightBottomPanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // ActionListener�� ����
		names.addListSelectionListener(new NameListListener());
		MySearchListener l = new MySearchListener();
		search.addActionListener(l);								// �ؽ�Ʈ �ڽ����� ���� ���� ���� ���� �� ��
        bSearch.addActionListener(l);								// ��ư���� ���� ������ ��
        bSave.addActionListener(new SaveButtonListener());
        bDelete.addActionListener(new DeleteButtonListener());
        bNew.addActionListener(new NewButtonListener());
        
        // Ŭ���̾�� ������ â ����
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.setSize(700,350);
        frame.setVisible(true);
    }

    // DB�� �����ϴ� �޼ҵ�
    private void dbConnectionInit() {
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");					// JDBC����̹��� JVM�������� ��������
    		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/proj1", "root", "mite");	// DB �����ϱ�
    		prepareList();
    	}
        catch (ClassNotFoundException cnfe) {
            System.out.println("JDBC ����̹� Ŭ������ ã�� �� �����ϴ� : " + cnfe.getMessage());
        }
        catch (Exception ex) {
            System.out.println("DB ���� ���� : " + ex.getMessage());
        }
	}

    // DB�� �ִ� ��ü ���ڵ带 �ҷ��ͼ� ����Ʈ�� �ѷ��ִ� �޼�
    public void prepareList() {
    	try {
    		Statement stmt = conn.createStatement();			// SQL ���� �ۼ��� ����  Statement ��ü ����

    		// ���� DB�� �ִ� ���� �����ؼ� ������ ����� names ����Ʈ�� ����ϱ�
    		ResultSet rs = stmt.executeQuery("SELECT * FROM pet");
    		Vector<String> list = new Vector<String>();
    		while (rs.next()) {
    			list.add(rs.getString("name"));		
    		}
    		stmt.close();										// statement�� ����� �ݴ� ����
    		Collections.sort(list);								// �켱 ��������
    		names.setListData(list);							// names�� ���� �Ӽ��� �״�� �ΰ� ���빰�� �ٲ۴�
    		if (!list.isEmpty())								// ����Ʈ�� �ٲ�� ���� �׻� ù��° �������� ����Ű�� 
    			names.setSelectedIndex(0);
    	} catch (SQLException sqlex) {
    		System.out.println("SQL ���� : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
    }

	// ����Ʈ �ڽ��� �׼��� �߻��ϸ� ó���ϴ� ������
	public class NameListListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent lse) {					// ����Ʈ�� ������ �ٲ𶧸��� ȣ��
			if (!lse.getValueIsAdjusting() && !names.isSelectionEmpty()) {  // ���� ������ �� ���� ��쿡 ó��
		    	try {
		    		Statement stmt = conn.createStatement();				// SQL ���� ����� ���� Statement ��ü
		    		ResultSet rs = stmt.executeQuery("SELECT * FROM pet WHERE name = '" +
		    				(String)names.getSelectedValue() + "'");
		    		rs.next();												// �������� ���ϵǾ ù��° ������ ��� 
		    		name.setText(rs.getString("name"));			// DB���� ���� �� ���� ������ �ý�Ʈ �ڽ� ä��
		    		owner.setText(rs.getString("owner"));		
		    		species.setText(rs.getString("species"));	
		    		if (rs.getString("gender").equals("m"))			
		    			male.setSelected(true);
		    		else
		    			female.setSelected(true);
		    		birth.setText(rs.getDate("birth").toString());
		    		stmt.close();
		    	} catch (SQLException sqlex) {
		    		System.out.println("SQL ���� : " + sqlex.getMessage());
		    		sqlex.printStackTrace();
		    	} catch (Exception ex) {
		    		System.out.println("DB Handling ����(����Ʈ ������) : " + ex.getMessage());
		    		ex.printStackTrace();
		    	}
			}
		}
	}
	
	// ���� ������Ʈ�� ������
	public class MySearchListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			int index = names.getNextMatch(search.getText().trim(), 0, Position.Bias.Forward);
			if (index != -1) {
				names.setSelectedIndex(index);
			}
			// search.setText("");
		}
	}

	// ���� ��ư�� ������
	public class DeleteButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
	    	try {
	    		Statement stmt = conn.createStatement();				// SQL ���� �ۼ��� ����  Statement ��ü ����
	    		stmt.executeUpdate("DELETE FROM pet WHERE name = '" +
	    				name.getText().trim() + "'");
	    		stmt.close();
	    		prepareList();											// ����Ʈ �ڽ� �� ����Ʈ�� �ٽ� ä�� 
	    	} catch (SQLException sqlex) {
	    		System.out.println("SQL ���� : " + sqlex.getMessage());
	    		sqlex.printStackTrace();
	    	} catch (Exception ex) {
	    		System.out.println("DB Handling ����(DELETE ������) : " + ex.getMessage());
	    		ex.printStackTrace();
	    	}
		}
	}

	// ���� ��ư�� ������
	public class SaveButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
	    	try {
				String savedName = name.getText().trim();				// ���� �� ��, �̰����� �ε����� �����ϱ� ���� �̸� �ӽ�����
	    		Statement stmt = conn.createStatement();				// SQL ���� �ۼ��� ����  Statement ��ü ����
	    		stmt.executeUpdate("DELETE FROM pet WHERE name = '" +	// ���� ���ڵ� �����ϰ� (name �ʵ�� ������ ���ٰ� ����)
	    				savedName + "'");
	    		String gender;
	    		if (male.isSelected())
	    			gender = "m";
	    		else
	    			gender = "f";
	    		stmt.executeUpdate("INSERT INTO pet (name, owner, species, gender, birth) VALUES ('" +	// �� ���ڵ�� ����
	    				savedName + "', '" +
	    				owner.getText().trim() + "', '" +
	    				species.getText().trim() + "', '" +
	    				gender + "', '" +
	    				birth.getText().trim() + "')");
	    		stmt.close();
	    		prepareList();											// �ٽ� �ѷ� 
				int index = names.getNextMatch(savedName, 0, Position.Bias.Forward);
				names.setSelectedIndex(index);
	    	} catch (SQLException sqlex) {
	    		System.out.println("SQL ���� : " + sqlex.getMessage());
	    		sqlex.printStackTrace();
	    	} catch (Exception ex) {
	    		System.out.println("DB Handling ����(SAVE ������) : " + ex.getMessage());
	    		ex.printStackTrace();
	    	}
		}
	}

	public class NewButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			name.setText("");
			owner.setText("");
			species.setText("");
			male.setSelected(true);
			female.setSelected(false);
			birth.setText("");
			names.clearSelection();
		}
	}

}