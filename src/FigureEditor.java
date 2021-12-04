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
import java.io.*;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FigureEditor extends JFrame {
	String selectedBtn = "";
	Vector<Shape> shapeArray = new Vector<>();
	CenterPanel cp = new CenterPanel();
	
	public FigureEditor() {
		setTitle("Figure Editor");
		setSize(600, 300);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(cp, BorderLayout.CENTER);
		c.add(new WestPanel(), BorderLayout.WEST);
		
		setVisible(true);
	}

	private void unselectAll() {
		for (Shape item : shapeArray) {
			if (item instanceof Rectangle || item instanceof Circle || item instanceof Line) item.selected = false;
		}
	}
	
	private class CenterPanel extends JPanel {
		private Point start, end;
		// 도형 이동 시 마우스를 누른 점을 기준으로 하기 위한 변수
		private int offX = 0, offY = 0;

		public CenterPanel() {
			setLayout(new FlowLayout());
			setBackground(Color.YELLOW);

			addMouseListener(new mouseListener());
			addMouseMotionListener(new mouseListener());
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(start == null) {
				g.setColor(Color.BLUE);
				for (Shape item : shapeArray) {
					if (item instanceof Rectangle) {
						((Rectangle) item).draw(g);
					} else if (item instanceof Circle) {
						((Circle) item).draw(g);
					} else if (item instanceof Line) {
						((Line) item).draw(g);
					}
				}
				return;
			}
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
				// 도형 선택 기능
				Point point = e.getPoint();
				for (Shape item : shapeArray) {
					if (item instanceof Rectangle || item instanceof Circle) {
						if ((point.x >= item.x && point.y >= item.y) && (point.x <= item.x + item.width && point.y <= item.y + item.height)) {
							// 이미 선택된 도형이 있을 시 선택 해제
							unselectAll();
							item.selected = true;
							break;
						}
						else item.selected = false;
					}
					else if (item instanceof Line) {
						if (point.x >= Math.min(item.x, item.width) && point.y >= Math.min(item.y, item.height) && point.x <= Math.max(item.x, item.width) && point.y <= Math.max(item.y, item.height)) {
							// 이미 선택된 도형이 있을 시 선택 해제
							unselectAll();
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

				for (Shape item : shapeArray) {
					if (item instanceof Rectangle || item instanceof Circle || item instanceof Line) {
						if (item.selected) {
							offX = e.getPoint().x - item.x;
							offY = e.getPoint().y - item.y;
							break;
						}
					}
				}
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				end = e.getPoint();
				for (Shape item : shapeArray) {
					if (item instanceof Rectangle || item instanceof Circle || item instanceof Line) {
						if (item.selected) {
							int lastX = item.x, lastY = item.y;
							// 도형 크기 조절 기능
							// 왼쪽 위로 크기 조절
							if ((item.x - 10 <= end.x && item.x + 10 >= end.x) && (item.y - 10 <= end.y && item.y + 10 >= end.y)) {
								if (item.width > 0 && item.height > 0) {
									item.x = e.getPoint().x;
									item.y = e.getPoint().y;
									if (!(item instanceof Line)) {
										item.width = item.width - (item.x - lastX);
										item.height = item.height - (item.y - lastY);
									}
								}
								repaint();
							}
							// 오른쪽 아래로 크기 조절
							else if (!(item instanceof Line) && (item.x + item.width - 10 <= end.x && item.x + item.width + 10 >= end.x) && (item.y + item.height - 10 <= end.y && item.y + item.height + 10 >= end.y)) {
								item.width = item.width + (e.getPoint().x - (item.x + item.width));
								item.height = item.height + (e.getPoint().y - (item.y + item.height));
							}
							else if ((item instanceof Line) && (item.width - 10 <= end.x && item.width + 10 >= end.x) && (item.height - 10 <= end.y && item.height + 10 >= end.y)) {
								item.width = e.getPoint().x;
								item.height = e.getPoint().y;
							}
							// 도형 이동 기능
							else if (!(item instanceof Line) && (e.getPoint().x >= item.x && e.getPoint().y >= item.y) && (e.getPoint().x <= item.x + item.width && e.getPoint().y <= item.y + item.height)) {
								item.x = e.getPoint().x - offX;
								item.y = e.getPoint().y - offY;

								if (item instanceof Line) {
									item.width -= lastX - item.x;
									item.height -= lastY - item.y;
								}
							}
							else if ((item instanceof Line) && (Math.min(item.x, item.width) <= e.getPoint().x && Math.max(item.x, item.width) >= e.getPoint().x) && (Math.min(item.y, item.height) <= e.getPoint().y && Math.max(item.y, item.height) >= e.getPoint().y)) {
								item.x = e.getPoint().x - offX;
								item.y = e.getPoint().y - offY;
								item.width -= lastX - item.x;
								item.height -= lastY - item.y;
							}
							repaint();
							break;
						}
					}
				}
				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				end = e.getPoint();

				// 도형 등록 기능
				if (selectedBtn.equals("사각") || selectedBtn.equals("타원") || selectedBtn.equals("직선")) {
					Shape shp = null;
					if (selectedBtn.equals("사각"))
						shp = new Rectangle(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.abs(start.x - end.x), Math.abs(start.y - end.y));
					else if (selectedBtn.equals("타원"))
						shp = new Circle(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.abs(start.x - end.x), Math.abs(start.y - end.y));
					else if (selectedBtn.equals("직선")) shp = new Line(start.x, start.y, end.x, end.y);
					shapeArray.add(shp);
				}

				// 데이터 초기화
				selectedBtn = "";
				start = null;
				end = null;
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

				if (selectedBtn.equals("복사")) {
					Shape shp = null;
					for (Shape item : shapeArray) {
						if ((item instanceof Rectangle || item instanceof Circle || item instanceof Line) && item.selected) {
							if (item instanceof Rectangle) shp = new Rectangle(item.x + 10, item.y + 10, item.width, item.height);
							else if (item instanceof Circle) shp = new Circle(item.x + 10, item.y + 10, item.width, item.height);
							else if (item instanceof Line) shp = new Line(item.x + 10, item.y + 10, item.width + 10, item.height + 10);
							break;
						}
					}
					shapeArray.add(shp);
					cp.repaint();
				}
				else if (selectedBtn.equals("삭제")) {
					for (int i = 0; i < shapeArray.size(); i++) {
						if ((shapeArray.get(i) instanceof Rectangle || shapeArray.get(i) instanceof Circle || shapeArray.get(i) instanceof Line) && shapeArray.get(i).selected) {
							shapeArray.remove(i);
							break;
						}
					}
					cp.repaint();
				}
				else if (selectedBtn.equals("저장")) {
					try {
						saveObjectToFile("out.dat");
					} catch (IOException ex) {
						System.out.println("IOException");
					}
				}
				else if (selectedBtn.equals("불러오기")) {
					try {
						shapeArray = loadObjectFromFile("out.dat");
					} catch (IOException ex) {
						System.out.println("IOException");
					} catch (ClassNotFoundException ex) {
						System.out.println("ClassNotFoundException");
					}
				}

				// 모든 도형 선택 해제
				unselectAll();
				cp.repaint();
			}
		}

		public void saveObjectToFile(String fileName) throws IOException {
			// ObjectOutputStream을 이용하여 파일에 클래스 저장
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));

			for (Shape item : shapeArray) {
				out.writeObject(item);
			}
			out.close();
		}
		public Vector<Shape> loadObjectFromFile(String fileName) throws IOException, ClassNotFoundException {
			// ObjectInputStream을 이용해 파일에서 클래스를 읽고 res에 저장
			Vector<Shape> res = new Vector<>();
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));

			Shape s;
			try {
				while ((s = (Shape) in.readObject()) != null) {
					res.add(s);
				}
			} catch (EOFException e) {
				in.close();
				return res;
			}
			return res;
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