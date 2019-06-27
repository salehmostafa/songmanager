public class Song {
	private String filePath, toString;
	
	public Song(String filePath) {
		this.filePath = filePath;
		this.toString = null;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	@Override
	public String toString() {
		if(toString == null) {
			toString = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
			toString = capitalizeAppropriately(toString);
		}
		return toString;
	}
	
	private static String capitalizeAppropriately(String string) {
		String remainder = string;
		char curr;
		for(int i = 0; i < remainder.length(); i++) {
			curr = remainder.charAt(i);
			if(!Character.isLetter(curr) || Character.isUpperCase(curr)) {
				continue;
			}
			if(i == 0 || remainder.charAt(i - 1) == ' ') {
				remainder = remainder.substring(0, i) + 
					Character.toUpperCase(curr) + remainder.substring(i + 1);
			}
		}
		return remainder;
	}
	
	public static void main(String[] args) {
		System.out.println(new Song("test.mp4").toString());
	}
}
