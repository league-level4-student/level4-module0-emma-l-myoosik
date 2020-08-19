package _02_Pixel_Art;

//import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;

public class PixelArtMaker implements MouseListener, ActionListener{
	static final String DATA_FILE = "src/_02_Pixel_Art/imageFile.dat";
	private JFrame window;
	private GridInputPanel gip;
	private GridPanel gp = null;
	ColorSelectionPanel csp;
	private JButton save;
	private JButton loading;
	
	static boolean clickedLoading = false;
	
	public void start() {
		gip = new GridInputPanel(this);	
		window = new JFrame("Pixel Art");
		window.setLayout(new FlowLayout());
		window.setResizable(false);
		save = new JButton("save");
		save.addActionListener(this);
		loading = new JButton("load");
		loading.addActionListener(this);
		
		window.add(gip);
		window.add(loading);
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	public void submitGridData(int w, int h, int r, int c) {
		if (clickedLoading == true) setGp(gp);
		else setGp(new GridPanel(w, h, r, c));
		
		csp = new ColorSelectionPanel();
		window.remove(gip);
		window.remove(loading);
		window.add(getGp());
		window.add(csp);
		window.add(save);
		
		getGp().repaint();
		getGp().addMouseListener(this);
		window.pack();
	}

	
	public static void main(String[] args) {
		new PixelArtMaker().start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		getGp().setColor(csp.getSelectedColor());
		System.out.println(csp.getSelectedColor());
		getGp().clickPixel(e.getX(), e.getY());
		getGp().repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public GridPanel getGp() {
		return gp;
	}

	public void setGp(GridPanel gp) {
		this.gp = gp;
	}
	
	private static void save(GridPanel gp) {
		try (FileOutputStream fos = new FileOutputStream(new File(PixelArtMaker.DATA_FILE)); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(gp);
			System.out.print("done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static GridPanel load() {
		try (FileInputStream fis = new FileInputStream(new File(PixelArtMaker.DATA_FILE)); ObjectInputStream ois = new ObjectInputStream(fis)) {
			return (GridPanel) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			// This can occur if the object we read from the file is not
			// an instance of any recognized class
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.print("action perforemd");
		if (e.getSource() == save) {
			save(getGp());
		}
		else if (e.getSource() == loading) {
			setGp(load());
			clickedLoading = true;
		}
	}
}
