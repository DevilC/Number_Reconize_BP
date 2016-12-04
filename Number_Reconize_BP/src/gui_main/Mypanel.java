package gui_main;
import java.awt.*; 
import java.awt.event.*; 
import java.util.Vector; 

import javax.swing.JPanel;

public class Mypanel extends JPanel {
	private Vector<Vector<Point>> FreedomDatas = new Vector<Vector<Point>>(); //freeDom时使用
	private Color lineColor = Color.BLACK; 
    private int lineWidth = 10; 
    
    public Mypanel() 
    { 
        addMouseListener(new MouseAdapter() 
        { 
            public void mousePressed(MouseEvent e) 
            { 
                        Point p = new Point(e.getX(),e.getY()); 
                        Vector<Point> newLine = new Vector<Point>(); 
                        newLine.add(p); 
                        FreedomDatas.add(newLine);  
            }
        
        public void mouseReleased(MouseEvent e) 
        { 
        	repaint();
        }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() 
        { 
            public void mouseDragged(MouseEvent e) 
            {  
                    Point p = new Point(e.getX(),e.getY()); 
                    int n = FreedomDatas.size()-1; //拿到最后一条线的位置 
                    Vector<Point> lastLine = FreedomDatas.get(n); 
                    lastLine.add(p); 
            }
        });
    }
    
    public void cleanAll() 
    { 
        FreedomDatas.clear(); 
        repaint(); 
    } 
    
    public void paint(Graphics g) 
    { 
    	g.clearRect(0, 0, getWidth(), getHeight()); 
        g.setColor(lineColor); 
         
        Graphics2D g_2D = (Graphics2D)g; 
        BasicStroke stroke = new BasicStroke(lineWidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND); 
        g_2D.setStroke(stroke); 
        
        Vector<Point> v; 
        Point s,e; 
        int i,j,m; 
        int n = FreedomDatas.size(); 
        for(i=0;i<n;i++) 
        { 
            v = FreedomDatas.get(i); 
            m = v.size()-1; 
            for(j=0;j<m;j++) 
            { 
                s = (Point)v.get(j); 
                e = (Point)v.get(j+1); 
                g.drawLine(s.x, s.y, e.x, e.y); 
            }
        }
    }
}
