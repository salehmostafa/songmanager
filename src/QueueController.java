import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JSlider;

public class QueueController extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7593065910949709997L;
	private JPanel contentPane;
	private SongQueue queue;
	private JTextField song;
	
	private DefaultListModel<Song> model;
	private JList<Song> songQueue;
	
	private JSlider songTime;

	/**
	 * Create the frame.
	 */
	public QueueController() {
		this.queue = new SongQueue(this);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		song = new JTextField();
		song.setBounds(6, 6, 280, 28);
		contentPane.add(song);
		song.setColumns(10);
		
		this.model = new DefaultListModel<>();
		this.songQueue = new JList<>(model);

		songQueue.setSize(170, 122);
		songQueue.setLocation(310, 40);
		contentPane.add(songQueue);
		JButton addSong = new JButton("Add Song");
		addSong.setBounds(6, 40, 280, 29);
		addSong.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(new File(song.getText().trim()).exists()) {
					System.out.println("Attempting to add...");
					Song added = new Song(song.getText().trim());

					queue.addSong(added);
					model.addElement(added);
					song.setText("");
				} else {
					System.err.println("Such a file doesn't exist!");
				}
			}
		});
		contentPane.add(addSong);
		
		JButton resume = new JButton("Play/Pause");
		resume.setBounds(6, 81, 134, 29);
		resume.addActionListener(l -> queue.doAction(queue.isPlaying() ? Command.PAUSE_SONG : Command.PLAY_SONG));
		contentPane.add(resume);
		
		JButton btnShuffle = new JButton("Shuffle");
		btnShuffle.setBounds(152, 81, 134, 29);
		btnShuffle.addActionListener(l -> queue.doAction(Command.SHUFFLE));
		contentPane.add(btnShuffle);
		
		JTextArea txtrAddCurrentSong = new JTextArea();
		txtrAddCurrentSong.setText("ADD CURRENT SONG");
		txtrAddCurrentSong.setBounds(311, 215, 169, 16);
		contentPane.add(txtrAddCurrentSong);
		
		JTextArea txtrCurrentSongList = new JTextArea();
		txtrCurrentSongList.setText("Current Song List: ");
		txtrCurrentSongList.setBounds(335, 12, 117, 16);
		contentPane.add(txtrCurrentSongList);
		
		JButton btnQueue = new JButton("Remove Song");
		btnQueue.setBounds(310, 174, 170, 29);
		btnQueue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = songQueue.getSelectedIndex();
				if(index >= 0) {
					queue.removeSong(model.getElementAt(index));
					model.remove(index);
				}
			}
		});
		contentPane.add(btnQueue);
		
		JTextArea txtrVolume = new JTextArea();
		txtrVolume.setText("Volume: ");
		txtrVolume.setBounds(16, 166, 55, 16);
		contentPane.add(txtrVolume);
		
		JSlider volume = new JSlider();
		volume.setBounds(93, 161, 186, 29);
		volume.setMajorTickSpacing(50);
		volume.setMinorTickSpacing(25);
		volume.setPaintTicks(true);
		volume.addChangeListener(l -> queue.setVolume(volume.getValue()));
		contentPane.add(volume);
		
		JTextArea txtrSpeed = new JTextArea();
		txtrSpeed.setText("Speed: ");
		txtrSpeed.setBounds(16, 128, 55, 16);
		contentPane.add(txtrSpeed);
		
		JSlider speed = new JSlider(0, 200);
		speed.setBounds(93, 122, 180, 29);
		speed.setValue(100);
		speed.addChangeListener(l -> queue.setRate(speed.getValue()));
		speed.setMajorTickSpacing(100);
		speed.setMinorTickSpacing(25);
		speed.setSnapToTicks(true);
		speed.setPaintTicks(true);
		contentPane.add(speed);
		
		JTextArea txtrSongTime = new JTextArea();
		txtrSongTime.setText("Song time: ");
		txtrSongTime.setBounds(16, 204, 65, 16);
		contentPane.add(txtrSongTime);
		
		songTime = new JSlider();
		songTime.setBounds(93, 200, 186, 29);
		songTime.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				queue.goToTime(songTime.getValue());
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				queue.goToTime(songTime.getValue());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
		contentPane.add(songTime);
		
		setVisible(true);
		setupComponents(contentPane.getBackground(), this);
	}
	
	public void updateSongTime() {
		songTime.setValue((int) (queue.getCurrentSongTime()));
	}
	
	private static void setupComponents(Color color, Component c) {
		if(c instanceof JTextArea) {
			c.setBackground(color);
			((JTextArea) c).setEditable(false);
			return;
		} else if(c instanceof Container) {
			for(Component cp : ((Container) c).getComponents()) {
				setupComponents(color, cp);
			}
		}
	}
}