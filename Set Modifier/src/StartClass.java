import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;



public class StartClass
{
	
	
	
	public static JFrame window;
	
	public static Container container;
	
	
	
	public static Dimension screenSize;
	
	
	
	public static JTextField loadPath;

	public static JTextField savePath;
	
	public static JComboBox<String> conditionCount;
	
	
	
	public static JLabel conditionLabel;
	
	
	
	public static JComboBox<String> conditionKey1;
	
	public static JComboBox<String> conditionOperation1;
	
	public static JTextField conditionValue1;
	
	
	
	public static JComboBox<String> conditionKey2;
	
	public static JComboBox<String> conditionOperation2;
	
	public static JTextField conditionValue2;
	
	
	
	public static JComboBox<String> conditionKey3;
	
	public static JComboBox<String> conditionOperation3;
	
	public static JTextField conditionValue3;
	
	
	
	public static JLabel replacementLabel;
	
	
	
	public static JComboBox<String> replacementKey;
	
	public static JComboBox<String> replacementOperation;
	
	public static JTextField replacementValue;
	
	
	
	public static JButton button;
	
	
	
	public static JLabel folderMessage;
	
	public static JLabel imageMessage;
	
	public static JLabel saveMessage;
	
	public static JLabel saveNameMessage;
	
	
	
	
	
	public static void main(String[] args)
	{
		
		GUI.setFont(new javax.swing.plaf.FontUIResource("Arial",java.awt.Font.BOLD,20));
		
		
		
		window = new JFrame("Set Modifier");
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		window.setLocation(screenSize.width/10, screenSize.height/10);
		
		
		
		container = window.getContentPane();
		
		container.setLayout(new GridBagLayout());
		
		
		
		String desktopPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator;
		
		
		
		loadPath = GUI.addLabeledTextField(container, 0, 0, "Path to Set file :", desktopPath + "SETNAME.mse-set", true, 400, 7);
		
		//savePath = GUI.addLabeledTextField(container, 0, 1, "Save the Set to :", desktopPath + "RESULTNAME.mse-set", true, 400, 7);
		
		
		
		conditionCount = GUI.addLabeledComboBox(container, 0, 2, "Number of conditions :", new String[] {"0", "1", "2", "3"}, true, 400, 7);
		
		
		
		conditionLabel = GUI.addLabel(container, GUI.createConstraints(0, 3, 0, 10, 8, 1), "On all the cards in the Set");
		
		
		
		conditionKey1 = GUI.addComboBox(container, GUI.createConstraints(0, 5, 0, 10, 4, 1), new ArrayList<String>(Modifier.conditionOptions.keySet()));
		
		conditionOperation1 = GUI.addComboBox(container, GUI.createConstraints(4, 5, 0, 10, 4, 1), Modifier.defaultTextConditionOptions);
		
		conditionValue1 = GUI.addTextField(container, GUI.createConstraints(0, 6, 0, 10, 8, 1), "", 0);
		
		
		
		conditionKey2 = GUI.addComboBox(container, GUI.createConstraints(0, 8, 0, 10, 4, 1), new ArrayList<String>(Modifier.followingConditionOptions.keySet()));
		
		conditionOperation2 = GUI.addComboBox(container, GUI.createConstraints(4, 8, 0, 10, 4, 1), Modifier.defaultTextConditionOptions);
		
		conditionValue2 = GUI.addTextField(container, GUI.createConstraints(0, 9, 0, 10, 8, 1), "", 0);
		
		
		
		conditionKey3 = GUI.addComboBox(container, GUI.createConstraints(0, 11, 0, 10, 4, 1), new ArrayList<String>(Modifier.followingConditionOptions.keySet()));
		
		conditionOperation3 = GUI.addComboBox(container, GUI.createConstraints(4, 11, 0, 10, 4, 1), Modifier.defaultTextConditionOptions);
		
		conditionValue3 = GUI.addTextField(container, GUI.createConstraints(0, 12, 0, 10, 8, 1), "", 0);
		
		
		
		replacementLabel = GUI.addLabel(container, GUI.createConstraints(0, 13, 0, 10, 8, 1), "Apply the following operation :");
		
		
		
		replacementKey = GUI.addComboBox(container, GUI.createConstraints(0, 14, 0, 10, 4, 1), new ArrayList<String>(Modifier.replacementOptions.keySet()));
		
		replacementOperation = GUI.addComboBox(container, GUI.createConstraints(4, 14, 0, 10, 4, 1), Modifier.defaultTextReplacementOptions);
		
		replacementValue = GUI.addTextField(container, GUI.createConstraints(0, 15, 0, 10, 8, 1), "", 0);
		
		
		
		button = GUI.addButton(container, GUI.createConstraints(0, 16, 0, 10, 8, 1), "Modify Set");
		
		
		
		folderMessage = GUI.addLabel(container, GUI.createConstraints(0, 17, 0, 10, 8, 1), "");
		
		imageMessage = GUI.addLabel(container, GUI.createConstraints(0, 18, 0, 10, 8, 1), "");
		
		saveMessage = GUI.addLabel(container, GUI.createConstraints(0, 19, 0, 10, 8, 1), "");
		
		saveNameMessage = GUI.addLabel(container, GUI.createConstraints(0, 20, 0, 10, 8, 1), "");
		
		
		
		conditionCount.addActionListener(new ConditionCountComboBoxListener(conditionCount, conditionLabel, conditionKey1, conditionOperation1, conditionValue1, conditionKey2, conditionOperation2, conditionValue2, conditionKey3, conditionOperation3, conditionValue3));
		
		
		
		conditionKey1.addActionListener(new KeyComboBoxListener(conditionKey1, conditionOperation1, conditionValue1, Modifier.conditionOptions));
		
		conditionOperation1.addActionListener(new OperationComboBoxListener(conditionKey1, conditionOperation1, conditionValue1));
		
		conditionKey2.addActionListener(new KeyComboBoxListener(conditionKey2, conditionOperation2, conditionValue2, Modifier.followingConditionOptions));
		
		conditionOperation2.addActionListener(new OperationComboBoxListener(conditionKey2, conditionOperation2, conditionValue2));
		
		conditionKey3.addActionListener(new KeyComboBoxListener(conditionKey3, conditionOperation3, conditionValue3, Modifier.followingConditionOptions));
		
		conditionOperation3.addActionListener(new OperationComboBoxListener(conditionKey3, conditionOperation3, conditionValue3));
		
		
		
		replacementKey.addActionListener(new KeyComboBoxListener(replacementKey, replacementOperation, replacementValue, Modifier.replacementOptions));
		
		replacementOperation.addActionListener(new OperationComboBoxListener(replacementKey, replacementOperation, replacementValue));
		
		
		
		button.addActionListener(new ModifyButtonListener(button));
		
		
		
		conditionCount.setSelectedIndex(0);
		
		
		
		//Hack to allow a component to start or end at the mid point of another component on another row
		GUI.addZeroHeightVoid(container, GUI.createConstraints(0, 21, 1, 1));
		GUI.addZeroHeightVoid(container, GUI.createConstraints(1, 21, 1, 1));
		GUI.addZeroHeightVoid(container, GUI.createConstraints(2, 21, 1, 1));
		GUI.addZeroHeightVoid(container, GUI.createConstraints(3, 21, 1, 1));
		GUI.addZeroHeightVoid(container, GUI.createConstraints(4, 21, 1, 1));
		GUI.addZeroHeightVoid(container, GUI.createConstraints(5, 21, 1, 1));
		GUI.addZeroHeightVoid(container, GUI.createConstraints(6, 21, 1, 1));
		GUI.addZeroHeightVoid(container, GUI.createConstraints(7, 21, 1, 1));
		
		
		
		window.pack();
		
		window.setVisible(true);
		
	}
	
	
	
	
	
	static class ModifyButtonListener implements ActionListener
	{
		
		JButton button;
		
		public ModifyButtonListener(JButton button)
		{
			
			super();
			
			this.button = button;
			
		}
		
		@Override
		public void actionPerformed(ActionEvent event)
		{
			
			button.setEnabled(false);
			
			folderMessage.setText("");
			
			imageMessage.setText("");
			
			saveMessage.setText("");
			
			saveNameMessage.setText("");
			
			folderMessage.setForeground(Color.black);
			
			imageMessage.setForeground(Color.black);
			
			saveMessage.setForeground(Color.black);
			
			saveNameMessage.setForeground(Color.black);
			
			
			
			String loadPathString = loadPath.getText();
			
			String conditionCountString = String.valueOf(conditionCount.getSelectedItem());
			
			List<String> conditionKeyStrings = new ArrayList<String>(Arrays.asList(String.valueOf(conditionKey1.getSelectedItem()), String.valueOf(conditionKey2.getSelectedItem()), String.valueOf(conditionKey3.getSelectedItem())));
			
			List<String> conditionOperationStrings = new ArrayList<String>(Arrays.asList(String.valueOf(conditionOperation1.getSelectedItem()), String.valueOf(conditionOperation2.getSelectedItem()), String.valueOf(conditionOperation3.getSelectedItem())));
			
			List<String> conditionValueStrings = new ArrayList<String>(Arrays.asList(conditionValue1.getText(), conditionValue2.getText(), conditionValue3.getText()));
			
			String replacementKeyString = String.valueOf(replacementKey.getSelectedItem());
			
			String replacementOpertaionString = String.valueOf(replacementOperation.getSelectedItem());
			
			String replacementValueString = replacementValue.getText();
			
			
			
			new ModifierWorker(loadPathString, conditionCountString, conditionKeyStrings, conditionOperationStrings, conditionValueStrings, replacementKeyString, replacementOpertaionString, replacementValueString).execute();
			
			
			
			new ButtonEnablerLaterWorker(button, 1500l).execute();
			
		}
	}
	
	
	
	static class ConditionCountComboBoxListener implements ActionListener
	{
		
		JComboBox<String> conditionCountComboBox;
		
		JLabel conditionLabel;
		
		
		
		JComboBox<String> conditionKeyComboBox1;
		
		JComboBox<String> conditionOperationComboBox1;
		
		JTextField conditionValueTextField1;
		
		
		
		JComboBox<String> conditionKeyComboBox2;
		
		JComboBox<String> conditionOperationComboBox2;
		
		JTextField conditionValueTextField2;
		
		
		
		JComboBox<String> conditionKeyComboBox3;
		
		JComboBox<String> conditionOperationComboBox3;
		
		JTextField conditionValueTextField3;
		
		public ConditionCountComboBoxListener
		(
			JComboBox<String> conditionCountComboBox, JLabel conditionLabel,
			JComboBox<String> conditionKeyComboBox1, JComboBox<String> conditionOperationComboBox1, JTextField conditionValueTextField1,
			JComboBox<String> conditionKeyComboBox2, JComboBox<String> conditionOperationComboBox2, JTextField conditionValueTextField2,
			JComboBox<String> conditionKeyComboBox3, JComboBox<String> conditionOperationComboBox3, JTextField conditionValueTextField3
		)
		{
			
			super();
			
			this.conditionCountComboBox = conditionCountComboBox;
			
			this.conditionLabel = conditionLabel;
			
			
			
			this.conditionKeyComboBox1 = conditionKeyComboBox1;
			
			this.conditionOperationComboBox1 = conditionOperationComboBox1;
			
			this.conditionValueTextField1 = conditionValueTextField1;
			
			
			
			this.conditionKeyComboBox2 = conditionKeyComboBox2;
			
			this.conditionOperationComboBox2 = conditionOperationComboBox2;
			
			this.conditionValueTextField2 = conditionValueTextField2;
			
			
			
			this.conditionKeyComboBox3 = conditionKeyComboBox3;
			
			this.conditionOperationComboBox3 = conditionOperationComboBox3;
			
			this.conditionValueTextField3 = conditionValueTextField3;
			
		}
		
		@Override
		public void actionPerformed(ActionEvent event)
		{
			
			String conditionKeyString = String.valueOf(conditionCountComboBox.getSelectedItem());
			
			if (conditionKeyString.equals("0"))
			{
				
				conditionLabel.setText("On all the cards in the Set");
				
				
				
				conditionKeyComboBox1.setVisible(false);
				
				conditionOperationComboBox1.setVisible(false);
				
				conditionValueTextField1.setVisible(false);
				
				
				
				conditionKeyComboBox2.setVisible(false);
				
				conditionOperationComboBox2.setVisible(false);
				
				conditionValueTextField2.setVisible(false);
				
				
				
				conditionKeyComboBox3.setVisible(false);
				
				conditionOperationComboBox3.setVisible(false);
				
				conditionValueTextField3.setVisible(false);
				
			}
			
			else if (conditionKeyString.equals("1"))
			{
				
				conditionLabel.setText("On all the cards that meet this condition :");
				
				
				
				conditionKeyComboBox1.setVisible(true);
				
				conditionOperationComboBox1.setVisible(true);
				
				if (String.valueOf(conditionOperationComboBox1.getSelectedItem()).endsWith(":")) conditionValueTextField1.setVisible(true);
				
				else conditionValueTextField1.setVisible(false);
				
				
				
				conditionKeyComboBox2.setVisible(false);
				
				conditionOperationComboBox2.setVisible(false);
				
				conditionValueTextField2.setVisible(false);
				
				
				
				conditionKeyComboBox3.setVisible(false);
				
				conditionOperationComboBox3.setVisible(false);
				
				conditionValueTextField3.setVisible(false);
				
			}
			
			else if (conditionKeyString.equals("2"))
			{
				
				conditionLabel.setText("On all the cards that meet both these conditions :");
				
				
				
				conditionKeyComboBox1.setVisible(true);
				
				conditionOperationComboBox1.setVisible(true);
				
				if (String.valueOf(conditionOperationComboBox1.getSelectedItem()).endsWith(":")) conditionValueTextField1.setVisible(true);
				
				else conditionValueTextField1.setVisible(false);
				
				
				
				conditionKeyComboBox2.setVisible(true);
				
				conditionOperationComboBox2.setVisible(true);
				
				if (String.valueOf(conditionOperationComboBox2.getSelectedItem()).endsWith(":")) conditionValueTextField2.setVisible(true);
				
				else conditionValueTextField2.setVisible(false);
				
				
				
				conditionKeyComboBox3.setVisible(false);
				
				conditionOperationComboBox3.setVisible(false);
				
				conditionValueTextField3.setVisible(false);
				
			}
			
			else if (conditionKeyString.equals("3"))
			{
				
				conditionLabel.setText("On all the cards that meet all these conditions :");
				
				
				
				conditionKeyComboBox1.setVisible(true);
				
				conditionOperationComboBox1.setVisible(true);
				
				if (String.valueOf(conditionOperationComboBox1.getSelectedItem()).endsWith(":")) conditionValueTextField1.setVisible(true);
				
				else conditionValueTextField1.setVisible(false);
				
				
				
				conditionKeyComboBox2.setVisible(true);
				
				conditionOperationComboBox2.setVisible(true);
				
				if (String.valueOf(conditionOperationComboBox2.getSelectedItem()).endsWith(":")) conditionValueTextField2.setVisible(true);
				
				else conditionValueTextField2.setVisible(false);
				
				
				
				conditionKeyComboBox3.setVisible(true);
				
				conditionOperationComboBox3.setVisible(true);
				
				if (String.valueOf(conditionOperationComboBox3.getSelectedItem()).endsWith(":")) conditionValueTextField3.setVisible(true);
				
				else conditionValueTextField3.setVisible(false);
				
			}
			
			window.pack();
			
		}
	}
	
	
	
	static class KeyComboBoxListener implements ActionListener
	{
		
		JComboBox<String> keyComboBox;
		
		JComboBox<String> operationComboBox;
		
		JTextField valueTextField;
		
		Map<String, List<String>> options;
		
		public KeyComboBoxListener(JComboBox<String> keyComboBox, JComboBox<String> operationComboBox, JTextField valueTextField, Map<String, List<String>> options)
		{
			
			super();
			
			this.keyComboBox = keyComboBox;
			
			this.operationComboBox = operationComboBox;
			
			this.valueTextField = valueTextField;
			
			this.options = options;
			
		}
		
		@Override
		public void actionPerformed(ActionEvent event)
		{
			
			String keyString = String.valueOf(keyComboBox.getSelectedItem());
			
			if (keyString.equals("Delete the card"))
			{
				
				operationComboBox.setVisible(false);
				
				valueTextField.setVisible(false);
				
			}
			
			else
			{
				
				operationComboBox.setVisible(true);
				
				String operationString = String.valueOf(operationComboBox.getSelectedItem());
				
				List<String> optionsList = options.get(keyString);
				
				GUI.replaceComboBoxContents(operationComboBox, optionsList);
				
				if (optionsList.contains(operationString))
				{
					
					operationComboBox.setSelectedItem(operationString);
					
				}
				
				operationString = String.valueOf(operationComboBox.getSelectedItem());
				
				valueTextField.setVisible(operationString.endsWith(":"));
				
			}
			
			window.pack();
			
		}
	}
	
	
	
	static class OperationComboBoxListener implements ActionListener
	{
		
		JComboBox<String> keyComboBox;
		
		JComboBox<String> operationComboBox;
		
		JTextField valueTextField;
		
		public OperationComboBoxListener(JComboBox<String> keyComboBox, JComboBox<String> operationComboBox, JTextField valueTextField)
		{
			
			super();
			
			this.keyComboBox = keyComboBox;
			
			this.operationComboBox = operationComboBox;
			
			this.valueTextField = valueTextField;
			
		}
		
		@Override
		public void actionPerformed(ActionEvent event)
		{
			
			String operationString = String.valueOf(operationComboBox.getSelectedItem());
			
			valueTextField.setVisible(operationString.endsWith(":"));
			
			window.pack();
			
		}
	}
}
