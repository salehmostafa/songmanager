import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

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

	/**
	 * Create the frame.
	 */
	public QueueController() {
		this.queue = new SongQueue();
		
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
		
		JButton skip = new JButton("Next");
		skip.setBounds(6, 118, 134, 29);
		skip.addActionListener(l -> queue.doAction(Command.PLAY_NEXT_SONG));
		contentPane.add(skip);
		
		JButton pause = new JButton("Pause");
		pause.setBounds(152, 81, 134, 29);
		pause.addActionListener(l -> queue.doAction(Command.PAUSE_SONG));
		contentPane.add(pause);
		
		JButton resume = new JButton("Play");
		resume.setBounds(6, 81, 134, 29);
		resume.addActionListener(l -> queue.doAction(Command.PLAY_SONG));
		contentPane.add(resume);
		
		JButton restart = new JButton("Restart Song");
		restart.setBounds(6, 159, 134, 29);
		restart.addActionListener(l -> queue.doAction(Command.RESTART_SONG));
		contentPane.add(restart);
		
		JButton btnPlayLastSong = new JButton("Back");
		btnPlayLastSong.setBounds(152, 118, 134, 29);
		btnPlayLastSong.addActionListener(l -> queue.doAction(Command.PLAY_LAST_SONG));
		contentPane.add(btnPlayLastSong);
		
		JButton btnStartOver = new JButton("Restart Queue");
		btnStartOver.setBounds(152, 159, 134, 29);
		btnStartOver.addActionListener(l -> queue.doAction(Command.RESTART_QUEUE));
		contentPane.add(btnStartOver);
		
		JButton btnRepeat = new JButton("Repeat Song");
		btnRepeat.setBounds(6, 203, 134, 29);
		btnRepeat.addActionListener(l -> queue.doAction(Command.REPEAT_CURRENT_SONG));
		contentPane.add(btnRepeat);
		
		JButton slower = new JButton("Slower");
		slower.setBounds(6, 244, 134, 29);
		slower.addActionListener(l -> queue.doAction(Command.MAKE_SLOWER));
		contentPane.add(slower);
		
		JButton faster = new JButton("Faster");
		faster.setBounds(152, 244, 134, 29);
		faster.addActionListener(l -> queue.doAction(Command.MAKE_FASTER));
		contentPane.add(faster);
		
		JButton btnVolumeUp = new JButton("Volume Up");
		btnVolumeUp.setBounds(152, 285, 134, 29);
		btnVolumeUp.addActionListener(l -> queue.doAction(Command.LOUDER));
		contentPane.add(btnVolumeUp);
		
		JButton btnVolumeDown = new JButton("Volume Down");
		btnVolumeDown.setBounds(6, 285, 134, 29);
		btnVolumeDown.addActionListener(l -> queue.doAction(Command.QUIETER));
		contentPane.add(btnVolumeDown);
		
		JButton btnShuffle = new JButton("Shuffle");
		btnShuffle.setBounds(152, 203, 134, 29);
		btnShuffle.addActionListener(l -> queue.doAction(Command.SHUFFLE));
		contentPane.add(btnShuffle);
		
		JTextArea txtrAddCurrentSongs = new JTextArea();
		txtrAddCurrentSongs.setText("ADD CURRENT SONGS LIST");
		txtrAddCurrentSongs.setBounds(298, 203, 169, 16);
		contentPane.add(txtrAddCurrentSongs);
		
		JTextArea txtrAddLoadSong = new JTextArea();
		txtrAddLoadSong.setText("ADD LOAD SONG");
		txtrAddLoadSong.setBounds(298, 231, 169, 16);
		contentPane.add(txtrAddLoadSong);
		
		JTextArea txtrAddSaveSong = new JTextArea();
		txtrAddSaveSong.setText("ADD SAVE SONG (NAME)");
		txtrAddSaveSong.setBounds(298, 259, 196, 16);
		contentPane.add(txtrAddSaveSong);
		
		JTextArea txtrAddCurrentSong = new JTextArea();
		txtrAddCurrentSong.setText("ADD CURRENT SONG");
		txtrAddCurrentSong.setBounds(298, 288, 169, 16);
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
		
		setVisible(true);
	}
}