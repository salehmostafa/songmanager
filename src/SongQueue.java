

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class SongQueue implements Runnable {//extends JFrame implements Runnable {
	private MediaPlayer player;
	private Command command;

	private List<Song> songs;
	private int currentSong = 0;
	private double time;
	private double rate = 1.0;
	private double volume = 0.5;
	
	public SongQueue() {
		this.command = Command.DO_NOTHING;
		this.songs = new ArrayList<>();
		//this.setVisible(true);

		//Starts a new thread running.
		new Thread(this).start();
	}
	
	public void goToTime(double time) {
		this.command = Command.MOVE_TO_SONG_LOCATION;
		this.time = time;
	}
	
	public void doAction(Command playNextSong) {
		if(playNextSong != Command.MOVE_TO_SONG_LOCATION) {
			this.command = playNextSong;
			System.out.println("Command: " + playNextSong);
		}
	}
	
	public void setSongs(List<Song> songs) {
		this.songs = songs;
		this.currentSong = 0;
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

	@Override
	public void run() {
		while(command != Command.END_APPLICATION) {
			switch(command) {
			case PLAY_LAST_SONG:
				if(currentSong == 0) {
					currentSong = songs.size() - 1;
				} else {
					currentSong--;
				}
				player.stop();
				playCurrentSong();
				command = Command.DO_NOTHING;
				break;
			case PLAY_NEXT_SONG:
				if(currentSong == songs.size() - 1) {
					currentSong = 0;
				} else {
					currentSong++;
				}
				if(player != null) {
					player.stop();
				}
				playCurrentSong();
				command = Command.DO_NOTHING;
				break;
			case MOVE_TO_SONG_LOCATION:
				player.seek(new Duration(time));
				command = Command.DO_NOTHING;
				break;
			case PAUSE_SONG:
				player.pause();
				command = Command.DO_NOTHING;
				break;
			case RESTART_SONG:
				player.stop();
				player.play();
				command = Command.DO_NOTHING;
				break;
			case PLAY_SONG:
				if(player != null && player.getStatus() == Status.PAUSED) {
					player.play();
				} else if(player == null || player.getStatus() != Status.PLAYING) {
					playCurrentSong();
				}
				command = Command.DO_NOTHING;
				break;
			case REPEAT_CURRENT_SONG:
				player.setOnEndOfMedia(new Runnable() {
					@Override
					public void run() {
						command = Command.RESTART_SONG;
					}
				});
				command = Command.DO_NOTHING;
				break;
			case SHUFFLE:
				Collections.shuffle(getSongs());
				command = Command.DO_NOTHING;
				break;
			case MAKE_SLOWER:
				rate = player.getRate() / 2.0;
				if(rate == 0) {
					rate = 1;
				}
				player.setRate(rate);
				command = Command.DO_NOTHING;
				break;
			case MAKE_FASTER:
				rate = player.getRate() * 2.0;
				if(rate > 8) {
					rate = 8;
				}
				player.setRate(rate);
				command = Command.DO_NOTHING;
				break;
			case LOUDER:
				volume += 0.1;
				if(volume > 1.0) {
					volume = 1.0;
				}
				player.setVolume(volume);
								
				command = Command.DO_NOTHING;
				break;
			case QUIETER:
				volume -= 0.1;
				if(volume < 0) {
					volume = 0;
				}
				player.setVolume(volume);
				command = Command.DO_NOTHING;
				break;
			case RESTART_QUEUE:
				currentSong = 0;
				player.stop();
				playCurrentSong();
				command = Command.DO_NOTHING;
				break;
			default:
				break;
			}
		}
	}
	
	private void playCurrentSong() {
		System.out.println("Playing song: " + songs.get(currentSong));
		Media media = new Media(Paths.get(songs.get(currentSong).getFilePath()).toUri().toString());
		player = new MediaPlayer(media);
		System.out.println(player.getVolume());
		player.setAutoPlay(true);
		player.setVolume(volume);
		player.setRate(rate);
		player.play();
		player.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				command = Command.PLAY_NEXT_SONG;
			}
		});
	}
}