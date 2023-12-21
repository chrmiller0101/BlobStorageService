package gov.state.ds.blobservice.utilities;

import java.io.File;
import java.io.IOException;

public class CommandLineEncode {

	public static void main(String[] args) {
		//ffmpeg -i samplevideo.mp4 -single_file 1 -movflags faststart -codec copy samp-optimized.mpd
//		String[] cmd={"C:\\chris\\videos\\ffmpeg","-i", "concat:C:\\journalism\\videos\\vid1.ts|C:\\journalism\\videos\\vid2.ts", "-c", "copy", "C:\\journalism\\videos\\output.mp4"};
//		String[] cmd={"C:\\chris\\videos\\ffmpeg","-i", "C:\\chris\\videos\\test3\\samplevideo.mp4", "-single_file", "1", "-movflags", "faststart", "-codec", "copy", "C:\\chris\\videos\\test3\\samp-o.mpd"};
//		String[] cmd={"C:\\chris\\videos\\ffmpeg","-i", "C:\\chris\\videos\\test3\\samplevideo.mp4", "-movflags", "faststart", "-codec", "copy", "C:\\chris\\videos\\test3\\samp-o.mpd"};
// this one works		String[] cmd={"C:\\chris\\videos\\ffmpeg","-i", "C:\\chris\\videos\\test3\\samplevideo.mp4", "-movflags", "faststart", "-codec", "copy", "samp-o.mpd"};
		String[] cmd={"C:\\chris\\videos\\ffmpeg","-i", "C:\\chris\\videos\\test4\\samplevideo.mp4", "-single_file", "1", "-codec", "copy", "samp-1.m3u8"};

		try {
	//		Runtime.getRuntime().exec(cmd);
			Runtime.getRuntime().exec(cmd, null, new File("C:\\chris\\videos\\test4"));

		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

}
