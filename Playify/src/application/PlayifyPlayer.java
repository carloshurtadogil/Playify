package application;
import javazoom.jl.player.*;
import javazoom.jl.decoder.JavaLayerException;


public class PlayifyPlayer {
	
	private Player musicPlayer;
	private Song current, next, previous;
	
	/**
	 * Retrieve the player
	 * @return The music player found in this class
	 */
	public Player getMusicPlayer() {
		return musicPlayer;
	}
	
	/**
	 * For the transfer of a music player from the previous controller
	 * @param p The player to be transferred
	 */
	public void setMusicPlayer(Player p) {
		musicPlayer = p;
	}
	
	/**
	 * Play the current
	 */
	public void play() throws JavaLayerException {
		if(musicPlayer != null) {
			musicPlayer.play();
		}
	}
	
	/**
	 * Pause the current song
	 */
	public void pause() throws JavaLayerException {
		if(musicPlayer != null) {
			//To be implemented
		}
	}
	
	/**
	 * Stop the current song
	 */
	
	public void stop() throws JavaLayerException {
		if(musicPlayer != null) {
			//To be implemented
		}
	}
	
	/**
	 * Set the song that is to be played
	 * @param s The song to be played
	 */
	public void setCurrent(Song s) {
		current = s;
	}
	
	/**
	 * Retrieve the song that is being played
	 * @return The current song being played
	 */
	public Song getCurrent() {
		return current;
	}
	
	/**
	 * Load the next song to be played
	 * @param s The next song to be played
	 */
	public void setNext(Song s) {
		next = s;
	}
	
	/**
	 * Retrieve the song to be played next
	 * @return The next song in the queue
	 */
	public Song getNext() {
		return next;
	}
	
	/**
	 * Load the previous song that was played
	 * @param s The previous song played
	 */
	public void setPrevious(Song s) {
		next = s;
	}
	
	/**
	 * Retrieve the previous song that was played
	 * @return The previous song from the queue
	 */
	public Song getPrevious() {
		return previous;
	}
}












