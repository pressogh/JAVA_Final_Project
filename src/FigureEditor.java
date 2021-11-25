import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FigureEditor extends JFrame {
	String selectedBtn = "사각";
	
	public FigureEditor() {
		setTitle("Figure Editor");
		setSize(600, 300);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(new CenterPanel(), BorderLayout.CENTER);
		c.add(new WestPanel(), BorderLayout.WEST);
		
		setVisible(true);
	}
	
	private class CenterPanel extends JPanel {
		public CenterPanel() {
			setLayout(new FlowLayout());
			setBackground(Color.YELLOW);
		}
	}
	
	private class ButtonPanel extends JPanel {
		public ButtonPanel() {
			setLayout(new GridLayout(7, 3, 3, 3));
			setBackground(Color.BLUE);
			
			String menuItem[] = {"사각", "직선", "타원", "복사", "삭제", "저장", "불러오기"};
            for (String item : menuItem) {
            	JButton btn = new JButton(item);
            	btn.addActionListener(new MenuClickListener());
            	add(btn);
            }
		}
		
		class MenuClickListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedBtn = e.getActionCommand();
			}
		}
	}
	
	private class WestPanel extends JPanel {
		public WestPanel() {
			setLayout(new FlowLayout());
			setBackground(Color.LIGHT_GRAY);
			
			add(new ButtonPanel());
		}
	}
	
	public static void main(String[] args) {
		new FigureEditor();
	}
}