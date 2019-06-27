import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class SongQueue {
	private MediaPlayer player;

	private List<Song> songs;

	private int currentSong = 0;

	private Duration time;
	private double rate = 1.0;
	private double volume = 0.5;
	
	private final QueueController controller;
	
	public SongQueue(QueueController controller) {
		this.songs = new ArrayList<>();
		this.controller = controller;
	}
	
	public void doAction(Command action) {
		switch (action) {
		case PAUSE_SONG:
			time = player.getCurrentTime();
			player.pause();
			break;
		case PLAY_SONG:
			if(player == null) {
				Media media = new Media(Paths.get(songs.get(currentSong).getFilePath()).toUri().toString());
				player = new MediaPlayer(media);

				player.setStartTime(Duration.ZERO);
				player.setVolume(volume);
				player.setRate(rate);
			}

			player.play();
			
			new Thread() {
				@Override
				public void run() {
					while(player != null && player.getStatus() != Status.PLAYING) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					while(player != null && player.getStatus() == Status.PLAYING) {
						controller.updateSongTime();
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			
			player.setOnEndOfMedia(() -> {
				System.out.println("Going!");
				currentSong++;
				if(currentSong >= songs.size()) {
					currentSong = 0;
				}
				if(songs.size() > 0) {
					doAction(Command.PLAY_SONG);
				}
				time = null;
				player.dispose();
				player = null;
			});
			break;
		case SHUFFLE:
			Collections.shuffle(getSongs());
			break;
		default:
			break;
		}
	}
	
	/**
	 * Returns the current time in milliseconds.
	 */
	public double getCurrentSongTime() {
		System.out.println(100.0 * player.getCurrentTime().toMillis() / getCurrentSongLength());
		return 100.0 * player.getCurrentTime().toMillis() / getCurrentSongLength();
	}
	
	/**
	 * Returns the length of the current song in milliseconds.
	 */
	public double getCurrentSongLength() {
		return player.getStopTime().subtract(player.getStartTime()).toMillis();
	}
	
	public void goToTime(int time) {
		this.time = new Duration(0.01 * time * getCurrentSongLength());
		player.seek(this.time);
	}
	
	public void setVolume(int volume) {
		if(player != null) {
			this.volume = volume * 0.01;
			player.setVolume(this.volume);
		}
	}
	
	public void setRate(int rate) {
		if(player != null) {
			this.rate = rate == 0 ? 0.01 : rate * 0.01;
			player.setRate(this.rate);
		}
	}
	
	public void addSong(Song song) {
		getSongs().add(song);
	}
	
	public void removeSong(Song song) {
		getSongs().remove(song);
	}
	
	public List<Song> getSongs() {
		return songs;
	}
	
	public boolean isPlaying() {
		return player != null && player.getStatus() == Status.PLAYING;
	}
}