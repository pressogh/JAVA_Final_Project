import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FigureEditor extends JFrame {
	String selectedBtn = "";
	Vector<Shape> shapeArray = new Vector<>();
	
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
		Point start, end;
		public CenterPanel() {
			setLayout(new FlowLayout());
			setBackground(Color.YELLOW);

			addMouseListener(new mouseListener());
			addMouseMotionListener(new mouseListener());
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(start == null)
				return;
			g.setColor(Color.BLUE);
			int x = Math.min(start.x, end.x);
			int y = Math.min(start.y, end.y);
			int width = Math.abs(start.x - end.x);
			int height = Math.abs(start.y - end.y);

			// selectedBtn에 따라 다르게 그림
			switch(selectedBtn) {
				case "사각":
					g.drawRect(x, y, width, height);
					break;
				case "타원":
					g.drawOval(x, y, width, height);
					break;
				case "직선":
					g.drawLine(start.x, start.y, end.x, end.y);
					break;
			}

			// 이미 저장된 모양에 따라 화면에 그림을 그림
			for (Shape shape : shapeArray) {
				if (shape instanceof Rectangle) {
					((Rectangle) shape).draw(g);
				} else if (shape instanceof Circle) {
					((Circle) shape).draw(g);
				} else if (shape instanceof Line) {
					((Line) shape).draw(g);
				}
			}
		}
		private class mouseListener extends MouseAdapter {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point point = e.getPoint();
				for (Shape item : shapeArray) {
					if (item instanceof Rectangle || item instanceof Circle) {
						if ((point.x >= item.x && point.y >= item.y) && (point.x <= item.x + item.width && point.y <= item.y + item.height)) {
							for (Shape ele : shapeArray) {
								if (ele instanceof Rectangle || ele instanceof Circle || ele instanceof Line) ele.selected = false;
							}

							item.selected = true;
							break;
						}
						else item.selected = false;
					}
					else if (item instanceof Line) {
						if (point.x >= Math.min(item.x, item.width) && point.y >= Math.min(item.y, item.height) && point.x <= Math.max(item.x, item.width) && point.y <= Math.max(item.y, item.height)) {
							for (Shape ele : shapeArray) {
								if (ele instanceof Rectangle || ele instanceof Circle || ele instanceof Line) ele.selected = false;
							}
							item.selected = true;
							break;
						}
						else item.selected = false;
					}
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				start = e.getPoint();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				end = e.getPoint();
				for (Shape item : shapeArray) {
					if (item instanceof Rectangle || item instanceof Circle || item instanceof Line)
						if (item.selected) {
							int lastX = item.x, lastY = item.y;
							item.x = e.getPoint().x;
							item.y = e.getPoint().y;

							if (item instanceof Line) {
								item.width -= lastX - item.x;
								item.height -= lastY - item.y;
							}
							repaint();
						}
				}
				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				end = e.getPoint();

				Shape shp = null;
				if (selectedBtn.equals("사각")) shp = new Rectangle(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.abs(start.x - end.x), Math.abs(start.y - end.y));
				else if (selectedBtn.equals("타원") )shp = new Circle(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.abs(start.x - end.x), Math.abs(start.y - end.y));
				else if (selectedBtn.equals("직선")) shp = new Line(start.x, start.y, end.x, end.y);
				shapeArray.add(shp);
				selectedBtn = "";
				repaint();
			}
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
				for (Shape item : shapeArray) {
					if (item instanceof Rectangle || item instanceof Circle || item instanceof Line) item.selected = false;
				}
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

class Shape {
	int x, y;
	int width, height;
	boolean selected;

	public Shape(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.selected = false;
	}
}
class Rectangle extends Shape {
	public Rectangle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	public void draw(Graphics g) {
		g.drawRect(x, y, width, height);
		if (selected) {
			g.drawRect(x - 2, y - 2, 4, 4);
			g.drawRect(x + width - 2, y + height - 2, 4, 4);
		}
	}
}
class Circle extends Shape {
	public Circle(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	public void draw(Graphics g) {
		g.drawOval(x, y, width, height);
		if (selected) {
			g.drawRect(x - 2, y - 2, 4, 4);
			g.drawRect(x + width - 2, y + height - 2, 4, 4);
		}
	}
}
class Line extends Shape {
	public Line(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2, y2);
	}
	public void draw(Graphics g) {
		g.drawLine(x, y, width, height);
		if (selected) {
			g.drawRect(x - 2, y - 2, 4, 4);
			g.drawRect(width - 2,height - 2, 4, 4);
		}
	}
}